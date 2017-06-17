package oo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Map {
	private static int g[][] = new int[85][85];
	private int n, x;
	public void work(String s){
		File file = new File(s);
		InputStreamReader isr;
        BufferedReader buff;
        try {
        	InputStream instr = new FileInputStream(file);
            isr = new InputStreamReader(instr);
            buff = new BufferedReader(isr);
            String str;
            n = 1;
            while ((str = buff.readLine()) != null)
            {
            	str = str.replaceAll(" ", "");
            	str = str.replaceAll("\t", "");
            	int l;
            	l = str.length();
            	if (l != 80){
            		System.out.println("Map Invalid!!!");
            		System.exit(0);
            	}
            	//System.out.println(str);
            	for (int i = 0; i < 80; ++i){
            		x = str.charAt(i) - '0';
            		if (x < 0 || x > 3){
            			System.out.println("Map Invalid!!!");
            			System.exit(0);
            		}
            		g[n][i+1] = x;
            	}
            	if (n > 80){
                	System.out.println("Map Invalid!!!");
        			System.exit(0);
                }
            	n++;
            }
            System.out.println("Map Is Okay");
        }
        catch (Exception e){}
	}
	
	public static int[][] getg(){
		return g;
	}
}
