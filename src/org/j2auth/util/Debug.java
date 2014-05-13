package org.j2auth.util;

import java.util.HashMap;
import java.util.Map;

/**
 * debug tool
 * @author volador
 *
 */
public class Debug {
	
	//debug show type
	public static final String FILE = "file";
	public static final String CLASS = "class";
	public static final String METHOD = "method";
	public static final String LINE = "line";
	
	public static void debug(Object obj, String...showtype){
		StackTraceElement stacks[] = Thread.currentThread().getStackTrace();
		StackTraceElement index = stacks[2];
		
		if(showtype.length == 0){
			showtype = new String[2];
			showtype[0] = FILE;
			showtype[1] = LINE;
		}
		
		Map<String,String> shows = new HashMap<String,String>();
		for(String item : showtype){
			if(item.equals(FILE)){
				shows.put(FILE,index.getFileName());
			}else if(item.equals(CLASS)){
				shows.put(CLASS,index.getClassName());
			}else if(item.equals(METHOD)){
				shows.put(METHOD,index.getMethodName());
			}else{
				shows.put(LINE, index.getLineNumber() + "");
			}
		}
		System.out.println("DEBUG: " + shows);
		
		System.out.println(obj);
		System.out.println();
	}
}
