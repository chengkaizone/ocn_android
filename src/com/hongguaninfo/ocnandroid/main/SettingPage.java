package com.hongguaninfo.ocnandroid.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 设置页面---未使用
 * 
 * @author chengkai
 * 
 */
public class SettingPage extends CommonPage implements OnClickListener {
	private TextView title;
	private ProgressBar pb;
	private Button setPass, update;

	public void onCreate(Bundle save) {
		super.onCreate(save);
		setContentView(R.layout.set_main);
		init();
	}

	private void init() {
		title = (TextView) findViewById(R.id.base_top_title);
		pb = (ProgressBar) findViewById(R.id.base_top_progress);
		title.setText("设置");
		setPass = (Button) findViewById(R.id.set_pass_button);
		update = (Button) findViewById(R.id.set_update_button);
		setPass.setOnClickListener(this);
		update.setOnClickListener(this);
	}

	public void onClick(View v) {
		Intent intent = null;
		int id = v.getId();
		switch (id) {
		case R.id.set_pass_button:
			intent = new Intent(SettingPage.this, UpdatePassword.class);
			break;
		case R.id.set_update_button:
			intent = new Intent(SettingPage.this, UpdateVersion.class);
			break;
		}
		startActivity(intent);
		finish();
	}
}
