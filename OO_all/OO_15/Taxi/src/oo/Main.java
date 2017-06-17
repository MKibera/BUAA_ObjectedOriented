package oo;
/**
 * @startuml
 * Alice -> Bob : Hello
 * Bob --> Alice : Hello too
 * @enduml
 */
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
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
	
	private static ArrayList<PrintStream> p2 = new ArrayList<PrintStream>();
	private static PrintStream p2_temp;
	
	public static int wt;
	final static int M = 100;
	final static int M1 = 30;//普通出租车
	final static int M2 = 70;//追踪出租车
	public static TaxiGUI gui;
	public static Light l;
	private static Car c[];
	public boolean repOK(){
		/*@ REQUIRES: None;
		@ MODIFIES: None;
		@ Effects: \result == invariant(this);
		@ Invariant(this): None;
		*/
		return true;
	}
	public static void main(String[] args){
		/*@ Requires: String类型的地图路径;
		@ Modifies: System.out;
		@ Effects:  Normal behavior
					读入地图，读入线程不断读入请求，调度器线程执行，分配给100个出租车的线程执行
					过程中出现任何异常操作 ==> exceptional_behavior(e);
		*/
		try{
			
			File file = new File("map.txt");//可以修改地图文件
			
			File file2= new File("light.txt");
			
			gui = new TaxiGUI();
			mapInfo mi=new mapInfo();
			mi.readmap(file.getAbsolutePath());//mi.readmap("map.txt");//在这里设置地图文件路径
			gui.LoadMap(mi.map, 80);
			
			Map mp = new Map();
			mp.work(file.getAbsolutePath());
			mp.work2(file2.getAbsolutePath());
			
			System.out.println("Tot Traffic Light: "+mp.tot);
			
			Random rand = new Random();
			wt = 200 + rand.nextInt(301);//[200,500]产生随机数
			//wt=100;
			System.out.printf("Light Wait Time %d\n",wt);
			l = new Light();
			
			l.setUncaughtExceptionHandler(new MyException());
			l.start();
			
			RequestQueue rq = new RequestQueue();
			RequestAdd ra = new RequestAdd(rq);
			
			String f_n2;
			
			c = new Car[105];
			
			for (int i = 1; i <= M1; ++i){//追踪出租车
				 c[i] = new Car2(rq, i, c, 1);
				 f_n2 = "track"+i+".txt";
				 FileOutputStream fs2 = new FileOutputStream(new File(f_n2));
				 p2_temp = new PrintStream(fs2);
				 p2.add(p2_temp);
			}
			
			for (int i = M1+1; i <= M1+M2; ++i){//普通出租车
				 c[i] = new Car(rq, i, c, 0);
			}
			
			ra.setUncaughtExceptionHandler(new MyException());
			ra.start();//startTime = ra.getStartTime();
			
			Dispatch d = new Dispatch(rq, c);
			//指向同一块内存区域rq进行读写操作，一方面RequestAdd在不断地读入指令，另一方面不断接受出租车的投简历进行分配
			d.setUncaughtExceptionHandler(new MyException());
			d.start();
			for (int i = 1; i <= M; ++i){
				 //c[i].setUncaughtExceptionHandler(new MyException());
				 //new Thread(c[i]).start();
			}
		}
		catch (Exception e){
			//System.exit(0);
		}
	}
	Car[] init_taxi(){
		/*@ REQUIRES: None;
		@ MODIFIES: None;
		@ EFFECTS: \result == c;
		*/
		return c;
	}
	public synchronized static PrintStream LogTaxiTrack(int i){
		/*@ REQUIRES: (p.get(i) exists);
		@ MODIFIES: None;
		@ EFFECTS: \result == p.get(i);
		@ THREAD_REQUIRES: \locked(this);
		@ THREAD_EFFECTS: \locked();
		*/
		return p2.get(i-1);
	}
}
