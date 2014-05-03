package com.hongguaninfo.ocnandroid.main;

import java.util.List;

import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hongguaninfo.ocnandroid.adapters.RepairAdapter;
import com.hongguaninfo.ocnandroid.assist.OnRefreshListener;
import com.hongguaninfo.ocnandroid.assist.PullRefreshListView;
import com.hongguaninfo.ocnandroid.beans.AuthUser;
import com.hongguaninfo.ocnandroid.beans.ClientInfo;
import com.hongguaninfo.ocnandroid.service.EastService;
import com.hongguaninfo.ocnandroid.service.ServiceFactory;
import com.hongguaninfo.ocnandroid.utils.JsonUtil;
import com.hongguaninfo.ocnandroid.utils.OSService;
import com.hongguaninfo.ocnandroid.utils.OSUtil;

/**
 * 维修页面
 * 
 * @author chengkai
 * 
 */
public class RepairPage extends CommonPage {
	private TextView title;
	private ProgressBar pb;
	private PullRefreshListView list;
	private EastService service;
	private AuthUser user;
	private ImageView goOrder;
	private List<ClientInfo> infos;
	private RepairAdapter adapter;
    private LocationManager locationManager;
    	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			pb.setVisibility(View.GONE);
			
			if (msg.what == 0x123) {
				showHint("网络错误！");
			}
			if (msg.what == 0x111) {
				pb.setVisibility(View.VISIBLE);
			} else if (msg.what == 0x333) {
				showHint("登记失败!");
				adapter.notifyDataSetChanged();
			} else if (msg.what == 0x666) {
				showHint("登记成功");
				adapter.notifyDataSetChanged();
			} else if (msg.what == 0x999) {
				showHint("网络错误!");
			}else if(msg.what==0x234){
				if(OSUtil.getTest(RepairPage.this)){
					infos = JsonUtil.getTestMaintainList();
					title.setText("维修列表(" + infos.size() + ")");
					adapter = new RepairAdapter(RepairPage.this, infos, user, this,locationManager);
					if (list.getAdapter() != null) {
//						System.out.println(true);
					} else {
//						System.out.println(false);
					}
					if(infos.size()==0){
						list.setVisibility(View.GONE);
					}
					list.setAdapter(adapter);
				}else{
					showHint("网络错误！");
				}
				list.onRefreshComplete();
			}else {
				String json = (String) msg.obj;
				infos = JsonUtil.getClientMaintainList(json);
				title.setText("维修列表(" + infos.size() + ")");

				adapter = new RepairAdapter(RepairPage.this, infos, user, this,locationManager);
				if (list.getAdapter() != null) {
//					System.out.println(true);
				} else {
//					System.out.println(false);
				}
				list.onRefreshComplete();//刷新完成
				if(infos.size()==0){
					list.setVisibility(View.GONE);
				}
				list.setAdapter(adapter);
			}
		}
	};

	public void onCreate(Bundle save) {
		super.onCreate(save);
		setContentView(R.layout.repair_page);
		user = (AuthUser) this.getIntent().getSerializableExtra("AuthUser");
		service = ServiceFactory.getService(this);
        locationManager= OSService.getLocationManager(this);
		init();
	}

	private void init() {
		title = (TextView) findViewById(R.id.base_top_title);
		pb = (ProgressBar) findViewById(R.id.base_top_progress);
		goOrder=(ImageView)findViewById(R.id.repair_go_order);
		title.setText("维修列表");
		list = (PullRefreshListView) findViewById(R.id.repair_list);
		list.initHeader(R.layout.refreshing_view, R.id.indicator_arraw,
				R.id.indicator_text, R.id.refreshing_pb, R.id.refresh_time);
		list.setOnRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				requestInfo();
			}
		});
		goOrder.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent in=new Intent(RepairPage.this,OrderPage.class);
				in.putExtra("AuthUser", user);
				startActivity(in);
				finish();
			}
		});
		//requestInfo();
	}

	@Override
	public void onResume() {
		pb.setVisibility(View.VISIBLE);
		requestInfo();
		super.onResume();
	}

	private void requestInfo() {
		new Thread() {
			public void run() {
				if(OSUtil.getTest(RepairPage.this)){
					slp();
					handler.sendEmptyMessage(0x234);
				}else{
					long start =System.currentTimeMillis();
					String str = service.maintainList(user.getUserId() + "",
							user.getBranchCompany());
					System.out.println("获取维修列表的时间"+(System.currentTimeMillis()-start));
					if (str != null) {
						Message msg = new Message();
						msg.obj = str;
						handler.sendMessage(msg);
					} else {
						handler.sendEmptyMessage(0x234);
					}
				}
			}
		}.start();
	}
}