package com.hongguaninfo.ocnandroid.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.hongguaninfo.ocnandroid.beans.AuthUser;
import com.hongguaninfo.ocnandroid.beans.ClientInfo;
import com.hongguaninfo.ocnandroid.beans.CustomAccount;
import com.hongguaninfo.ocnandroid.service.EastService;
import com.hongguaninfo.ocnandroid.service.ServiceFactory;
import com.hongguaninfo.ocnandroid.utils.JsonUtil;
import com.hongguaninfo.ocnandroid.utils.OSUtil;

/**
 * 
 * @author chengkai
 * 
 */
public class OrderPrePage extends CommonPage implements OnClickListener {
	private ProgressBar pb;
	private TextView title;

	private TextView repair_no, repair_name, repair_phone;
	private Spinner result;
	// 改约--改派--回单--退单；
	private Button btn1, btn2, btn3, btn4;
	private ClientInfo info;
	private AuthUser user;
	private String[] opera = { "RE_SU", "RE_FA", "RE_YF" };
	private String mtainResult = opera[0];
	private EastService service;
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			pb.setVisibility(View.GONE);
			if (msg.what == 0x999) {
				showHint("获取客户信息失败");
			} else if (msg.arg1 == 0x123) {
				String str = (String) msg.obj;
				CustomAccount custom = JsonUtil.getTestCustom(str);
				if (custom != null) {
					repair_name.setText(custom.getCustomName());
					repair_phone.setText(custom.getCustomPhone());
				}
			}
		}
	};

	public void onCreate(Bundle save) {
		super.onCreate(save);
		setContentView(R.layout.repair_first);
		info = (ClientInfo) (this.getIntent()
				.getSerializableExtra("ClientInfo"));
		user = this.queryUserInfo();
		service = ServiceFactory.getService(this);
		init();
		requestCustomInfo();
	}

	private void init() {
		pb = (ProgressBar) findViewById(R.id.base_top_progress);
		title = (TextView) findViewById(R.id.base_top_title);
		title.setText("维修");
		repair_no = (TextView) findViewById(R.id.order_back_no);
		repair_name = (TextView) findViewById(R.id.order_back_name);
		repair_phone = (TextView) findViewById(R.id.order_back_phone);
		repair_no.setText(info.getOrderNo());
		result = (Spinner) findViewById(R.id.order_back_result);
		btn1 = (Button) findViewById(R.id.order_back_gy);
		btn2 = (Button) findViewById(R.id.order_back_gp);
		btn3 = (Button) findViewById(R.id.order_back_hd);
		btn4 = (Button) findViewById(R.id.order_back_td);
		btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);
		btn3.setOnClickListener(this);
		btn4.setOnClickListener(this);
		result.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				mtainResult = opera[position];
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
	}

	public void onClick(View v) {
		int id = v.getId();
		Intent intent = new Intent();
		intent.putExtra("orderId", info.getOrderId());
		intent.putExtra("orderNo", info.getOrderNo());
		intent.putExtra("custId", info.getCustId());
		intent.putExtra("mtainResult", mtainResult);
		switch (id) {
		case R.id.order_back_gy:
			// 跳转改约
			intent.setClass(OrderPrePage.this, EnsurePage.class);
			break;
		case R.id.order_back_gp:
			// 跳转改派
			intent.putExtra("type", "2");
			intent.setClass(OrderPrePage.this, MaintainBackPage.class);
			break;
		case R.id.order_back_hd:
			//回单页面
			showHint("请稍后...");
			intent.setClass(OrderPrePage.this, OrderBackPage.class);
			break;
		case R.id.order_back_td:
			// 跳转退单
			intent.putExtra("type", "3");
			intent.setClass(OrderPrePage.this, MaintainBackPage.class);
			break;
		}
		startActivity(intent);
		finish();
	}

	private void requestCustomInfo() {
		pb.setVisibility(View.VISIBLE);
		new Thread() {
			public void run() {
				if(OSUtil.getTest(OrderPrePage.this)){
					slp();
					Message msg=new Message();
					msg.arg1 = 0x123;
					msg.obj="test";
					handler.sendMessage(msg);
				}else{
					String str = service.custAccount(info.getCustId());
					if (str != null) {
						Message msg = new Message();
						msg.arg1 = 0x123;
						msg.obj = str;
						handler.sendMessage(msg);
					} else {
						handler.sendEmptyMessage(0x999);
					}
				}
			}
		}.start();
	}
}
