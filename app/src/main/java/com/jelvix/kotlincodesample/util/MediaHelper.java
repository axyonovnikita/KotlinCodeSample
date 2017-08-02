package com.jelvix.kotlincodesample.util;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

import com.jelvix.kotlincodesample.KotlinDemoApplication;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public class MediaHelper {

    public static int getDisplayWidth() {
        return getDisplayPoint().x;
    }

    public static int getDisplayHeight() {
        return getDisplayPoint().y;
    }

    private static Point getDisplayPoint() {
        Context applicationContext = KotlinDemoApplication.getAppComponent().provideContext();
        Display display = ((WindowManager) applicationContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        return point;
    }
}
