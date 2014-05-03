package com.hongguaninfo.ocnandroid.main;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.hongguaninfo.ocnandroid.beans.AuthUser;
import com.hongguaninfo.ocnandroid.service.EastService;
import com.hongguaninfo.ocnandroid.service.ServiceFactory;
import com.hongguaninfo.ocnandroid.utils.DateUtil;

/**
 * 改约页面
 * 
 * @author chengkai
 * 
 */
public class EnsurePage extends CommonPage {
	private TextView title;
	private ProgressBar pb;
	private Button btn;
	private Spinner sp_time;
	private EastService service;
	private DatePicker date;
	private Button submitReservation;
	private int year, month, day;
	private String[] times = { "上午", "下午", "晚上" };
	private String[] timeType = { "A", "P", "N" };
	private String curTimeType = timeType[0];
	private ArrayAdapter<String[]> spAdapter;
	private EditText inputRemark;
	private long curTime, resultTime;
	// 最终时间
	private String resultDate = "";
	// 以下是传递过来的值
	private String mtainResult = "";
	private String orderId = "";
	private String userId = "";

	private AuthUser user;
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
					System.out.println("--->" + str);
					showHint("提交失败！");
				}
			}
		}
	};

	public void onCreate(Bundle save) {
		super.onCreate(save);
		setContentView(R.layout.other_p1);
		user = this.queryUserInfo();
		Intent intent = this.getIntent();
		orderId = intent.getStringExtra("orderId");
		mtainResult = intent.getStringExtra("mtainResult");
		userId = user.getUserId();
		System.out.println(orderId + "\t" + mtainResult + "\t" + userId);
		init();
		initMenu(R.layout.other_p1);
	}

	private void init() {
		title = (TextView) findViewById(R.id.base_top_title);
		pb = (ProgressBar) findViewById(R.id.base_top_progress);
		btn = (Button) findViewById(R.id.ensure_button);
		inputRemark = (EditText) findViewById(R.id.other1_input);
		title.setText("维修改约");
		service = ServiceFactory.getService(this);

		Intent intent = getIntent();
		orderId = intent.getStringExtra("orderId");
		mtainResult = intent.getStringExtra("mtainResult");
		//userId = intent.getStringExtra("userId");

		date = (DatePicker) findViewById(R.id.other1_date);
		initDate();
		sp_time = (Spinner) findViewById(R.id.other1_sptime);
		spAdapter = new ArrayAdapter(this,
				android.R.layout.simple_spinner_item, times);
		spAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_time.setAdapter(spAdapter);
		sp_time.setSelection(0, true);
		sp_time.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				curTimeType = timeType[position];
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		// 初始化时间对话框
		date.init(year, month, day, new OnDateChangedListener() {
			public void onDateChanged(DatePicker view, int year,
					int monthOfYear, int dayOfMonth) {
				EnsurePage.this.year = year;
				EnsurePage.this.month = monthOfYear+1;
				EnsurePage.this.day = dayOfMonth;
				System.out.println(year + "-" + monthOfYear + "-" + dayOfMonth);
				resultDate = EnsurePage.this.year + "-" + EnsurePage.this.month
						+ "-" + EnsurePage.this.day;
				resultTime = DateUtil.getDate(resultDate);
				System.out.println(curTime - resultTime);
			}
		});
		submitReservation = (Button) findViewById(R.id.other1_toggle_button);

		// 设置保存的监听
		submitReservation.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (curTime > resultTime) {
					showHint("请选择正确的时间！");
					return;
				}
				String remark = inputRemark.getText().toString().trim();
				String reservPeriod = curTimeType;
				Map<String, String> map = new HashMap<String, String>();
				map.put("userId", userId);
				map.put("orderId", orderId);
				map.put("remark", remark);
				map.put("mtainResult", mtainResult);
				map.put("resultDate", resultDate);
				map.put("reservPeriod", reservPeriod);
				request(map);
			}
		});
	}

	private void request(final Map<String, String> map) {
		pb.setVisibility(View.VISIBLE);
		new Thread() {
			public void run() {
				String return_flag = service.maintainReservation(map);
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

	// 为时间对话框初始化时间
	private void initDate() {
		Calendar cal = Calendar.getInstance();
		year = cal.get(Calendar.YEAR);
		month = cal.get(Calendar.MONTH);
		day = cal.get(Calendar.DAY_OF_MONTH);
		resultDate = year + "-" + (month+1) + "-" + day;
		System.out.println("初始化时间--->"+resultDate);
		curTime = DateUtil.getDate(resultDate);
		resultTime = DateUtil.getDate(resultDate);
		cal=null;
	}
}
