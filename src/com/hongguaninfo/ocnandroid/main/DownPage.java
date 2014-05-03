package com.hongguaninfo.ocnandroid.main;

import java.io.File;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 通知下载页面---未使用
 * 
 * @author chengkai
 * 
 */
public class DownPage extends Activity {

	private TextView declare, precent;
	private ProgressBar pb;
	private DownService.MyBinder binder;
	private NotificationManager ncm;
	private int cent = 0;
	Handler h = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 0x123) {
				precent.setText(cent + "%");
				pb.setProgress(cent);
			}
		}
	};
	private ServiceConnection conn = new ServiceConnection() {
		public void onServiceConnected(ComponentName name, IBinder service) {
			binder = (DownService.MyBinder) service;
		}

		public void onServiceDisconnected(ComponentName name) {
		}
	};

	public void onCreate(Bundle save) {
		super.onCreate(save);
		setContentView(R.layout.download_page);
		declare = (TextView) findViewById(R.id.download_title);
		pb = (ProgressBar) findViewById(R.id.download_pb);
		precent = (TextView) findViewById(R.id.download_percent);
		ncm = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);

		Intent intent = new Intent(DownPage.this, DownService.class);
		// 绑定服务
		this.bindService(intent, conn, Context.BIND_AUTO_CREATE);
		new Thread() {
			public void run() {
				while (true) {
					if (binder != null) {
						cent = binder.getCurrentNum();
						h.sendEmptyMessage(0x123);
						if (cent == 100) {
							ncm.cancel(100);
							update();
							return;
						}
					}
				}
			}
		}.start();
	}

	public void update() {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File("/mnt/sdcard/"
				+ "ocn_android.apk")),
				"application/vnd.android.package-archive");
		startActivity(intent);
	}

	protected void onDestroy() {
		// 解除绑定
		this.unbindService(conn);
		super.onDestroy();
	}
}
