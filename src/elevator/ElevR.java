package elevator;

import java.util.Date;

public class ElevR extends Thread {
	private int id;
	private Elevator ele;
	private RequestQueue rq_e;
	//private long startTime;
	public ElevR(int id_t,Elevator ele_t,RequestQueue rq_e_t,long startTime_t){
		//startTime = startTime_t;
		id = id_t;
		ele = ele_t;
		rq_e = rq_e_t;
	}
	public void run(){
		while(true){
			//System.out.println(ele.getReqF());
			if (ele.getReqF() == -1) {//拼命 做指令，直至所有的指令为空
				for(int i = 0; i < rq_e.getCnt(); ++i){
					//System.out.println("UUU");
					if (ele.getReqF() == -1 && rq_e.getFloor(i) != -1) {//int flo,int id,int typ,int upd){
						System.out.println(i+"电梯"+id+"增加去楼层的主请求:"+rq_e.getFloor(i));
						ele.reqSet(rq_e.getFloor(i),rq_e.getElvid(i),rq_e.getType(i),rq_e.getUpdown(i),rq_e.getTime(i));
						rq_e.done(i);
						break;
					}
				}		
			} 
		
			mywork();
			ele.work();
			//System.out.println(id+":::"+ele.getFloor());
		}
	}	
	private void mywork() //执行操作
	{
		int out_i;
		out_i = 0;
		int open_f = 0;
		if (rq_e.getCnt()>0&&ele.getReqF()>1)
			System.out.println("电梯"+id+"目前有"+rq_e.getCnt()+"个请求，"+"目标去"+ele.getReqF()+"目前在"+ele.getFloor());
		if (ele.getReqF() != -1 && ele.getFloor() == ele.getReqF()){
			open_f = 1;//主请求做完了
		}
		if (open_f==0){
			for(int i = 0; i < rq_e.getCnt(); ++i){
				//System.out.println("@@@@"+ele.getReqF()+rq_e.getFloor(i));
				//if (ele.getReqF()!=-1&&rq_e.getFloor(i)！=-1)
					//if (ele.getReqF() == ele.getFloor() || rq_e.getFloor(i) == ele.getFloor()){
				
				if  (ele.getReqF() != -1 && rq_e.getFloor(i) != -1 
					&& rq_e.getFloor(i) == ele.getFloor()){//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
						open_f = 2;//捎带了一个请求
						out_i = i;
						break;
					}
			}
		}
		if (open_f > 0) {
			boolean flag = true;
			//if (ele.getReqF() == ele.getFloor())//是不是到了终点了
			{
				if (ele.getState()==0){
					ele.open();
					flag = false;
				}
				if (open_f==1){
					System.out.println((new Date().getTime())+":"+ele.toString(ele.getReqF()));
					Main.WriteFile().println((new Date().getTime())+":"+ele.toString(ele.getReqF()));
				}else{
					System.out.println((new Date().getTime())+":"+ele.toString(rq_e.getFloor(out_i)));
					Main.WriteFile().println((new Date().getTime())+":"+ele.toString(rq_e.getFloor(out_i)));
					rq_e.done(out_i);
				}
			}
			
			if (flag){
				ele.open();
			}
		}
	}
}
