package com.mnunez.pagertest.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.mnunez.pagertest.R;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by mnunez on 5/26/17.
 */

public class UIUtils {

    public static void showSimpleAlert(Context context, String message, String okButton) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle(context.getString(R.string.app_name));
        builder.setMessage(message);
        builder.setPositiveButton(okButton, null);
        builder.show();
    }

    public static void showAlertWithListeners(Activity activity, String message,
                                              String positiveBtn, String negativeBtn,
                                              DialogInterface.OnClickListener negativeListener,
                                              DialogInterface.OnClickListener positiveListener) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(activity);
        builder.setMessage(message);
        builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle(R.string.app_name);
        builder.setPositiveButton(positiveBtn, positiveListener);
        builder.setNegativeButton(negativeBtn, negativeListener);
        builder.setCancelable(false);
        builder.create().show();
    }

    public static void showAlertWithOneListener(Activity activity, String message, String btn,
                                                DialogInterface.OnClickListener listener) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(activity);
        builder.setMessage(message);
        builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle(R.string.app_name);
        builder.setPositiveButton(btn, listener);
        builder.create().show();
    }

    public static void showToast(Activity activity, String message) {
        if (activity != null) {
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        if (activity != null && activity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }
}
