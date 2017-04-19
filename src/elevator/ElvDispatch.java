package elevator;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Date;

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
	public synchronized void run(){
	//public void run(){
		while(true) {
			boolean i_flag = true;
			for (int j = 1; j <= 3; ++j){//判断目前是不是IDLE状态，如果是的话，就更新launchtime,这个值是三部电梯刚从IDLE状态变成运动状态的偏移时间
				i_flag = true;
				if (elev_three[j].getState() != -2){
						i_flag = false;
						break;
					}
			}
			if (i_flag)
				RequestAdd.setLaunchTime();
			
			for (int j = 1; j <= 3; ++j){//给三个电梯分配主请求
				for(int i = 0; i < rq_elv[j].getCnt(); ++i){
					if (elev_three[j].getReqF() == -1 && rq_elv[j].getFloor(i) != -1) {
						//System.out.println(i+"电梯"+j+"增加去楼层的主请求:"+rq_elv[j].getFloor(i));
						elev_three[j].reqSet(i, rq_elv[j].getFloor(i),rq_elv[j].getElvid(i),rq_elv[j].getType(i),rq_elv[j].getUpdown(i),rq_elv[j].getTime(i));
						rq_elv[j].done(i);
						break;
					}
				}
			}
			
			for (int i = 0; i < rq.getCnt(); ++i){
				flo = rq.getFloor(i);
				upd = rq.getUpdown(i);
				typ = rq.getType(i);
				id = rq.getElvid(i);
				tim = rq.getTime(i);
				if (flo==-1)
					continue;
				int des;
//——————————————————————————————————分配ER————————————————————————————————————————————	
				if (typ==2) {
					//if ( (upd == 1 && Light.getUpp(flo)) || (upd == -1 && Light.getDow(flo)) ){
					if (Light.getF(id,flo)){
						
						//System.out.println("ER Light"+"id"+id+"flo"+flo);
						if (Main.IsConsole())
							System.out.println((new Date().getTime())+":SAME "+out_s(flo,upd,typ,id,tim));
						Main.WriteFile().println((new Date().getTime())+":SAME "+out_s(flo,upd,typ,id,tim));
						rq.done(i);
	//continue;
						break;
					}	
					des = elev_three[id].getReqF();
					if (des != -1){
						if (upd == 1 && flo == des){//:SAME [FR,9,DOWN,1]
							if (Main.IsConsole())
								System.out.println((new Date().getTime())+":SAME "+out_s(flo,upd,typ,id,tim));
							Main.WriteFile().println((new Date().getTime())+":SAME "+out_s(flo,upd,typ,id,tim));
							rq.done(i);
	//continue;
							break;
						}
					}
						
					rq_elv[id].add_element(flo, id, typ, upd, tim);
					rq.done(i);
						
					//elev_three[id].reqSet(flo, id, typ, upd, tim);
					//elev_three[id].setState();
					//System.out.println(i+" ER ADD to");
					
					long startTime = RequestAdd.getStartTime();
					//	System.out.println(id+" ElvDispatch"+((new Date().getTime()-startTime)/1000.0));
						
mainAdd(id);
	
					//System.out.println(id+","+flo+"setF"+",true");
					Light.setF(id, flo, true);
	//continue;
					break;
				}
					
//——————————————————————————接下来是FR的分配哪一个电梯的问题了————————————————————————————————————
					
				if ( (upd == 1 && Light.getUpp(flo)) || (upd == -1 && Light.getDow(flo)) ){
					//System.out.println("Light" + flo);
					if (Main.IsConsole())
						System.out.println((new Date().getTime())+":SAME "+out_s(flo,upd,typ,id,tim));
					Main.WriteFile().println((new Date().getTime())+":SAME "+out_s(flo,upd,typ,id,tim));
					rq.done(i);
	//continue;
					break;
				}
					
				id_carry = -1;
				id_choose = -1;
				for (int j = 1; j <= 3; ++j) {
					System.out.println("j"+j);
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
					System.out.println(rq+" "+id_carry);
					des = elev_three[id_carry].getReqF();
					if (des != -1){
						boolean flag = false;
						if (upd == elev_three[id_carry].getState() && flo == des && typ == elev_three[id_carry].getReqT()){//和主请求本质
							flag = true;
							if (Main.IsConsole())
								System.out.println((new Date().getTime())+":SAME "+out_s(flo,upd,typ,id,tim));
							Main.WriteFile().println((new Date().getTime())+":SAME "+out_s(flo,upd,typ,id,tim));
							rq.done(i);
	//continue;
							break;
						}
							
						for (int k = 0; k < rq_elv[id_carry].getCnt(); ++k)//判断除了主请求的其它请求，因为主请求此时的getfloor是-1
						{
							if (upd == elev_three[id_carry].getState() && flo == rq_elv[id_carry].getFloor(k))	
							{
								//System.out.println("id_carry"+id_carry+"k="+k);
								if (Main.IsConsole())
									System.out.println((new Date().getTime())+":SAME "+out_s(flo,upd,typ,id,tim));
								Main.WriteFile().println((new Date().getTime())+":SAME "+out_s(flo,upd,typ,id,tim));
								rq.done(i);
								flag = true;
								break;
							}
						}
						if (flag)
	//continue;
							break;
					}
					rq_elv[id_carry].add_element(flo, id, typ, upd, tim);
					rq.done(i);
					//System.out.println(i+" FR Carry"+id_carry);
						
					if (upd == 1)
						Light.setUpp(flo, true);//upp[flo] = true;
					if (upd == -1)
						Light.setDow(flo, true);
					continue;
				}
					
				id_choose = -1;
				for (int j = 1; j <= 3; ++j){
					if (choose(j)){
						if (id_choose < 0){
								id_choose = j;
								//System.out.println(j+"liang"+elev_three[j].getTot());
						}
						else{
							id_choose = elev_three[j].getTot() < elev_three[id_choose].getTot()
							? j : id_choose;
							//System.out.println(j+"liang"+elev_three[j].getTot());
						}
								//if (elev_three[j].getTot() < elev_three[id_choose].getTot())
								//	id_choose = j;
						}
				}
				if (id_choose >= 1) {
					des = elev_three[id_choose].getReqF();
					if (des != -1){
						boolean flag = false;
						for (int k = 0; k < rq_elv[id_choose].getCnt(); ++k)
						if (upd == elev_three[id_choose].getState() && flo == rq_elv[id_choose].getFloor(k))
						{
							//System.out.println("id_choose"+id_choose+"k="+k);
							if (Main.IsConsole())
								System.out.println((new Date().getTime())+":SAME "+out_s(flo,upd,typ,id,tim));
							Main.WriteFile().println((new Date().getTime())+":SAME "+out_s(flo,upd,typ,id,tim));
							rq.done(i);
							flag = true;
							break;
						}
						if (flag){
	//continue;
							break;
						}
					}
						
					rq_elv[id_choose].add_element(flo, id, typ, upd, tim);
					//elev_three[id_choose].reqSet(flo, id, typ, upd, tim);
					//elev_three[id_choose].setState();
					//System.out.println("ID_CHOOSE");						
mainAdd(id_choose);
					rq.done(i);
					//System.out.println(i+" FR Choose"+id_choose);
						
					if (upd == 1)
						Light.setUpp(flo, true);//upp[flo] = true;
					if (upd == -1)
						Light.setDow(flo, true);//dow[flo] = true;
					continue;
				}
				break;
			}
		}	
	}
	
	public synchronized void mainAdd(int no){
		if (elev_three[no].getReqF() == -1) {//拼命 做指令，直至所有的指令为空
			for(int ii = 0; ii < rq_elv[no].getCnt(); ++ii){
				if (rq_elv[no].getFloor(ii) != -1) {//int flo,int id,int typ,int upd){
					System.out.println("新增加的！"+ii+"电梯"+no+"增加去楼层的主请求:"+rq_elv[no].getFloor(ii));
					elev_three[no].reqSet(ii, rq_elv[no].getFloor(ii),rq_elv[no].getElvid(ii),rq_elv[no].getType(ii),rq_elv[no].getUpdown(ii),rq_elv[no].getTime(ii));
					rq_elv[no].done(ii);
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
		System.out.println(e+"now_dir = "+now_dir+"toFloor="+toFloor+"now_floor="+now_floor);
		System.out.println("typ"+typ+"upd"+upd+"flo"+flo);
		if ( (typ == 1 && upd == now_dir) && 
				( (upd == 1 && flo <= toFloor && flo >= now_floor) 
				|| (upd == -1 && flo >= toFloor && flo <= now_floor) ) ){
			System.out.println("@@##");
				return true;
			}
		if ((typ == 2) && 
			( (now_dir == 1 && flo > now_floor) 
				|| (now_dir == -1 && flo < now_floor) ) ){
			return true;
		}
			//Lack a situation (4)//Or not 
		return false;
	}
	
	boolean choose(int e){
		//System.out.println("Choose"+e+","+elev_three[e].getState()+","+elev_three[e].getReqF());
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
