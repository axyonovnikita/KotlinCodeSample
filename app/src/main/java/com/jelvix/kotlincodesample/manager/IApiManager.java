package com.jelvix.kotlincodesample.manager;

import com.jelvix.kotlincodesample.api.api.entity.User;
import com.jelvix.kotlincodesample.api.api.entity.response.CommentResponse;
import com.jelvix.kotlincodesample.api.api.entity.response.CommentsResponse;
import com.jelvix.kotlincodesample.api.api.entity.response.LikeResponse;
import com.jelvix.kotlincodesample.api.api.entity.response.MediaResponse;
import com.jelvix.kotlincodesample.api.api.entity.response.RelationshipResponse;
import com.jelvix.kotlincodesample.api.api.entity.response.UsersResponse;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public interface IApiManager {

    // User Endpoints

    Single<User> getHimself();

    Single<User> getUser(String userId);

    Observable<MediaResponse> getUserRecentMedia(String userId, String maxId);


    // Relationship

    Observable<UsersResponse> getHimselfFollowsUsers(String maxId);

    Observable<UsersResponse> getHimselfFollowedByUsers(String maxId);

    Single<RelationshipResponse> getRelationToUser(String userId);

    Single<RelationshipResponse> followUser(String userId);

    Single<RelationshipResponse> unfollowUser(String userId);


    // Like

    Observable<UsersResponse> getLikedUsers(String mediaId, String maxId);

    Single<LikeResponse> likeMedia(String mediaId);

    Single<LikeResponse> disLikeMedia(String mediaId);


    // Comments

    Single<CommentsResponse> getMediaComments(String mediaId);

    Single<CommentResponse> sendComment(String mediaId, String text);

    Single<LikeResponse> deleteComment(String mediaId, String commentId);
}
