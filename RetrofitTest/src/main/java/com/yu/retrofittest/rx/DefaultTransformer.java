package com.yu.retrofittest.rx;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * 用于rxjava+retrofit请求网络时进行预处理；接口需要定义为Observable<Response<GankEn>>的形式
 * 1,code不是200~300之间则抛出异常，可在onError中处理；
 * 2,重试
 * 3,线程的切换
 */
public class DefaultTransformer<T extends Response<R>, R> implements Observable.Transformer<T, R> {
    @Override
    public Observable<R> call(Observable<T> tObservable) {
        return tObservable
                .map(new Func1<T, R>() {// 200~300,取出实体,否则抛出异常
                    @Override
                    public R call(T t) {
                        if (t.isSuccessful())
                            return t.body();
                        else
                            throw new ApiException(t.code(), t.message());
                    }
                })
                .retry(new Func2<Integer, Throwable, Boolean>() {// 重试
                    @Override
                    public Boolean call(Integer integer, Throwable throwable) {
                        return throwable instanceof ApiException && integer < 1;
                    }
                })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .unsubscribeOn(Schedulers.io())
                .compose(SchedulersCompat.<R>applyIoSchedulers());// 线程切换
    }

    public static DefaultTransformer create() {
        return new DefaultTransformer();
    }
}
