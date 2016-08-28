package com.yu.devlibrary.util;

import java.util.regex.Pattern;

/**
 * @author yu
 *         Create on 16/8/28.
 */
public class RegexUtils {

    public static boolean isValidEmail(String email){
        boolean isValidEmail = false;
        Pattern pattern = Pattern.compile("^[a-z0-9]+([._\\\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$");
        isValidEmail = pattern.matcher(email).matches();
        return isValidEmail;
    }

    public static boolean isValidPhone(String phone){
        boolean isValidPhone = false;
        Pattern pattern = Pattern.compile("^((13[0-9])|(17[^4,\\D])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
        isValidPhone = pattern.matcher(phone).matches();
        return isValidPhone;
    }
}
