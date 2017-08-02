package com.jelvix.kotlincodesample.api.api.entity.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.jelvix.kotlincodesample.api.api.entity.Meta;
import com.jelvix.kotlincodesample.api.api.entity.Pagination;
import com.jelvix.kotlincodesample.api.api.entity.User;

import java.util.List;

public class UsersResponse implements Parcelable {

    @SerializedName("data")
    private List<User> users;
    private Meta meta;
    private Pagination pagination;

    public UsersResponse() {
    }

    public List<User> getUsers() {
        return users;
    }

    public Meta getMeta() {
        return meta;
    }

    public Pagination getPagination() {
        return pagination;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.users);
        dest.writeParcelable(this.meta, flags);
        dest.writeParcelable(this.pagination, flags);
    }

    protected UsersResponse(Parcel in) {
        this.users = in.createTypedArrayList(User.CREATOR);
        this.meta = in.readParcelable(Meta.class.getClassLoader());
        this.pagination = in.readParcelable(Pagination.class.getClassLoader());
    }

    public static final Creator<UsersResponse> CREATOR = new Creator<UsersResponse>() {
        @Override
        public UsersResponse createFromParcel(Parcel source) {
            return new UsersResponse(source);
        }

        @Override
        public UsersResponse[] newArray(int size) {
            return new UsersResponse[size];
        }
    };
}
