package com.yu.retrofittest.rx;

import android.net.ParseException;
import android.support.annotation.NonNull;

import com.google.gson.JsonParseException;
import com.yu.retrofittest.view.BaseView;

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

    private BaseView loading;
    private boolean showLoading;

    public ApiSubscriber(@NonNull BaseView loading) {
        this(loading, true);
    }

    /**
     * @param loading     baseview
     * @param showLoading 是否显示loading
     */
    public ApiSubscriber(@NonNull BaseView loading, boolean showLoading) {
        this.loading = loading;
        this.showLoading = showLoading;
    }

    @Override
    public void onStart() {
        if (showLoading) loading.showLoading();
    }

    @Override
    public void onCompleted() {
        if (showLoading) loading.dismissLoading();
    }

    /**
     * 只要链式调用中抛出了异常都会走这个回调
     */
    public void onError(Throwable e) {
        if (showLoading) loading.dismissLoading();

        if (e instanceof ApiException) {
            loading.toast(e.getMessage());
            if (((ApiException) e).code == 100) {// token 失效
                // TODO: 2016/9/19 0019  
            }
        } else if (e instanceof ConnectException
                || e instanceof SocketTimeoutException) {
            loading.toast("网络异常，请检查您的网络");
        } else if (e instanceof HttpException) {
            loading.toast("网络不畅，请稍后再试！");
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            loading.toast("数据解析异常");
        } else {
            loading.toast("服务端异常");
        }
        e.printStackTrace();

    }
}