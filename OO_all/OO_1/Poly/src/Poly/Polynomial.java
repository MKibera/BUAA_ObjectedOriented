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
	
	public Polynomial(String str){//���캯��
		strArr = str.toCharArray();//System.out.println(strArr);
		l = str.length();
		i = 0;
	}
	
	static int work(){
		int tot_pare;//tot_pare: number of Left parenthesis�����Եĸ����� 
		while (flag==0 && i < l){
			
			pre_work();//������ʽǰ�ķ���
			
			compute_poly_sign();//�������ʽ�ķ���
		    
			Arrays.fill(visit,false);//visit[i]=false ����ָ��Ϊiû�ֹ�
		    tot_pare = 0;
			while (flag ==0 && strArr[i] != '}'){
		    	
		        compute_sign_num(1, "Lack of cofficent");//����ϵ���ķ���
		        
		        int coff_num_unsigned = calc();//����ϵ����ֵ(�����Ƿ���)
		        
		        coff_num = sign_num * coff_num_unsigned;//ϵ����ֵ=����*����ֵ
		        
		        compute_sign_num(2, "Lack of power index");//����ָ���ķ���
		        	
		        int power_num_unsigned = calc();//����ָ����ֵ(�����Ƿ���)
		        
		        power_num = sign_num * power_num_unsigned;//ָ����ֵ=����*����ֵ
		        tot_pare++;//���Եĸ�������һ��
		        
		        if (power_num < 0 || visit[power_num] || tot_pare > 50){//�ж�ָ��n�Ƿ�С��0���Լ����n�Ƿ�֮ǰ��û�г��ֹ����Լ����Եĸ�������50
		        	flag = -1;
		        	System.out.println("Invalid input");
		            break;
		        }
		        
		        visit[power_num] = true;//�����ϵ������Ϊtrue
		        poly[power_num] += coff_num*sign_poly;
		        
		        suf_work();//�������Ե�)����
		    }
		}
		return flag;
	}

	static void compute_poly_sign(){//�������ʽ�ķ���
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
	
	static void pre_work(){//������ʽǰ�ķ���
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
	
	static void compute_sign_num(int type, String outstr){//��������������ķ���
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
	
	static int calc(){//�����޷�����
		if (flag != 0){
			return 0;
		}
		int num = 0;//ֵ
		int cnt = 0;//ͳ�Ƴ��˷���λ�ĳ��ִ���
        while (isnumber(strArr[i])){
                num = num*10 + strArr[i]-'0';
            ++i;
            ++cnt;
            if (cnt == 7){//������ִ�������6��ֱ�����������Ϣ
            	flag = -1;
            	//System.out.println("The number is wrong");
            	System.out.println("Invalid input");
            	return 0;
            }
        }
        return num;
	}
	static void suf_work(){//�������Ե�)������ģ�
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
	int inspect_Arr(){//����Ƿ��зǷ��ַ�������ʽ����ʽ�ĸ�������
		int tot_curl;//tot_curl:number of left curly bracket������ʽ�ĸ�����
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
		
		if (tot_curl > 20){//�������ʽ�ĸ�������50��
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



