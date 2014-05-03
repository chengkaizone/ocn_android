package com.hongguaninfo.ocnandroid.service;

import android.content.Context;
import com.hongguaninfo.ocnandroid.serviceimpl.EastServiceImpl;

/**
 * 服务工厂类
 * 
 * @author Administrator
 * 
 */
public class ServiceFactory {
	/**
	 * 通过工厂类创建webservice的实例
	 * 
	 * @param context
	 * @return
	 */
	public static EastService getService(Context context) {
		return new EastServiceImpl(context);
	}
}
