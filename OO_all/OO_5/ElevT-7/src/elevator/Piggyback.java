package elevator;

import java.util.Comparator;

public class Piggyback {
	private int i;  
    private int f;  
    public Piggyback(int i,int f)  
    {  
        this.i = i;  
        this.f = f;  
    }  
    public int getI()  
    {  
         return this.i;  
    }  
  
    public int getF()  
    {  
         return this.f;  
    }  
    /*public String toString()  
    {  
         return getName() + " - " + getPopulation();  
    }  */
    static Comparator<Piggyback> Order =  new Comparator<Piggyback>(){  
            public int compare(Piggyback o1, Piggyback o2) {  
                // TODO Auto-generated method stub  
                int f1 = o1.getF();  
                int f2 = o2.getF();
                int i1 = o1.getI();
                int i2 = o2.getI();
                if (myabs(f1)!=myabs(f2)){
                	return (myabs(f1)-myabs(f2));
                }
                return (i1-i2);
                /*if(myabs(f1) < myabs(f2)){  
                    return 1;  
                }  
                return 0;*/
                /*else if(myabs(f1) > myabs(f2))
                {  
                    return -1;  
                }  
                else  
                {  
                    return 0;  
                } */ 
            }  
        };  
        //Queue<test> priorityQueue =  new PriorityQueue<test>(11,OrderIsdn);  
          
                  
              
        /*test t1 = new test("t1",1);  
        test t3 = new test("t3",3);  
        test t2 = new test("t2",2);  
        test t4 = new test("t4",0);  
        priorityQueue.add(t1);  
        priorityQueue.add(t3);  
        priorityQueue.add(t2);  
        priorityQueue.add(t4);
        System.out.println(priorityQueue.poll().toString());  */
    public static Comparator<Piggyback> getCmp(){
    	return Order;
    }
    static int myabs(int t){//求一个数的绝对值
		return (t>=0)?t:-t;
	}
}
