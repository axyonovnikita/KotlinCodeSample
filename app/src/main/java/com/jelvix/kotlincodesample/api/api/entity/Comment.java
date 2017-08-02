package com.jelvix.kotlincodesample.api.api.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public class Comment implements Parcelable {

    private String id;
    private String text;
    @SerializedName("from")
    private User from;
    @SerializedName("created_time")
    private Date createdTime;
    private boolean isCaption;

    public Comment() {
    }

    public void updateComment(Comment comment) {
        this.id = comment.getId();
        this.text = comment.getText();
        this.from = comment.getFrom();
        this.createdTime = comment.getCreatedTime();
        this.isCaption = comment.isCaption();
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public User getFrom() {
        return from;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public boolean isCaption() {
        return isCaption;
    }

    public void setIsCaption(boolean caption) {
        isCaption = caption;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Comment comment = (Comment) obj;
        return id != null && comment.id != null && id.equals(comment.id);
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
        dest.writeString(this.text);
        dest.writeParcelable(this.from, flags);
        dest.writeLong(this.createdTime != null ? this.createdTime.getTime() : -1);
        dest.writeByte(this.isCaption ? (byte) 1 : (byte) 0);
    }

    protected Comment(Parcel in) {
        this.id = in.readString();
        this.text = in.readString();
        this.from = in.readParcelable(User.class.getClassLoader());
        long tmpCreatedTime = in.readLong();
        this.createdTime = tmpCreatedTime == -1 ? null : new Date(tmpCreatedTime);
        this.isCaption = in.readByte() != 0;
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel source) {
            return new Comment(source);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };
}
