package com.jelvix.kotlincodesample.ui.activity.navigation.root;

import android.os.Parcel;
import android.os.Parcelable;

import com.jelvix.kotlincodesample.api.api.entity.User;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public class UnfollowRoute implements Parcelable {

    private User user;

    public UnfollowRoute(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.user, flags);
    }

    protected UnfollowRoute(Parcel in) {
        this.user = in.readParcelable(User.class.getClassLoader());
    }

    public static final Creator<UnfollowRoute> CREATOR = new Creator<UnfollowRoute>() {
        @Override
        public UnfollowRoute createFromParcel(Parcel source) {
            return new UnfollowRoute(source);
        }

        @Override
        public UnfollowRoute[] newArray(int size) {
            return new UnfollowRoute[size];
        }
    };
}