package com.mnunez.pagertest.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mnunez.pagertest.BuildConfig;
import com.mnunez.pagertest.R;
import com.mnunez.pagertest.models.AppInfoSingleton;
import com.mnunez.pagertest.network.ApiHandler;
import com.mnunez.pagertest.utils.StringUtils;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

/**
 * Created by mnunez on 5/31/17.
 */

public class StatusDialogFragment extends DialogFragment implements View.OnClickListener, EditText.OnEditorActionListener {

    private EditText vStatus;
    private Button vCancel, vConfirm;
    private WebSocket mWebSocket;
    private StatusHandlerListener mStatusHandlerListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.change_status_fragment, container, false);
        vStatus = (EditText) view.findViewById(R.id.status);
        vCancel = (Button) view.findViewById(R.id.cancel);
        vCancel.setOnClickListener(this);
        vConfirm = (Button) view.findViewById(R.id.confirm);
        vConfirm.setOnClickListener(this);
        return view;
    }

    // TODO check how to properly close the socket after message sent
    private void handleStatusChange() {
        String status = vStatus.getText().toString();
        if (StringUtils.isNotEmpty(status)) {
            AppInfoSingleton.getInstance().getmLoggedUser().setStatus(status);
            mStatusHandlerListener.updateStatus(status);
            ApiHandler apihHandler = new ApiHandler();
            WebSocketListener listener = new WebSocketListener() {
                @Override
                public void onOpen(WebSocket webSocket, Response response) {
                    super.onOpen(webSocket, response);
                }
            };
            mWebSocket = apihHandler.createWebSocket(BuildConfig.WEB_SOCKET_URL, listener);
            mWebSocket.send(status);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        if (dialog.getWindow() != null) {
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mStatusHandlerListener = (StatusHandlerListener) getActivity();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                getDialog().dismiss();
                break;
            case R.id.confirm:
                handleStatusChange();
                getDialog().dismiss();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i == EditorInfo.IME_ACTION_DONE) {
            handleStatusChange();
            getDialog().dismiss();
        }
        return false;
    }

    public interface StatusHandlerListener {
        void updateStatus(String status);
    }
}
