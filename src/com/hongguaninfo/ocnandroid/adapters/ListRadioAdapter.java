package com.hongguaninfo.ocnandroid.adapters;

import java.util.List;

import com.hongguaninfo.ocnandroid.main.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * 未使用此类
 * 
 * @author Administrator
 * 
 */
public class ListRadioAdapter extends BaseAdapter {
	private List<String> data;
	private LayoutInflater inflater;
	private boolean[] brr;

	public ListRadioAdapter(Context context, List<String> data) {
		this.data = data;
		inflater = LayoutInflater.from(context);
		brr = new boolean[data.size()];
	}


	public int getCount() {
		return data.size();
	}


	public String getItem(int position) {
		return data.get(position);
	}


	public long getItemId(int position) {
		return position;
	}

	public void selected(int loc) {
		for (int i = 0; i < data.size(); i++) {
			if (i == loc) {
				brr[i] = true;
			} else {
				brr[i] = false;
			}
		}
		this.notifyDataSetChanged();
	}


	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder();
			convertView = inflater.inflate(R.layout.radio_item, null);
			holder.tv = (TextView) convertView.findViewById(R.id.radio_tv);
			holder.rb = (RadioButton) convertView.findViewById(R.id.radio_rb);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.tv.setText(data.get(position));
		holder.rb.setChecked(brr[position]);
		return convertView;
	}

	private class Holder {
		TextView tv;
		RadioButton rb;
	}
}
