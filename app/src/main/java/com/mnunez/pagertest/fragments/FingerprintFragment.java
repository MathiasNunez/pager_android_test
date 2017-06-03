package com.mnunez.pagertest.fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mnunez.pagertest.R;


/**
 * Created by mnunez on 06/03/17.
 */

public class FingerprintFragment extends DialogFragment {

    private Button vCancel;
    private FingerprintManagerCompat mFingerprintManager;
    private FingerprintCallback mFingerprintCallback;
    private CancellationSignal mCancellationSignal;
    private boolean mAuthHasBeenCanceled = false;

    public static FingerprintFragment newInstance(FingerprintManagerCompat fingerprintManager) {
        FingerprintFragment f = new FingerprintFragment();
        f.mFingerprintManager = fingerprintManager;
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fingerprint_fragment, container, false);
        vCancel = (Button) v.findViewById(R.id.cancel);
        setCancelable(false);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mCancellationSignal = new CancellationSignal();
        vCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
                mAuthHasBeenCanceled = true;
                mCancellationSignal.cancel();
            }
        });
        mFingerprintManager.authenticate(null, 0, mCancellationSignal, new FingerprintManagerCompat.AuthenticationCallback() {

            @Override
            public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                getDialog().dismiss();
                mFingerprintCallback.success();
            }

            @Override
            public void onAuthenticationError(int errMsgId, CharSequence errString) {
                super.onAuthenticationError(errMsgId, errString);
                handleAuthenticationError(errString.toString());
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                handleAuthenticationError(getString(R.string.error_fingerprint));
            }
        }, null);
    }

    private void handleAuthenticationError(String errorMsg) {
        try {
            if (getActivity() != null) {
                PowerManager powerManager = (PowerManager) getActivity().getSystemService(Context.POWER_SERVICE);
                if (powerManager.isScreenOn()) {
                    if (!mAuthHasBeenCanceled && !mCancellationSignal.isCanceled()) {
                        mFingerprintCallback.error(errorMsg);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        mCancellationSignal.cancel();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mFingerprintCallback = (FingerprintCallback) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mFingerprintCallback = null;
    }

    public interface FingerprintCallback {
        public void success();

        public void error(String errorMsg);
    }
}
