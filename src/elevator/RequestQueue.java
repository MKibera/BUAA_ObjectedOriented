package elevator;

import java.util.*;

public class RequestQueue {
	//private boolean flag;//当前读入的这一行是否合法
	//private int minFloor,maxFloor;//表示读入的楼层允许的最大值和最小值
	private int cnt;
	private ArrayList<Integer> elvid = new ArrayList<Integer>();//
	private ArrayList<Integer> floor = new ArrayList<Integer>();//存储所有合法的楼层信息的Arraylist
	private ArrayList<Integer> type = new ArrayList<Integer>();//存储所有合法的类型信息的Arraylist
	private ArrayList<Integer> updown = new ArrayList<Integer>();//存储所有合法的上下信息的Arraylist
	private ArrayList<Long> time = new ArrayList<Long>();
	//private boolean[] used = new boolean[100010];
	private ArrayList<Boolean> used = new ArrayList<Boolean>();
	
	RequestQueue(int min_f, int max_f){//构造函数，初始化minFloor,maxFloor
		//minFloor = min_f;
		//maxFloor = max_f;
		cnt = 0;
	}
	
	public synchronized void add_element(int flo,int id,int typ,int upd, long tim){
		cnt++;
		//System.out.println("cnt"+cnt);
		floor.add(flo);
		elvid.add(id);
		type.add(typ);
		updown.add(upd);
		used.add(true);
		time.add(tim);
		notifyAll();
	}	
	
	public synchronized int getCnt(){
		//System.out.println(cnt);
		return cnt;
	}
	
	public int Busy(){
		int tot = 0;
		for (int i = 0; i < cnt; ++i){
			if (used.get(i)){
				tot++;
			}
		}
		return tot;
	}

	public synchronized int getFloor(int i){
		if (!used.get(i))
			return -1;
		return floor.get(i);
	}
	
	public synchronized long getTime(int i){
		if (!used.get(i))
			return -1;
		return time.get(i);
	}
	
	public synchronized int getType(int i){
		if (!used.get(i))
			return -1;
		return type.get(i);
	}
	
	public synchronized int getUpdown(int i){
		if (!used.get(i))
			return -1;
		return updown.get(i);
	}
	
	public synchronized int getElvid(int i){
		if (!used.get(i))
			return -1;
		return elvid.get(i);
	}
	
	public synchronized void done(int i){
		//System.out.println("Over"+i);
		used.set(i, false);
	}
}
