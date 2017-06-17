package oo;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;


public class Request {
	final static String[] s1 = {"renamed","modified","path-changed","size-changed"};
	final static String[] s2 = {"record-detail","record-summary","recover"};
	static Scanner sc = new Scanner(System.in);
	private String str_t;
	private String[] strAll;
	private int tot, n, type1, type2, sum;//tot统计的是所有合法指令的个数，sum统计的是所有不同文件/目录的个数
	private File f;
	private ArrayList<Integer> t1 = new ArrayList<Integer>();//存储所有合法的类型信息的Arraylist
	private ArrayList<Integer> t2 = new ArrayList<Integer>();//存储所有合法的类型信息的Arraylist
	private ArrayList<String> r = new ArrayList<String>();//存储所有合法的类型信息的Arraylist
	
	public Request(){
		
	}
	public void work(){
		tot = sum = 0;
		while (true){
			str_t = sc.nextLine();
			if (str_t.equals("end")){
				/*if (sum < 5){
					System.out.println("Fewer Than 8 Monitors");
					System.exit(0);
				}*/
				System.out.println("Input Done");
				break;
			}
			try{
				strAll = str_t.trim().split(",");
				n = strAll.length;
				if (n != 5 || !strAll[0].equals("IF") || !strAll[3].equals("THEN")){
					System.out.println("Invalid Input");
					continue;
				}
				f = new File(strAll[1]);
				if (f.exists()) {
					//this.path = strl[1];
					r.add(strAll[1]);
				}else{
					System.out.println("Invalid Input");
					continue;
				}
				boolean flag = false;
				for (int i = 0; i < 4; ++i){
					if (strAll[2].equals(s1[i])){
						flag = true;
						type1 = i;
						t1.add(i);
						break;
					}
				}
				if (!flag){
					System.out.println("Invalid Input");
					continue;
				}
				
				flag = false;
				for (int i = 0; i < 3; ++i){
					if (strAll[4].equals(s2[i])){
						flag = true;
						type2 = i;
						t2.add(i);
						break;
					}
				}
				if (!flag){
					System.out.println("Invalid Input");
					continue;
				}
				if ((type2==2) && (type1 == 1 || type1 == 3)){
					System.out.println("Invalid Input");
					continue;
				}
				
				int sign = 0 ;
				for (int i = 0; i < tot; ++i){
					if (type1==t1.get(i) && type2==t2.get(i) && strAll[1].equals(r.get(i))){
						//System.out.println("SAME");
						sign = -1;
						break;
					}
					if (strAll[1].equals(r.get(i))){
						sign = 1;
					}
				}
				if (sign == -1)
					continue;
				if (sign == 0)
					sum++;
				if (sum > 8){
					System.out.println("More Than 8 Monitors");
					System.exit(0);
				}
				System.out.println("Input OK");
				tot++;
			}
			catch (Exception e) {}
		}
	}
	public int getType1(int i){
		return t1.get(i);
	}
	public int getType2(int i){
		return t2.get(i);
	}
	public String getR(int i){
		return r.get(i);
	}
	public int getTot(){
		return tot;
	}
}
