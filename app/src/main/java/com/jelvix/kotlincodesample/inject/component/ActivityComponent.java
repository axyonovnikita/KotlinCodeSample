package com.jelvix.kotlincodesample.inject.component;

import com.jelvix.kotlincodesample.inject.scope.ActivityScope;
import com.jelvix.kotlincodesample.ui.activity.base.NavigationActivity;

import dagger.Component;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

@Component(dependencies = {AppComponent.class})
@ActivityScope
public interface ActivityComponent {

    void inject(NavigationActivity navigationActivity);
}
