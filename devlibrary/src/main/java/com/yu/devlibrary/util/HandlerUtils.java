package com.yu.devlibrary.util;

import android.os.Handler;
import android.os.Looper;

/**
 * @author yu
 *         Create on 16/10/15.
 */
public class HandlerUtils {

    private static Handler sHandler = new Handler(Looper.getMainLooper());

    public static void runOnUI(Runnable r) {
        sHandler.post(r);
    }

    public static void runOnUIDelayed(Runnable r, long delayMills) {
        sHandler.postDelayed(r, delayMills);
    }

    public static void removeRunnable(Runnable r) {
        if (r == null) {
            sHandler.removeCallbacksAndMessages(null);
        } else {
            sHandler.removeCallbacks(r);
        }
    }
}
