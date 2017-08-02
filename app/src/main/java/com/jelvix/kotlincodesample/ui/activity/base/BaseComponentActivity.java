package com.jelvix.kotlincodesample.ui.activity.base;

import android.content.Intent;
import android.os.Bundle;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.jelvix.kotlincodesample.BuildConfig;
import com.jelvix.kotlincodesample.JelvixDemoApplication;
import com.jelvix.kotlincodesample.R;
import com.jelvix.kotlincodesample.api.api.exception.ApiException;
import com.jelvix.kotlincodesample.inject.component.ActivityComponent;
import com.jelvix.kotlincodesample.inject.component.DaggerActivityComponent;
import com.jelvix.kotlincodesample.presenter.base.IView;
import com.jelvix.kotlincodesample.ui.activity.base.presenter.BaseActivityPresenter;
import com.jelvix.kotlincodesample.ui.activity.login.LoginActivity;
import com.jelvix.kotlincodesample.ui.activity.login.LoginActivity_;
import com.jelvix.kotlincodesample.util.bean.Alert;
import com.jelvix.kotlincodesample.util.bean.AlertDialogHelper;
import com.jelvix.kotlincodesample.util.bean.ProgressDialogHelper;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

import io.reactivex.disposables.Disposable;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

@EActivity
public abstract class BaseComponentActivity extends MvpAppCompatActivity implements IView, IBaseActivityView {

    private static ActivityComponent activityComponent = DaggerActivityComponent.builder()
            .appComponent(JelvixDemoApplication.getAppComponent())
            .build();

    @InjectPresenter
    BaseActivityPresenter baseActivityPresenter;

    @Bean
    protected ProgressDialogHelper progressDialogHelper;
    @Bean
    protected AlertDialogHelper alertDialogHelper;


    /* lifecycle */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        alertDialogHelper.dismissDialog(true);
        progressDialogHelper.dismissDialog();
    }


    /* dependencies injection */

    protected ActivityComponent getActivityComponent() {
        return activityComponent;
    }


    /* public methods */

    public void logout() {
        baseActivityPresenter.logout();
        if (!(this instanceof LoginActivity)) {
            LoginActivity_.intent(this)
                    .flags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    .start();
        }
    }

    @Override
    public void setAlertDismissListener(AlertDialogHelper.DoOnDismiss doOnDismiss) {
        alertDialogHelper.setDoOnDismiss(doOnDismiss);
    }

    @Override
    public void handleError(Throwable throwable) {
        Alert alert;
        if (throwable instanceof ApiException) {
            ApiException apiException = (ApiException) throwable;
            Alert.DoOnError doOnError = null;
            String message;
            switch (apiException.getErrorCode()) {
                case ApiException.CODE_NO_INTERNET:
                    message = getString(R.string.error_no_internet);
                    break;
                case ApiException.CODE_API_BAD_REQUEST:
                    switch (apiException.getErrorType()) {
                        case ApiException.TYPE_NO_AUTHORIZED:
                            message = apiException.getMessage();
                            doOnError = this::logout;
                            break;
                        default:
                            message = apiException.getMessage();
                    }
                    break;
                default:
                    message = apiException.getMessage();
            }
            alert = Alert.newBuilder().setDoOnError(doOnError).setMessage(message).create();
        } else {
            if (BuildConfig.DEBUG) {
                throwable.printStackTrace();
            }
            String errorMessage = throwable.getMessage() != null ? throwable.getMessage() : getString(R.string.error_unknown);
            alert = Alert.newBuilder().setMessage(errorMessage).create();
        }
        alertDialogHelper.showAlertDialog(alert);
    }

    @Override
    public void showAlertDialog(String message) {
        alertDialogHelper.showAlertDialog(message, null);
    }

    @Override
    public void showAlertDialog(Alert alert) {
        alertDialogHelper.showAlertDialog(alert);
    }

    @Override
    public void hideAlertDialog() {
        alertDialogHelper.dismissDialog();
    }

    @Override
    public void showProgressDialog() {
        progressDialogHelper.showProgressDialog();
    }

    @Override
    public void showProgressDialogWithDisposableCancellation(Disposable disposable) {
        progressDialogHelper.showProgressDialogWithDisposableCancellation(disposable);
    }

    @Override
    public void hideProgressDialog() {
        progressDialogHelper.dismissDialog();
    }
}
