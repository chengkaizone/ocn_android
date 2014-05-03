package com.hongguaninfo.ocnandroid.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

/**
 * 双缓冲区绘图demo 手写板--用于客户电子签名
 * 
 * @author JianbinZhu
 * 
 */
public class View7 extends View {
	private Paint paint;
	public Canvas cacheCanvas;
	public Bitmap cachebBitmap;
	private Path path;
	private Button button;

	public View7(Context context) {
		super(context);
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStrokeWidth(3);
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.CYAN);
		path = new Path();
		cachebBitmap = Bitmap.createBitmap(480, 820, Config.ARGB_8888);
		cacheCanvas = new Canvas(cachebBitmap);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.GRAY);

		// 绘制上一次的，否则不连贯
		canvas.drawBitmap(cachebBitmap, 0, 0, null);
		canvas.drawPath(path, paint);
	}

	private float cur_x, cur_y;
	private boolean isMoving;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		float x = event.getX();
		float y = event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			cur_x = x;
			cur_y = y;
			path.moveTo(cur_x, cur_y);
			isMoving = true;
			break;
		}

		case MotionEvent.ACTION_MOVE: {
			// 二次曲线方式绘制
			path.quadTo(cur_x, cur_y, x, y);
			// 下面这个方法貌似跟上面一样
			// path.lineTo(x, y);
			cur_x = x;
			cur_y = y;
			break;
		}

		case MotionEvent.ACTION_UP: {
			// 鼠标弹起保存最后状态
			cacheCanvas.drawPath(path, paint);
			path.reset();
			isMoving = false;
			break;
		}
		}

		// 刷新界面
		invalidate();

		return true;
	}
}