package com.jelvix.kotlincodesample.ui.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jelvix.kotlincodesample.R;
import com.jelvix.kotlincodesample.api.ApiConstants;
import com.jelvix.kotlincodesample.api.api.entity.User;
import com.jelvix.kotlincodesample.ui.adapter.RelationRecyclerViewAdapter;
import com.jelvix.kotlincodesample.util.CheckNull;
import com.jelvix.kotlincodesample.util.CircleTransformation;
import com.squareup.picasso.Picasso;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public class RelationHolder extends RecyclerView.ViewHolder {

    private ImageView userAvatar_imageView;
    private TextView userName_textView;
    private TextView fullName_textView;
    private Button follow_button;
    private Context context;

    public RelationHolder(View itemView) {
        super(itemView);
        userAvatar_imageView = (ImageView) itemView.findViewById(R.id.userAvatar_imageView);
        userName_textView = (TextView) itemView.findViewById(R.id.userName_textView);
        fullName_textView = (TextView) itemView.findViewById(R.id.fullName_textView);
        follow_button = (Button) itemView.findViewById(R.id.follow_button);
        context = itemView.getContext();
    }

    public void bind(User user, boolean isUserOwner, RelationRecyclerViewAdapter.RelationHandler relationHandler) {
        if (user == null) {
            return;
        }
        CheckNull.check(user.getProfilePicture(), avatarUrl -> {
            final int size = context.getResources().getDimensionPixelSize(R.dimen.user_avatar_size);
            Picasso.with(context).load(avatarUrl)
                    .resize(size, size)
                    .centerCrop()
                    .transform(new CircleTransformation())
                    .noPlaceholder()
                    .into(userAvatar_imageView);
        });
        userName_textView.setText(user.getUsername());
        fullName_textView.setText(user.getFullName());

        follow_button.setVisibility(isUserOwner ? View.INVISIBLE : View.VISIBLE);
        follow_button.setOnClickListener(isUserOwner ? null : v -> relationHandler.onFollowButtonClick(user));
        if (!isUserOwner) {
            switch (user.getRelationship().getOutgoingStatus()) {
                case ApiConstants.STATUS_FOLLOWS:
                    follow_button.setText(context.getString(R.string.following));
                    break;
                case ApiConstants.STATUS_NONE:
                    follow_button.setText(context.getString(R.string.follow));
                    break;
            }
        }
        itemView.setOnClickListener(v -> relationHandler.onItemClick(user));
    }
}
