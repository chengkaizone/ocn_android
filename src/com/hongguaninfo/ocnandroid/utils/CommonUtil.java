package com.hongguaninfo.ocnandroid.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ListView;

public class CommonUtil {
	/**
	 * 链接网络以获得json文件数据
	 * 
	 * @param url
	 * @return
	 */
	public static String getJson(String url) {
//		System.out.println("获取数据文件--->");
		try {
			InputStream input = CommonUtil.getInputStream(url);
			if (input != null) {
				InputStreamReader isr = new InputStreamReader(input, "utf-8");
				BufferedReader reader = new BufferedReader(isr);
				String tmp = "";
				StringBuilder result = new StringBuilder();
				while ((tmp = reader.readLine()) != null) {
					result.append(tmp);
				}
				String temp = result.toString();
				temp = "[" + temp + "]";
//				System.out.println(temp);
				return temp;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
//			System.out.println("网络异常！");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取链接的输入流
	 * 
	 * @param str
	 * @return
	 */
	public static InputStream getInputStream(String str) {
		HttpClient client = new DefaultHttpClient();
		try {
			HttpGet get = new HttpGet(str);
			HttpResponse response = client.execute(get);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				return entity.getContent();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取位图
	 * 
	 * @param str
	 * @return
	 */
	public static Bitmap getBitmap(String str) {
		try {
			InputStream input = getInputStream(str);
			if (input != null) {
				Bitmap bitmap = BitmapFactory.decodeStream(input);
				return bitmap;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void setAnim(Context context, ListView... list) {
		for (ListView e : list) {
			e.setLayoutAnimation(OSUtil.getLac(context));
		}
	}
}
