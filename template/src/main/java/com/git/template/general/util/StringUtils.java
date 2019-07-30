package com.git.template.general.util;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
	
	private StringUtils(){};
	
	/**
	 * enhance the origin replaceAll function of String;
	 * 	originally ,you could use $1...$n to get the matched group but u are hardly to translate it;
	 * 	now ,with the last parameter of this method(a Function),u can use this method as in JS
	 * @param input
	 * @param regex
	 * @param replacer a Function,which could accept a String[](the matched groups) as parameter ,and return a String,as the replaced String
	 * @return
	 */
	public static String replace(String input,String regex,Function<String[],String> replacer) {
		if(input==null || regex ==null) return input;
		StringBuilder sb = new StringBuilder();
		
		Matcher m = Pattern.compile(regex).matcher(input);
		int start = 0;
		
		while(m.find()) {
			// 添加未被匹配到的部分
			sb.append(input.substring(start,m.start()));
			start = m.end();
			
			// 添加匹配到的部分(被替换后)
			String[] matched = new String[m.groupCount()+1];
			for(int i =0;i<=m.groupCount();i++) {
				matched[i] = m.group(i);
			}
			sb.append(replacer.apply(matched));
		}
		
		sb.append(input.substring(start));
		return sb.toString();
	}
	
	public static String toCamel(String input) {
		return replace(input,"_([a-z])",strs->{
			return strs[1].toUpperCase();
		}) ;
	}
	
	public static String antiCamel(String input) {
		return replace(input,"([a-z0-9])([A-Z])",strs->{
			return new StringBuilder().append(strs[1]).append("_").append(strs[2].toLowerCase()).toString();
		}) ;
	}
	
	public static void main(String[] args) {
		String input = "abCd";
		System.out.println(antiCamel(input));
		
		String s = "ab_cd";
		System.out.println(toCamel(s));
	}
	
}
