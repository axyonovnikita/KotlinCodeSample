package com.jelvix.kotlincodesample.ui.fragment.root;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.jelvix.kotlincodesample.R;
import com.jelvix.kotlincodesample.api.api.entity.Media;
import com.jelvix.kotlincodesample.api.api.entity.User;
import com.jelvix.kotlincodesample.ui.activity.root.IRootNavigator;
import com.jelvix.kotlincodesample.ui.adapter.RelationRecyclerViewAdapter;
import com.jelvix.kotlincodesample.ui.fragment.base.BaseComponentFragment;
import com.jelvix.kotlincodesample.ui.fragment.root.presenter.IRelationView;
import com.jelvix.kotlincodesample.ui.fragment.root.presenter.RelationPresenter;
import com.jelvix.kotlincodesample.ui.view.recycler.auto_loading.AutoLoadingRecyclerView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.ViewById;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

@EFragment(R.layout.fragment_relation)
public class RelationFragment extends BaseComponentFragment<IRootNavigator> implements IRelationView {

    @InstanceState
    @FragmentArg
    protected int relationMode;
    @InstanceState
    @FragmentArg
    protected Media media;

    @InjectPresenter
    RelationPresenter relationPresenter;

    @ProvidePresenter
    RelationPresenter provideRelationPresenter() {
        return new RelationPresenter(relationMode, media);
    }

    @ViewById(R.id.relation_recyclerView)
    protected AutoLoadingRecyclerView<User> relation_recyclerView;


    /* lifecycle */

    @AfterViews
    protected void afterViews() {
        getNavigatorView().changeAppBarState(true);
        switch (relationMode) {
            case IRelationView.FOLLOWERS_SCREEN:
                getNavigatorView().setTitle(getString(R.string.followers));
                break;
            case IRelationView.FOLLOWING_SCREEN:
                getNavigatorView().setTitle(getString(R.string.following));
                break;
            case IRelationView.LIKES_SCREEN:
                getNavigatorView().setTitle(getString(R.string.likes));
                break;
        }
    }


    /* public methods */

    @Override
    public void setRelationAdapter(RelationRecyclerViewAdapter relationAdapter) {
        relation_recyclerView.setAdapter(relationAdapter);
        relation_recyclerView.setItemAnimator(null);
        relation_recyclerView.startLoading(paginationInfo -> relationPresenter.getRelations(paginationInfo), throwable -> relationPresenter.handleError(throwable));
    }
}
