package elevator;

import java.util.*;

public class RequestQueue {
	//Overview:请求队列类，所有的请求塞在里面
	static Scanner sc = new Scanner(System.in);
	//static String str;//读入一个字符串
	ArrayList<Long> list_t = new ArrayList<Long>();//存储所有合法的时间信息的Arraylist
	ArrayList<Integer> list_f = new ArrayList<Integer>();//存储所有合法的楼层信息的Arraylist
	ArrayList<Integer> list_type = new ArrayList<Integer>();//存储所有合法的类型信息的Arraylist
	ArrayList<Integer> list_updown = new ArrayList<Integer>();//存储所有合法的上下信息的Arraylist
	int floor,i,tot,last_floor,updown,last_updown,type,last_type;
	Long last_t,time;
	//last_t 上一个请求的时间 time：当前时间 其它类似
	boolean flag;//当前读入的这一行是否合法
	int minFloor,maxFloor;//表示读入的楼层允许的最大值和最小值
	
	ArrayList<String> out_iv = new ArrayList<String>();
	
	RequestQueue(int min_f, int max_f){//构造函数，初始化minFloor,maxFloor
		/*@ Requires: None;
		@ Modifies: minFloor, maxFloor;
		@ Effects: minFloor == min_f;
				   maxFloor == max_f;
		*/
		minFloor = min_f;
		maxFloor = max_f;
	}
	public boolean repOK(){
		/*@ REQUIRES: None;
		@ MODIFIES: None;
		@ Effects: \result == invariant(this);
		@ Invariant(this): 恒为true;
		*/
		return true;
	}
//	void work(){//逐行读入，直至出现，调用Request来判断是否是合法的。并判断是不是满足时间的递增的要求
//		/*@ Requires: None;
//		@ Modifies: i, str, System.out;
//		@ Effects: floor == \old(floor)+1;
//				   time == \old(time)+0.5;
//		*/
//		i = 1;
//		while (true){
//			str = sc.nextLine();
//			str = str.replaceAll(" +","");//Delete all spaces.
//			if (str.equals("run")){
//				break;
//			}
//			else{
//				Request req = new Request(str,minFloor,maxFloor);
//				//req.work();
//				rq_add(req);
//			}
//			
//			if (i > 100000){//如果读入的总请求大于100000（即不包括run这一行），则输出“Too Many Requests”并终止程序
//				System.out.println("Too Many Requests");
//				//System.out.println("Invalid input");
//				System.exit(0);
//			}
//			++i;
//		}
//		sc.close();
//	}
	Long[] getArray_time(){//返回所有的合法的输入的时间信息，以数组形式
		/*@ Requires: None;
		@ Modifies: None;
		@ Effects: \result == ArrayList的list_t转成数组
		*/
		Long[] timeArr = new Long[list_t.size()];
		for (int i = 0; i < list_t.size(); ++i){
			timeArr[i] = (Long) list_t.get(i);
		}
		return timeArr;
	}
	int[] getArray_floor(){//返回所有的合法的输入的楼层信息，以数组形式
		/*@ Requires: None;
		@ Modifies: None;
		@ Effects: \result == list_f的list_t转成数组
		*/
		int[] floorArr = new int[list_f.size()];
		for (i = 0; i < list_f.size(); ++i){
			floorArr[i] = (int) list_f.get(i);
		}
		return floorArr;
	}
	int[] getArray_updown(){//返回所有的合法的输入的上还是下信息，以数组形式
		/*@ Requires: None;
		@ Modifies: None;
		@ Effects: \result == list_updown的list_t转成数组
		*/
		int[] updownArr = new int[list_updown.size()];
		for (i = 0; i < list_updown.size(); ++i){
			updownArr[i] = (int) list_updown.get(i);
			//System.out.print(updownArr[i]+" ");
		}
		//System.out.println("");
		return updownArr;
	}
	int[] getArray_type(){//返回所有的合法的输入的类型(FR还是ER)信息，以数组形式
		/*@ Requires: None;
		@ Modifies: None;
		@ Effects: \result == list_type的list_t转成数组;
		*/
		int[] typeArr = new int[list_type.size()];
		for (i = 0; i < list_type.size(); ++i){
			typeArr[i] = (int) list_type.get(i);
		}
		return typeArr;
	}
//	public static void out_invalid(){
//		/*@ Requires: None;
//		@ Modifies: System.out;
//		@ Effects: 输出out_t
//		*/
//		for (String out_t:out_iv){
//			System.out.println(out_t);
//		}
//	}
	
	public int getcnt(){
		/*@ Requires: None;
		@ Modifies: None;
		@ Effects: \result == list_t.size();
		*/
		return list_t.size();
	}
	
	public void rq_add(Request req){
		/*@ Requires: None;
		@ Modifies: tot, list_t, list_type, list_updown, last_t, last_floor, last_updown, last_type, System.out;
		@ Effects: \result == list_f的list_t转成数组
		*/
		String s;
		s = req.getstr();
		
		flag = false;
		if (req.getsign()){
			time = req.getnum2();
			floor = req.getnum1();
			updown = req.getupdown();
			type = req.gettype();
			if ((tot==0&&time==0&&floor==1)||(tot>0&&time>=last_t)){
					flag = true;
					tot++;
					list_f.add(floor);
					list_t.add(time);
					list_type.add(type);
					list_updown.add(updown);
					last_t = time;
					last_floor = floor;
					last_updown = updown;
					last_type = type;
			}
			else{
				//System.out.println("Line"+i+":Wrong input");
				//System.out.println("Line"+i+":Invalid input");
				
				System.out.println("INVALID ["+s+"]");//INVALID [REQUEST]
				//out_iv.add("INVALID ["+str+"]");
			}
		}else{
			//System.out.println("Line"+i+":Invalid input");
			
			System.out.println("INVALID ["+s+"]");//INVALID [REQUEST]
			//out_iv.add("INVALID ["+str+"]");
		}
	}
}
