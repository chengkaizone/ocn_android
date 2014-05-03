package com.hongguaninfo.ocnandroid.utils;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TableLayout.LayoutParams;

import com.hongguaninfo.ocnandroid.main.R;

/**
 * 组件工厂类
 * 
 * @author Administrator
 * 
 */
public class ViewFactory {
	/**
	 * 通过资源id获取位图
	 * 
	 * @param context
	 * @param resid
	 * @return
	 */
	public static Bitmap getBitmap(Context context, int resid) {
		return BitmapFactory.decodeResource(context.getResources(), resid);
	}

	public static TextView createText(Context context, int id, String str) {
		TextView text = (TextView) LayoutInflater.from(context).inflate(
				R.layout.title_text, null);
		text.setId(id);
		text.setText(str);
		return text;
	}

	public static TextView createText(Context context, String str) {
		TextView text = new TextView(context);
		text.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		text.setTextColor(Color.BLACK);
		text.setText(str);
		return text;
	}

	public static EditText createEdit(Context context) {
		EditText et = new EditText(context);
		et.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		et.setBackgroundResource(R.drawable.east_edit_login);
		et.setMinLines(2);
		et.setPadding(10, 5, 10, 5);
		et.setGravity(Gravity.TOP | Gravity.LEFT);
		return et;
	}

	/**
	 * 创建对话框对象
	 * 
	 * @param context
	 * @param view
	 * @param msg
	 * @return
	 */
	public static Builder createDialog(Context context, View view, String msg) {
		AlertDialog.Builder b = new Builder(context);
		b.setMessage(msg);
		b.setView(view);
		b.create();
		return b;
	}

	/**
	 * 获取ListView缩放动画控制器
	 * 
	 * @param context
	 * @return
	 */
	public static LayoutAnimationController getLac(Context context) {
		AnimationSet set = new AnimationSet(true);
		ScaleAnimation scale = new ScaleAnimation(0.1f, 1.0f, 0.1f, 1.0f);
		set.addAnimation(scale);
		scale.setDuration(500);
		LayoutAnimationController lac = new LayoutAnimationController(set);
		lac.setOrder(LayoutAnimationController.ORDER_NORMAL);
		lac.setDelay(0.2f);
		return lac;
	}

	/**
	 * 获取ListView缩放和透明度的动画控制器
	 * 
	 * @param context
	 * @return
	 */
	public static LayoutAnimationController getScaleAlpha(Context context) {
		AnimationSet set = new AnimationSet(true);
		ScaleAnimation scale = new ScaleAnimation(0.1f, 1.0f, 0.1f, 1.0f);
		AlphaAnimation alpha = new AlphaAnimation(0.1f, 1.0f);
		set.addAnimation(scale);
		set.addAnimation(alpha);
		scale.setDuration(500);
		alpha.setDuration(500);
		LayoutAnimationController lac = new LayoutAnimationController(set);
		lac.setOrder(LayoutAnimationController.ORDER_NORMAL);
		lac.setDelay(0.2f);
		return lac;
	}
}
