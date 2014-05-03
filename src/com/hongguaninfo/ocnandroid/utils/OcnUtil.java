package com.hongguaninfo.ocnandroid.utils;

import java.io.InputStream;
import java.util.Properties;
/**
 * 读取配置文件工具类
 * @author chengkai
 *
 */
public class OcnUtil {
	private static Properties env = new Properties();

	static {
		try {
			// 相对路径获得输入流
			InputStream is = OcnUtil.class
					.getResourceAsStream("/ocndb.properties");
			env.load(is);
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String getTestImsi(){
		return env.getProperty("TESTIMSI");
	}
	public static String getNameSpace(){
		return env.getProperty("NAMESPACE");
	}
	public static String getWsdl(){
		return env.getProperty("WSDL");
	}
	public static String getSoft(){
		return env.getProperty("SOFT");
	}
	public static String getOrder(){
		return env.getProperty("ORDER");
	}
	public static String getMaintain(){
		return env.getProperty("MAINTAIN");
	}
	public static String getUpload(){
		return env.getProperty("UPLOAD");
	}
	public static String getUploadApk(){
		return env.getProperty("UPLOAD_APK");
	}
	
	public static String getUploadGps(){
		return env.getProperty("UPLOAD_GPS");
	}
	public static void setProcessCount(int count){
		env.put("PROCESSCOUNT", count);
	}
	public static int getProcessCount(){
		String temp=env.getProperty("PROCESSCOUNT","20");
		return Integer.parseInt(temp);
	}
	public static void setTimeStep(long step){
		env.put("TIMESTEP", step);
	}
	public static long getTimeStep(){
		String temp=env.getProperty("TIMESTEP","60000");
		return Long.parseLong(temp);
	}
}