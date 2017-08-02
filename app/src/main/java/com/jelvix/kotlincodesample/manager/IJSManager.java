package com.jelvix.kotlincodesample.manager;

import android.webkit.WebView;

import io.reactivex.Single;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public interface IJSManager {

    interface OnLoginFailedCallback {
        void onLoginFailed();
    }

    Single<String> login(String login, String password, WebView webView, OnLoginFailedCallback onLoginFailedCallback);
}
