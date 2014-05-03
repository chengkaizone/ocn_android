package com.hongguaninfo.ocnandroid.widgets;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Builders {
	private int padding = 16;;
	private int textSize = 16;
	private int textColor=Color.BLACK;
	private Context context;
	private AlertDialog dialog;
	private String content;
	private View view;
	private TextView hintView;
	private Button positive;
	private Button negative;
	private String positiveValue;
	private String negativeValue;
	private OnClickListener positiveButton;
	private OnClickListener negativeButton;

	public Builders(Context context) {
		this.context = context;
	}

	public AlertDialog create() {
		Builder builder = new AlertDialog.Builder(context);
		this.dialog = builder.create();
		if (view == null) {
		} else {
			if (hintView != null) {
				hintView.setPadding(padding, padding, padding, padding);
				hintView.setTextSize(textSize);
				hintView.setTextColor(textColor);
				hintView.setText(content);
			}
			if (positiveButton != null) {
				if (positive != null) {
					positive.setText(positiveValue);
					positive.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							dialog.dismiss();
							positiveButton.onClick(v);
						}
					});
				}
			} else {
				if (positive != null) {
					positive.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							dialog.dismiss();
						}
					});
				}
			}
			if (negativeButton != null) {
				if (negative != null) {
					negative.setText(negativeValue);
					negative.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							dialog.dismiss();
							negativeButton.onClick(v);
						}
					});
				}
			} else {
				if (negative != null) {
					negative.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							dialog.dismiss();
						}
					});
				}
			}
		}
		return dialog;
	}

	public AlertDialog show() {
		dialog = create();
		dialog.show();
		if (view != null) {
			dialog.setContentView(view);
		}
		return dialog;
	}

	public Builders setPositiveButton(String value,
			final OnClickListener positiveButton) {
		this.positiveValue = value;
		this.positiveButton = positiveButton;
		return this;
	}

	public Builders setPositiveButton(int valueId,
			final OnClickListener positiveButton) {
		this.positiveValue = context.getResources().getString(valueId);
		this.positiveButton = positiveButton;
		return this;
	}

	public Builders setNegativeButton(String value,
			final OnClickListener negativeButton) {
		this.negativeValue = value;
		this.negativeButton = negativeButton;
		return this;
	}

	public Builders setNegativeButton(int valueId,
			final OnClickListener negativeButton) {
		this.negativeValue = context.getResources().getString(valueId);
		this.negativeButton = negativeButton;
		return this;
	}
	public Builders setView(int viewId, int textViewId, int positiveId,
			int negativeId) {
		this.view = LayoutInflater.from(context).inflate(viewId, null);
		this.hintView = (TextView) this.view.findViewById(textViewId);
		this.positive = (Button) this.view.findViewById(positiveId);
		this.negative = (Button) this.view.findViewById(negativeId);
		return this;
	}

	public Builders setContent(String content) {
		this.content = content;
		if (hintView != null) {
			hintView.setText(content);
		}
		return this;
	}
}
