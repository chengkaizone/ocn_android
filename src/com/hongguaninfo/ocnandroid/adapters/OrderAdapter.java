package com.hongguaninfo.ocnandroid.adapters;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.hongguaninfo.ocnandroid.beans.AuthUser;
import com.hongguaninfo.ocnandroid.beans.ClientInfo;
import com.hongguaninfo.ocnandroid.main.OrderWritePage;
import com.hongguaninfo.ocnandroid.main.R;

/**
 * 预约列表适配器
 * 
 * @author Administrator
 * 
 */
public class OrderAdapter extends BaseAdapter {
	private Context context;
	private List<ClientInfo> data;
	private LayoutInflater inflater;
	private AuthUser user;

	public OrderAdapter(Context context, List<ClientInfo> data, AuthUser user) {
		this.context = context;
		this.data = data;
		this.user = user;
		inflater = LayoutInflater.from(context);
	}


	public int getCount() {
		return data.size();
	}


	public ClientInfo getItem(int loc) {
		return data.get(loc);
	}


	public long getItemId(int loc) {
		return loc;
	}


	public View getView(int loc, View view, ViewGroup parent) {
		Holder holder = null;
		if (view == null) {
			holder = new Holder();
			view = inflater.inflate(R.layout.order_item, null);
			holder.item_addr = (TextView) view
					.findViewById(R.id.order_item_addr);
			holder.item_no = (TextView) view.findViewById(R.id.order_item_no);
			holder.item_name = (TextView) view
					.findViewById(R.id.order_item_name);
            holder.item_accountNo = (TextView) view
					.findViewById(R.id.order_item_accountNo);
			holder.item_bug = (TextView) view.findViewById(R.id.order_item_bug);
			holder.item_phone = (TextView) view
					.findViewById(R.id.order_item_phone);
			holder.item_ctime = (TextView) view
					.findViewById(R.id.order_item_ctime);
			holder.item_ptime = (TextView) view
					.findViewById(R.id.order_item_ptime);
			holder.item_divicename = (TextView) view
					.findViewById(R.id.order_item_divicename);
            holder.item_crtype = (TextView) view
					.findViewById(R.id.order_item_crtype);
            holder.item_remark = (TextView) view
					.findViewById(R.id.order_item_remark);
			holder.item_write = (Button) view
					.findViewById(R.id.order_item_write);
			view.setTag(holder);
		} else {
			holder = (Holder) view.getTag();
		}
		final ClientInfo info = data.get(loc);
		holder.item_addr.setText("地址:" + info.getAddr());
		holder.item_bug.setText(info.getBug());
		holder.item_name.setText(info.getCustName());
		holder.item_accountNo.setText(info.getAccountNo());
		holder.item_no.setText(info.getOrderNo());
		holder.item_phone.setText(info.getPhone() + "");
		holder.item_ctime.setText(info.getCreatDate());
		holder.item_ptime.setText(info.getReservtionDate());
		holder.item_divicename.setText(info.getDeviceName());
		holder.item_crtype.setText(info.getRepairType());
		holder.item_remark.setText(info.getRemark());
		holder.item_write.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(context, OrderWritePage.class);
				intent.putExtra("ClientInfo", info);
				intent.putExtra("AuthUser", user);
				context.startActivity(intent);
			}
		});
		return view;
	}

	private class Holder {
		TextView item_addr;
		TextView item_no;
		TextView item_name;
		TextView item_accountNo;
		TextView item_bug;
		TextView item_phone;
		TextView item_ctime;
		TextView item_ptime;
		TextView item_divicename;
        TextView item_crtype;
        TextView item_remark;
		Button item_write;
	}
}
