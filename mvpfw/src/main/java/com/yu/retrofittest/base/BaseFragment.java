package com.yu.retrofittest.base;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;


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
        initData();
    }

    protected abstract P createPresenter();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != mPresenter)
            mPresenter.detachView();
    }

    public abstract void initData();

    @Override
    public void toast(String msg) {
        Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() {
        if (loadingDialog == null)
            loadingDialog = new ProgressDialog.Builder(getActivity()).create();
        loadingDialog.show();
    }

    @Override
    public void hideLoading() {
        if (loadingDialog != null && loadingDialog.isShowing()) loadingDialog.dismiss();
    }

    @Override
    public void onError() {
        
    }
}
