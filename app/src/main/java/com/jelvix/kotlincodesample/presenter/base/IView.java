package com.jelvix.kotlincodesample.presenter.base;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.jelvix.kotlincodesample.presenter.base.strategy.AlertStrategy;
import com.jelvix.kotlincodesample.presenter.base.strategy.ProgressDialogStrategy;
import com.jelvix.kotlincodesample.util.bean.Alert;
import com.jelvix.kotlincodesample.util.bean.AlertDialogHelper;

import io.reactivex.disposables.Disposable;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public interface IView extends MvpView {

    String ACTION_HIDE = "ACTION_HIDE";

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setAlertDismissListener(AlertDialogHelper.DoOnDismiss doOnDismiss);

    @StateStrategyType(AlertStrategy.class)
    void handleError(Throwable e);

    @StateStrategyType(AlertStrategy.class)
    void showAlertDialog(String message);

    @StateStrategyType(AlertStrategy.class)
    void showAlertDialog(Alert alert);

    @StateStrategyType(value = AlertStrategy.class, tag = ACTION_HIDE)
    void hideAlertDialog();

    @StateStrategyType(ProgressDialogStrategy.class)
    void showProgressDialog();

    @StateStrategyType(ProgressDialogStrategy.class)
    void showProgressDialogWithDisposableCancellation(Disposable disposable);

    @StateStrategyType(value = ProgressDialogStrategy.class, tag = ACTION_HIDE)
    void hideProgressDialog();
}
