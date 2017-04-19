package elevator;
public class Request {
	//static final Long max_time =  2147483647L;//可以读到的时间的最大数
	private int maxFloor, minFloor;//表示读入的楼层允许的最大值和最小值
	private int i, j, l, type, num1, updown;
	private int num2;
	//i,j:循环变量  l:字符串的长度
	//type:该行读入的类型(FR还是ER) num1:读入的第一个数(楼层)
	//num2:读入的第二个数(类型) updown:上下信息
	private boolean sign;//标记这个表达式是否合法
	private String str;//读入的字符串
	private char[] strArr;//把读入的字符串转化为的字符数组
	Request(String s,int min_f,int max_f){//构造函数，初始化一些变量
		l = s.length();
		strArr = s.toCharArray();
		str = s;
		minFloor = min_f;
		maxFloor = max_f;
	}
	void work(){//主要的处理字符串过程，判断其是否合法，并保存信息到布尔变量sign
	  try{
		sign = false;//初始为不合法
		if (l < 9){
			return;
		}
		updown = -1;
		
		if (str.substring(0,4).equals("(FR,")){//if (strArr[0]=='('&&strArr[1]=='F'&&strArr[2]=='R'&&strArr[3]==','){
			type = 1;
			for (j = 4;j < l; ++j){
				if (strArr[j]==','){
						break;
				}
			}
			num1 = Integer.parseInt(str.substring(4,j));
			i = j+1;
		}else if (str.substring(0,5).equals("(ER,#")){//if (strArr[0]=='('&&strArr[1]=='E'&&strArr[2]=='R'&&strArr[3]==','){
				type = 2;
				for (j = 5;j < l; ++j){
					if (strArr[j]==','){
						break;
					}
				}
				num1 = Integer.parseInt(str.substring(5,j));
				i = j+1;
				}else{
					return;
				}
		if (type==1){
			if (str.substring(i,i+2).equals("UP")){
				updown = 1;//UP 原来0是UP, 1是down
				i = i+2;
			}else if (str.substring(i,i+4).equals("DOWN")){
				updown = -1;//DOWN
				i = i+4;
			} 
		}
		if (type == 2){
			for (j = i;j < l; ++j){
				if (strArr[j]==')'){
					break;
				}
			}
			num2 = Integer.parseInt(str.substring(i,j));
		}else
			j = i;
		if (j == l-1 && strArr[j]==')'){
			sign = false;
			if (type == 1){
				if (((num1 > minFloor && num1 < maxFloor) || (num1==minFloor&&updown == 1) 
						|| (num1==maxFloor&&updown==-1) ))
					sign = true;
			}
			if (type == 2){
				if (num1 >= 1 && num1 <=3 && num2 >= minFloor && num2 <= maxFloor)
					sign = true;
			}
		}
	  }
	  catch (Exception e){
		  sign = false;
		  return;
	  }
	}
	boolean getsign(){//返回sign，若为true代表这行没有语法错误
		return sign;
	}
	int getnum1(){//返回表达式的第一个数，为楼层信息
		if (type == 2)
			return num2;
		return num1;
	}
	int getnum2(){
		if (type == 2)
			return num1;
		return 0;
	}
	int getupdown(){//返回是上还是下
		return updown;
	}
	int gettype(){//返回时FR还是ER
		return type;
	}
}
