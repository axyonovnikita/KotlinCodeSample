package com.jelvix.kotlincodesample.ui.view.recycler;

import io.reactivex.Observable;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public interface PagingListener<T> {
    Observable<T> onNextPage(int offset);
}
