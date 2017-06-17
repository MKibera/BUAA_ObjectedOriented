package elevator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

class Disp {
	//Overview:调度器，傻瓜策略
	final static String[] s = {"UP","DOWN","STILL"};
	Long[] time;//保存所有合法的时间的数组
	int[] floor;//保存所有合法的楼层的数组
	int[] type;//保存所有合法的类型的数组
	int[] updown;//保存所有合法的上下的数组
	int n, i, j, k;//n:所有合法输入的个数
	int[] used;
	int now_f;//当前的行数
	double now_t;//当前的时间
	Queue<Integer> q_f = new LinkedList<Integer>();//下标的队列
	double[] t_end;
	
	public boolean repOK(){
		/*@ REQUIRES: None;
		@ MODIFIES: None;
		@ Effects: \result == invariant(this);
		@ Invariant(this): 恒为true
		*/
		return true;
	}
	
	public Disp(Long[] t, int[] f,int[] typ,int[] upd) {//构造函数
		/*@ Requires: None;
		@ Modifies: time, floor, type, updown;
		@ Effects: time == t;
				   floor == f;
				   type == typ;
				   updown == upd;
		*/
		time = t;
		floor = f;
		type = typ;
		updown = upd;
	}
	
	void work(){
		/*@ Requires: None;
		@ Modifies: n, i, j, k, now_f, now_t, q_f, t_end;
		@ Effects: 采用Fool Schuedule来求出电梯的执行结果
		*/
		n = time.length;//所有合法输入的个数
		double[] t_end = new double[n];//已经算出来的到达时间
		now_t = 0;
		now_f = 1;
		for (i = 0; i < n; ++i){
			boolean flag;
			flag = false;
			for (j = 0; j < i; ++j){
				if (time[i]==time[j]&&type[i]==type[j]
					&&floor[i]==floor[j]&&updown[i]==updown[j]){
				//如果该条指令和之前的某一条完全一样，则忽略
					//System.out.println("Line"+i+":Ignored.Same as the previous");//last one");
					flag = true;
					break;
				}
			}
			if (flag){
				continue;
			}
			double t_q;
			for (Integer i_q:q_f){
				t_q = t_end[i_q];
				if (t_q >= time[i] && floor[i_q]==floor[i] && type[i_q]==type[i] 
						&& updown[i_q]==updown[i]){
					flag = true;
					break;
				}
			}
			if (flag){
				//System.out.println("Line"+i+":Ignored");
				continue;
			}
			while (q_f.peek() != null){
				int i2_q = q_f.peek();
				t_q = t_end[i2_q];
				if (t_q < time[i]){
					q_f.poll();
				}else{
					break;
				}
			}
			
			if (now_t>=time[i]){//如果当前请求的时间小于等于上一个电梯的到达时间
				now_t += 0.5*myabs(now_f-floor[i]);//就按照上一个电椅的到达时间计算
			}else{
				now_t = 1.0*time[i]+0.5*myabs(now_f-floor[i]);//否则就要等到这条指令的发出时间再启动
			}
			String out;
			if (floor[i] > now_f){
				out = s[0];//输出UP
			}else if (floor[i] < now_f){
				out = s[1];//输出DOWN
			}else{
				out = s[2];//输出STILL
			}
			
			if (floor[i] == now_f){
//				System.out.print("("+floor[i]+","+out+",");
//				System.out.printf("%.1f",now_t+1.0);//保留一位小数
//				System.out.println(")");
			}else{
//				System.out.print("("+floor[i]+","+out+",");
//				System.out.printf("%.1f",now_t);//保留一位小数
//				System.out.println(")");
			}
			now_f = floor[i];
			now_t++;
			
			q_f.offer(i);
			t_end[i] = now_t;
		}
	}
	int myabs(int t){//求一个数的绝对值
		/*@ Requires: None;
		@ Modifies: None;
		@ Effects: \result == t的绝对值
		*/
		return (t>=0)?t:-t;
	}
}

public class Dispatch extends Disp {
	//Overview:调度器，ALS策略
	public Dispatch(Long[] t, int[] f, int[] typ, int[] upd) {
		/*@ Requires: None;
		@ Modifies: None;
		@ Effects: 构造函数的初始化
		*/
		super(t, f, typ, upd);
	}
	public boolean repOK(){
		/*@ REQUIRES: None;
		@ MODIFIES: None;
		@ Effects: \result == invariant(this);
		@ Invariant(this): 恒为true
		*/
		return true;
	}
	Piggyback pb;
	Queue<Piggyback> q = new PriorityQueue<Piggyback>(pb.getCmp());
	Elevator elv;
	int now_dir;//
	int main_req;
	ArrayList<String> out_ess = new ArrayList<String>();
	
	void work(){
		/*@ Requires: None;
		@ Modifies: elv, n, used, t_end, now_f, main_req, pb;
		@ Effects: 不断取出主请求来执行
		*/
		elv = new Elevator();	
		n = time.length;//所有合法输入的个数
		used = new int[n];
		t_end = new double[n];
		Arrays.fill(used, 0);
		now_f = 1;
		
		for (i = 0; i < n; ++i){
			if (used[i]==0){
				main_req = i;
				exec(main_req);
				while (q.peek() != null){
					pb = q.peek();
					main_req = pb.getI();
					exec(main_req);
				}
			}
		}
		elv.repOK();
		//RequestQueue.out_invalid();
		//out_essential();
	}
	
	void exec(int m_req){
		/*@ Requires: None;
		@ Modifies: System.out, now_dir, pb, q, n, i, j, k, now_f, now_t, q_f, t_end;
		@ Effects: 采用ALS_Schedule来求出电梯的执行结果
		*/
		int j;
		if (!flag_req(m_req)){
			if (used[m_req] == -1){
				//System.out.println("Exactly the same request as another");
				
				System.out.print("SAME ");//SAME [FR,9,DOWN,1]
				out_s(m_req, "", 0);
				//out_ess.add("SAME "+out_s(m_req, "", 0));
			}
			if (used[m_req] == -2){
				//System.out.println("Essentially the same request as another");
				
				System.out.print("SAME ");//SAME [FR,9,DOWN,1]
				out_s(m_req, "", 0);
				//out_ess.add("SAME "+out_s(m_req, "", 0));
			}
			return ;
		}
		
		now_f = elv.getFloor();
		if (floor[m_req] > now_f){
			now_dir = 0;//UP
		}else if (floor[m_req] < now_f){
			now_dir = 1;//DOWN
		}else{
			now_dir = 2;//STILL
		}
		
		boolean sign_exist = false;
		for (Piggyback pb_t:q)//{
			if (pb_t.getI() == m_req){
				sign_exist = true;
				break;
			}
		//}
		
		if (!sign_exist){
			pb = new Piggyback(m_req,floor[m_req]-elv.getFloor());
			q.add(pb);
		}
		
		int floor_tot, floor_tot_main;
		now_t = mymax(elv.getTime(),1.0*time[m_req]);
		elv.setTime(now_t);
		
		for (j = 0; j < n; ++j){
			if (j != m_req && used[j] == 0){
				floor_tot = floor_tot_main = 0;
				for (Piggyback pb_t:q){
					if (floor[pb_t.getI()]<floor[j])
						floor_tot++;
					if (floor[pb_t.getI()]<floor[m_req])
						floor_tot_main++;
				}
				if (0.5*myabs(floor[j]-elv.getFloor())+now_t+1.0*floor_tot<=time[j]){
					continue;
				}
				if (0.5*myabs(floor[m_req]-elv.getFloor())+now_t+1.0*floor_tot_main<=time[j]){
					continue;
				}
				
				int e,r;
				e = m_req;
				r = j;
				boolean tmp = ( (type[r] == 1 && updown[r] == now_dir) && ( (updown[r] == 0 && floor[r] <= floor[e] && floor[r] > elv.getFloor()) || (updown[r] == 1 && floor[r]>=floor[e] && floor[r] < elv.getFloor()) ))||((type[r] == 2) && ( (now_dir == 0 && floor[r] > elv.getFloor()) || (now_dir == 1 && floor[r] < elv.getFloor()) ));
//if (isCarry(m_req, j)){
				if (tmp){
					pb = new Piggyback(j,floor[j]-elv.getFloor());
					q.add(pb);
					used[j] = 1;//a sign that it's under use but not completed;
				}
			}
		}
		
		int i_this;
		boolean sign;
		now_f = elv.getFloor();
		if (now_dir == 0){//UP
			for (k = now_f+1; k <= floor[m_req]; ++k){
				elv.up_1();
				//System.out.println(elv.getFloor()+","+elv.getTime());
				if (q.peek() != null && elv.getFloor() == floor[q.peek().getI()]){
					sign = false;
					while (q.peek() != null && elv.getFloor() == floor[q.peek().getI()]){
						i_this = q.peek().getI();
						if (!flag_req(i_this)){
							used[i_this] = -3;
							//System.out.println("Essentially the same request as another");
							
							System.out.print("SAME ");//SAME [FR,9,DOWN,1]
							out_s(i_this, "", 0);
							//out_ess.add("SAME "+out_s(m_req, "", 0));
							q.poll();
							continue;
						}
						used[i_this] = 2;//completed
						out_s(i_this, s[now_dir], 1);
						q_f.offer(i_this);
						q.poll();
						t_end[i_this] = elv.getTime()+1;
						sign = true;
					}
					if (sign){
						elv.open();
					}
				}
			}
		}else if (now_dir == 1){//DOWN
			for (k = now_f-1; k >= floor[m_req]; --k){
				elv.down_1();
				if (q.peek() != null && elv.getFloor() == floor[q.peek().getI()]){
					while (q.peek() != null && elv.getFloor() == floor[q.peek().getI()]){
						i_this = q.peek().getI();
						used[i_this] = 2;//completed
						out_s(i_this, s[now_dir], 1);
						q_f.offer(i_this);
						q.poll();
						t_end[i_this] = elv.getTime()+1;
					}
					elv.open();
				}
			}
		}else{//STILL
			used[m_req] = 2;
			elv.open();
			//System.out.println("STILL"+elv.getTime());
			i_this = m_req;
			
			out_s(i_this, s[now_dir], 1);
			q_f.offer(i_this);
			q.poll();	
			t_end[i_this] = elv.getTime();
		}
	}
	
	private double mymax(double x, double y) {
		/*@ Requires: None;
		@ Modifies: None;
		@ Effects: \result == (x>y)?x:y;
		*/
		return (x>y)?x:y;
	}

//	boolean isCarry(int e,int r){
//		/*@ Requires: None;
//		@ Modifies: None;
//		@ Effects: \result == 请求e可以捎带请求r ==> true
//							  请求e不可以捎带请求r ==> false;
//		*/
//		boolean tmp = ( (type[r] == 1 && updown[r] == now_dir) && 
//				( (updown[r] == 0 && floor[r] <= floor[e] && floor[r] > elv.getFloor()) 
//						|| (updown[r] == 1 && floor[r]>=floor[e] && floor[r] < elv.getFloor()) )
//							)
//				||
//				((type[r] == 2) && 
//						( (now_dir == 0 && floor[r] > elv.getFloor()) 
//							|| (now_dir == 1 && floor[r] < elv.getFloor()) )
//							);
//		
////		if ( (type[r] == 1 && updown[r] == now_dir) && 
////			( (updown[r] == 0 && floor[r] <= floor[e] && floor[r] > elv.getFloor()) 
////			|| (updown[r] == 1 && floor[r]>=floor[e] && floor[r] < elv.getFloor()) )
////				){
////			return true;
////		}
////		if ((type[r] == 2) && 
////			( (now_dir == 0 && floor[r] > elv.getFloor()) 
////				|| (now_dir == 1 && floor[r] < elv.getFloor()) )
////				){
////			return true;
////		}
//		//Lack a situation (4)//Or not 
////return false;
//		return tmp;
//	}
	
	boolean flag_req(int i){
		/*@ Requires: None;
		@ Modifies: used;
		@ Effects: \result == 请求i此时可以执行 ==> true
							  否则 ==> false;
		*/
		int j;
		for (j = 0; j < i; ++j){
			if (time[i]==time[j]&&type[i]==type[j]
				&&floor[i]==floor[j]&&updown[i]==updown[j]){
			//如果该条指令和之前的某一条完全一样，则忽略
				//System.out.println("Line"+i+":Ignored.Same as the previous");//last one");
				used[i] = -1;
				return false;
			}
		}
		
		double t_q;
		for (Integer i_q:q_f){
			t_q = t_end[i_q];
			if (t_q >= time[i] && floor[i_q]==floor[i] && type[i_q]==type[i] 
					&& updown[i_q]==updown[i]){
				used[i] = -2;
				return false;
			}
		}
		return true;
	}
	void out_s(int i_this,String dir,int flag){
		/*@ Requires: None;
		@ Modifies: System.out, elv;
		@ Effects: 按照标准格式输出请求的到达；
		*/
		if (type[i_this] == 1){//FR
			System.out.print("[FR,"+floor[i_this]+","+s[updown[i_this]]+","+time[i_this]+"]");
		}else{
			System.out.print("[ER,"+floor[i_this]+","+time[i_this]+"]");
		}
		if (flag == 1){
			System.out.print("/");
			elv.setDir(dir);
			System.out.println(elv);
		}else{
			System.out.println("");
		}
	}
	/*String out_s(int i_this,String dir,int flag){
		String ans;
		if (type[i_this] == 1){//FR
			//System.out.print("[FR,"+floor[i_this]+","+s[updown[i_this]]+","+time[i_this]+"]");
			ans = "[FR,"+floor[i_this]+","+s[updown[i_this]]+","+time[i_this]+"]";
		}else{
			//System.out.print("[ER,"+floor[i_this]+","+time[i_this]+"]");
			ans = "[ER,"+floor[i_this]+","+time[i_this]+"]";
		}
		if (flag == 1){
			System.out.print(ans+"/");
			elv.setDir(dir);
			System.out.println(elv);
		}else{
			//System.out.println("");
		}
		return ans;
	}*/
	
	
//	public void out_essential(){
//		/*@ Requires: None;
//		@ Modifies: System.out;
//		@ Effects: 输出out_ess里的所有;
//		*/
//		for (String out_t:out_ess){
//			System.out.println(out_t);
//		}
//	}
}
