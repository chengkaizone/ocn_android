package com.hongguaninfo.ocnandroid.utils;

import java.io.File;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.app.SearchManager;
import android.app.UiModeManager;
import android.app.WallpaperManager;
import android.app.admin.DevicePolicyManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.DropBoxManager;
import android.os.PowerManager;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.text.ClipboardManager;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

/**
 * 获取手机系统信息的及系统服务类 23个系统服务类--6个系统信息--1个测试信息--1个菜单相关类
 * 
 * @author chengkai
 * 
 */
public class OSService {
	public static final String TAG = "OSService";

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
	 * 获取esn编码
	 * 
	 * @param context
	 * @return
	 */
	public static String getESN(Context context) {
		String esn = getTelephonyManager(context).getDeviceId();
		return esn;
	}

	/**
	 * 获取imsi编码--又名手机串号
	 * 
	 * @param context
	 * @return
	 */
	public static String getIMSI(Context context) {
		String imsi = getTelephonyManager(context).getSubscriberId();
		return imsi;
	}

	/**
	 * 获取本机号码
	 * 
	 * @param context
	 * @return
	 */
	public static String getSIMNumber(Context context) {
		String sim = getTelephonyManager(context).getLine1Number();
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
	 * 获取菜单布局解析器
	 * 
	 * @param context
	 * @return
	 */
	public static MenuInflater getMenuInflater(Context context) {
		return new MenuInflater(context);
	}

	/**
	 * 获取layout布局解析器
	 * 
	 * @param context
	 * @return
	 */
	public static LayoutInflater getLayoutInflater(Context context) {
		return (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	/**
	 * 获取设备管理器
	 * 
	 * @param context
	 * @return
	 */
	public static TelephonyManager getTelephonyManager(Context context) {
		return (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
	}

	/**
	 * 获取输入法管理器
	 * 
	 * @param context
	 * @return
	 */
	public static InputMethodManager getInputMethodManager(Context context) {
		return (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
	}

	/**
	 * 获取剪贴板管理器
	 * 
	 * @param context
	 * @return
	 */
	public static ClipboardManager getClipboardManager(Context context) {
		return (ClipboardManager) context
				.getSystemService(Context.CLIPBOARD_SERVICE);
	}

	/**
	 * 获取音频管理器
	 * 
	 * @param context
	 * @return
	 */
	public static AudioManager getAudioManager(Context context) {
		return (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
	}

	/**
	 * 获取网盘管理器
	 * 
	 * @param context
	 * @return
	 */
	public static DropBoxManager getDropBoxManager(Context context) {
		return (DropBoxManager) context
				.getSystemService(Context.DROPBOX_SERVICE);
	}

	/**
	 * 获取网络连接管理器
	 * 
	 * @param context
	 * @return
	 */
	public static ConnectivityManager getConnectivityManager(Context context) {
		return (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
	}

	/**
	 * 获取设备方针管理器
	 * 
	 * @param context
	 * @return
	 */
	public static DevicePolicyManager getDevicePolicyManager(Context context) {
		return (DevicePolicyManager) context
				.getSystemService(Context.DEVICE_POLICY_SERVICE);
	}

	/**
	 * 获取UI模型管理器---可以控制夜间/行车等模式
	 * @param context
	 * @return
	 */
	public static UiModeManager getUiModeManager(Context context) {
		return (UiModeManager) context
				.getSystemService(Context.UI_MODE_SERVICE);
	}

	/**
	 * 获取通知管理器
	 * 
	 * @param context
	 * @return
	 */
	public static NotificationManager getNotificationManager(Context context) {
		return (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
	}

	/**
	 * 获取窗口管理器
	 * 
	 * @param context
	 * @return
	 */
	public static WindowManager getWindowManager(Context context) {
		return (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
	}

	/**
	 * 获取墙纸服务
	 * 
	 * @param context
	 * @return
	 */
	public static WallpaperManager getWallpaperManager(Context context) {
		return (WallpaperManager) context
				.getSystemService(Context.WALLPAPER_SERVICE);
	}

	/**
	 * 获取键盘管理器
	 * 
	 * @param context
	 * @return
	 */
	public static KeyguardManager getKeyguardManager(Context context) {
		return (KeyguardManager) context
				.getSystemService(Context.KEYGUARD_SERVICE);
	}

	/**
	 * 获取传感器管理服务
	 * 
	 * @param context
	 * @return
	 */
	public static SensorManager getSensorManager(Context context) {
		return (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
	}

	/**
	 * 获取电源管理器
	 * 
	 * @param context
	 * @return
	 */
	public static SearchManager getSearchManager(Context context) {
		return (SearchManager) context.getSystemService(Context.SEARCH_SERVICE);
	}

	/**
	 * 获取电源管理器
	 * 
	 * @param context
	 * @return
	 */
	public static PowerManager getPowerManager(Context context) {
		return (PowerManager) context.getSystemService(Context.POWER_SERVICE);
	}

	/**
	 * 获取位置管理器
	 * 
	 * @param context
	 * @return
	 */
	public static LocationManager getLocationManager(Context context) {
		return (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
	}

	/**
	 * 获取闹钟管理器
	 * 
	 * @param context
	 * @return
	 */
	public static AlarmManager getAlarmManager(Context context) {
		return (AlarmManager) context.getSystemService(Context.AUDIO_SERVICE);
	}

	/**
	 * 获取振动器
	 * 
	 * @param context
	 * @return
	 */
	public static Vibrator getVibrator(Context context) {
		return (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
	}

	/**
	 * 获取活动管理器
	 * 
	 * @param context
	 * @return
	 */
	public static ActivityManager getActivityManager(Context context) {
		return (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
	}

	/**
	 * 获取账户管理器
	 * 
	 * @param context
	 * @return
	 */
	public static AccountManager getAccountManager(Context context) {
		return (AccountManager) context
				.getSystemService(Context.ACCOUNT_SERVICE);
	}

	/**
	 * 获取wifi管理器
	 * 
	 * @param context
	 * @return
	 */
	public static WifiManager getWifiManager(Context context) {
		return (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
	}

	/**
	 * 获取可访问性管理器
	 * 
	 * @param context
	 * @return
	 */
	public static AccessibilityManager getAccessibilityManager(Context context) {
		return (AccessibilityManager) context
				.getSystemService(Context.ACCESSIBILITY_SERVICE);
	}

	/**
	 * 测试手机信息
	 * 
	 * @return
	 */
	public static String getDeviceInfo() {
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
}