package com.jelvix.kotlincodesample.ui.adapter.holder.media;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jelvix.kotlincodesample.R;
import com.jelvix.kotlincodesample.api.api.entity.Media;
import com.jelvix.kotlincodesample.ui.adapter.MediaRecyclerViewAdapter;
import com.jelvix.kotlincodesample.util.BitmapUtils;
import com.jelvix.kotlincodesample.util.CheckNull;
import com.jelvix.kotlincodesample.util.MediaHelper;
import com.squareup.picasso.Picasso;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public class PostSpanHolder extends RecyclerView.ViewHolder {

    private ImageView thumbnail_imageView;
    private ImageView badge_imageView;
    private Context context;

    public PostSpanHolder(View itemView) {
        super(itemView);
        thumbnail_imageView = (ImageView) itemView.findViewById(R.id.thumbnail_imageView);
        badge_imageView = (ImageView) itemView.findViewById(R.id.badge_imageView);
        context = itemView.getContext();
    }

    public void bind(Media media, MediaRecyclerViewAdapter.MediaHandler mediaHandler) {
        int mediaSize = (MediaHelper.getDisplayWidth() / MediaRecyclerViewAdapter.SPAN_MODE_SPAN_COUNT) - itemView.getPaddingLeft() - itemView.getPaddingRight();
        ViewGroup.LayoutParams layoutParams = thumbnail_imageView.getLayoutParams();
        layoutParams.height = mediaSize;
        layoutParams.width = mediaSize;
        thumbnail_imageView.setImageBitmap(BitmapUtils.getTransparentPlaceholder(mediaSize, mediaSize));
        CheckNull.check(media.getImages().getStandardResolution().getUrl(), mediaUrl -> {
            Picasso.with(context).load(mediaUrl)
                    .fit()
                    .centerCrop()
                    .noPlaceholder()
                    .into(thumbnail_imageView);
        });
        switch (media.getMediaType(false)) {
            case Media.TYPE_IMAGE_SPAN:
                badge_imageView.setVisibility(View.INVISIBLE);
                break;
            case Media.TYPE_VIDEO_SPAN:
                badge_imageView.setImageResource(R.drawable.ic_video);
                badge_imageView.setVisibility(View.VISIBLE);
                break;
            case Media.TYPE_CAROUSEL_SPAN:
                badge_imageView.setImageResource(R.drawable.ic_carousel);
                badge_imageView.setVisibility(View.VISIBLE);
                break;
        }
        thumbnail_imageView.setOnClickListener(v -> mediaHandler.onSmallMediaClick(media));
    }
}
