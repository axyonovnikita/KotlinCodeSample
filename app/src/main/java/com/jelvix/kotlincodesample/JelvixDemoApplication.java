package com.jelvix.kotlincodesample;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import com.arellomobile.mvp.MvpFacade;
import com.jelvix.kotlincodesample.inject.component.AppComponent;
import com.jelvix.kotlincodesample.inject.component.DaggerAppComponent;
import com.jelvix.kotlincodesample.inject.module.AppModule;
import com.squareup.leakcanary.LeakCanary;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public class JelvixDemoApplication extends Application {

    public static AppComponent appComponent;

    @Override
    public void onCreate() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        if (BuildConfig.DEBUG) {
            LeakCanary.install(this);
        }
        super.onCreate();
        initDaggerComponents();
        appComponent.inject(this);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        MvpFacade.init();
    }

    private void initDaggerComponents() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }
}
