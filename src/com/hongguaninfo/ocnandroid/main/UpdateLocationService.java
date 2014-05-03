package com.hongguaninfo.ocnandroid.main;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * 位置服务未使用
 * 
 * @author chengkai
 * 
 */
public class UpdateLocationService extends Service {

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

}
