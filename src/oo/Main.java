package oo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class Main {
	private static PrintStream summ,deta,reco,ss;
	private static Task tas;
	public static void main(String[] args) {
		try{
			//TestThread test0 = new TestThread();//在读入开始前的测试线程
			//test0.start();
			
			tas = new Task();
			FileOutputStream fs1 = new FileOutputStream(new File("summary.txt"));
		 	summ = new PrintStream(fs1);//p.println(10);
		 	FileOutputStream fs2 = new FileOutputStream(new File("detail.txt"));
		 	deta = new PrintStream(fs2);//p.println(10);
		 	
			Request rq =  new Request();
			rq.work();
			Trigger[] tr = new Trigger[rq.getTot()]; 
			int summary_cnt = 0;
			for (int i = 0; i < rq.getTot(); ++i){
				if (rq.getType2(i) == 1)//如果是record-summary
					summary_cnt++;
				tr[i] = new Trigger(summary_cnt, rq.getR(i), rq.getType1(i), rq.getType2(i));
			}
			for (int i = 0; i < rq.getTot(); ++i){
				tr[i].setUncaughtExceptionHandler(new MyException());
				tr[i].start();//new Thread(tr[i]).start();
			}
			
			//TestThread test1 = new TestThread();//在读入结束后的测试线程！
			//test1.start();
		}
		catch (Exception e){
			//System.exit(0);
		}
	}
	
	public static PrintStream SummaryFile(){
		return summ;
	}

	public static PrintStream DetailFile(){
		return deta;
	}
	
	public static Task getTask(){
		return tas;
	}
}
