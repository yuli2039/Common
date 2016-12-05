package com.yu.retrofittest.base;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Window;
import android.widget.Toast;

public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements IView {

    protected P mPresenter;
    private AlertDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        mPresenter = createPresenter();
        if (null != mPresenter)
            mPresenter.onCreate();
//        ButterKnife.bind(this);
        afterInitview();
    }

    protected abstract int getLayoutId();

    protected abstract P createPresenter();

    protected abstract void afterInitview();

    @Override
    protected void onStart() {
        super.onStart();
        if (null != mPresenter)
            mPresenter.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != mPresenter)
            mPresenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != mPresenter)
            mPresenter.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (null != mPresenter)
            mPresenter.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mPresenter)
            mPresenter.onDestory();
    }

    @Override
    public void toast(String msg) {
        if (!TextUtils.isEmpty(msg))
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() {
        if (loadingDialog == null)
            loadingDialog = new ProgressDialog.Builder(this).create();
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
