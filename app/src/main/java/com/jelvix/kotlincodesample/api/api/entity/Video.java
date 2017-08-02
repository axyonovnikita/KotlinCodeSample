package com.jelvix.kotlincodesample.api.api.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public class Video implements Parcelable {

    @SerializedName("low_bandwidth")
    private Size lowBandwidth;
    @SerializedName("lowResolution")
    private Size low_resolution;
    @SerializedName("standard_resolution")
    private Size standardResolution;
    private VideoState videoState = new VideoState();

    public Video() {
    }

    public Size getThumbnail() {
        return lowBandwidth;
    }

    public Size getLow_resolution() {
        return low_resolution;
    }

    public Size getStandardResolution() {
        return standardResolution;
    }

    public VideoState getVideoState() {
        return videoState;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.lowBandwidth, flags);
        dest.writeParcelable(this.low_resolution, flags);
        dest.writeParcelable(this.standardResolution, flags);
        dest.writeParcelable(this.videoState, flags);
    }

    protected Video(Parcel in) {
        this.lowBandwidth = in.readParcelable(Size.class.getClassLoader());
        this.low_resolution = in.readParcelable(Size.class.getClassLoader());
        this.standardResolution = in.readParcelable(Size.class.getClassLoader());
        this.videoState = in.readParcelable(VideoState.class.getClassLoader());
    }

    public static final Creator<Video> CREATOR = new Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel source) {
            return new Video(source);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };
}
