package com.jelvix.kotlincodesample.ui.view;

import android.view.MotionEvent;
import android.view.View;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public abstract class DoubleClickListener implements View.OnTouchListener {

    private static final long DOUBLE_CLICK_TIME_DELTA = 300;

    private long lastClickTime = 0;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            long clickTime = System.currentTimeMillis();
            if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
                onDoubleClick(v);
            }
            lastClickTime = clickTime;
        }
        return false;
    }

    public abstract void onDoubleClick(View v);
}
