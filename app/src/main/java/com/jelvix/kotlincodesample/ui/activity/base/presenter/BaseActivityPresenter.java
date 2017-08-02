package com.jelvix.kotlincodesample.ui.activity.base.presenter;


import com.jelvix.kotlincodesample.manager.IPreferenceManager;
import com.jelvix.kotlincodesample.presenter.base.RxBasePresenter;
import com.jelvix.kotlincodesample.ui.activity.base.IBaseActivityView;

import javax.inject.Inject;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public class BaseActivityPresenter extends RxBasePresenter<IBaseActivityView> {

    @Inject
    IPreferenceManager preferenceManager;

    public BaseActivityPresenter() {
        getPresenterComponent().inject(this);
    }

    public void logout() {
        preferenceManager.logout();
    }
}
