package com.mnunez.pagertest;

import com.mnunez.pagertest.models.AppInfoSingleton;
import com.mnunez.pagertest.models.Role;
import com.mnunez.pagertest.models.SocketEventEnum;
import com.mnunez.pagertest.models.User;
import com.mnunez.pagertest.utils.ParseUtils;

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

public class ParsingTests {

    private String userText = "{\"event\": \"user_new\",\"user\": {\"name\": \"Emiliano Viscarra\",\"avatar\": \"https://www.dropbox.com/s/p1qr5zqnjy4du03/emi.png?dl=1\",\"github\": \"chompas\",\"role\": 1,\"gender\": \"Male\",\"languages\": [\"en\", \"es\"],\"tags\": [\"Objective-C\", \"Management\"],\"location\": \"us\"}}";
    private String statusText = "{\"event\":\"state_change\",\"user\":\"chompas\",\"state\":\"Test\"}\n";
    private User user;


    @Before
    public void prepareAppInfoSingleton() {
        Map<String, Role> roles = new HashMap<>();
        Role role1 = new Role("1", "Role Desc 1");
        Role role2 = new Role("2", "Role Desc 2");
        roles.put(role1.getId(), role1);
        roles.put(role2.getId(), role2);
        AppInfoSingleton.getInstance().setmRoles(roles);

        List<User> users = new ArrayList<>();
        user = new User();
        user.setGithub("chompas");
        user.setRole(role1);
        user.setStatus("no status");
        users.add(user);
        AppInfoSingleton.getInstance().setmUsers(users);
    }

    @Test
    public void test_updateUser() {
        ParseUtils.parseAndUpdateUser(userText);
        boolean found = false;
        for (User u : AppInfoSingleton.getInstance().getmUsers()) {
            if (u.getGithub().equalsIgnoreCase("chompas")) {
                found = true;
            }
        }
        assertTrue(found);
    }

    @Test
    public void test_parseEventType() {
        SocketEventEnum socketEventEnum = ParseUtils.parseEventType(userText);
        assertTrue(SocketEventEnum.USER == socketEventEnum);
    }

    @Test
    public void test_parseUpdateStatus(){
        ParseUtils.parseAndUpdateStatusChanged(statusText);
        assertTrue(user.getStatus().equals("Test"));
    }

}
