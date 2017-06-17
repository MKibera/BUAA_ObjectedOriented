package elevator;

public class Main {
	final static int MINFLOOR = 1;
	final static int MAXFLOOR = 10;
	public static void main(String[] args) {
	  try{
		Floor floor = new Floor(MINFLOOR,MAXFLOOR);//构造一个楼层类的对象，并把允许的最大值传入一个调度类的对象。
		Dispatch disp = new Dispatch();
		disp.work(floor.getMin(),floor.getMax());
	}
	  catch (Exception e){
		  System.exit(1);
	  }
	}
}
