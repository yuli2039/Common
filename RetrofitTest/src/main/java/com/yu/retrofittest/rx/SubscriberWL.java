package com.yu.retrofittest.rx;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import rx.Subscriber;

/**
 * 订阅时自动显示progressbar的订阅者，必须在主线程订阅
 *
 * @author yu
 *         Create on 16/7/26.
 */
public abstract class SubscriberWL<T> extends Subscriber<T> {

    Dialog dialog;

    public SubscriberWL(Context context, boolean cancelable) {
        dialog = new Dialog(context);// init dialog
        if (cancelable)// 取消订阅
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    if (!SubscriberWL.this.isUnsubscribed())
                        SubscriberWL.this.unsubscribe();
                }
            });
    }

    @Override
    public void onStart() {
        Log.e("SubscriberWL","--onStart--");
        dialog.show();
    }

    @Override
    public void onCompleted() {
        Log.e("SubscriberWL","--onCompleted--");
        if (dialog.isShowing())
            dialog.dismiss();
    }

    @Override
    public void onError(Throwable e) {
        Log.e("SubscriberWL","--onError--");
        if (dialog.isShowing())
            dialog.dismiss();
    }

}
