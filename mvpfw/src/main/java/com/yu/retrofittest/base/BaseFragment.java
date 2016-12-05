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
public abstract class BaseFragment<P extends BasePresenter> extends Fragment implements IView {

    private AlertDialog loadingDialog;
    protected P mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
        if (null != mPresenter)
            mPresenter.onCreate();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
//        ButterKnife.bind(this, view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    public abstract P createPresenter();

    @Override
    public void onStart() {
        super.onStart();
        if (null != mPresenter)
            mPresenter.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != mPresenter)
            mPresenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (null != mPresenter)
            mPresenter.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (null != mPresenter)
            mPresenter.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mPresenter)
            mPresenter.onDestory();
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
