package com.mnunez.restapi.network;

import com.google.gson.Gson;
import com.mnunez.restapi.common.HttpMethodEnum;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

/**
 * Created by mnunez on 5/26/17.
 */
public class RestApihHandler {

    private OkHttpClient mHttpClient;

    public RestApihHandler() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.connectTimeout(60, TimeUnit.SECONDS);
        clientBuilder.readTimeout(60, TimeUnit.SECONDS);
        clientBuilder.writeTimeout(60, TimeUnit.SECONDS);
        clientBuilder.retryOnConnectionFailure(true);
        mHttpClient = clientBuilder.build();
    }

    public WebSocket createWebSocket(String url, WebSocketListener listener) {
        try {
            Request request = new Request.Builder().url(url).build();
            WebSocket socket = mHttpClient.newWebSocket(request, listener);
            mHttpClient.dispatcher().executorService().shutdown();
            return socket;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void execute(HttpMethodEnum method, String url, HashMap<String, String> params, RestApiCallback<Response> callback) {
        switch (method) {
            case POST:
                doPost(url, params, callback);
                break;
            case GET:
                doGet(url, callback);
                break;
            default:
                break;
        }
    }

    private void doPost(String url, HashMap<String, String> params, final RestApiCallback<Response> callback) {
        try {
            MediaType mMediaType = MediaType.parse("application/json");
            Gson gson = new Gson();
            String body = gson.toJson(params);
            RequestBody requestBody = RequestBody.create(mMediaType, body);
            Request.Builder requestBuilder = new Request.Builder();
            requestBuilder.url(url).post(requestBody);
            Request request = requestBuilder.build();
            mHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    callback.onError(e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    callback.onSuccess(response);
                }
            });
        } catch (Exception e) {
            callback.onError(e.getMessage());
        }
    }

    private void doGet(String url, final RestApiCallback<Response> callback) {
        try {
            Request.Builder requestBuilder = new Request.Builder();
            requestBuilder.url(url);
            Request request = requestBuilder.build();
            mHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    callback.onError(e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    callback.onSuccess(response);
                }
            });
        } catch (Exception e) {
            callback.onError(e.getMessage());
        }
    }

}
