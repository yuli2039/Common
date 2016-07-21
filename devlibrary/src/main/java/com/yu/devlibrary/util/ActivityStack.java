package com.yu.devlibrary.util;

import android.app.Activity;

import java.util.LinkedList;
import java.util.List;

/**
 * 模拟activity栈
 *
 * @author yu
 *         Create on 16/4/18.
 */
public class ActivityStack {

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

    public static boolean isExists(String name) {
        for (Activity aty : STACK) {
            if (aty.getClass().getSimpleName().equals(name))
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
