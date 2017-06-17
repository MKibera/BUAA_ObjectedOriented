package elevator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class Dispatch extends Disp {
	//Overview:使用ALS策略来根据请求调度电梯运行的调度器
	/* 表示对象：Elevator elevator, int[] floor,int[] type,int[] updown,Long[] time
	 * 抽象函数：AF(c)=(elevator_,floor_,type_,updown_,time_)where
	 * c.T==T_,c.elevator==elevator_,c.floor=floor_,c.time==time_,c.updown=updown_
	 * 不变式：elv!=null&&time!=null&&floor!=null&&type!=null&&updown!=null;
	 */
	
	Elevator elv;
	public Dispatch(Long[] t, int[] f, int[] typ, int[] upd) {
		/*@ Requires: None;
		@ Modifies: None;
		@ Effects: 构造函数的初始化
		*/
		super(t, f, typ, upd);
		elv = new Elevator();
	}
	public boolean repOK(){
		/*@ REQUIRES: None;
		@ MODIFIES: None;
		@ Effects: \result == invariant(this);
		@ Invariant(this): elv!=null&&time!=null&&floor!=null&&type!=null&&updown!=null;
		*/
		return elv!=null&&time!=null&&floor!=null&&type!=null&&updown!=null;
	}
	Piggyback pb;
	Queue<Piggyback> q = new PriorityQueue<Piggyback>(pb.getCmp());
	
	int now_dir;//
	int main_req;
	ArrayList<String> out_ess = new ArrayList<String>();
	
	void work(){
		/*@ Requires: None;
		@ Modifies: elv, n, used, t_end, now_f, main_req, pb;
		@ Effects: 当存在还没有执行的指令时，把它取出来作为主请求来执行。
				   然后把q中的请求都拿出来执行。
		*/
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
				   首先检查主请求的正确性(即有没有和之前的请求重复过)
				   根据楼层关系求出电梯前进的方向
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
}
