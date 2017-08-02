package com.jelvix.kotlincodesample.manager.impl;

import com.jelvix.kotlincodesample.api.ApiConstants;
import com.jelvix.kotlincodesample.api.api.IApi;
import com.jelvix.kotlincodesample.api.api.entity.User;
import com.jelvix.kotlincodesample.api.api.entity.response.CommentResponse;
import com.jelvix.kotlincodesample.api.api.entity.response.CommentsResponse;
import com.jelvix.kotlincodesample.api.api.entity.response.LikeResponse;
import com.jelvix.kotlincodesample.api.api.entity.response.MediaResponse;
import com.jelvix.kotlincodesample.api.api.entity.response.RelationshipResponse;
import com.jelvix.kotlincodesample.api.api.entity.response.UserResponse;
import com.jelvix.kotlincodesample.api.api.entity.response.UsersResponse;
import com.jelvix.kotlincodesample.manager.IApiManager;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public class ApiManager implements IApiManager {

    private final IApi api;

    public ApiManager(IApi api) {
        this.api = api;
    }


    // User Endpoints

    @Override
    public Single<User> getHimself() {
        return api.getHimself()
                .map(UserResponse::getUser);
    }

    @Override
    public Single<User> getUser(String userId) {
        return api.getUser(userId)
                .map(UserResponse::getUser);
    }

    @Override
    public Observable<MediaResponse> getUserRecentMedia(String userId, String maxId) {
        return api.getUserRecentMedia(userId, maxId, ApiConstants.DEFAULT_LIMIT);
    }


    // Relationship

    @Override
    public Observable<UsersResponse> getHimselfFollowsUsers(String maxId) {
        return api.getHimselfFollowsUsers(maxId, ApiConstants.DEFAULT_LIMIT);
    }

    @Override
    public Observable<UsersResponse> getHimselfFollowedByUsers(String maxId) {
        return api.getHimselfFollowedByUsers(maxId, ApiConstants.DEFAULT_LIMIT);
    }

    @Override
    public Single<RelationshipResponse> getRelationToUser(String userId) {
        return api.getRelationToUser(userId);
    }

    @Override
    public Single<RelationshipResponse> followUser(String userId) {
        return api.changeRelationToUser(userId, ApiConstants.ACTION_FOLLOW);
    }

    @Override
    public Single<RelationshipResponse> unfollowUser(String userId) {
        return api.changeRelationToUser(userId, ApiConstants.ACTION_UNFOLLOW);
    }


    // Like

    @Override
    public Observable<UsersResponse> getLikedUsers(String mediaId, String maxId) {
        return api.getLikedUsers(mediaId, maxId, ApiConstants.DEFAULT_LIMIT);
    }

    @Override
    public Single<LikeResponse> likeMedia(String mediaId) {
        return api.likeMedia(mediaId);
    }

    @Override
    public Single<LikeResponse> disLikeMedia(String mediaId) {
        return api.disLikeMedia(mediaId);
    }


    // Comments

    @Override
    public Single<CommentsResponse> getMediaComments(String mediaId) {
        return api.getMediaComments(mediaId);
    }

    @Override
    public Single<CommentResponse> sendComment(String mediaId, String text) {
        return api.sendComment(mediaId, text);
    }

    @Override
    public Single<LikeResponse> deleteComment(String mediaId, String commentId) {
        return api.deleteComment(mediaId, commentId);
    }
}
