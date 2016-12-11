package com.yu.devlibrary.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;


/**
 * 需要在application里初始化，提供一些api用于app状态判断和activity栈管理等
 *
 * @author yu
 *         Create on 16/5/7.
 */
public class AppManager {

    private static Context sContext;
    private static int activeCount = 0;// 当前活动的activity数
    private static boolean isForground = false;// 应用是否在前台

    public static void init(Application application) {
        sContext = application.getApplicationContext();
        activeCount = 0;
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
     * @return 当前应用是否在前台
     */
    public static boolean isForground() {
        return isForground;
    }


    public static void jump(Class<? extends Activity> clazz) {
        try {
            Activity context = ActivityStack.getCurrentActivity();
            context.startActivity(new Intent(context, clazz));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void jump(Class<? extends Activity> clazz, String key, Parcelable value) {
        try {
            Activity context = ActivityStack.getCurrentActivity();
            context.startActivity(new Intent(context, clazz).putExtra(key, value));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void jump(Class<? extends Activity> clazz, String key, Serializable value) {
        try {
            Activity context = ActivityStack.getCurrentActivity();
            context.startActivity(new Intent(context, clazz).putExtra(key, value));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Activity getCurrentActicity() {
        return ActivityStack.getCurrentActivity();
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
        private static void add(Activity aty) {
            synchronized (ActivityStack.class) {
                STACK.addLast(aty);
            }
        }

        // 出栈
        private static void remove(Activity aty) {
            synchronized (ActivityStack.class) {
                if (STACK.contains(aty))
                    STACK.remove(aty);
            }
        }

        private static Activity getCurrentActivity() {
            return STACK.getLast();
        }

        private static boolean isExists(Class<? extends Activity> clazz) {
            for (Activity aty : STACK) {
                if (aty.getClass().getSimpleName().equals(clazz.getSimpleName()))
                    return true;
            }
            return false;
        }

        private static void exitApp() {
            synchronized (ActivityStack.class) {
                List<Activity> copy = new LinkedList<>(STACK);
                for (Activity aty : copy) {
                    aty.finish();
                }
                copy.clear();

                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }

        private static void finishExcept(Class<? extends Activity> clazz) {
            synchronized (ActivityStack.class) {
                List<Activity> copy = new LinkedList<>(STACK);
                for (Activity aty : copy) {
                    if (!aty.getClass().equals(clazz)) aty.finish();
                }
                copy.clear();
            }
        }
    }
}
