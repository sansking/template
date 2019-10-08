package com.git.template.general.util;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
	
	private StringUtils(){};
	
	/**
	 * 用于增强String的replace方法,实现类似js中的效果: str.replace(/regex/,function(){});
	 * @param input
	 * @param regex
	 * @param replacer 接收String[]作为参数的函数,该String数组为每一次匹配得到的group数组
	 * 					该函数的返回值会替代匹配到的那一部分字符串
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
