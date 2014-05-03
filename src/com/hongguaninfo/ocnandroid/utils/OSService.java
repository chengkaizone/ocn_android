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
 * ��ȡ�ֻ�ϵͳ��Ϣ�ļ�ϵͳ������ 23��ϵͳ������--6��ϵͳ��Ϣ--1��������Ϣ--1���˵������
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
	 * ��ȡapk�汾��
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
	 * ��ȡesn����
	 * 
	 * @param context
	 * @return
	 */
	public static String getESN(Context context) {
		String esn = getTelephonyManager(context).getDeviceId();
		return esn;
	}

	/**
	 * ��ȡimsi����--�����ֻ�����
	 * 
	 * @param context
	 * @return
	 */
	public static String getIMSI(Context context) {
		String imsi = getTelephonyManager(context).getSubscriberId();
		return imsi;
	}

	/**
	 * ��ȡ��������
	 * 
	 * @param context
	 * @return
	 */
	public static String getSIMNumber(Context context) {
		String sim = getTelephonyManager(context).getLine1Number();
		return sim;
	}

	/**
	 * ��ȡ�ֻ����ͱ���
	 * 
	 * @return
	 */
	public static String getTermcode() {
		String termcode = android.os.Build.MODEL;
		return termcode;
	}

	/**
	 * ��ȡ�˵����ֽ�����
	 * 
	 * @param context
	 * @return
	 */
	public static MenuInflater getMenuInflater(Context context) {
		return new MenuInflater(context);
	}

	/**
	 * ��ȡlayout���ֽ�����
	 * 
	 * @param context
	 * @return
	 */
	public static LayoutInflater getLayoutInflater(Context context) {
		return (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	/**
	 * ��ȡ�豸������
	 * 
	 * @param context
	 * @return
	 */
	public static TelephonyManager getTelephonyManager(Context context) {
		return (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
	}

	/**
	 * ��ȡ���뷨������
	 * 
	 * @param context
	 * @return
	 */
	public static InputMethodManager getInputMethodManager(Context context) {
		return (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
	}

	/**
	 * ��ȡ�����������
	 * 
	 * @param context
	 * @return
	 */
	public static ClipboardManager getClipboardManager(Context context) {
		return (ClipboardManager) context
				.getSystemService(Context.CLIPBOARD_SERVICE);
	}

	/**
	 * ��ȡ��Ƶ������
	 * 
	 * @param context
	 * @return
	 */
	public static AudioManager getAudioManager(Context context) {
		return (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
	}

	/**
	 * ��ȡ���̹�����
	 * 
	 * @param context
	 * @return
	 */
	public static DropBoxManager getDropBoxManager(Context context) {
		return (DropBoxManager) context
				.getSystemService(Context.DROPBOX_SERVICE);
	}

	/**
	 * ��ȡ�������ӹ�����
	 * 
	 * @param context
	 * @return
	 */
	public static ConnectivityManager getConnectivityManager(Context context) {
		return (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
	}

	/**
	 * ��ȡ�豸���������
	 * 
	 * @param context
	 * @return
	 */
	public static DevicePolicyManager getDevicePolicyManager(Context context) {
		return (DevicePolicyManager) context
				.getSystemService(Context.DEVICE_POLICY_SERVICE);
	}

	/**
	 * ��ȡUIģ�͹�����---���Կ���ҹ��/�г���ģʽ
	 * @param context
	 * @return
	 */
	public static UiModeManager getUiModeManager(Context context) {
		return (UiModeManager) context
				.getSystemService(Context.UI_MODE_SERVICE);
	}

	/**
	 * ��ȡ֪ͨ������
	 * 
	 * @param context
	 * @return
	 */
	public static NotificationManager getNotificationManager(Context context) {
		return (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
	}

	/**
	 * ��ȡ���ڹ�����
	 * 
	 * @param context
	 * @return
	 */
	public static WindowManager getWindowManager(Context context) {
		return (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
	}

	/**
	 * ��ȡǽֽ����
	 * 
	 * @param context
	 * @return
	 */
	public static WallpaperManager getWallpaperManager(Context context) {
		return (WallpaperManager) context
				.getSystemService(Context.WALLPAPER_SERVICE);
	}

	/**
	 * ��ȡ���̹�����
	 * 
	 * @param context
	 * @return
	 */
	public static KeyguardManager getKeyguardManager(Context context) {
		return (KeyguardManager) context
				.getSystemService(Context.KEYGUARD_SERVICE);
	}

	/**
	 * ��ȡ�������������
	 * 
	 * @param context
	 * @return
	 */
	public static SensorManager getSensorManager(Context context) {
		return (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
	}

	/**
	 * ��ȡ��Դ������
	 * 
	 * @param context
	 * @return
	 */
	public static SearchManager getSearchManager(Context context) {
		return (SearchManager) context.getSystemService(Context.SEARCH_SERVICE);
	}

	/**
	 * ��ȡ��Դ������
	 * 
	 * @param context
	 * @return
	 */
	public static PowerManager getPowerManager(Context context) {
		return (PowerManager) context.getSystemService(Context.POWER_SERVICE);
	}

	/**
	 * ��ȡλ�ù�����
	 * 
	 * @param context
	 * @return
	 */
	public static LocationManager getLocationManager(Context context) {
		return (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
	}

	/**
	 * ��ȡ���ӹ�����
	 * 
	 * @param context
	 * @return
	 */
	public static AlarmManager getAlarmManager(Context context) {
		return (AlarmManager) context.getSystemService(Context.AUDIO_SERVICE);
	}

	/**
	 * ��ȡ����
	 * 
	 * @param context
	 * @return
	 */
	public static Vibrator getVibrator(Context context) {
		return (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
	}

	/**
	 * ��ȡ�������
	 * 
	 * @param context
	 * @return
	 */
	public static ActivityManager getActivityManager(Context context) {
		return (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
	}

	/**
	 * ��ȡ�˻�������
	 * 
	 * @param context
	 * @return
	 */
	public static AccountManager getAccountManager(Context context) {
		return (AccountManager) context
				.getSystemService(Context.ACCOUNT_SERVICE);
	}

	/**
	 * ��ȡwifi������
	 * 
	 * @param context
	 * @return
	 */
	public static WifiManager getWifiManager(Context context) {
		return (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
	}

	/**
	 * ��ȡ�ɷ����Թ�����
	 * 
	 * @param context
	 * @return
	 */
	public static AccessibilityManager getAccessibilityManager(Context context) {
		return (AccessibilityManager) context
				.getSystemService(Context.ACCESSIBILITY_SERVICE);
	}

	/**
	 * �����ֻ���Ϣ
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