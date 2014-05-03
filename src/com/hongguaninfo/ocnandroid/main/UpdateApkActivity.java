package com.hongguaninfo.ocnandroid.main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hongguaninfo.ocnandroid.utils.OcnUtil;

/**
 * 更新版本
 * 
 * @author chengkai
 * 
 */
public class UpdateApkActivity extends Activity {
	private ProgressBar pb;
	private TextView tv;
	private LinearLayout lay;
	private String upload_apk = OcnUtil.getUploadApk();
//	private String test_url="http://172.17.93.87:8080/test/ocn_android.apk";
	private String fileName="ocn_android.apk";
	public static final String RES_LOAD_FOLDER = "/sdcard/";
	int progress=0;
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			if(msg.what==0x123){
				if(progress!=100){
					tv.setText("已下载："+progress+"%");
					pb.setProgress(progress);
				}else{
					tv.setText("下载完成!");
					pb.setVisibility(View.GONE);
				}
			}
		}
	};
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.updateapk);
		TextView title = (TextView) findViewById(R.id.base_top_title);
		title.setText("更新版本");
		lay=(LinearLayout)findViewById(R.id.update_lay);
		pb=(ProgressBar)findViewById(R.id.update_pb);
		tv = (TextView) findViewById(R.id.update_tv);
		new AlertDialog.Builder(UpdateApkActivity.this)
		.setTitle("系统更新")
		.setMessage("发现新版本，请更新！")
		// 设置内容
		.setPositiveButton("确定",// 设置确定按钮
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int which) {
						if (Environment.getExternalStorageState().equals(
								Environment.MEDIA_MOUNTED)){
							downFile(RES_LOAD_FOLDER,upload_apk);
						}else{
							Toast.makeText(UpdateApkActivity.this,"sd卡不存在,请插入sd卡",2000).show();
						}
					}
				})
		.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				finish();
			}
		}).show();// 创建并显示
	}

	void downFile(final String dir,final String url) {
		lay.setVisibility(View.VISIBLE);
		new Thread() {
			public void run() {
				HttpClient client = new DefaultHttpClient();
				// params[0]代表连接的url
				HttpGet get = new HttpGet(url);
				HttpResponse response;
				try {
					response = client.execute(get);
					HttpEntity entity = response.getEntity();
					long length = entity.getContentLength();
					InputStream is = entity.getContent();
					FileOutputStream fileOutputStream = null;
					if (is != null) {
						if(dir.equals(RES_LOAD_FOLDER)){
							File file = new File(dir, fileName);
							fileOutputStream = new FileOutputStream(file);
						}else{
							fileOutputStream=openFileOutput(fileName, Context.MODE_APPEND);
						}
						byte[] buf = new byte[1024];
						int ch = -1;
						int count = 0;
//						System.out.println("---"+length);
						while ((ch = is.read(buf)) != -1) {
							fileOutputStream.write(buf, 0, ch);
							count += ch;
//							System.out.println("count--->"+count);
							if (length==count) {
//								System.out.println("if--"+progress);
								progress=100;
								handler.sendEmptyMessage(0x123);
							}else{
//								System.out.println(count+"---"+length);
//								System.out.println("else--"+progress+"--"+(int)(100*count/length));
								progress=(int)(count*100/length);
								handler.sendEmptyMessage(0x123);
							}
						}
//						System.out.println("count:"+count+"---length"+length);
					}
					fileOutputStream.flush();
					if (fileOutputStream != null) {
						fileOutputStream.close();
					}
					down();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	private void down() {
		handler.post(new Runnable() {
			public void run() {
				update();
			}
		});
	}

	private void update() {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(
				Uri.fromFile(new File(RES_LOAD_FOLDER + "ocn_android.apk")),
				"application/vnd.android.package-archive");
		startActivity(intent);
	}
}