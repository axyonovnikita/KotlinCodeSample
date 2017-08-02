package com.jelvix.kotlincodesample.util.bean;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.widget.ProgressBar;

import com.jelvix.kotlincodesample.R;

import org.androidannotations.annotations.EBean;

import io.reactivex.disposables.Disposable;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

@EBean
public class ProgressDialogHelper {

    private final Context context;

    public final String title;
    public final String message;

    private ProgressDialog dialog;

    public ProgressDialogHelper(Context context) {
        this.context = context;

        Resources resources = context.getResources();

        title = resources.getString(R.string.processing);
        message = resources.getString(R.string.please_wait);
    }

    public void showProgressDialog(String title, String message, Disposable disposable) {
        dismissDialog();

        dialog = ProgressDialog.show(context, title, message, true, true);
        if (disposable != null) {
            dialog.setOnDismissListener(dialog1 -> {
                if (!disposable.isDisposed()) {
                    disposable.dispose();
                }
            });
        }

        ProgressBar progressBar = (ProgressBar) dialog.findViewById(android.R.id.progress);
        Drawable drawable = progressBar.getIndeterminateDrawable();
        if (drawable != null)
            drawable.setColorFilter(ContextCompat.getColor(context, R.color.accent), PorterDuff.Mode.SRC_IN);
    }

    public void showProgressDialog(String title) {
        showProgressDialog(title, "", null);
    }

    public void showProgressDialog() {
        showProgressDialog(title, message, null);
    }

    public void showProgressDialogWithDisposableCancellation(Disposable disposable) {
        showProgressDialog(title, message, disposable);
    }

    public void dismissDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }
}