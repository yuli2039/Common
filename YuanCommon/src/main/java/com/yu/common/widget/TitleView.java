package com.yu.common.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yu.common.R;


/**
 * 标题栏
 *
 * @author yu
 */
public class TitleView extends RelativeLayout {

    private View view;
    private ImageView btnLeft;
    private ImageView btnRightIv;
    private Button btnRight;
    private TextView tvTitle;

    public TitleView(Context context) {
        super(context);
        init(context);
    }

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @SuppressLint("InflateParams")
    private void init(Context context) {
        view = View.inflate(context, R.layout.title_view, this);
        btnLeft = (ImageView) view.findViewById(R.id.btnLeft);
        btnRightIv = (ImageView) view.findViewById(R.id.btnRightIv);
        btnRight = (Button) view.findViewById(R.id.btnRight);
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
    }

    /**
     * 传null则隐藏标题
     */
    public TitleView setTitle(String title) {
        if (TextUtils.isEmpty(title)) {
            tvTitle.setVisibility(View.GONE);
        } else {
            tvTitle.setText(title);
        }
        return this;
    }

	/*
     * 设置左侧返回键
	 */

    public TitleView setLeftBtnVisibility(boolean visiable) {
        btnLeft.setVisibility(visiable ? View.VISIBLE : View.GONE);
        return this;
    }

    public TitleView setLeftBtnListener(OnClickListener listener) {
        btnLeft.setOnClickListener(listener);
        return this;
    }

	/*
     * 设置右侧文字按钮
	 */

    public TitleView setRightBtnText(String text) {
        btnRight.setText(text);
        return this;
    }

    public TitleView setRightBtnVisibility(boolean visiable) {
        btnRight.setVisibility(visiable ? View.VISIBLE : View.GONE);
        return this;
    }

    public TitleView setRightBtn(String txt, OnClickListener listener) {
        if (TextUtils.isEmpty(txt)) {
            btnRight.setVisibility(View.GONE);
        } else {
            btnRight.setText(txt);
            btnRight.setOnClickListener(listener);
        }
        return this;
    }

	/*
     * 设置右侧图形按钮
	 */

    public TitleView setRightIvBtnVisibility(boolean visiable) {
        btnRightIv.setVisibility(visiable ? View.VISIBLE : View.GONE);
        return this;
    }

    public TitleView setRightIvBtnListener(OnClickListener listener) {
        btnRightIv.setOnClickListener(listener);
        return this;
    }

    public View getView() {
        return view;
    }

}
