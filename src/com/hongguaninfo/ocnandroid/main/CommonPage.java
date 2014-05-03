package com.hongguaninfo.ocnandroid.main;

import java.io.File;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.LayoutInflater.Factory;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.hongguaninfo.ocnandroid.beans.AuthUser;
import com.hongguaninfo.ocnandroid.helper.EastDatabaseHelper;
import com.hongguaninfo.ocnandroid.utils.OSUtil;
import com.hongguaninfo.ocnandroid.widgets.Builders;

/**
 * 公共类---用于实现共有方法--创建菜单等
 *
 * @author chengkai
 *
 */
public class CommonPage extends Activity {
	public static final String RES_LOAD_FOLDER = "/mnt/sdcard/";
	public NotificationManager ncm;
	private final String CLOSE_APP = "close_own";
	private SharedPreferences share;
	private SharedPreferences.Editor editor;
	private CloseBroadcastReceiver receiver;
	public View mainView;
	private View window;
	public PopupWindow popup;
	private int flag=1;

	public void onCreate(Bundle save) {
		super.onCreate(save);
		receiver = new CloseBroadcastReceiver();
		ncm = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);
		share = this.getSharedPreferences("east", Context.MODE_PRIVATE);
		editor = share.edit();
		int tmp = share.getInt("times", 0);
		editor.putInt("times", ++tmp);
		editor.commit();
		queryUserInfo();
		
	}
	public void onRestart(){
		super.onRestart();
		overridePendingTransition(R.anim.anim_zoom_in,
				R.anim.anim_zoom_out);
	}
	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		overridePendingTransition(R.anim.anim_hyperspace_in,
				R.anim.anim_hyperspace_out);
	}
	public void onResume() {
		super.onRestart();
		IntentFilter filter = new IntentFilter();
		filter.addAction(CLOSE_APP);
		this.registerReceiver(receiver, filter);
	}

	@Override
	protected void onDestroy() {
		this.unregisterReceiver(receiver);
		super.onDestroy();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
			switch (keyCode) {
			case KeyEvent.KEYCODE_MENU:
				System.out.println("响应菜单键");
				flag=1;
				popup.showAtLocation(mainView, Gravity.BOTTOM|
						Gravity.CENTER_HORIZONTAL, 0, 0);
				break;
			}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 初始化自定义菜单
	 */
	public void initMenu(int layoutView) {
		window = LayoutInflater.from(this).inflate(R.layout.root_menu, null);
		mainView = LayoutInflater.from(this).inflate(layoutView, null);
		LinearLayout lay1 = (LinearLayout) window.findViewById(R.id.menu_home);
		LinearLayout lay2 = (LinearLayout) window
				.findViewById(R.id.menu_update);
		LinearLayout lay3 = (LinearLayout) window
				.findViewById(R.id.menu_password);
		LinearLayout lay4 = (LinearLayout) window.findViewById(R.id.menu_exit);
		lay1.setOnClickListener(menuListener);
		lay2.setOnClickListener(menuListener);
		lay3.setOnClickListener(menuListener);
		lay4.setOnClickListener(menuListener);
		// 第一个参数菜单布局文件、 第二个参数菜单宽度 、第三个参数菜单高度、4菜单是否可获焦
		popup = new PopupWindow(window, -1, -2, true);
		popup.setAnimationStyle(R.style.menu_animation);
		popup.setOutsideTouchable(true);// 设置popup之外可以触摸
		// 此行用于响应返回键--加入此行对象不能为null值否则返回键不响应
//		popup.setBackgroundDrawable(new BitmapDrawable());
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
	public void initNewMenu(int layoutView) {
		window = LayoutInflater.from(this).inflate(R.layout.root_menu, null);
		mainView = LayoutInflater.from(this).inflate(layoutView, null);
		LinearLayout lay1 = (LinearLayout) window.findViewById(R.id.menu_home);
		LinearLayout lay2 = (LinearLayout) window
				.findViewById(R.id.menu_update);
		LinearLayout lay3 = (LinearLayout) window
				.findViewById(R.id.menu_password);
		LinearLayout lay4 = (LinearLayout) window.findViewById(R.id.menu_exit);
		lay1.setOnClickListener(menuListener);
		lay2.setOnClickListener(menuListener);
		lay3.setOnClickListener(menuListener);
		lay4.setOnClickListener(menuListener);
		// 第一个参数菜单布局文件、 第二个参数菜单宽度 、第三个参数菜单高度、4菜单是否可获焦
		popup = new PopupWindow(window, -1, -2, true);
		popup.setAnimationStyle(R.style.menu_animation);
		popup.setOutsideTouchable(true);// 设置popup之外可以触摸
		// 此行用于响应返回键--加入此行对象不能为null值否则返回键不响应
		popup.setBackgroundDrawable(new BitmapDrawable());
		window.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_MENU) {
					popup.dismiss();
				}
				return false;
			}
		});
	}

	// 菜单单击监听器
	View.OnClickListener menuListener = new View.OnClickListener() {
		public void onClick(View v) {
			int id = v.getId();
			switch (id) {
			case R.id.menu_home:
				startActivity(new Intent(CommonPage.this, UserInfoPage.class));
				popup.dismiss();
				break;
			case R.id.menu_update:
				startActivity(new Intent(CommonPage.this, UpdateApkActivity.class));
				popup.dismiss();
				break;
			case R.id.menu_password:
				startActivity(new Intent(CommonPage.this, UpdatePassword.class));
				popup.dismiss();
				break;
			case R.id.menu_exit:
				popup.dismiss();
				showQuit(1);
				break;
			}
		}
	};

	// 控制菜单的背景色--没有反应--系统菜单
	private void setMenuBackground() {
		this.getLayoutInflater().setFactory(new Factory() {
			public View onCreateView(String name, Context context,
					AttributeSet attrs) {
				if (name.equalsIgnoreCase("com.android.internal.view.menu.IconMenuItemView")) {
					try {
						LayoutInflater f = getLayoutInflater();
						final View view = f.createView(name, null, attrs);
						new Handler().post(new Runnable() {
							public void run() {
								view.setBackgroundColor(Color.GREEN);
							}
						});
						return view;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				return null;
			}
		});
	}

	public AuthUser queryUserInfo() {
		EastDatabaseHelper helper = new EastDatabaseHelper(this, "east.db3", 1);
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.query("user", null, null, null, null, null, null,
				null);
		for (int i = 0; i < cursor.getCount(); ++i) {
			cursor.moveToPosition(i);
			String userId = cursor.getString(cursor.getColumnIndex("userid"));
			if (userId.equals(OSUtil.getUserId(this))) {
				AuthUser user = new AuthUser();
				String userName = cursor.getString(cursor
						.getColumnIndex("username"));
				String password = cursor.getString(cursor
						.getColumnIndex("password"));
				String empno = cursor.getString(cursor.getColumnIndex("empno"));
				String phone = cursor.getString(cursor.getColumnIndex("phone"));
				String stationName = cursor.getString(cursor
						.getColumnIndex("stationname"));
				String status = cursor.getString(cursor
						.getColumnIndex("status"));
				String branchCompany = cursor.getString(cursor
						.getColumnIndex("company"));
//				System.out.println("query-->" + userName + "--" + userId + "--"
//						+ password + "--" + empno + "--" + phone + "--"
//						+ stationName + "--" + status + "--" + branchCompany);
				user.setUserName(userName);
				user.setUserId(userId);
				user.setPassword(password);
				user.setUserEmpno(empno);
				user.setUserPhone(phone);
				user.setStationName(stationName);
				user.setUserStatus(status);
				user.setBranchCompany(branchCompany);
				cursor.close();
				db.close();
				helper = null;
				return user;
			}
		}
		cursor.close();
		db.close();
		helper = null;
		return null;
	}
	
	public void showHint(String str) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}

	// 广播接收器用于结束应用
	private class CloseBroadcastReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(CLOSE_APP)) {
				finish();
			}
		}
	}

	public boolean exit() {
		Intent intent = new Intent();
		intent.setAction(CLOSE_APP);
		this.sendBroadcast(intent);
		return true;
	}

	private void showQuit(final int type) {
		new Builders(CommonPage.this)
		.setContent("你确定退出吗？")
		.setView(R.layout.builder_dialog,R.id.custom_content,
				R.id.custom_positive,R.id.custom_negative)
		.setPositiveButton("确定",new View.OnClickListener() {
			public void onClick(View v) {
				// 如果为0；结束自身；为1结束应用；
				if (type == 0) {
					finish();
				} else if (type == 1) {
					exit();
				}
			}
		}).setNegativeButton("取消",null).show();
	}

	public void sendNotifyInfo() {
		// 启动service
		Intent intentService = new Intent(this, DownService.class);
		intentService.putExtra("total", 100);// 百分比
		startService(intentService);

		Notification noti = new Notification();
		noti.icon = R.drawable.icon;
		noti.defaults = Notification.DEFAULT_SOUND;
		Intent intent = new Intent(this, DownPage.class);
		// 指定跳向UI界面--不确定的界面
		PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
		// 发送通知
		noti.setLatestEventInfo(this, "下载apk文件", "文件下载", pi);
		// 当值为100时处理取消通知栏
		ncm.notify(100, noti);
	}

	public void update() {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(
				Uri.fromFile(new File(RES_LOAD_FOLDER + "ocn_android.apk")),
				"application/vnd.android.package-archive");
		startActivity(intent);
	}

	public void slp(){
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}