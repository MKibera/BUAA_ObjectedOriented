package elevator;

import java.util.*;

public class Elevator {
	final static String[] s = {"UP","DOWN","STILL"};
	private Long[] time;//保存所有合法的时间的数组
	private int[] floor;//保存所有合法的楼层的数组
	private int[] type;//保存所有合法的类型的数组
	private int[] updown;//保存所有合法的上下的数组
	private int n, i, j;//n:所有合法输入的个数
	static ArrayList<Integer> floor_sametime = new ArrayList<Integer>();//new<Integer> ArrayList();
	int now_f;//当前的行数
	double now_t;//当前的时间
	Queue<Integer> q_f = new LinkedList<Integer>();//下标的队列
	public Elevator(Long[] t, int[] f,int[] typ,int[] upd) {//构造函数
		time = t;
		floor = f;
		type = typ;
		updown = upd;
	}
	
	void work(){//求出输出结果
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
			
			/*if (type[i]==1){
				int v;
				int sig = 0;
				if (i > 0 && time[i]==last_time){
					for(int k = 0;k < floor_sametime.size(); k++){
			           v = (int) floor_sametime.get(k);
			           if (v == floor[i]){
			        	   System.out.println("Line"+i+":Ignored.DON'T ALLOW "
			        	   	+ "THE SAME FLOOR AT THE SAME TIME TO UP AND DOWN");
			        	   sig = 1;
			        	   break;
			           }
			        }
				}
				if (sig==1){
					continue;
				}
				
				if (i > 0 && time[i]==last_time){
					floor_sametime.add(floor[i]);
				}else{
					floor_sametime.clear();
				}
				floor_sametime.add(floor[i]);
			}*/
			
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
	static int myabs(int t){//求一个数的绝对值
		return (t>=0)?t:-t;
	}
}
