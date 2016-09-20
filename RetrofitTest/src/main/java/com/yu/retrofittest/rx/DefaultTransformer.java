package com.yu.retrofittest.rx;

import com.yu.retrofittest.entity.BaseEntity;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 做重试和线程切换操作
 *
 * @author yu
 */
public class DefaultTransformer<T> implements Observable.Transformer<T, T> {
    @Override
    public Observable<T> call(Observable<T> tObservable) {
        return tObservable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<T, T>() {// 通用错误处理，判断code
                    @Override
                    public T call(T t) {
                        if (t instanceof BaseEntity) {
                            BaseEntity e = (BaseEntity) t;
                            int code = e.getCode();
                            if (!String.valueOf(code).startsWith("2"))// 返回的业务code不是2开头的，直接走错误流程
                                throw new ApiException(code, e.getMessage());
                        }
                        return t;
                    }
                });
    }

    public static <T> DefaultTransformer<T> create() {
        return new DefaultTransformer<>();
    }
}
