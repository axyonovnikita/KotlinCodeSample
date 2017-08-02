package com.jelvix.kotlincodesample.util;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public class BitmapUtils {

    private BitmapUtils() {
        throw new InstantiationError();
    }

    public static Bitmap getTransparentPlaceholder(int width, int height) {
        Bitmap.Config conf = Bitmap.Config.ALPHA_8;
        Bitmap bmp = Bitmap.createBitmap(width, height, conf);
        bmp.eraseColor(Color.TRANSPARENT);
        return bmp;
    }
}