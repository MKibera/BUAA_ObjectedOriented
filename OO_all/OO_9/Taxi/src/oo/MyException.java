package oo;

class MyException implements Thread.UncaughtExceptionHandler{
	public void uncaughtException(Thread t, Throwable e){
		/*@ REQUIRES: None;
		@ MODIFIES: System.out;
		@ EFFECTS: System.out.println("Exception!");
		*/
		//System.out.println("Exception!");
	}
}
