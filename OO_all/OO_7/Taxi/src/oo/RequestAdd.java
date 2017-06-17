package oo;

import java.util.Scanner;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Date;

public class RequestAdd extends Thread {
	static Scanner sc = new Scanner(System.in);
	private String str;
	private int num1, num2, num3, num4, tot;
	private static long tim;
	private RequestQueue rq;
	private boolean same_sign;
	public RequestAdd(RequestQueue rq_t){
		tot = 1;
		rq = rq_t;
	}
	public void run(){
		
		while (true){//[CR,src,dst]
			str = sc.nextLine();
			str = str.replaceAll(" +","");//Delete all spaces.
			
			Request req = new Request(str);
			req.work();
			if (req.getsign()){
				num1 = req.getnum1();
				num2 = req.getnum2();
				num3 = req.getnum3();
				num4 = req.getnum4();
				
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
					if (num1 == rq.getsrcx(i) && num2 == rq.getsrcy(i) && num3 == rq.getdstx(i) 
							&& num4 == rq.getdsty(i) && tim == rq.gettime(i)){
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
			}else{
				System.out.println("Invalid Customer Request");
			}
		}
	}
}
