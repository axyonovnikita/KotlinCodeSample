package com.jelvix.kotlincodesample.ui.adapter.holder;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jelvix.kotlincodesample.R;
import com.jelvix.kotlincodesample.api.ApiConstants;
import com.jelvix.kotlincodesample.api.api.entity.User;
import com.jelvix.kotlincodesample.ui.adapter.MediaRecyclerViewAdapter;
import com.jelvix.kotlincodesample.util.CheckNull;
import com.jelvix.kotlincodesample.util.CircleTransformation;
import com.squareup.picasso.Picasso;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public class ProfileHolder extends RecyclerView.ViewHolder {

    private View content_view;
    private ImageView userAvatar_imageView;
    private TextView fullName_textView;
    private TextView bio_textView;
    private TextView web_textView;
    private TextView postsCount_textView;
    private TextView followingCount_textView;
    private TextView followersCount_textView;
    private AppCompatImageView gridMode_imageView;
    private AppCompatImageView listMode_imageView;
    private Button follow_button;
    private View posts_view;
    private View followers_view;
    private View following_view;
    private ProgressBar progressBar;
    private Context context;

    public ProfileHolder(View itemView) {
        super(itemView);
        content_view = itemView.findViewById(R.id.content_view);
        userAvatar_imageView = (ImageView) itemView.findViewById(R.id.userAvatar_imageView);
        fullName_textView = (TextView) itemView.findViewById(R.id.fullName_textView);
        bio_textView = (TextView) itemView.findViewById(R.id.bio_textView);
        web_textView = (TextView) itemView.findViewById(R.id.web_textView);
        postsCount_textView = (TextView) itemView.findViewById(R.id.postsCount_textView);
        followingCount_textView = (TextView) itemView.findViewById(R.id.followingCount_textView);
        followersCount_textView = (TextView) itemView.findViewById(R.id.followersCount_textView);
        gridMode_imageView = (AppCompatImageView) itemView.findViewById(R.id.gridMode_imageView);
        listMode_imageView = (AppCompatImageView) itemView.findViewById(R.id.listMode_imageView);
        follow_button = (Button) itemView.findViewById(R.id.follow_button);
        posts_view = itemView.findViewById(R.id.posts_view);
        followers_view = itemView.findViewById(R.id.followers_view);
        following_view = itemView.findViewById(R.id.following_view);
        progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
        context = itemView.getContext();
    }

    public void bind(User user, String ownerId, boolean isFullMode, MediaRecyclerViewAdapter.ProfileHandler profileHandler) {
        if (user == null) {
            return;
        }
        content_view.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        CheckNull.check(user.getProfilePicture(), avatarUrl -> {
            final int size = context.getResources().getDimensionPixelSize(R.dimen.user_avatar_size);
            Picasso.with(context).load(avatarUrl)
                    .resize(size, size)
                    .centerCrop()
                    .transform(new CircleTransformation())
                    .noPlaceholder()
                    .into(userAvatar_imageView);
        });
        fullName_textView.setText(user.getFullName());
        bio_textView.setText(user.getBio());
        CheckNull.check(user.getWebsite(), web -> {
            web_textView.setText(web);
            web_textView.setVisibility(View.VISIBLE);
        }, () -> web_textView.setVisibility(View.GONE));
        CheckNull.check(user.getCounts(), counts -> {
            postsCount_textView.setText(String.valueOf(counts.getMedia()));
            followingCount_textView.setText(String.valueOf(counts.getFollows()));
            followersCount_textView.setText(String.valueOf(counts.getFollowedBy()));
        });

        posts_view.setOnClickListener(v -> profileHandler.onPostsClick());

        gridMode_imageView.setOnClickListener(v -> profileHandler.onChangeMode(false));
        listMode_imageView.setOnClickListener(v -> profileHandler.onChangeMode(true));

        boolean isUserOwner = ownerId.equals(user.getId());
        follow_button.setVisibility(isUserOwner ? View.INVISIBLE : View.VISIBLE);
        followers_view.setEnabled(isUserOwner);
        following_view.setEnabled(isUserOwner);
        followers_view.setOnClickListener(isUserOwner ? v -> profileHandler.onFollowersClick() : null);
        following_view.setOnClickListener(isUserOwner ? v -> profileHandler.onFollowingClick() : null);
        if (!isUserOwner) {
            switch (user.getRelationship().getOutgoingStatus()) {
                case ApiConstants.STATUS_FOLLOWS:
                    follow_button.setText(context.getString(R.string.following));
                    break;
                case ApiConstants.STATUS_NONE:
                    follow_button.setText(context.getString(R.string.follow));
                    break;
            }
            follow_button.setOnClickListener(v -> profileHandler.onChangeFollowingState(user.getRelationship().getOutgoingStatus()));
        }

        if (isFullMode) {
            gridMode_imageView.setColorFilter(ContextCompat.getColor(context, R.color.gray));
            listMode_imageView.setColorFilter(ContextCompat.getColor(context, R.color.black));
        } else {
            gridMode_imageView.setColorFilter(ContextCompat.getColor(context, R.color.black));
            listMode_imageView.setColorFilter(ContextCompat.getColor(context, R.color.gray));
        }
    }
}
