package com.jelvix.kotlincodesample.ui.fragment.root.presenter;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.jelvix.kotlincodesample.presenter.base.IView;
import com.jelvix.kotlincodesample.ui.adapter.CommentsRecyclerViewAdapter;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

@StateStrategyType(AddToEndSingleStrategy.class)
public interface ICommentsView extends IView {

    void setCommentsAdapter(CommentsRecyclerViewAdapter commentsAdapter);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void scrollToPosition(int position);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void onSendComment(int position, boolean isSuccess);
}
