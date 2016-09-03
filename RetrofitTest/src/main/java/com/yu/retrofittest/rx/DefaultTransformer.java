package com.yu.retrofittest.rx;

import java.net.SocketTimeoutException;

import rx.Observable;
import rx.functions.Func2;

/**
 * 做重试和线程切换操作
 */
public class DefaultTransformer<T> implements Observable.Transformer<T, T> {
    @Override
    public Observable<T> call(Observable<T> tObservable) {
        return tObservable
                .retry(new Func2<Integer, Throwable, Boolean>() {// 重试
                    @Override
                    public Boolean call(Integer integer, Throwable throwable) {
                        return throwable instanceof SocketTimeoutException && integer < 1;
                    }
                })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .unsubscribeOn(Schedulers.io())
                .compose(SchedulersCompat.<T>applyIoSchedulers());// 线程切换
    }

    public static DefaultTransformer create() {
        return new DefaultTransformer();
    }
}
