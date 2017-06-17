package elevator;

import java.text.DecimalFormat;

interface ElevatorJob {
	public void up_1();
	public void down_1();
	public void open();
}

public class Elevator implements ElevatorJob {
	final static String[] s = {"UP","DOWN","STILL"};
	private static int floor;
	private static double time;
	private static String dir;
	public Elevator(){
		floor = 1;
		time = 0;
	}
	
	public void setDir(String d){
		dir = d;
	}
	public void setTime(double t){
		time = t;
	}
	public int getFloor(){
		return floor;
	}
	public double getTime(){
		return time;
	}	
	
	public void up_1(){
		floor++;
		time = time+0.5;
	}
	public void down_1() {
		floor--;
		time += 0.5;
	}
	public void open(){
		time++;
	}
	public String toString(){
		//System.out.println("("+elv.getFloor()+","+s[now_dir]+","+elv.getTime()+")");
		String st;  
        DecimalFormat decimalFormat = new DecimalFormat("#0.0");//格式化设置  
		st = "("+floor+","+dir+","+decimalFormat.format(time)+")";
		return st;
	}
}
