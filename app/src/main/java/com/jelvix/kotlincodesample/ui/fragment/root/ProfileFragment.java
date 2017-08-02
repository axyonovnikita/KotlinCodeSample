package com.jelvix.kotlincodesample.ui.fragment.root;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.jelvix.kotlincodesample.R;
import com.jelvix.kotlincodesample.api.api.entity.Media;
import com.jelvix.kotlincodesample.api.api.entity.User;
import com.jelvix.kotlincodesample.ui.activity.root.IRootNavigator;
import com.jelvix.kotlincodesample.ui.adapter.MediaRecyclerViewAdapter;
import com.jelvix.kotlincodesample.ui.fragment.base.BaseComponentFragment;
import com.jelvix.kotlincodesample.ui.fragment.root.presenter.IProfileView;
import com.jelvix.kotlincodesample.ui.fragment.root.presenter.ProfilePresenter;
import com.jelvix.kotlincodesample.ui.view.recycler.auto_loading.AutoLoadingRecyclerView;
import com.jelvix.kotlincodesample.util.autoplay.AutoPlayHelper;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.ViewById;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

@EFragment(R.layout.fragment_profile)
public class ProfileFragment extends BaseComponentFragment<IRootNavigator> implements IProfileView, IRootAppBarListener {

    @InstanceState
    @FragmentArg
    protected User user;

    @InjectPresenter
    ProfilePresenter profilePresenter;

    @ProvidePresenter
    ProfilePresenter provideProfilePresenter() {
        return new ProfilePresenter(user);
    }

    @ViewById(R.id.posts_swipeRefreshLayout)
    protected SwipeRefreshLayout posts_swipeRefreshLayout;
    @ViewById(R.id.posts_recyclerView)
    protected AutoLoadingRecyclerView<Media> posts_recyclerView;

    private AutoPlayHelper autoPlayHelper;


    /* lifecycle */

    @AfterViews
    protected void afterViews() {
        autoPlayHelper = new AutoPlayHelper();
        getNavigatorView().changeAppBarState(true);
        posts_swipeRefreshLayout.setOnRefreshListener(() -> {
            profilePresenter.updateAdapter();
            posts_recyclerView.reset();
            posts_swipeRefreshLayout.setRefreshing(false);
        });

        posts_recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    autoPlayHelper.onScrollStateChange(posts_recyclerView);
                }
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                autoPlayHelper.onScrollStateChange(posts_recyclerView);
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    @Override
    public void onDestroyView() {
        autoPlayHelper.release(posts_recyclerView);
        super.onDestroyView();
    }

    @Override
    public void onPause() {
        super.onPause();
        autoPlayHelper.stopPlaying();
    }

    @Override
    public void onStart() {
        super.onStart();
        autoPlayHelper.onScrollStateChange(posts_recyclerView);
    }


    /* public methods */

    @Override
    public void setMediaAdapter(MediaRecyclerViewAdapter mediaAdapter) {
        posts_recyclerView.setAdapter(mediaAdapter);
        posts_recyclerView.setItemAnimator(null);
        posts_recyclerView.startLoading(paginationInfo -> profilePresenter.getMedia(paginationInfo), throwable -> profilePresenter.handleError(throwable));
    }

    @Override
    public void setLayoutManagerMode(boolean isFullMode) {
        int spanCount = isFullMode ? MediaRecyclerViewAdapter.FULL_MODE_SPAN_COUNT : MediaRecyclerViewAdapter.SPAN_MODE_SPAN_COUNT;
        ((StaggeredGridLayoutManager) posts_recyclerView.getLayoutManager()).setSpanCount(spanCount);
    }

    @Override
    public void onUserUpdate(User user) {
        getNavigatorView().setTitle(user.getUsername());
    }

    @Override
    public void scrollToPosition(int position) {
        getNavigatorView().changeAppBarState(false);
        posts_recyclerView.post(() -> ((StaggeredGridLayoutManager) posts_recyclerView.getLayoutManager()).scrollToPositionWithOffset(position, 0));
    }

    @Override
    public void onAppBarDoubleClick() {
        getNavigatorView().changeAppBarState(true);
        posts_recyclerView.post(() -> ((StaggeredGridLayoutManager) posts_recyclerView.getLayoutManager()).scrollToPositionWithOffset(0, 0));
    }
}
