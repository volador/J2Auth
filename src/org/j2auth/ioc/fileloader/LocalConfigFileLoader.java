package org.j2auth.ioc.fileloader;


import java.io.File;  
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

/**
 * 加载本地配置文件,默认路径在classpath下<br>
 * <pre>
 * 启动参数:
 * 	config_file_name:启动文件名
 * 	config_file_path:启动参数
 * </pre>
 * @author volador
 *
 */
public class LocalConfigFileLoader extends AbstractFileLoader{
	
	//默认配置文件名
	private static String BEANS_FILE_NAME = "beans.xml";
	//默认配置文件路径 :classpath
	private static String BEANS_FILE_PATH = "";
	
	private static final String BEANS_FILE_NAME_KEY = "beans_file_name";
	private static final String BEANS_FILE_PATH_KEY = "beans_file_path";
	
	public LocalConfigFileLoader(Map<String,String> initParams) {
		String config_file_name = initParams.get(BEANS_FILE_NAME_KEY);
		String config_file_path = initParams.get(BEANS_FILE_PATH_KEY);
		if(config_file_name != null && config_file_name.length() > 0) BEANS_FILE_NAME = config_file_name;
		if(config_file_path != null && config_file_path.length() > 0) BEANS_FILE_PATH = config_file_path;
	}
	
	public InputStream loadFile() {
		if(BEANS_FILE_PATH.length() == 0) return loadDefaultPathFile();
		String fullFileName = null;
		if(BEANS_FILE_PATH.lastIndexOf(File.separator) == BEANS_FILE_PATH.length() - 1){
			fullFileName = BEANS_FILE_PATH + BEANS_FILE_NAME;
		}else{
			fullFileName = BEANS_FILE_PATH + File.separator + BEANS_FILE_NAME;
		}
		InputStream stream = null;
		try {
			stream = new FileInputStream(fullFileName);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("fail to load beans file.",e);
		}
		return stream;
	}

	private InputStream loadDefaultPathFile() {
		InputStream stream = this.getClass().getResourceAsStream("/" + BEANS_FILE_NAME);
		if(stream == null){
			URL fullUrl = this.getClass().getResource("/");
			throw new RuntimeException("can not load "+BEANS_FILE_NAME+" in classpath.",
					new FileNotFoundException(fullUrl.toString() + BEANS_FILE_NAME));
		}
		return stream;
	}

}
