package com.yu.devlibrary.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 通用工具，各种util
 */
public final class AppUtils {

    /**
     * 第一次和第二次的退出间隔时间基准
     */
    private static final long EXIT_TWICE_INTERVAL = 2000;
    private static long mExitTime = 0;

    /**
     * 第二次按退出则返回true,否则返回false
     *
     * @return
     */
    public static boolean exitTwice() {
        long newExitTime = System.currentTimeMillis();
        if (newExitTime - mExitTime > EXIT_TWICE_INTERVAL) {
            mExitTime = newExitTime;
            return false;
        } else {
            return true;
        }
    }

    public static String formatDate(long time) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    /**
     * 重启应用
     */
    public static void restartApplication() {
        Activity context = ActivityStack.getCurrentActicity();
        Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(context.getPackageName())
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    /**
     * 获取app的版本数versionCode,比如38
     *
     * @return
     */
    public static int versionCode() {
        int result = 0;
        String packageName = AppTracker.sContext.getPackageName();
        PackageInfo packageInfo;
        try {
            packageInfo = AppTracker.sContext.getPackageManager().getPackageInfo(packageName, 0);
            result = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new AssertionError(e);
        }
        return result;
    }

    /**
     * 获取app的版本名versionName,比如0.6.9
     *
     * @return
     */
    public static String versionName() {
        String result = null;
        String packageName = AppTracker.sContext.getPackageName();
        PackageInfo packageInfo;
        try {
            packageInfo = AppTracker.sContext.getPackageManager().getPackageInfo(packageName, 0);
            result = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            throw new AssertionError(e);
        }
        return result;
    }

    /**
     * 获取app的名称
     *
     * @return
     */
    public static String appName() {
        String result = null;
        String packageName = AppTracker.sContext.getPackageName();
        ApplicationInfo applicationInfo;
        try {
            PackageManager packageManager = AppTracker.sContext.getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(packageName, 0);
            result = packageManager.getApplicationLabel(applicationInfo).toString();
        } catch (PackageManager.NameNotFoundException e) {
            throw new AssertionError(e);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 调用相机前判断，否则会crash
     *
     * @param context
     * @return
     */
    public static boolean hasCamera(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    /**
     * 判断一个app是否在运行
     *
     * @param packageName app的包名
     * @return 在运行则返回true, 否则false
     */
    public static boolean isRunning(String packageName) {
        if (packageName == null) {
            packageName = AppTracker.sContext.getPackageName();
        }
        ActivityManager am = (ActivityManager) AppTracker.sContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> infos = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo rapi : infos) {
            if (rapi.processName.equals(packageName))
                return true;
        }
        return false;
    }

    /**
     * 判断一个activity是否在前台运行
     *
     * @param activityName activity的全路径名称
     * @return 在前台则返回true, 否则返回false
     */
    public static boolean isTopActivy(String activityName) {
        ActivityManager manager = (ActivityManager) AppTracker.sContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);
        String cmpNameTemp = null;

        if (runningTaskInfos != null) {
            cmpNameTemp = runningTaskInfos.get(0).topActivity.getShortClassName();
        }

        return cmpNameTemp != null && cmpNameTemp.endsWith(activityName);
    }

    /**
     * 把自身从父View中移除
     */
    public static void removeSelfFromParent(View view) {
        if (view != null) {
            ViewParent parent = view.getParent();
            if (parent != null && parent instanceof ViewGroup) {
                ViewGroup group = (ViewGroup) parent;
                group.removeView(view);
            }
        }
    }

    /**
     * 获取缓存大小
     *
     * @param context
     * @return
     */
    public static long getCacheSize(Context context) {
        return FileUtils.getFileSize(context.getCacheDir())
                + FileUtils.getFileSize(context.getExternalCacheDir());
    }

    /**
     * 删除应用数据： cache, file, share prefs, databases
     *
     * @param context
     */
    public static void clearAllCache(Context context) {
        clearCache(context);
        clearFiles(context);
        clearSharedPreference(context);
        clearDatabase(context);
    }

    /**
     * 删除应用缓存目录
     *
     * @param context
     */
    public static void clearCache(Context context) {
        FileUtils.delete(context.getCacheDir());
        FileUtils.delete(context.getExternalCacheDir());
    }

    /**
     * 删除应用文件目录
     *
     * @param context
     */
    public static void clearFiles(Context context) {
        FileUtils.delete(context.getFilesDir());
    }

    /**
     * 删除应用Shared Prefrence目录
     *
     * @param context
     */
    public static void clearSharedPreference(Context context) {
        FileUtils.delete(new File("/data/data/" + context.getPackageName() + "/shared_prefs"));
    }

    /**
     * 删除应用数据库目录
     *
     * @param context
     */
    public static void clearDatabase(Context context) {
        FileUtils.delete(new File("/data/data/" + context.getPackageName() + "/databases"));
    }

    /**
     * 获取设备唯一id
     */
    public static String getUUID(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * 安装APK
     */
    public static void installApk(Context context, String apkPath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + apkPath), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 卸载应用
     */
    public static void unInstallApk(Context context, String packageName) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        Uri packageURI = Uri.parse("package:" + packageName);
        intent.setData(packageURI);
        context.startActivity(intent);
    }


    /**
     * 显示输入法
     *
     * @param context
     * @param view
     */
    public static void showSoft(Context context, View view) {
        InputMethodManager imm =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    /**
     * 隐藏输入法
     *
     * @param context
     * @param view
     */
    public static void hideSoft(Context context, View view) {
        InputMethodManager imm =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 通用MD5加密
     *
     * @param string
     * @return
     */
    public static String toMd5(String string) {
        byte[] hash;

        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }

        return hex.toString();
    }

}
