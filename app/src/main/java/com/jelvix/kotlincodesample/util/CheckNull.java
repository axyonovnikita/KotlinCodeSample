package com.jelvix.kotlincodesample.util;

import android.support.annotation.NonNull;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public class CheckNull {

    @FunctionalInterface
    public interface OnAction<T> {
        void onAction(T argument);
    }

    @FunctionalInterface
    public interface OnNull {
        void onNull();
    }

    public static <T> void check(T argument, @NonNull OnAction<T> onAction) {
        if (argument != null) {
            onAction.onAction(argument);
        }
    }

    public static <T> void check(T argument, @NonNull OnAction<T> onAction, @NonNull OnNull onNull) {
        if (argument != null) {
            onAction.onAction(argument);
        } else {
            onNull.onNull();
        }
    }
}