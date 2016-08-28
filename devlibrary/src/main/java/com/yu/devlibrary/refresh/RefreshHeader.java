package com.yu.devlibrary.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yu.devlibrary.R;


/**
 * 下拉刷新头
 *
 * @author yu
 */
public class RefreshHeader extends FrameLayout implements IRefreshStatus {
    private static final int ANIMATION_DURATION = 180;

    private Animation mRotateAnimation;
    private Animation mResetRotateAnimation;
    private View ptrArrow;
    private ProgressBar ptrPb;
    private TextView ptrTv;

    public RefreshHeader(Context context) {
        this(context, null);
    }

    public RefreshHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        initAnimation();
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.ptr_refresh_header, this);
        ptrArrow = view.findViewById(R.id.ptr_arrow);
        ptrPb = (ProgressBar) view.findViewById(R.id.ptr_progressbar);
        ptrTv = (TextView) view.findViewById(R.id.ptr_tv);
    }

    private void initAnimation() {
        mRotateAnimation = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateAnimation.setDuration(ANIMATION_DURATION);
        mRotateAnimation.setFillAfter(true);

        mResetRotateAnimation = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        mResetRotateAnimation.setDuration(ANIMATION_DURATION);
        mResetRotateAnimation.setFillAfter(true);
    }

    @Override
    public void reset() {
        ptrArrow.clearAnimation();
        ptrTv.setText(R.string.ptr_normal);
        ptrPb.setVisibility(View.GONE);
        ptrArrow.setVisibility(View.VISIBLE);
    }

    @Override
    public void refreshing() {
        ptrArrow.clearAnimation();
        ptrTv.setText(R.string.ptr_refreshing);
        ptrPb.setVisibility(View.VISIBLE);
        ptrArrow.setVisibility(View.GONE);
    }

    @Override
    public void pullToRefresh() {
        ptrArrow.clearAnimation();
        ptrArrow.startAnimation(mRotateAnimation);
        ptrTv.setText(R.string.ptr_release_refresh);
    }

    @Override
    public void releaseToRefresh() {
        ptrArrow.clearAnimation();
        ptrArrow.startAnimation(mResetRotateAnimation);
        ptrTv.setText(R.string.ptr_normal);
    }

    @Override
    public void pullProgress(float pullDistance, float pullProgress) {

    }
}
