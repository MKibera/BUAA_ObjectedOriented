package oo;

import java.io.File;
import java.io.FileOutputStream;
import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import oo.TaxiGUI;
import oo.gv;
import oo.mapInfo;

class mapInfo{
	int[][] map=new int[80][80];
	public void readmap(String path){//读入地图信息
		//Requires:String类型的地图路径,System.in
		//Modifies:System.out,map[][]
		//Effects:从文件中读入地图信息，储存在map[][]中
		Scanner scan=null;
		File file=new File(path);
		if(file.exists()==false){
			System.out.println("地图文件不存在,程序退出");
			System.exit(1);
			return;
		}
		try {
			scan = new Scanner(new File(path));
		} catch (FileNotFoundException e) {
			
		}
		for(int i=0;i<80;i++){
			String[] strArray = null;
			try{
				strArray=scan.nextLine().split("");
			}catch(Exception e){
				System.out.println("地图文件信息有误，程序退出");
				System.exit(1);
			}
			for(int j=0;j<80;j++){
				try{
					this.map[i][j]=Integer.parseInt(strArray[j]);
				}catch(Exception e){
					System.out.println("地图文件信息有误，程序退出");
					System.exit(1);
				}
			}
		}
		scan.close();
	}
}

public class Main {
	final static int M = 100;
	public static TaxiGUI gui;
	public static void main(String[] args){
		/*@ Requires: String类型的地图路径;
		@ Modifies: None;
		@ Effects:  Normal behavior
					读入地图，读入线程不断读入请求，调度器线程执行，分配给100个出租车的线程执行
					过程中出现任何异常操作 ==> exceptional_behavior(e)
		*/
		try{
			
			File file = new File("map.txt");//可以修改地图文件
			
			gui = new TaxiGUI();
			mapInfo mi=new mapInfo();
			mi.readmap(file.getAbsolutePath());//mi.readmap("map.txt");//在这里设置地图文件路径
			gui.LoadMap(mi.map, 80);
			
			Map mp = new Map();
			mp.work(file.getAbsolutePath());
			Car c[] = new Car[105];
			for (int i = 1; i <= M; ++i){
				c[i] = new Car(i, c);
			}
			
			RequestQueue rq = new RequestQueue();
			RequestAdd ra = new RequestAdd(rq);
			
			ra.setUncaughtExceptionHandler(new MyException());
			ra.start();//startTime = ra.getStartTime();
			
			Dispatch d = new Dispatch(rq, c);
			//指向同一块内存区域rq进行读写操作，一方面RequestAdd在不断地读入指令，另一方面不断接受出租车的投简历进行分配
			d.setUncaughtExceptionHandler(new MyException());
			d.start();
			for (int i = 1; i <= M; ++i){
				c[i].setUncaughtExceptionHandler(new MyException());
				c[i].start();
			}
		}
		catch (Exception e){
			//System.exit(0);
		}
	}
}
