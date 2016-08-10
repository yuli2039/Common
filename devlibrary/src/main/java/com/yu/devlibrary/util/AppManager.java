package com.yu.devlibrary.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import java.util.LinkedList;
import java.util.List;


/**
 * 需要在application里初始化，提供一些api用于app状态判断和activity栈管理等
 *
 * @author yu
 *         Create on 16/5/7.
 */
public class AppManager {

    private static Application sContext;

    private static final int STATUS_FORCE_KILLED = -1;
    private static final int STATUS_NORMAL = 0;

    /**
     * 当前应用的状态，默认为被回收状态；
     * ps：当最小化应用后，如果被强杀或者回收了，此值会重置为默认；
     * 我们在应用的第一个界面（WelcomeActivity）修改此值为normal状态，在BaseActivity中判断此值，
     * 如果处于回收状态，则可以进行重启等炒作，否则才进行初始化等操作，以此规避回收后恢复界面时某些对象为空引起crash问题；
     */
    private int appStatus = STATUS_FORCE_KILLED;
    private int activeCount = 0;// 当前活动的activity数
    private boolean isForground = false;// 应用是否在前台


    private AppManager() {
    }

    public static AppManager getInstance() {
        return IntstanceHolder.sIntance;
    }

    private static class IntstanceHolder {
        private static final AppManager sIntance = new AppManager();
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
            public void onActivityDestroyed(android.app.Activity activity) {
                ActivityStack.remove(activity);
            }
        });
    }

    /**
     * @return 全局context
     */
    public static Context appContext() {
        if (sContext == null)
            throw new IllegalStateException("AppManager must be init in application!");
        return sContext;
    }

    /**
     * 设置应用为正常状态
     */
    public void setStatusNormal() {
        this.appStatus = STATUS_NORMAL;
    }

    /**
     * @return 当前应用是否在前台
     */
    public boolean isForground() {
        return isForground;
    }

    /**
     * @return 当前应用是否被回收
     */
    public boolean isForceKilled() {
        return appStatus == STATUS_FORCE_KILLED;
    }

    // **********************************************
    public static Activity getCurrentActicity() {
        return ActivityStack.getCurrentActicity();
    }

    public static boolean isExists(Class<? extends Activity> clazz) {
        return ActivityStack.isExists(clazz);
    }

    public static void finishExcept(Class<? extends Activity> clazz) {
        ActivityStack.finishExcept(clazz);
    }

    public static void exitApp() {
        ActivityStack.exitApp();
    }

    /**
     * 模拟activity栈，对activity进行管理
     */
    private static class ActivityStack {
        private static final LinkedList<Activity> STACK = new LinkedList<>();

        // 入栈
        public static void add(Activity aty) {
            synchronized (ActivityStack.class) {
                STACK.addLast(aty);
            }
        }

        // 出栈
        public static void remove(Activity aty) {
            synchronized (ActivityStack.class) {
                if (STACK.contains(aty))
                    STACK.remove(aty);
            }
        }

        public static Activity getCurrentActicity() {
            return STACK.getLast();
        }

        public static boolean isExists(Class<? extends Activity> clazz) {
            for (Activity aty : STACK) {
                if (aty.getClass().getSimpleName().equals(clazz.getSimpleName()))
                    return true;
            }
            return false;
        }

        public static void exitApp() {
            synchronized (ActivityStack.class) {
                List<Activity> copy = new LinkedList<>(STACK);
                for (Activity aty : copy) {
                    aty.finish();
                }
                copy.clear();

                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }

        public static void finishExcept(Class<? extends Activity> clazz) {
            synchronized (ActivityStack.class) {
                List<Activity> copy = new LinkedList<>(STACK);
                for (Activity aty : copy) {
                    if (!aty.getClass().equals(clazz))
                        aty.finish();
                }
                copy.clear();
            }
        }
    }
}
