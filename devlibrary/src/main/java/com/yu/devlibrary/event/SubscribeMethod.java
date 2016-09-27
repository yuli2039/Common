package com.yu.devlibrary.event;

import java.lang.reflect.Method;

/**
 * @author yu
 *         Create on 2016/9/27 0027.
 */
class SubscribeMethod {
    Method method;
    ThreadMode threadMode;
    Object subscriber;

    SubscribeMethod(Method method, ThreadMode threadMode, Object subscriber) {
        this.method = method;
        this.threadMode = threadMode;
        this.subscriber = subscriber;
    }
}
