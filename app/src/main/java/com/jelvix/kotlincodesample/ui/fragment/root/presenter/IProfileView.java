package com.jelvix.kotlincodesample.ui.fragment.root.presenter;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.jelvix.kotlincodesample.api.api.entity.User;
import com.jelvix.kotlincodesample.presenter.base.IView;
import com.jelvix.kotlincodesample.ui.adapter.MediaRecyclerViewAdapter;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

@StateStrategyType(AddToEndSingleStrategy.class)
public interface IProfileView extends IView {

    void setMediaAdapter(MediaRecyclerViewAdapter mediaAdapter);

    void setLayoutManagerMode(boolean isFullMode);

    void onUserUpdate(User user);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void scrollToPosition(int position);
}
