package oo;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Scanner;

public class Request {
	private String str;
	static Scanner sc = new Scanner(System.in);
	private int i, j, l, num1, num2, num3, num4;
	private boolean sign;
	public Request(String str_t){
		str = str_t;
		l = str.length();
	}
	public void work(){//[CR,src,dst]
		//[CR,(x1,y1),(x2,y2)]
		try{
			sign = false;
			
			if (l < 16){
				//System.out.println("Invalid Customer Request");
				return;
			}
				
			i = 0;
			if (str.substring(0,5).equals("[CR,(")){
				for (j = 5;j < l; ++j){
					if (str.charAt(j) == ',')
						break;
				}
				num1 = Integer.parseInt(str.substring(5,j));
				i = j+1;
			}else{
				//System.out.println("Invalid Customer Request");
				//continue;	
				return ;
			}
			for (j = i; j < l; ++j){
				if (str.charAt(j) == ')')
					break;
			}
			num2 = Integer.parseInt(str.substring(i,j));
			i = j+3;//),(
			
			for (j = i; j < l; ++j){
				if (str.charAt(j) == ',')
					break;
			}
			num3 = Integer.parseInt(str.substring(i,j));
			i = j+1;
			
			for (j = i; j < l; ++j){
				if (str.charAt(j) == ')')
					break;
			}
			if (j == l-2 && str.charAt(j) == ')' && str.charAt(j+1) == ']'){
				num4 = Integer.parseInt(str.substring(i,j));
			}else{
				//System.out.println("Invalid Customer Request");
				//continue;
				return ;
			}
			if (find(num1)&&find(num2)&&find(num3)&&find(num4)){
				sign = true;
				//Main.gui.RequestTaxi(new Point(num1-1,num2-1), new Point(num3-1,num4-1));
			}
			//System.out.println("Customer Request OK");
			//++tot;
		}
		catch (Exception e){
			sign = false;
			return ;
		}
	}
	public boolean find(int x){
		return (x>=1 && x<= 80);
	}
	public boolean getsign(){//返回sign，若为true代表这行没有语法错误
		return sign;
	}
	public int getnum1(){//返回表达式的第一个数，为楼层信息
		return num1;
	}
	public int getnum2(){
		return num2;
	}
	public int getnum3(){
		return num3;
	}
	public int getnum4(){
		return num4;
	}
}