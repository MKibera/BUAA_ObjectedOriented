package elevator;

import java.util.Arrays;

public class Light {
	private static boolean[] upp = new boolean[22];
	private static boolean[] dow = new boolean[22];
	private static boolean[][] fff = new boolean[5][22];
	
	public Light(){
		Arrays.fill(upp, false);
		Arrays.fill(dow, false);
		Arrays.fill(fff, false);
	}
	public synchronized static boolean getUpp(int i){
		return upp[i];
	}
	
	public synchronized static boolean getDow(int i){
		return dow[i];
	}
	
	public synchronized static boolean getF(int i, int j){
		return fff[i][j];
	}
	
	public synchronized static void setF(int i,int j,boolean t){
		fff[i][j] = t;
	}
	
	
	public synchronized static void setDow(int i,boolean t){
		dow[i] = t;
	}
	
	public synchronized static void setUpp(int i,boolean t){
		upp[i] = t;
	}
}
