package com.jelvix.kotlincodesample.api.api.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public class VideoState implements Parcelable {

    private boolean isUserStopVideo;
    private long savedVideoPosition;
    private boolean isNeedToPlay;
    private boolean isPrepared;
    private boolean isReadyToPlay;

    public VideoState() {
    }

    public boolean isUserStopVideo() {
        return isUserStopVideo;
    }

    public void setUserStopVideo(boolean userStopVideo) {
        isUserStopVideo = userStopVideo;
    }

    public long getSavedVideoPosition() {
        return savedVideoPosition;
    }

    public void setSavedVideoPosition(long savedVideoPosition) {
        this.savedVideoPosition = savedVideoPosition;
    }

    public boolean isNeedToPlay() {
        return isNeedToPlay;
    }

    public void setNeedToPlay(boolean needToPlay) {
        isNeedToPlay = needToPlay;
    }

    public boolean isPrepared() {
        return isPrepared;
    }

    public void setPrepared(boolean prepared) {
        isPrepared = prepared;
    }

    public boolean isReadyToPlay() {
        return isReadyToPlay;
    }

    public void setReadyToPlay(boolean readyToPlay) {
        isReadyToPlay = readyToPlay;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isUserStopVideo ? (byte) 1 : (byte) 0);
        dest.writeLong(this.savedVideoPosition);
        dest.writeByte(this.isNeedToPlay ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isPrepared ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isReadyToPlay ? (byte) 1 : (byte) 0);
    }

    protected VideoState(Parcel in) {
        this.isUserStopVideo = in.readByte() != 0;
        this.savedVideoPosition = in.readLong();
        this.isNeedToPlay = in.readByte() != 0;
        this.isPrepared = in.readByte() != 0;
        this.isReadyToPlay = in.readByte() != 0;
    }

    public static final Creator<VideoState> CREATOR = new Creator<VideoState>() {
        @Override
        public VideoState createFromParcel(Parcel source) {
            return new VideoState(source);
        }

        @Override
        public VideoState[] newArray(int size) {
            return new VideoState[size];
        }
    };
}
