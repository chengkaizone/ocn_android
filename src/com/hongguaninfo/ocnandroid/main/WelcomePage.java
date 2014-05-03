package com.hongguaninfo.ocnandroid.main;

import com.hongguaninfo.ocnandroid.utils.OSService;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.widget.Toast;

/**
 * 欢迎页面
 * 
 * @author chengkai
 * 
 */
public class WelcomePage extends Activity {
	// 消息处理器
	Handler handler = new Handler();

	public void onCreate(Bundle save) {
		super.onCreate(save);
		setContentView(R.layout.welcome_main);
		//检查GPS定位功能是否正常！
		openGPSSettings();
		NetworkInfo net=OSService.getConnectivityManager(this).getActiveNetworkInfo();
		if(net==null){
			Toast.makeText(this,"网络连接未打开!",2000).show();
		}
		// 延迟两秒执行run1线程
		handler.postAtTime(run1, 200);
	}

	Runnable run1 = new Runnable() {
		public void run() {
			// 设置延迟两秒执行线程run2
			handler.postDelayed(run2, 200);
			// 设置图片可见
		}
	};
	Runnable run2 = new Runnable() {
		public void run() {
			// 跳转到详情页
			startActivity(new Intent(WelcomePage.this, LoginPage.class));
			// 销毁当前页面
			finish();
		}
	};

	// 开机确认手机是否开启GPS
	private void openGPSSettings() {
		LocationManager alm = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		if (alm.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER)
				&& alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
			Toast.makeText(this, "GPS模块正常", Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(this, "正在开启GPS...", Toast.LENGTH_SHORT).show();
			Intent GPSIntent = new Intent();
			GPSIntent.setClassName("com.android.settings",
		        "com.android.settings.widget.SettingsAppWidgetProvider");
			GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
			GPSIntent.setData(Uri.parse("custom:3"));
			try {
				PendingIntent.getBroadcast(this, 0, GPSIntent, 0).send();
			} catch (CanceledException e) {
				e.printStackTrace();
			}
		}
	}

}