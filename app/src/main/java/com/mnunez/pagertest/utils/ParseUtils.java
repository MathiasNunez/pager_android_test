package com.mnunez.pagertest.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mnunez.pagertest.models.AppInfoSingleton;
import com.mnunez.pagertest.models.Role;
import com.mnunez.pagertest.models.SocketEventEnum;
import com.mnunez.pagertest.models.User;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Response;

/**
 * Created by mnunez on 6/1/17.
 */

public class ParseUtils {

    public static void parseAndUpdateUser(String text) {
        Gson gson = new Gson();
        JsonElement jsonElement = gson.fromJson(text, JsonElement.class);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonObject userJson = jsonObject.get("user").getAsJsonObject();
        User user = gson.fromJson(userJson, User.class);
        int i = 0;
        boolean found = false;
        while (i < AppInfoSingleton.getInstance().getmUsers().size() && !found) {
            if (user != null &&
                    user.getGithub().equalsIgnoreCase(AppInfoSingleton.getInstance().getmUsers().get(i).getGithub())) {
                found = true;
            }
            i++;
        }
        if (user != null) {
            Role role = AppInfoSingleton.getInstance().getmRoles().get(user.getRoleId());
            user.setRole(role);
            if (found) {
                i--;
                AppInfoSingleton.getInstance().getmUsers().remove(i);
                AppInfoSingleton.getInstance().getmUsers().add(user);
            } else {
                AppInfoSingleton.getInstance().getmUsers().add(user);
            }
        }
    }

    public static List<User> parseUsers(Response response) {
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<User>>() {
        }.getType();
        List<User> users = gson.fromJson(response.body().charStream(), listType);
        for (User u : users) {
            Role r = AppInfoSingleton.getInstance().getmRoles().get(u.getRoleId());
            u.setRole(r);
        }
        return users;
    }

    public static Map<String, Role> parseRoles(Response response) {
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, String>>() {
        }.getType();
        Map<String, String> rolesString = gson.fromJson(new InputStreamReader(response.body().byteStream()), mapType);
        Map<String, Role> roles = new HashMap<>();
        for (String key : rolesString.keySet()) {
            Role role = new Role(key, rolesString.get(key));
            roles.put(key, role);
        }
        return roles;
    }

    public static SocketEventEnum parseEventType(String text) {
        SocketEventEnum socketEventEnum = null;
        Gson gson = new Gson();
        JsonElement jsonElement = gson.fromJson(text, JsonElement.class);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String event = jsonObject.get("event").getAsString();
        if (StringUtils.isNotEmpty(event)) {
            socketEventEnum = SocketEventEnum.get(event);
        }
        return socketEventEnum;
    }

    public static void parseAndUpdateStatusChanged(String text) {
        Gson gson = new Gson();
        JsonElement jsonElement = gson.fromJson(text, JsonElement.class);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String user = jsonObject.get("user").getAsString();
        String status = jsonObject.get("state").getAsString();

        if (AppInfoSingleton.getInstance().getmLoggedUser() != null &&
                AppInfoSingleton.getInstance().getmLoggedUser().getGithub().equalsIgnoreCase(user)) {
            AppInfoSingleton.getInstance().getmLoggedUser().setStatus(status);
        } else {
            int i = 0;
            boolean found = false;
            while (i < AppInfoSingleton.getInstance().getmUsers().size() && !found) {
                if (StringUtils.isNotEmpty(user) &&
                        user.equalsIgnoreCase(AppInfoSingleton.getInstance().getmUsers().get(i).getGithub())) {
                    found = true;
                    AppInfoSingleton.getInstance().getmUsers().get(i).setStatus(status);
                }
                i++;
            }
        }
    }

}
