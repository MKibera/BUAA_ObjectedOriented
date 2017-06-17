package elevator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

class Disp {
	final static String[] s = {"UP","DOWN","STILL"};
	static Long[] time;//保存所有合法的时间的数组
	static int[] floor;//保存所有合法的楼层的数组
	static int[] type;//保存所有合法的类型的数组
	static int[] updown;//保存所有合法的上下的数组
	static int n, i, j, k;//n:所有合法输入的个数
	static int[] used;
	static int now_f;//当前的行数
	double now_t;//当前的时间
	static Queue<Integer> q_f = new LinkedList<Integer>();//下标的队列
	static double[] t_end;
	
	public Disp(Long[] t, int[] f,int[] typ,int[] upd) {//构造函数
		time = t;
		floor = f;
		type = typ;
		updown = upd;
	}
	
	void work(){//Fool Schuedule: 求出输出结果
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
				System.out.print("("+floor[i]+","+out+",");
				System.out.printf("%.1f",now_t+1.0);//保留一位小数
				System.out.println(")");
			}else{
				System.out.print("("+floor[i]+","+out+",");
				System.out.printf("%.1f",now_t);//保留一位小数
				System.out.println(")");
			}
			now_f = floor[i];
			now_t++;
			
			q_f.offer(i);
			t_end[i] = now_t;
		}
	}
	int myabs(int t){//求一个数的绝对值
		return (t>=0)?t:-t;
	}
}

public class Dispatch extends Disp {
	public Dispatch(Long[] t, int[] f, int[] typ, int[] upd) {
		super(t, f, typ, upd);
	}
	

	static Piggyback pb;
	Queue<Piggyback> q = new PriorityQueue<Piggyback>(pb.getCmp());
	static Elevator elv;
	int now_dir;//
	int main_req;
	static ArrayList<String> out_ess = new ArrayList<String>();
	
	void work(){//ALS_Schedule
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
		//RequestQueue.out_invalid();
		//out_essential();
	}
	
	void exec(int m_req){
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
		for (Piggyback pb_t:q){
			if (pb_t.getI() == m_req){
				sign_exist = true;
				break;
			}
		}
		
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
				if (isCarry(m_req, j)){
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
		return (x>y)?x:y;
	}

	boolean isCarry(int e,int r){
		if ( (type[r] == 1 && updown[r] == now_dir) && 
			( (updown[r] == 0 && floor[r] <= floor[e] && floor[r] > elv.getFloor()) 
			|| (updown[r] == 1 && floor[r]>=floor[e] && floor[r] < elv.getFloor()) )
				){
			return true;
		}
		if ((type[r] == 2) && 
			( (now_dir == 0 && floor[r] > elv.getFloor()) 
				|| (now_dir == 1 && floor[r] < elv.getFloor()) )
				){
			return true;
		}
		//Lack a situation (4)//Or not 
		return false;
	}
	
	boolean flag_req(int i){
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
	static void out_s(int i_this,String dir,int flag){
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
	/*static String out_s(int i_this,String dir,int flag){
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
	public void out_essential(){
		for (String out_t:out_ess){
			System.out.println(out_t);
		}
	}
}
