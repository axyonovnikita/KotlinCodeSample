package com.jelvix.kotlincodesample.ui.view.recycler.auto_loading;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public class AutoLoadingRecyclerViewExceptions extends RuntimeException {

    public AutoLoadingRecyclerViewExceptions() {
        super("Exception in AutoLoadingRecyclerView");
    }

    public AutoLoadingRecyclerViewExceptions(String detailMessage) {
        super(detailMessage);
    }
}
