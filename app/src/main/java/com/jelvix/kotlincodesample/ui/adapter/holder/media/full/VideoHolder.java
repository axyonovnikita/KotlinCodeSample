package com.jelvix.kotlincodesample.ui.adapter.holder.media.full;

import android.view.View;
import android.view.ViewGroup;

import com.devbrackets.android.exomedia.ui.widget.VideoView;
import com.jelvix.kotlincodesample.R;
import com.jelvix.kotlincodesample.api.api.entity.Media;
import com.jelvix.kotlincodesample.api.api.entity.Video;
import com.jelvix.kotlincodesample.ui.adapter.MediaRecyclerViewAdapter;
import com.jelvix.kotlincodesample.util.MediaHelper;
import com.jelvix.kotlincodesample.util.VideoHelper;
import com.jelvix.kotlincodesample.util.autoplay.IAutoPlayViewHolder;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public class VideoHolder extends PostHolder implements IAutoPlayViewHolder {

    private VideoView media_videoView;
    private Video video;
    private VideoHelper videoHelper = new VideoHelper();

    public VideoHolder(View itemView) {
        super(itemView);
        media_videoView = (VideoView) itemView.findViewById(R.id.media_videoView);
    }

    public void bind(Media media, MediaRecyclerViewAdapter.MediaHandler mediaHandler) {
        super.bind(media, mediaHandler);
        video = media.getVideos();
        int mediaSize = MediaHelper.getDisplayWidth();
        ViewGroup.LayoutParams layoutParams = media_videoView.getLayoutParams();
        layoutParams.height = mediaSize;
        layoutParams.width = mediaSize;
    }


    /* auto play methods */

    @Override
    public void startPlay() {
        videoHelper.startPlay(media_videoView, video);
    }

    @Override
    public void stopPlay() {
        videoHelper.stopPlay(media_videoView, video);
    }

    @Override
    public void release() {
        videoHelper.releaseVideoView(media_videoView, video);
    }

    @Override
    public void reset() {
        videoHelper.resetVideoView(media_videoView, video);
    }

    @Override
    public void setup() {
        videoHelper.setupVideoView(media_videoView, video);
    }
}
