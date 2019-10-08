package com.git.template.general.srcviewer;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class Viewer {
	
	
	private static final URL targetURL = Viewer.class.getResource("/") ;
	private static String srcDir;
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		File targetDir = new File(targetURL.getFile());
		File parent = targetDir.getParentFile().getParentFile();
		
		File javaSrc = new File(parent,"src/main/java");
		
		fallThrough(javaSrc,0);
	}

	private static void fallThrough(File src,int rank) {
		showDir(src,rank++);	
		if(src.isDirectory()){
			for(String child : src.list()){
				File childFile = new File(src,child);	
				if(!childFile.isDirectory()) showThis(childFile.getName(), rank);
			}
			
			for(String child : src.list()){
				File childFile = new File(src,child);			
				if(childFile.isDirectory()) fallThrough(childFile, rank);
			}
		}
	}
	private static List<String> hasShown = new ArrayList<>();
	
	private static void showDir(File src, int rank) {
		String rankStr = Integer.toString(rank);
		if(hasShown.contains(rankStr)) return;
		else System.out.println(src+":");
	}

	
	

	private static void showThis(String childFile, int rank) {
		for (int i=0;i<rank;i++) {
			System.out.print("-");
		}
		System.out.println(childFile);
	}
}
