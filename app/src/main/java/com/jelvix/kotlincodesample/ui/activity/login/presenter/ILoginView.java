package com.jelvix.kotlincodesample.ui.activity.login.presenter;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.jelvix.kotlincodesample.api.api.entity.User;
import com.jelvix.kotlincodesample.presenter.base.IView;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public interface ILoginView extends IView {

    @StateStrategyType(AddToEndSingleStrategy.class)
    void tuneScreen(boolean isSignedIn, User user);
}
