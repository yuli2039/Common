package com.yu.retrofittest.util;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;


/**
 * 应用状态追踪，主要用于注册activity栈，判断应用是否在前台和是否被强杀等状态</p>
 * 需要在application里初始化
 *
 * @author yu
 *         Create on 16/5/7.
 */
public class AppTracker {

    private static final int STATUS_FORCE_KILLED = -1;
    private static final int STATUS_NORMAL = 0;

    private static AppTracker sInstance;
    public static Application sContext;// 全局context

    /**
     * 当前应用的状态，默认为被回收状态；
     * ps：当最小化应用后，如果被强杀或者回收了，此值会重置为默认；
     * 我们在应用的第一个界面（welcomeActivity）修改此值为normal状态，在BaseActivity中判断此值，
     * 如果不为被强杀的状态，才进行初始化等操作，以此规避回收后恢复界面时某些对象为空引起crash问题；
     */
    private int appStatus = STATUS_FORCE_KILLED;
    private int activeCount = 0;// 当前活动的activity数

    /**
     * 当前应用是否在前台
     * 用于推送时判断是更新红点还是弹出notifycation
     */
    private boolean isForground = false;


    private AppTracker() {
    }

    public static AppTracker getInstance() {
        if (sInstance == null) {
            synchronized (AppTracker.class) {
                if (sInstance == null) {
                    sInstance = new AppTracker();
                }
            }
        }
        return sInstance;
    }

    public void init(Application application) {
        sContext = application;
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                ActivityStack.add(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {
                activeCount++;
            }

            @Override
            public void onActivityResumed(Activity activity) {
                isForground = true;
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                activeCount--;
                if (activeCount <= 0) {
                    isForground = false;
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                ActivityStack.remove(activity);
            }
        });
    }

    /**
     * 当前应用是否被回收
     */
    public boolean isForceKilled() {
        return appStatus == STATUS_FORCE_KILLED;
    }

    /**
     * 设置应用为正常状态
     */
    public void setStatusNormal() {
        this.appStatus = STATUS_NORMAL;
    }

    /**
     * 当前应用是否在前台
     */
    public boolean isForground() {
        return isForground;
    }

}
