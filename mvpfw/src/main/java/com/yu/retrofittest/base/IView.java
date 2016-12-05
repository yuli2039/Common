package com.yu.retrofittest.base;

/**
 * @author yu
 *         Create on 16/8/13.
 */
public interface IView {
    void toast(String msg);

    void showLoading();

    void hideLoading();

    void onError();
}
