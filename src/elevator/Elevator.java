package elevator;


import java.text.DecimalFormat;
import java.util.Date;

interface ElevatorJob {
	public void up_1();
	public void down_1();
	//public void open(int free);
	public void open();
}

public class Elevator implements ElevatorJob {
	final static String[] s = {"UP","DOWN","STILL"};
	private int number;
	private  int floor;
	private double time;
	private long startTime;
	private int state;//1:UP,0:STILL,-1:DOWN
	private int flo, id, typ, upd, tot;//目标的下一个请求的具体内容
	private long tim;
	public Elevator(int number_t,long startTime_t){
		//startTime = RequestAdd.getStartTime();
		number = number_t;
		flo = id = typ = upd = -1;
		tot = 0;
		floor = 1;
		time = 0;
		state = -2;//处于IDLE状态
	}
	
	public synchronized void reqSet(int flo_t,int id_t,int typ_t,int upd_t,long tim_t)//判断这个指令是不是空，然后求出状态啥的
	{
		flo = flo_t;
		id = id_t;
		typ = typ_t;
		upd = upd_t;
		tim = tim_t;
		if (flo > floor) 
			state = 1;
		else if (flo < floor) 
			state = -1;
		else state = 0;
	}
	
	public synchronized void work()//移动一层
	{
		if (flo == -1)
			return;
		//System.out.println("GOAL"+flo+"NOW"+floor);
		if (flo < floor) {//目标楼层小于当前楼层，下降
			down_1();
			return ;
		} 
		if (flo == floor) {//静止不动
			//stop();
			state = 0;
			flo = id = typ = upd = -1;
			return ;
		} 
		up_1();
		return ;
	}	
	
	public  int getState(){
		return state;
	}
	public synchronized int getTot(){
		return tot;
	}
	public synchronized int getReqF(){
		return flo;
	}
	public synchronized int getReqT(){
		return typ;
	}
	public synchronized int getReqU(){
		return upd;
	}
	public synchronized int getReqI(){
		return id;
	}
	
	public synchronized  int getFloor(){
		return floor;
	}
	public synchronized double getTime(){
		return time;
	}	
	
	//！！！！！！！！！！！！！！
	/*public void stop(){
		state = 0;
		//flo = id = typ = upd = -1;
	}*/
	
	public synchronized void up_1(){
		try{ 
			tot++;
			state = 1;
			floor++;
			time = time+0.5;
			Thread.sleep(deviation(3000)); 
		} 
		catch(Exception e){}
	}
	public synchronized void down_1() {
		try{ 
			tot++;
			state = -1;
			floor--;
			time += 0.5;
			Thread.sleep(deviation(3000)); 
		} 
		catch(Exception e){}
	}
	/*public  void open(int free){//0:没有继续的任务了，状态变为IDLE; 否则保持不变
		try{ 
			time++;
			//doorop = true;
			if (free==0)
				state = -2;
			Thread.sleep(1000*6);
		} 
		catch(Exception e){}
		
	}*/
	public void open(){//0:没有继续的任务了，状态变为IDLE; 否则保持不变
		try{ 
			time++;
			//state = 0;
			Thread.sleep(deviation(6000)); 
		} 
		catch(Exception e){}
	}
	public synchronized void setState(){
		//state = 0;
		if (flo > floor) 
			state = 1;
		else if (flo < floor) 
			state = -1;
		else state = 0;
	}
	public String toString(int flo_t){//（#电梯, 楼层, STILL, 累积运动量, t）
		startTime = RequestAdd.getStartTime();
		String st;  
		final String[] s = {"DOWN","STILL","UP"};
		int s_t = upd + 1;
		DecimalFormat decimalFormat = new DecimalFormat("#0.0");//格式化设置 
		if (typ == 1){//FR
			//System.out.print("[FR,"+flo+","+s[s_t]+"]");
			st = "[FR,"+flo_t+","+s[s_t]+","+decimalFormat.format((tim-startTime)/1000.0)+"]";
		}else{
			//System.out.print("[ER,#"+id+","+flo+"]");//(ER,#1,4)
			st = "[ER,#"+id+","+flo_t+","+decimalFormat.format((tim-startTime)/1000.0)+"]";
		}
		s_t = state+1;
		st += " / (#"+number+","+floor+","+s[s_t]+","+tot+","+decimalFormat.format(nowTime())+")";
		return st;
	}
	public double nowTime(){
		startTime = RequestAdd.getStartTime();
		return (new Date().getTime()-startTime)/1000.0;
	}
	public long deviation(long k){
		startTime = RequestAdd.getStartTime();
		return k - (new Date().getTime()-startTime) % k;
	}
}
