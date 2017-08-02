package com.jelvix.kotlincodesample.ui.fragment.base.dialog;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

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

public class BaseComponentDialogFragment<T extends INavigatorView> extends MvpDialogFragment implements IView {

    private T navigatorView;


    /* lifecycle */

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        FragmentActivity activity = getActivity();
        if (activity instanceof INavigatorView) {
            navigatorView = (T) activity;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        navigatorView = null;
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
            throw new RuntimeException("BaseComponentDialogFragment must be used in BaseComponentActivity");
        }
    }
}
