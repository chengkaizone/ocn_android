package com.hongguaninfo.ocnandroid.adapters;

import java.util.List;

import com.hongguaninfo.ocnandroid.main.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
/**
 * 下拉菜单多选适配器
 * @author Administrator
 *
 */
public class MultiSpinnerAdapter extends BaseAdapter {
	private List<String> data;
	private LayoutInflater inflater;
	private boolean[] isSelected;

	public MultiSpinnerAdapter(Context context, List<String> data) {
		this.data = data;
		this.inflater = LayoutInflater.from(context);
		isSelected = new boolean[data.size()];
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public String getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void check(int loc) {
		isSelected[loc] = !isSelected[loc];
		this.notifyDataSetChanged();
	}

	public boolean[] getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(boolean[] isSelected) {
		this.isSelected = isSelected;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder();
			convertView = inflater.inflate(R.layout.spinner_adapter, null);
			holder.item = (TextView) convertView
					.findViewById(R.id.adapter_spinner_item);
			holder.cb = (CheckBox) convertView
					.findViewById(R.id.adapter_spinner_cb);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		boolean tmp = isSelected[position];
		if (tmp) {
			holder.cb.setChecked(true);
		} else {
			holder.cb.setChecked(false);
		}
		holder.item.setText(data.get(position));
		return convertView;
	}

	private class Holder {
		TextView item;
		CheckBox cb;
	}
}
