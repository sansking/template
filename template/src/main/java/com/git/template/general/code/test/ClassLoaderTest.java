package com.git.template.general.code.test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import com.git.template.general.util.DynamicLoader;

/**
 * MemoryClassLoader和StringClassLoader的使用方式
 */
public class ClassLoaderTest {
	public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		String str = "package com.git.template.test1;"+"\r\n"+
				"import java.util.ArrayList;"+"\r\n"+
				"import java.util.List;"+"\r\n"+
				"public class ShowMsg {"+"\r\n"+
					"public static void main(String[] args) {"+"\r\n"+
					"	List<String> list = new ArrayList<>();"+"\r\n"+
					"	list.add(\"Show List Message1\");"+"\r\n"+
					"	System.out.println(list);"+"\r\n"+
					"}"+"\r\n"+
				"}";
		
		Map<String, byte[]> bytecode = DynamicLoader.compile(str);
		DynamicLoader.MemoryClassLoader classLoader = new DynamicLoader.MemoryClassLoader(bytecode);
	    Class clazz = classLoader.loadClass("com.git.template.test1.ShowMsg");
	    Method method = clazz.getMethod("main",String[].class);
	    method.invoke(null, (Object)args);
	    
	    
	    // method = null;
	    // clazz = null;
	    
	    String str2 = "package com.git.template.test1;"+"\r\n"+
				"import java.util.ArrayList;"+"\r\n"+
				"import java.util.List;"+"\r\n"+
				"public class ShowMsg {"+"\r\n"+
					"public static void main(String[] args) {"+"\r\n"+
					"	List<String> list = new ArrayList<>();"+"\r\n"+
					"	list.add(\"Show NEXT Message2\");"+"\r\n"+
					"	System.out.println(list);"+"\r\n"+
					"}"+"\r\n"+
				"}";
	    Map<String, byte[]> obyte = DynamicLoader.compile(str2);
	    DynamicLoader.MemoryClassLoader classLoader2 = new DynamicLoader.MemoryClassLoader(obyte);
	    Class clazz2 = classLoader2.loadClass("com.git.template.test1.ShowMsg");
	    Method method2 = clazz2.getMethod("main",String[].class);
		method2.invoke(null, (Object)args);	   
		
		
		Class clazz3 = Class.forName("com.git.template.test1.ShowMsg");
		Method method3 = clazz3.getMethod("main",String[].class);
		method3.invoke(null, (Object)args);	
	}
}
