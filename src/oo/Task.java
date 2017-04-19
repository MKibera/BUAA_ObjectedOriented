package oo;

import java.util.Arrays;

public class Task {
	final static String[] s1 = {"renamed ","modified ","path-changed ","size-changed "};
	private int sum[] = new int[5];//{"renamed","modified","path-changed","size-changed"};
	public Task(){
		Arrays.fill(sum, 0);
	}
	public synchronized void add(int i){
		sum[i]++;
	}
	public synchronized void out_summary(){
		try{
			for (int i = 0; i < 4; ++i){
				System.out.println("Trigger "+s1[i]+sum[i]+" times");
				Main.SummaryFile().println("Trigger "+s1[i]+sum[i]+" times");
			}
			System.out.println("------------------------------");
			Main.SummaryFile().println("------------------------------");
		}
		catch (Exception e) {}
	}
}
