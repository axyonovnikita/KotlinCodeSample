package com.jelvix.kotlincodesample.api.api.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public class UserCounts implements Parcelable {

    private int media;
    private int follows;
    @SerializedName("followed_by")
    private int followedBy;

    public UserCounts() {
    }

    public int getMedia() {
        return media;
    }

    public int getFollows() {
        return follows;
    }

    public int getFollowedBy() {
        return followedBy;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.media);
        dest.writeInt(this.follows);
        dest.writeInt(this.followedBy);
    }

    protected UserCounts(Parcel in) {
        this.media = in.readInt();
        this.follows = in.readInt();
        this.followedBy = in.readInt();
    }

    public static final Creator<UserCounts> CREATOR = new Creator<UserCounts>() {
        @Override
        public UserCounts createFromParcel(Parcel source) {
            return new UserCounts(source);
        }

        @Override
        public UserCounts[] newArray(int size) {
            return new UserCounts[size];
        }
    };
}
