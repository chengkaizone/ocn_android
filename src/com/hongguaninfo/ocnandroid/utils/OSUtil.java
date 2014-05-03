package com.hongguaninfo.ocnandroid.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.telephony.TelephonyManager;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;

/**
 * 获取手机系统信息的工具类
 *
 * @author Administrator
 *
 */
public class OSUtil {
    public static String getVersionName(Activity act) {
        String name = "";
        try {
            name = act.getPackageManager().getPackageInfo(act.getPackageName(),
                    0).versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return name;
    }

    /**
     * 获取apk版本号
     *
     * @param act
     * @return
     */
    public static int getVersionCode(Activity act) {
        int code = 1;
        try {
            code = act.getPackageManager().getPackageInfo(act.getPackageName(),
                    0).versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return code;
    }

    /**
     * 从应用中获取已登录用户名；第一次使用返回空字符串
     *
     * @param context
     * @return
     */
    public static String getUser(Context context) {
        SharedPreferences prefer = context.getSharedPreferences("east",
                Context.MODE_PRIVATE);
        String user = prefer.getString("user", "");// 默认返回空；
        prefer = null;
        return user;
    }

    /**
     * 通过应用文件获取用户id
     *
     * @param context
     * @return
     */
    public static String getUserId(Context context) {
        SharedPreferences prefer = context.getSharedPreferences("east",
                Context.MODE_PRIVATE);
        String user = prefer.getString("userid", "");// 默认返回空；
        prefer = null;
        return user;
    }

    /**
     * 通过应用文件获取OrderId
     *
     * @param context
     * @return
     */
    public static String getOrderId(Context context) {
        SharedPreferences prefer = context.getSharedPreferences("east",
                Context.MODE_PRIVATE);
        String user = prefer.getString("orderid", "");// 默认返回空；
        prefer = null;
        return user;
    }
    /**
     * 获取更新密码状态
     *
     * @param context
     * @return
     */
    public static boolean getUpdateState(Context context) {
        SharedPreferences prefer = context.getSharedPreferences("east",
                Context.MODE_PRIVATE);
        boolean flag=prefer.getBoolean("update",false);
        prefer = null;
        return flag;
    }
    /**
     * 获取初始密码
     * @param context
     * @return
     */
    public static String getDefaultPsd(Context context) {
        SharedPreferences prefer = context.getSharedPreferences("east",
                Context.MODE_PRIVATE);
        String psd=prefer.getString("defaultPsd","123456");
        prefer = null;
        return psd;
    }

    /**
     * 将用户名放入应用文件中
     *
     * @param context
     * @param user
     * @return
     */
    public static boolean putUser(Context context, String user) {
        try {
            SharedPreferences prefer = context.getSharedPreferences("east",
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefer.edit();
            editor.putString("user", user);
            editor.commit();
            prefer = null;
            editor = null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    /**
     * 更新密码状态
     *
     * @param context
     * @param flag
     * @return
     */
    public static boolean putUpdateState(Context context,boolean flag) {
        try {
            SharedPreferences prefer = context.getSharedPreferences("east",
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefer.edit();
            editor.putBoolean("update", flag);
            editor.commit();
            prefer = null;
            editor = null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 放入userId
     *
     * @param context
     * @param userId
     * @return
     */
    public static boolean putUserId(Context context, String userId) {
        try {
            SharedPreferences prefer = context.getSharedPreferences("east",
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefer.edit();
            editor.putString("userid", userId);
            editor.commit();
            prefer = null;
            editor = null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 放入orderId
     *
     * @param context
     * @param userId
     * @return
     */
    public static boolean putOrderId(Context context, String userId) {
        try {
            SharedPreferences prefer = context.getSharedPreferences("east",
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefer.edit();
            editor.putString("orderid", userId);
            editor.commit();
            prefer = null;
            editor = null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    /**
     * 检保存是否第一次使用客户端
     * @param context
     * @param flag
     * @return
     */
    public static boolean putFlag(Context context, boolean flag) {
        try {
            SharedPreferences prefer = context.getSharedPreferences("east",
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefer.edit();
            editor.putBoolean("isFirst", flag);
            editor.commit();
            prefer = null;
            editor = null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    /**
     * 判断是否第一次使用客户端
     * @param context
     * @return
     */
    public static boolean getFlag(Context context) {
        SharedPreferences prefer = context.getSharedPreferences("east",
                Context.MODE_PRIVATE);
        boolean flag=prefer.getBoolean("isFirst",false);
        prefer = null;
        return flag;
    }
    /**
     * 判断是否使用模拟无网络数据
     * @param context
     * @return
     */
    public static boolean getTest(Context context) {
        SharedPreferences prefer = context.getSharedPreferences("east",
                Context.MODE_PRIVATE);
        boolean flag=prefer.getBoolean("isTest",false);
        prefer = null;
        /*return flag;*/
        return true;
    }
    /**
     * 保存测试服务器数据
     * @param context
     * @param flag
     * @return
     */
    public static boolean putTest(Context context, boolean flag) {
        try {
            SharedPreferences prefer = context.getSharedPreferences("east",
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefer.edit();
            editor.putBoolean("isTest", flag);
            editor.commit();
            prefer = null;
            editor = null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    /**
     * 获取esn编码
     *
     * @param context
     * @return
     */
    public static String getESN(Context context) {
        String esn = getManager(context).getDeviceId();
        return esn;
    }

    /**
     * 获取imsi编码--又名手机串号
     *
     * @param context
     * @return
     */
    public static String getIMSI(Context context) {
        String imsi = getManager(context).getDeviceId();
        return imsi;
    }

    /**
     * 获取本机号码
     *
     * @param context
     * @return
     */
    public static String getSIMNumber(Context context) {
        String sim = getManager(context).getLine1Number();
        // String sim=getManager(context).getSimSerialNumber();
        return sim;
    }

    /**
     * 获取手机机型编码
     *
     * @return
     */
    public static String getTermcode() {
        String termcode = android.os.Build.MODEL;
        return termcode;
    }

    /**
     * 添加ListView的动态显示效果
     *
     * @return
     */
    public static LayoutAnimationController getLac(Context context) {
        ScaleAnimation scale = new ScaleAnimation(0.1f, 1.0f, 0.1f, 1.0f);
        scale.setDuration(500);
        LayoutAnimationController lac = new LayoutAnimationController(scale);
        lac.setOrder(LayoutAnimationController.ORDER_NORMAL);
        lac.setDelay(0.3f);
        return lac;
    }

    // 获取设备管理器
    private static TelephonyManager getManager(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager;
    }

    /**
     * 测试手机信息
     *
     * @return
     */
    public static String getInfo() {
        String phoneInfo = "Product: " + android.os.Build.PRODUCT;
        phoneInfo += "\n CPU_ABI: " + android.os.Build.CPU_ABI;
        phoneInfo += "\n TAGS: " + android.os.Build.TAGS;
        phoneInfo += "\n VERSION_CODES.BASE: "
                + android.os.Build.VERSION_CODES.BASE;
        phoneInfo += "\n MODEL: " + android.os.Build.MODEL;
        phoneInfo += "\n SDK: " + android.os.Build.VERSION.SDK;
        phoneInfo += "\n VERSION.RELEASE: " + android.os.Build.VERSION.RELEASE;
        phoneInfo += "\n DEVICE: " + android.os.Build.DEVICE;
        phoneInfo += "\n DISPLAY: " + android.os.Build.DISPLAY;
        phoneInfo += "\n BRAND: " + android.os.Build.BRAND;
        phoneInfo += "\n BOARD: " + android.os.Build.BOARD;
        phoneInfo += "\n FINGERPRINT: " + android.os.Build.FINGERPRINT;
        phoneInfo += "\n ID: " + android.os.Build.ID;
        phoneInfo += "\n MANUFACTURER: " + android.os.Build.MANUFACTURER;
        phoneInfo += "\n USER: " + android.os.Build.USER;
        return phoneInfo;
    }

    /***
     * 获取数据值
     * @param context
     * @return
     */
    public static boolean getClientStatus(Context context) {
        SharedPreferences prefer = context.getSharedPreferences("east",
                Context.MODE_PRIVATE);
        boolean bool=prefer.getBoolean("clientStatus",false);
        prefer = null;
        return bool;
    }

    /***
     * 设置“电话支持排障”时，bool为true
     * @param context
     * @param bool
     * @return
     */
    public static boolean putClientStatus(Context context,boolean bool){
        try {
            SharedPreferences prefer = context.getSharedPreferences("east",
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefer.edit();
            editor.putBoolean("clientStatus", bool);
            editor.commit();
            prefer = null;
            editor = null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


}