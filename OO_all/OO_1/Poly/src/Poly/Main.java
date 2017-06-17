package Poly;

import java.io.*;
import java.math.*;
import java.util.*;

public class Main{
	final static int maxn = 1000010;//ȫ�ֳ���
	public static String str;
	public static int l, i, 
					   flag;//flag�������Ķ���ʽ�Ƿ��Ǵ���ı�־�������-1�����Ѽ����Ƿ���0������ʱû����1������ȷ��
    static Polynomial p;
	
	public static void main(String[] args) {
		try{
			String str = input_pre();//����һ���ַ���,Ԥ����(ɾ���ո�)
			if (str == null || str.length() <= 0 || (str.length()==1&&str.charAt(0)=='}')){//����ǿ��ַ���������Ϊ}��ֱ�����
				System.out.println("Invalid input");
				return ;
			} 
		
			p = new Polynomial(str);//����һ������ʽ�Ķ���
			flag = p.inspect_Arr();//����Ƿ��зǷ��ַ�������ʽ����ʽ�ĸ�������
			if (flag == 0){
				flag = Polynomial.work();//ִ�м����������
			}
			if (flag == 1){
				out();//�������ʽ
			}
		}
		catch (Exception e){
			System.out.println("Invalid input");
		}
}
	
	
	static String input_pre(){//����һ���ַ���,Ԥ����(ɾ���ո�)
		Scanner sc = new Scanner(System.in);
		String str_t;
		str_t = sc.nextLine();
		sc.close();
		str_t = str_t.replaceAll(" +","");//Delete all spaces.
		return str_t;
	}
	
	public static void out(){//�������ʽ
		int tot = 0;
		boolean flag_zero;//�����ǲ��ǿն���ʽ�ı�־
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
        if (flag_zero){//����ǿն���ʽ�������{(0,0)}
        	System.out.println("{(0,0)}");
        }
        else{
        	System.out.println("}");
        }
	}
}
