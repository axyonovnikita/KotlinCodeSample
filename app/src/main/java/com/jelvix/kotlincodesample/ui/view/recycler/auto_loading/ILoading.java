package com.jelvix.kotlincodesample.ui.view.recycler.auto_loading;

import java.util.List;

import io.reactivex.Observable;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public interface ILoading<T> {

    Observable<List<T>> getLoadingObservable(PaginationInfo offsetAndLimit);

}
