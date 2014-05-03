package com.hongguaninfo.ocnandroid.adapters;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hongguaninfo.ocnandroid.beans.Notice;
import com.hongguaninfo.ocnandroid.main.NoticeDetailPage;
import com.hongguaninfo.ocnandroid.main.R;

/**
 * 公告列表适配器
 * 
 * @author Administrator
 * 
 */
public class NoticeAdapter extends BaseAdapter {
	private Context context;
	private List<Notice> data;
	private LayoutInflater inflater;

	public NoticeAdapter(Context context, List<Notice> data) {
		this.context = context;
		this.data = data;
		inflater = LayoutInflater.from(context);
	}


	public int getCount() {
		return data.size();
	}


	public Notice getItem(int position) {
		return data.get(position);
	}


	public long getItemId(int position) {
		return position;
	}


	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder();
			convertView = inflater.inflate(R.layout.notice_item, null);
			holder.title = (TextView) convertView
					.findViewById(R.id.notice_title);
			holder.date = (TextView) convertView
					.findViewById(R.id.notice_publishdate);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		final Notice notice = data.get(position);
		holder.title.setText(notice.getTitle());
		holder.date.setText(notice.getPublishDate());
//		System.out.println("content--->"+notice.getContent());
		convertView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(context, NoticeDetailPage.class);
				intent.putExtra("Notice", notice);
				context.startActivity(intent);
			}
		});
		return convertView;
	}

	private class Holder {
		TextView title, date;
	}
}
