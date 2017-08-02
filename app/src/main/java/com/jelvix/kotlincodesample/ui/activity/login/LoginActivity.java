package com.jelvix.kotlincodesample.ui.activity.login;

import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.jelvix.kotlincodesample.R;
import com.jelvix.kotlincodesample.api.api.entity.User;
import com.jelvix.kotlincodesample.ui.activity.base.NavigationActivity;
import com.jelvix.kotlincodesample.ui.activity.login.presenter.ILoginView;
import com.jelvix.kotlincodesample.ui.activity.login.presenter.LoginPresenter;
import com.jelvix.kotlincodesample.ui.activity.navigation.Screens;
import com.jelvix.kotlincodesample.ui.activity.navigation.login.LoginNavigator;
import com.jelvix.kotlincodesample.util.CheckNull;
import com.jelvix.kotlincodesample.util.CircleTransformation;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import ru.terrakok.cicerone.Navigator;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

@EActivity(R.layout.activity_login)
public class LoginActivity extends NavigationActivity implements ILoginView {

    @InjectPresenter
    LoginPresenter loginPresenter;

    @ViewById(R.id.unauthorized_view)
    protected View unauthorized_view;
    @ViewById(R.id.authorized_view)
    protected View authorized_view;
    @ViewById(R.id.userName_editText)
    protected EditText userName_editText;
    @ViewById(R.id.password_editText)
    protected EditText password_editText;
    @ViewById(R.id.progressBar)
    protected ProgressBar progressBar;
    @ViewById(R.id.userAvatar_imageView)
    protected ImageView userAvatar_imageView;
    @ViewById(R.id.changeUsername_textView)
    protected TextView changeUsername_textView;
    @ViewById(R.id.chooseAccount_floatingActionButton)
    protected FloatingActionButton chooseAccount_floatingActionButton;
    @ViewById(R.id.login_button)
    protected Button login_button;
    @ViewById(R.id.login_webView)
    protected WebView login_webView;
    @ViewById(R.id.loginFailed_view)
    protected View loginFailed_view;


    /* navigation */

    @Override
    protected Navigator getNavigator() {
        return new LoginNavigator(this);
    }


    /* lifecycle */

    @Override
    public void onBackPressed() {
        if (loginFailed_view.isShown()) {
            hideErrorLoginMessage();
        } else {
            super.onBackPressed();
        }
    }

    /* views */

    @Click(R.id.login_button)
    protected void login_button_click() {
        if (isValidCredentials() || loginPresenter.isSignedIn()) {
            loginPresenter.login(userName_editText.getText().toString(), password_editText.getText().toString(),
                    login_webView, () -> loginFailed_view.setVisibility(View.VISIBLE));
        }
    }

    @Click(R.id.changeAccount_textView)
    protected void changeAccount_textView_click() {
        loginPresenter.changeAccount();
    }

    @Click({R.id.chooseAccount_floatingActionButton, R.id.loginError_textView})
    protected void chooseAccount_views_click() {
        animateChooseAccountButton(true);
        router.navigateTo(Screens.FRAGMENT_DIALOG_DEFAULT_USER);
    }


    /* public methods */

    @Override
    public void showProgressDialog() {
        progressBar.setVisibility(View.VISIBLE);
        userName_editText.setEnabled(false);
        password_editText.setEnabled(false);
        login_button.setEnabled(false);
    }

    @Override
    public void hideProgressDialog() {
        progressBar.setVisibility(View.INVISIBLE);
        userName_editText.setEnabled(true);
        password_editText.setEnabled(true);
        login_button.setEnabled(true);
    }

    @Override
    public void tuneScreen(boolean isSignedIn, User user) {
        unauthorized_view.setVisibility(isSignedIn ? View.GONE : View.VISIBLE);
        chooseAccount_floatingActionButton.setVisibility(isSignedIn ? View.GONE : View.VISIBLE);
        authorized_view.setVisibility(isSignedIn ? View.VISIBLE : View.GONE);
        if (isSignedIn) {
            CheckNull.check(user.getProfilePicture(), avatarUrl -> {
                Picasso.with(this).load(avatarUrl)
                        .fit()
                        .centerCrop()
                        .noPlaceholder()
                        .transform(new CircleTransformation())
                        .into(userAvatar_imageView);
            });
            String username = user.getUsername();
            login_button.setText(getString(R.string.login_as, username));
            changeUsername_textView.setText(getString(R.string.not_username, username));
        } else {
            login_button.setText(R.string.sign_in_with_instagram);
        }
    }

    public void animateChooseAccountButton(boolean isNeedOpen) {
        chooseAccount_floatingActionButton.startAnimation(AnimationUtils.loadAnimation(this, isNeedOpen ? R.anim.rotate_open_animation : R.anim.rotate_close_animation));
    }

    public void setCredentials(String username, String password) {
        hideErrorLoginMessage();
        userName_editText.setText(username);
        password_editText.setText(password);
    }


    /* private methods */

    private void hideErrorLoginMessage() {
        loginFailed_view.setVisibility(View.GONE);
        loginPresenter.interruptLoginProcess();
    }

    private boolean isValidCredentials() {
        boolean isEmptyUserName = userName_editText.getEditableText().toString().isEmpty();
        if (isEmptyUserName) {
            userName_editText.setError(getString(R.string.empty_username));
        }
        boolean isEmptyPassword = password_editText.getEditableText().toString().isEmpty();
        if (isEmptyPassword) {
            password_editText.setError(getString(R.string.empty_password));
        }
        return !isEmptyUserName && !isEmptyPassword;
    }
}
