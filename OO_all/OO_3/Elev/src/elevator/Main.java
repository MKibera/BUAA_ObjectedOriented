package elevator;

public class Main {
	final static int MINFLOOR = 1;
	final static int MAXFLOOR = 10;
	public static void main(String[] args) {
	  try{
		Floor floor = new Floor(MINFLOOR,MAXFLOOR);//构造一个楼层类的对象，并把允许的最大值传入一个调度类的对象。
		RequestQueue rq = new RequestQueue(floor.getMin(),floor.getMax());
		rq.work();
		Dispatch disp = new Dispatch(rq.getArray_time(),rq.getArray_floor(),
				rq.getArray_type(),rq.getArray_updown());
		disp.work();
	}
	  catch (Exception e){
		  System.exit(1);
	  }
	}
}
