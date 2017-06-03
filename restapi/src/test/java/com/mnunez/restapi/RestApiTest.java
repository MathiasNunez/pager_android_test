package com.mnunez.restapi;


import com.google.gson.Gson;
import com.mnunez.restapi.common.HttpMethodEnum;
import com.mnunez.restapi.network.RestApiCallback;
import com.mnunez.restapi.network.RestApihHandler;

import org.junit.Test;

import java.util.HashMap;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

import static org.junit.Assert.assertTrue;

public class RestApiTest {

    @Test
    public void doGetTest() throws Exception {
        RestApihHandler apihHandler = new RestApihHandler();
        apihHandler.execute(HttpMethodEnum.GET, "https://pager-team.herokuapp.com/roles",
                null, new RestApiCallback<Response>() {
                    @Override
                    public void onSuccess(Response object) {
                        Gson gson = new Gson();
                        gson.fromJson(object.body().charStream(), Object.class);
                        // This is just to test that get method is executed properly
                        assertTrue(true);
                    }

                    @Override
                    public void onError(String message) {
                        assertTrue(false);
                    }
                });
    }

    @Test
    public void doPostTest() throws Exception {
        RestApihHandler apihHandler = new RestApihHandler();
        HashMap<String, String> params = new HashMap<>();
        params.put("title", "Test 1");
        params.put("body", "test body");
        params.put("userId", "1");
        apihHandler.execute(HttpMethodEnum.POST, "https://jsonplaceholder.typicode.com/posts",
                params, new RestApiCallback<Response>() {
                    @Override
                    public void onSuccess(Response object) {
                        Gson gson = new Gson();
                        gson.fromJson(object.body().charStream(), Object.class);
                        // This is just to test that post method is executed properly
                        assertTrue(true);
                    }

                    @Override
                    public void onError(String message) {
                        assertTrue(false);
                    }
                });
    }

    @Test
    public void doTestWebSocket() {
        RestApihHandler apihHandler = new RestApihHandler();
        apihHandler.createWebSocket("ws://echo.websocket.org", new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                webSocket.send("test");
                assertTrue(response.code() == 200 || response.code() == 201);
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                assertTrue(text.equals("test"));
            }
        });


    }

}