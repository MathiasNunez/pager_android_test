package com.mnunez.pagertest.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.mnunez.pagertest.R;
import com.mnunez.pagertest.models.AppInfoSingleton;
import com.mnunez.pagertest.models.Role;
import com.mnunez.pagertest.models.User;
import com.mnunez.pagertest.network.ApiHandler;
import com.mnunez.pagertest.utils.UIUtils;
import com.mnunez.restapi.network.RestApiCallback;
import com.wang.avi.AVLoadingIndicatorView;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;

import java.util.List;
import java.util.Map;

/**
 * Created by mnunez on 5/30/17.
 */

public class SplashActivity extends Activity {

    private AVLoadingIndicatorView mLoading;
    private ApiHandler mApiHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mLoading = (AVLoadingIndicatorView) findViewById(R.id.loading);
        mLoading.show();
        mApiHandler = new ApiHandler();
        mApiHandler.doGetRoles(new RestApiCallback<Map<String, Role>>() {
            @Override
            public void onSuccess(Map<String, Role> roles) {
                AppInfoSingleton.getInstance().setmRoles(roles);
                getUsers();
            }

            @Override
            public void onError(final String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        UIUtils.showSimpleAlert(SplashActivity.this, message, getString(R.string.text_ok));
                    }
                });
            }
        });

        checkForUpdates();
    }

    private void getUsers() {
        mApiHandler.doGetUsers(new RestApiCallback<List<User>>() {
            @Override
            public void onSuccess(List<User> users) {
                AppInfoSingleton.getInstance().setmUsers(users);
                goToLogin();
            }

            @Override
            public void onError(final String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        UIUtils.showSimpleAlert(SplashActivity.this, message, getString(R.string.text_ok));
                    }
                });
            }
        });
    }

    private void goToLogin() {
        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        checkForCrashes();
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterManagers();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterManagers();
    }

    private void checkForCrashes() {
        CrashManager.register(this);
    }

    private void checkForUpdates() {
        UpdateManager.register(this);
    }

    private void unregisterManagers() {
        UpdateManager.unregister();
    }

}
