package com.mnunez.pagertest.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mnunez.pagertest.R;
import com.mnunez.pagertest.fragments.FingerprintFragment;
import com.mnunez.pagertest.models.AppInfoSingleton;
import com.mnunez.pagertest.models.User;
import com.mnunez.pagertest.utils.SessionUtils;
import com.mnunez.pagertest.utils.StringUtils;
import com.mnunez.pagertest.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mnunez on 5/30/17.
 */

public class LoginActivity extends Activity implements View.OnClickListener, EditText.OnEditorActionListener, FingerprintFragment.FingerprintCallback {

    private EditText vUsername, vPassword;
    private Button vLoginBtn;
    private FingerprintManagerCompat fingerprintManager;
    private boolean mHasHardWareForFingerprint = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        vUsername = (EditText) findViewById(R.id.username);
        vPassword = (EditText) findViewById(R.id.password);
        vLoginBtn = (Button) findViewById(R.id.login);
        vLoginBtn.setOnClickListener(this);
        vLoginBtn.setOnEditorActionListener(this);
        initFingerprint();
    }

    private void initFingerprint() {
        fingerprintManager = FingerprintManagerCompat.from(this);
        if (fingerprintManager.isHardwareDetected() && fingerprintManager.hasEnrolledFingerprints()) {
            mHasHardWareForFingerprint = true;
            if (SessionUtils.getUseFingerprint(this)
                    && StringUtils.isNotEmpty(SessionUtils.getUser(this))) {
                FingerprintFragment fragment = FingerprintFragment.newInstance(fingerprintManager);
                fragment.show(getFragmentManager(), FingerprintFragment.class.getSimpleName());
            }
        }
    }


    @Override
    public void onClick(View view) {
        doLogin(true, vUsername.getText().toString());
    }

    private void doLogin(boolean askForFingerprint, String username) {
        int i = 0;
        boolean found = false;
        while (i < AppInfoSingleton.getInstance().getmUsers().size() && !found) {
            if (StringUtils.isNotEmpty(username) && username.equalsIgnoreCase(AppInfoSingleton.getInstance().getmUsers().get(i).getGithub())) {
                found = true;
                AppInfoSingleton.getInstance().setmLoggedUser(AppInfoSingleton.getInstance().getmUsers().get(i));
            }
            i++;
        }
        if (found) {
            AppInfoSingleton.getInstance().getmUsers().remove(i - 1);
            List<User> users = new ArrayList<>();
            users.addAll(AppInfoSingleton.getInstance().getmUsers());
            AppInfoSingleton.getInstance().setmFilteredUsers(users);
            SessionUtils.storeUser(this, username);
            if (askForFingerprint && mHasHardWareForFingerprint) {
                UIUtils.showAlertWithListeners(this, getString(R.string.fingerprint_remember_touch_id),
                        getString(R.string.text_ok), getString(R.string.text_cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                goToHome();
                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SessionUtils.storeUser(LoginActivity.this, vUsername.getText().toString());
                                SessionUtils.storeUseFringerprint(LoginActivity.this, true);
                                goToHome();
                            }
                        });

            } else {
                goToHome();
            }
        } else {
            UIUtils.showSimpleAlert(this, getString(R.string.login_error_message), getString(R.string.text_ok));
            vUsername.setText("");
            vPassword.setText("");
        }
    }

    private void goToHome() {
        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        finish();
    }

    @Override
    public boolean onEditorAction(TextView textView, int action, KeyEvent keyEvent) {
        if (action == EditorInfo.IME_ACTION_DONE) {
            doLogin(true, vUsername.getText().toString());
        }
        return false;
    }

    @Override
    public void success() {
        doLogin(false, SessionUtils.getUser(this));
    }

    @Override
    public void error(String errorMsg) {
        UIUtils.showToast(this, errorMsg);
    }
}
