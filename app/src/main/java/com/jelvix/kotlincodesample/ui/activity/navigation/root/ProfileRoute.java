package com.jelvix.kotlincodesample.ui.activity.navigation.root;

import android.os.Parcel;
import android.os.Parcelable;

import com.jelvix.kotlincodesample.api.api.entity.User;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public class ProfileRoute implements Parcelable {

    private User user;

    public ProfileRoute(User user) {
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

    protected ProfileRoute(Parcel in) {
        this.user = in.readParcelable(User.class.getClassLoader());
    }

    public static final Creator<ProfileRoute> CREATOR = new Creator<ProfileRoute>() {
        @Override
        public ProfileRoute createFromParcel(Parcel source) {
            return new ProfileRoute(source);
        }

        @Override
        public ProfileRoute[] newArray(int size) {
            return new ProfileRoute[size];
        }
    };
}