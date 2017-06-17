package oo;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

//public class Trigger implements Runnable{ //extends Thread {
public class Trigger extends Thread {
	private String r;
	private int typ, t1, t2, cnt, num;
	//private File[] files;
	private MyFile f;
	private Map<String,Long> v_len, v_time, v_use;
	private Map<String,Boolean> v_new;
	private boolean size_changed_flag,size_changed_d;
	public Trigger(int num_t,String r_t,int t1_t,int t2_t){
		num = num_t;
		r = r_t;
		t1 = t1_t;
		t2 = t2_t;
		v_len = new HashMap<String, Long>();
		v_time = new HashMap<String, Long>();
		v_use = new HashMap<String, Long>();
		v_new = new HashMap<String, Boolean>();
		if ((new MyFile(r)).isFile())
			typ = 1;
		else
			typ = 0;
		size_changed_flag = (t1 == 3 && t2 != 2);
		size_changed_d = true;
		cnt = 0;
	}
	
	
	public void run(){
		while (true){
			try {
				sleep(777);//Thread.sleep(777);
			} catch (Exception e) {}
	synchronized (Trigger.class) 
	{
			try{
				run_work();
			
				cnt++;
				if (num == 1)
					Main.getTask().out_summary();
			}
			catch (Exception e) {}
	}
			//try{
			//	wait();
			//}
			//catch (Exception e){
			//}
		}	
	}
	
	public void run_work(){
		//if (!(new MyFile(r)).exists())
		//	return;
		if (!(new MyFile(r)).exists()){
			if (size_changed_flag && !size_changed_d){
				//System.out.println();
				return ;
			}
			if (t1 == 1)//modified
				return;
		}
		
		//if ((new MyFile(r)).exists())
		{		
			if (t1 == 0)
				work_r(r, cnt, typ);
				
			if (t1 == 1)
				work(r, cnt);
			
			if (t1 == 2)
				work_p(r, cnt, typ);
			
			if (t1 == 3)
				work_s(r, cnt, typ);
			
			for (String key: v_len.keySet()){//遍历键,无序//System.out.println(key +" "+ v_use.get(key)+" "+cnt);
				if (v_use.get(key) != cnt && v_use.get(key) >= 0){
					v_use.put(key, (long)-1);
					if (t1==1&&typ==0){//modified
						System.out.println("modified "+((new File(r)).getParent())+" delete a new file");
						if (t2==0)
							Main.DetailFile().println("modified "+((new File(r)).getParent())+" delete a new file");
						Main.getTask().add(t1);
					}
					if (t1 == 3){//size-changed
						System.out.println("size-changed "+key+" deleted");
						if (t2==0)
							Main.DetailFile().println("deleted "+key+" deleted");
						Main.getTask().add(t1);
						size_changed_d = false;
					}
					if (t1 == 0 && typ == 1 && key.equals(r)){
						work_r_search(key, typ);//
						break;
					}
					if (t1 == 0 && (new File(r)).isDirectory()){
						work_r_search(key, typ);
					}
					if (t1 == 2 && typ == 1 && key.equals(r)){//因为重命名操作一定不会是检查文件的重命名
						//System.out.println("11111111");	
						work_p_search(key, typ);
					}
					if (t1 == 2 && (new File(r)).isDirectory()){//因为重命名操作一定不会是检查文件的重命名
						work_p_search(key, typ);
					}
				}
			}
		}
	}
	
	public void work(String route,long k){
		//System.out.println("Now:"+route);
		File[] files;
		
		long siz = 0;
		files = new MyFile(route).listFiles();
		
		if (files==null)
			return;
		
		boolean nnew;
		f = new MyFile(route);
		siz = 0;
		
		for (int i = 0; i < files.length; ++i) {
			//System.out.println(files[i].getAbsolutePath()+"   "+files[i].toString());
			nnew = false;
			if (files[i].isFile()){//如果是一个标准文件(不是目录)，在windows下一般没有标准文件和特殊文件的区分，因此等效于f.exist()
				if (v_time.get(files[i].getAbsolutePath()) == null){//上一次没有这个文件
					nnew = true; 
					if (k != 0){
		//System.out.println("modified "+files[i].getParent()+" create a new file");
		//if (t2==0)
			//Main.DetailFile().println("modified "+files[i].getParent()+" create a new file");
		//Main.getTask().add(t1);
					}
					//add(files[i], k);
				}else{
					if (files[i].lastModified() != v_time.get(files[i].getAbsolutePath())){
						if (k != 0){
							System.out.println("modified "+files[i]+" from "+v_time.get(files[i].getAbsolutePath())+" to "+files[i].lastModified());
							if (t2==0)
								Main.DetailFile().println("modified "+files[i]+" from "+v_time.get(files[i].getAbsolutePath())+" to "+files[i].lastModified());
							Main.getTask().add(t1);
						}
						//add(files[i], k);
					}
				}
				add(files[i], k, nnew);
				siz += files[i].length();
			}else{//代表是一个目录，isDirectory() 为true
				work(files[i].toString(), k);
			}
		}
		
		if (k != 0){
			if (v_time.get(route) == null){
				System.out.println("modified "+f.getParent()+" create a new folder");
				if (t2==0)
					Main.DetailFile().println("modified "+f.getParent()+" create a new folder");
				Main.getTask().add(t1);
			}else if (f.lastModified() != v_time.get(route)){
				//System.out.println("modified "+ route + " from "+v_time.get(route)+" to "+f.lastModified());
			}
		}
		
		v_time.put(route, f.lastModified());
		v_len.put(route, siz);//v_len[files[i].toString()] = files[i].length();
		v_use.put(route, k);
	}
	public void work_s(String route,long k,int type){
		//System.out.println("Now:"+route);
		File[] files;
		long siz = 0;
		boolean nnew;
				
		f = new MyFile(route);
		if (type ==1){//file
			files = new File(f.getParent()).listFiles();
		}else{
			files = f.listFiles();
		}
		
		if (files==null)
			return;
		
		siz = 0;
		for (int i = 0; i < files.length; ++i) {
			//System.out.println(files[i].getAbsolutePath()+"   "+files[i].toString());
			nnew = false;		
			if (files[i].isFile()){//如果是一个标准文件(不是目录)，在windows下一般没有标准文件和特殊文件的区分，因此等效于f.exist()
				if (v_time.get(files[i].getAbsolutePath()) == null){
					nnew = true;
					if (k != 0){
						System.out.println("size-changed "+ files[i] + " create-file");
						if (t2==0)
							Main.DetailFile().println("size-changed "+ files[i] + " create-file");
						Main.getTask().add(t1);
					}
					//add(files[i], k);
				}else{
					if (files[i].length() != v_len.get(files[i].getAbsolutePath())){
						if (k != 0){
							System.out.println("size-changed "+files[i].getAbsolutePath()+" from "+v_len.get(files[i].getAbsolutePath())+" to "+files[i].length());
							if (t2==0)
								Main.DetailFile().println("size-changed "+files[i].getAbsolutePath()+" from "+v_len.get(files[i].getAbsolutePath())+" to "+files[i].length());
							Main.getTask().add(t1);
						}
						//add(files[i], k);
					}
				}
				add(files[i], k, nnew);
				siz += files[i].length();
			}else{//代表是一个目录，isDirectory() 为true
				if (type == 0)
					work_s(files[i].toString(), k, type);
			}
		}
		
		if (k != 0 && type == 0){
			if (v_len.get(route) == null){
				System.out.println("size-changed "+route+" create-folder");
				if (t2==0)
					Main.DetailFile().println("size-changed "+route+" create-folder");
				Main.getTask().add(t1);
			}else if (siz != v_len.get(route)){
				System.out.println("size-changed "+ route + " from "+v_len.get(route)+" to "+siz);
				if (t2==0)
					Main.DetailFile().println("size-changed "+ route + " from "+v_len.get(route)+" to "+siz);
				Main.getTask().add(t1);
			}
		}
		
		if (type == 0){
			v_time.put(route, f.lastModified());
			v_len.put(route, siz);//v_len[files[i].toString()] = files[i].length();
			v_use.put(route, k);
		}
	}
	public void work_p(String route,long k,int type){//!!!!!
		//System.out.println("path-changed"+"Now:"+route+typ);
		File[] files;
		boolean nnew;
		f = new MyFile(route);
		
		if (type ==1){//file
			files = new File(f.getParent()).listFiles();
		}else{
			files = f.listFiles();
		}	
		
		if (files==null)
			return;
		
		for (int i = 0; i < files.length; ++i) {
			//System.out.println(files[i].getAbsolutePath()+"   "+files[i].toString());
			nnew = false;
			if (files[i].isFile()){//如果是一个标准文件(不是目录)，在windows下一般没有标准文件和特殊文件的区分，因此等效于f.exist()
				if (v_time.get(files[i].getAbsolutePath()) == null)
					nnew = true;
				add(files[i], k, nnew);
			}else{//代表是一个目录，isDirectory() 为true
				work_p(files[i].getAbsolutePath(), k, 0);
			}
		}
	}
	public void work_r(String route,long k,int type){//!!!!!
		//System.out.println("renamed"+"Now:"+route+typ);
			File[] files;
			boolean nnew;
			f = new MyFile(route);
			if (type ==1){//file
				files = new File(f.getParent()).listFiles();
			}else{
				files = f.listFiles();
			}
			
			if (files==null)
				return;
			
			for (int i = 0; i < files.length; ++i) {
				//System.out.println(files[i].getAbsolutePath()+"   "+files[i].toString());
				nnew = false;
				if (files[i].isFile()){//如果是一个标准文件(不是目录)，在windows下一般没有标准文件和特殊文件的区分，因此等效于f.exist()
					if (v_time.get(files[i].getAbsolutePath()) == null)
						nnew = true;
					add(files[i], k, nnew);
				}else{//代表是一个目录，isDirectory() 为true
					if (type == 0)//如果监测的是文件夹，需要向下搜索
						work_r(files[i].toString(), k, 0);
				}
			}
	}
	public void work_r_search(String route,int type){
		//System.out.println("Now:"+route);
		File[] files;
		f = new MyFile(route);
		files = new File(f.getParent()).listFiles();
		
		if (files==null)
			return;
		
		for (int i = 0; i< files.length; ++i) {
			//System.out.println(files[i].getAbsolutePath()+"   "+f.getAbsolutePath()+" "+v_new.get(files[i].getAbsolutePath()));
			if (files[i].isFile()&&v_new.get(files[i].getAbsolutePath())&&files[i].getParent().equals(f.getParent())&&!files[i].getName().equals(f.getName())){
				//if (t2!=2)
				System.out.println("renamed "+route+" from "+f.getName()+" to "+files[i].getName());
				if (t2==0)
					Main.DetailFile().println("renamed "+route+" from "+f.getName()+" to "+files[i].getName());
				//if (t2!=2)
				Main.getTask().add(t1);
				v_new.put(files[i].getAbsolutePath(), false);
				if (type == 1){//FILE
					if (t2 != 2)
						this.r = files[i].getAbsolutePath();//this.f = (MyFile) files[i].getAbsoluteFile();
				}
				if (t2 == 2){//recover
					MyFile temp = new MyFile(files[i].getAbsolutePath());
					temp.rename(route);
					System.out.println("Rename A file OK!");
				}
			}
		}
	}
	public void work_p_search(String route,int type){
		//System.out.println("Now:"+route);
		File[] files;
		f = new MyFile(route);
		files = new File(f.getParent()).listFiles();
		
		if (files==null)
			return;
		
		MyFile temp;
		
		for (String key: v_len.keySet()){//
			temp = new MyFile(key);
			//System.out.println(v_new.get(key)+","+temp.getParent()+","+f.getParent()+","+temp.getName()+","+f.getName());	
			if (v_use.get(key)==cnt&&v_new.get(key)&&!temp.getParent().equals(f.getParent())&&temp.getName().equals(f.getName())){
				System.out.println("path-changed from "+route+" to "+temp.getAbsolutePath());
				if (t2==0)
					Main.DetailFile().println("path-changed from "+route+" to "+temp.getAbsolutePath());
				Main.getTask().add(t1);
				v_new.put(temp.getAbsolutePath(), false);
				if (type == 1){//FILE
					if (t2 != 2)
						this.r = temp.getAbsolutePath();//this.f = temp.getAbsolutePath();
						//System.out.println("YES");
				}
				if (t2 == 2){//recover
					//MyFile temp = new MyFile(files[i].getAbsolutePath());
					temp.rename(route);
					System.out.println("Rename A file OK!");
				}
			}
		}
	}
	public void add(File fff,long k,boolean t){
		v_time.put(fff.getAbsolutePath(), fff.lastModified());
		v_len.put(fff.getAbsolutePath(), fff.length());//v_len[files[i].toString()] = files[i].length();
		v_use.put(fff.getAbsolutePath(), k);
		v_new.put(fff.getAbsolutePath(), t);
	}
}