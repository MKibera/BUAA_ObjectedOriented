package Poly;

import java.util.Arrays;
import java.util.Scanner;

public class Polynomial {
	final static int maxn = 1000010;
	static int poly[] = new int[maxn];
	private static boolean visit[] = new boolean[maxn]; 
	private static int l, i, flag, 
						sign_num, sign_poly, coff_num, power_num;
	private static char [] strArr;
	
	public Polynomial(String str){//构造函数
		strArr = str.toCharArray();//System.out.println(strArr);
		l = str.length();
		i = 0;
	}
	
	static int work(){
		int tot_pare;//tot_pare: number of Left parenthesis（数对的个数） 
		while (flag==0 && i < l){
			
			pre_work();//检查多项式前的符号
			
			compute_poly_sign();//计算多项式的符号
		    
			Arrays.fill(visit,false);//visit[i]=false 代表指数为i没现过
		    tot_pare = 0;
			while (flag ==0 && strArr[i] != '}'){
		    	
		        compute_sign_num(1, "Lack of cofficent");//计算系数的符号
		        
		        int coff_num_unsigned = calc();//计算系数的值(不考虑符号)
		        
		        coff_num = sign_num * coff_num_unsigned;//系数的值=符号*绝对值
		        
		        compute_sign_num(2, "Lack of power index");//计算指数的符号
		        	
		        int power_num_unsigned = calc();//计算指数的值(不考虑符号)
		        
		        power_num = sign_num * power_num_unsigned;//指数的值=符号*绝对值
		        tot_pare++;//数对的个数增加一个
		        
		        if (power_num < 0 || visit[power_num] || tot_pare > 50){//判断指数n是否小于0，以及这个n是否之前有没有出现过，以及数对的个数大于50
		        	flag = -1;
		        	System.out.println("Invalid input");
		            break;
		        }
		        
		        visit[power_num] = true;//将这个系数更新为true
		        poly[power_num] += coff_num*sign_poly;
		        
		        suf_work();//处理数对的)及，
		    }
		}
		return flag;
	}

	static void compute_poly_sign(){//计算多项式的符号
		if (flag != 0){
			return ;
		}
		sign_poly = 1;
	    if (strArr[i]=='+'){
	        ++i;
	    }
	    else if (strArr[i] == '-'){
	        sign_poly = -1;
	        ++i;
	    }
	    
	    if (strArr[i] != '{'){
	        System.out.println("Invalid input");
	        flag = -1;
	        return ;
	    }
	    else{//strArr[i]=='{'
	        ++i;
	        if (strArr[i]=='}'){
	            //System.out.println("{} is not permitted");
	        	System.out.println("Invalid input");
	            flag = -1;
	            return ;
	        }
	        if (strArr[i]!='('){
	        	System.out.println("Invalid input");
	            flag = -1;
	            return;
	        }
	    }
	}
	
	static void pre_work(){//检查多项式前的符号
		if ((i<l) && (strArr[i]=='}')){
			++i;
		}
		if (i == l){
			flag = 1;//The process exists no problem.
			return ;
		}
		if (strArr[i] != '{'  && strArr[i] != '+' && strArr[i] != '-'){
	            System.out.println("Invalid input");
	            flag = -1;
	            return ;
	    }
	}
	
	static void compute_sign_num(int type, String outstr){//计算数对里的数的符号
		if (flag != 0){
			return ;
		}
		if (type == 1 && strArr[i]=='('){//||strArr[i]==','){
            ++i;
        }
		if (type == 2 && strArr[i]==','){
			++i;
		}
        sign_num = 1;
        if (strArr[i]=='+'){
            ++i;
        }
        else if (strArr[i]=='-'){
            sign_num = -1;
            ++i;
        }
        else if (!isnumber(strArr[i])){
                //System.out.println("Lack of cofficent");
        		//System.out.println(outstr);
        		System.out.println("Invalid input");
                flag = -1;
            }
	}
	
	static int calc(){//计算无符号数
		if (flag != 0){
			return 0;
		}
		int num = 0;//值
		int cnt = 0;//统计除了符号位的出现次数
        while (isnumber(strArr[i])){
                num = num*10 + strArr[i]-'0';
            ++i;
            ++cnt;
            if (cnt == 7){//如果出现次数大于6，直接输出错误信息
            	flag = -1;
            	//System.out.println("The number is wrong");
            	System.out.println("Invalid input");
            	return 0;
            }
        }
        return num;
	}
	static void suf_work(){//处理数对的)及后面的，
		if (flag != 0){
			return ;
		}
		if (strArr[i]==')'){
            ++i;
        }//until the char is ','||'}'
        else{
        	System.out.println("Invalid input");
        	flag = -1;
        	return ;
        }
        
        if (strArr[i]==','){
        	++i;
        }
        else if (strArr[i]!='}'){
        	System.out.println("Invalid input");
        	flag = -1;
        	return ;
        }
	}
	int inspect_Arr(){//检查是否含有非法字符、单项式多项式的个数限制
		int tot_curl;//tot_curl:number of left curly bracket（多项式的个数）
		tot_curl = 0;
		for (int i = 0; i < l; ++i){
			if (isspace(strArr[i])||isnumber(strArr[i])||strArr[i]=='{'||strArr[i]=='}'||strArr[i]=='+'||strArr[i]=='-'||strArr[i]==','||strArr[i]=='('||strArr[i]==')'){
					if (strArr[i]=='{'){
						tot_curl++;
					}
					continue;
			}
			//System.out.println("Illegal character:"+strArr[i]);
			System.out.println("Illegal character");
			return -1;
		}
		
		if (tot_curl > 20){//如果多项式的个数大于50个
			//System.out.println("Excessive polynomial");
			System.out.println("Invalid input");
			return -1;
		}
		return 0; 
	}
	static boolean isspace(char c){
		return (c == ' ');
	}
	static boolean isnumber(char c){
		return (c >= '0' && c <= '9'); 
	}
}



