package com.jelvix.kotlincodesample.api.api;

import com.jelvix.kotlincodesample.api.api.entity.response.CommentResponse;
import com.jelvix.kotlincodesample.api.api.entity.response.CommentsResponse;
import com.jelvix.kotlincodesample.api.api.entity.response.LikeResponse;
import com.jelvix.kotlincodesample.api.api.entity.response.MediaResponse;
import com.jelvix.kotlincodesample.api.api.entity.response.RelationshipResponse;
import com.jelvix.kotlincodesample.api.api.entity.response.UserResponse;
import com.jelvix.kotlincodesample.api.api.entity.response.UsersResponse;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public interface IApi {

    // User Endpoints

    @GET("users/self")
    Single<UserResponse> getHimself();

    @GET("users/{user_id}")
    Single<UserResponse> getUser(@Path("user_id") String userId);

    @GET("users/{user_id}/media/recent")
    Observable<MediaResponse> getUserRecentMedia(@Path("user_id") String userId, @Query("max_id") String maxId, @Query("count") Integer count);


    // Relationship

    @GET("users/self/follows")
    Observable<UsersResponse> getHimselfFollowsUsers(@Query("max_id") String maxId, @Query("count") Integer count);

    @GET("users/self/followed-by")
    Observable<UsersResponse> getHimselfFollowedByUsers(@Query("max_id") String maxId, @Query("count") Integer count);

    @GET("users/{user_id}/relationship")
    Single<RelationshipResponse> getRelationToUser(@Path("user_id") String userId);

    @POST("users/{user_id}/relationship")
    @FormUrlEncoded
    Single<RelationshipResponse> changeRelationToUser(@Path("user_id") String userId, @Field("action") String action);


    // Like

    @GET("media/{media_id}/likes")
    Observable<UsersResponse> getLikedUsers(@Path("media_id") String mediaId, @Query("max_id") String maxId, @Query("count") Integer count);

    @POST("media/{media_id}/likes")
    Single<LikeResponse> likeMedia(@Path("media_id") String mediaId);

    @DELETE("media/{media_id}/likes")
    Single<LikeResponse> disLikeMedia(@Path("media_id") String mediaId);


    // Comments

    @GET("media/{media_id}/comments")
    Single<CommentsResponse> getMediaComments(@Path("media_id") String mediaId);

    @POST("media/{media_id}/comments")
    @FormUrlEncoded
    Single<CommentResponse> sendComment(@Path("media_id") String mediaId, @Field("text") String text);

    @DELETE("media/{media_id}/comments/{comment_id}")
    Single<LikeResponse> deleteComment(@Path("media_id") String mediaId, @Path("comment_id") String commentId);
}
