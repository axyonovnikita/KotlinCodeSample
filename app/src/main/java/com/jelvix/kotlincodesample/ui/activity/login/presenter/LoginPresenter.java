package com.jelvix.kotlincodesample.ui.activity.login.presenter;

import android.webkit.WebView;

import com.arellomobile.mvp.InjectViewState;
import com.jelvix.kotlincodesample.manager.IApiManager;
import com.jelvix.kotlincodesample.manager.IJSManager;
import com.jelvix.kotlincodesample.manager.IPreferenceManager;
import com.jelvix.kotlincodesample.presenter.base.RxBasePresenter;
import com.jelvix.kotlincodesample.ui.activity.navigation.Screens;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.terrakok.cicerone.Router;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

@InjectViewState(view = ILoginView.class)
public class LoginPresenter extends RxBasePresenter<ILoginView> {

    @Inject
    IPreferenceManager preferenceManager;
    @Inject
    IApiManager apiManager;
    @Inject
    Router router;
    @Inject
    IJSManager jsManager;

    private Disposable loginDisposable;

    public LoginPresenter() {
        getPresenterComponent().inject(this);
        getViewState().tuneScreen(preferenceManager.isSignedIn(), preferenceManager.getProfile());
    }

    public boolean isSignedIn() {
        return preferenceManager.isSignedIn();
    }

    public void login(String login, String password, WebView webView, IJSManager.OnLoginFailedCallback onLoginFailedCallback) {
        if (preferenceManager.isSignedIn()) {
            addDisposable(start(Schedulers.io(), apiManager.getHimself())
                    .doOnSuccess(user -> preferenceManager.setProfile(user))
                    .compose(applySingleProgress())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(user -> router.navigateTo(Screens.ACTIVITY_ROOT), this::handleError));
        } else {
            loginDisposable = start(AndroidSchedulers.mainThread(), jsManager.login(login, password, webView, onLoginFailedCallback))
                    .observeOn(Schedulers.io())
                    .doOnSuccess(token -> preferenceManager.setToken(token))
                    .flatMap(token -> apiManager.getHimself())
                    .doOnSuccess(user -> preferenceManager.setProfile(user))
                    .compose(applySingleProgress())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(user -> router.navigateTo(Screens.ACTIVITY_ROOT), this::handleError);
            addDisposable(loginDisposable);
        }
    }

    public void changeAccount() {
        preferenceManager.logout();
        getViewState().tuneScreen(false, null);
    }

    public void interruptLoginProcess() {
        if (loginDisposable != null && !loginDisposable.isDisposed()) {
            loginDisposable.dispose();
            hideProgressDialog();
        }
    }
}
