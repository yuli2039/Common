package com.yu.retrofittest.rx;

import com.yu.retrofittest.http.HttpResult;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 错误处理和线程切换
 *
 * @author yu
 */
public class DefaultTransformer<T> implements Observable.Transformer<HttpResult<T>, T> {

    @Override
    public Observable<T> call(Observable<HttpResult<T>> tObservable) {
        return tObservable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<HttpResult<T>, Observable<T>>() {
                    @Override
                    public Observable<T> call(HttpResult<T> result) {
                        if (result.isError()) return Observable.error(new ApiException(-200, "request fail..."));
                        return Observable.just(result.getResults());
                    }
                });
    }
}