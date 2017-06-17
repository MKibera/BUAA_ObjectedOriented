package oo;

import java.util.Scanner;
import java.util.Set;
import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

public class RequestQueue {
	private ArrayList<Integer> srcx = new ArrayList<Integer>();
	private ArrayList<Integer> srcy = new ArrayList<Integer>();
	private ArrayList<Integer> dstx = new ArrayList<Integer>();
	private ArrayList<Integer> dsty = new ArrayList<Integer>();
	private ArrayList<Long> time = new ArrayList<Long>();
	private ArrayList<Boolean> used = new ArrayList<Boolean>();
	private ArrayList<Boolean> first = new ArrayList<Boolean>();
	private ArrayList<Set<Integer>> choice = new ArrayList<Set<Integer>>();
	private static ArrayList<PrintStream> p = new ArrayList<PrintStream>();
	private static PrintStream p_temp;
	public RequestQueue(){
	}
	public boolean repOK(){
		/*@ REQUIRES: None;
		@ MODIFIES: None;
		@ Effects: \result == invariant(this);
		@ Invariant(this): (\all int i;  0 <= i < srcx.size(); srcx.get(i) >= 1 && srcx.get(i) <=80 &&
			srcy.get(i) >= 1 && srcy.get(i) <=80 && dstx.get(i) >= 1 && dstx.get(i) <=80 && 
			dsty.get(i) >= 1 && dsty.get(i) <=80);
		*/
		for (int i = 0; i < srcx.size(); ++i){
			if (srcx.get(i)<1||srcx.get(i)>80)
				return false;
			if (srcy.get(i)<1||srcy.get(i)>80)
				return false;
			if (dstx.get(i)<1||dstx.get(i)>80)
				return false;
			if (dsty.get(i)<1||dsty.get(i)>80)
				return false;
		}
		return true;
	}
	public synchronized void add_element(long tim,int num1,int num2,int num3,int num4,boolean used_t) {
		/*@ REQUIRES: (choice.get(i) exists);
		@ MODIFIES: time, srcx, srcy, dstx, dsty, used, first, v, p
		@ EFFECTS:  normal_behavior
					主要的存储属性的ArrayList的Add操作
					(!\exist File(f_n)) ==> System.out.println("File Exception!");	
		@ THREAD_REQUIRES: \locked(this);
		@ THREAD_EFFECTS: \locked();
		*/
		try{	
			
			time.add(tim);//求出第一条指令的系统时间，作为基准时间
			srcx.add(num1);
			srcy.add(num2);
			dstx.add(num3);	
			dsty.add(num4);
			used.add(used_t);
			first.add(true);
			
			if (srcx.size() > 1000){
				System.out.println("Too Many Requests");
				System.exit(0);
			}
			
			Set<Integer> v= new HashSet<Integer>();
			choice.add(v);
			String f_n = "log"+p.size()+".txt";
			FileOutputStream fs = new FileOutputStream(new File(f_n));
		 	p_temp = new PrintStream(fs);//p.println(10);
		 	p.add(p_temp);
		 	
			//TaxiGUI gui=new TaxiGUI();
			//gui.RequestTaxi(new Point(num1-1,num2-1), new Point(num3-1,num4-1));//读入大量请求，请注释掉这句话！！！
		 }
		 catch (Exception e){
			 System.out.println("File Exception!");
		 }
	}
	public synchronized int getsrcx(int i){
		/*@ REQUIRES: (srcx.get(i) exists);
		@ MODIFIES: None;
		@ EFFECTS: \result == srcx.get(i);
		@ THREAD_REQUIRES: \locked(this);
		@ THREAD_EFFECTS: \locked();
		*/
		return srcx.get(i);
	}
	
	public synchronized int getsrcy(int i){
		/*@ REQUIRES: (srcy.get(i) exists);
		@ MODIFIES: None;
		@ EFFECTS: \result == srcy.get(i);
		@ THREAD_REQUIRES: \locked(this);
		@ THREAD_EFFECTS: \locked();
		*/
		return srcy.get(i);
	}
	
	public synchronized int getdstx(int i){
		/*@ REQUIRES: (dstx.get(i) exists);
		@ MODIFIES: None;
		@ EFFECTS: \result == dstx.get(i);
		@ THREAD_REQUIRES: \locked(this);
		@ THREAD_EFFECTS: \locked();
		*/
		return dstx.get(i);
	}
	public synchronized int getdsty(int i){
		/*@ REQUIRES: (dsty.get(i) exists);
		@ MODIFIES: None;
		@ EFFECTS: \result == dsty.get(i);
		@ THREAD_REQUIRES: \locked(this);
		@ THREAD_EFFECTS: \locked();
		*/
		return dsty.get(i);
	}
	public synchronized long gettime(int i){
		/*@ REQUIRES: (time.get(i) exists);
		@ MODIFIES: None;
		@ EFFECTS: \result == time.get(i);
		@ THREAD_REQUIRES: \locked(this);
		@ THREAD_EFFECTS: \locked();
		*/
		return time.get(i);
	}
	public synchronized int gettot(){
		/*@ REQUIRES: None;
		@ MODIFIES: None;
		@ EFFECTS: \result == srcx.size();
		@ THREAD_REQUIRES: \locked(this);
		@ THREAD_EFFECTS: \locked();
		*/
		return srcx.size();
	}
	
	public synchronized boolean getused(int i){
		/*@ REQUIRES: (choice.get(i) exists);
		@ MODIFIES: None;
		@ EFFECTS: used.get(i);
		@ THREAD_REQUIRES: \locked(this);
		@ THREAD_EFFECTS: \locked();
		*/
		return used.get(i);
	}
	public synchronized Set<Integer> getset(int i){
		/*@ REQUIRES: (choice.get(i) exists);
		@ MODIFIES: None;
		@ EFFECTS: \result == choice.get(i);
		@ THREAD_REQUIRES: \locked(this);
		@ THREAD_EFFECTS: \locked();
		*/
		return choice.get(i);
	}
	public synchronized void setused(int i,boolean t){
		/*@ REQUIRES: (used.get(i) exists);
		@ MODIFIES: used;
		@ EFFECTS: used.set(i, t);
		@ THREAD_REQUIRES: \locked(this);
		@ THREAD_EFFECTS: \locked();
		*/
		used.set(i, t);
	}
	public synchronized void setset(int i,int j){
		/*@ REQUIRES: (choice.get(i) exists);
		@ MODIFIES: choice;
		@ EFFECTS: choice.get(i).add(j);
		@ THREAD_REQUIRES: \locked(this);
		@ THREAD_EFFECTS: \locked();
		*/
		choice.get(i).add(j);
	}
	public synchronized static PrintStream LogFile(int i){
		/*@ REQUIRES: (p.get(i) exists);
		@ MODIFIES: None;
		@ EFFECTS: \result == p.get(i);
		@ THREAD_REQUIRES: \locked(this);
		@ THREAD_EFFECTS: \locked();
		*/
		return p.get(i);
	}
	public synchronized void setfirst(int i){
		/*@ REQUIRES: (first.get(i) exists);
		@ MODIFIES: first;
		@ EFFECTS: first.set(i, false);
		@ THREAD_REQUIRES: \locked(this);
		@ THREAD_EFFECTS: \locked();
		*/
		first.set(i, false);
	}
	public synchronized boolean getfirst(int i){
		/*@ REQUIRES: (first.get(i) exists);
		@ MODIFIES: None;
		@ EFFECTS: \result == first.get(i);
		@ THREAD_REQUIRES: \locked(this);
		@ THREAD_EFFECTS: \locked();
		*/
		return first.get(i);
	}
}
