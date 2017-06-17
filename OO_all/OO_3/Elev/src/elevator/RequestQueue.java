package elevator;

import java.util.*;

public class RequestQueue {
	static Scanner sc = new Scanner(System.in);
	static String str;//读入一个字符串
	static ArrayList<Long> list_t = new ArrayList<Long>();//存储所有合法的时间信息的Arraylist
	static ArrayList<Integer> list_f = new ArrayList<Integer>();//存储所有合法的楼层信息的Arraylist
	static ArrayList<Integer> list_type = new ArrayList<Integer>();//存储所有合法的类型信息的Arraylist
	static ArrayList<Integer> list_updown = new ArrayList<Integer>();//存储所有合法的上下信息的Arraylist
	static int floor,i,tot,last_floor,updown,last_updown,type,last_type;
	static Long last_t,time;
	//last_t 上一个请求的时间 time：当前时间 其它类似
	static boolean flag;//当前读入的这一行是否合法
	static int minFloor,maxFloor;//表示读入的楼层允许的最大值和最小值
	
	static ArrayList<String> out_iv = new ArrayList<String>();
	
	RequestQueue(int min_f, int max_f){//构造函数，初始化minFloor,maxFloor
		minFloor = min_f;
		maxFloor = max_f;
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
				Request req = new Request(str,minFloor,maxFloor);
				req.work();
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
						
						System.out.println("INVALID ["+str+"]");//INVALID [REQUEST]
						//out_iv.add("INVALID ["+str+"]");
					}
				}else{
					//System.out.println("Line"+i+":Invalid input");
					
					System.out.println("INVALID ["+str+"]");//INVALID [REQUEST]
					//out_iv.add("INVALID ["+str+"]");
				}
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
	Long[] getArray_time(){//返回所有的合法的输入的时间信息，以数组形式
		Long[] timeArr = new Long[list_t.size()];
		for (i = 0; i < list_t.size(); ++i){
			timeArr[i] = (Long) list_t.get(i);
		}
		return timeArr;
	}
	int[] getArray_floor(){//返回所有的合法的输入的楼层信息，以数组形式
		int[] floorArr = new int[list_f.size()];
		for (i = 0; i < list_f.size(); ++i){
			floorArr[i] = (int) list_f.get(i);
		}
		return floorArr;
	}
	int[] getArray_updown(){//返回所有的合法的输入的上还是下信息，以数组形式
		int[] updownArr = new int[list_updown.size()];
		for (i = 0; i < list_updown.size(); ++i){
			updownArr[i] = (int) list_updown.get(i);
			//System.out.print(updownArr[i]+" ");
		}
		//System.out.println("");
		return updownArr;
	}
	int[] getArray_type(){//返回所有的合法的输入的类型(FR还是ER)信息，以数组形式
		int[] typeArr = new int[list_type.size()];
		for (i = 0; i < list_type.size(); ++i){
			typeArr[i] = (int) list_type.get(i);
		}
		return typeArr;
	}
	public static void out_invalid(){
		for (String out_t:out_iv){
			System.out.println(out_t);
		}
	}
}
