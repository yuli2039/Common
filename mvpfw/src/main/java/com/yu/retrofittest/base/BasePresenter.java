package com.yu.retrofittest.base;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * BasePresenter,子类继承后，在实现对应具体页面的Presenter
 */
public class BasePresenter<V extends IView> implements IPresenter {

    private CompositeSubscription mCompositeSubscription;
    protected V mView;

    public BasePresenter(V view) {
        this.mView = view;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestory() {
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
    private void onUnsubscribe() {
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
        }
    }

    protected void removeSubscription(Subscription subscribe) {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.remove(subscribe);
        }
    }
}
