package elevator;

import java.text.DecimalFormat;
import java.util.Arrays;

public class ElvDispatch extends Thread {
	private RequestQueue rq;
	private RequestQueue[] rq_elv;
	private Elevator[] elev_three;
	private int id_carry, id_choose;
	private int flo, upd, typ, id;
	private long tim;
	
	
	public ElvDispatch(RequestQueue rq_t,RequestQueue[] rq_elv_t,Elevator[] ele_three_t){
		rq = rq_t;
		rq_elv = rq_elv_t;
		elev_three = ele_three_t;
		
	}
	//public synchronized void run(){
	public void run(){
		
		while(true) {
			//synchronized (rq) 
			{
				//System.out.println(RLmain.lengthGet());
				for (int i = 0; i < rq.getCnt(); ++i){
					//System.out.println("6666");
					flo = rq.getFloor(i);
					upd = rq.getUpdown(i);
					typ = rq.getType(i);
					id = rq.getElvid(i);
					tim = rq.getTime(i);
					
					if (flo==-1)
						continue;
					
					int des;
					if (typ==2) {
						des = elev_three[id].getReqF();
						if (des != -1){
							if (upd == 1 && flo == des){//SAME [FR,9,DOWN,1]
								System.out.println("SAME "+out_s(flo,upd,typ,id,tim));
								//Main.WriteFile().println("SAME "+out_s(flo,upd,typ,id,tim));
								rq.done(i);
								continue;
							}
						}
						
						rq_elv[id].add_element(flo, id, typ, upd, tim);
						rq.done(i);
						
						//RLmain.del(k);
						//System.out.println(newreq +""+ newreq.timeGet() + " add to " + elevs[newreq.elnoGet()-1].nameGet());
						
						
						//elev_three[id].reqSet(flo, id, typ, upd, tim);
						//elev_three[id].setState();
						System.out.println(" ER ADD to");
						mainAdd(id_choose);
						continue;
					}
					
					//接下来是FR的分配哪一个电梯的问题了
					
					id_carry = -1;
					id_choose = -1;
					for (int j = 1; j <= 3; ++j) {
						if (carry(j,flo,upd,typ,id))//选择可以捎带的最小运动量的电梯
							if (id_carry < 0) 
								id_carry = j;
							else
								id_carry = elev_three[j].getTot() < elev_three[id_carry].getTot()
											? j : id_carry;
								//if (elev_three[j].getTot() < elev_three[id_carry].getTot())
								//	id_carry = j;
							
					}
					if (id_carry >= 1) {
						
						des = elev_three[id_carry].getReqF();
						if (des != -1){
							boolean flag = false;
							if (upd == elev_three[id_carry].getState() && flo == des){//和主请求本质
								flag = true;
								
								//System.out.println("&&&##" + rq_elv[id_carry].getFloor(k));
								
								System.out.println("SAME "+out_s(flo,upd,typ,id,tim));
								//Main.WriteFile().println("SAME "+out_s(flo,upd,typ,id,tim));
								rq.done(i);

								continue;
							}
							
							for (int k = 0; k < rq_elv[id_carry].getCnt(); ++k)//判断除了主请求的其它请求，因为主请求此时的getfloor是-1
							{
								//System.out.println(k+"判断id_carry的重复"+elev_three[id_carry].getState()+"p"+ rq_elv[id_carry].getFloor(k));
								//System.out.println("----"+upd+"p"+flo);
								if (upd == elev_three[id_carry].getState() && flo == rq_elv[id_carry].getFloor(k))
									
								{
									//System.out.println("&&&##" + rq_elv[id_carry].getFloor(k));
								//}
								
								//if (upd == 1 && flo == des){//SAME [FR,9,DOWN,1]
									System.out.println("SAME "+out_s(flo,upd,typ,id,tim));
									//Main.WriteFile().println("SAME "+out_s(flo,upd,typ,id,tim));
									rq.done(i);
									flag = true;
									break;
								}
							}
							if (flag){
								continue;
							}
						}
						
						rq_elv[id_carry].add_element(flo, id, typ, upd, tim);
						rq.done(i);
						System.out.println("FR Add"+id_carry);
						continue;
					}
					for (int j = 1; j <= 3; ++j){
						//System.out.println("YYY");
						if (choose(j))
							if (id_choose < 0)
								id_choose = j; 
							else//{
								id_choose = elev_three[j].getTot() < elev_three[id_choose].getTot()
								? j : id_choose;
								//if (elev_three[j].getTot() < elev_three[id_choose].getTot())
								//	id_choose = j;
							//}
					}
					if (id_choose >= 1) {
						des = elev_three[id_choose].getReqF();
						if (des != -1){
							boolean flag = false;
							for (int k = 0; k < rq_elv[id_choose].getCnt(); ++k)
							if (upd == elev_three[id_choose].getState() && flo == rq_elv[id_choose].getFloor(k))
							{
								System.out.println("%%%" + rq_elv[id_choose].getFloor(k));
							//}
							
							//if (upd == 1 && flo == des){//SAME [FR,9,DOWN,1]
								System.out.println("SAME "+out_s(flo,upd,typ,id,tim));
								
								//Main.WriteFile().println("SAME "+out_s(flo,upd,typ,id,tim));
								rq.done(i);
								flag = true;
								break;
							}
							if (flag){
								continue;
							}
						}
						
						rq_elv[id_choose].add_element(flo, id, typ, upd, tim);
						//elev_three[id_choose].reqSet(flo, id, typ, upd, tim);
						//elev_three[id_choose].setState();
						mainAdd(id_choose);
						rq.done(i);
						System.out.println("FR Add~~~"+id_choose);
						continue;
					}
				}
			}
		}	
	}
	
	public  void mainAdd(int no){
		if (elev_three[no].getReqF() == -1) {//拼命 做指令，直至所有的指令为空
			for(int i = 0; i < rq_elv[no].getCnt(); ++i){
				//System.out.println("UUU");
				if (rq_elv[no].getFloor(i) != -1) {//int flo,int id,int typ,int upd){
					System.out.println("新增加的！"+i+"电梯"+no+"增加去楼层的主请求:"+rq_elv[no].getFloor(i));
					elev_three[no].reqSet(rq_elv[no].getFloor(i),rq_elv[no].getElvid(i),rq_elv[no].getType(i),rq_elv[no].getUpdown(i),rq_elv[no].getTime(i));
					rq_elv[no].done(i);
					return;
				}
			}	
		}
	}
	
	boolean carry(int e, int flo,int upd,int typ,int id){//当前状态能否捎带请求r
		if (elev_three[e].getReqF() == -1)
			return false;
		int now_dir, toFloor, now_floor;
		now_dir = elev_three[e].getState();
		toFloor = elev_three[e].getReqF();//e.n当前运动的目标楼层
		now_floor = elev_three[e].getFloor();//e.e_n 电梯当前的楼层
		//System.out.println("now_dir = "+now_dir+"toFloor="+toFloor+"now_floor="+now_floor);
		
		if ( (typ == 1 && upd == now_dir) && 
				( (upd == 1 && flo <= toFloor && flo > now_floor) 
				|| (upd == 1 && flo >= toFloor && flo < now_floor) )
					){
				return true;
			}
		if ((typ == 2) && 
			( (now_dir == 1 && flo > now_floor) 
				|| (now_dir == -1 && flo < now_floor) )
				){
			return true;
		}
			//Lack a situation (4)//Or not 
		return false;
	}
	
	
	boolean choose(int e){
		if (elev_three[e].getState() == -2 //IDLE
			&& elev_three[e].getReqF() == -1)
			return true;
		return false;
	}
	
	public String out_s(int flo,int upd,int typ,int id,long tim){//（#电梯, 楼层, STILL, 累积运动量, t）
		//System.out.println("("+elv.getFloor()+","+s[now_dir]+","+elv.getTime()+")");
		String st;  
		
		final String[] s = {"DOWN","STILL","UP"};
		int s_t = upd + 1;
		
		DecimalFormat decimalFormat = new DecimalFormat("#0.0");//格式化设置  
		
		if (typ == 1){//FR
			//System.out.print("[FR,"+flo+","+s[s_t]+"]");
			st = "[FR,"+flo+","+s[s_t]+","+decimalFormat.format((tim-RequestAdd.getStartTime())/1000.0)+"]";
		}else{
			//System.out.print("[ER,#"+id+","+flo+"]");//(ER,#1,4)
			st = "[ER,#"+id+","+flo+","+decimalFormat.format((tim-RequestAdd.getStartTime())/1000.0)+"]";
		}
		
		return st;
	}
}
