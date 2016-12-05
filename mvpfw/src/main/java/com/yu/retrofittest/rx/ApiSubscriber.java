package com.yu.retrofittest.rx;

import android.net.ParseException;
import android.support.annotation.NonNull;

import com.google.gson.JsonParseException;
import com.yu.retrofittest.base.IView;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * 订阅时自动显示progressbar的订阅者，必须在主线程订阅
 * 对请求进行统一的错误处理
 *
 * @author yu
 *         Create on 16/7/26.
 */
public abstract class ApiSubscriber<T> extends Subscriber<T> {

    private IView mView;
    private boolean showLoading;
    private boolean showToast;

    public ApiSubscriber(@NonNull IView mBaseView) {
        this(mBaseView, true, true);
    }

    public ApiSubscriber(@NonNull IView view, boolean showLoading, boolean showToast) {
        this.mView = view;
        this.showLoading = showLoading;
        this.showToast = showToast;
    }

    @Override
    public void onStart() {
        if (showLoading) mView.showLoading();
    }

    @Override
    public void onCompleted() {
        if (showLoading) mView.hideLoading();
    }

    /**
     * 只要链式调用中抛出了异常都会走这个回调
     */
    public void onError(Throwable e) {
        if (showLoading)
            mView.hideLoading();

        if (e instanceof ApiException) {
            toast(e.getMessage());

            // TODO:处理特定异常，如token过期等
        } else if (e instanceof ConnectException || e instanceof SocketTimeoutException) {// 超时
            toast("网络不畅，请稍后再试！");
        } else if (e instanceof HttpException) {// server 异常
            toast("服务器异常，请稍后再试！");
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            toast("数据解析异常");
        } else {
            toast("服务器繁忙，请稍后再试！");
        }

        mView.onError();

        e.printStackTrace();
    }

    private void toast(String s) {
        if (showToast)
            mView.toast(s);
    }
}