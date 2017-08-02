package com.jelvix.kotlincodesample.api.api.entity.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.jelvix.kotlincodesample.api.api.entity.Meta;

public class LikeResponse implements Parcelable {

    private Meta meta;

    public LikeResponse() {
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
        dest.writeParcelable(this.meta, flags);
    }

    protected LikeResponse(Parcel in) {
        this.meta = in.readParcelable(Meta.class.getClassLoader());
    }

    public static final Creator<LikeResponse> CREATOR = new Creator<LikeResponse>() {
        @Override
        public LikeResponse createFromParcel(Parcel source) {
            return new LikeResponse(source);
        }

        @Override
        public LikeResponse[] newArray(int size) {
            return new LikeResponse[size];
        }
    };
}
