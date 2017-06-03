package com.mnunez.pagertest.utils;

/**
 * Created by mnunez on 5/26/17.
 */
public class StringUtils {


    public static boolean isEmpty(String str) {
        boolean result = false;
        if ((str == null) || "".equals(str.trim()) || "null".equals(str.trim())) {
            result = true;
        }
        return result;
    }


    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }


    public static String nullTrim(String value) {
        return (value != null ? value.trim() : null);
    }

}
