package com.yu.devlibrary.refresh;

/**
 * {@link XRefreshLayout#mRefreshView} all the custom drop-down refresh view need to
 * implements the interface
 */
public interface IRefreshStatus {
    /**
     * 重置状态
     * When the content view has reached top and refresh has been completed, view will be reset.
     */
    void reset();

    /**
     * 显示progressbar
     * Refresh View is refreshing
     */
    void refreshing();

    /**
     * 到达刷新点，旋转箭头
     * Refresh View is dropped down to the refresh point
     */
    void pullToRefresh();

    /**
     * 到刷新点，然后往回，旋转箭头到初始状态，不触发刷新
     * Refresh View is released into the refresh point
     */
    void releaseToRefresh();

    /**
     * 下拉的距离和比例
     *
     * @param pullDistance The drop-down distance of the refresh View
     * @param pullProgress The drop-down progress of the refresh View and the pullProgress may be more than 1.0f
     */
    void pullProgress(float pullDistance, float pullProgress);
}
