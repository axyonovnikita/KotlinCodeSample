package com.jelvix.kotlincodesample.api.api.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public class User implements Parcelable {

    private String id;
    private String username;
    @SerializedName("full_name")
    private String fullName;
    @SerializedName("profile_picture")
    private String profilePicture;
    private String bio;
    private String website;
    private UserCounts counts;
    private Relationship relationship;

    public User() {
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public String getBio() {
        return bio;
    }

    public String getWebsite() {
        return website;
    }

    public UserCounts getCounts() {
        return counts;
    }

    public Relationship getRelationship() {
        return relationship;
    }

    public void setRelationship(Relationship relationship) {
        this.relationship = relationship;
    }

    public void updateRelationship(Relationship relationship) {
        if (relationship == null) {
            return;
        }
        String outgoingStatus = relationship.getOutgoingStatus();
        if (outgoingStatus != null) {
            this.relationship.setOutgoingStatus(outgoingStatus);
        }
        String incomingStatus = relationship.getIncomingStatus();
        if (incomingStatus != null) {
            this.relationship.setIncomingStatus(incomingStatus);
        }
        this.relationship = relationship;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return id != null && user.id != null && id.equals(user.id);
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
        dest.writeString(this.username);
        dest.writeString(this.fullName);
        dest.writeString(this.profilePicture);
        dest.writeString(this.bio);
        dest.writeString(this.website);
        dest.writeParcelable(this.counts, flags);
        dest.writeParcelable(this.relationship, flags);
    }

    protected User(Parcel in) {
        this.id = in.readString();
        this.username = in.readString();
        this.fullName = in.readString();
        this.profilePicture = in.readString();
        this.bio = in.readString();
        this.website = in.readString();
        this.counts = in.readParcelable(UserCounts.class.getClassLoader());
        this.relationship = in.readParcelable(Relationship.class.getClassLoader());
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
