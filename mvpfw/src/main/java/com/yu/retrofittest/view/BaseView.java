package com.yu.retrofittest.view;

/**
 * @author yu
 *         Create on 16/8/13.
 */
public interface BaseView {
    void toast(String msg);

    void showLoading();

    void dismissLoading();
}
