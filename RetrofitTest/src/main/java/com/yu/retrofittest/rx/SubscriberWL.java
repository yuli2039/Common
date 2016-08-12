package com.yu.retrofittest.rx;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import java.io.IOException;
import java.net.SocketTimeoutException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * 订阅时自动显示progressbar的订阅者，必须在主线程订阅
 * 对请求进行统一的错误处理
 * @author yu
 *         Create on 16/7/26.
 */
public abstract class SubscriberWL<T> extends Subscriber<T> {

    Dialog dialog;

    public SubscriberWL(Context context, boolean cancelable) {
        dialog = new ProgressDialog(context);// init dialog
        if (cancelable)// 取消订阅
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    if (!isUnsubscribed())
                        unsubscribe();
                }
            });
    }

    @Override
    public void onStart() {
        Log.e("SubscriberWL", "--onStart--");
        dialog.show();
    }

    @Override
    public void onCompleted() {
        Log.e("SubscriberWL", "--onCompleted--");
        if (dialog.isShowing())
            dialog.dismiss();
    }

    @Override
    public final void onError(Throwable e) {
        Log.e("SubscriberWL", "--onError--" + e.toString());
        if (dialog.isShowing())
            dialog.dismiss();

        if (e instanceof SocketTimeoutException) {
            Log.e("SubscriberWL", "连接超时");
        } else if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            Log.e("SubscriberWL", "httpException : code = " + httpException.code());
            Log.e("SubscriberWL", "httpException : message = " + httpException.getMessage());
            if (httpException.response() != null && httpException.response().errorBody() != null) {
                try {
                    onError(httpException.code(), httpException.response().errorBody().string());
                    return;
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            onError(httpException.code(), httpException.getMessage());
        }
    }

    protected void onError(int errorCode, String errorBody) {
    }

}
