package com.jelvix.kotlincodesample.api.api.entity.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.jelvix.kotlincodesample.api.api.entity.Meta;
import com.jelvix.kotlincodesample.api.api.entity.Pagination;
import com.jelvix.kotlincodesample.api.api.entity.Relationship;

public class RelationshipResponse implements Parcelable {

    @SerializedName("data")
    private Relationship relationship;
    private Meta meta;
    private Pagination pagination;

    public RelationshipResponse() {
    }

    public Relationship getRelationship() {
        return relationship;
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
        dest.writeParcelable(this.relationship, flags);
        dest.writeParcelable(this.meta, flags);
        dest.writeParcelable(this.pagination, flags);
    }

    protected RelationshipResponse(Parcel in) {
        this.relationship = in.readParcelable(Relationship.class.getClassLoader());
        this.meta = in.readParcelable(Meta.class.getClassLoader());
        this.pagination = in.readParcelable(Pagination.class.getClassLoader());
    }

    public static final Creator<RelationshipResponse> CREATOR = new Creator<RelationshipResponse>() {
        @Override
        public RelationshipResponse createFromParcel(Parcel source) {
            return new RelationshipResponse(source);
        }

        @Override
        public RelationshipResponse[] newArray(int size) {
            return new RelationshipResponse[size];
        }
    };
}
