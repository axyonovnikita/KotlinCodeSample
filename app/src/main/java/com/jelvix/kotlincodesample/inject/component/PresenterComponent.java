package com.jelvix.kotlincodesample.inject.component;

import com.jelvix.kotlincodesample.inject.module.JSModule;
import com.jelvix.kotlincodesample.inject.scope.PresenterScope;
import com.jelvix.kotlincodesample.ui.activity.base.presenter.BaseActivityPresenter;
import com.jelvix.kotlincodesample.ui.activity.login.presenter.LoginPresenter;
import com.jelvix.kotlincodesample.ui.activity.root.presenter.RootPresenter;
import com.jelvix.kotlincodesample.ui.fragment.dialog.presenter.UnfollowDialogPresenter;
import com.jelvix.kotlincodesample.ui.fragment.root.presenter.CommentsPresenter;
import com.jelvix.kotlincodesample.ui.fragment.root.presenter.ProfilePresenter;
import com.jelvix.kotlincodesample.ui.fragment.root.presenter.RelationPresenter;

import dagger.Component;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

@Component(dependencies = AppComponent.class, modules = JSModule.class)
@PresenterScope
public interface PresenterComponent {

    void inject(BaseActivityPresenter baseActivityPresenter);

    void inject(LoginPresenter loginPresenter);

    void inject(UnfollowDialogPresenter unfollowDialogPresenter);

    void inject(RootPresenter rootPresenter);

    void inject(ProfilePresenter profilePresenter);

    void inject(RelationPresenter relationPresenter);

    void inject(CommentsPresenter commentsPresenter);

}
