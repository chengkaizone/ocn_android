package com.hongguaninfo.ocnandroid.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hongguaninfo.ocnandroid.adapters.GridAdapter;
import com.hongguaninfo.ocnandroid.beans.AuthUser;
import com.hongguaninfo.ocnandroid.helper.EastDatabaseHelper;
import com.hongguaninfo.ocnandroid.service.EastService;
import com.hongguaninfo.ocnandroid.service.ServiceFactory;
import com.hongguaninfo.ocnandroid.utils.OSService;
import com.hongguaninfo.ocnandroid.utils.OSUtil;
import com.hongguaninfo.ocnandroid.utils.OcnUtil;
import com.hongguaninfo.ocnandroid.utils.UploadGPSxy;
import com.hongguaninfo.ocnandroid.widgets.Builders;

/**
 * 主界面
 *
 * @author chengkai
 *
 */
public class MainPage extends CommonPage {
    private int[] draws = { R.drawable.yuyue, R.drawable.weixiu,
            R.drawable.icon2, R.drawable.sign1 };
    private String[] srr = { "预约", "维修", "公告", "签到" };
    private Class[] crr = { OrderPage.class, RepairPage.class,
            NoticePage.class, SettingPage.class };
    private ProgressBar pb;
    private GridView grid;
    private GridAdapter adapter;
    private boolean flag = false;// 是否签到
    public static EastService service;
    private LocationManager lmanager;
    private AuthUser user;
    private String userId;
    private boolean isFirst=true;
    private boolean isGPS = true;
    // 用户状态： 0--表示未签到 ；1--表示未签退 ；2--表示以签退
    String status = "0";
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            int result = msg.arg1;
            pb.setVisibility(View.GONE);
            if (result == 0) {
                showHint("请求签到失败！请重新签到！");
            } else if (result == 1) {
                showHint("签到成功！");
                status = "1";
                adapter.setOnline();
                flag = true;
            } else if (result == 2) {
                showHint("你今天已经成功签退！不允许再次签到！");
            } else if (result == 5) {
                showHint("你已成功签退！");
                status = "2";
                adapter.setOffline(1);
                flag = false;
            } else if (result == -5) {
                showHint("签退失败！请重新签退！");
            } else if (result == -9) {
                showHint("网络错误！请检查你的网络连接！");
            }
            updateStatus();
        }
    };

    public void onCreate(Bundle save) {
        super.onCreate(save);
        setContentView(R.layout.main);
        lmanager=OSService.getLocationManager(this);
        init();
    }

    // 初始化本地控件
    public void init() {
        pb = (ProgressBar) findViewById(R.id.base_top_progress);
        grid = (GridView) findViewById(R.id.main_grid);
        mainView = LayoutInflater.from(this).inflate(R.layout.main, null);
        service = ServiceFactory.getService(this);
        user = (AuthUser) this.getIntent().getSerializableExtra("AuthUser");
        status = user.getUserStatus();
        userId=user.getUserId();
        grid.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (status.equals("0")) {
                    if (position == 3) {
                        requestSignIn();
                    } else {
                        showHint("未签到！请您先签到！");
                    }
                } else if (status.equals("1")) {
                    if (position == 3) {
                        show(0, "签退后就下班！是否确认签退？");
                    } else {
                        skipNext(position);
                    }
                } else {
                    if (position == 3) {
                        showHint("无法再次签到！请联系调度人员！");
                    } else {
                        skipNext(position);
                    }
                }
            }
        });
        initItem();
        updateToNewLocation(lmanager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
        lmanager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                300 * 1000, 50,new LocationListener() {

            public void onStatusChanged(String provider, int status,
                                        Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

           //GPS不能定位时调用的方法
            public void onProviderDisabled(String provider) {
                updateToNewLocation(lmanager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
               /* LocationManager locationManagerNet = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                Location locationNet = locationManagerNet
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                updateToNewLocation(locationNet);*/
            }

            public void onLocationChanged(Location location) {
                updateToNewLocation(location);
            }
        });
        initMenu(R.layout.main);
        //在这里只会注册一次(
        Intent temp=new Intent(this,NotifyService.class);
		startService(temp);
    }

    // 初始化列表项
    private void initItem() {
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < srr.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                    draws[i]);
            map.put("img", bitmap);
            map.put("desc", srr[i]);
            data.add(map);
        }
        adapter = new GridAdapter(this, data);
        grid.setAdapter(adapter);
        updateStatus();
    }

    // 松手事件
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showQuit(1);
        }
        return super.onKeyDown(keyCode, event);
    }
    

    @Override
	protected void onDestroy() {
    	this.stopService(new Intent(this,NotifyService.class));
		super.onDestroy();
	}

	// 签到提示 0表示再次出现提示--1表示执行签退
    public void show(final int i, String info) {
        new Builders(MainPage.this)
                .setContent(info)
                .setView(R.layout.builder_dialog,R.id.custom_content,
                        R.id.custom_positive,R.id.custom_negative)
                .setPositiveButton("确定",new View.OnClickListener() {
                    public void onClick(View v) {
                        // 如果为0；结束自身；为1结束应用；
                        if (i == 0) {
                            show(1, "请再次确认是否签退？");
                        } else {
                            requestQuit();
                        }
                    }
                }).setNegativeButton("取消",null).show();
    }

    private void requestSignIn() {
        pb.setVisibility(View.VISIBLE);
        //true代表模拟数据
        if(OSUtil.getTest(MainPage.this)){
        	slp();
        	Message msg=new Message();
        	msg.arg1 = 1;
            handler.sendMessage(msg);
        }else{
        	new Thread() {
        		@Override
        		public void run() {
        			String str = service.signInOrOut(user.getUserId() + "", "0");
//				System.out.println("请求签到--->" + str);
        			Message msg = new Message();
        			if (str != null && str.equals("true")) {
        				msg.arg1 = 1;
        				handler.sendMessage(msg);
        			} else if (str != null && str.equals("false")) {
        				msg.arg1 = 0;
        				handler.sendMessage(msg);
        			} else {
        				msg.arg1 = -9;// 网络原因
        				handler.sendMessage(msg);
        			}
        		}
        	}.start();
        }
    }

    private void requestQuit() {
        pb.setVisibility(View.VISIBLE);
        new Thread() {
            @Override
            public void run() {
                String str = service.signInOrOut(user.getUserId() + "", "1");
                //System.out.println("请求签退--->" + str);
                Message msg = new Message();
                if (str != null && str.equals("true")) {
                    msg.arg1 = 5;
                    handler.sendMessage(msg);
                } else if (str != null && str.equals("false")) {
                    msg.arg1 = -5;
                    handler.sendMessage(msg);
                } else {
                    msg.arg1 = -9;// 网络原因
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    private void showQuit(final int type) {
        new Builders(MainPage.this)
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

    // 更新状态
    private void updateStatus() {
        if (status.equals("0")) {
            adapter.setOffline(0);
        } else if (status.equals("2")) {
            adapter.setOffline(1);
        } else if (status.equals("1")) {
            adapter.setOnline();
        }
        user.setUserStatus(status);
        updateStatus(user);
    }

    // 判断是否存在此用户
    public void updateStatus(AuthUser user) {
        EastDatabaseHelper helper = new EastDatabaseHelper(this, "east.db3", 1);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query("user", null, null, null, null, null, null,
                null);
        for (int i = 0; i < cursor.getCount(); ++i) {
            cursor.moveToPosition(i);
            String srcId = cursor.getString(cursor.getColumnIndex("userid"));
            if (user.getUserId().equals(srcId)) {
                helper.updateStatus(user.getUserId(), user.getUserStatus());
                cursor.close();
                db.close();
                helper = null;
                // 代表存在此用户
                return;
            }
        }
    }

    /***
     *
     * @param loc 位置
     */
    private void updateToNewLocation(Location loc) {

        if (loc != null) {
            if(isGPS){
                Toast.makeText(this, "获取地理信息成功", Toast.LENGTH_SHORT).show();
                isGPS = false;
            }
            double latitude = loc.getLatitude();
            double longitude = loc.getLongitude();
            final String path =OcnUtil.getUploadGps()+"?userId="+userId+"&latitude="
                    + longitude+"&longitude=" + latitude;
            new Thread(){
                public void run() {
                    UploadGPSxy.uploadGPSXY(path);
                };
            }.start();
        } else {
            if(isFirst){
                //Toast.makeText(this, "无法获取地理信息", Toast.LENGTH_SHORT).show();
                isFirst=false;
                updateToNewLocation(lmanager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
            }
        }
    }


    // 跳转到下一个界面
    private void skipNext(int loc) {
        Intent intent = new Intent(this, crr[loc]);
        intent.putExtra("AuthUser", user);
        startActivity(intent);
    }
    //使用到后台服务的方法
    public String getNewOrder(){
    	//此处调用webservice方法
    	return "hehe";
    }
}
