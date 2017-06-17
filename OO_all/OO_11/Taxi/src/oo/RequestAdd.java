package oo;

import java.util.Scanner;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Date;

public class RequestAdd extends Thread {
	final static int N = 80;
	static Scanner sc = new Scanner(System.in);
	private String str;
	private int num1, num2, num3, num4, tot, plot1, plot2, guix1, guiy1, guix2, guiy2;
	private static long tim;
	private RequestQueue rq;
	private boolean same_sign;
	public RequestAdd(RequestQueue rq_t){
		/*@ REQUIRES: None;
		@ MODIFIES: tot, rq;
		@ EFFECTS: tot == 1;
				   rq == rq_t;
		*/
		tot = 1;
		rq = rq_t;
	}
	public boolean repOK(){
		/*@ REQUIRES: None;
		@ MODIFIES: None;
		@ Effects: \result == invariant(this);
		@ Invariant(this): rq != null;
		*/
		if (rq==null)
			return false;
		return true;
	}
	public void run(){
		/* @ REQUIRES: System.in contains a line of text
	    @ MODIFIES: System.out, num1, num2, num3, num4, tot, num1, num2, str, tim, rq, same_sign;
	    @ EFFECTS: 读入一行，并用Request判断其是否合法，并输出相应信息。合法的话把该请求加入到请求队列里。
	    */

		while (true){//[CR,src,dst]
			str = sc.nextLine();
			str = str.replaceAll(" +","");//Delete all spaces.
			
			Request req = new Request(str);
			req.work();
			
			if (!req.getsign()){
				System.out.println("Invalid Request");
				continue;
			}
			num1 = req.getnum1();
			num2 = req.getnum2();
			num3 = req.getnum3();
			num4 = req.getnum4();
			
			
			
			plot1 = job(num1,num2);
			plot2 = job(num3,num4);
			//System.out.println("#$%"+num1+" "+num2);
			if (req.gettype() == 2){//OPEN
				if (num3+1 == num1 && num4 == num2){
					int temp = num3;
					num3 = num1;
					num1 = temp;
				}
				if (num3 == num1 && num4+1 == num2){
					int temp = num4;
					num4 = num2;
					num2 = temp;
				}
				if (num3 == num1+1 && num4 == num2){
					if (Map.getg()[0][num1][num2] == 0){
						System.out.println("OPEN OK");
						Map.change(num1, num2, 2);
						Main.gui.SetRoadStatus(new Point(num1-1, num2-1), new Point(num3-1, num4-1), 1);// status 0关闭 1打开
					}else if (Map.getg()[0][num1][num2] == 1){
						System.out.println("OPEN OK");
						Map.change(num1, num2, 3);
						Main.gui.SetRoadStatus(new Point(num1-1,num2-1), new Point(num3-1, num4-1), 1);// status 0关闭 1打开
					}else{
						System.out.println("Invalid OPEN: Already Exists!");
					}
				}else if (num4 == num2+1 && num3 == num1){
					if (Map.getg()[0][num1][num2] == 0){
						System.out.println("OPEN OK");
						Map.change(num1, num2, 1);
						Main.gui.SetRoadStatus(new Point(num1-1,num2-1), new Point(num3-1, num4-1), 1);// status 0关闭 1打开
					}else if (Map.getg()[0][num1][num2] == 2){
						System.out.println("OPEN OK");
						Map.change(num1, num2, 3);
						Main.gui.SetRoadStatus(new Point(num1-1,num2-1), new Point(num3-1, num4-1), 1);// status 0关闭 1打开
					}else{
						System.out.println("Invalid OPEN: Already Exists!");
					}
				}else{
					System.out.println("Invalid OPEN");
				}
			}
			
			if (req.gettype() == 1){
				
				if (num3+1 == num1 && num4 == num2){
					int temp = num3;
					num3 = num1;
					num1 = temp;
				}
				if (num3 == num1 && num4+1 == num2){
					int temp = num4;
					num4 = num2;
					num2 = temp;
				}
				
				if (num3 == num1+1 && num4 == num2){
					if (Map.getg()[0][num1][num2] == 2){
						System.out.println("CLOSE OK");
						Map.change(num1, num2, 0);
						Main.gui.SetRoadStatus(new Point(num1-1,num2-1), new Point(num3-1, num4-1), 0);// status 0关闭 1打开
					}else if (Map.getg()[0][num1][num2] == 3){
						System.out.println("CLOSE OK");
						Map.change(num1, num2, 1);
						Main.gui.SetRoadStatus(new Point(num1-1,num2-1), new Point(num3-1, num4-1), 0);// status 0关闭 1打开
					}else{
						System.out.println("Invalid CLOSE: Not Exists!");
					}
				}else if (num4 == num2+1 && num3 == num1){
					if (Map.getg()[0][num1][num2] == 1){
						System.out.println("CLOSE OK");
						Map.change(num1, num2, 0);
						Main.gui.SetRoadStatus(new Point(num1-1,num2-1), new Point(num3-1, num4-1), 0);// status 0关闭 1打开
					
					}else if (Map.getg()[0][num1][num2] == 3){
						System.out.println("CLOSE OK");
						Map.change(num1, num2, 2);
						Main.gui.SetRoadStatus(new Point(num1-1,num2-1), new Point(num3-1, num4-1), 0);// status 0关闭 1打开
					}else{
						System.out.println("Invalid CLOSE: Not Exists!");
					}
				}else{
					System.out.println("Invalid CLOSE");
				}
			}
			
			if (req.gettype() == 0){
				tim = new Date().getTime();
				//System.out.println("time from"+tim);
			tim = tim / 100;
			tim = tim * 100;
				//System.out.println("time to"+tim);
				//time.add(tim);//求出第一条指令的系统时间，作为基准时间
				//src.add(num1);
				//dst.add(num2);
				same_sign = false;
				for (int i = 0; i < rq.gettot(); ++i){
					if (num1 == rq.getsrcx(i) && num2 == rq.getsrcy(i) && num3 == rq.getdstx(i) && num4 == rq.getdsty(i) 
							&& tim == rq.gettime(i)){
						//&& myabs(tim-rq.gettime(i)) <= 100 ){
						same_sign = true;
						break;
					}
				}
				if (!same_sign){
					rq.add_element(tim,num1,num2,num3,num4,false);
					System.out.println("Customer Request OK");
					tot++;
				}else{
					System.out.println("Same Customer Request");
				}
			}
		}
	}
	public int job(int x,int y){
		/*@ REQUIRES: None;
		@ MODIFIES: None;
		@ EFFECTS: \result == 80*(x-1) + y;
		*/
		return 80*(x-1) + y;
	}
	public long myabs(long l){
		/*@ REQUIRES: None;
		@ MODIFIES: None;
		@ EFFECTS: \result == l的绝对值
		*/
		if (l < 0)
			return -1*l;
		return l;
	}
	public void calcxy(int pos1, int pos2){
		guix1 = (pos1-1) / N+1;
		guiy1 = pos1 % N;
		if (guiy1 == 0)
			guiy1 = N;
		guix2 = (pos2-1) / N+1;
		guiy2 = pos2 % N;
		if (guiy2 == 0)
			guiy2 = N;
	}
}
