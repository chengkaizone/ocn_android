package com.hongguaninfo.ocnandroid.main;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hongguaninfo.ocnandroid.beans.Notice;
import com.hongguaninfo.ocnandroid.utils.Htmls;

/**
 * 公告详情页
 * 
 * @author chengkai
 * 
 */
public class NoticeDetailPage extends CommonPage {
	private TextView titlebar;
	private ProgressBar pb;
	private WebView web;
	private Notice notice;
	public void onCreate(Bundle save) {
		super.onCreate(save);
		setContentView(R.layout.notice_web_detail);
		notice = (Notice) this.getIntent().getSerializableExtra("Notice");
		init();
	}

	private void init() {
		titlebar = (TextView) findViewById(R.id.base_top_title);
		pb = (ProgressBar) findViewById(R.id.base_top_progress);
		titlebar.setText("公告详细");
		web=(WebView)findViewById(R.id.notice_web_load_web);
		// 设置滚动条不可见
		web.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		web.getSettings().setDefaultTextEncodingName("utf-8");
		String html=Htmls.getHtmlOverString(notice.getTitle(),notice.getPublishDate(), 
				notice.getContent(),true);
		web.loadData(html, "text/html", "utf-8");
	}
}
