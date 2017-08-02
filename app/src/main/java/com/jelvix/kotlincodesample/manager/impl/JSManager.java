package com.jelvix.kotlincodesample.manager.impl;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jelvix.kotlincodesample.R;
import com.jelvix.kotlincodesample.api.ApiConstants;
import com.jelvix.kotlincodesample.api.api.exception.ApiException;
import com.jelvix.kotlincodesample.api.api.exception.ErrorDetails;
import com.jelvix.kotlincodesample.manager.IJSManager;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public class JSManager implements IJSManager {

    private final static String JS_INTERFACE = "JSInterface";

    private Context context;

    public JSManager(Context context) {
        this.context = context;
    }

    @Override
    public Single<String> login(String login, String password, WebView webView, OnLoginFailedCallback onLoginFailedCallback) {
        Single<String> loginSingle = Single.create(observableEmitter -> {
            clearCookies();
            webView.getSettings().setJavaScriptEnabled(true);
            AuthWebViewClient authWebViewClient = new AuthWebViewClient(observableEmitter, login, password, context.getString(R.string.try_later), onLoginFailedCallback);
            webView.setWebViewClient(authWebViewClient);
            JavaScriptInterface javaScriptInterface = new JavaScriptInterface(observableEmitter);
            webView.addJavascriptInterface(javaScriptInterface, JS_INTERFACE);
            webView.loadUrl(ApiConstants.URL_AUTH_FULL);
        });
        return loginSingle
                .doOnDispose(this::clearCookies)
                .doFinally(this::clearCookies);
    }

    @SuppressWarnings("deprecation")
    private void clearCookies() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
        } else {
            CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(context);
            cookieSyncManager.startSync();
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
            cookieSyncManager.stopSync();
            cookieSyncManager.sync();
        }
    }


    /* inner classes */

    public class JavaScriptInterface {

        private SingleEmitter<String> emitter;

        public JavaScriptInterface(SingleEmitter<String> emitter) {
            this.emitter = emitter;
        }

        @JavascriptInterface
        public void parseError(String error) {
            emitter.onError(new ApiException(ApiException.CODE_BAD_CREDENTIALS, new ErrorDetails(ApiException.CODE_BAD_CREDENTIALS, error)));
        }
    }

    public static class AuthWebViewClient extends WebViewClient {

        private SingleEmitter<String> emitter;
        private String login;
        private String password;
        private int pageCount;
        private boolean isSuccess;
        private String defaultErrorMessage;
        private OnLoginFailedCallback onLoginFailedCallback;

        public AuthWebViewClient(SingleEmitter<String> emitter, String login, String password, String defaultErrorMessage, OnLoginFailedCallback onLoginFailedCallback) {
            this.emitter = emitter;
            this.login = login;
            this.password = password;
            this.defaultErrorMessage = defaultErrorMessage;
            this.onLoginFailedCallback = onLoginFailedCallback;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (isSuccess) {
                super.onPageFinished(view, url);
                return;
            } else if (pageCount >= 2) {
                view.loadUrl(getErrorHandlingScript());
            } else if (url.startsWith(ApiConstants.URL_INSTAGRAM_LOGIN)) {
                ++pageCount;
                view.loadUrl(getAuthenticationScript(login, password));
            } else if (url.startsWith(ApiConstants.URL_INSTAGRAM_AUTHORIZE)) {
                ++pageCount;
                view.loadUrl(getAuthorizeScript());
            } else {
                onLoginFailedCallback.onLoginFailed();
            }
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            emitter.onError(ApiException.generateNoInternetError());
            super.onReceivedError(view, request, error);
        }

        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return handleUri(url);
        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return handleUri(request.getUrl().toString());
        }

        private boolean handleUri(String url) {
            if (url.startsWith(ApiConstants.URL_CALLBACK)) {
                isSuccess = true;
                Uri uri = Uri.parse(url);
                String fragment = uri.getFragment();
                if (fragment != null) {
                    String parts[] = fragment.split("=");
                    if (parts[0].equals(ApiConstants.FRAGMENT_ACCESS_TOKEN)) {
                        String requestToken = parts[1];
                        emitter.onSuccess(requestToken);
                    } else {
                        emitter.onError(new ApiException(ApiException.CODE_BAD_CREDENTIALS, new ErrorDetails(ApiException.CODE_BAD_CREDENTIALS, defaultErrorMessage)));
                    }
                } else {
                    String error = uri.getQueryParameter(ApiConstants.QUERY_ERROR_DESCRIPTION);
                    if (error != null) {
                        emitter.onError(new ApiException(ApiException.CODE_BAD_CREDENTIALS, new ErrorDetails(ApiException.CODE_BAD_CREDENTIALS, error)));
                    } else {
                        emitter.onError(new ApiException(ApiException.CODE_BAD_CREDENTIALS, new ErrorDetails(ApiException.CODE_BAD_CREDENTIALS, defaultErrorMessage)));
                    }
                }
                return true;
            }
            return false;
        }

        private String getAuthenticationScript(String username, String password) {
            return "javascript:(function() { " +
                    "document.getElementById('id_username').value = '" + username + "';" +
                    "document.getElementById('id_password').value = '" + password + "';" +
                    "document.getElementById('login-form').submit(); " +
                    "})()";
        }

        private String getAuthorizeScript() {
            return "javascript:(function() { " +
                    "var confirm = document.getElementsByClassName('confirm')[0];" +
                    "if (confirm != null) {" +
                    "confirm.click();" +
                    "} else {" +
                    JS_INTERFACE +".parseError('You are not a sandbox user of this client');" +
                    "}})()";
        }

        private String getErrorHandlingScript() {
            return "javascript:(function() { " +
                    "var alerts = document.getElementById('alerts'); " +
                    "if (alerts != null) " +
                    JS_INTERFACE +".parseError(document.getElementById('alerts').getElementsByTagName('p')[0].innerHTML);" +
                    "else "+
                    JS_INTERFACE +".parseError('" + defaultErrorMessage + "');" +
                    "})()";
        }
    }
}
