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
import com.jelvix.kotlincodesample.ui.activity.navigation.root.ProfileRoute;
import com.jelvix.kotlincodesample.ui.activity.navigation.root.UnfollowRoute;
import com.jelvix.kotlincodesample.ui.adapter.RelationRecyclerViewAdapter;
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

@InjectViewState(view = IRelationView.class)
public class RelationPresenter extends RxBasePresenter<IRelationView> {

    @Inject
    IApiManager apiManager;
    @Inject
    IPreferenceManager preferenceManager;
    @Inject
    Context applicationContext;
    @Inject
    Router router;

    private int relationMode;
    protected Media media;

    private RelationRecyclerViewAdapter relationRecyclerViewAdapter;

    public RelationPresenter(int relationMode, Media media) {
        getPresenterComponent().inject(this);
        this.relationMode = relationMode;
        this.media = media;
        relationRecyclerViewAdapter = new RelationRecyclerViewAdapter(applicationContext, preferenceManager.getUserId(), relationHandler);
        getViewState().setRelationAdapter(relationRecyclerViewAdapter);
        subscribeToRxBus();
    }


    /* public methods */

    public Observable<List<User>> getRelations(PaginationInfo paginationInfo) {
        switch (relationMode) {
            case IRelationView.FOLLOWERS_SCREEN:
                return getFollowedUsers(paginationInfo);
            case IRelationView.FOLLOWING_SCREEN:
                return getFollowUsers(paginationInfo);
            case IRelationView.LIKES_SCREEN:
                return getLikedUsers(paginationInfo);
            default:
                throw new IllegalStateException("Unsupported screen format, use RelationMode constants");
        }
    }


    /* private methods */

    private Observable<List<User>> getFollowUsers(PaginationInfo paginationInfo) {
        return apiManager.getHimselfFollowsUsers(paginationInfo.getMaxId())
                .flatMap(userResponse -> {
                    relationRecyclerViewAdapter.updatePaginationKeys(userResponse.getPagination().getNextMaxId());
                    return updateUsersRelations(userResponse.getUsers());
                });
    }

    private Observable<List<User>> getFollowedUsers(PaginationInfo paginationInfo) {
        return apiManager.getHimselfFollowedByUsers(paginationInfo.getMaxId())
                .flatMap(userResponse -> {
                    relationRecyclerViewAdapter.updatePaginationKeys(userResponse.getPagination().getNextMaxId());
                    return updateUsersRelations(userResponse.getUsers());
                });
    }

    private Observable<List<User>> getLikedUsers(PaginationInfo paginationInfo) {
        return apiManager.getLikedUsers(media.getId(), paginationInfo.getMaxId())
                .flatMap(userResponse -> {
                    relationRecyclerViewAdapter.updatePaginationKeys(null);
                    return updateUsersRelations(userResponse.getUsers());
                });
    }

    private Observable<List<User>> updateUsersRelations(List<User> users) {
        return Observable.fromIterable(users)
                .flatMap(user -> apiManager.getRelationToUser(user.getId())
                        .map(RelationshipResponse::getRelationship)
                        .map(relationship -> {
                            user.setRelationship(relationship);
                            return user;
                        })
                        .toObservable())
                .toList()
                .toObservable();
    }

    private void followUser(User user) {
        addDisposable(start(Schedulers.io(), apiManager.followUser(user.getId()))
                .map(RelationshipResponse::getRelationship)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(relation -> {
                    user.updateRelationship(relation);
                    RxBus.getInstance().sendEvent(new RxBusEvent(RxBusEvent.RELATION_CHANGE, user));
                }, RelationPresenter.this::handleError));
    }

    private void subscribeToRxBus() {
        addDisposable(RxBus.getInstance().getEvents().observeOn(AndroidSchedulers.mainThread()).subscribe(subject -> {
            switch (subject.getEventKey()) {
                case RxBusEvent.RELATION_CHANGE:
                    User user = (User) subject.getEventObject();
                    relationRecyclerViewAdapter.updateRelation(user);
                    break;
            }
        }, this::handleError));
    }


    /* callbacks */

    private RelationRecyclerViewAdapter.RelationHandler relationHandler = new RelationRecyclerViewAdapter.RelationHandler() {
        @Override
        public void onItemClick(User user) {
            router.navigateTo(Screens.FRAGMENT_ROOT_PROFILE, new ProfileRoute(user));
        }

        @Override
        public void onFollowButtonClick(User user) {
            String outgoingStatus = user.getRelationship().getOutgoingStatus();
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
}
