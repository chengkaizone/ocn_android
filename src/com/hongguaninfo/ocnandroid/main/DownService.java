package com.hongguaninfo.ocnandroid.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.hongguaninfo.ocnandroid.utils.NetUtil;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;

/**
 * 下载服务---未使用
 * 
 * @author chengkai
 * 
 */
public class DownService extends Service {
	// 文件目录
	public static final String RES_LOAD_FOLDER = "mnt/sdcard/";
	int total = 0;
	int currentNum = 0;
	MyBinder binder;

	@Override
	public IBinder onBind(Intent intent) {
		return new MyBinder();
	}

	public class MyBinder extends Binder {
		public int getCurrentNum() {
			return currentNum;
		}

		public int getTotal() {
			return total;
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
//		System.out.println("service---创建");
	}

	@Override
	public void onStart(Intent intent, int startId) {
//		System.out.println(intent == null);
		if (intent != null) {
			total = intent.getIntExtra("total", 0);
		}
		new Thread() {
			public void run() {
				// for (int i = 0; i <= total; i++) {
				// try {
				// Thread.sleep(500);
				// currentNum = i;
				// } catch (InterruptedException e) {
				// e.printStackTrace();
				// }
				// }
				updateApk(DownService.this, "");
			}
		}.start();
		super.onStart(intent, startId);
	}

	public void updateApk(Context context, String link) {
		FileOutputStream fileOutputStream = null;
		HttpClient client = new DefaultHttpClient();
		try {
			HttpGet get = new HttpGet(link);
			HttpResponse response = client.execute(get);
			HttpEntity entity = response.getEntity();
			long len = entity.getContentLength();
			InputStream is = entity.getContent();
			if (is != null) {
				File file = new File(RES_LOAD_FOLDER, "ocn_android.apk");
				fileOutputStream = new FileOutputStream(file);
				// 缓存1M
				byte[] brr = new byte[1024];
				int ch = -1;
				long count = 0;
				while ((ch = is.read(brr)) != -1) {
					fileOutputStream.write(brr, 0, ch);
					count += ch;
					currentNum = (int) (count / len);
				}
			}
			fileOutputStream.flush();
			if (fileOutputStream != null) {
				fileOutputStream.close();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(
				Uri.fromFile(new File(RES_LOAD_FOLDER + "mocnoss.apk")),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}
}
