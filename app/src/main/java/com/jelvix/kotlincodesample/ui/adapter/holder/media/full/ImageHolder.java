package com.jelvix.kotlincodesample.ui.adapter.holder.media.full;

import android.content.Context;
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

public class ImageHolder extends PostHolder {

    private ImageView media_imageView;
    private Context context;

    public ImageHolder(View itemView) {
        super(itemView);
        media_imageView = (ImageView) itemView.findViewById(R.id.media_imageView);
        context = itemView.getContext();
    }

    @Override
    public void bind(Media media, MediaRecyclerViewAdapter.MediaHandler mediaHandler) {
        super.bind(media, mediaHandler);
        int mediaSize = MediaHelper.getDisplayWidth();
        ViewGroup.LayoutParams layoutParams = media_imageView.getLayoutParams();
        layoutParams.height = mediaSize;
        layoutParams.width = mediaSize;
        media_imageView.setImageBitmap(BitmapUtils.getTransparentPlaceholder(mediaSize, mediaSize));
        CheckNull.check(media.getImages().getStandardResolution().getUrl(), mediaUrl -> {
            Picasso.with(context).load(mediaUrl)
                    .fit()
                    .centerCrop()
                    .noPlaceholder()
                    .into(media_imageView);
        });
    }
}
