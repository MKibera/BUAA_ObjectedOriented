package oo;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Random;

public class Light extends Thread {
	//新增的红绿灯线程类
	public volatile int sta;
	private int wt;
	private volatile long t0, t, t_s;
	public volatile long t_last,t_next;
	public int col[][];
	public Light(){
		/*@ REQUIRES: None;
		@ MODIFIES: this;
		@ EFFECTS: wt = Main.wt;
				   sta = 1;
		*/
		wt = Main.wt;
		sta = 1;//代表最初始的是上下方向的是绿灯		
		col = new int[85][85];
		Random rand = new Random();
		for (int i = 1; i <= 80; ++i)
		for (int j = 1; j <= 80; ++j){
			col[i][j] = rand.nextInt(2);
		}
	}
	public boolean repOK(){
		/*@ REQUIRES: None;
		@ MODIFIES: None;
		@ Effects: \result == invariant(this);
		@ Invariant(this): wt >= 50 && wt <= 100 && (sta == 0 || sta == 1) 
							&& \all i in 1 to 80 \all j in 1 to 80 ==> 
							(col[i][j] == 0 && col[i][j] == 1);
		*/
		if (wt<50||wt>100)
			return false;
		if (sta!=0 && sta!=1)
			return false;
		for (int i = 1; i <= 80; ++i)
			for (int j = 1; j <= 80; ++j)
				if (col[i][j] < 0 || col[i][j] > 1)
					return false;
				
		return true;
	}
	public void run(){
		/*@ REQUIRES: None;
		@ MODIFIES: sta, t0, t, t_s, t_last, t_next;
		@ EFFECTS: normal_behavior
				   按周期变化出红绿灯的状态值sta
				   sleep出现异常 ==> exceptional_behavior (e);
		*/
		t0 = -1;
		while (true){
			t = new Date().getTime();
			t_last = t;
			//System.out.println("^^"+t);
			if (t0==-1){
				t0 = t;
				t_s = wt;
			}else
				t_s = wt-(t-t0) % wt;
			if (t_s==0)
				t_s = wt;
	//System.out.println("delay time"+t_s);
			t_next = t+t_s;
	//System.out.println("time_next:"+t_next);
			try { 
				sleep(t_s);
				//sleep(wt);
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			sta = 1-sta;
			for (int i = 1; i <= 80; ++i)
			for (int j = 1; j <= 80; ++j){
				boolean tmp = Map.getw()[i][j];
				if (tmp){
					if (sta!=col[i][j])//if (Main.l.sta==Main.l.col[x][y]){//上下方向是红灯
						Main.gui.SetLightStatus(new Point(i-1, j-1), 2);//2 东西方向为红灯(南北向绿灯)
					else
						Main.gui.SetLightStatus(new Point(i-1, j-1), 1);//1 东西方向为绿灯(南北向红灯)
				}else{
					Main.gui.SetLightStatus(new Point(i-1, j-1), 0);//0 没有红绿灯
				}
			}	
	//System.out.println("Actual Time:"+new Date().getTime());
			//System.out.printf("Light Now %d\n", sta);
		}
	}
}