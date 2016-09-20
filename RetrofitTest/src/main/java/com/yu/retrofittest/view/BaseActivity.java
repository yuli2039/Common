package com.yu.retrofittest.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.yu.retrofittest.presenter.BasePresenter;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * baseview的默认实现
 */
public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity
        implements BaseView {

    private CompositeSubscription mCompositeSubscription;
    protected P mPresenter;
    private AlertDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
        setContentView(getLayoutId());

//        ButterKnife.bind(this);
        init();
    }

    protected abstract int getLayoutId();

    protected abstract P createPresenter();

    protected abstract void init();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null)// 解绑view时，取消rx订阅
            mPresenter.detachView();
        onUnsubscribe();
    }

    @Override
    public void toast(String msg) {
//        CenterToast.show(this, msg);

    }

    @Override
    public void showLoading() {
        if (loadingDialog == null) {
            loadingDialog = new ProgressDialog.Builder(this).create();
        }
        loadingDialog.show();
    }

    @Override
    public void dismissLoading() {
        if (loadingDialog != null && loadingDialog.isShowing())
            loadingDialog.dismiss();
    }


    /**
     * 所有rx订阅后，需要调用此方法，用于在detachView时取消订阅
     */
    protected void addSubscription(Subscription subscribe) {
        if (mCompositeSubscription == null)
            mCompositeSubscription = new CompositeSubscription();
        mCompositeSubscription.add(subscribe);
    }


    /**
     * 取消本页面所有订阅
     */
    protected void onUnsubscribe() {
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
        }
    }

}
