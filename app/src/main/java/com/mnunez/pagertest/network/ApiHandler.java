package com.mnunez.pagertest.network;

import com.mnunez.pagertest.BuildConfig;
import com.mnunez.pagertest.models.Role;
import com.mnunez.pagertest.models.User;
import com.mnunez.pagertest.utils.ParseUtils;
import com.mnunez.restapi.common.HttpMethodEnum;
import com.mnunez.restapi.network.RestApiCallback;
import com.mnunez.restapi.network.RestApihHandler;

import java.util.List;
import java.util.Map;

import okhttp3.Response;

/**
 * Created by mnunez on 5/30/17.
 */

public class ApiHandler extends RestApihHandler {

    public void doGetRoles(final RestApiCallback<Map<String, Role>> callback) {
        execute(HttpMethodEnum.GET, BuildConfig.GET_ROLES_URL, null, new RestApiCallback<Response>() {
            @Override
            public void onSuccess(Response object) {
                try {
                    callback.onSuccess(ParseUtils.parseRoles(object));
                } catch (Exception e) {
                    callback.onError(e.getMessage());
                }
            }

            @Override
            public void onError(String message) {
                callback.onError(message);
            }
        });
    }

    public void doGetUsers(final RestApiCallback<List<User>> callback) {
        execute(HttpMethodEnum.GET, BuildConfig.GET_USERS_URL, null, new RestApiCallback<Response>() {
            @Override
            public void onSuccess(Response object) {
                callback.onSuccess(ParseUtils.parseUsers(object));
            }

            @Override
            public void onError(String message) {
                callback.onError(message);
            }
        });
    }
}
