package elevator;

import java.util.*;

public class RequestAdd {
	static Scanner sc = new Scanner(System.in);
	static int i;
	static String str;//读入一个字符串
	
	static ArrayList<String> out_iv = new ArrayList<String>();
	
	RequestQueue rq;
	
	RequestAdd(RequestQueue rq_t){//构造函数
		rq = rq_t;
	}
	void work(){//逐行读入，直至出现，调用Request来判断是否是合法的。并判断是不是满足时间的递增的要求
		i = 1;
		while (true){
			str = sc.nextLine();
			str = str.replaceAll(" +","");//Delete all spaces.
			if (str.equals("run")){
				break;
			}
			else{
				Request req = new Request(str,1,10);
				//req.work();
				rq.rq_add(req);
			}
			
			if (i > 100000){//如果读入的总请求大于100000（即不包括run这一行），则输出“Too Many Requests”并终止程序
				System.out.println("Too Many Requests");
				//System.out.println("Invalid input");
				System.exit(0);
			}
			++i;
		}
		sc.close();
	}
	
	public static void out_invalid(){
		for (String out_t:out_iv){
			System.out.println(out_t);
		}
	}
}
