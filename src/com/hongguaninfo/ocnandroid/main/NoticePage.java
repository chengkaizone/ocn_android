package com.hongguaninfo.ocnandroid.main;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hongguaninfo.ocnandroid.adapters.NoticeAdapter;
import com.hongguaninfo.ocnandroid.assist.OnRefreshListener;
import com.hongguaninfo.ocnandroid.assist.PullRefreshListView;
import com.hongguaninfo.ocnandroid.beans.AuthUser;
import com.hongguaninfo.ocnandroid.beans.Notice;
import com.hongguaninfo.ocnandroid.service.EastService;
import com.hongguaninfo.ocnandroid.service.ServiceFactory;
import com.hongguaninfo.ocnandroid.utils.JsonUtil;
import com.hongguaninfo.ocnandroid.utils.OSUtil;
import com.hongguaninfo.ocnandroid.utils.ViewFactory;

/**
 * 公告列表
 * 
 * @author chengkai
 * 
 */
public class NoticePage extends CommonPage {
	private TextView title;
	private ProgressBar pb;
	private PullRefreshListView list;
	private EastService service;
	private AuthUser user;
	private List<Notice> notices = new ArrayList<Notice>();
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			pb.setVisibility(View.GONE);
			if (msg.what == 0x123) {
				showHint("获取数据错误！");
				list.onRefreshComplete();
			} else {
				String info = (String) msg.obj;
				// 解析返回数据
				notices = JsonUtil.getNoticeList(info);
				NoticeAdapter adapter = new NoticeAdapter(NoticePage.this,
						notices);
				list.onRefreshComplete();
				list.setAdapter(adapter);
			}
		}
	};

	public void onCreate(Bundle save) {
		super.onCreate(save);
		setContentView(R.layout.notice_page);
		user = (AuthUser) this.getIntent().getSerializableExtra("AuthUser");
		init();
		// 开启子线程请求服务器端的公告信息数据
	}

	private void init() {
		title = (TextView) findViewById(R.id.base_top_title);
		pb = (ProgressBar) findViewById(R.id.base_top_progress);
		title.setText("公告");
		pb.setVisibility(View.VISIBLE);
		list = (PullRefreshListView) findViewById(R.id.notice_listview);
		list.initHeader(R.layout.refreshing_view, R.id.indicator_arraw,
				R.id.indicator_text, R.id.refreshing_pb, R.id.refresh_time);
		service = ServiceFactory.getService(this);
		list.setLayoutAnimation(ViewFactory.getScaleAlpha(this));
		list.setOnRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				requestInfo();
			}
		});
		requestInfo();
	}
	private void requestInfo(){
		new Thread() {
			public void run() {

				String info=null;
				if(OSUtil.getTest(NoticePage.this)){
					slp();
					info=initNoticeInfo();
				}else{
					System.out.println("无数据！");
					info = service.getNoticeList(user.getBranchCompany(),
							user.getStation());
				}
				if (info != null) {
					Message msg = new Message();
					msg.obj = info;
					handler.sendMessage(msg);
				} else {
					handler.sendEmptyMessage(0x123);
				}
			}
		}.start();
	}
	private String initNoticeInfo(){
		AssetManager asset = this.getAssets();
		try {
			InputStream input = asset.open("notice.txt");
			InputStreamReader isr = new InputStreamReader(input, "utf-8");
			BufferedReader reader = new BufferedReader(isr);
			StringBuilder json = new StringBuilder();
			String temp = "";
			while ((temp = reader.readLine()) != null) {
				json.append(temp);
			}
			return json.toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	private void downFile(String str){
		FileOutputStream output=null;
		try {
			output=new FileOutputStream("/mnt/sdcard/notice.txt");
			PrintStream p=new PrintStream(output);
			p.print(str);
			p.flush();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
}
