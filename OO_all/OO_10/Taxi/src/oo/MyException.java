package oo;

class MyException implements Thread.UncaughtExceptionHandler{
	public boolean repOK(){
		/*@ REQUIRES: None;
		@ MODIFIES: None;
		@ Effects: \result == invariant(this);
		*/
		return true;
	}
	public void uncaughtException(Thread t, Throwable e){
		/*@ REQUIRES: None;
		@ MODIFIES: System.out;
		@ EFFECTS: System.out.println("Exception!");
		*/
		//System.out.println("Exception!");
	}
}
