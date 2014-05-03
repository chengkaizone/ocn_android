package com.hongguaninfo.ocnandroid.main;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hongguaninfo.ocnandroid.beans.AuthUser;
import com.hongguaninfo.ocnandroid.helper.EastDatabaseHelper;
import com.hongguaninfo.ocnandroid.service.EastService;
import com.hongguaninfo.ocnandroid.service.ServiceFactory;
import com.hongguaninfo.ocnandroid.utils.OSUtil;

/**
 * 修改密码
 *
 * @author chengkai
 *
 */
public class UpdatePassword extends Activity implements OnClickListener {
    private TextView title;
    private ProgressBar pb;
    private EditText old, news1, news2;
    private Button save;
    private EastService service;
    private AuthUser user;
    private String newPsd;
    private String oldPwd;
    private boolean flag;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            pb.setVisibility(View.GONE);
            if (msg.what == 0x123) {
                showHint("原密码错误！");
                save.setEnabled(true);
            } else if (msg.what == 0x666) {
                showHint("密码修改成功！");
                //更新密码操作
                updatePsd(OSUtil.getUserId(UpdatePassword.this),newPsd);
                //如果第一次使用客户端从登陆界面进入、需要传递用户信息
                if(flag){
                	Intent intent=new Intent(UpdatePassword.this,MainPage.class);
                	intent.putExtra("AuthUser", user);
                	startActivity(intent);
                }
                finish();
                UpdatePassword.this.overridePendingTransition(R.anim.scale_anim,0);
            } else if (msg.what == 0x999) {
                save.setEnabled(true);
                //系统错误
                showHint("网络错误！无法修改密码！");
            }
        }
    };

    public void onCreate(Bundle save) {
        super.onCreate(save);
        setContentView(R.layout.set_pass_page);
        flag=OSUtil.getFlag(this);
        if(flag){
        	user = (AuthUser) this.getIntent().getSerializableExtra("AuthUser");
        }
        init();
    }

    private void init() {
        title = (TextView) findViewById(R.id.base_top_title);
        pb = (ProgressBar) findViewById(R.id.base_top_progress);
        title.setText("设置密码");
        old = (EditText) findViewById(R.id.set_old);
        news1 = (EditText) findViewById(R.id.set_news1);
        news2 = (EditText) findViewById(R.id.set_news2);
        save = (Button) findViewById(R.id.set_save_button);
        save.setOnClickListener(this);
        service = ServiceFactory.getService(this);
    }

    public void onClick(View v) {
        int id = v.getId();
        String n = old.getText().toString().trim();
        String n1 = news1.getText().toString().trim();
        String n2 = news2.getText().toString().trim();
//		System.out.println(n1 + "====" + n2);
        switch (id) {
            case R.id.set_save_button:
                if (!n1.equals(n2)) {
                    showHint("两次密码输入不一致！请重新输入！");
                    news1.setText("");
                    news2.setText("");
                } else if (n1.length() < 6) {
                    showHint("密码不能少于6位！请重新输入！");
                    news1.setText("");
                    news2.setText("");
                }
                else if(n1.equals("123456")){
                    showHint("新密码不能为初始密码");
                    news1.setText("");
                    news2.setText("");
                }
                else {
                    pb.setVisibility(View.VISIBLE);
                    save.setEnabled(false);
                    newPsd=n1;
                    requestUpdatePsd(n, n1);
                }
                break;
        }
    }

    private void requestUpdatePsd(final String oldPwd, final String pass) {
        new Thread() {
            public void run() {
                String result = service.updatePwd(
                        OSUtil.getUserId(UpdatePassword.this), oldPwd, pass);
				System.out.println(OSUtil.getUserId(UpdatePassword.this));
                if(result==null){
                    handler.sendEmptyMessage(0x999);
                } else if (result.equals("0")) {
                    handler.sendEmptyMessage(0x123);
                } else if (result.equals("1")) {
                    handler.sendEmptyMessage(0x666);
                } else if (result.equals("2")) {
                    handler.sendEmptyMessage(0x999);
                } else {
                    handler.sendEmptyMessage(0x999);
                }

            }
        }.start();
    }


    /***
     * 重写返回键方法
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            //如果没有更新成功过密码提示不能进入主界面操作
//			System.out.println("userpsd"+queryUserInfo().getPassword());
//			System.out.println("def-->"+OSUtil.getDefaultPsd(this));
            if(this.queryUserInfo().equals(OSUtil.getDefaultPsd(this))){
                startActivity(new Intent(this,LoginPage.class));
                finish();
            }else{
                finish();
                UpdatePassword.this.overridePendingTransition(R.anim.scale_anim,0);
            }
        }
        return true;
    }

    public void showHint(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    /***
     * 更新本地数据表的用户密码
     * @param userId
     * @param psd
     */
    private void updatePsd(String userId,String psd){
        EastDatabaseHelper helper = new EastDatabaseHelper(this, "east.db3", 1);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query("user", null, null, null, null, null, null,
                null);
        for (int i = 0; i < cursor.getCount(); ++i) {
            cursor.moveToPosition(i);
            String srcId = cursor.getString(cursor.getColumnIndex("userid"));
            if (userId.equals(srcId)) {
                helper.updatePassword(userId,psd);
                break;
            }
        }
        cursor.close();
        db.close();
        helper = null;
    }

    /**
     *  从本地表中得到当前用户的密码
     * @return
     */
    private String queryUserInfo() {
        String pwd="";
        EastDatabaseHelper helper = new EastDatabaseHelper(this, "east.db3", 1);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query("user", null, null, null, null, null, null,
                null);
        for (int i = 0; i < cursor.getCount(); ++i) {
            cursor.moveToPosition(i);
            String userId = cursor.getString(cursor.getColumnIndex("userid"));
            if (userId.equals(OSUtil.getUserId(this))) {
                pwd = cursor.getString(cursor.getColumnIndex("password"));
                break;
            }
        }
        cursor.close();
        db.close();
        helper = null;
        return pwd;
    }
}
