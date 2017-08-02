package com.jelvix.kotlincodesample.ui.activity.navigation.root;

import android.os.Parcel;
import android.os.Parcelable;

import com.jelvix.kotlincodesample.api.api.entity.Media;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public class CommentsRoute implements Parcelable {

    private Media media;

    public CommentsRoute(Media media) {
        this.media = media;
    }

    public Media getMedia() {
        return media;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.media, flags);
    }

    protected CommentsRoute(Parcel in) {
        this.media = in.readParcelable(Media.class.getClassLoader());
    }

    public static final Creator<CommentsRoute> CREATOR = new Creator<CommentsRoute>() {
        @Override
        public CommentsRoute createFromParcel(Parcel source) {
            return new CommentsRoute(source);
        }

        @Override
        public CommentsRoute[] newArray(int size) {
            return new CommentsRoute[size];
        }
    };
}