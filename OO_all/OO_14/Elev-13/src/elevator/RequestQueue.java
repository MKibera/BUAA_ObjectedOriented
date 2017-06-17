package elevator;

import java.util.*;

public class RequestQueue {
	//Overview:请求队列类，将从RequestAdd里所有读入的请求的信息塞到里面
	/*
	 * 表示对象：ArrayList<Long> list_t,ArrayList<Integer> list_f,
	 * 		   ArrayList<Integer> list_type,ArrayList<Integer> list_updown;
	 * 抽象函数：AF(c)=(list_t_,list_f_,list_updown_,list_type_)where
	 * c.list_t = list_t_, c.list_f = list_f_, c.list_updown = list_updown_, c.list_type = list_type_
	 * 不变式：list_t!=null&&list_f!=null&&list_type!=null&&list_updown!=null;
	 */
	
	int floor,i,tot,last_floor,updown,last_updown,type,last_type;
	Long last_t,time;
	//last_t 上一个请求的时间 time：当前时间 其它类似
	boolean flag;//当前读入的这一行是否合法
	int minFloor,maxFloor;//表示读入的楼层允许的最大值和最小值
	
	ArrayList<String> out_iv = new ArrayList<String>();
	ArrayList<Long> list_t;
	ArrayList<Integer> list_f;
	ArrayList<Integer> list_type;
	ArrayList<Integer> list_updown;
	
	RequestQueue(int min_f, int max_f){//构造函数，初始化minFloor,maxFloor
		/*@ Requires: None;
		@ Modifies: minFloor, maxFloor;
		@ Effects: minFloor == min_f;
				   maxFloor == max_f;
		*/
		list_t = new ArrayList<Long>();//存储所有合法的时间信息的Arraylist
		list_f = new ArrayList<Integer>();//存储所有合法的楼层信息的Arraylist
		list_type = new ArrayList<Integer>();//存储所有合法的类型信息的Arraylist
		list_updown = new ArrayList<Integer>();//存储所有合法的上下信息的Arraylist
		
		minFloor = min_f;
		maxFloor = max_f;
	}
	public boolean repOK(){
		/*@ REQUIRES: None;
		@ MODIFIES: None;
		@ Effects: \result == invariant(this);
		@ Invariant(this): list_t!=null&&list_f!=null&&list_type!=null&&list_updown!=null;
		*/
		return list_t!=null&&list_f!=null&&list_type!=null&&list_updown!=null;
	}
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
		@ Effects: \result == ArrayList的list_f转成数组
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
		@ Effects: \result == ArrayList的list_updown转成数组
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
		@ Effects: \result == ArrayList的list_type转成数组;
		*/
		int[] typeArr = new int[list_type.size()];
		for (i = 0; i < list_type.size(); ++i){
			typeArr[i] = (int) list_type.get(i);
		}
		return typeArr;
	}
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
		@ Effects:  list_i == (\old)list_.add(req.i), i = f,t,type,updown
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
