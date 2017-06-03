package com.mnunez.pagertest.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by mnunez on 6/3/17.
 */

public class SessionUtils {

    private static final String PREFERENCE_KEY = "pager_test_mnunez";
    private static final String USER = "user";
    private static final String USE_FINGERPRINT = "use_fingerprint";

    public static void storeUser(Context c, String user) {
        SharedPreferences.Editor editor = c.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE).edit();
        editor.putString(USER, user);
        editor.apply();
    }

    public static String getUser(Context c) {
        return c.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE).getString(USER, null);
    }

    public static void storeUseFringerprint(Context c, boolean useFingerprint) {
        SharedPreferences.Editor editor = c.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE).edit();
        editor.putBoolean(USE_FINGERPRINT, useFingerprint);
        editor.apply();
    }

    public static boolean getUseFingerprint(Context c) {
        return c.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE).getBoolean(USE_FINGERPRINT, false);
    }

}
