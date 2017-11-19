package com.popularpenguin.bakingapp.Data;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class Step implements Parcelable {
    private int mId;
    private String mShortDescription;
    private String mDescription;
    private String mVideoURL;
    private String mThumbnailURL;

    public Step(int id,
                @NonNull String shortDescription,
                @NonNull String description,
                @NonNull String videoURL,
                @NonNull String thumbnailURL) {

        mId = id;
        mShortDescription = shortDescription;
        mDescription = description;
        mVideoURL = videoURL;
        mThumbnailURL = thumbnailURL;
    }

    private Step(Parcel in) {
        mId = in.readInt();
        mShortDescription = in.readString();
        mDescription = in.readString();
        mVideoURL = in.readString();
        mThumbnailURL = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mShortDescription);
        dest.writeString(mDescription);
        dest.writeString(mVideoURL);
        dest.writeString(mThumbnailURL);
    }

    static final Parcelable.Creator<Step> CREATOR = new Parcelable.Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel source) {
            return new Step(source);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };

    public int getId() { return mId; }

    public String getShortDescription() { return mShortDescription; }
    public void setShortDescription(@NonNull String shortDescription) {
        mShortDescription = shortDescription;
    }

    public String getDescription() { return mDescription; }
    public void setDescription(@NonNull String description) {
        mDescription = description;
    }

    public String getVideoURL() { return mVideoURL; }
    public void setVideoURL(@NonNull String videoURL) {
        mVideoURL = videoURL;
    }

    public String getThumbnailURL() { return mThumbnailURL; }
    public void setThumbnailURL(@NonNull String thumbnailURL) {
        mThumbnailURL = thumbnailURL;
    }
}
