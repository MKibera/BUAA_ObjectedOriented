package elevator;

import java.util.Scanner;
import java.util.Date;

public class RequestAdd extends Thread {
	static Scanner sc = new Scanner(System.in);
	private String str_all, str;
	private int flo, upd, typ, id, tot, num_valid;
	private RequestQueue rq;
	private static long tim_t, tim, launch;
	public RequestAdd(RequestQueue rq_t){
		rq = rq_t;
	}
	public void run(){
		tot = 1;
		tim_t = 0;
		
		while (true){
			num_valid = 0;
			str_all = sc.nextLine();
			str_all = str_all.replaceAll(" +","");//Delete all spaces.
			//if (str_all != null)
			//{
				if (tim_t ==0)
					tim_t = new Date().getTime();//求出第一条指令的系统时间，作为基准时间
			//}
			if (str_all.equals("run")){
				System.exit(0);
			}else{
				String[] st_a = str_all.split(";",-1);//用;隔开成许多小的指令，然后不加-1会产生分割；；成null的情况
				for (int i = 0 ; i < st_a.length ; ++i) {
					str = st_a[i];
					Request req = new Request(str,1,20);
					req.work();
					if (num_valid < 10 && req.getsign()){
						flo = req.getnum1();
						upd = req.getupdown();
						typ = req.gettype();
						id = req.getnum2();
						tim = new Date().getTime();
						rq.add_element(flo, id, typ, upd, tim);
						//System.out.println("OK ["+str+"]");//INVALID [REQUEST]
						num_valid++;
					}else{
						if (Main.IsConsole())
							System.out.println((new Date().getTime())+":INVALID ["+str+"]");//INVALID [REQUEST]
						Main.WriteFile().println((new Date().getTime())+":INVALID ["+str+"]");
					}
				}
			}
			
			/*if (tot > 100000){//如果读入的总请求大于100000（即不包括run这一行），则输出“Too Many Requests”并终止程序
				if (Main.IsConsole())
					System.out.println("Too Many Requests");
				Main.WriteFile().println("Too Many Requests");
				System.exit(0);
			}*/
			++tot;
		}
	}
	public static long getStartTime(){
		return tim_t;
	}
	public static void setLaunchTime(){
		launch = tim;
	}
	public static long getLaunchTime(){
		long tt;
		tt = new Date().getTime();
		//if (tim_t==0)
		//	return 0;
		//if (launch-tim_t < 0)
		//	return 0;
		return (long)(tt-tim_t);
	}
}
