package com.jelvix.kotlincodesample.api.api.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public class Media implements Parcelable {

    public static final int TYPE_IMAGE = 100;
    public static final int TYPE_VIDEO = 101;
    public static final int TYPE_CAROUSEL = 102;
    public static final int TYPE_IMAGE_SPAN = 200;
    public static final int TYPE_VIDEO_SPAN = 201;
    public static final int TYPE_CAROUSEL_SPAN = 202;

    private String id;
    private String type;
    private User user;
    private Image images;
    private Video videos;
    @SerializedName("carousel_media")
    private List<CarouselMedia> carouselMedias;
    @SerializedName("created_time")
    private Date createdTime;
    private Comment caption;
    @SerializedName("user_has_liked")
    private boolean isLiked;
    private CountEntity likes;
    private List<String> tags;
    private String filter;
    private CountEntity comments;
    @SerializedName("link")
    private String previewLink;
    private Location location;
    @SerializedName("users_in_photo")
    private List<UserInPhoto> usersInPhotos;
    private int savedPagerPosition;

    public Media() {
    }

    public void updateMedia(Media media) {
        this.id = media.getId();
        this.type = media.getType();
        this.user = media.getUser();
        this.images = media.getImages();
        this.videos = media.getVideos();
        this.carouselMedias = media.getCarouselMedias();
        this.createdTime = media.getCreatedTime();
        this.caption = media.getCaption();
        this.isLiked = media.isLiked();
        this.likes.setCount(media.getLikeCount());
        this.tags = media.getTags();
        this.filter = media.getFilter();
        this.comments = media.getComments();
        this.previewLink = media.getPreviewLink();
        this.location = media.getLocation();
        this.usersInPhotos = media.getUsersInPhotos();
        this.savedPagerPosition = media.getSavedPagerPosition();
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public User getUser() {
        return user;
    }

    public Image getImages() {
        return images;
    }

    public Video getVideos() {
        return videos;
    }

    public List<CarouselMedia> getCarouselMedias() {
        return carouselMedias;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public Comment getCaption() {
        return caption;
    }

    public void setCaption(Comment caption) {
        this.caption = caption;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public int getLikeCount() {
        return likes.getCount();
    }

    public void setLikeCount(int count) {
        likes.setCount(count);
    }

    public List<String> getTags() {
        return tags;
    }

    public String getFilter() {
        return filter;
    }

    public CountEntity getComments() {
        return comments;
    }

    public void setCommentCount(int count) {
        comments.setCount(count);
    }

    public String getPreviewLink() {
        return previewLink;
    }

    public Location getLocation() {
        return location;
    }

    public List<UserInPhoto> getUsersInPhotos() {
        return usersInPhotos;
    }

    public int getSavedPagerPosition() {
        return savedPagerPosition;
    }

    public void setSavedPagerPosition(int savedPagerPosition) {
        this.savedPagerPosition = savedPagerPosition;
    }

    public int getMediaType(boolean isFullMode) {
        switch (type) {
            case "image":
                return isFullMode ? TYPE_IMAGE : TYPE_IMAGE_SPAN;
            case "video":
                return isFullMode ? TYPE_VIDEO : TYPE_VIDEO_SPAN;
            case "carousel":
                return isFullMode ? TYPE_CAROUSEL : TYPE_CAROUSEL_SPAN;
            default:
                return -1;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Media media = (Media) obj;
        return id != null && media.id != null && id.equals(media.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.type);
        dest.writeParcelable(this.user, flags);
        dest.writeParcelable(this.images, flags);
        dest.writeParcelable(this.videos, flags);
        dest.writeTypedList(this.carouselMedias);
        dest.writeLong(this.createdTime != null ? this.createdTime.getTime() : -1);
        dest.writeParcelable(this.caption, flags);
        dest.writeByte(this.isLiked ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.likes, flags);
        dest.writeStringList(this.tags);
        dest.writeString(this.filter);
        dest.writeParcelable(this.comments, flags);
        dest.writeString(this.previewLink);
        dest.writeParcelable(this.location, flags);
        dest.writeTypedList(this.usersInPhotos);
        dest.writeInt(this.savedPagerPosition);
    }

    protected Media(Parcel in) {
        this.id = in.readString();
        this.type = in.readString();
        this.user = in.readParcelable(User.class.getClassLoader());
        this.images = in.readParcelable(Image.class.getClassLoader());
        this.videos = in.readParcelable(Video.class.getClassLoader());
        this.carouselMedias = in.createTypedArrayList(CarouselMedia.CREATOR);
        long tmpCreatedTime = in.readLong();
        this.createdTime = tmpCreatedTime == -1 ? null : new Date(tmpCreatedTime);
        this.caption = in.readParcelable(Comment.class.getClassLoader());
        this.isLiked = in.readByte() != 0;
        this.likes = in.readParcelable(CountEntity.class.getClassLoader());
        this.tags = in.createStringArrayList();
        this.filter = in.readString();
        this.comments = in.readParcelable(CountEntity.class.getClassLoader());
        this.previewLink = in.readString();
        this.location = in.readParcelable(Location.class.getClassLoader());
        this.usersInPhotos = in.createTypedArrayList(UserInPhoto.CREATOR);
        this.savedPagerPosition = in.readInt();
    }

    public static final Creator<Media> CREATOR = new Creator<Media>() {
        @Override
        public Media createFromParcel(Parcel source) {
            return new Media(source);
        }

        @Override
        public Media[] newArray(int size) {
            return new Media[size];
        }
    };
}
