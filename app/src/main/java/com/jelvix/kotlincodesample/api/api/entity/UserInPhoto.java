package com.jelvix.kotlincodesample.api.api.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public class UserInPhoto implements Parcelable {

    private User user;
    private Position position;

    public UserInPhoto() {
    }

    public User getUser() {
        return user;
    }

    public Position getPosition() {
        return position;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.user, flags);
        dest.writeParcelable(this.position, flags);
    }

    protected UserInPhoto(Parcel in) {
        this.user = in.readParcelable(User.class.getClassLoader());
        this.position = in.readParcelable(Position.class.getClassLoader());
    }

    public static final Creator<UserInPhoto> CREATOR = new Creator<UserInPhoto>() {
        @Override
        public UserInPhoto createFromParcel(Parcel source) {
            return new UserInPhoto(source);
        }

        @Override
        public UserInPhoto[] newArray(int size) {
            return new UserInPhoto[size];
        }
    };
}
