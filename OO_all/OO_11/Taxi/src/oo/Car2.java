package oo;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import oo.gv;

interface MyIterator {
	public boolean repOK();
	/*@ Effects: \result == invariant(this);
	@ Invariant(this): t != null && len >= -0 && cnt >= -1 && cnt < len;
	*/
	public int len();//返回这次服务请求总共经过了多少个节点（从接单开始到最终到用户想去的终点）
	/*@ Effects: \result == len;
	*/
	public boolean hasNext();//是否还有路径的下个节点
	/*@ Effects: \result == (cnt+1>=0 && cnt+1<=len-1) ==> true;
						  (cnt+1 <0 || cnt+1>len-1) ==> false;
	*/
	
	public String next();//返回路径的下个节点
	/*@ Effects: \result == (cnt-1 >= len || cnt < 1) ? null :  String of next position
	*/
	
	public boolean hasPrev();//是否还有路径的下个节点
	/*@ Effects: \result == (cnt-1>=0 && cnt-1<=len-1) ==> true;
						  (cnt-1<0 || cnt-1>len-1) ==> false;
	*/
	
	public String prev();//返回路径的上个节点
	/*@ Effects: \result == (cnt-1 >= len || cnt < 1) ? null :  String of previous position
	*/

	public void work_first();//求出租车起始的x和y坐标
	/*@ MODIFIES: x_first, y_first;
	@ Effects: x_first, y_first为出租车起始的x和y坐标
	*/
	
	public int getx_first();//返回出租车的起始x坐标
	/*@ Effects: \result == x_first;
	*/
	
	public int gety_first();//返回出租车的起始y坐标
	/*@ Effects: \result == y_first;
	*/
}

public class Car2 extends Car implements Runnable{
	private int car_id_son;
	
	public Car2(RequestQueue rq_t,int id_t, Car[] c_all_t, int spec_t){
		/*@ Requires: None;
		@ Modifies: id, c_all, pos, wait_start, Main.gui, wait_step;
		@ Effects: id == id_t;
		   			 c_all == c_all_t;
		   			 pos == rand.nextInt(N*N) + 1;
		   			 以及一些构造函数的初始化
		*/
		super(rq_t,id_t,c_all_t,spec_t);
		car_id_son = id_t;
	}
	public boolean repOK(){
		/*@ REQUIRES: None;
		@ MODIFIES: None;
		@ Effects: \result == invariant(this);
		@ Invariant(this): super.repOK() && car_id_son >= 1 && car_id_son <= 100;
		*/
		return super.repOK() && car_id_son >= 1 && car_id_son <= 100;
	}
	
	public void out_it(){
		/*@ REQUIRES: None;
		@ MODIFIES: System.out;
		@ Effects: 输出到track*.txt服务信息
		*/
		MyIterator it = this.It(cus_id, id);
		//System.out.println(id+"~~~~~~"+"("+rq.getsrcx(cus_id)+","+rq.getsrcy(cus_id));
		Main.LogTaxiTrack(id).println("请求"+cus_id);
		Main.LogTaxiTrack(id).println("请求产生时间: "+rq.gettime(cus_id)+
				" 请求发出位置: "+"("+rq.getsrcx(cus_id)+","+rq.getsrcy(cus_id)+")"+" 请求目的地位置: "+
				"("+rq.getdstx(cus_id)+","+rq.getdsty(cus_id)+")");
		it.work_first();
		Main.LogTaxiTrack(id).println("出租车抢到单时的所处位置:"+"("+String.valueOf(it.getx_first())+", "+String.valueOf(it.gety_first())+")");
		Main.LogTaxiTrack(id).println("出租车去接乘客的路径:");
		if (cus_id==-1){
			//System.out.println("EEENNNNDDD");
			return ;
		}
		
		while (it.hasNext()){
			//System.out.print(it.next());
			Main.LogTaxiTrack(id).print(it.next() + " ---> ");
		}
		//System.out.println("");
		Main.LogTaxiTrack(id).println("到达用户目的地");
	}
	public TaxiIterator It(int req_id_con,int car_id_t){
	/*@ REQUIRES: None;
	@ MODIFIES: None;
	@ Effects: \result == new TaxiIterator(this, req_id_con);
	*/
		return new TaxiIterator(this, req_id_con, car_id_t);
	}
	
	//private static class TaxiIterator implements MyIterator
	private class TaxiIterator implements MyIterator {
		private int sign;
		private Car2 t;
		private int len;
		private int cnt;
		private int req_id, car_id;
		private int x_first, y_first;
		
		TaxiIterator(Car2 it,int req_id_t,int car_id_t){
		/*@ Requires: None;
		@ Modifies: t, len, cnt, req_id, car_id, sign;
		@ Effects: 一些构造函数的初始化
		*/	
			t = it;
			len = it.spath[cus_id].size();
			cnt = -1;
			req_id = req_id_t;
			car_id = car_id_t;
			//System.out.println("car_id"+car_id);
			sign = 0;
		}
		
		public boolean repOK(){
			/*@ REQUIRES: None;
			@ MODIFIES: None;
			@ Effects: \result == invariant(this);
			@ Invariant(this): t != null && len >= -0 && cnt >= -1 && cnt < len;
			*/
			if (t== null) 
				return false;
			if (len < 0) 
				return false;
			if (cnt <-1 || cnt >= len) 
				return false;
			return true;
		}
		
		public int len(){
		/*@ REQUIRES: None;
		@ MODIFIES: None;
		@ Effects: \result == len;
		*/
			return len;
		}
		
		public boolean hasNext(){
		/*@ REQUIRES: None;
		@ MODIFIES: None;
		@ Effects: \result == (cnt+1>=0 && cnt+1<=len-1) ==> true;
							  (cnt+1 <0 || cnt+1>len-1) ==> false;
		*/
			return (cnt+1>=0 && cnt+1<=len-1);
		}
		
		public String next(){
		/*@ Requires: None;
		@ Modifies: None;
		@ Effects: \result == (cnt-1 >= len || cnt < 1) ? null :  String of next position
		*/
			if (cnt+1>=len||cnt+1<0) {
				return null;
			}	
			cnt ++;
			return cal();
		}
		
		public boolean hasPrev(){
		/*@ REQUIRES: None;
		@ MODIFIES: None;
		@ Effects: \result == (cnt-1>=0 && cnt-1<=len-1) ==> true;
							  (cnt-1<0 || cnt-1>len-1) ==> false;
		*/
			return (cnt-1>=0 && cnt-1<=len-1);
		}
		
		public String prev(){
		/*@ Requires: None;
		@ Modifies: None;
		@ Effects: \result == (cnt-1 >= len || cnt < 1) ? null :  String of previous position
		*/
			if (cnt>len || cnt<1) {
				return null;
			}
			cnt--;
			return cal();
		}
		public void work_first(){
			/*@ REQUIRES: None;
			@ MODIFIES: x_first, y_first;
			@ Effects: x_first, y_first为出租车起始的x和y坐标
			*/
			int u;
			u = (int) t.spath[req_id].get(0);

			x_first = (u-1) / N+1;
			y_first = u % N;
			if (y_first == 0)
				y_first = N;
		}
		public String cal(){
			/*@ Requires: None;
			@ Modifies: None;
			@ Effects: \result == t.spath[req_id]中位于cnt的坐标，以字符串形式
			*/
			int u,x,y;
			u = (int) t.spath[req_id].get(cnt);

			x = (u-1) / N+1;
			y = u % N;
			if (y == 0)
				y = N;
			String tmp;
			int p,q;
			p = rq.getsrcx(cus_id);
			q = rq.getsrcy(cus_id);
			
			if (sign == 1){
				Main.LogTaxiTrack(car_id).println("到达用户坐标接到乘客，准备去往用户目的地");//System.out.println("服务");
				Main.LogTaxiTrack(car_id).println("去目的地途中所行驶的路径: ");
				sign = -1;
			}
			if (sign == 0 && x == rq.getsrcx(cus_id) && y == rq.getsrcy(cus_id))
				sign = 1;
			tmp = new String("("+String.valueOf(x)+","+String.valueOf(y)+")");
			return tmp;
		}
		public int getx_first(){
			/*@ REQUIRES: None;
			@ MODIFIES: None;
			@ Effects: \result == x_first;
			*/
			return x_first;
		}
		
		public int gety_first(){
			/*@ REQUIRES: None;
			@ MODIFIES: None;
			@ Effects: \result == y_first;
			*/
			return y_first;
		}
	}
	
}
