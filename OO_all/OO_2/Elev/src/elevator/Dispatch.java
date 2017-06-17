package elevator;

public class Dispatch {
	void work(int min_f,int max_f){//构造一个请求队列的对象。并把其读入的合法信息放入电梯类进行计算
		RequestQueue rq = new RequestQueue(min_f,max_f);
		rq.work();
		Elevator elv = new Elevator(rq.getArray_time(),rq.getArray_floor(),
									rq.getArray_type(),rq.getArray_updown());
		elv.work();
	}
}
