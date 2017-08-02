package com.jelvix.kotlincodesample.inject.module;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

@Module
public class AppModule {

    private final Application application;

    public AppModule(Application context) {
        this.application = context;
    }

    @Singleton
    @Provides
    Context provideContext() {
        return application.getApplicationContext();
    }

    @Singleton
    @Provides
    Resources provideResources() {
        return application.getResources();
    }

}