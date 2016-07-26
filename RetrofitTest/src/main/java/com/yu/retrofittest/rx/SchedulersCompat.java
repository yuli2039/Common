package com.yu.retrofittest.rx;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 此类用于重用rx链式调用中的 线程切换代码
 * 使用compose操作符应用
 */
public class SchedulersCompat {

    private static final Observable.Transformer ioTransformer = new Observable.Transformer() {
        @Override
        public Object call(Object observable) {
            return ((Observable) observable).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io());
        }
    };
    private static final Observable.Transformer executorTransformer = new Observable.Transformer() {
        @Override
        public Object call(Object observable) {
            return ((Observable) observable).subscribeOn(Schedulers.from(ExecutorManager.eventExecutor))
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.from(ExecutorManager.eventExecutor));
        }
    };

    /**
     * Don't break the chain: use RxJava's compose() operator
     */

    public static <T> Observable.Transformer<T, T> applyIoSchedulers() {
        return (Observable.Transformer<T, T>) ioTransformer;
    }

    public static <T> Observable.Transformer<T, T> applyExecutorSchedulers() {
        return (Observable.Transformer<T, T>) executorTransformer;
    }
}