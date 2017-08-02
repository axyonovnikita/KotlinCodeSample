package com.jelvix.kotlincodesample.ui.activity.root.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.jelvix.kotlincodesample.manager.IPreferenceManager;
import com.jelvix.kotlincodesample.presenter.base.RxBasePresenter;
import com.jelvix.kotlincodesample.ui.activity.navigation.Screens;
import com.jelvix.kotlincodesample.ui.activity.navigation.root.ProfileRoute;

import javax.inject.Inject;

import ru.terrakok.cicerone.Router;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

@InjectViewState(view = IRootView.class)
public class RootPresenter extends RxBasePresenter<IRootView> {

    @Inject
    Router router;
    @Inject
    IPreferenceManager preferenceManager;

    public RootPresenter() {
        getPresenterComponent().inject(this);
        ProfileRoute data = new ProfileRoute(preferenceManager.getProfile());
        router.newRootScreen(Screens.FRAGMENT_ROOT_PROFILE, data);
    }
}
