package oo;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Map {
	private static int g[][][] = new int[3][85][85];
	private static int f[][][] = new int [85][85][6];
	private static boolean w[][] = new boolean[85][85];
	private static int h[][] = new int [85][85];
	public int tot;
	
	public boolean repOK(){
		/*@ REQUIRES: None;
		@ MODIFIES: None;
		@ Effects: 检查合法性的工作在work函数里做了，因此这里恒为true.
		@ Invariant(this): None;
		*/
		return true;
	}
	
	public void work(String s){
		/*@ REQUIRES: (读入的地图信息没有问题);
		@ MODIFIES: g;
		@ EFFECTS: normal bejavior
				   扫描过程中出现非法访问操作 ==> exceptional_behavior()
		*/
		int n, x;
		File file = new File(s);
		InputStreamReader isr;
        BufferedReader buff;
        try {
        	InputStream instr = new FileInputStream(file);
            isr = new InputStreamReader(instr);
            buff = new BufferedReader(isr);
            String str;
            n = 1;
            while ((str = buff.readLine()) != null)
            {
            	str = str.replaceAll(" ", "");
            	str = str.replaceAll("\t", "");
            	int l;
            	l = str.length();
            	if (l != 80){
            		System.out.println("Map Invalid!!!");
            		System.exit(0);
            	}
            	//System.out.println(str);
            	for (int i = 0; i < 80; ++i){
            		x = str.charAt(i) - '0';
            		if (x < 0 || x > 3){
            			System.out.println("Map Invalid!!!");
            			System.exit(0);
            		}
            		g[0][n][i+1] = g[1][n][i+1] =  x;
            	}
            	if (n > 80){
                	System.out.println("Map Invalid!!!");
        			System.exit(0);
                }
            	n++;
            }
            System.out.println("Map Is Okay");
        }
        catch (Exception e){}
	}
	
	public void work2(String s){
		/*@ REQUIRES: (读入的地图信息没有问题);
		@ MODIFIES: w, h;
		@ EFFECTS: normal bejavior
				   扫描过程中出现非法访问操作 ==> exceptional_behavior()
		*/
		//Arrays.fill(h, -1);
		int n, x;
		boolean sign;
		File file = new File(s);
		InputStreamReader isr;
        BufferedReader buff;
        try {
        	InputStream instr = new FileInputStream(file);
            isr = new InputStreamReader(instr);
            buff = new BufferedReader(isr);
            String str;
            n = 1;
            tot = 0;
            while ((str = buff.readLine()) != null)
            {
            	str = str.replaceAll(" ", "");
            	str = str.replaceAll("\t", "");
            	int l;
            	l = str.length();
            	if (l != 80){
            		System.out.println("Light Invalid!!!");
          System.exit(0);
            	}
            	//System.out.println(str);
            	for (int i = 0; i < 80; ++i){
            		x = str.charAt(i) - '0';
            		if (x < 0 || x > 1){
            			System.out.println("Light Invalid!!!");
          System.exit(0);
            		}
            		w[n][i+1] = false;
            		h[n][i+1] = -1;
            		if (x==1){
            			sign = false;
            			if (n==1&&i+1==1){
            				if (g[0][n][i+1]==3)
            					sign = true;
            			}else if (n==80&&i+1==80){
            				if ((g[0][n][i]==1||g[0][n][i]==3)&&(g[0][n-1][i+1]==2||g[0][n-1][i+1]==3))
            					sign = true;
            			}else if (n==1&&i+1==80){
            				if ((g[0][n][i+1]==2)&&(g[0][n][i]==1||g[0][n][i]==3))
            					sign = true;
            			}else if (n==80&&i+1==1){
            				if ((g[0][n][i+1]==1)&&(g[0][n-1][i+1]==2||g[0][n-1][i+1]==3))
                				sign = true;
                		}else if (n==1){
            				if ((g[0][n][i+1]==3)&&(g[0][n][i]==1||g[0][n][i]==3))
                				sign = true;
            			}else if (i+1==1){
            				if ((g[0][n][i+1]==3)&&(g[0][n-1][i+1]==2||g[0][n-1][i+1]==3))
                				sign = true;
            			}else if (n==80){
            				if ((g[0][n][i+1]==1)&&(g[0][n][i]==1||g[0][n][i]==3)&&(g[0][n-1][i+1]==2||g[0][n-1][i+1]==3))
                				sign = true;
            			}else if (i+1==80){
            				if ((g[0][n][i+1]==2)&&(g[0][n][i]==1||g[0][n][i]==3)&&(g[0][n-1][i+1]==2||g[0][n-1][i+1]==3))
                				sign = true;
            			}else if ((g[0][n][i+1]==3)&&(g[0][n][i]==1||g[0][n][i]==3)&&(g[0][n-1][i+1]==2||g[0][n-1][i+1]==3)){
            				sign = true;
            			}
            			if (sign){
            				w[n][i+1] = true;
            				h[n][i+1] = (++tot);
            				
            			}else{
     //System.out.println("Light Not Match Crossroad ("+n +" , "+(i+1)+")");
            			}
            		}
            			
            	}
            	if (n > 80){
                	System.out.println("Light Invalid!!!");
        System.exit(0);
                }
            	n++;
            }
            System.out.println("Light Is Okay");
        }
        catch (Exception e){}
	}
	
	public synchronized static int[][][] getg(){
		/*@ REQUIRES: None;
		@ MODIFIES: None;
		@ EFFECTS: \result = g;
		@ THREAD_REQUIRES: \locked(this);
		@ THREAD_EFFECTS: \locked();
		*/
		return g;
	}
	
	public synchronized static boolean[][] getw(){
		/*@ REQUIRES: None;
		@ MODIFIES: None;
		@ EFFECTS: \result = w;
		@ THREAD_REQUIRES: \locked(this);
		@ THREAD_EFFECTS: \locked();
		*/
		return w;
	}
	
	public synchronized static void change(int x,int y,int z){
		/*@ REQUIRES: (g[x][y] exists);
		@ MODIFIES: None;
		@ EFFECTS: g[x][y] = z;
		@ THREAD_REQUIRES: \locked(this);
		@ THREAD_EFFECTS: \locked();
		*/
		//System.out.printf("g[%d][%d] changes from %d to %d\n",x,y,g[x][y],z);
		g[0][x][y] = z;
	}
	public synchronized static void addflow(int x,int y,int dir,int type){
		/*@ REQUIRES: (f[x][y][dir] exists && 0 <= dir <= 4);
		@ MODIFIES: None;
		@ EFFECTS: f[x][y][dir]++;
		@ THREAD_REQUIRES: \locked(this);
		@ THREAD_EFFECTS: \locked();
		*/
		if (dir<0)
			return;
		if (check(x,y,dir,type))
			f[x][y][dir]++;
	}
	public synchronized static boolean check(int x,int y,int dir,int type){
		if (dir==0){
			if (g[0][x][y] != 2 && g[0][x][y] != 3){
	//System.out.println("^^^&*(CLOSED"+x+" "+y+" "+dir+" "+g[0][x][y]+" "+g[1][x][y]+" "+type);
				return false;
			}
		}
		if (dir==1){
			if (g[0][x][y] != 1 && g[0][x][y] != 3){
	//System.out.println("@@@@&*(CLOSED"+x+" "+y+" "+dir+" "+g[0][x][y]+" "+g[1][x][y]+type);
				return false;
			}
		}
		if (dir==2){//LEFT
			if (y-1>=1&&g[0][x][y-1] != 1 && g[0][x][y-1] != 3){
	//System.out.println("$$$$&*(CLOSED"+x+" "+y+" "+dir+" "+g[0][x][y-1]+" "+g[1][x][y-1]+type);
				return false;
			}
		}
		if (dir==3){//Up
			if (x-1>=1&&g[0][x-1][y] != 2 && g[0][x-1][y] != 3){
	//System.out.println("####&*(CLOSED"+x+" "+y+" "+dir+" "+g[0][x-1][y]+" "+g[1][x-1][y]+type);
				return false;
			}
		}
		return true;
	}
	public synchronized static void subflow(int x,int y,int dir,int type){
		/*@ REQUIRES: (f[x][y][dir] exists && f[x][y][dir] > 0 && 0 <= dir <= 4);
		@ MODIFIES: None;
		@ EFFECTS: f[x][y][dir]--;
		@ THREAD_REQUIRES: \locked(this);
		@ THREAD_EFFECTS: \locked();
		*/
		if (dir<0)
			return;
		if (check(x,y,dir,type))
			f[x][y][dir]--;
		if (f[x][y][dir] < 0){
			//System.out.println("WRONG: Subflow"+x+y+dir);
			//System.exit(0);
		}
	}
	public synchronized static int getflow(int x,int y,int dir){
		/*@ REQUIRES: (f[x][y][dir] exists);
		@ MODIFIES: None;
		@ EFFECTS: \result == f[x][y][dir];
		@ THREAD_REQUIRES: \locked(this);
		@ THREAD_EFFECTS: \locked();
		*/
		return f[x][y][dir];
	}
}
