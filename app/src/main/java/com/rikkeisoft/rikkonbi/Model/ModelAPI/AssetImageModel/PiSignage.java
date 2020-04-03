package com.rikkeisoft.rikkonbi.Model.ModelAPI.AssetImageModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PiSignage {
    @SerializedName("fileUrl")
    @Expose
    private String mFileUrl;

    @SerializedName("isVideo")
    @Expose
    private boolean mIsVideo;

    public PiSignage(String fileUrl, boolean isVideo) {
        this.mFileUrl = fileUrl;
        this.mIsVideo = isVideo;
    }

    public PiSignage(){

    }

    public String getFileUrl() {
        return mFileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.mFileUrl = fileUrl;
    }

    public boolean isVideo() {
        return mIsVideo;
    }

    public void setVideo(boolean video) {
        mIsVideo = video;
    }
}
