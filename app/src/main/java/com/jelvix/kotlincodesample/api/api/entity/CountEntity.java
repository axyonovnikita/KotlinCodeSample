package com.jelvix.kotlincodesample.api.api.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public class CountEntity implements Parcelable {

    private int count;

    public CountEntity() {
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.count);
    }

    protected CountEntity(Parcel in) {
        this.count = in.readInt();
    }

    public static final Creator<CountEntity> CREATOR = new Creator<CountEntity>() {
        @Override
        public CountEntity createFromParcel(Parcel source) {
            return new CountEntity(source);
        }

        @Override
        public CountEntity[] newArray(int size) {
            return new CountEntity[size];
        }
    };
}
