package com.yu.retrofittest.base;


import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * BasePresenter,子类继承后，在实现对应具体页面的Presenter
 */
public class BasePresenter<V> {
    private CompositeSubscription mCompositeSubscription;
    protected V mView;

    public BasePresenter(V view) {
        attachView(view);
    }

    public void attachView(V view) {
        if (!(view instanceof BaseView))
            throw new IllegalStateException("the View must instance of BaseView");
        this.mView = view;
    }

    public void detachView() {
        this.mView = null;
        onUnsubscribe();
    }

    /**
     * 所有rx订阅后，需要调用此方法，用于在detachView时取消订阅
     */
    protected void addSubscription(Subscription subscribe) {
        if (mCompositeSubscription == null)
            mCompositeSubscription = new CompositeSubscription();
        mCompositeSubscription.add(subscribe);
    }


    /**
     * 取消本页面所有订阅
     */
    protected void onUnsubscribe() {
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
        }
    }
}
