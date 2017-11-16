package com.popularpenguin.bakingapp.Data;

import android.support.annotation.NonNull;

public class Step {
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
