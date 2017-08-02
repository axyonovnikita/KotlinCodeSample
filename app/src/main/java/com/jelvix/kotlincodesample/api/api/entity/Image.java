package com.jelvix.kotlincodesample.api.api.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public class Image implements Parcelable {

    private Size thumbnail;
    @SerializedName("lowResolution")
    private Size low_resolution;
    @SerializedName("standard_resolution")
    private Size standardResolution;

    public Image() {
    }

    public Size getThumbnail() {
        return thumbnail;
    }

    public Size getLow_resolution() {
        return low_resolution;
    }

    public Size getStandardResolution() {
        return standardResolution;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.thumbnail, flags);
        dest.writeParcelable(this.low_resolution, flags);
        dest.writeParcelable(this.standardResolution, flags);
    }

    protected Image(Parcel in) {
        this.thumbnail = in.readParcelable(Size.class.getClassLoader());
        this.low_resolution = in.readParcelable(Size.class.getClassLoader());
        this.standardResolution = in.readParcelable(Size.class.getClassLoader());
    }

    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel source) {
            return new Image(source);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };
}
