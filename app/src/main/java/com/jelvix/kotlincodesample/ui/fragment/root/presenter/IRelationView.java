package com.jelvix.kotlincodesample.ui.fragment.root.presenter;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.jelvix.kotlincodesample.presenter.base.IView;
import com.jelvix.kotlincodesample.ui.adapter.RelationRecyclerViewAdapter;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

@StateStrategyType(AddToEndSingleStrategy.class)
public interface IRelationView extends IView {

    int FOLLOWERS_SCREEN = 0;
    int FOLLOWING_SCREEN = 1;
    int LIKES_SCREEN = 2;

    void setRelationAdapter(RelationRecyclerViewAdapter relationAdapter);
}
