package com.hongguaninfo.ocnandroid.main;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.hongguaninfo.ocnandroid.beans.AuthUser;
import com.hongguaninfo.ocnandroid.service.EastService;
import com.hongguaninfo.ocnandroid.service.LinkInfo;
import com.hongguaninfo.ocnandroid.service.ServiceFactory;
import com.hongguaninfo.ocnandroid.utils.OcnUtil;

/**
 * 浏览器---维修业务---已改为本地应用
 * 
 * @author chengkai
 * 
 */
public class MaintainPage extends CommonPage {
	private TextView title;
	private ProgressBar pb;
	private EastService service;
	private WebView webView;
	private ProgressBar bar;
	private boolean isShow = false;
	private String url = OcnUtil.getMaintain();
	// 传值获取
	private String custId = "";
	private String orderId = "";
	private String orderNo = "";
	// 默认值
	private String type = "2";
	// 查询数据库获取
	private String userId = "";
	private String branchCompany = "";

	private AuthUser user;

	public void onCreate(Bundle save) {
		super.onCreate(save);
		setContentView(R.layout.webview);
		Intent intent = getIntent();
		user = this.queryUserInfo();
		userId = user.getUserId();
		branchCompany = user.getBranchCompany();
		custId = intent.getStringExtra("custId");
		orderId = intent.getStringExtra("orderId");
		orderNo = intent.getStringExtra("orderNo");
		type = intent.getStringExtra("type");
		init();
	}

	private void init() {
		title = (TextView) findViewById(R.id.base_top_title);
		pb = (ProgressBar) findViewById(R.id.base_top_progress);
		bar = (ProgressBar) findViewById(R.id.web_tab_bar);
		webView = (WebView) findViewById(R.id.webview);
		title.setText("维修");
		// 设置可以处理javaScript代码
		webView.getSettings().setJavaScriptEnabled(true);
		// 请求可以获得焦点
		webView.requestFocus();
		// 设置滚动条不可见
		webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		// 设置不可以支持缩放
		webView.getSettings().setSupportZoom(false);
		// 设置出现缩放工具必须同时实现才支持缩放
		webView.getSettings().setBuiltInZoomControls(false);
		// 设置事件侦听传递链接参数；否则会调用系统浏览器
		webView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				webView.loadUrl(url);
				return super.shouldOverrideUrlLoading(view, url);
			}
		});
		webView.addJavascriptInterface(new Object() {
			public void startMap(String oder_id) {
				// 调用客户签名
				Tip tipwindow = new Tip(MaintainPage.this);
				tipwindow.filename = oder_id;
				tipwindow.show();
			}
		}, "demo");
		webView.addJavascriptInterface(new Object() {
			public void startMaintain(String messgae) {
				showHint(messgae);
			}
		}, "maintain");
		webView.addJavascriptInterface(new Object() {
			public void backOrderAndroid(String type) {
				showHint("回单成功");
				finish();
			}
		}, "backOrder");
		webView.addJavascriptInterface(new Object() {
			public void toChangeReservation(String mtainResult, String userId,
					String orderId, String type) {
				if ("1".equals(type)) {
					// 改约
					finish();
					Intent intent = new Intent(MaintainPage.this,
							EnsurePage.class);
					intent.putExtra("mtainResult", mtainResult);
					intent.putExtra("userId", userId);
					intent.putExtra("orderId", orderId);
					startActivityForResult(intent, 0);
				} else {
					finish();
					Intent intent = new Intent(MaintainPage.this,
							MaintainBackPage.class);
					intent.putExtra("mtainResult", mtainResult);
					intent.putExtra("userId", userId);
					intent.putExtra("orderId", orderId);
					// 改派type=2 退单type=3
					intent.putExtra("type", type);
					startActivityForResult(intent, 0);
				}

			}
		}, "changeReservation");

		webView.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int newProgress) {
				// 设置加载进度
				// WebPage.this.setProgress(newProgress*100);
				// 点击连接时设置可见
				bar.setVisibility(View.VISIBLE);
				// 进度条的进度是0-10000；所以要*100?
				bar.setProgress(newProgress);
				if (newProgress >= 100) {
					// 设置加载完成时不可见
					bar.setVisibility(View.GONE);
				}
				super.onProgressChanged(view, newProgress);
			}

			@Override
			public void onReceivedTitle(WebView view, String title) {
				// 设置窗口主题
				getWindow().setTitle(title);
				super.onReceivedTitle(view, title);
			}
		});
		webView.loadUrl(url + "?userId=" + userId + "&branchCompany="
				+ branchCompany + "&custAccountId=" + custId + "&orderId="
				+ orderId + "&orderNo=" + orderNo);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (webView.canGoBack()) {
				webView.goBack();
			} else {
				// showDialog();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onResume() {

		// pb.setVisibility(View.VISIBLE);
		webView.loadUrl(url + "?userId=" + userId + "&branchCompany="
				+ branchCompany + "&custAccountId=" + custId + "&orderId="
				+ orderId + "&orderNo=" + orderNo);
		super.onResume();
		// finish();
	}

	private void showDialog() {
		AlertDialog.Builder b = new Builder(this);
		b.setTitle("退出").setMessage("你确定退出浏览器吗？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				}).setNegativeButton("取消", null).create().show();
	}
}
