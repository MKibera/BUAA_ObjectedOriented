package elevator;

import java.text.DecimalFormat;

interface ElevatorJob {
	public void up_1();
	public void down_1();
	public void open();
}

public class Elevator implements ElevatorJob {
	//Overview: 电梯类，负责电梯的运动
	final static String[] s = {"UP","DOWN","STILL"};
	private static int floor;
	private static double time;
	private static String dir;
	public Elevator(){
		/*@ Requires: None;
		@ Modifies: floor, time;
		@ Effects: floor == 1;
				   time == 0;
		*/
		floor = 1;
		time = 0;
	}
	
	public boolean repOK(){
		/*@ REQUIRES: None;
		@ MODIFIES: None;
		@ Effects: \result == invariant(this);
		@ Invariant(this): 恒为true;
		*/
		return true;
	}
	
	public void setDir(String d){
		/*@ Requires: None;
		@ Modifies: dir;
		@ Effects: dir == d;
		*/
		dir = d;
	}
	public void setTime(double t){
		/*@ Requires: None;
		@ Modifies: time;
		@ Effects: time == t;
		*/
		time = t;
	}
	public int getFloor(){
		/*@ Requires: None;
		@ Modifies: None;
		@ Effects: \result == floor;
		*/
		return floor;
	}
	public double getTime(){
		/*@ Requires: None;
		@ Modifies: None;
		@ Effects: \result == time;
		*/
		return time;
	}	
	
	public void up_1(){
		/*@ Requires: None;
		@ Modifies: floor, time;
		@ Effects: floor == \old(floor)+1;
				   time == \old(time)+0.5;
		*/
		floor++;
		time = time+0.5;
	}
	public void down_1() {
		/*@ Requires: None;
		@ Modifies: floor, time;
		@ Effects: floor == \old(floor)-1;
				   time == \old(time)-0.5;
		*/
		floor--;
		time += 0.5;
	}
	public void open(){
		/*@ Requires: None;
		@ Modifies: time;
		@ Effects: time == \old(time)+1;
		*/
		time++;
	}
	public String toString(){
		/*@ Requires: None;
		@ Modifies: None;
		@ Effects: 返回电梯的时间和楼层信息的字符串
		*/
		//System.out.println("("+elv.getFloor()+","+s[now_dir]+","+elv.getTime()+")");
		String st;  
        DecimalFormat decimalFormat = new DecimalFormat("#0.0");//格式化设置  
		st = "("+floor+","+dir+","+decimalFormat.format(time)+")";
		return st;
	}
}
