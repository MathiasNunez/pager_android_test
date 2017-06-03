package com.mnunez.pagertest;

import com.mnunez.pagertest.models.AppInfoSingleton;
import com.mnunez.pagertest.models.Role;
import com.mnunez.pagertest.models.User;
import com.mnunez.pagertest.utils.FilterUtils;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;

/**
 * Created by mnunez on 6/3/17.
 */

public class FilterTest {

    private Role role1;

    @Before
    public void prepareData() {
        Map<String, Role> roles = new HashMap<>();
        role1 = new Role("1", "Role Desc 1");
        roles.put(role1.getId(), role1);
        AppInfoSingleton.getInstance().setmRoles(roles);

        List<User> users = new ArrayList<>();
        User user = new User();
        user.setGithub("mathias");
        user.setRole(role1);
        user.setRoleId(role1.getId());
        user.setStatus("testing");
        user.setTags(new String[]{"test"});
        users.add(user);

        User user2 = new User();
        user2.setGithub("mathias2");
        user2.setRole(role1);
        user2.setRoleId(role1.getId());
        user2.setStatus("testing");
        user2.setTags(new String[]{"test"});
        users.add(user2);

        AppInfoSingleton.getInstance().setmUsers(users);
        AppInfoSingleton.getInstance().setmFilteredUsers(new ArrayList<User>());
    }

    @Test
    public void test_filterUsers_Found() {
        FilterUtils.filterUsers(role1, null, "testing");
        assertTrue(AppInfoSingleton.getInstance().getmFilteredUsers().size() == 2);
    }

    @Test
    public void test_filterUsers_NotFound() {
        FilterUtils.filterUsers(new Role("2324", null), "sarasa", null);
        assertTrue(AppInfoSingleton.getInstance().getmFilteredUsers().size() == 0);
    }

}
