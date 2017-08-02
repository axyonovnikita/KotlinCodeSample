package com.jelvix.kotlincodesample.api.api.entity.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.jelvix.kotlincodesample.api.api.entity.Meta;
import com.jelvix.kotlincodesample.api.api.entity.User;

public class UserResponse implements Parcelable {

    @SerializedName("data")
    private User user;
    private Meta meta;

    public UserResponse() {
    }

    public User getUser() {
        return user;
    }

    public Meta getMeta() {
        return meta;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.user, flags);
        dest.writeParcelable(this.meta, flags);
    }

    protected UserResponse(Parcel in) {
        this.user = in.readParcelable(User.class.getClassLoader());
        this.meta = in.readParcelable(Meta.class.getClassLoader());
    }

    public static final Creator<UserResponse> CREATOR = new Creator<UserResponse>() {
        @Override
        public UserResponse createFromParcel(Parcel source) {
            return new UserResponse(source);
        }

        @Override
        public UserResponse[] newArray(int size) {
            return new UserResponse[size];
        }
    };
}
