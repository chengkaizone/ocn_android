package com.hongguaninfo.ocnandroid.beans;

/**
 * 版本信息
 * 
 * @author Administrator
 * 
 */
public class EastVersionInfo implements java.io.Serializable {
	// 版本名
	private String versionName;
	// 版本号
	private String versionCode;
	// 应用名称
	private String appName;
	// apk名称
	private String apkName;

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getApkName() {
		return apkName;
	}

	public void setApkName(String apkName) {
		this.apkName = apkName;
	}

}
