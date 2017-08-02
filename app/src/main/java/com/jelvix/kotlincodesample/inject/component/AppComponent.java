package com.jelvix.kotlincodesample.inject.component;

import android.content.Context;
import android.content.res.Resources;

import com.jelvix.kotlincodesample.JelvixDemoApplication;
import com.jelvix.kotlincodesample.inject.module.ApiModule;
import com.jelvix.kotlincodesample.inject.module.AppModule;
import com.jelvix.kotlincodesample.inject.module.NavigationModule;
import com.jelvix.kotlincodesample.inject.module.PreferenceModule;
import com.jelvix.kotlincodesample.manager.IApiManager;
import com.jelvix.kotlincodesample.manager.IPreferenceManager;

import javax.inject.Singleton;

import dagger.Component;
import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.Router;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

@Singleton
@Component(modules = {AppModule.class, ApiModule.class, PreferenceModule.class, NavigationModule.class})
public interface AppComponent {

    void inject(JelvixDemoApplication application);

    Context provideContext();

    Resources provideResources();

    NavigatorHolder provideNavigatorHolder();

    Router provideRouter();

    IApiManager provideApiManager();

    IPreferenceManager providePreferenceManager();

}
