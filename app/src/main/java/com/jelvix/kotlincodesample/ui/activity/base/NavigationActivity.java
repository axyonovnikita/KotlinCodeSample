package com.jelvix.kotlincodesample.ui.activity.base;

import android.os.Bundle;

import javax.inject.Inject;

import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.Router;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public abstract class NavigationActivity extends BaseComponentActivity {

    @Inject
    protected NavigatorHolder navigatorHolder;
    @Inject
    protected Router router;


    /* lifecycle */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigatorHolder.setNavigator(getNavigator());
    }

    @Override
    protected void onPause() {
        navigatorHolder.removeNavigator();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        router.exit();
    }


    /* navigation */

    protected abstract Navigator getNavigator();
}
