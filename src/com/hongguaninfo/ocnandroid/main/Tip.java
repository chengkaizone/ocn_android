package com.hongguaninfo.ocnandroid.main;

import java.io.File;
import java.io.FileOutputStream;

import com.hongguaninfo.ocnandroid.service.LinkInfo;
import com.hongguaninfo.ocnandroid.utils.OcnUtil;
import com.hongguaninfo.ocnandroid.utils.UploadUtil;
import com.hongguaninfo.ocnandroid.widgets.SignView;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * 上传签名
 * 
 * @author chengkai
 * 
 */
public class Tip {
	private Dialog mDialog;
	public String filename;
	private static String requestURL1 = OcnUtil.getUpload();

	public Tip(final Context context) {
		mDialog = new Dialog(context, R.layout.dialog);
		Window window = mDialog.getWindow();
		WindowManager.LayoutParams wl = window.getAttributes();
		window.setAttributes(wl);
		window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		window.setLayout(ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		LinearLayout abslayout = new LinearLayout(context);
		mDialog.setContentView(abslayout);
		mDialog.setFeatureDrawableAlpha(Window.FEATURE_OPTIONS_PANEL, 0);
		abslayout.setOrientation(1);
		AbsoluteLayout.LayoutParams lp1 =

		new AbsoluteLayout.LayoutParams(

		ViewGroup.LayoutParams.WRAP_CONTENT,

		ViewGroup.LayoutParams.WRAP_CONTENT, 0, 100);
		Button btn1 = new Button(context);
		btn1.setText("保存");
		btn1.setId(1);
		abslayout.addView(btn1, lp1);
		final SignView view7 = new SignView(context);
		abslayout.addView(view7, lp1);
		// yishangshi bujujiemian
		btn1.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				view7.cacheCanvas.save(Canvas.ALL_SAVE_FLAG);
				view7.cacheCanvas.restore();
				String ALBUM_PATH = "/data/data/com.hongguaninfo.ocnandroid.main/";
				try {
					FileOutputStream out = new FileOutputStream(ALBUM_PATH
							+ filename + ".png");
					if (view7.cacheBitmap != null) {
						view7.cacheBitmap.compress(Bitmap.CompressFormat.PNG,
								90, out);
					}
					File file = new File(ALBUM_PATH + filename + ".png");
					if (file != null) {
						UploadUtil.uploadFile(file, requestURL1);
						file.delete();
						Toast.makeText(context, "签名上传成功", Toast.LENGTH_SHORT)
								.show();
					}
					mDialog.dismiss();
				} catch (Exception e) {
					Toast.makeText(context, "签名上传失败", Toast.LENGTH_SHORT)
							.show();
					e.printStackTrace();
				}
			}
		});
	}

	public void show() {
		mDialog.show();
	}

}