package com.yu.devlibrary.util;

/**
 * 数字类型转换工具类
 *
 * @author yuyh.
 */
public class NumberUtils {

    public static int convert2int(String intStr, int defValue) {
        try {
            return Integer.parseInt(intStr);
        } catch (NumberFormatException e) {
        }
        return defValue;
    }

    public static long convert2long(String longStr, long defValue) {
        try {
            return Long.parseLong(longStr);
        } catch (NumberFormatException e) {
        }
        return defValue;
    }

    public static float convert2float(String fStr, float defValue) {
        try {
            return Float.parseFloat(fStr);
        } catch (NumberFormatException e) {
        }
        return defValue;
    }

    public static double convert2double(String dStr, double defValue) {
        try {
            return Double.parseDouble(dStr);
        } catch (NumberFormatException e) {
        }
        return defValue;
    }


    public static Integer convert2Integer(String intStr) {
        try {
            return Integer.parseInt(intStr);
        } catch (NumberFormatException e) {
        }
        return null;
    }

    public static Long convert2Long(String longStr) {
        try {
            return Long.parseLong(longStr);
        } catch (NumberFormatException e) {
        }
        return null;
    }

    public static Float convert2Float(String fStr) {
        try {
            return Float.parseFloat(fStr);
        } catch (NumberFormatException e) {
        }
        return null;
    }

    public static Double convert2Double(String dStr) {
        try {
            return Double.parseDouble(dStr);
        } catch (NumberFormatException e) {
        }
        return null;
    }
}
