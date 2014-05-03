package com.hongguaninfo.ocnandroid.main;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import com.hongguaninfo.ocnandroid.beans.AuthUser;
import com.hongguaninfo.ocnandroid.helper.EastDatabaseHelper;
import com.hongguaninfo.ocnandroid.utils.OSUtil;

/**
 * 显示用户信息页面
 * 
 * @author chengkai
 * 
 */
public class UserInfoPage extends Activity {
	private TextView title;
	private TextView userName, userId, empno, phone, station, status;
	private AuthUser user;

	public void onCreate(Bundle save) {
		super.onCreate(save);
		setContentView(R.layout.user_info);
		user = this.queryUserInfo();
		init();
	}

	private void init() {
		title = (TextView) findViewById(R.id.base_top_title);
		title.setText("用户信息");
		userName = (TextView) findViewById(R.id.user_name);
		userId = (TextView) findViewById(R.id.user_id);
		empno = (TextView) findViewById(R.id.user_empno);
		phone = (TextView) findViewById(R.id.user_phone);
		station = (TextView) findViewById(R.id.user_station_name);
		status = (TextView) findViewById(R.id.user_status);

		userName.setText(user.getUserName());
		userId.setText(user.getUserId());
		empno.setText(user.getUserEmpno());
		phone.setText(user.getUserPhone());
		station.setText(user.getStationName());
		String st = user.getUserStatus();
		if (st.equals("0")) {
			status.setText("未签到");
		} else if (st.equals("1")) {
			status.setText("已签到");
		} else if (st.equals("2")) {
			status.setText("已签退");
		}
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
}
