package com.jelvix.kotlincodesample.inject.module;

import android.content.Context;

import com.jelvix.kotlincodesample.manager.IPreferenceManager;
import com.jelvix.kotlincodesample.manager.impl.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

@Module
public class PreferenceModule {

    @Singleton
    @Provides
    IPreferenceManager providePreferenceManager(Context context) {
        return new PreferenceManager(context);
    }
}
