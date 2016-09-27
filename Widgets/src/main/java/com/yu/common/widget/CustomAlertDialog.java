package com.yu.common.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.yu.common.R;


//    new CustomAlertDialog(context)
//            .setTitleText("")// 传""或null自动隐藏
//            .setMsgText("haha")// 传""或null自动隐藏
//            .setRightBtn("ok", null) // 传""或null自动隐藏按钮，监听器直接传null
//            .setLeftBtn("cancel", new View.OnClickListener() {// 传""或null自动隐藏按钮
//                @Override
//                public void onClick(View v) {
//            }
//            }).show();

public class CustomAlertDialog {

	private Context context;
	private Dialog dialog;
	private LinearLayout lLayout_bg;
	private TextView txt_title;
	private TextView txt_msg;
	private Button btn_neg;
	private Button btn_pos;
	//	private View hLine;
	private View vLine;
	private int screenWidth;

	public CustomAlertDialog(Activity context) {
		this.context = context;
		screenWidth = context.getResources().getDisplayMetrics().widthPixels;

		build();
	}

	@SuppressLint("InflateParams")
	private CustomAlertDialog build() {
		// 获取Dialog布局
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_custom, null);

		// 获取自定义Dialog布局中的控件
		lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);
		txt_title = (TextView) view.findViewById(R.id.txt_title);
		txt_msg = (TextView) view.findViewById(R.id.txt_msg);
		btn_neg = (Button) view.findViewById(R.id.btn_neg);
		btn_pos = (Button) view.findViewById(R.id.btn_pos);
		//	hLine = view.findViewById(R.id.hLine);
		vLine = view.findViewById(R.id.vLine);
		txt_title.setVisibility(View.GONE);
		txt_msg.setVisibility(View.GONE);

		// 定义Dialog布局和参数
		dialog = new Dialog(context, R.style.AlertDialogStyle);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(true);
		dialog.setContentView(view);

		// 调整dialog背景大小
		lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (screenWidth * 0.75),
				LayoutParams.WRAP_CONTENT));
		return this;
	}

	public CustomAlertDialog setCancelable(boolean cancel) {
		dialog.setCancelable(cancel);
		return this;

	}

	public CustomAlertDialog setCanceledOnTouchOutside(boolean cancel) {
		dialog.setCanceledOnTouchOutside(cancel);
		return this;

	}

	public CustomAlertDialog setRightBtn(String text, final OnClickListener listener) {
		if (TextUtils.isEmpty(text)) {
			btn_pos.setText("确定");
			btn_pos.setVisibility(View.GONE);
			vLine.setVisibility(View.GONE);
		} else {
			btn_pos.setText(text);
			btn_pos.setVisibility(View.VISIBLE);
		}
		btn_pos.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				if (null != listener)
					listener.onClick(v);
			}
		});
		return this;
	}

	public CustomAlertDialog setLeftBtn(String text, final OnClickListener listener) {
		if (TextUtils.isEmpty(text)) {
			btn_neg.setText("取消");
			btn_neg.setVisibility(View.GONE);
			vLine.setVisibility(View.GONE);
		} else {
			btn_neg.setText(text);
			btn_neg.setVisibility(View.VISIBLE);
		}
		btn_neg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				if (null != listener)
					listener.onClick(v);
			}
		});
		return this;
	}

	/**
	 * 设置标题文字
	 *
	 * @param text
	 * @return
	 */
	public CustomAlertDialog setTitleText(String text) {
		if (TextUtils.isEmpty(text)) {
			txt_title.setVisibility(View.GONE);
		} else {
			txt_title.setText(text);
			txt_title.setVisibility(View.VISIBLE);
		}
		return this;
	}

	/**
	 * 设置正文文字
	 *
	 * @param text
	 * @return
	 */
	public CustomAlertDialog setMsgText(String text) {
		if (TextUtils.isEmpty(text)) {
			txt_msg.setVisibility(View.GONE);
		} else {
			txt_msg.setText(text);
			txt_msg.setVisibility(View.VISIBLE);
		}
		return this;
	}

	public void show() {
		dialog.show();
	}
}
