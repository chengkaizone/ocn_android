package com.hongguaninfo.ocnandroid.main;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.hongguaninfo.ocnandroid.adapters.ListRadioAdapter;
import com.hongguaninfo.ocnandroid.beans.AuthUser;
import com.hongguaninfo.ocnandroid.beans.ClientInfo;
import com.hongguaninfo.ocnandroid.service.EastService;
import com.hongguaninfo.ocnandroid.service.ServiceFactory;
import com.hongguaninfo.ocnandroid.utils.DateUtil;

/**
 * 填写维修单
 * 
 * @author chengkai
 * 
 */
public class OrderWritePage extends CommonPage {
	private String[] srr = { "上门维修", "电话已联系好", "用户改期", "无法联系用户", "电话支持排障",
			"区域性故障维护", "其他" };
	private String[] times = { "上午", "下午", "晚上" };
	private String[] operas = { "改约", "改派", "回单", "退单" };
	private String[] srrType = { "CT_IN", "CT_AD", "CT_CH", "CT_NA", "CT_SD",
			"CT_RG", "CT_OT" };
	private String[] timeType = { "A", "P", "N" };
	private String[] operasType = { "1", "2", "3", "4" };
	private String curSrrType = srrType[0];
	private String curTimeType = timeType[0];
	private String curOpera = operasType[0];
	private ArrayAdapter<String[]> spAdapter1, spAdapter2, spAdapter3;
	private LinearLayout operalay, date_time;
	private long curTime, resultTime;
	private Spinner sp;
	private Spinner sp_time;
	private Spinner sp_opera;
	private int curIndex = 0;
	private EditText input1;
	private DatePicker date;
	private Button toggle;
	private int year, month, day;
	private TextView title;
	private ProgressBar pb;
	// 最终时间
	private String resultDate = "";
	private EastService service;
	private ClientInfo info;
	private AuthUser user;

	private TextView no, name, phone;
	private ListRadioAdapter operaAdapter, timeAdapter;
	Handler handler=new Handler(){
		public void handleMessage(Message msg) {
			pb.setVisibility(View.GONE);
			if(msg.what==0x999){
				showHint("提交失败!");
                toggle.setEnabled(true);
			}
			if(msg.arg1==0x666){
				String result=(String)msg.obj;
				if(result.equals("true")){
					showHint("提交成功!");
					finish();
				}else{
					showHint("提交失败!");
                    toggle.setEnabled(true);
				}
			}
		}
	};
	public void onCreate(Bundle save) {
		super.onCreate(save);
		setContentView(R.layout.order_write_page);
		info = (ClientInfo) this.getIntent().getSerializableExtra("ClientInfo");
		user = (AuthUser) this.getIntent().getSerializableExtra("AuthUser");
		init();
	}

	private void init() {
		// 初始化主题
		title = (TextView) findViewById(R.id.base_top_title);
		pb = (ProgressBar) findViewById(R.id.base_top_progress);
		title.setText("提交预约信息");

		// 客户信息
		no = (TextView) findViewById(R.id.order_write_no);
		name = (TextView) findViewById(R.id.order_write_name);
		phone = (TextView) findViewById(R.id.order_write_phone);
		no.setText(info.getOrderNo());
		name.setText(info.getCustName());
		phone.setText(info.getPhone());
		// 多项选择
		operalay = (LinearLayout) findViewById(R.id.order_opera_lay);
		date_time = (LinearLayout) findViewById(R.id.order_date_time_lay);
		input1 = (EditText) findViewById(R.id.order_write_et);
		sp = (Spinner) findViewById(R.id.order_write_sp);
		sp_time = (Spinner) findViewById(R.id.orders_sptime);
		sp_opera = (Spinner) findViewById(R.id.orders_spopera);
		date = (DatePicker) findViewById(R.id.order_write_date);
		toggle = (Button) findViewById(R.id.order_toggle_button);
		service = ServiceFactory.getService(this);
		initSpinners();
		initDate();
		// 设置保存的监听
		toggle.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Map<String, String> map = new HashMap<String, String>();
				String reservResult = curSrrType;
				String userId = user.getUserId().toString();
				String orderId = info.getOrderId();
				String remark = input1.getText().toString().trim();
				map.put("userId", userId);
				map.put("orderId", orderId);
				map.put("remark", remark);
				map.put("reservResult", reservResult);
                //2012.7.19将“电话支持排障”和“上门维修”一样处理，预约后，工单状态为待登记状态
				if (curSrrType.equals("CT_IN") || curSrrType.equals("CT_SD")) {
//					System.out.println(curTime + "=====" + resultTime);
					if (curTime > resultTime) {
						showHint("请选择正确的时间！");
						return;
					}
					String reservDate = resultDate;
					String reservPeriod = curTimeType;
					map.put("reservDate", reservDate);
					map.put("reservPeriod", reservPeriod);
					map.put("option", "");
					requestBack(0,map);
				} else if (curOpera.equals("1")) {
//					System.out.println(curTime + "=====" + resultTime);
					if (curTime > resultTime) {
						showHint("请选择正确的时间！");
						return;
					}
					String reservDate = resultDate;
					String reservPeriod = curTimeType;
					map.put("reservDate", reservDate);
					map.put("reservPeriod", reservPeriod);
					map.put("option", "9");
					requestBack(0,map);
				} else {
					if (remark.equals("")) {
						showHint("备注不能为空请填写！");
						return;
					} else {
						if (curOpera.equals("2")) {
							map.put("type", "2");
							requestBack(1,map);
						} else if (curOpera.equals("3")) {
							map.put("type", "3");
							requestBack(1,map);
						} else if (curOpera.equals("4")) {
							map.put("type", "4");
							requestBack(1,map);
						}
					}

				}
			}

		});
		// 初始化时间对话框--初始化时间时月份不能加以0开始计算
		date.init(year, month, day, new OnDateChangedListener() {
			public void onDateChanged(DatePicker view, int year,
					int monthOfYear, int dayOfMonth) {
				OrderWritePage.this.year = year;
				OrderWritePage.this.month = monthOfYear + 1;
				OrderWritePage.this.day = dayOfMonth;
//				System.out.println(year + "-" + monthOfYear + "-" + dayOfMonth);
				resultDate = OrderWritePage.this.year + "-"
						+ OrderWritePage.this.month + "-"
						+ OrderWritePage.this.day;
//				System.out.println("change--->" + resultDate);
				// 格式化时也要+1转化为长整形
				resultTime = DateUtil.getDate(resultDate);
//				System.out.println(curTime - resultTime);
			}
		});
	}

    /***
     *  填写预约信息后保存
     * @param type 0预约改约  1回单退单改派
     * @param map
     */
	public void requestBack(final int type,final Map<String,String> map){
		pb.setVisibility(View.VISIBLE);
        toggle.setEnabled(false);
		new Thread(){
			public void run() {
				Message msg=new Message();
				if(type==0){
					String return_flag = service.reservationSave(map);
					if(return_flag!=null){
						msg.obj=return_flag;
						msg.arg1=0x666;
						handler.sendMessage(msg);
					}else{
						handler.sendEmptyMessage(0x999);
					}
				}else if(type==1){
					String return_flag = service.reservationDistributeorBackorReturn(map);
					if(return_flag!=null){
						msg.obj=return_flag;
						msg.arg1=0x666;
						handler.sendMessage(msg);
					}else{
						handler.sendEmptyMessage(0x999);
					}
				}
			};
		}.start();
	}
	/**
	 * 下拉菜单监听器--上门维修
     * 2012.7.19  新增当预约结果为“电话支持排障”时，和预约结果为“上门维修”的处理一样
	 */
	private OnItemSelectedListener listener = new OnItemSelectedListener() {
		public void onItemSelected(AdapterView<?> arg0, View view, int loc,
				long arg3) {
//			System.out.println("sp");
			curSrrType = srrType[loc];
			if (loc == 0 || loc==4) {
				date_time.setVisibility(View.VISIBLE);
				operalay.setVisibility(View.GONE);
			} else if (loc != 0 && loc!=4 && curIndex == 0) {
				// 下拉其他选项时设置可见
				operalay.setVisibility(View.VISIBLE);
				// 隐藏时间
				date_time.setVisibility(View.VISIBLE);
			} else if (loc != 0 && loc!=4 && curIndex != 0) {
				operalay.setVisibility(View.VISIBLE);
				date_time.setVisibility(View.GONE);
			}
		}

		public void onNothingSelected(AdapterView<?> arg0) {
		}
	};

	private void initSpinners() {
		// 上门维修。。。
		spAdapter1 = new ArrayAdapter(this,
				android.R.layout.simple_spinner_item, srr);
		// 设置自顶下拉项
		spAdapter1
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 为下拉列表设置适配
		sp.setAdapter(spAdapter1);
		sp.setSelection(0, true);
		// 设置被选监听
		sp.setOnItemSelectedListener(listener);
		// 时间段适配器
		spAdapter2 = new ArrayAdapter(this,
				android.R.layout.simple_spinner_item, times);
		spAdapter2
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_time.setAdapter(spAdapter2);
		sp_time.setSelection(0, true);
		sp_time.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				curTimeType = timeType[position];
				// 获取被选的值
//				System.out.println(times[position]);
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		// 改约适配器
		spAdapter3 = new ArrayAdapter(this,
				android.R.layout.simple_spinner_item, operas);
		spAdapter3
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_opera.setAdapter(spAdapter3);
		sp_opera.setSelection(0, true);
		sp_opera.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				curIndex = position;
				curOpera = operasType[position];
				date_time.setVisibility(View.VISIBLE);
				if (position != 0) {
					// 隐藏时间
					date_time.setVisibility(View.GONE);
				} else {
					// 显示时间
					date_time.setVisibility(View.VISIBLE);
				}
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
	}

	// 为时间对话框初始化时间
	private void initDate() {
		resultDate = getCurDate();
		curTime =DateUtil.getDate(resultDate);
		resultTime =curTime;
//		System.out.println("时间-->" +curTime);
	}

	private String getCurDate() {
		Calendar cal = Calendar.getInstance();
		year = cal.get(Calendar.YEAR);
		month = cal.get(Calendar.MONTH);
		day = cal.get(Calendar.DAY_OF_MONTH);
		// 显示时要加1
		String str = year + "-" + (month + 1) + "-" + day;
//		System.out.println("str-->"+str);
		cal = null;
		return str;
	}
}
