package com.jelvix.kotlincodesample.util;

import android.net.Uri;
import android.text.TextUtils;

import com.devbrackets.android.exomedia.listener.VideoControlsButtonListener;
import com.devbrackets.android.exomedia.ui.widget.VideoControls;
import com.devbrackets.android.exomedia.ui.widget.VideoView;
import com.jelvix.kotlincodesample.api.api.entity.Video;
import com.jelvix.kotlincodesample.api.api.entity.VideoState;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public class VideoHelper {

    public void setupVideoView(VideoView videoView, Video video) {
        VideoState videoState = video.getVideoState();
        VideoControls videoControls = videoView.getVideoControls();
        videoView.setReleaseOnDetachFromWindow(false);
        if (videoControls != null) {
            videoControls.setButtonListener(new VideoControlsButton(videoView, videoState));
        }
        videoView.setOnSeekCompletionListener(() -> {
            if (!videoState.isPrepared()) {
                return;
            }
            videoState.setReadyToPlay(true);
            if (videoState.isNeedToPlay() && !videoState.isUserStopVideo()) {
                videoView.start();
            } else {
                videoView.pause();
            }
        });
        videoView.setOnPreparedListener(() -> {
            videoState.setPrepared(true);
            long savedVideoPosition = videoState.getSavedVideoPosition();
            if (savedVideoPosition != 0) {
                videoState.setReadyToPlay(false);
                videoView.seekTo(savedVideoPosition);
            } else {
                videoState.setReadyToPlay(true);
                if (videoState.isNeedToPlay() && !videoState.isUserStopVideo()) {
                    videoView.start();
                } else {
                    videoView.pause();
                }
            }
        });
        videoView.setOnCompletionListener(() -> {
            videoState.setSavedVideoPosition(0);
            videoView.restart();
        });
        videoState.setPrepared(false);
        videoState.setReadyToPlay(false);
        String url = video.getStandardResolution().getUrl();
        videoView.setVideoURI(TextUtils.isEmpty(url) ? null : Uri.parse(url));
    }

    public void startPlay(VideoView videoView, Video video) {
        VideoState videoState = video.getVideoState();
        videoState.setNeedToPlay(true);
        if (videoView.isPlaying() || videoState.isUserStopVideo()) {
            return;
        }
        if (videoState.isReadyToPlay()) {
            videoView.start();
        }
    }

    public void stopPlay(VideoView videoView, Video video) {
        VideoState videoState = video.getVideoState();
        if (videoView.isPlaying()) {
            videoView.pause();
            if (videoState.isReadyToPlay()) {
                videoState.setSavedVideoPosition(videoView.getCurrentPosition());
            }
            videoState.setNeedToPlay(false);
        }
    }

    public void resetVideoView(VideoView videoView, Video video) {
        VideoState videoState = video.getVideoState();
        if (videoState.isReadyToPlay()) {
            videoState.setSavedVideoPosition(videoView.getCurrentPosition());
        }
        videoView.reset();
    }

    public void releaseVideoView(VideoView videoView, Video video) {
        VideoState videoState = video.getVideoState();
        if (videoState.isReadyToPlay()) {
            videoState.setSavedVideoPosition(videoView.getCurrentPosition());
        }
        videoView.release();
    }


    /* inner classes */

    private class VideoControlsButton implements VideoControlsButtonListener {
        private VideoView videoView;
        private VideoState videoState;

        private VideoControlsButton(VideoView videoView, VideoState videoState) {
            this.videoView = videoView;
            this.videoState = videoState;
        }

        @Override
        public boolean onPlayPauseClicked() {
            videoState.setUserStopVideo(videoView.isPlaying());
            return false;
        }

        @Override
        public boolean onPreviousClicked() {
            return false;
        }

        @Override
        public boolean onNextClicked() {
            return false;
        }

        @Override
        public boolean onRewindClicked() {
            return false;
        }

        @Override
        public boolean onFastForwardClicked() {
            return false;
        }
    }
}
