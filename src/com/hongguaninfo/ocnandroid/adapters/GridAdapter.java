package com.hongguaninfo.ocnandroid.adapters;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.hongguaninfo.ocnandroid.main.R;

/**
 * 主界面适配器
 * 
 * @author Administrator
 * 
 */
public class GridAdapter extends BaseAdapter {
	private List<Map<String, Object>> data;
	private LayoutInflater inflater;
	private Bitmap online, offline, onicon, officon;
	private boolean flag = false;// 未签到状态

	public GridAdapter(Context context, List<Map<String, Object>> data) {
		this.data = data;
		inflater = LayoutInflater.from(context);
		online = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.status_online);
		offline = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.status_offline);
		onicon = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.sign);
		officon = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.icon4);
	}

	public int getCount() {
		return data.size();
	}

	public Map<String, Object> getItem(int position) {
		return data.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	/**
	 * 这只签退状态
	 * 
	 * @param type
	 */
	public void setOffline(int type) {
		Map<String, Object> map = data.get(3);
		map.put("img", onicon);
		if (type == 0) {
			map.put("desc", "签到");
		} else if (type == 1) {
			map.put("desc", "签到");
		}
		flag = false;// 未签到
		this.notifyDataSetChanged();
	}

	/**
	 * 设置在线
	 */
	public void setOnline() {
		Map<String, Object> map = data.get(3);
		map.put("desc", "签退");
		map.put("img", officon);
		flag = true;// 已签到
		this.notifyDataSetChanged();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder();
			convertView = inflater.inflate(R.layout.grid_item, null);
			holder.img = (ImageView) convertView
					.findViewById(R.id.grid_item_img);
			holder.text = (TextView) convertView
					.findViewById(R.id.grid_item_desc);
			holder.state = (ImageView) convertView
					.findViewById(R.id.grid_item_state);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		Map<String, Object> map = data.get(position);
		if (position != 3) {
			holder.img.setImageBitmap((Bitmap) map.get("img"));
			holder.text.setText((String) map.get("desc"));
			holder.state.setVisibility(View.INVISIBLE);
		} else {
			holder.state.setVisibility(View.VISIBLE);
			if (flag) {
				holder.state.setImageBitmap(online);
			} else {
				holder.state.setImageBitmap(offline);
			}
			holder.img.setImageBitmap((Bitmap) map.get("img"));
			holder.text.setText((String) map.get("desc"));
		}
		return convertView;
	}

	private class Holder {
		ImageView img;
		ImageView state;
		TextView text;
	}
}
