package com.hongguaninfo.ocnandroid.main;

import java.util.HashMap;
import java.util.Map;

import com.hongguaninfo.ocnandroid.service.EastService;
import com.hongguaninfo.ocnandroid.service.ServiceFactory;
import com.hongguaninfo.ocnandroid.utils.JsonUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.DatePicker.OnDateChangedListener;

/**
 * 填写改派和退单页
 * 
 * @author chengkai
 * 
 */
public class MaintainBackPage extends CommonPage {
	private TextView title;
	private ProgressBar pb;
	private Button btn;
	private EastService service;
	private Button submitReservation;
	private EditText inputRemark;
	// 最终时间
	private String mtainResult = "";
	private String orderId = "";
	private String userId = "";
	private String type = "";
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			pb.setVisibility(View.GONE);
			if (msg.what == 0x999) {
				showHint("提交失败!");
			} else if (msg.arg1 == 0x123) {
				String str = (String) msg.obj;
				if ("true".equals(str)) {
					showHint("提交成功!");
					finish();
				} else {
					showHint("网络异常！");
				}
			}
		}
	};

	public void onCreate(Bundle save) {
		super.onCreate(save);
		setContentView(R.layout.other_p2);
		Intent intent = getIntent();
		type = intent.getStringExtra("type");
		orderId = intent.getStringExtra("orderId");
		mtainResult = intent.getStringExtra("mtainResult");
		userId = this.queryUserInfo().getUserId();
//		System.out.println(type + "\t" + orderId + "\t" + mtainResult + "\t"
//				+ userId);
		init();
	}

	private void init() {
		title = (TextView) findViewById(R.id.base_top_title);
		pb = (ProgressBar) findViewById(R.id.base_top_progress);
		btn = (Button) findViewById(R.id.ensure_button);
		inputRemark = (EditText) findViewById(R.id.other2_input);
		if ("2".equals(type)) {
			title.setText("维修改派");
		} else {
			title.setText("维修退单");
		}

		service = ServiceFactory.getService(this);
		submitReservation = (Button) findViewById(R.id.other2_toggle_button);
		// 设置保存的监听
		submitReservation.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String remark = inputRemark.getText().toString().trim();
				if(remark.equals("")){
					showHint("操作备注不能为空!");
					return;
				}
				pb.setVisibility(View.VISIBLE);
				Map<String, String> map = new HashMap<String, String>();
				map.put("userId", userId);
				map.put("orderId", orderId);
				map.put("remark", remark);
				map.put("mtainResult", mtainResult);
				map.put("type", type);
				request(map);
			}
		});
	}

	private void request(final Map<String, String> map) {
		new Thread() {
			public void run() {
				String return_flag = service.maintainBack(map);
				if (return_flag != null) {
					Message msg = new Message();
					msg.arg1 = 0x123;
					msg.obj = return_flag;
					handler.sendMessage(msg);
				} else {
					handler.sendEmptyMessage(0x999);
				}
			};
		}.start();
	}
}
