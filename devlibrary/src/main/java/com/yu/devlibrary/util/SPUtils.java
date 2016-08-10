package com.yu.devlibrary.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author yu
 */
public final class SPUtils {

    public static final String SHARED_NAME = "config";

    public static <T> void put(String key, T value) {
        SharedPreferences sp = AppManager.appContext().getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if (value == null) {
            editor.putString(key, null);
        } else {
            if (value.getClass() == Boolean.class) {
                editor.putBoolean(key, (Boolean) value);
            } else if (value.getClass() == Float.class) {
                editor.putFloat(key, (Float) value);
            } else if (value.getClass() == Integer.class) {
                editor.putInt(key, (Integer) value);
            } else if (value.getClass() == Long.class) {
                editor.putLong(key, (Long) value);
            } else if (value.getClass() == String.class) {
                editor.putString(key, (String) value);
            } else {
                throw new RuntimeException("the put value type can't support.");
            }
        }

        SharedPreferencesCompat.apply(editor);
    }

    public static String get(String key, String defaultValue) {
        SharedPreferences sp = AppManager.appContext().getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE);
        return sp.getString(key, defaultValue);
    }

    public static boolean get(String key, boolean defaultValue) {
        SharedPreferences sp = AppManager.appContext().getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defaultValue);
    }

    public static float get(String key, float defaultValue) {
        SharedPreferences sp = AppManager.appContext().getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE);
        return sp.getFloat(key, defaultValue);
    }

    public static int get(String key, int defaultValue) {
        SharedPreferences sp = AppManager.appContext().getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE);
        return sp.getInt(key, defaultValue);
    }

    public static long get(String key, long defaultValue) {
        SharedPreferences sp = AppManager.appContext().getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE);
        return sp.getLong(key, defaultValue);
    }

    public static void remove(String key) {
        SharedPreferences sp = AppManager.appContext().getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit().remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    public static void clear() {
        SharedPreferences sp = AppManager.appContext().getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit().clear();
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 检查是否可以调用apply方法，否则调用commit
     */
    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
            }
            return null;
        }

        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            editor.commit();
        }
    }

}
