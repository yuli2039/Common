package com.yu.devlibrary.slist;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yu.devlibrary.R;

/**
 * 默认加载更多的脚布局
 *
 * @author yu
 */
public class LoadMoreFooter extends FrameLayout {

    public final static int STATE_LOADING = 0;
    public final static int STATE_COMPLETE = 1;
    public final static int STATE_NOMORE = 2;

    private TextView mText;
    private ProgressBar mProgressBar;
    private int mState;
    private View view;

    public LoadMoreFooter(Context context) {
        super(context);
        initView(context);
    }

    public LoadMoreFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public void initView(Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.load_more_footer, this);
        mProgressBar = (ProgressBar) view.findViewById(R.id.pb);
        mText = (TextView) findViewById(R.id.tv);
        setState(STATE_COMPLETE);
    }

    public int getState() {
        return mState;
    }

    public void setState(int state) {
        switch (state) {
            case STATE_LOADING:
                view.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.VISIBLE);
                mText.setText(R.string.lmf_loading);
                mState = STATE_LOADING;
                break;
            case STATE_COMPLETE:
                view.setVisibility(View.GONE);
                mState = STATE_COMPLETE;
                break;
            case STATE_NOMORE:
                view.setVisibility(View.VISIBLE);
                mText.setText(R.string.lmf_no_more);
                mProgressBar.setVisibility(View.GONE);
                mState = STATE_NOMORE;
                break;
        }
    }
}
