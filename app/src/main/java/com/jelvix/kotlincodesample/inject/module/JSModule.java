package com.jelvix.kotlincodesample.inject.module;

import android.content.Context;

import com.jelvix.kotlincodesample.manager.IJSManager;
import com.jelvix.kotlincodesample.manager.impl.JSManager;

import dagger.Module;
import dagger.Provides;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

@Module
public class JSModule {

    @Provides
    IJSManager provideJSManager(Context context) {
        return new JSManager(context);
    }
}
