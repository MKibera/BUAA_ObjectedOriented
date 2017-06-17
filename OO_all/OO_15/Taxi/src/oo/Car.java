package oo;
/**
 * @startuml
 * [*] --> 等待服务
 * 等待服务 -> 停止运行 : wait_step > 100
 * 等待服务 -> 准备服务 : spfa():setstate(3)
 * 等待服务 : 四处游走等待抢乘客请求
 * 等待服务 -> 等待服务
 * 服务 -> 停止运行 : pos == pla
 * 服务 : 出租车接到了客，送客服去目的地
 * 服务 -> 服务
 * 准备服务 -> 停止运行 : pos == destination
 * 准备服务 -> 准备服务
 * 准备服务 : 去接乘客的路上
 * 停止运行 -> 等待服务
 * 停止运行 -> 服务
 * note bottom of 停止运行 : 1s后回到其它状态
 * note bottom of 服务 :  完成当前服务(到达用户的目的地)后，停止运行1s
 * note bottom of 等待服务 : 出租车抢到用户请求后进入等待服务\n 等待服务持续20s后，停止运行1s
 * note bottom of 准备服务 : 到达用户等待位置，停止运行1s
 * note "出租车状态图" as N1
 * @enduml
 */
/*
@startuml
scale 350 width
[*] --> READY

TOCUS : working(int)
TOCUS --> IDLE:pathIndex == path.length()/
TOCUS --> TOCUS:pathIndex != path.length()/
TODES --> IDLE:pathIndex == path.length()/
TODES : working(int)
TODES --> TODES:pathIndex != path.length()/
READY --> IDLE:ticktock==0/
READY --> READY:200ms之后[ticktock!=0]/
READY : driving()
READY : checkTrafficLight(int, int, int)
READY --> TOCUS:addCust(Customer)/
READY --> TOCUS:listening()\n[!customers.isEmpty()]/
IDLE --> TODES:1s之后\n[前一个状态为TOCUS]/
IDLE --> READY:1s之后\n[前一个状态为TODES或者READY]/
note left of READY: READY代表等待服务状态
note left of TODES: TODES代表服务状态
note bottom of IDLE:IDLE代表停止运行状态
note left of TOCUS:TOCUS代表准备服务状态
@enduml
*/

//停止运行，服务，等待服务，准备服务
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import oo.gv;

public class Car implements Runnable{ 
//extends Thread {
	final static int N = 80;
	final static int M = 100;
	final static int M1 = 70;//普通出租车
	final static int M2 = 30;//追踪出租车
	final static int xx[] = {1,0,0,-1,0};
	final static int yy[] = {0,1,-1,0,0};
	private int credit, pos,  dir, lastdir, nx, ny, pla, vx, vy;
	private int x, y, cnt, state, wait_step;
	//private volatile int state;//private  int  state;
	Queue<Integer> q = new LinkedList<Integer>();
	private int dis[] = new int[6410];
	private boolean vis[] = new boolean[6410];
	private int prev[] = new int[6410];
	private int destination;
	private long wait_start, now_t;
	private Car[] c_all;
	private int nextx,nexty;
	protected int cus_id, id;
	
	private int spec;
	protected ArrayList<Integer> serve = new ArrayList<Integer>();
	protected ArrayList<Integer> send = new ArrayList<Integer>();
	//protected ArrayList<Integer> spath[] = new ArrayList<Integer>()[110];
	protected ArrayList[] spath = new ArrayList[2010];
	//Map<String, String> map = new HashMap<String, String>();
	//Map<Integer, ArrayList> spath = new HashMap<Integer,ArrayList<Integer>>();
	protected static RequestQueue rq;
	//private Thread t;
	
	public Car(RequestQueue rq_t,int id_t, Car[] c_all_t, int spec_t){
		/*@ Requires: None;
		@ Modifies: id, c_all, pos, wait_start, Main.gui, wait_step;
		@ Effects: id == id_t;
		   			 c_all == c_all_t;
		   			 pos == rand.nextInt(N*N) + 1;
		   			 以及一些构造函数的初始化
		*/	
		rq = rq_t;
		for (int i = 0; i < spath.length; ++i)
			spath[i] = new ArrayList<Integer>();
		
		c_all = c_all_t;
		id = id_t;
		credit = 0;
		Random rand = new Random();
		pos = rand.nextInt(N*N) + 1;
		workxy();
		System.out.printf("Taxi %2d Located At (%2d,%2d)\n", id, x, y);
		setstate(2);
		Main.gui.SetTaxiStatus(id, new Point(x-1,y-1), state);
		wait_start = new Date().getTime();
	wait_step = 0;
		destination = -1;
		spec = spec_t;
		cus_id = -1;
		Main.gui.SetTaxiType(id, spec_t);//void SetTaxiType(int index, int type)
		Thread t = new Thread(this);
		t.setUncaughtExceptionHandler(new MyException());
		t.start();
	}
	public boolean repOK(){
		/*@ REQUIRES: None;
		@ MODIFIES: None;
		@ Effects: \result == invariant(this);
		@ Invariant(this): id >= 1 && pos >= 1 && pos <= N*N && c_all[id] != null;
		*/
		if (id < 1)
			return false;
		if (pos<1 || pos>N*N)
			return false;
		if (c_all[id] == null)
			return false;
		return true;
	}
	
	public void out_it(){
		
	}
	
	public void run(){
		/*@ Requires: None;
		@ Modifies: System.out, id, credit, pos, cus_id, dir, x, y, cnt, state, nextx, nexty, Map, Main.gui;
		@ Effects: normal behavior
				   进行车的运动
				   sleep出现异常 ==> exceptional_behavior (e);
		@ THREAD_REQUIRES: \locked(this);
		@ THREAD_EFFECTS: \locked();
		*/
		while (true){
			workxy();
			if (state == 3){//接单状态
				
//out_pos();
	//if (pos != destination)
		spath[cus_id].add(pos);
				Main.gui.SetTaxiStatus(id, new Point(x-1,y-1), state);	
				if (pos != destination){
					boolean exist_b = !myexist(pos,prev[pos]);
					if (exist_b){
						workxy();
						//System.out.println("MISS At ("+x+" , "+y+")");
						spfa_2(destination);
					}
					lastdir = dir;
					dir = cal_dir(pos, prev[pos]);//cal_dir(job(x,y), prev[pos]);
	//System.out.println("***"+pos+" "+prev[pos]);
	Map.addflow(x, y, dir,1);
	//System.out.println("NNN"+pos+" "+prev[pos]);				
					nextx = (prev[pos]-1) / N+1;
					nexty = prev[pos] % N;
					if (nexty == 0)
						nexty = N;	
					Map.addflow(nextx,nexty,3-dir,2);
				}
				
				if (pos == destination){//到达用户的地方了
					//System.out.println("Customer "+cus_id+" Taxi "+ id+ " Come!");
					//RequestQueue.LogFile(cus_id).println("Customer "+cus_id+" Taxi "+ id+ " Come!");
					setstate(0);//state = 0;//进入停止运行状态1秒
					Main.gui.SetTaxiStatus(id, new Point(x-1,y-1), state);
					try {
						Thread.sleep(1000);//Thread.Thread.sleep(777);
					} catch (Exception e) {}
					setstate(1);//state = 1;//停止完后进入服务状态//System.out.println("To Go to the dest!!!");//RequestQueue.LogFile(cus_id).println("To Go to the dest!!!");
					Main.gui.SetTaxiStatus(id, new Point(x-1,y-1), state);
					spfa_2(pla);
					//System.out.println("请求 "+cus_id+" 的行驶路线：");
					////if (spec == 0)
						RequestQueue.LogFile(cus_id).println("请求 "+cus_id+" 的行驶路线：");
					continue;
				}
				try {
					Thread.sleep(200+delay(lastdir,dir));//Thread.Thread.sleep(777);
				} catch (Exception e) {}
	Map.subflow(x, y, dir,-3);
	Map.subflow(nextx,nexty,3-dir,-4);
	
				setpos(prev[pos]);//pos = prev[pos];
				
			}else if (state == 1){//处于服务状态
				
	spath[cus_id].add(pos);
	
				cnt++;
				out_pos();//System.out.println("UUUU"+id);
				
				workxy();				
				Main.gui.SetTaxiStatus(id, new Point(x-1,y-1), state);
				if (cnt % 7 ==0){
					//System.out.println("");
					////if (spec == 0)
						RequestQueue.LogFile(cus_id).println("");
				}
				
				if (pos != pla){
					boolean exist_b = !myexist(pos,prev[pos]);
					if (exist_b){
						workxy();
						//System.out.println("Serve MISS At "+pos+"   ("+x+" , "+y+")");
						spfa_2(pla);
						//System.out.println("^^^^^^"+pos+" , "+prev[pos]);
					}
					lastdir = dir;
					dir = cal_dir(pos, prev[pos]);//cal_dir(job(x,y), prev[pos]);
					Map.addflow(x, y, dir,5);
					
					nextx = (prev[pos]-1) / N+1;
					nexty = prev[pos] % N;
					if (nexty == 0)
						nexty = N;	
					Map.addflow(nextx,nexty,3-dir,6);
				}
				
				
				
				if (pos == pla){
					//System.out.println("Customer "+cus_id+" Arrived By Taxi "+ id);
					//System.out.println("到达目的地！");
					////if (spec == 0)
						RequestQueue.LogFile(cus_id).println("到达目的地！");
					credit += 3;
					setstate(0);//state = 0;//停止状态
					Main.gui.SetTaxiStatus(id, new Point(x-1,y-1), state);
					try {
						Thread.sleep(1000);//Thread.Thread.sleep(777);
					} catch (Exception e) {}
					setstate(2);//state = 2;//进入等待状态
					Main.gui.SetTaxiStatus(id, new Point(x-1,y-1), state);
					wait_start = new Date().getTime();
		wait_step = 0;
out_it();
					continue;
				}
				try {
					Thread.sleep(200+delay(lastdir,dir));//Thread.Thread.sleep(777);
				} catch (Exception e) {}
	Map.subflow(x, y, dir,-1);
	Map.subflow(nextx,nexty,3-dir,-2);
				setpos(prev[pos]);//pos = prev[pos];
			}else{//处于等待状态，每次随机一个方向到处跑
				workxy();					
				Main.gui.SetTaxiStatus(id, new Point(x-1,y-1), state);
				
				now_t = new Date().getTime();
		wait_step++;
				//if (now_t - wait_start >= 20000){
				if (wait_step > 100){
					//System.out.println("TTTTT"+(now_t - wait_start)+"PPPP"+wait_step);
					setstate(0);//state = 0;//停止运行
					Main.gui.SetTaxiStatus(id, new Point(x-1,y-1), state);
					try {
						Thread.sleep(1000);//Thread.Thread.sleep(777);
					} catch (Exception e) {}
					setstate(2);//state = 2;//进入等待状态
					Main.gui.SetTaxiStatus(id, new Point(x-1,y-1), state);
					wait_start = new Date().getTime();
		wait_step = 0;
				}
				
				Random rand = new Random();
				int ccc = 0;
				ArrayList<Integer> ttt = new ArrayList<Integer>();
				ttt.clear();
				
		int min_flow=(int) 1e9;
				boolean tmp = Map.getg()[spec][x][y] == 2 || Map.getg()[spec][x][y] == 3;
				if (tmp){//Car Down
					int f = Map.getflow(x, y, 0);
					if (f == min_flow){
						//if (Map.getflow(x, y, 0) != 0)
						//	System.out.println("Dir:"+x+" "+y+" "+dir+" "+" "+Map.getflow(x, y, 0));
						min_flow = Math.min(min_flow, Map.getflow(x, y, 0));
						ttt.add(0);
						ccc++;
					}else if (f < min_flow){
						ttt.clear();
						ccc = 0;
						min_flow = Math.min(min_flow, Map.getflow(x, y, 0));
						ttt.add(0);
						ccc++;
					}
					
				}
				int g;
				int ggg;
				g = Map.getg()[spec][x][y];
				if (g == 1 || g == 3){//Car Right
					ggg = Map.getflow(x, y, 1);
					if (ggg == min_flow){
						//if (Map.getflow(x, y, 1) != 0)
						//	System.out.println("Dir:"+x+" "+y+" "+dir+" "+" "+Map.getflow(x, y, 1));
						min_flow = Math.min(min_flow, Map.getflow(x, y, 1));
						ttt.add(1);
						ccc++;
					}else if (ggg < min_flow){
						ttt.clear();
						ccc = 0;
						min_flow = Math.min(min_flow, Map.getflow(x, y, 1));
						ttt.add(1);
						ccc++;
					}
				}
				g = Map.getg()[spec][x][y-1];
				if (y-1>=1&&g == 1 || g == 3){//Car Left
					ggg = Map.getflow(x, y, 2); 
					if (ggg == min_flow){
						//if (Map.getflow(x, y, 2) != 0)
						//	System.out.println("Dir:"+x+" "+y+" "+dir+" "+" "+Map.getflow(x, y, 2));
						min_flow = Math.min(min_flow, Map.getflow(x, y, 2));
						ttt.add(2);
						ccc++;
					}else if (ggg < min_flow){
						ttt.clear();
						ccc = 0;
						min_flow = Math.min(min_flow, Map.getflow(x, y, 2));
						ttt.add(2);
						ccc++;
					}
				}
				g = Map.getg()[spec][x-1][y];
				if (x-1>=1&&g == 2 || g == 3){//Car Up
					ggg = Map.getflow(x, y, 3);
					if (ggg == min_flow){
						//if (Map.getflow(x, y, 0) != 0)
						//	System.out.println("Dir:"+x+" "+y+" "+dir+" "+" "+Map.getflow(x, y, 0));
						min_flow = Math.min(min_flow, Map.getflow(x, y, 3));
						ttt.add(3);
						ccc++;
					}else if (ggg < min_flow){
						ttt.clear();
						ccc = 0;
						min_flow = Math.min(min_flow, Map.getflow(x, y, 3));
						ttt.add(3);
						ccc++;
					}
				}
			/*--------------------------
			 * -------------------------
			 * -------------------------	
			 */
				lastdir = dir;
				if (ccc > 0)
					dir = ttt.get(rand.nextInt(ccc));
				else {
					//System.out.println("EDSQ"+ccc+" "+min_flow);
					dir =  4;
				}
	//System.out.println("CHOOSE dir"+dir);
	Map.addflow(x, y, dir,3);
	nextx = x+xx[dir];
	nexty = y+yy[dir];
	Map.addflow(nextx, nexty, 3-dir,4);
				//System.out.println("dir"+dir);
				try {
					Thread.sleep(200+delay(lastdir,dir));//Thread.sleep(777);
				} catch (Exception e) {}
				//x = x+xx[dir];
				//y = y+yy[dir];
		Map.subflow(x, y, dir,-5);
		Map.subflow(nextx, nexty, 3-dir,-6);
				setxy(x+xx[dir], y+yy[dir]);
				setpos(80*(x-1)+y);//pos = 80*(x-1) + y;
			}
		}
	}
	public synchronized long delay(int lastdir,int direction){//绿灯不能左转，红灯不能前行
		/*@ REQUIRES: None;
		@ MODIFIES: None;
		@ EFFECTS:  (需要等待红绿灯) ==> \result == wt;
					(没有红绿灯或者不需要等待红绿灯) ==> \result == 0;
		@ THREAD_REQUIRES: \locked(this);
		@ THREAD_EFFECTS: \locked();
		*/
		long wt =  Main.l.t_last+Main.wt-new Date().getTime();
		
		if (!Map.getw()[x][y])//代表该点没有红绿灯
			return 0;
		int dir_type=-1;//0:直行；1:左转; 2:右转
		if (lastdir == dir){//掉头不走红绿灯吗!!! || lastdir == 3-dir
			dir_type = 0;
		}
		if ((lastdir==0&&dir==1)||(lastdir==3&&dir==2)||(lastdir==2&&dir==0)||(lastdir==1&&dir==3)){
			dir_type = 1;
		}
		if ((lastdir==0&&dir==2)||(lastdir==3&&dir==1)||(lastdir==2&&dir==3)||(lastdir==1&&dir==0)){
			dir_type = 2;
		}
		if (dir_type==-1){
			//System.out.println("ERROR DELAY lastdir:"+lastdir+" dir:"+dir);
			return 0;
		}
		if (dir==0||dir==3){//代表此时上下方向
			if (Main.l.sta==Main.l.col[x][y]){//上下方向是红灯 ==0
				if (dir_type == 0){
					//System.out.println(id+"Light get 1");//上下方向的直行
					//System.out.println(id+"DELAY"+wt);
					return wt;
				}
				return 0;
			}else{//绿灯
				if (dir_type == 1){
					//System.out.println(id+"Light get 2");//上下方向的左转
					//System.out.println(id+"DELAY"+wt);
					return wt;
				}
				return 0;
			}
		}
		if (dir==1||dir==2){//代表此时左右方向
			if (Main.l.sta==Main.l.col[x][y]){//上下方向是红灯 ==1
				if (dir_type == 0){
					//System.out.println(id+"Light get 3");//左右方向的直行
					//System.out.println(id+"DELAY"+wt);
					return wt;
				}
				return 0;
			}else{//绿灯
				if (dir_type == 1){
					//System.out.println(id+"Light get 4");//左右方向的左转
					//System.out.println(id+"DELAY"+wt);
					return wt;
				}
				return 0;
			}
		}
		//System.out.println("！！！ERROR DELAY ");
		return 0;
	}
	public synchronized void setpos(int pos_t){
		/*@ REQUIRES: None;
		@ MODIFIES: None;
		@ EFFECTS: pos == pos_t;
		@ THREAD_REQUIRES: \locked(this);
		@ THREAD_EFFECTS: \locked();
		*/
		pos = pos_t;
	}
	public synchronized void setxy(int x_t,int y_t){
		/*@ REQUIRES:  1 <= x_t <= 80 && 1 <= y_t <= 80
		@ MODIFIES: Main.gui;
		@ EFFECTS: Main.gui.SetTaxiStatus(id, new Point(x-1,y-1), state);
		@ THREAD_REQUIRES: \locked(this);
		@ THREAD_EFFECTS: \locked();
		*/
		x = x_t;
		y = y_t;
		Main.gui.SetTaxiStatus(id, new Point(x-1,y-1), state);
	}
	public synchronized void workxy(){
		/*@ REQUIRES: 0 <= pos <= 6400
		@ MODIFIES: None
		@ EFFECTS: x == (pos-1) / N+1;
				   (pos % N == 0) ==> (y == N);
				   (pos % N != 0) ==> (y == pos);
		@ THREAD_REQUIRES: \locked(this);
		@ THREAD_EFFECTS: \locked();
		*/
		x = (pos-1) / N+1;
		y = pos % N;
		if (y == 0)
			y = N;	
		Main.gui.SetTaxiStatus(id, new Point(x-1,y-1), state);
	}
	
	public synchronized int getstate(){
		/*@ REQUIRES: None;
		@ MODIFIES: None;
		@ EFFECTS: Main.gui.SetTaxiStatus(id, new Point(x-1,y-1), state);
		@ THREAD_REQUIRES: \locked(this);
		@ THREAD_EFFECTS: \locked();
		*/
		return state;
	}
	public synchronized int getcredit(){
		/*@ REQUIRES: None;
		@ MODIFIES: None;
		@ EFFECTS: \result == credit;
		@ THREAD_REQUIRES: \locked(this);
		@ THREAD_EFFECTS: \locked();
		*/
		return credit;
	}
	public synchronized void spfa(int cn_t,int u,int place_t){//接到一个单，是编号为cn_t的请求，起始点是u，目的地是place_t
		/*@ REQUIRES: None;
		@ MODIFIES: cus_id, destination, pla;
		@ EFFECTS: \result == credit;
		@ THREAD_REQUIRES: \locked(this);
		@ THREAD_EFFECTS: \locked();
		*/
		//credit++;//接到单 信用值加一
serve.add(cn_t);
		cus_id = cn_t;
		destination = u;
		pla = place_t;
		spfa_2(u);
		setstate(3);//state = 3;//接单状态，前往用户的位置
		Main.gui.SetTaxiStatus(id, new Point(x-1,y-1), state);
	}
	public synchronized void add_credit(){
		/*@ REQUIRES: None;
		@ MODIFIES: credit;
		@ EFFECTS: \result == credit;
		@ THREAD_REQUIRES: \locked(this);
		@ THREAD_EFFECTS: \locked();
		*/
		credit++;
	}
	public synchronized void spfa_2(int u){//起始点是u
		/*@ REQUIRES: None;
		@ MODIFIES: dis, vis, vx, vy, q, prev;
		@ EFFECTS: 求出以为起始点的最短路径;
		@ THREAD_REQUIRES: \locked(this);
		@ THREAD_EFFECTS: \locked();
		*/
		int i, v;
		q.clear();
		Arrays.fill(dis, 1000000);
		Arrays.fill(vis, false);
		Arrays.fill(prev, 0);
		q.offer(u);
		dis[u] = 0;
		while (!q.isEmpty()){
			v = q.poll();//System.out.print("222222SPFA ");//out_pos(v);
			vis[v] = true;
			
			vx = (v-1) / N+1;
			vy = v % N;
			if (vy == 0){
				vy = N;
			}
			if (vy + 1 <= 80 && Map.getg()[spec][vx][vy]==1 || Map.getg()[spec][vx][vy]==3){
				i = job(vx,vy+1);
				slack(i,v);
			}
			if (vx + 1 <= 80 && Map.getg()[spec][vx][vy]==2 || Map.getg()[spec][vx][vy]==3){
				i = job(vx+1,vy);
				slack(i,v);
			}
			if (vx - 1 >= 1 && Map.getg()[spec][vx-1][vy]==2 || Map.getg()[spec][vx-1][vy]==3){
				i = job(vx-1,vy);
				slack(i,v);
			}
			if (vy - 1 >= 1 && Map.getg()[spec][vx][vy-1]==1 || Map.getg()[spec][vx][vy-1]==3){
				i = job(vx,vy-1);
				slack(i,v);
			}
			vis[v] = false;
		}
	}
	
	public synchronized void slack(int i,int v){
		/*@ REQUIRES: None;
		@ MODIFIES: dis, vis, prev, q;
		@ EFFECTS: 进行顶点i和的松弛操作
		@ THREAD_REQUIRES: \locked(this);
		@ THREAD_EFFECTS: \locked();
		*/
		int dir_t1,dir_t2,ix,iy;
		boolean sign = false;
		if (dis[v]+1==dis[i]){
			dir_t1 = cal_dir(i,prev[i]);
			ix = (i-1) / N+1;
			iy = i % N;
			if (iy == 0)
				iy = N;	
			dir_t2 = cal_dir(i,v);
			if (Map.getflow(ix, iy, dir_t1) > Map.getflow(ix, iy, dir_t2)){
				sign = true;
				//System.out.println("YES"+"ix="+ix+" iy="+iy+"  dir_prev"+dir_t1+"flow: "+Map.getflow(ix, iy, dir_t1)
				//	+" dir_v"+dir_t2+ " flow="+Map.getflow(ix, iy, dir_t2));
				//RequestQueue.LogFile(cus_id).println("YES"+"ix="+ix+" iy="+iy+"  dir_prev"+dir_t1+"flow: "+Map.getflow(ix, iy, dir_t1)
					//+" dir_v"+dir_t2+ " flow="+Map.getflow(ix, iy, dir_t2));
			}
		}
		
		if (sign || dis[v] + 1 < dis[i]){// || (dis[v] + 1 == dis[i] && Map.getflow(ix, iy, dir_t)){
			dis[i] = dis[v] + 1;
			prev[i] = v;
			if (!vis[i]){
				q.offer(i);
				vis[i] = true;
			}	
		}
	}
	
	public synchronized void setstate(int state_t){
		/*@ REQUIRES:  None;
		@ MODIFIES: state;
		@ EFFECTS: state == state_t;
		@ THREAD_REQUIRES: \locked(this);
		@ THREAD_EFFECTS: \locked();
		*/
		state = state_t;
	}
	
	public synchronized boolean find(){
		/*@ REQUIRES: None;
		@ MODIFIES: None;
		@ EFFECTS: 如果是合法的方向且存在这条边 ==> \result == true
				   如果是合法非法或不存在这条边 ==> \result == false
		@ THREAD_REQUIRES: \locked(this);
		@ THREAD_EFFECTS: \locked();
		*/
		workxy();
		if (x >= 1 && x <= 80 && y >= 1 && y <= 80 && nx >= 1 && nx <= 80 && ny >= 1 && ny <= 80){
			if (dir == 0 && (Map.getg()[spec][x][y]==2 || Map.getg()[spec][x][y]==3))
				return true;
			if (dir == 1 && (Map.getg()[spec][nx][ny]==2 || Map.getg()[spec][nx][ny]==3))
				return true;
			if (dir == 2 && (Map.getg()[spec][x][y]==1 || Map.getg()[spec][x][y]==3))
				return true;
			if (dir == 3 && (Map.getg()[spec][nx][ny]==2 || Map.getg()[spec][nx][ny]==3))
				return true;
			if (dir == 4){
				//System.out.println("dir==4");
				return true;
			}
		}
		return false;
	}
	public synchronized void out_pos(){
		/*@ REQUIRES: None;
		@ MODIFIES: None;
		@ EFFECTS: 如果是合法的方向且存在这条边 ==> \result == true
				   如果是合法非法或不存在这条边 ==> \result == false
		@ THREAD_REQUIRES: \locked(this);
		@ THREAD_EFFECTS: \locked();
		*/
		workxy();//System.out.println("Taxi "+id + "Now At " + pos + " ( "+x+", "+y+")");//System.out.print(" ("+x+", "+y+") --->");
		////if (spec == 0)
			RequestQueue.LogFile(cus_id).print(state + " ("+x+", "+y+") --->");
	}
	public synchronized int job(int x,int y){
		/*@ REQUIRES: None;
		@ MODIFIES: None;
		@ EFFECTS: \result == 80*(x-1) + y;
		@ THREAD_REQUIRES: \locked(this);
		@ THREAD_EFFECTS: \locked();
		*/
		return 80*(x-1) + y;
	}
	public synchronized int cal_dir(int pos_from,int pos_to){//(int pos_last,int pos_to){
		/*@ REQUIRES: 1 <= pos <= 6400 && 1 <= pos_last <= 6400
		@ MODIFIES: None;
		@ EFFECTS: \result = pos_last关于的pos的方向，0代表下方、1代表右方、2代表左方、3代表上方
		@ THREAD_REQUIRES: \locked(this);
		@ THREAD_EFFECTS: \locked();
		*/
		int fromx,fromy,tox,toy;
		
		fromx = (pos_from-1) / N+1;
		fromy = pos_from % N;
		if (fromy == 0)
			fromy = N;
		
		tox = (pos_to-1) / N+1;
		toy = pos_to % N;
		if (toy == 0)
			toy = N;
		if (tox == fromx+1 && toy == fromy)//Car Down
			return 0;
		if (tox == fromx && toy == fromy+1)//Car Right
			return 1;
		if (tox == fromx && toy == fromy-1)//Car Left
			return 2;
		if (tox == fromx-1 && toy == fromy)//Car Up
			return 3;
		//System.out.printf("Cal_dir Error from (%d,%d) to (%d,%d)\n",fromx,fromy,tox,toy);
		//System.exit(0);
		return -1;
	}
	public synchronized boolean myexist(int s,int t){
	/*@ REQUIRES: 1 <= s <= 6400 && 1 <= t <= 6400
		@ MODIFIES: None;
		@ EFFECTS: \result = s和t是否存在边相连
		@ THREAD_REQUIRES: \locked(this);
		@ THREAD_EFFECTS: \locked();
		*/
		int sx, sy, tx, ty;
		sx = (s-1) / N+1;
		sy = s % N;
		if (sy == 0)
			sy = N;	
		
		tx = (t-1) / N+1;
		ty = t % N;
		if (ty == 0)
			ty = N;
		
//System.out.printf("HHHH from (%d,%d) to (%d,%d)\n",sx,sy,tx,ty);
		
		if (sx == tx && sy == ty+1){//s is right to t
			if (Map.getg()[spec][tx][ty] != 1 && Map.getg()[spec][tx][ty] != 3){
				//System.out.printf("Miss from (%d,%d) to (%d,%d)\n",sx,sy,tx,ty);
				return false;
			}
			return true;
		}
		if (sx == tx && sy == ty-1){//s is left to t
			if (Map.getg()[spec][sx][sy] != 1 && Map.getg()[spec][sx][sy] != 3){
				//System.out.printf("Miss from (%d,%d) to (%d,%d)\n",sx,sy,tx,ty);
				return false;
			}
			return true;
		}
		if (sx == tx+1 && sy == ty){//s is down to t
			if (Map.getg()[spec][tx][ty] != 2 && Map.getg()[spec][tx][ty] != 3){
				//System.out.printf("Miss from (%d,%d) to (%d,%d)\n",sx,sy,tx,ty);
				return false;
			}
			return true;
		}
		if (sx == tx-1 && sy == ty){//s is up to t
			if (Map.getg()[spec][sx][sy] != 2 && Map.getg()[spec][sx][sy] != 3){
				//System.out.printf("Miss from (%d,%d) to (%d,%d)\n",sx,sy,tx,ty);
				return false;
			}
			return true;
		}
		//System.out.println("ERR!");
		return false;
	}
	/*********************************************************************************************
	  						下面的信息为指导书的三(5）要求的接口
	 *********************************************************************************************/
	public synchronized int getx(){//返回对象的x坐标
		/*@ REQUIRES: None;
		@ MODIFIES: x, y;
		@ EFFECTS: \result == x;
		@ THREAD_REQUIRES: \locked(this);
		@ THREAD_EFFECTS: \locked();
		*/
		workxy();
		return x;
	}
	public synchronized int gety(){//返回对象的y坐标
		/*@ REQUIRES: None;
		@ MODIFIES: x, y;
		@ EFFECTS: \result == y;
		@ THREAD_REQUIRES: \locked(this);
		@ THREAD_EFFECTS: \locked();
		*/
		workxy();
		return y;
	}
	public long getTime(){//返回当前的时刻
		/*@ REQUIRES: None;
		@ MODIFIES: None;
		@ EFFECTS: \result == new Date().getTime();
		*/
		return (new Date().getTime());
	}
	public ArrayList<Car> state_req(int sta){//返回处于指定状态的出租车对象
		/*@ REQUIRES: None;
		@ MODIFIES: state_v;
		@ EFFECTS: (\all int i;  1 <= i <= 100);
				   state_v.add(c_all[i]);
		*/
		ArrayList<Car> state_v = new ArrayList<Car>();
		for (int i = 1; i <= 100; ++i){
			if (c_all[i].getstate() == sta){
				state_v.add(c_all[i]);
			}
		}
		return state_v;
	}
}
