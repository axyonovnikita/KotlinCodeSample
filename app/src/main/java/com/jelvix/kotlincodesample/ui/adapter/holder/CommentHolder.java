package com.jelvix.kotlincodesample.ui.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jelvix.kotlincodesample.R;
import com.jelvix.kotlincodesample.api.api.entity.Comment;
import com.jelvix.kotlincodesample.api.api.entity.User;
import com.jelvix.kotlincodesample.ui.adapter.CommentsRecyclerViewAdapter;
import com.jelvix.kotlincodesample.util.CheckNull;
import com.jelvix.kotlincodesample.util.CircleTransformation;
import com.jelvix.kotlincodesample.util.DateTimeHelper;
import com.squareup.picasso.Picasso;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public class CommentHolder extends RecyclerView.ViewHolder {

    private ImageView avatar_imageView;
    private TextView userName_textView;
    private TextView text_textView;
    private TextView time_textView;
    private Context context;
    private boolean isCanDelete;

    public CommentHolder(View itemView) {
        super(itemView);
        avatar_imageView = (ImageView) itemView.findViewById(R.id.avatar_imageView);
        userName_textView = (TextView) itemView.findViewById(R.id.userName_textView);
        text_textView = (TextView) itemView.findViewById(R.id.text_textView);
        time_textView = (TextView) itemView.findViewById(R.id.time_textView);
        context = itemView.getContext();
    }

    public void bind(Comment comment, boolean isCanDelete, CommentsRecyclerViewAdapter.CommentHandler commentHandler) {
        this.isCanDelete = isCanDelete;
        User user = comment.getFrom();
        CheckNull.check(user.getProfilePicture(), avatarUrl -> {
            final int size = context.getResources().getDimensionPixelSize(R.dimen.user_avatar_size);
            Picasso.with(context).load(avatarUrl)
                    .resize(size, size)
                    .centerCrop()
                    .transform(new CircleTransformation())
                    .noPlaceholder()
                    .into(avatar_imageView);
        });
        userName_textView.setText(user.getUsername());
        text_textView.setText(comment.getText());
        time_textView.setText(DateTimeHelper.convertTime(comment.getCreatedTime()));
        avatar_imageView.setOnClickListener(v -> commentHandler.onAvatarClick(user));
    }

    public boolean isCanDelete() {
        return isCanDelete;
    }
}
