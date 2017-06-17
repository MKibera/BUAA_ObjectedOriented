package elevator;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ElevatorTest {
	private static int cnt = 0;
	RequestQueue rq;
	Request req;
	Dispatch disp;
	
	@BeforeClass
	public static void setUpBeforeClass() {
		System.out.println("/**********ElevatorTest************/");
	}

	@Before
	public void before() {
		rq = new RequestQueue(1,10);
		cnt=cnt+1;
		System.out.printf("-----------test %d------------\n",cnt);
	}
	
	@After  
    public void after() {  
		rq = null;
		disp = null;
        System.out.println("-----------------------------");  
    }  
	
	@Test
	public void test() {
		
		req = new Request("(FR,1,UP)",1,10);
		//assertEquals(true, req.getsign());
		rq.rq_add(req);
		assertEquals(0, rq.getcnt());//assert rq.getcnt() == 0;
		
		req = new Request("(FR,1,UP,0)",1,10);
		//assertEquals(true, req.getsign());
		rq.rq_add(req);
		assertEquals(1, rq.getcnt());//assert rq.getcnt() == 0;
		
		
		req = new Request("(FR,7,UP,2)",1,10);
		rq.rq_add(req);
		assertEquals(2, rq.getcnt());//assert rq.getcnt() == 0;
		
		req = new Request("(FR,7,UP,1)",1,10);
		rq.rq_add(req);
		assertEquals(2, rq.getcnt());//assert rq.getcnt() == 0;
		
		req = new Request("(FR,4,UP,3)",1,10);
		rq.rq_add(req);
		assertEquals(3, rq.getcnt());//assert rq.getcnt() == 0;
		
		req = new Request("(ER,4,4)",1,10);
		rq.rq_add(req);
		assertEquals(4, rq.getcnt());//assert rq.getcnt() == 0;
		
		req = new Request("(ER,5,10)",1,10);
		rq.rq_add(req);
		assertEquals(5, rq.getcnt());//assert rq.getcnt() == 0;
		
		req = new Request("(ER,7,11)",1,10);
		rq.rq_add(req);
		assertEquals(6, rq.getcnt());//assert rq.getcnt() == 0;
		
		req = new Request("(ER,7,11)",1,10);
		rq.rq_add(req);
		assertEquals(7, rq.getcnt());//assert rq.getcnt() == 0;
		
		req = new Request("(ER,6,11)",1,10);
		rq.rq_add(req);
		assertEquals(8, rq.getcnt());//assert rq.getcnt() == 0;
		
		req = new Request("(ER,9,11)",1,10);
		rq.rq_add(req);
		assertEquals(9, rq.getcnt());//assert rq.getcnt() == 0;
		
		req = new Request("(FR,5,UP,15)",1,10);
		rq.rq_add(req);
		assertEquals(10, rq.getcnt());//assert rq.getcnt() == 0;
		
		req = new Request("(FR,9,UP,16)",1,10);
		rq.rq_add(req);
		assertEquals(11, rq.getcnt());//assert rq.getcnt() == 0;
		
		req = new Request("(FR,9,DOWN,17)",1,10);
		rq.rq_add(req);
		assertEquals(12, rq.getcnt());//assert rq.getcnt() == 0;

		disp = new Dispatch(rq.getArray_time(),rq.getArray_floor(),
				rq.getArray_type(),rq.getArray_updown());
		disp.work();
		
		req = new Request("(FR,9,DOWN,18)",1,10);
		rq.rq_add(req);
		assertEquals(13, rq.getcnt());//assert rq.getcnt() == 0;
		
		req = new Request("(ER,9,18)",1,10);
		rq.rq_add(req);
		assertEquals(14, rq.getcnt());//assert rq.getcnt() == 0;

		req = new Request("(FR,9,UP,18)",1,10);
		rq.rq_add(req);
		assertEquals(15, rq.getcnt());//assert rq.getcnt() == 0;
		
		req = new Request("(FR,5,UP,19)",1,10);
		rq.rq_add(req);
		assertEquals(16, rq.getcnt());//assert rq.getcnt() == 0;
		
		req = new Request("(FR,3,UP,21)",1,10);
		rq.rq_add(req);
		assertEquals(17, rq.getcnt());//assert rq.getcnt() == 0;
		
		disp = new Dispatch(rq.getArray_time(),rq.getArray_floor(),
				rq.getArray_type(),rq.getArray_updown());
		disp.work();

		assertEquals(true, rq.repOK());
		assertEquals(true, disp.repOK());
		
		Disp dd;
		dd = new Disp(rq.getArray_time(),rq.getArray_floor(),
				rq.getArray_type(),rq.getArray_updown());
		dd.work();

		assertEquals(true, dd.repOK());
	}
	
	@Test
	public void test2() {
		
		req = new Request("(FR,10,DOWN,100)",1,10);
		rq.rq_add(req);
		assertEquals(0, rq.getcnt());//assert rq.getcnt() == 0;
		
		req = new Request("(FR,1,UP,0)",1,10);
		rq.rq_add(req);
		assertEquals(1, rq.getcnt());//assert rq.getcnt() == 0;
		
		req = new Request("(FR,1,UP,0)",1,10);
		rq.rq_add(req);
		assertEquals(2, rq.getcnt());//assert rq.getcnt() == 0;
		
		req = new Request("(FR,1,UP,1)",1,10);
		rq.rq_add(req);
		assertEquals(3, rq.getcnt());//assert rq.getcnt() == 0;
		
		disp = new Dispatch(rq.getArray_time(),rq.getArray_floor(),
				rq.getArray_type(),rq.getArray_updown());
		disp.work();

		assertEquals(true, rq.repOK());
		assertEquals(true, disp.repOK());
	}
	
	@Test
	public void test3() {
		
		req = new Request("(FR,1,UP,0)",1,10);
		rq.rq_add(req);
		assertEquals(1, rq.getcnt());//assert rq.getcnt() == 0;
		
		req = new Request("(FR,9,UP,9)",1,10);
		rq.rq_add(req);
		assertEquals(2, rq.getcnt());//assert rq.getcnt() == 0;
		
		req = new Request("(ER,1,10)",1,10);
		rq.rq_add(req);
		assertEquals(3, rq.getcnt());//assert rq.getcnt() == 0;
		
		req = new Request("(ER,5,11)",1,10);
		rq.rq_add(req);
		assertEquals(4, rq.getcnt());//assert rq.getcnt() == 0;

		disp = new Dispatch(rq.getArray_time(),rq.getArray_floor(),
				rq.getArray_type(),rq.getArray_updown());
		disp.work();

		assertEquals(true, rq.repOK());
		assertEquals(true, disp.repOK());
	}
	
}
