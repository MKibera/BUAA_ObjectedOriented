package Poly;

import java.io.*;
import java.math.*;
import java.util.*;

public class Main{
	final static int maxn = 1000010;//全局常量
	public static String str;
	public static int l, i, 
					   flag;//flag代表读入的多项式是否是错误的标志。如果是-1代表已检测出非法，0代表暂时没错误，1代表正确。
    static Polynomial p;
	
	public static void main(String[] args) {
		try{
			String str = input_pre();//读入一行字符串,预处理(删除空格)
			if (str == null || str.length() <= 0 || (str.length()==1&&str.charAt(0)=='}')){//如果是空字符串，或者为}则直接输出
				System.out.println("Invalid input");
				return ;
			} 
		
			p = new Polynomial(str);//构造一个多项式的对象
			flag = p.inspect_Arr();//检查是否含有非法字符、单项式多项式的个数限制
			if (flag == 0){
				flag = Polynomial.work();//执行计算的主过程
			}
			if (flag == 1){
				out();//输出多项式
			}
		}
		catch (Exception e){
			System.out.println("Invalid input");
		}
}
	
	
	static String input_pre(){//读入一行字符串,预处理(删除空格)
		Scanner sc = new Scanner(System.in);
		String str_t;
		str_t = sc.nextLine();
		sc.close();
		str_t = str_t.replaceAll(" +","");//Delete all spaces.
		return str_t;
	}
	
	public static void out(){//输出多项式
		int tot = 0;
		boolean flag_zero;//代表是不是空多项式的标志
		flag_zero = true;
        for (i = 0; i < maxn; ++i){
            if (Polynomial.poly[i] != 0){
            	flag_zero = false;
            	if (tot==0){
                    System.out.print("{("+Polynomial.poly[i]+","+i+")");
                }
                else{
                    System.out.print(", ("+Polynomial.poly[i]+","+i+")");
                }
                tot++;
            }
        }
        if (flag_zero){//如果是空多项式，则输出{(0,0)}
        	System.out.println("{(0,0)}");
        }
        else{
        	System.out.println("}");
        }
	}
}
