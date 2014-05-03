package com.hongguaninfo.ocnandroid.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hongguaninfo.ocnandroid.beans.AuthUser;
import com.hongguaninfo.ocnandroid.helper.EastDatabaseHelper;
import com.hongguaninfo.ocnandroid.serviceimpl.EastServiceImpl;
import com.hongguaninfo.ocnandroid.utils.JsonUtil;
import com.hongguaninfo.ocnandroid.utils.OSService;
import com.hongguaninfo.ocnandroid.utils.OSUtil;

/**
 * 登录页面
 *
 * @author chengkai
 *
 */
public class LoginPage extends Activity implements OnClickListener {
	private Button login_btn;
	private EditText userinput, passinput;
	private ProgressBar pb;
	private EastServiceImpl service;
	private String userName, pass;
	private TextView titlebar;
	private InputMethodManager imm;
	double start=0;
	double end=0;
	/**
	 * 返回0表示用户无效--1表示手机还未注册--2用户未注册--3密码错误
	 */
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			end=System.currentTimeMillis();
//			System.out.println("结束计时--->"+end);
//			System.out.println(end-start);
//			System.out.println("时间差--->"+"end-start="+((end-start)/1000));
			pb.setVisibility(View.GONE);
			login_btn.setEnabled(true);
			if (msg.what == -1) {
				showHint("网络错误！");
			} else if (msg.what == 0x111) {
				showHint("无效的用户名！");
			} else if (msg.what == 0x222) {
				showHint("手机还未在系统上注册");
			} else if (msg.what == 0x333) {
				showHint("用户还未注册");
			} else if (msg.what == 0x444) {
				showHint("密码错误！");
			} else {
				String json = (String) msg.obj;
				System.out.println(json);
				List<AuthUser> list = JsonUtil.parseUsers(json);
				if (list.size() > 0) {
					AuthUser us = list.get(0);
//					System.out.println(us.getUserId() + "==="
//							+ us.getLoginName());
					// showHint("登录成功！");
					pb.setVisibility(View.GONE);
					// 执行数据库操作
					operaData(us);
					OSUtil.putUser(LoginPage.this, userName);
					OSUtil.putUserId(LoginPage.this, us.getUserId() + "");
					//如果等于初始密码，则更新密码
					if(pass.equals(OSUtil.getDefaultPsd(LoginPage.this))){
						//是初始密码表示第一次使用客户端
						OSUtil.putFlag(LoginPage.this, true);
						Intent intent = new Intent(LoginPage.this, UpdatePassword.class);
						intent.putExtra("AuthUser", us);
						startActivity(intent);
					}else{
						// 跳转至主页面
						Intent intent = new Intent(LoginPage.this, MainPage.class);
						intent.putExtra("AuthUser", us);
						startActivity(intent);
					}
					finish();
					overridePendingTransition(R.anim.anim_hyperspace_in,
							R.anim.anim_hyperspace_out);
				} else {
					showHint("获取用户数据失败！");
				}
			}
		}
	};

	// 创建活动时回调此方法
	public void onCreate(Bundle save) {
		super.onCreate(save);
		setContentView(R.layout.login);
		init();
	}

	// 初始化控件
	private void init() {
		pb = (ProgressBar) findViewById(R.id.base_top_progress);
		titlebar = (TextView) findViewById(R.id.base_top_title);
		titlebar.setText("用户登录");
		login_btn = (Button) findViewById(R.id.login_button);
		userinput = (EditText) findViewById(R.id.loginusername);
		passinput = (EditText) findViewById(R.id.loginpassword);
		login_btn.setOnClickListener(this);
		service = new EastServiceImpl(this);
		imm = (InputMethodManager) this
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		initMenu(R.layout.login);
	}

	// 当界面可以获得焦点时回调此方法
	protected void onResume() {
		String user = OSUtil.getUser(this);
		userinput.setText(user);
		super.onResume();
	}

	// 监听器回调的方法
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.login_button:
//			login_btn.setEnabled(false);
			userName = userinput.getText().toString().trim();
			pass = passinput.getText().toString().trim();
			if (userName.equals("") || pass.equals("")) {
				showHint("请输入完整的登录信息！");
//				login_btn.setEnabled(true);
				return;
			} else {
				if (imm.isActive()) {
					imm.hideSoftInputFromWindow(passinput.getWindowToken(), 0);
				}
			}
//			Toast.makeText(LoginPage.this,"请稍后...",2000).show();
//			start=System.currentTimeMillis();
//			System.out.println("计时开始--->"+start);
//			System.out.println(userName + "====" + pass);
			NetworkInfo net=OSService.getConnectivityManager(this).getActiveNetworkInfo();
			if(net==null){
				Toast.makeText(this,"网络连接未打开!无法登录!!!",2000).show();
			}else{
				pb.setVisibility(View.VISIBLE);
				login_btn.setEnabled(false);
				if(OSUtil.getTest(LoginPage.this)){
					new Thread(){
						public void run(){
							try {
								Thread.sleep(1000);//模拟联网
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							if(!userName.equals("gan")){
								handler.sendEmptyMessage(0x333);
							}else if(!pass.equals("111111")){
								handler.sendEmptyMessage(0x444);
							}else{
								Message msg = new Message();
								msg.obj = jsonStr();
								handler.sendMessage(msg);
							}
						}
					}.start();
				}else{
					new Thread() {
						public void run() {
							String strjson = service.loginCheck(userName, pass);
							if (strjson != null) {
								if (strjson.equals("0")) {
									handler.sendEmptyMessage(0x111);
								} else if (strjson.equals("1")) {
									handler.sendEmptyMessage(0x222);
								} else if (strjson.equals("2")) {
									handler.sendEmptyMessage(0x333);
								} else if (strjson.equals("3")) {
									handler.sendEmptyMessage(0x444);
								} else {
									Message msg = new Message();
									msg.obj = strjson;
									handler.sendMessage(msg);
								}
							} else {
								// 网络错误
								handler.sendEmptyMessage(-1);
							}
						}
					}.start();
				}
			}
			break;
		}
	}

	private void showHint(String str) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}

	private void operaData(AuthUser us) {
		EastDatabaseHelper helper = new EastDatabaseHelper(this, "east.db3", 1);
		// 没有数据就插入数据
		if (!isExists(us)) {
			helper.insertUser(us.getUserName(), us.getUserId() + "",
					us.getPassword(), us.getUserEmpno(), us.getUserPhone(),
					us.getStationName(), us.getUserStatus(),
					us.getBranchCompany());
		}
		helper = null;
	}

	// 判断是否存在此用户
	public boolean isExists(AuthUser user) {
		EastDatabaseHelper helper = new EastDatabaseHelper(this, "east.db3", 1);
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.query("user", null, null, null, null, null, null,
				null);
		for (int i = 0; i < cursor.getCount(); ++i) {
			cursor.moveToPosition(i);
			String srcId = cursor.getString(cursor.getColumnIndex("userid"));
			if (user.getUserId().equals(srcId)) {
				helper.updateUser(user.getUserId(), user.getPassword(),
						user.getUserStatus());
				cursor.close();
				db.close();
				helper = null;
				// 代表存在此用户
				return true;
			}
		}
		cursor.close();
		db.close();
		helper = null;
		return false;
	}
	private PopupWindow popup;
	private View window;
	private TextView menuTest;
	public View mainView;
	private int flag=1;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//事件响应分为活动层和视图层；焦点在活动层时事件如果在视图层重写会响应两次；反之响应一次
			switch (keyCode) {
			case KeyEvent.KEYCODE_MENU:
				System.out.println("响应菜单键");
				/*flag=1;
				*//**
				 * 第一个参数只代表一个布局---官方文档指明要求是父类布局--欠妥popup的显示位置屈居于布局本身及后面三个参数
				 * 如果宽高已经充满则后面的属性无效
				 *//*
				if(mainView!=null)
				popup.showAtLocation(mainView, Gravity.BOTTOM|
						Gravity.CENTER_HORIZONTAL, 0, 0);*/
				return true;
			}
		return super.onKeyDown(keyCode, event);
	}
	public void initMenu(int layoutView) {
		window = LayoutInflater.from(this).inflate(R.layout.menu_1, null);
		menuTest=(TextView)window.findViewById(R.id.test_menu);
		if(OSUtil.getTest(LoginPage.this)){
			menuTest.setText("测试服务器");
		}else{
			menuTest.setText("模拟无网络");
		}
		mainView = LayoutInflater.from(this).inflate(layoutView, null);
		LinearLayout lay1 = (LinearLayout) window
				.findViewById(R.id.menu_switch_theme);
		lay1.setOnClickListener(menuListener);
		// 第一个参数菜单布局文件、 第二个参数菜单宽度 、第三个参数菜单高度、4菜单是否可获焦
		popup = new PopupWindow(window, -1, -2, true);
		popup.setAnimationStyle(R.style.menu_animation);
		popup.setOutsideTouchable(true);// 设置popup之外可以触摸
		window.setFocusableInTouchMode(true);
		window.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				switch(keyCode){
				case KeyEvent.KEYCODE_MENU:
				case KeyEvent.KEYCODE_BACK:
					System.out.println("响应视图层");
					if(flag==1){
						flag=2;
					}else if(flag==2){
						popup.dismiss();						
					}
					break;
				}
				return true;
			}
		});
	}
	// 菜单单击监听器
		View.OnClickListener menuListener = new View.OnClickListener() {
			public void onClick(View v) {
				int id = v.getId();
				switch (id) {
				case R.id.menu_switch_theme:
					boolean bool=OSUtil.getTest(LoginPage.this);
					if(bool){
						OSUtil.putTest(LoginPage.this, false);
						menuTest.setText("模拟无网络");
					}else{
						OSUtil.putTest(LoginPage.this, true);
						menuTest.setText("测试服务器");
					}
					popup.dismiss();
					break;
				}
			}
		};
		private AuthUser initUser(){
			AssetManager asset = this.getAssets();
			try {
				InputStream input = asset.open("user.txt");
				InputStreamReader isr = new InputStreamReader(input, "gbk");
				BufferedReader reader = new BufferedReader(isr);
				StringBuilder json = new StringBuilder();
				String temp = "";
				while ((temp = reader.readLine()) != null) {
					json.append(temp);
				}
				List<AuthUser> list = JsonUtil.parseUsers(json.toString());
				return list.get(0);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		private String jsonStr(){
			AssetManager asset = this.getAssets();
			try {
				InputStream input = asset.open("user.txt");
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
}
