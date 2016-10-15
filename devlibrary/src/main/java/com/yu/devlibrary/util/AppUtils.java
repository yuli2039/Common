package com.yu.devlibrary.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;

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
        Activity context = AppManager.getCurrentActicity();
        Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(context.getPackageName())
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
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
     * 获取设备唯一id
     */
    public static String getUUID(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * 获取 MAC 地址
     * 须配置android.permission.ACCESS_WIFI_STATE权限
     */
    public static String getMacAddress(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        String mac = info.getMacAddress();
        LogUtils.i(TAG, " MAC：" + mac);
        return mac;
    }

    public static AssetManager getAssets() {
        return AppManager.appContext().getAssets();
    }

    public static Resources getResource() {
        return AppManager.appContext().getResources();
    }

}
