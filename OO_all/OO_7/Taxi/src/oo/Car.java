package oo;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import oo.gv;

public class Car extends Thread {
	final static int N = 80;
	final static int xx[] = {1,-1,0,0,0};
	final static int yy[] = {0,0,1,-1,0};
	private int id, credit, pos, cus_id, dir, nx, ny, pla, vx, vy;
	private int x, y, cnt, state;
	//private volatile int state;//private  int  state;
	Queue<Integer> q = new LinkedList<Integer>();
	private int dis[] = new int[6410];
	private boolean vis[] = new boolean[6410];
	private int prev[] = new int[6410];
	private int destination;
	private long wait_start, now_t;
	private Car[] c_all;
	public Car(int id_t, Car[] c_all_t){
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
		destination = -1;
	}
	
	public void run(){
		while (true){
			workxy();
			if (state == 3){//接单状态			
				Main.gui.SetTaxiStatus(id, new Point(x-1,y-1), state);			

				if (pos == destination){//到达用户的地方了
					//System.out.println("Customer "+cus_id+" Taxi "+ id+ " Come!");
					//RequestQueue.LogFile(cus_id).println("Customer "+cus_id+" Taxi "+ id+ " Come!");
					setstate(0);//state = 0;//进入停止运行状态1秒
					Main.gui.SetTaxiStatus(id, new Point(x-1,y-1), state);
					try {
						sleep(1000);//Thread.sleep(777);
					} catch (Exception e) {}
					setstate(1);//state = 1;//停止完后进入服务状态//System.out.println("To Go to the dest!!!");//RequestQueue.LogFile(cus_id).println("To Go to the dest!!!");
					Main.gui.SetTaxiStatus(id, new Point(x-1,y-1), state);
					spfa_2(pla);
					//System.out.println("请求 "+cus_id+" 的行驶路线：");
					RequestQueue.LogFile(cus_id).println("请求 "+cus_id+" 的行驶路线：");
					continue;
				}
				try {
					sleep(200);//Thread.sleep(777);
				} catch (Exception e) {}
				setpos(prev[pos]);//pos = prev[pos];
			}else if (state == 1){//处于服务状态
				cnt++;
				out_pos();//System.out.println("UUUU"+id);
				
				workxy();				
				Main.gui.SetTaxiStatus(id, new Point(x-1,y-1), state);
				
				if (cnt % 7 ==0){
					//System.out.println("");
					RequestQueue.LogFile(cus_id).println("");
				}
				if (pos == pla){
					//System.out.println("Customer "+cus_id+" Arrived By Taxi "+ id);
					//System.out.println("到达目的地！");
					RequestQueue.LogFile(cus_id).println("到达目的地！");
					credit += 3;
					setstate(0);//state = 0;//停止状态
					Main.gui.SetTaxiStatus(id, new Point(x-1,y-1), state);
					try {
						sleep(1000);//Thread.sleep(777);
					} catch (Exception e) {}
					setstate(2);//state = 2;//进入等待状态
					Main.gui.SetTaxiStatus(id, new Point(x-1,y-1), state);
					wait_start = new Date().getTime();
					continue;
				}
				try {
					sleep(200);//Thread.sleep(777);
				} catch (Exception e) {}
				setpos(prev[pos]);//pos = prev[pos];
			}else{//处于等待状态，每次随机一个方向到处跑
				workxy();					
				Main.gui.SetTaxiStatus(id, new Point(x-1,y-1), state);
				
				now_t = new Date().getTime();
				if (now_t - wait_start >= 20000){
					setstate(0);//state = 0;//停止运行
					Main.gui.SetTaxiStatus(id, new Point(x-1,y-1), state);
					try {
						sleep(1000);//Thread.sleep(777);
					} catch (Exception e) {}
					setstate(2);//state = 2;//进入等待状态
					Main.gui.SetTaxiStatus(id, new Point(x-1,y-1), state);
					wait_start = new Date().getTime();
				}
				
				Random rand = new Random();
				int ccc = 0;
				ArrayList<Integer> ttt = new ArrayList<Integer>();
				ttt.clear();
				if (Map.getg()[x][y] == 2 || Map.getg()[x][y] == 3){
					ttt.add(0);
					ccc++;
				}
				if (Map.getg()[x][y] == 1 || Map.getg()[x][y] == 3){
					ttt.add(2);
					ccc++;
				}
				if (x-1>=1&&Map.getg()[x-1][y] == 2 || Map.getg()[x-1][y] == 3){
					ttt.add(1);
					ccc++;
				}
				if (y-1>=1&&Map.getg()[x][y-1] == 1 || Map.getg()[x][y-1] == 3){
					ttt.add(3);
					ccc++;
				}	
				if (ccc > 0)
					dir = ttt.get(rand.nextInt(ccc));
				else 
					dir =  4;
				//System.out.println("dir"+dir);
				try {
					sleep(200);//Thread.sleep(777);
				} catch (Exception e) {}
				//x = x+xx[dir];
				//y = y+yy[dir];
				setxy(x+xx[dir], y+yy[dir]);
				setpos(80*(x-1)+y);//pos = 80*(x-1) + y;
			}
		}
	}
	public synchronized void setpos(int pos_t){
		pos = pos_t;
	}
	public synchronized void setxy(int x_t,int y_t){
		x = x_t;
		y = y_t;
		Main.gui.SetTaxiStatus(id, new Point(x-1,y-1), state);
	}
	public synchronized void workxy(){
		x = (pos-1) / N+1;
		y = pos % N;
		if (y == 0)
			y = N;	
		Main.gui.SetTaxiStatus(id, new Point(x-1,y-1), state);
	}
	
	public synchronized int getstate(){
		return state;
	}
	public synchronized int getcredit(){
		return credit;
	}
	public synchronized void spfa(int cn_t,int u,int place_t){//接到一个单，是编号为cn_t的请求，起始点是u，目的地是place_t
		credit++;//接到单 信用值加一
		cus_id = cn_t;
		destination = u;
		pla = place_t;
		spfa_2(u);
		setstate(3);//state = 3;//接单状态，前往用户的位置
		Main.gui.SetTaxiStatus(id, new Point(x-1,y-1), state);
	}
	public synchronized void spfa_2(int u){//起始点是u
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
			if (vy + 1 <= 80 && Map.getg()[vx][vy]==1 || Map.getg()[vx][vy]==3){
				i = job(vx,vy+1);
				slack(i,v);
			}
			if (vx + 1 <= 80 && Map.getg()[vx][vy]==2 || Map.getg()[vx][vy]==3){
				i = job(vx+1,vy);
				slack(i,v);
			}
			if (vx - 1 >= 1 && Map.getg()[vx-1][vy]==2 || Map.getg()[vx-1][vy]==3){
				i = job(vx-1,vy);
				slack(i,v);
			}
			if (vy - 1 >= 1 && Map.getg()[vx][vy-1]==1 || Map.getg()[vx][vy-1]==3){
				i = job(vx,vy-1);
				slack(i,v);
			}
			vis[v] = false;
		}
	}
	
	public synchronized void slack(int i,int v){
		if (dis[v] + 1 < dis[i]){
			dis[i] = dis[v] + 1;
			prev[i] = v;
			if (!vis[i]){
				q.offer(i);
				vis[i] = true;
			}	
		}
	}
	
	public synchronized void setstate(int state_t){
		state = state_t;
	}
	
	public synchronized boolean find(){
		//final static int xx[] = {1,-1,0,0,0};
		//final static int yy[] = {0,0,1,-1,0};
		workxy();
		if (x >= 1 && x <= 80 && y >= 1 && y <= 80 && nx >= 1 && nx <= 80 && ny >= 1 && ny <= 80){
			if (dir == 0 && (Map.getg()[x][y]==2 || Map.getg()[x][y]==3))
				return true;
			if (dir == 1 && (Map.getg()[nx][ny]==2 || Map.getg()[nx][ny]==3))
				return true;
			if (dir == 2 && (Map.getg()[x][y]==1 || Map.getg()[x][y]==3))
				return true;
			if (dir == 3 && (Map.getg()[nx][ny]==2 || Map.getg()[nx][ny]==3))
				return true;
			if (dir == 4)
				return true;
		}
		return false;
	}
	public synchronized void out_pos(){
		workxy();
		//System.out.println("Taxi "+id + "Now At " + pos + " ( "+x+", "+y+")");
		//System.out.print(" ("+x+", "+y+") --->");
		RequestQueue.LogFile(cus_id).print(state + " ("+x+", "+y+") --->");
	}
	public synchronized int job(int x,int y){
		return 80*(x-1) + y;
	}
	
	
	/*********************************************************************************************
	  						下面的信息为指导书的三（5）要求的接口
	 *********************************************************************************************/
	public synchronized int getx(){//返回对象的x坐标
		workxy();
		return x;
	}
	public synchronized int gety(){//返回对象的y坐标
		workxy();
		return y;
	}
	public long getTime(){//返回当前的时刻
		return (new Date().getTime());
	}
	public ArrayList<Car> state_req(int sta){//返回处于指定状态的出租车对象
		ArrayList<Car> state_v = new ArrayList<Car>();
		for (int i = 1; i <= 100; ++i){
			if (c_all[i].getstate() == sta){
				state_v.add(c_all[i]);
			}
		}
		return state_v;
	}
}
