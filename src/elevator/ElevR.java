package elevator;

import java.util.Date;

public class ElevR extends Thread {
	private int id;
	private Elevator ele;
	private RequestQueue rq_e;
	private long startTime;
	public ElevR(int id_t,Elevator ele_t,RequestQueue rq_e_t,long startTime_t){
		startTime = RequestAdd.getStartTime();
		id = id_t;
		ele = ele_t;
		rq_e = rq_e_t;
	}
	public void run(){
		while(true){
			int tt;
			synchronized (this){
				tt  = ele.getReqF();
			}
			if (tt == -1) {//拼命 做指令，直至所有的指令为空
				continue;
			} 
			mywork();
			ele.work();
		}
	}	
	private void mywork() //执行操作
	{
		int out_i;
		out_i = 0;
		int open_f = 0;
		startTime = RequestAdd.getStartTime();
		//	System.out.println("电梯"+id+"目前有"+rq_e.getCnt()+"个请求，"+"目标去"+ele.getReqF()+"目前在"+ele.getFloor());
		if (ele.getReqF() != -1 && ele.getFloor() == ele.getReqF()){
			open_f = 1;//主请求做完了
			//System.out.println(id+" YES"+((new Date().getTime()-startTime)/1000.0));
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
			//if (ele.getReqF() == ele.getFloor())
			if (ele.getState()==0){//如果是STILL, 先开门后输出；否则先输出时间后开门
				ele.open();
				flag = false;
			}
			if (open_f==1){//代表主请求做完了
				if (Main.IsConsole())
					System.out.println((new Date().getTime())+":"+ele.toString(ele.getReqF(),ele.getReqI(),ele.getReqT(),ele.getReqU()));
				Main.WriteFile().println((new Date().getTime())+":"+ele.toString(ele.getReqF(),ele.getReqI(),ele.getReqT(),ele.getReqU()));
				if (ele.getReqT()==1){//1:FR
					//System.out.println("setUp/setDown:"+ele.getReqF()+"false");
					if (ele.getReqU() == 1)//
						Light.setUpp(ele.getReqF(), false);
					if (ele.getReqU() == -1)
						Light.setDow(ele.getReqF(), false);
				}else{//2:ER
						//System.out.println("setF:"+id+","+ele.getReqF()+"false");
						Light.setF(id, ele.getReqF(), false);
					}
				//在主请求做完的同时看有没有可合并的指令
				for(int j = 0; j < rq_e.getCnt(); ++j){
					if  (ele.getReqF() != -1 && rq_e.getFloor(j) != -1 
							&& rq_e.getFloor(j) == ele.getFloor()
								&& rq_e.getType(j) != ele.getReqT()){//除了楼层和主请求一样到达了当前楼层，注意它的类型要相反，一个是FR,一个是ER
						work_out(j);
						//System.out.println("主请求的合并，"+j);
						//break;
					}
				}
				
			}else{//否则是选取的分支里的一条指令
					work_out(out_i);
					
					for(int j = out_i+1; j < rq_e.getCnt(); ++j){
						if  (ele.getReqF() != -1 && rq_e.getFloor(j) != -1 
								&& rq_e.getFloor(j) == ele.getFloor()
									&& rq_e.getType(j) != rq_e.getType(out_i)){//除了楼层和主请求一样到达了当前楼层，注意它的类型要相反，一个是FR,一个是ER
							work_out(j);
							//System.out.println("捎带的合并，"+j);
							//break;
						}
					}
					
				}
			
			if (flag)
				ele.open();

			boolean idle_flag;//判断这一部电梯是不是IDLE状态，即所有指令都不在工作就是IDLE状态
			idle_flag = true;
			for(int h = 0; h < rq_e.getCnt(); ++h){
				if (rq_e.getFloor(h) != -1){
					idle_flag = false;
					break;
				}
			}
			if (idle_flag && ele.getState() != -2  && open_f==1){//！！！必须是主请求做完了才有可能把Idle设置了吧
					//System.out.println(id+ " IDLE ");
				ele.setIdle();
			}
		}
	}
	
	public void work_out(int out_i){
		if (Main.IsConsole())
			System.out.println((new Date().getTime())+":"+ele.toString(rq_e.getFloor(out_i),rq_e.getElvid(out_i),rq_e.getType(out_i),rq_e.getUpdown(out_i)));
		Main.WriteFile().println((new Date().getTime())+":"+ele.toString(rq_e.getFloor(out_i),rq_e.getElvid(out_i),rq_e.getType(out_i),rq_e.getUpdown(out_i)));
			
		if (rq_e.getType(out_i) == 1){	//1:FR
			//System.out.println("setUp/setDown:"+rq_e.getFloor(out_i)+"false");
			if (rq_e.getUpdown(out_i) == 1)//
				Light.setUpp(rq_e.getFloor(out_i), false);
			if (rq_e.getUpdown(out_i) == -1)
				Light.setDow(rq_e.getFloor(out_i), false);
		}else{
			//System.out.println("~~~setF:"+id+","+rq_e.getFloor(out_i)+"false");
			Light.setF(id, out_i, false);
		}
		rq_e.done(out_i);//这一分支里的指令就做完了。主请求不需要这句话，因为主请求刚加进去就做了。
	}
}
