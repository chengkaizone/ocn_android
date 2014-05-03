package com.hongguaninfo.ocnandroid.main;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 更新版本
 * 
 * @author chengkai
 * 
 */
public class UpdateVersion extends Activity {
	private TextView title;
	private ProgressBar pb;
	NotificationManager ncm;
	public static final String RES_LOAD_FOLDER = "/mnt/sdcard/";

	private void init() {
		title = (TextView) findViewById(R.id.base_top_title);
		pb = (ProgressBar) findViewById(R.id.base_top_progress);
		title.setText("更新版本");
		ncm = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);
	}

	public void onCreate(Bundle save) {
		super.onCreate(save);
		setContentView(R.layout.updateapk);
		init();
	}
}