package com.jelvix.kotlincodesample.api.api.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public class CarouselMedia implements Parcelable {

    private Image images;
    private Video videos;
    @SerializedName("users_in_photo")
    private List<UserInPhoto> usersInPhotos;
    private String type;

    public CarouselMedia() {
    }

    public Image getImages() {
        return images;
    }

    public Video getVideos() {
        return videos;
    }

    public List<UserInPhoto> getUsersInPhotos() {
        return usersInPhotos;
    }

    public String getType() {
        return type;
    }

    public int getMediaType() {
        switch (type) {
            case "image":
                return Media.TYPE_IMAGE;
            case "video":
                return Media.TYPE_VIDEO;
            default:
                return -1;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.images, flags);
        dest.writeParcelable(this.videos, flags);
        dest.writeTypedList(this.usersInPhotos);
        dest.writeString(this.type);
    }

    protected CarouselMedia(Parcel in) {
        this.images = in.readParcelable(Image.class.getClassLoader());
        this.videos = in.readParcelable(Video.class.getClassLoader());
        this.usersInPhotos = in.createTypedArrayList(UserInPhoto.CREATOR);
        this.type = in.readString();
    }

    public static final Creator<CarouselMedia> CREATOR = new Creator<CarouselMedia>() {
        @Override
        public CarouselMedia createFromParcel(Parcel source) {
            return new CarouselMedia(source);
        }

        @Override
        public CarouselMedia[] newArray(int size) {
            return new CarouselMedia[size];
        }
    };
}
