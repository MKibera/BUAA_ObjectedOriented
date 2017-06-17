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
	public synchronized void add_element(long tim,int num1,int num2,int num3,int num4,boolean used_t) {
		try{	
			time.add(tim);//求出第一条指令的系统时间，作为基准时间
			srcx.add(num1);
			srcy.add(num2);
			dstx.add(num3);	
			dsty.add(num4);
			used.add(used_t);
			first.add(true);
			Set<Integer> v= new HashSet<Integer>();
			choice.add(v);
			String f_n = "log"+p.size()+".txt";
			FileOutputStream fs = new FileOutputStream(new File(f_n));
		 	p_temp = new PrintStream(fs);//p.println(10);
		 	p.add(p_temp);
		 	
			TaxiGUI gui=new TaxiGUI();
			gui.RequestTaxi(new Point(num1-1,num2-1), new Point(num3-1,num4-1));//读入大量请求，请注释掉这句话！！！
		 }
		 catch (Exception e){
			 System.out.println("File Exception!");
		 }
	}
	public synchronized int getsrcx(int i){
		return srcx.get(i);
	}
	
	public synchronized  int getsrcy(int i){
		return srcy.get(i);
	}
	
	public synchronized int getdstx(int i){
		return dstx.get(i);
	}
	public synchronized int getdsty(int i){
		return dsty.get(i);
	}
	public synchronized long gettime(int i){
		return time.get(i);
	}
	public synchronized int gettot(){
		return srcx.size();
	}
	
	public synchronized boolean getused(int i){
		return used.get(i);
	}
	public synchronized Set<Integer> getset(int i){
		return choice.get(i);
	}
	public synchronized void setused(int i,boolean t){
		used.set(i, t);
	}
	public synchronized void setset(int i,int j){
		choice.get(i).add(j);
	}
	public synchronized static PrintStream LogFile(int i){
		return p.get(i);
	}
	public synchronized void setfirst(int i){
		first.set(i, false);
	}
	public synchronized boolean getfirst(int i){
		return first.get(i);
	}
}
