package com.yu.common.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * 可以处理和viewpager嵌套使用的预加载问题</p>
 * 预加载只加载view，在用户可见时才请求数据；
 */
public abstract class BaseFragment extends Fragment {

    protected boolean isViewInitiated;// view初始化完毕
    protected boolean isVisibleToUser;// 对用户可见？
    protected boolean isDataInitiated;// 是否加载过数据

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        ButterKnife.bind(this, view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        ButterKnife.unbind(this);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isViewInitiated = true;
        prepareFetchData(false);
    }

    /**
     * @param isVisibleToUser 此页面对用户是否可见
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        prepareFetchData(false);
    }

    /**
     * 在此请求网络数据
     */
    public abstract void fetchData();

    /**
     * 判断view加载完毕，页面可见，并且没有加载数据,才调用请求数据的方法
     *
     * @param forceUpdate 是否强制刷新
     */
    public boolean prepareFetchData(boolean forceUpdate) {
        if (isVisibleToUser && isViewInitiated && (!isDataInitiated || forceUpdate)) {
            isDataInitiated = true;
            fetchData();
            return true;
        }
        return false;
    }


}
