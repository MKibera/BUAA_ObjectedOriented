package oo;

class MyException implements Thread.UncaughtExceptionHandler{
	public void uncaughtException(Thread t, Throwable e){
		System.out.println("Exception!");
	}
}
