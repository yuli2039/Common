package com.yu.retrofittest.rx.bus;

import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * rxbus订阅线程
 */
public enum EventThread {
    /**
     * 主线程
     */
    MAIN_THREAD,
    /**
     * 新的线程
     */
    NEW_THREAD,
    /**
     * 读写线程
     */
    IO,
    /**
     * 计算工作默认线程
     */
    COMPUTATION,
    /**
     * 在当前线程中按照队列方式执行
     */
    TRAMPOLINE,
    /**
     * 当前线程
     */
    IMMEDIATE;
//    EXECUTOR,
//    HANDLER;

    public Scheduler getScheduler() {
        Scheduler scheduler;
        switch (this) {
            case MAIN_THREAD:
                scheduler = AndroidSchedulers.mainThread();
                break;
            case NEW_THREAD:
                scheduler = Schedulers.newThread();
                break;
            case IO:
                scheduler = Schedulers.io();
                break;
            case COMPUTATION:
                scheduler = Schedulers.computation();
                break;
            case TRAMPOLINE:
                scheduler = Schedulers.trampoline();
                break;
            case IMMEDIATE:
                scheduler = Schedulers.immediate();
                break;
            default:
                scheduler = AndroidSchedulers.mainThread();
        }
        return scheduler;
    }
}
