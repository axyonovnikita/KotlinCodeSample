package com.jelvix.kotlincodesample.api.api.entity.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.jelvix.kotlincodesample.api.api.entity.Media;
import com.jelvix.kotlincodesample.api.api.entity.Meta;
import com.jelvix.kotlincodesample.api.api.entity.Pagination;

import java.util.List;

public class MediaResponse implements Parcelable {

    @SerializedName("data")
    private List<Media> media;
    private Meta meta;
    private Pagination pagination;

    public MediaResponse() {
    }

    public List<Media> getMedia() {
        return media;
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
        dest.writeTypedList(this.media);
        dest.writeParcelable(this.meta, flags);
        dest.writeParcelable(this.pagination, flags);
    }

    protected MediaResponse(Parcel in) {
        this.media = in.createTypedArrayList(Media.CREATOR);
        this.meta = in.readParcelable(Meta.class.getClassLoader());
        this.pagination = in.readParcelable(Pagination.class.getClassLoader());
    }

    public static final Creator<MediaResponse> CREATOR = new Creator<MediaResponse>() {
        @Override
        public MediaResponse createFromParcel(Parcel source) {
            return new MediaResponse(source);
        }

        @Override
        public MediaResponse[] newArray(int size) {
            return new MediaResponse[size];
        }
    };
}
