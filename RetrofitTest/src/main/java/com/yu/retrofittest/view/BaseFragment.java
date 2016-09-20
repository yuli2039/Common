package com.yu.retrofittest.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.yu.retrofittest.presenter.BasePresenter;



/**
 * baseview的默认实现
 */
public abstract class BaseFragment<P extends BasePresenter> extends Fragment
        implements BaseView {

    private AlertDialog loadingDialog;
    protected P mPresenter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
//        ButterKnife.bind(this, view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter = createPresenter();
        fetchData();
    }

    protected abstract P createPresenter();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null)
            mPresenter.detachView();
    }

    /**
     * 在此请求网络数据
     */
    public abstract void fetchData();

    @Override
    public void toast(String msg) {
//        CenterToast.show(this.getActivity(), msg);
    }

    @Override
    public void showLoading() {
        if (loadingDialog == null) {
            loadingDialog = new ProgressDialog.Builder(getActivity()).create();
        }
        loadingDialog.show();
    }

    @Override
    public void dismissLoading() {
        if (loadingDialog != null && loadingDialog.isShowing())
            loadingDialog.dismiss();
    }
}
