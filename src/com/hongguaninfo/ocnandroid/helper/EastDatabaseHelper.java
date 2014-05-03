package com.hongguaninfo.ocnandroid.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 操作数据库用于---管理用户表
 *
 * @author Administrator
 *
 */
public class EastDatabaseHelper extends SQLiteOpenHelper {
    private static final String DB = "east.db3";

    public EastDatabaseHelper(Context paramContext, String dataName,
                              int paramInt) {
        super(paramContext, DB, null, paramInt);
    }

    public EastDatabaseHelper(Context paramContext, String dataName,
                              SQLiteDatabase.CursorFactory paramCursorFactory, int paramInt) {
        super(paramContext, DB, paramCursorFactory, paramInt);
    }

    public void onCreate(SQLiteDatabase db) {
        String drop = "drop table if exists user";
        db.execSQL(drop);
        String sql = "create table user(_id integer primary key autoincrement,'username'"
                + " text not null,'userid' text not null,"
                + "'password' text not null,'empno' text not null,"
                + "'phone' text not null,'stationname' text not null,"
                + "'status' text not null,'company' text not null)";
        db.execSQL(sql);
    }

    public void onUpgrade(SQLiteDatabase db, int paramInt1, int paramInt2) {
        String drop = "drop table if exists user";
        db.execSQL(drop);
        onCreate(db);
    }

    public void insertUser(String userName, String userId, String password,
                           String empno, String phone, String stationName, String status,
                           String branchCompany) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("username", userName);
        cv.put("userid", userId);
        cv.put("password", password);
        cv.put("empno", empno);
        cv.put("phone", phone);
        cv.put("stationname", stationName);
        cv.put("status", status);
        cv.put("company", branchCompany);
        db.insert("user", null, cv);
        db.close();
    }

    public void updateUser(String userId, String password, String status) {
        SQLiteDatabase db = getWritableDatabase();
        String str = "update user set password='" + password + "' , status='"
                + status + "' where userid='" + userId + "'";
        db.execSQL(str);
        db.close();
    }

    public void updateStatus(String userId, String status) {
        SQLiteDatabase db = getWritableDatabase();
        String str = "update user set status='" + status + "' where userid='"
                + userId + "'";
        db.execSQL(str);
        db.close();
    }

    public void updatePassword(String userId, String password) {
        SQLiteDatabase db = getWritableDatabase();
        String str = "update user set password='" + password + "' where userid='"
                + userId + "'";
        db.execSQL(str);
        db.close();
    }
}
