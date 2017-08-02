package com.jelvix.kotlincodesample.api.api.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public class Relationship implements Parcelable {

    @SerializedName("outgoing_status")
    private String outgoingStatus;
    @SerializedName("incoming_status")
    private String incomingStatus;

    public Relationship() {
    }

    public String getOutgoingStatus() {
        return outgoingStatus;
    }

    public void setOutgoingStatus(String outgoingStatus) {
        this.outgoingStatus = outgoingStatus;
    }

    public String getIncomingStatus() {
        return incomingStatus;
    }

    public void setIncomingStatus(String incomingStatus) {
        this.incomingStatus = incomingStatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.outgoingStatus);
        dest.writeString(this.incomingStatus);
    }

    protected Relationship(Parcel in) {
        this.outgoingStatus = in.readString();
        this.incomingStatus = in.readString();
    }

    public static final Creator<Relationship> CREATOR = new Creator<Relationship>() {
        @Override
        public Relationship createFromParcel(Parcel source) {
            return new Relationship(source);
        }

        @Override
        public Relationship[] newArray(int size) {
            return new Relationship[size];
        }
    };
}
