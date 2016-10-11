package com.yu.retrofittest.rx;

import com.yu.retrofittest.entity.BaseEntity;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 错误处理和线程切换
 *
 * @author yu
 */
public class DefaultTransformer<T> implements Observable.Transformer<T, T> {
    @Override
    public Observable<T> call(Observable<T> tObservable) {
        return tObservable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
//                .map(new Func1<T, T>() {// 通用错误处理，判断code,也可以用自定义converter来预处理异常
//                    @Override
//                    public T call(T t) {
//                        if (t instanceof BaseEntity) {
//                            BaseEntity e = (BaseEntity) t;
//                            int code = e.getCode();
//                            if (code != 200) throw new ApiException(code, e.getMessage());
//                        }
//                        return t;
//                    }
//                })
                .flatMap(new Func1<T, Observable<T>>() {
                    @Override
                    public Observable<T> call(T t) {
                        if (t instanceof BaseEntity) {
                            BaseEntity e = (BaseEntity) t;
                            int code = e.getCode();
                            if (code != 200)
                                return Observable.error(new ApiException(code, e.getMessage()));
                        }
                        return Observable.just(t);
                    }
                });
    }

    public static <T> DefaultTransformer<T> create() {
        return new DefaultTransformer<>();
    }
}
