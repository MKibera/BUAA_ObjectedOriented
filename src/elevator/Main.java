package elevator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Date;

public class Main {
	
	final static int MINFLOOR = 1;
	final static int MAXFLOOR = 20;
	final static int MAXN = 4;
	static int i;
	private static PrintStream p;
	public static void main(String[] args) {
		try{
			//System.out.println("K");
		 	FileOutputStream fs = new FileOutputStream(new File("result.txt"));
		 	p = new PrintStream(fs);//p.println(10);
		    long startTime = new Date().getTime();//得到1970-1-1 0:0:0到现在的毫秒数
		  
			Floor floor = new Floor(MINFLOOR,MAXFLOOR);//构造一个楼层类的对象，并把允许的最大值传入一个调度类的对象。
			RequestQueue rq = new RequestQueue(floor.getMin(),floor.getMax());
			RequestQueue rq_elv[] = new RequestQueue[MAXN];
			RequestAdd ra = new RequestAdd(rq);
			
			ra.setUncaughtExceptionHandler(new MyException());
			ra.start();//startTime = ra.getStartTime();
	
			ElevR eleR[] = new ElevR[MAXN];
			Elevator elev[] = new Elevator[MAXN];
			
			for (i = 1; i < MAXN; ++i){
				rq_elv[i] = new RequestQueue(floor.getMin(),floor.getMax());
				elev[i] = new Elevator(i,startTime);
				eleR[i] = new ElevR(i,elev[i],rq_elv[i],startTime);
			}
			ElvDispatch ed = new ElvDispatch(rq, rq_elv, elev);
	
			ed.setUncaughtExceptionHandler(new MyException());
			ed.start();
			
			for (i = 1; i < MAXN; ++i){
				eleR[i].setUncaughtExceptionHandler(new MyException());
				eleR[i].start();
			}
		}
		catch (Exception e){
			System.exit(0);
		}
	}
	
	public static PrintStream WriteFile(){
		return p;
	}
	
	public static boolean IsConsole(){
		//是否允许输出到控制台，true为允许，即会输出到控制台；false为不允许
		return false;//true;
	}
}
