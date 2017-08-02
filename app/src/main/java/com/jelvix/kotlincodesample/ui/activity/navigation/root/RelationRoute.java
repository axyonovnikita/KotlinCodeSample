package com.jelvix.kotlincodesample.ui.activity.navigation.root;

import android.os.Parcel;
import android.os.Parcelable;

import com.jelvix.kotlincodesample.api.api.entity.Media;
import com.jelvix.kotlincodesample.ui.fragment.root.presenter.IRelationView;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public class RelationRoute implements Parcelable {

    private int relationMode;
    private Media media;

    public RelationRoute(int relationMode) {
        this.relationMode = relationMode;
    }

    public RelationRoute(Media media) {
        this.relationMode = IRelationView.LIKES_SCREEN;
        this.media = media;
    }

    public int getRelationMode() {
        return relationMode;
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
        dest.writeInt(this.relationMode);
        dest.writeParcelable(this.media, flags);
    }

    protected RelationRoute(Parcel in) {
        this.relationMode = in.readInt();
        this.media = in.readParcelable(Media.class.getClassLoader());
    }

    public static final Creator<RelationRoute> CREATOR = new Creator<RelationRoute>() {
        @Override
        public RelationRoute createFromParcel(Parcel source) {
            return new RelationRoute(source);
        }

        @Override
        public RelationRoute[] newArray(int size) {
            return new RelationRoute[size];
        }
    };
}