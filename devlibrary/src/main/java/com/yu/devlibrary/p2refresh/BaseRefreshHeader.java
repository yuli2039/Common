package com.yu.devlibrary.p2refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * @author yu
 *         Create on 16/7/13.
 */
public abstract class BaseRefreshHeader extends LinearLayout {

    public static final int STATE_NORMAL = 0;
    public static final int STATE_RELEASE_TO_REFRESH = 1;
    public static final int STATE_REFRESHING = 2;
    public static final int STATE_DONE = 3;

    public BaseRefreshHeader(Context context) {
        super(context);
    }

    public BaseRefreshHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 下拉时回调
     *
     * @param delta 偏移量
     */
    public abstract void onMove(float delta);

    /**
     * 在此方法中判断高度和当前状态来切换刷新头的状态
     *
     * @return 通过返回值判断是否需要刷新
     */
    public abstract boolean releaseAction();

    /**
     * 刷新完成
     */
    public abstract void refreshComplete();

    /**
     * @return 可见高度
     */
    public abstract int getVisibleHeight();

    /**
     * @return 现在的刷新头状态
     */
    public abstract int getState();

    /**
     * @param state 设置刷新头状态
     */
    public abstract void setState(int state);
}
