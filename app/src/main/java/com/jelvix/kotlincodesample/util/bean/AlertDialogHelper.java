package com.jelvix.kotlincodesample.util.bean;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

import com.jelvix.kotlincodesample.R;

import org.androidannotations.annotations.EBean;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

@EBean
public class AlertDialogHelper {

    private Context context;
    private AlertDialog alertDialog;
    private DoOnDismiss doOnDismiss;

    public AlertDialogHelper(Context context) {
        this.context = context;
    }

    public void setDoOnDismiss(DoOnDismiss doOnDismiss) {
        this.doOnDismiss = doOnDismiss;
    }

    public void showAlertDialog(String message, @Nullable Alert.DoOnError doOnError) {
        Alert alertBuilder = Alert.newBuilder().setMessage(message).setDoOnError(doOnError).create();
        showAlertDialog(alertBuilder);
    }

    public void showAlertDialog(Alert alert) {
        if (alertDialog != null) {
            alertDialog.setOnDismissListener(null);
            alertDialog.dismiss();
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        switch (alert.getType()) {
            case Alert.ERROR:
                alertDialogBuilder.setTitle(R.string.warning);
                break;
            case Alert.SUCCESS:
                alertDialogBuilder.setTitle(R.string.success);
                break;
        }
        if (alert.getMessage() != null) {
            alertDialogBuilder.setMessage(alert.getMessage());
        }
        alertDialogBuilder.setOnDismissListener(dialog -> {
            if (alert.getDoOnError() != null) {
                alert.getDoOnError().doOnError();
            }
            if (doOnDismiss != null) {
                doOnDismiss.doOnDismiss();
            }
        });
        alertDialogBuilder.setPositiveButton(R.string.ok, (dialog, which) -> {
            if (alert.getDoOnSuccess() != null) {
                alert.getDoOnSuccess().doOnSuccess();
            }
        });

        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void dismissDialog(boolean isActivityDestroy) {
        if (alertDialog == null) {
            return;
        }
        if (isActivityDestroy) {
            alertDialog.setOnDismissListener(null);
        }
        dismissDialog();
    }

    public void dismissDialog() {
        if (alertDialog == null) {
            return;
        }
        if (alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
        alertDialog = null;
    }


    /* Functional interface */

    @FunctionalInterface
    public interface DoOnDismiss {
        void doOnDismiss();
    }
}
