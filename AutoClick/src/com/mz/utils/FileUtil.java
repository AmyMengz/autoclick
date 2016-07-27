package com.mz.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.mz.autoclick.R;
import com.mz.config.Contants;


import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

public class FileUtil {
	static FileUtil instance;
//	WeakReference<Context> 
	Context context;
	private FileUtil(Context context) {
		this.context=context;
	}
	public static FileUtil getInstance(Context context){
		if(instance==null){
			instance=new FileUtil(context);
		}
		return instance;
	}
	public void copySoundFile(String source,String target){
		InputStream is=null;;
		FileOutputStream fos=null;
		try {
			if(!isSdCardOK()){
				Toast.makeText(context, context.getString(R.string.no_sd_card), Toast.LENGTH_SHORT).show();
				return;
			}
			File dir=checkDir();
			File targetFile=new File(dir, target);
			if(targetFile.exists()) return;
			targetFile.createNewFile();
			is=context.getResources().getAssets().open(source);
			fos=new FileOutputStream(targetFile);
			int tmp=-1;
			byte[] buf=new byte[1024];
			while ((tmp=is.read(buf))!=-1) {
				fos.write(buf, 0, tmp);
			}
			fos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(is!=null){
				try {
					is.close();
					is=null;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}if(fos!=null){
				try {
					fos.close();
					fos=null;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		
	}
	/**
	 * 纪录重启次数与实践
	 */
	public static void recordReboot(){
		FileWriter writer=null;
		try {
			File dir=checkDir();
			File rebootRecord=new File(dir, Contants.RECORD_REBOOT);
			if(!rebootRecord.exists()){
				rebootRecord.createNewFile();
			}
			writer=new FileWriter(rebootRecord,true);
			SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String time= dateFormat.format(new Date(System.currentTimeMillis()));
			writer.write(time+"\n");
			writer.flush();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(writer!=null){
				try {
					writer.close();
					writer=null;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
	}
	public static int getRebootTimes(){
		File dir=checkDir();
		File rebootRecord=new File(dir, Contants.RECORD_REBOOT);
		if(!rebootRecord.exists()){
			return 0;
		}
		BufferedReader  reader=null;
		int count=0;;
		try{
			reader=new BufferedReader(new FileReader(rebootRecord));
			String tmp = null;
			while((tmp=reader.readLine())!=null){
				count++;
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(reader!=null){
				try {
					reader.close();
					reader=null;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return count;
	}
	
	
	/**
	 * 清除重启纪录
	 */
	public static void removeRebootRecord(){
		File dir=checkDir();
		File rebootRecord=new File(dir, Contants.RECORD_REBOOT);
		if(rebootRecord.exists()){
			rebootRecord.delete();
		}
	}
	/**
	 * 父文件检测
	 * @return
	 */
	public static File checkDir(){
		File file=new File(Contants.EXTRA_PATH);
		if(!file.exists()){
			file.mkdirs();
		}
		return file;
	}
	public static boolean isSdCardOK(){
		return Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
	}

}
