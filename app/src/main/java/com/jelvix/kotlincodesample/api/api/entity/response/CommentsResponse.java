package com.jelvix.kotlincodesample.api.api.entity.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.jelvix.kotlincodesample.api.api.entity.Comment;
import com.jelvix.kotlincodesample.api.api.entity.Meta;
import com.jelvix.kotlincodesample.api.api.entity.Pagination;

import java.util.List;

public class CommentsResponse implements Parcelable {

    @SerializedName("data")
    private List<Comment> comments;
    private Meta meta;
    private Pagination pagination;

    public CommentsResponse() {
    }

    public List<Comment> getComments() {
        return comments;
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
        dest.writeTypedList(this.comments);
        dest.writeParcelable(this.meta, flags);
        dest.writeParcelable(this.pagination, flags);
    }

    protected CommentsResponse(Parcel in) {
        this.comments = in.createTypedArrayList(Comment.CREATOR);
        this.meta = in.readParcelable(Meta.class.getClassLoader());
        this.pagination = in.readParcelable(Pagination.class.getClassLoader());
    }

    public static final Creator<CommentsResponse> CREATOR = new Creator<CommentsResponse>() {
        @Override
        public CommentsResponse createFromParcel(Parcel source) {
            return new CommentsResponse(source);
        }

        @Override
        public CommentsResponse[] newArray(int size) {
            return new CommentsResponse[size];
        }
    };
}
