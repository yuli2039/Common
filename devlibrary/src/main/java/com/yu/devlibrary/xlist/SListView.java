package com.yu.devlibrary.xlist;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * 支持加载更多的listview,默认不开启
 * 加载更多的footer和普通的footer不兼容，只能存在一种
 *
 * @author yu
 *         Create on 16/8/28.
 */
public class SListView extends ListView implements AbsListView.OnScrollListener {

    private LoadMoreListener listener;
    private boolean loadMoreEnable = false;// 默认不开启加载更多
    private LoadMoreFooter loadMoreFooter;
    private boolean isNoMore = false;

    public SListView(Context context) {
        super(context);
    }

    public SListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public interface LoadMoreListener {
        void onLoadMore();
    }

    @Override
    public void addFooterView(View v) {
        if (loadMoreEnable)
            throw new IllegalStateException("load more footer is enable,can not add custom footer view");
        super.addFooterView(v);
    }

    public void setLoadMoreListener(LoadMoreListener listener) {
        if (getFooterViewsCount() > 0)
            throw new IllegalStateException("already have custom footer view, can not support load more");

        this.listener = listener;
        loadMoreEnable = true;
        loadMoreFooter = new LoadMoreFooter(getContext());
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp2px(50));
        loadMoreFooter.setLayoutParams(layoutParams);
        super.addFooterView(loadMoreFooter);
        setOnScrollListener(this);
    }

    public void loadMoreComplete() {
        if (loadMoreFooter != null)
            loadMoreFooter.setState(LoadMoreFooter.STATE_COMPLETE);
    }

    public void setNoMore(boolean noMore) {
        this.isNoMore = noMore;
        if (loadMoreFooter != null)
            loadMoreFooter.setState(noMore ? LoadMoreFooter.STATE_NOMORE : LoadMoreFooter.STATE_COMPLETE);
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        switch (scrollState) {
            case OnScrollListener.SCROLL_STATE_IDLE:
                if (absListView.getLastVisiblePosition() == (absListView.getCount() - 1)
                        && loadMoreEnable
                        && !isNoMore
                        && listener != null) {

                    loadMoreFooter.setState(LoadMoreFooter.STATE_LOADING);
                    listener.onLoadMore();
                }
                break;
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {

    }

    public static int dp2px(float dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

}
