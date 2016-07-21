package com.yu.devlibrary.p2refresh;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

/**
 *
 * @author yu
 *         Create on 16/7/9.
 */
public class P2RefreshLayout extends LinearLayout {

    private static final float DRAG_RATE = 3;

    private View mContentView;

    /**
     * 最后一次触摸事件的y轴坐标
     */
    protected float mLastY = 0;

    protected OnRefreshListener mOnRefreshListener;
    private BaseRefreshHeader mRefreshHeader;


    public P2RefreshLayout(Context context) {
        this(context, null);
    }

    public P2RefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public P2RefreshLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
        mRefreshHeader = new ArrowRefreshHeader(context);
        addView(mRefreshHeader, 0);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mContentView = getChildAt(1);
    }

    /**
     * 判断子view是否滚动到了最顶部
     */
    protected boolean isTop() {
        if (mContentView instanceof RecyclerView) {
            RecyclerView.LayoutManager layoutManager = ((RecyclerView) mContentView).getLayoutManager();
            int firstVisibleItemPosition;
            if (layoutManager instanceof GridLayoutManager) {
                firstVisibleItemPosition = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                ((StaggeredGridLayoutManager) layoutManager).findFirstVisibleItemPositions(into);

                firstVisibleItemPosition = into[0];
                for (int value : into) {
                    if (value > firstVisibleItemPosition)
                        firstVisibleItemPosition = value;
                }
            } else {
                firstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
            }

            return firstVisibleItemPosition == 0 && layoutManager.getChildAt(0).getTop() >= getTop();

        } else if (mContentView instanceof AbsListView) {
            AbsListView lv = (AbsListView) mContentView;
            return lv.getFirstVisiblePosition() == 0 && lv.getChildAt(0).getTop() >= getTop();
        } else if (mContentView instanceof ScrollView) {
            return mContentView.getScrollY() <= 0;
        }

        return true;
    }

    /**
     * 在适当的时候拦截触摸事件，这里指的适当的时候是当mContentView滑动到顶部，并且是下拉时拦截触摸事件，否则不拦截，交给其child
     * view 来处理。
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP)
            return false;

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float mYOffset = ev.getRawY() - mLastY;
                // 如果拉到了顶部, 并且是下拉,则拦截触摸事件,从而转到onTouchEvent来处理下拉刷新事件
                if (isTop() && mYOffset > 0)
                    return true;
                break;
        }
        return false;
    }

    /**
     * 在这里处理触摸事件以达到下拉刷新
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                final float deltaY = event.getRawY() - mLastY;
                mLastY = event.getRawY();
                mRefreshHeader.onMove(deltaY / DRAG_RATE);
                if (mRefreshHeader.getVisibleHeight() > 0 && mRefreshHeader.getState() < ArrowRefreshHeader.STATE_REFRESHING) {
                    return false;
                }
                break;
            default:
                mLastY = -1; // reset
                if (mRefreshHeader.releaseAction()) {
                    if (mOnRefreshListener != null) {
                        mOnRefreshListener.onRefresh();
                    }
                }
                break;
        }
        return true;
    }

    /**
     * 设置正在刷新，显示刷新头，会自动触发刷新的回调
     * 比如一进入界面，就直接刷新
     */
    public void setRefreshing(boolean refreshing) {
        if (refreshing && mOnRefreshListener != null) {
            mRefreshHeader.setState(BaseRefreshHeader.STATE_REFRESHING);
            mRefreshHeader.onMove(mRefreshHeader.getMeasuredHeight());
            mOnRefreshListener.onRefresh();
        }
    }

    /**
     * 刷新结束，恢复状态
     */
    public void refreshComplete() {
        mRefreshHeader.refreshComplete();
    }

    /**
     * 设置下拉刷新监听器
     */
    public void setOnRefreshListener(OnRefreshListener listener) {
        mOnRefreshListener = listener;
    }

}
