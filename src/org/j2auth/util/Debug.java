package org.j2auth.util;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
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
	public static final String THREAD = "thread";
	
	//default outputter
	private static ThreadLocal<PrintWriter> out = new ThreadLocal<PrintWriter>(){
		@Override
		protected PrintWriter initialValue() {
			return new PrintWriter(System.out,true);
		}
	};
	
	/**
	 * change thread default outputer
	 * @param outer OutputStream
	 * @return true/false
	 */
	public static boolean setOuter(OutputStream outer){
		if(outer != null){
			out.set(new PrintWriter(outer,true));
			return true;
		}
		return false;
	}
	
	public static void show(Object obj, String...showtype){
		StackTraceElement stacks[] = Thread.currentThread().getStackTrace();
		StackTraceElement index = stacks[2];
		
		if(showtype.length == 0){
			showtype = new String[2];
			showtype[0] = FILE;
			showtype[1] = LINE;
		}
		
		Map<String,String> shows = new LinkedHashMap<String,String>();
		for(String item : showtype){
			if(item.equals(FILE)){
				shows.put(FILE,index.getFileName());
			}else if(item.equals(CLASS)){
				shows.put(CLASS,index.getClassName());
			}else if(item.equals(METHOD)){
				shows.put(METHOD,index.getMethodName());
			}else if(item.equals(LINE)){
				shows.put(LINE, index.getLineNumber() + "");
			}else{
				shows.put(THREAD, Thread.currentThread().getName());
			}
		}
		out.get().println("DEBUG: " + shows);
		
		out.get().println(obj);
		out.get().println();
	}
}
