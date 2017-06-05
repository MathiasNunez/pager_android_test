package com.mnunez.pagertest.models;

import java.util.List;
import java.util.Map;

/**
 * Created by mnunez on 6/3/17.
 */

public class AppInfoSingleton {

    private static AppInfoSingleton instance;
    private List<User> mUsers;
    private List<User> mFilteredUsers;
    private Map<String, Role> mRoles;
    private User mLoggedUser;

    public static AppInfoSingleton getInstance() {
        if (instance == null) {
            instance = new AppInfoSingleton();
        }
        return instance;
    }

    public List<User> getmUsers() {
        return mUsers;
    }

    public void setmUsers(List<User> mUsers) {
        this.mUsers = mUsers;
    }

    public Map<String, Role> getmRoles() {
        return mRoles;
    }

    public void setmRoles(Map<String, Role> mRoles) {
        this.mRoles = mRoles;
    }

    public User getmLoggedUser() {
        return mLoggedUser;
    }

    public void setmLoggedUser(User mLoggedUser) {
        this.mLoggedUser = mLoggedUser;
    }

    public List<User> getmFilteredUsers() {
        return mFilteredUsers;
    }

    public void setmFilteredUsers(List<User> mFilteredUsers) {
        this.mFilteredUsers = mFilteredUsers;
    }
}
