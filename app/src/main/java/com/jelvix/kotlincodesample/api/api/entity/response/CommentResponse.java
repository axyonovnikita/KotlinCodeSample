package com.jelvix.kotlincodesample.api.api.entity.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.jelvix.kotlincodesample.api.api.entity.Comment;
import com.jelvix.kotlincodesample.api.api.entity.Meta;
import com.jelvix.kotlincodesample.api.api.entity.Pagination;

public class CommentResponse implements Parcelable {

    @SerializedName("data")
    private Comment comment;
    private Meta meta;
    private Pagination pagination;

    public CommentResponse() {
    }

    public Comment getComment() {
        return comment;
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
        dest.writeParcelable(this.comment, flags);
        dest.writeParcelable(this.meta, flags);
        dest.writeParcelable(this.pagination, flags);
    }

    protected CommentResponse(Parcel in) {
        this.comment = in.readParcelable(Comment.class.getClassLoader());
        this.meta = in.readParcelable(Meta.class.getClassLoader());
        this.pagination = in.readParcelable(Pagination.class.getClassLoader());
    }

    public static final Creator<CommentResponse> CREATOR = new Creator<CommentResponse>() {
        @Override
        public CommentResponse createFromParcel(Parcel source) {
            return new CommentResponse(source);
        }

        @Override
        public CommentResponse[] newArray(int size) {
            return new CommentResponse[size];
        }
    };
}
