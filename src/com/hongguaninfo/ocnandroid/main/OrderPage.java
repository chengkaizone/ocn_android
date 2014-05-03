package com.hongguaninfo.ocnandroid.main;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hongguaninfo.ocnandroid.adapters.OrderAdapter;
import com.hongguaninfo.ocnandroid.assist.OnRefreshListener;
import com.hongguaninfo.ocnandroid.assist.PullRefreshListView;
import com.hongguaninfo.ocnandroid.beans.AuthUser;
import com.hongguaninfo.ocnandroid.beans.ClientInfo;
import com.hongguaninfo.ocnandroid.service.EastService;
import com.hongguaninfo.ocnandroid.service.LinkInfo;
import com.hongguaninfo.ocnandroid.service.ServiceFactory;
import com.hongguaninfo.ocnandroid.utils.JsonUtil;
import com.hongguaninfo.ocnandroid.utils.OSUtil;
import com.hongguaninfo.ocnandroid.utils.OcnUtil;

/**
 * 预约列表
 * 
 * @author chengkai
 * 
 */
public class OrderPage extends CommonPage {
	private TextView number, name, addr, num;
	private PullRefreshListView list;
	AuthUser user;
	private boolean isSkip=false;
	private TextView title;
	private ProgressBar pb;
	private String url = OcnUtil.getOrder();
	private EastService service;
	private ImageView goRepair;
	List<ClientInfo> infos = new ArrayList<ClientInfo>();
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			pb.setVisibility(View.GONE);
			if (msg.what == 0x123) {
				if(OSUtil.getTest(OrderPage.this)){
					infos = JsonUtil.getTestClientInfo();
					title.setText("预约列表(" + infos.size() + ")");
					OrderAdapter adapter = new OrderAdapter(OrderPage.this, infos,
							user);
					list.onRefreshComplete();
					if(infos.size()==0){
						list.setVisibility(View.GONE);
					}
					list.setAdapter(adapter);
					num.setText(infos.size() + "");
				}else{
					showHint("网络错误！");
				}
			} else {
				String json = (String) msg.obj;
				System.out.println(json);
				infos = JsonUtil.getClientInfo(json);
				title.setText("预约列表(" + infos.size() + ")");
				OrderAdapter adapter = new OrderAdapter(OrderPage.this, infos,
						user);
				list.onRefreshComplete();
				if(infos.size()==0){
					list.setVisibility(View.GONE);
				}
				list.setAdapter(adapter);
				num.setText(infos.size() + "");
			}
		}
	};

	public void onCreate(Bundle save) {
		super.onCreate(save);
		setContentView(R.layout.order_page);
		user = (AuthUser) this.getIntent().getSerializableExtra("AuthUser");
		if(user==null){
			user=queryUserInfo();
		}
		init();
	}

	@Override
	public void onResume() {
		requestInfo();
		super.onResume();
	}

	private void init() {
		
		title = (TextView) findViewById(R.id.base_top_title);
		pb = (ProgressBar) findViewById(R.id.base_top_progress);
		goRepair=(ImageView)findViewById(R.id.order_go_repair);
		title.setText("预约列表");
		number = (TextView) findViewById(R.id.order_number);
		addr = (TextView) findViewById(R.id.order_addr);
		name = (TextView) findViewById(R.id.order_name);
		num = (TextView) findViewById(R.id.order_num);
		list = (PullRefreshListView) findViewById(R.id.order_list);
		list.initHeader(R.layout.refreshing_view, R.id.indicator_arraw,
				R.id.indicator_text, R.id.refreshing_pb, R.id.refresh_time);
		list.setOnRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				requestInfo();
			}
		});
		name.setText(user.getLoginName());
		addr.setText(user.getStationName());
		num.setText("6");
		goRepair.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent in=new Intent(OrderPage.this,RepairPage.class);
				in.putExtra("AuthUser", user);
				startActivity(in);
				finish();
			}
		});
		service = ServiceFactory.getService(this);
		//requestInfo();
	}

	private void requestInfo() {
		pb.setVisibility(View.VISIBLE);
		new Thread() {
			public void run() {
				if(OSUtil.getTest(OrderPage.this)){
					slp();
					handler.sendEmptyMessage(0x123);
				}else{
					String str = service.reservationList(user.getUserId() + "",
							user.getBranchCompany());
					if (str != null) {
						Message msg = new Message();
						msg.obj = str;
						handler.sendMessage(msg);
					} else {
						handler.sendEmptyMessage(0x123);
					}
				}
			}
		}.start();
	}
}
