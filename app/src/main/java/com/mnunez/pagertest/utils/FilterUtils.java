package com.mnunez.pagertest.utils;

import com.mnunez.pagertest.models.AppInfoSingleton;
import com.mnunez.pagertest.models.Role;
import com.mnunez.pagertest.models.User;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mnunez on 6/3/17.
 */

public class FilterUtils {

    public static Map<String, User> filterUsers(Role role, String filterSkills, String filterStatus) {

        Map<String, User> filter = new HashMap<>();
        if (role != null) {
            for (User u : AppInfoSingleton.getInstance().getmUsers()) {
                if (u.getRoleId().equals(role.getId())) {
                    filter.put(u.getGithub(), u);
                }
            }
        }
        if (StringUtils.isNotEmpty(filterSkills)) {
            for (User u : AppInfoSingleton.getInstance().getmUsers()) {
                if (u.getTags() != null && Arrays.toString(u.getTags()).contains(filterSkills)) {
                    filter.put(u.getGithub(), u);
                }
            }
        }
        if (StringUtils.isNotEmpty(filterStatus)) {
            for (User u : AppInfoSingleton.getInstance().getmUsers()) {
                if (StringUtils.isNotEmpty(u.getStatus()) && u.getStatus().contains(filterStatus)) {
                    filter.put(u.getGithub(), u);
                }
            }
        }
        AppInfoSingleton.getInstance().getmFilteredUsers().clear();
        AppInfoSingleton.getInstance().getmFilteredUsers().addAll(filter.values());
        return filter;
    }
}
