package com.jelvix.kotlincodesample.ui.adapter.holder.media.full;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.jelvix.kotlincodesample.R;
import com.jelvix.kotlincodesample.api.api.entity.Comment;
import com.jelvix.kotlincodesample.api.api.entity.Media;
import com.jelvix.kotlincodesample.api.api.entity.User;
import com.jelvix.kotlincodesample.ui.adapter.MediaRecyclerViewAdapter;
import com.jelvix.kotlincodesample.ui.view.DoubleClickListener;
import com.jelvix.kotlincodesample.util.BitmapUtils;
import com.jelvix.kotlincodesample.util.CheckNull;
import com.jelvix.kotlincodesample.util.CircleTransformation;
import com.jelvix.kotlincodesample.util.MediaHelper;
import com.jelvix.kotlincodesample.util.StringHelper;
import com.squareup.picasso.Picasso;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public class PostHolder extends RecyclerView.ViewHolder {

    private View fullLike_view;
    private ImageView fullLike_imageView;
    private ImageView avatar_imageView;
    private TextView userName_textView;
    private TextView location_textView;
    private ImageView like_imageView;
    private ImageView comment_imageView;
    private TextView likeCount_textView;
    private TextView caption_textView;
    private TextView commentCount_textView;
    private Context context;
    private Media media;

    public PostHolder(View itemView) {
        super(itemView);
        fullLike_view = itemView.findViewById(R.id.fullLike_view);
        fullLike_imageView = (ImageView) itemView.findViewById(R.id.fullLike_imageView);
        avatar_imageView = (ImageView) itemView.findViewById(R.id.avatar_imageView);
        userName_textView = (TextView) itemView.findViewById(R.id.userName_textView);
        location_textView = (TextView) itemView.findViewById(R.id.location_textView);
        like_imageView = (ImageView) itemView.findViewById(R.id.like_imageView);
        comment_imageView = (ImageView) itemView.findViewById(R.id.comment_imageView);
        likeCount_textView = (TextView) itemView.findViewById(R.id.likeCount_textView);
        caption_textView = (TextView) itemView.findViewById(R.id.caption_textView);
        commentCount_textView = (TextView) itemView.findViewById(R.id.commentCount_textView);
        context = itemView.getContext();
    }

    public void bind(Media media, MediaRecyclerViewAdapter.MediaHandler mediaHandler) {
        int mediaSize = MediaHelper.getDisplayWidth();
        this.media = media;
        ViewGroup.LayoutParams layoutParams = fullLike_view.getLayoutParams();
        layoutParams.height = mediaSize;
        layoutParams.width = mediaSize;

        int avatarSize = context.getResources().getDimensionPixelSize(R.dimen.user_avatar_size_small);
        User user = media.getUser();
        CheckNull.check(user.getProfilePicture(), avatarUrl -> {
            Picasso.with(context).load(avatarUrl)
                    .fit()
                    .centerCrop()
                    .noPlaceholder()
                    .transform(new CircleTransformation())
                    .into(avatar_imageView);
        }, () -> avatar_imageView.setImageBitmap(BitmapUtils.getTransparentPlaceholder(avatarSize, avatarSize)));

        userName_textView.setText(user.getUsername());

        CheckNull.check(media.getLocation(), location -> {
            location_textView.setVisibility(View.VISIBLE);
            location_textView.setText(location.getName());
        }, () -> location_textView.setVisibility(View.GONE));

        fullLike_imageView.setVisibility(View.GONE);
        changeLikeState(media);

        int commentCount = media.getComments().getCount();
        Comment caption = media.getCaption();
        if (caption != null) {
            caption_textView.setVisibility(View.VISIBLE);
            caption_textView.setText(StringHelper.makeCaption(caption.getText(), caption.getFrom().getUsername()));
            ++commentCount;
        } else {
            caption_textView.setVisibility(View.GONE);
        }

        if (commentCount > 0) {
            commentCount_textView.setVisibility(View.VISIBLE);
            commentCount_textView.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(context, R.drawable.ic_right), null);
            commentCount_textView.setText(String.format(context.getString(R.string.comment_count), commentCount));
        } else {
            commentCount_textView.setVisibility(View.GONE);
        }

        like_imageView.setOnClickListener(v -> {
            startProceedLikeAnimation();
            mediaHandler.onLikeClick(media, this::startLikeAnimation);
        });
        likeCount_textView.setOnClickListener(v -> mediaHandler.onLikeCountClick(media));
        commentCount_textView.setOnClickListener(v -> mediaHandler.onCommentCountClick(media));
        comment_imageView.setOnClickListener(v -> mediaHandler.onCommentCountClick(media));
        caption_textView.setOnClickListener(v -> mediaHandler.onCommentCountClick(media));
        fullLike_view.setOnTouchListener(new DoubleClickListener() {
            @Override
            public void onDoubleClick(View v) {
                startProceedLikeAnimation();
                mediaHandler.onLikeClick(media, (changedMedia, isSuccess) -> startLikeAnimation(media, isSuccess));
            }
        });
    }


    /* private methods */

    private void startProceedLikeAnimation() {
        like_imageView.setEnabled(false);
        fullLike_view.setEnabled(false);
        Animation proceedAnimation = AnimationUtils.loadAnimation(context, R.anim.like_proceed_start_animation);
        proceedAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //ignore
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                like_imageView.startAnimation(animation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                //ignore
            }
        });
        like_imageView.startAnimation(proceedAnimation);
    }

    private void startLikeAnimation(Media media, boolean isSuccess) {
        if (!this.media.equals(media)) {
            return;
        }
        Animation animation = like_imageView.getAnimation();
        if (animation != null) {
            animation.reset();
            animation.cancel();
        }
        like_imageView.clearAnimation();
        like_imageView.setEnabled(true);
        fullLike_view.setEnabled(true);
        if (!isSuccess) {
            return;
        }
        changeLikeState(media);
        fullLike_imageView.setVisibility(View.VISIBLE);
        Animation fullLikeAnimation = AnimationUtils.loadAnimation(context, R.anim.like_animation);
        fullLikeAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //ignore
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                fullLike_imageView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                //ignore
            }
        });
        fullLike_imageView.startAnimation(fullLikeAnimation);
        Animation likeAnimation = AnimationUtils.loadAnimation(context, R.anim.like_animation);
        like_imageView.startAnimation(likeAnimation);
    }

    private void changeLikeState(Media media) {
        if (media.isLiked()) {
            like_imageView.setColorFilter(ContextCompat.getColor(context, R.color.red));
            fullLike_imageView.setColorFilter(ContextCompat.getColor(context, R.color.red));
        } else {
            like_imageView.setColorFilter(ContextCompat.getColor(context, R.color.black));
            fullLike_imageView.setColorFilter(ContextCompat.getColor(context, R.color.black));
        }
        int likeCount = media.getLikeCount();
        if (likeCount > 0) {
            likeCount_textView.setVisibility(View.VISIBLE);
            likeCount_textView.setText(String.format(context.getString(R.string.likes_count), likeCount));
            likeCount_textView.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(context, R.drawable.ic_right), null);
        } else {
            likeCount_textView.setVisibility(View.GONE);
        }
    }


    /* inner classes */

    public interface LikeStatusHandler {
        void onLikeStatusChange(Media media, boolean isSuccess);
    }
}
