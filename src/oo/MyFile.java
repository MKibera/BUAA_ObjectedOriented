package oo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

class MyFile extends File//
{
	private File f_self;
	public MyFile(String str){
		super(str);
		f_self = new File(str);
	}

	public synchronized long lastModified(){//返回最后修改时间
		try{
			return super.lastModified();
		}
		catch (Exception e){
			return 0;
		}
	}
	
	public synchronized File[] listFiles(){//把当前目录里的东西拆分成文件数组
		try{
			return super.listFiles();
		}
		catch (Exception e){
			return null;
		}
	}

	public synchronized boolean isFile(){//是不是文件
		try{
			return super.isFile();
		}
		catch (Exception e){
			return false;
		}
	}

	public synchronized String getParent(){//求父目录的字符串
		try{
			return super.getParent();
		}
		catch (Exception e){
			return null;
		}
	}
	
	public synchronized boolean createNewFile() throws IOException{ //创建一个文件
		//File f = new File(this.getAbsolutePath());
		/*File f_parent = new File(this.getParent());
		if (f.exists()){
			System.out.println("Already Exists!");
			return false;
		}
		if (!f_parent.exists()){
			System.out.println("Parent Not Exists!");
			return false;
		}*/
		try{
			boolean flag;
			//flag = f.createNewFile();//this.createNewFile(); XXXXXX!!!
				flag = super.createNewFile();
			if (!flag){
				System.out.println("Create Error!");
			}
			return flag;
		}
		catch (Exception e){
			return false;
		}
	}
	
	public synchronized long length(){//读取文件大小
		try{
			return super.length();
		}
		catch (Exception e){
			return 0;
		}
	}
	
	public synchronized String getName(){//读取文件名称
		try{
			return super.getName();
		}
		catch (Exception e){
			return null;
		}
	}
	
	public synchronized String getAbsolutePath(){//读取文件路径
		try{
			return super.getAbsolutePath();
		}
		catch (Exception e){
			return null;
		}
	}
	
	public synchronized boolean exists(){//文件是不是存在
		try{
			return super.exists();
			//File f = new File(this.getAbsolutePath());
			//return f.exists();
		}
		catch (Exception e){
			return false;
		}
	}
	
	public synchronized boolean mkdir(){//创建一个文件夹，不会递归创建上层的没有出现的文件夹
		try{
//File f = new File(this.getAbsolutePath());
			boolean flag;
//flag = f.mkdir();
			flag = super.mkdir();
			return flag;
		}
		catch (Exception e){
			return false;
		}
	}
	
	public synchronized boolean mkdirs(){//创建一个文件夹，会递归创建上层的没有出现的文件夹
		try{
//File f = new File(this.getAbsolutePath());
			boolean flag;
//flag = f.mkdirs();
			flag = super.mkdirs();
			return flag;
		}
		catch (Exception e){
			return false;
		}
	}
	
	public synchronized boolean delete()//删除一个文件或者文件夹，与FILE类不同的是，删除文件夹可以什么都删除，而FILE类只能删除空文件夹
	{
		try{
//File f = new File(this.getAbsolutePath());
			boolean flag;
			flag = true;
			File[] files;
		
//if (!f.exists()){
//if (!super.exists()){
//return false;
//}
//if (f.isFile()){
//flag = f.delete();
//}
			if (super.isFile()){
				flag = super.delete();
			}
			else {
//files = f.listFiles();
				files = super.listFiles();
				for (File t : files){
					MyFile temp = new MyFile(t.getAbsolutePath());
					flag = flag & t.delete();
				}
//flag = f.delete();
				flag = super.delete();
			}
			return flag;
		}
		catch (Exception e){
			return false;
		}
	}
	public synchronized void rename(String nnew){//重命名一个文件去另一个路径
		try{
			File new_f;
			boolean flag;
			new_f = new File(nnew);
			flag = this.renameTo(new_f);
			if (!flag){
				System.out.println("Rename Error!");
			}
		}
		catch (Exception e){
			return ;
		}
	}
	
	public synchronized boolean rewrite(String str)//文件写入
	{
		try {
//File f = new File(this.getAbsolutePath()+"\\"+path);
//System.out.println("FUCK");
			if (f_self.isFile()) {
				FileOutputStream outfile = new FileOutputStream(f_self);
				outfile.write((str).getBytes());//str+"\r\n":增加换行符
				outfile.close();
				return true;
			}
			return false;
		}
		catch (Exception e){
			return false;
		}	
	}
}