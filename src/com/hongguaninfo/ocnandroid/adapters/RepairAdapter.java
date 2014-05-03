package com.hongguaninfo.ocnandroid.adapters;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hongguaninfo.ocnandroid.beans.AuthUser;
import com.hongguaninfo.ocnandroid.beans.ClientInfo;
import com.hongguaninfo.ocnandroid.main.OrderPrePage;
import com.hongguaninfo.ocnandroid.main.R;
import com.hongguaninfo.ocnandroid.service.EastService;
import com.hongguaninfo.ocnandroid.service.LinkInfo;
import com.hongguaninfo.ocnandroid.service.ServiceFactory;
import com.hongguaninfo.ocnandroid.utils.DateUtil;
import com.hongguaninfo.ocnandroid.utils.OSUtil;
import com.hongguaninfo.ocnandroid.utils.OcnUtil;
import com.hongguaninfo.ocnandroid.utils.UploadGPSxy;

/**
 * 维修列表适配器
 *
 * @author Administrator
 *
 */
public class RepairAdapter extends BaseAdapter {
    private boolean isFirst = true;
    private boolean isGps = true;
    private Context context;
    private List<ClientInfo> data;
    private LayoutInflater inflater;
    private Handler handler;
    private EastService service;
    private LocationManager lManager;

    // 0代表不可点击--1代表可点击
    public RepairAdapter(Context context, List<ClientInfo> data, AuthUser user,
                         Handler handler, LocationManager lManager) {
        this.context = context;
        this.data = data;
        this.handler = handler;
        this.lManager = lManager;
        service = ServiceFactory.getService(context);
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
    public void setEnabled(boolean flag){

    }

    public View getView(int loc, View view, ViewGroup parent) {
        Holder holder = null;
        if (view == null) {
            holder = new Holder();
            view = inflater.inflate(R.layout.repair_item, null);
            holder.item_addr = (TextView) view
                    .findViewById(R.id.repair_item_addr);
            holder.item_no = (TextView) view.findViewById(R.id.repair_item_no);
            holder.item_name = (TextView) view
                    .findViewById(R.id.repair_item_name);
            holder.item_accountNo = (TextView) view
					.findViewById(R.id.order_item_accountNo);
            holder.item_phone = (TextView) view
                    .findViewById(R.id.repair_item_phone);
            holder.item_ctime = (TextView) view
                    .findViewById(R.id.repair_item_ctime);
            holder.item_reserve_result = (TextView) view.findViewById(R.id.reserve_result);
            holder.item_ptime = (TextView) view
                    .findViewById(R.id.repair_item_ptime);
            holder.item_divicename = (TextView) view
                    .findViewById(R.id.repair_item_divicename);
            holder.item_bug = (TextView) view.findViewById(R.id.repair_item_bug);
            holder.item_rtype = (TextView) view.findViewById(R.id.repair_item_crtype);
            holder.item_remark = (TextView) view.findViewById(R.id.repair_item_remark);

            holder.item_write = (Button) view
                    .findViewById(R.id.repair_item_btn2);
            holder.item_log = (Button) view.findViewById(R.id.repair_item_btn1);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        final ClientInfo info = data.get(loc);
        holder.item_addr.setText("地址:" + info.getAddr());
        holder.item_name.setText(info.getCustName());
        holder.item_no.setText(info.getOrderNo());
        holder.item_phone.setText(info.getPhone());
        holder.item_accountNo.setText(info.getAccountNo());
        holder.item_ctime.setText(info.getCreatDate());
        holder.item_reserve_result.setText(info.getReserveResult());
        holder.item_ptime.setText(info.getReservtionDate() + " "
                + info.getPeriodDate());
        holder.item_divicename.setText(info.getDeviceName());
        holder.item_bug.setText(info.getBug());
        holder.item_rtype.setText(info.getRepairType());
        holder.item_remark.setText(info.getRemark());

        // 填写维修信息事件触发
        holder.item_write.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if ("4".equals(info.getStatus())) {
                    if ("电话支持排障".equals(info.getReserveResult())) {
						OSUtil.putClientStatus(context, true);
					} else {
						OSUtil.putClientStatus(context, false);
					}
                    Intent intent = new Intent(context, OrderPrePage.class);
                    intent.putExtra("ClientInfo", info);
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "请先登记", Toast.LENGTH_SHORT).show();
                }

            }
        });

        String s = info.getStatus();
        if (s.equals("3")) {
            holder.item_log.setText("登记");
            holder.item_log.setBackgroundResource(R.drawable.east_big_button);
            holder.item_log.setEnabled(true);
        } else if (s.endsWith("4")) {
            holder.item_log.setText("已登记");
            holder.item_log.setBackgroundResource(R.drawable.east_btn_normal3);
            holder.item_log.setEnabled(false);
        }

        // 登记事件触发
        holder.item_log.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                //如果不允许登记；给出提示
                if(!isCanCheckIn(info.getReservtionDate())){
                    Toast.makeText(context, "不能在预约上门之前登记!", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 通知进度条显示
                handler.sendEmptyMessage(0x111);
                v.setEnabled(false);
                //登记时记录维修人员位置信息
                updateToNewLocation(lManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
                new Thread() {
                    public void run() {
                        String str = service.checkIn(info.getOrderId(),
                                OSUtil.getUserId(context));
                        if (str == null) {
//							System.out.println("服务器错误！");
                            handler.sendEmptyMessage(0x999);
                        } else if (str.equals("true")) {
//							System.out.println("登记成功");
                            info.setStatus("4");
                            handler.sendEmptyMessage(0x666);
                        } else if (str.equals("false")) {
//							System.out.println("登记失败!");
                            info.setStatus("3");
                            handler.sendEmptyMessage(0x333);
                        }

                    }
                }.start();
            }
        });
        return view;
    }

    /**
     * 判断能否登记，登记日期要在预约日期以后，才能登记
     * @param date
     * @return
     */
    private boolean isCanCheckIn(String date){
        long cliT= DateUtil.getDate(date);
        long curT=System.currentTimeMillis();
        //如果当前登记时间小于客户预约时间；不允许登记
        if(curT<cliT){
            return false;
        }
        return true;
    }

    private void updateToNewLocation(Location loc) {
        if (loc != null) {
            if (isGps) {
                isGps = false;
            }
            double latitude = loc.getLatitude();
            double longitude = loc.getLongitude();
            final String url_path = OcnUtil.getUploadGps() + "?userId="
                    + OSUtil.getUserId(context) + "&latitude=" + longitude
                    + "&longitude=" + latitude;
            new Thread() {
                public void run() {
                    UploadGPSxy.uploadGPSXY(url_path);
                };
            }.start();
        } else {
            if (isFirst) {
                // showHint("无法获取地理信息");
                isFirst = false;
                updateToNewLocation(lManager
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
            }
        }
    }

    private class Holder {
        TextView item_addr;
        TextView item_no;
        TextView item_name;
        TextView item_phone;
        TextView item_accountNo;
        TextView item_ctime;
        TextView item_reserve_result;
        TextView item_ptime;
        TextView item_divicename;
        TextView item_bug;
        TextView item_rtype;
        TextView item_remark;
        Button item_write;
        Button item_log;
    }
}
