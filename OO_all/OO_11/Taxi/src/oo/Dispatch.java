package oo;
import java.awt.Point;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;
public class Dispatch extends Thread {
	final static int N = 80;
	final static String condition[] = {"服务","接单","等待","运行"};
	private RequestQueue rq;
	private long time_now;
	private Car[] c;
	private int num, dist, dismin;
	private boolean sign, first_flag;
	Queue<Integer> q = new LinkedList<Integer>();
	private int dis[] = new int[6410];
	private boolean vis[] = new boolean[6410];
	
	public Dispatch(RequestQueue rq_t, Car[] c_t){
		/*@ REQUIRES: None;
		@ MODIFIES: rq, c;
		@ EFFECTS: rq == rq_t;
					c == c_t;
		*/
		rq = rq_t;
		c = c_t;
	}
	public boolean repOK(){
		/*@ REQUIRES: None;
		@ MODIFIES: None;
		@ Effects: \result == invariant(this);
		@ Invariant(this): rq != null && c != null; 
		*/
		if (rq==null)
			return false;
		if (c==null)
			return false;
		return true;
	}
	public void run(){
		/*@ REQUIRES: None;
		@ MODIFIES: System.out, num, dist, dismin, sign, first_flag, q, dis, vis, rq;
		@ EFFECTS: 把请求分配到每一个车上
		*/
		while (true){
			time_now = new Date().getTime();
			//if (rq.gettot()!=0)
			//System.out.println("**"+rq.gettot());
			for (int j = 0; j < rq.gettot(); ++j)
			{//这一部分进行判断的是每一个刚发出的请求它有哪些旁边2*2的出租车满足要求，并把满足要求的taxi放入每一个请求的set里(放入set是防止重复
				//System.out.println("@@@"+time_now+"  "+rq.gettime(j));
				first_flag = false;
				if (!rq.getused(j) && time_now >= rq.gettime(j) && time_now <= rq.gettime(j)+3000)
				{
					if (rq.getfirst(j)){//每次只要输出一次，因此引入了布尔量first
						rq.setfirst(j);
						
TaxiGUI gui=new TaxiGUI();
gui.RequestTaxi(new Point(rq.getsrcx(j)-1,rq.getsrcy(j)-1), new Point(rq.getdstx(j)-1,rq.getdsty(j)-1));//读入大量请求，请注释掉这句话！！！			
						
						first_flag = true;
						System.out.println("请求 "+j+" 发出时，以该请求的坐标为中心的4×4区域中的所有出租车状态、信用信息：");
						RequestQueue.LogFile(j).println("请求 "+j+" 发出时，以该请求的坐标为中心的4×4区域中的所有出租车状态、信用信息：");
					}
									
					//System.out.println("………");
					for (int i = 1; i <= 100; ++i){//if (rq.getfirst(j))//System.out.println(i+" $$$"+" "+c[i].getx()+" "+" "+c[i].gety()+"            "+c[i].getstate());
						if (myabs(c[i].getx()-rq.getsrcx(j))<=2 && myabs(c[i].gety()-rq.getsrcy(j))<=2){
							if (c[i].getstate() == 2){
								//System.out.println("%%%"+j +"   "+i);

								rq.setset(j,i);//rq.getset(j).add(i);
								//System.out.println(j+"~~~"+rq.getset(j).size());
							}
							if (first_flag){
								System.out.println("出租车 "+i+"： 目前在("+c[i].getx()+", "+c[i].gety()+"); " + condition[c[i].getstate()] + "状态; 信用值："+c[i].getcredit());
								RequestQueue.LogFile(j).println("出租车 "+i+"： 目前在("+c[i].getx()+", "+c[i].gety()+"); " + condition[c[i].getstate()] + "状态; 信用值："+c[i].getcredit());
								c[i].add_credit();//抢一次单，信用
							}
						}			
					}
					if (first_flag){
						System.out.println("--------------------------------------------------------------------------");
						RequestQueue.LogFile(j).println("--------------------------------------------------------------------------");
					}
				}
			}
			time_now = new Date().getTime();
			for (int j = 0; j < rq.gettot(); ++j)
			if (!rq.getused(j) && time_now > rq.gettime(j) + 3000){
			//这一部分是把到达3秒的请求拿出来进行处理
				int location = loc(rq.getsrcx(j), rq.getsrcy(j));//以起始点为源点求一波最短路径，试图找到当信用值相同时，离它最近的点
				spfa_2(location);
				
				sign = false;
				System.out.print("请求 "+j+" 在抢单时间窗内抢单的出租车: { ");
				RequestQueue.LogFile(j).print("请求 "+j+" 在抢单时间窗内抢单的出租车: { ");
				for(Integer k : rq.getset(j)){
					System.out.printf("%d ",k);
					RequestQueue.LogFile(j).printf("%d ",k);
		            if (c[k].getstate() == 2){
		            	if (sign == false){
		            		sign = true;
		            		num = k;
		            		dismin = dis[loc(c[k].getx(),c[k].gety())];//System.out.println("k=" + k+ "distmin= "+dismin);
		            		//dismin = myabs(c[k].getx()-rq.getsrcx(j))+myabs(c[k].gety()-rq.getsrcy(j));
		            	}else{
		            		//dist = myabs(c[k].getx()-rq.getsrcx(j))+myabs(c[k].gety()-rq.getsrcy(j));
		            		dist = dis[loc(c[k].getx(),c[k].gety())];
		            		//System.out.println("k=" + k+ "distmin= "+dismin + "dis="+dist);
		            		if ((c[k].getcredit() > c[num].getcredit()) || (c[k].getcredit() == c[num].getcredit()
		            				&& dist < dismin)){
		            				num = k;
		            				dismin = dist;
		            		}
		            	}
		            }
		        }
				System.out.println("}");
				RequestQueue.LogFile(j).println("}");
				System.out.println("--------------------------------------------------------------------------");
				RequestQueue.LogFile(j).println("--------------------------------------------------------------------------");
				
				if (sign){
					System.out.println("请求 "+j+" 分配成功，分配给了 "+num+" 出租车");
					RequestQueue.LogFile(j).println("请求 "+j+" 分配成功，分配给了 "+num+" 出租车");
					int temp, temp2;
					temp = loc(rq.getsrcx(j), rq.getsrcy(j));
					temp2 = loc(rq.getdstx(j), rq.getdsty(j));
					c[num].spfa(j, temp, temp2);//是编号为j的请求，起始点是temp，目的地是temp2
					rq.setused(j, true);
				}else{
					System.out.println("请求 "+j+"分配失败");
					RequestQueue.LogFile(j).println("请求 "+j+"分配失败");
					rq.setused(j, true);
				}
				System.out.println("--------------------------------------------------------------------------");
				RequestQueue.LogFile(j).println("--------------------------------------------------------------------------");
			}
		}
	}
	
	public void spfa_2(int u){//起始点是u
		/*@ REQUIRES: 1 <= u <= 6400;
		@ MODIFIES: q, dis, vis;
		@ EFFECTS: 求出起始点为的最短路径
		*/
		int i, v, vx, vy;
		q.clear();
		Arrays.fill(dis, 1000000);
		Arrays.fill(vis, false);
		q.offer(u);
		dis[u] = 0;
		while (!q.isEmpty()){
			v = q.poll();
			//System.out.print("222222SPFA ");
			//out_pos(v);
			vis[v] = true;
			
			vx = (v-1) / N+1;
			vy = v % N;
			if (vy == 0){
				vy = N;
			}
			if (vy + 1 <= 80 && Map.getg()[0][vx][vy]==1 || Map.getg()[0][vx][vy]==3){
				i = job(vx,vy+1);
				slack(i,v);
			}
			if (vx + 1 <= 80 && Map.getg()[0][vx][vy]==2 || Map.getg()[0][vx][vy]==3){
				i = job(vx+1,vy);
				slack(i,v);
			}
			if (vx - 1 >= 1 && Map.getg()[0][vx-1][vy]==2 || Map.getg()[0][vx-1][vy]==3){
				i = job(vx-1,vy);
				slack(i,v);
			}
			if (vy - 1 >= 1 && Map.getg()[0][vx][vy-1]==1 || Map.getg()[0][vx][vy-1]==3){
				i = job(vx,vy-1);
				slack(i,v);
			}
			vis[v] = false;
		}
	}
	public void slack(int i,int v){
		/*@ REQUIRES: None;
		@ MODIFIES: q, vis;
		@ EFFECTS: 对于最短路径的松弛操作；
		*/
		if (dis[v] + 1 < dis[i]){
			dis[i] = dis[v] + 1;
			if (!vis[i]){
				q.offer(i);
				vis[i] = true;
			}	
		}
	}
	public int job(int x,int y){
		/*@ REQUIRES: None;
		@ MODIFIES: None;
		@ EFFECTS: \retult == 80*(x-1)+y;
		*/
		return 80*(x-1) + y;
	}
	public int loc(int x,int y){
		/*@ REQUIRES: None;
		@ MODIFIES: None;
		@ EFFECTS: \retult == 80*(x-1)+y;
		*/
		return 80*(x-1)+y;
	}
	public int myabs(int x){
		/*@ REQUIRES: None;
		@ MODIFIES: None;
		@ EFFECTS: \retult == abs(x);
		*/
		if (x <= 0)
			return -x;
		return x;
	}
}
