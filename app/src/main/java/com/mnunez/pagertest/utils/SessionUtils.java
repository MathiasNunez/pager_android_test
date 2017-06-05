package com.mnunez.pagertest.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.mnunez.pagertest.models.Role;
import com.mnunez.pagertest.models.User;

/**
 * Created by mnunez on 6/3/17.
 */

public class SessionUtils {

    private static final String PREFERENCE_KEY = "pager_test_mnunez";
    private static final String USER = "user";
    private static final String USE_FINGERPRINT = "use_fingerprint";
    private static final String USER_FOLLOW_ID = "user_follow_id";
    private static final String USER_FOLLOW_NAME = "user_follow_name";
    private static final String USER_FOLLOW_ROLE_DESC = "user_follow_role_desc";
    private static final String USER_FOLLOW_AVATAR = "user_follow_avatar";


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

    public static void storeUserFollow(Context c, User user) {
        SharedPreferences.Editor editor = c.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE).edit();
        editor.putString(USER_FOLLOW_ID, user.getGithub());
        editor.putString(USER_FOLLOW_NAME, user.getName());
        editor.putString(USER_FOLLOW_ROLE_DESC, user.getRole().getDescription());
        editor.putString(USER_FOLLOW_AVATAR, user.getAvatar());
        editor.apply();
    }

    public static User getUserFollow(Context c) {
        User user = null;
        String id = c.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE).getString(USER_FOLLOW_ID, null);
        if (StringUtils.isNotEmpty(id)) {
            user = new User();
            user.setName(c.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE).getString(USER_FOLLOW_NAME, null));
            user.setGithub(id);
            user.setAvatar(c.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE).getString(USER_FOLLOW_AVATAR, null));
            Role role = new Role();
            role.setDescription(c.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE).getString(USER_FOLLOW_ROLE_DESC, null));
            user.setRole(role);
        }
        return user;
    }

    public static void deleteUserFollow(Context c) {
        SharedPreferences.Editor editor = c.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE).edit();
        editor.remove(USER_FOLLOW_ID);
        editor.remove(USER_FOLLOW_NAME);
        editor.remove(USER_FOLLOW_ROLE_DESC);
        editor.remove(USER_FOLLOW_AVATAR);
        editor.apply();
    }

}
