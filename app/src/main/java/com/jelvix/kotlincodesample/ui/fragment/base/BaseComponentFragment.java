package com.jelvix.kotlincodesample.ui.fragment.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.jelvix.kotlincodesample.presenter.base.IView;
import com.jelvix.kotlincodesample.ui.activity.base.BaseComponentActivity;
import com.jelvix.kotlincodesample.ui.activity.navigation.INavigatorView;
import com.jelvix.kotlincodesample.util.bean.Alert;
import com.jelvix.kotlincodesample.util.bean.AlertDialogHelper;

import io.reactivex.disposables.Disposable;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public abstract class BaseComponentFragment<T extends INavigatorView> extends MvpAppCompatFragment implements IView {

    private T navigatorView;


    /* lifecycle */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        navigatorView = null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        FragmentActivity activity = getActivity();
        if (activity instanceof INavigatorView) {
            navigatorView = (T) activity;
        }
    }


    /* public methods */

    public T getNavigatorView() {
        return navigatorView;
    }

    @Override
    public void setAlertDismissListener(AlertDialogHelper.DoOnDismiss doOnDismiss) {
        getBaseComponentActivity().setAlertDismissListener(doOnDismiss);
    }

    @Override
    public void handleError(Throwable e) {
        getBaseComponentActivity().handleError(e);
    }

    @Override
    public void showAlertDialog(String message) {
        getBaseComponentActivity().showAlertDialog(message);
    }

    @Override
    public void showAlertDialog(Alert alert) {
        getBaseComponentActivity().showAlertDialog(alert);
    }

    @Override
    public void hideAlertDialog() {
        getBaseComponentActivity().hideAlertDialog();
    }

    @Override
    public void showProgressDialog() {
        getBaseComponentActivity().showProgressDialog();
    }

    @Override
    public void showProgressDialogWithDisposableCancellation(Disposable disposable) {
        getBaseComponentActivity().showProgressDialogWithDisposableCancellation(disposable);
    }

    @Override
    public void hideProgressDialog() {
        ((BaseComponentActivity) getActivity()).hideProgressDialog();
    }

    private BaseComponentActivity getBaseComponentActivity() {
        FragmentActivity activity = getActivity();
        if (activity instanceof  BaseComponentActivity) {
            return (BaseComponentActivity) activity;
        } else {
            throw new RuntimeException("BaseComponentFragment must be used in BaseComponentActivity");
        }
    }
}
