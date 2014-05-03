package com.hongguaninfo.ocnandroid.main;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.EmbossMaskFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hongguaninfo.ocnandroid.service.LinkInfo;
import com.hongguaninfo.ocnandroid.utils.OcnUtil;
import com.hongguaninfo.ocnandroid.widgets.Builders;
import com.hongguaninfo.ocnandroid.widgets.ColorPickerDialog;
import com.hongguaninfo.ocnandroid.widgets.SignView;

/**
 * 签名页面--
 * 
 * @author chengkai
 * 
 */
public class SignPage extends Activity implements
		ColorPickerDialog.OnColorChangedListener {
	private TextView title;
	private ProgressBar pb;
	private SignView sign;
	private BlurMaskFilter blur;
	private EmbossMaskFilter emboss;
	private String filename;
	private String backFileName = "";
	private String preSignPath = "";
	private static String requestURL1 = OcnUtil.getUpload();

	public void onCreate(Bundle save) {
		super.onCreate(save);
		setContentView(R.layout.sign);
		filename = this.getIntent().getStringExtra("orderId");
		preSignPath = this.getIntent().getStringExtra("signPath");
//		System.out.println(preSignPath + "---->" + filename + "<---SignPage");
		init();
	}

	private void init() {
		sign = (SignView) findViewById(R.id.sign_signview);
		blur = new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL);
		emboss = new EmbossMaskFilter(new float[] { 1.5f, 1.5f, 1.5f }, 0.6f,
				6f, 4.2f);
		title = (TextView) findViewById(R.id.base_top_title);
		pb = (ProgressBar) findViewById(R.id.base_top_progress);
		title.setText("个性签名");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// 获取菜单解析器
		MenuInflater inflater = new MenuInflater(this);
		// 将菜单资源解析到菜单项中
		inflater.inflate(R.menu.picture_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case R.id.width_1:
			sign.getCachePaint().setStrokeWidth(1);
			break;
		case R.id.width_3:
			sign.getCachePaint().setStrokeWidth(3);
			break;
		case R.id.width_5:
			sign.getCachePaint().setStrokeWidth(5);
			break;
		case R.id.blur:
			sign.getCachePaint().setMaskFilter(blur);
			break;
		case R.id.emboss:
			sign.getCachePaint().setMaskFilter(emboss);
			break;
		case R.id.select_color:
			new ColorPickerDialog(this, this, sign.getCachePaint().getColor())
					.show();
			break;
		case R.id.save:
			uploadSign();
			break;
		case R.id.repicture:
			// 通知重新绘图
			sign.initParam();
			break;
		}
		return true;
	}

	private void uploadSign() {
		pb.setVisibility(View.VISIBLE);
		String ALBUM_PATH = "/data/data/com.hongguaninfo.ocnandroid.main/";
		try {
			Intent intent = new Intent();
			String path = ALBUM_PATH + filename + ".png";
			if (!preSignPath.equals("")) {
				if (!preSignPath.equals(path)) {
					File file = new File(preSignPath);
					if (file.exists()) {
						file.delete();
					}
				}
			}
			Bitmap bitmap = sign.getBitmap();
			Bundle bundle = new Bundle();
			bundle.putString("filename", filename);
			bundle.putString("path", path);
			bundle.putByteArray("bitmap", changeBitmapToByteArray(bitmap));
			intent.putExtras(bundle);
			FileOutputStream out = new FileOutputStream(path);
			if (bitmap != null) {
				bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
			}
			showHint("保存成功!");
			setResult(0, intent);
			finish();
		} catch (Exception e) {
			showHint("保存失败！");
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){
			hintSave();
		}
		return super.onKeyDown(keyCode, event);
	}

	private void hintSave(){
		new Builders(SignPage.this)
		.setContent("是否保存签名？")
		.setView(R.layout.builder_dialog,R.id.custom_content,
				R.id.custom_positive,R.id.custom_negative)
		.setPositiveButton("确定",new View.OnClickListener() {
			public void onClick(View v) {
				uploadSign();
			}
		}).setNegativeButton("取消",new View.OnClickListener() {
			public void onClick(View v) {
				setResult(1,new Intent());
				finish();
			}
		}).show();
	}
	// 保存签名
	private void saveBitmap() {
		String path = "/mnt/sdcard/pictures/";
		String fileName = System.currentTimeMillis() + ".png";
		try {
			File file = new File(path);
			if (!file.exists()) {
				// 创建文件夹
				file.mkdir();
			}
			// 声明文件
			File png = new File(path + fileName);
			// 创建图片文件
			png.createNewFile();
			// 打开文件输入流
			FileOutputStream output = new FileOutputStream(png);
			Bitmap cache = sign.getBitmap();
			// 压缩成二进制到指定输出流并指定格式保存图像
			cache.compress(Bitmap.CompressFormat.PNG, 100, output);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private byte[] changeBitmapToByteArray(Bitmap bit) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bit.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	public void colorChanged(int color) {
		sign.getCachePaint().setColor(color);
	}

	private void showHint(String str) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}
}
