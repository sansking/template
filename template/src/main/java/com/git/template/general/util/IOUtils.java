package com.git.template.general.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class IOUtils {
	
	private static int byteLimitation = 1 << 31;
	public static int getByteLimitation() {
		return byteLimitation;
	}
	public static void setByteLimitation(int byteLimitation) {
		IOUtils.byteLimitation = byteLimitation;
	}
	
	
	private IOUtils(){}
	
	public static List<String> getFileByLine(File file,String charset){
		return getFileByLine(file,charset,true);
	}
	public static List<String> getFileByLine(File file,boolean trim){
		return getFileByLine(file,"utf-8",trim);
	}
	public static List<String> getFileByLine(File file){
		return getFileByLine(file,"utf-8");
	}
	public static List<String> getFileByLine(File file,String charset,boolean trim){
		checkFile(file);
		List<String> list = new ArrayList<>();
		
		try(
			BufferedReader br = new BufferedReader(
					new InputStreamReader(
						new FileInputStream(file),charset));){
			String line;
			while((line=br.readLine())!=null){
				if(trim) list.add(line.trim());
				else list.add(line);
			}
			return list;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private static void checkFile(File file){
		if(file==null)
			throw new RuntimeException("文件为空");
		if(!file.exists())
			throw new RuntimeException("文件不存在");
		if(file.isDirectory())
			throw new RuntimeException("需要文件,但传入的是文件夹"+file.getAbsolutePath());
		
	}
	
	
	public static String getFile(File file,String charset,boolean trimEachLine){
		List<String> list = getFileByLine(file,charset,trimEachLine);
		StringBuffer sb = new StringBuffer();
		for (String string : list) {
			sb.append(string).append(System.lineSeparator());
		}
		if(sb.length()>0){
			return sb.substring(0, sb.lastIndexOf(System.lineSeparator()));
		}else{
			// 如果文件没有任何内容,则返回空字符串
			return "";
		}
	}
	public static String getFile(File file){
		return getFile(file,"utf-8",true);
	}
	public static String getFile(File file,String charset){
		return getFile(file,charset,true);
	}
	public static String getFile(File file,Boolean trimEachLine){
		return getFile(file,"utf-8",trimEachLine);
	}
	
	public static byte[] getBinaryFile(File file){
		checkFile(file);
		try(
			BufferedInputStream bis = new BufferedInputStream(
				new FileInputStream(file));	
		){
			int len = bis.available();
			if(len < byteLimitation){
				byte[] bytes = new byte[len];
				bis.read(bytes);
				return bytes;
			}else{
				throw new RuntimeException("文件过大,读取失败!");
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	
	
	public boolean writeTo(String src,String path,String charset){
		try(
			BufferedWriter bw = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(path),charset));
			){
			
			bw.write(src);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean writeTo(String src,String path){
		return writeTo(src,path,"utf-8");
	}
	
}
