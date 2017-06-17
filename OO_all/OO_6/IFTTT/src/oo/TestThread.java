package oo;

public class TestThread extends Thread {

	public void run(){
		try {
			sleep(2000);//Thread.sleep(2000);
		} catch (Exception e) {}
		synchronized (Trigger.class) 
		{
			try{
				/*
				 * 你可以在下面输入支持的线程安全类操作
				 */
				
				//MyFile sss,fff,ggg;
				//sss = new MyFile("D://hello.txt");
				//System.out.println(sss.createNewFile());
				//System.out.println("Last Modified time:"+sss.lastModified());
				//System.out.println("Length:"+sss.length());
				//System.out.println(sss.rewrite("6666"));
					
				//fff = new MyFile("D://HI.txt");
				//fff.createNewFile();
				//ggg = new MyFile("D://HIII.txt");
				//System.out.println(fff.renameTo(ggg));
			}
			catch (Exception e) {}
		}
	}
}
