package com.jelvix.kotlincodesample.ui.fragment.root.presenter;

import android.content.Context;

import com.arellomobile.mvp.InjectViewState;
import com.jelvix.kotlincodesample.api.ApiConstants;
import com.jelvix.kotlincodesample.api.api.entity.Media;
import com.jelvix.kotlincodesample.api.api.entity.User;
import com.jelvix.kotlincodesample.api.api.entity.response.RelationshipResponse;
import com.jelvix.kotlincodesample.manager.IApiManager;
import com.jelvix.kotlincodesample.manager.IPreferenceManager;
import com.jelvix.kotlincodesample.presenter.base.RxBasePresenter;
import com.jelvix.kotlincodesample.ui.activity.navigation.Screens;
import com.jelvix.kotlincodesample.ui.activity.navigation.root.CommentsRoute;
import com.jelvix.kotlincodesample.ui.activity.navigation.root.RelationRoute;
import com.jelvix.kotlincodesample.ui.activity.navigation.root.UnfollowRoute;
import com.jelvix.kotlincodesample.ui.adapter.MediaRecyclerViewAdapter;
import com.jelvix.kotlincodesample.ui.adapter.holder.media.full.PostHolder;
import com.jelvix.kotlincodesample.ui.view.recycler.auto_loading.PaginationInfo;
import com.jelvix.kotlincodesample.util.bus.RxBus;
import com.jelvix.kotlincodesample.util.bus.RxBusEvent;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.terrakok.cicerone.Router;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

@InjectViewState(view = IProfileView.class)
public class ProfilePresenter extends RxBasePresenter<IProfileView> {

    @Inject
    IApiManager apiManager;
    @Inject
    IPreferenceManager preferenceManager;
    @Inject
    Context applicationContext;
    @Inject
    Router router;

    private User user;
    private MediaRecyclerViewAdapter mediaRecyclerViewAdapter;

    public ProfilePresenter(User user) {
        this.user = user;
        getPresenterComponent().inject(this);
        mediaRecyclerViewAdapter = new MediaRecyclerViewAdapter(applicationContext, preferenceManager.getUserId(), profileHandler, mediaHandler);
        updateUser();
        getViewState().setMediaAdapter(mediaRecyclerViewAdapter);
        subscribeToRxBus();
    }


    /* public methods */

    public Observable<List<Media>> getMedia(PaginationInfo paginationInfo) {
        return apiManager.getUserRecentMedia(user.getId(), paginationInfo.getMaxId())
                .map(mediaResponse -> {
                    mediaRecyclerViewAdapter.updatePaginationKeys(mediaResponse.getPagination().getNextMaxId());
                    return mediaResponse.getMedia();
                });
    }

    public void updateAdapter() {
        mediaRecyclerViewAdapter.clearItems();
        updateUser();
    }


    /* private methods */

    private void updateUser() {
        addDisposable(start(Schedulers.io(), apiManager.getUser(user.getId()))
                .flatMap(user -> apiManager.getRelationToUser(user.getId())
                        .map(RelationshipResponse::getRelationship)
                        .map(relationship -> {
                            user.setRelationship(relationship);
                            return user;
                        }))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(updatedUser -> {
                    this.user = updatedUser;
                    mediaRecyclerViewAdapter.updateUser(user);
                    getViewState().onUserUpdate(user);
                }, this::handleError));
    }

    private void followUser(User user) {
        addDisposable(start(Schedulers.io(), apiManager.followUser(user.getId()))
                .map(RelationshipResponse::getRelationship)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(relation -> {
                    user.updateRelationship(relation);
                    RxBus.getInstance().sendEvent(new RxBusEvent(RxBusEvent.RELATION_CHANGE, user));
                }, this::handleError));
    }

    private void changeMediaLikeState(Media media, PostHolder.LikeStatusHandler likeStatusHandler) {
        String mediaId = media.getId();
        boolean isLiked = media.isLiked();
        addDisposable(start(Schedulers.io(), isLiked ? apiManager.disLikeMedia(mediaId) : apiManager.likeMedia(mediaId))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ignore -> {
                    media.setLiked(!isLiked);
                    media.setLikeCount(isLiked ? media.getLikeCount() - 1 : media.getLikeCount() + 1);
                    mediaRecyclerViewAdapter.updateMedia(media);
                    likeStatusHandler.onLikeStatusChange(media, true);
                }, (throwable) -> {
                    likeStatusHandler.onLikeStatusChange(media, false);
                    handleError(throwable);
                }));
    }

    private void subscribeToRxBus() {
        addDisposable(RxBus.getInstance().getEvents().observeOn(AndroidSchedulers.mainThread()).subscribe(subject -> {
            switch (subject.getEventKey()) {
                case RxBusEvent.RELATION_CHANGE:
                    updateUser();
                    break;
                case RxBusEvent.COMMENTS_CHANGE:
                    Media media = (Media) subject.getEventObject();
                    mediaRecyclerViewAdapter.updateMedia(media);
                    mediaRecyclerViewAdapter.notifyDataSetChanged();
                    break;
            }
        }, this::handleError));
    }


    /* callbacks */

    private MediaRecyclerViewAdapter.ProfileHandler profileHandler = new MediaRecyclerViewAdapter.ProfileHandler() {
        @Override
        public void onPostsClick() {
            getViewState().scrollToPosition(1);
        }

        @Override
        public void onFollowersClick() {
            router.navigateTo(Screens.FRAGMENT_ROOT_RELATION, new RelationRoute(IRelationView.FOLLOWERS_SCREEN));
        }

        @Override
        public void onFollowingClick() {
            router.navigateTo(Screens.FRAGMENT_ROOT_RELATION, new RelationRoute(IRelationView.FOLLOWING_SCREEN));
        }

        @Override
        public void onChangeMode(boolean isFullMode) {
            mediaRecyclerViewAdapter.setMode(isFullMode);
            getViewState().setLayoutManagerMode(isFullMode);
        }

        @Override
        public void onChangeFollowingState(String outgoingStatus) {
            switch (outgoingStatus) {
                case ApiConstants.STATUS_FOLLOWS:
                    router.navigateTo(Screens.FRAGMENT_DIALOG_UNFOLLOW, new UnfollowRoute(user));
                    break;
                case ApiConstants.STATUS_NONE:
                    followUser(user);
                    break;
            }
        }
    };

    private MediaRecyclerViewAdapter.MediaHandler mediaHandler = new MediaRecyclerViewAdapter.MediaHandler() {
        @Override
        public void onSmallMediaClick(Media media) {
            mediaRecyclerViewAdapter.setMode(true);
            getViewState().setLayoutManagerMode(true);
            getViewState().scrollToPosition(mediaRecyclerViewAdapter.getItemPosition(media) + 1);
        }

        @Override
        public void onLikeCountClick(Media media) {
            router.navigateTo(Screens.FRAGMENT_ROOT_RELATION, new RelationRoute(media));
        }

        @Override
        public void onLikeClick(Media media, PostHolder.LikeStatusHandler likeStatusHandler) {
            changeMediaLikeState(media, likeStatusHandler);
        }

        @Override
        public void onCommentCountClick(Media media) {
            router.navigateTo(Screens.FRAGMENT_ROOT_COMMENTS, new CommentsRoute(media));
        }
    };
}
