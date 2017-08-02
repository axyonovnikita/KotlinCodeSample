package com.jelvix.kotlincodesample.presenter.base;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.MvpPresenter;
import com.jelvix.kotlincodesample.JelvixDemoApplication;
import com.jelvix.kotlincodesample.inject.component.DaggerPresenterComponent;
import com.jelvix.kotlincodesample.inject.component.PresenterComponent;
import com.jelvix.kotlincodesample.util.bean.Alert;

import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.CompletableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public abstract class RxBasePresenter<View extends IView> extends MvpPresenter<View> {

    private static final int API_CALL_SUBSCRIPTION_DELAY = 500;

    private static PresenterComponent presenterComponent = DaggerPresenterComponent.builder()
            .appComponent(JelvixDemoApplication.getAppComponent())
            .build();

    private CompositeDisposable compositeDisposable;

    public RxBasePresenter() {
        compositeDisposable = new CompositeDisposable();
    }


    /* dependencies injection */

    protected PresenterComponent getPresenterComponent() {
        return presenterComponent;
    }


    /* lifecycle */

    @Override
    @CallSuper
    public void onDestroy() {
        clear();
        compositeDisposable = null;
    }


    /* public methods */

    public void showProgressDialog() {
        getViewState().showProgressDialog();
    }

    public void showProgressDialogWithDisposableCancellation(Disposable disposable) {
        getViewState().showProgressDialogWithDisposableCancellation(disposable);
    }

    public void hideProgressDialog() {
        getViewState().hideProgressDialog();
    }

    public void handleError(Throwable throwable) {
        getViewState().setAlertDismissListener(this::hideAlertDialog);
        getViewState().handleError(throwable);
    }

    public void showAlertDialog(Alert alert) {
        getViewState().setAlertDismissListener(this::hideAlertDialog);
        getViewState().showAlertDialog(alert);
    }

    public void showAlertDialog(String message) {
        getViewState().setAlertDismissListener(this::hideAlertDialog);
        getViewState().showAlertDialog(message);
    }

    public void hideAlertDialog() {
        getViewState().setAlertDismissListener(null);
        getViewState().hideAlertDialog();
    }


    /* rx methods */

    protected final void clear() {
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }

    protected final void addDisposable(@NonNull Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    protected <T> Observable<T> start(@Nullable Scheduler scheduler, @NonNull Observable<T> observable) {
        if (scheduler != null) {
            observable = observable.subscribeOn(scheduler);
        }
        return observable;
    }

    protected <T> Observable<T> start(@Nullable Scheduler scheduler, @NonNull T value) {
        Observable<T> observable = Observable.just(value);
        if (scheduler != null) {
            observable = observable.subscribeOn(scheduler);
        }
        return observable;
    }

    protected Completable start(@Nullable Scheduler scheduler, @NonNull Completable completable) {
        if (scheduler != null) {
            completable = completable.subscribeOn(scheduler);
        }
        return completable;
    }

    protected <T> Single<T> start(@Nullable Scheduler scheduler, @NonNull Single<T> single) {
        if (scheduler != null) {
            single = single.subscribeOn(scheduler);
        }
        return single;
    }


    /* Observable transformers */

    public <T> ObservableTransformer<T, T> applyProgress(@Nullable Action beforeRun, @Nullable Action afterRun) {
        return upstream -> upstream.observeOn(AndroidSchedulers.mainThread())
                .delaySubscription(API_CALL_SUBSCRIPTION_DELAY, TimeUnit.MILLISECONDS)
                .doOnSubscribe(disposable -> {
                    if (beforeRun == null) {
                        showProgressDialog();
                    } else {
                        beforeRun.run();
                    }
                })
                .doOnTerminate(afterRun != null ? afterRun : getViewState()::hideProgressDialog);
    }

    public <T> ObservableTransformer<T, T> applyProgress() {
        return applyProgress(this::showProgressDialog, this::hideProgressDialog);
    }

    public <T> ObservableTransformer<T, T> applyProgressWithDisposableCancellation() {
        return upstream -> upstream.observeOn(AndroidSchedulers.mainThread())
                .delaySubscription(API_CALL_SUBSCRIPTION_DELAY, TimeUnit.MILLISECONDS)
                .doOnSubscribe(this::showProgressDialogWithDisposableCancellation)
                .doOnTerminate(getViewState()::hideProgressDialog);
    }


    /* Completable transformers */

    public CompletableTransformer applyCompletableProgress(@Nullable Action beforeRun, @Nullable Action afterRun) {
        return upstream -> upstream.observeOn(AndroidSchedulers.mainThread())
                .delay(API_CALL_SUBSCRIPTION_DELAY, TimeUnit.MILLISECONDS)
                .doOnSubscribe(disposable -> {
                    if (beforeRun == null) {
                        showProgressDialog();
                    } else {
                        beforeRun.run();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(afterRun != null ? afterRun : getViewState()::hideProgressDialog);
    }

    public CompletableTransformer applyCompletableProgress() {
        return applyCompletableProgress(this::showProgressDialog, this::hideProgressDialog);
    }


    /* Single transformers */

    public <T> SingleTransformer<T, T> applySingleProgress(@Nullable Action beforeRun, @Nullable Action afterRun) {
        return upstream -> upstream
                .delay(API_CALL_SUBSCRIPTION_DELAY, TimeUnit.MILLISECONDS)
                .doOnSubscribe(disposable -> {
                    if (beforeRun == null) {
                        showProgressDialog();
                    } else {
                        beforeRun.run();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEvent((t, e) -> {
                    if (afterRun != null) {
                        afterRun.run();
                    } else {
                        getViewState().hideProgressDialog();
                    }
                });
    }

    public <T> SingleTransformer<T, T> applySingleProgress() {
        return applySingleProgress(this::showProgressDialog, this::hideProgressDialog);
    }
}
