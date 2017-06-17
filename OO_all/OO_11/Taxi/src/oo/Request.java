package oo;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Scanner;

public class Request {
	private String str;
	static Scanner sc = new Scanner(System.in);
	private int i, j, k, l, h, num1, num2, num3, num4, type;
	final static String ltype[] = {"[CR,(","[CLOSE,(","[OPEN,("};//[TRACK,(1),(1,2,4)]
	private boolean sign;
	static int ll[] = {5,8,7};
	public Request(String str_t){
		/*@ REQUIRES: None;
		@ MODIFIES: str, l;
		@ EFFECTS: str == str_t;
				   l == str.length();
		*/
		str = str_t;
		l = str.length();
	}
	public boolean repOK(){
		/*@ REQUIRES: None;
		@ MODIFIES: None;
		@ Effects: \result == invariant(this);
		@ Invariant(this): l >= 16 && sign == true;
		*/
		if (l < 16)
			return false;
		boolean sign;
		
		for (int i = 0; i < l; ++i){
			sign = false;
			if (str.charAt(i)==']'||str.charAt(i)!=')'||(str.charAt(i)>='0'&&str.charAt(i)<='9')){
				sign = true;
				continue;
			}
			for (String s:ltype){
				for (int j = 0; j < s.length(); ++j){
					if (str.charAt(i) == s.charAt(j)){
						sign = true;
						break;
					}
				}
				if (sign){
					break;
				}
			}
			if (!sign)
				return false;
		}
		return true;
	}
	

	public void work(){//[CR,src,dst]//[CR,(x1,y1),(x2,y2)]
		/*@ REQUIRES: None;
		@ MODIFIES: str, l, sign, i, j, k, l, h, num1, num2, num3, num4, type;
		@ EFFECTS: normal_behavior
				   查询这个字符串的合法性
				   (\exist int i ; !str.char[i] exists) ==> exceptional_behavior(sign = false)
		*/
		try{
			sign = false;
			
			if (l < 16){
				//System.out.println("Invalid Customer Request");
				return;
			}
			boolean sign_1 = false;
			
			i = 0;
			for (k = 0; k < 3; ++k){
				h = ll[k];
				if (str.substring(0,h).equals(ltype[k])){
					for (j = h; j < l; ++j){
						if (str.charAt(j) == ',')
							break;
					}
					num1 = Integer.parseInt(str.substring(h,j));
					i = j+1;
					sign_1 = true;
					type = k;
					break;
				}
			}
			if (!sign_1){
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
			if (sign && num1 == num3 && num2 == num4){//如果发出的请求的地点和当前地点一样，则为无效请求
				sign = false;
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
		/*@ REQUIRES: None;
		@ MODIFIES: None;
		@ EFFECTS: (x >= 1 && x <= 80) ==> \result = true;
				   (x >= 1 && x <= 80) ==> \result = false;
		*/
		return (x>=1 && x<= 80);
	}
	public boolean getsign(){//返回sign，若为true代表这行没有语法错误
		/*@ REQUIRES: None;
		@ MODIFIES: None;
		@ EFFECTS: \result == sign;
		*/
		return sign;
	}
	public int getnum1(){//返回表达式的第一个数，为楼层信息
		/*@ REQUIRES: None;
		@ MODIFIES: None;
		@ EFFECTS: \result == num1;
		*/
		return num1;
	}
	public int getnum2(){
		/*@ REQUIRES: None;
		@ MODIFIES: None;
		@ EFFECTS: \result == num2;
		*/
		return num2;
	}
	public int getnum3(){
		/*@ REQUIRES: None;
		@ MODIFIES: None;
		@ EFFECTS: \result == num3;
		*/
		return num3;
	}
	public int getnum4(){
		/*@ REQUIRES: None;
		@ MODIFIES: None;
		@ EFFECTS: \result == num4;
		*/
		return num4;
	}
	public int gettype(){
		/*@ REQUIRES: None;
		@ MODIFIES: None;
		@ EFFECTS: \result == type;
		*/
		return type;
	}
}