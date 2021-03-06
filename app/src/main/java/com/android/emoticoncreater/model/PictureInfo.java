package com.android.emoticoncreater.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 图片
 */

public class PictureInfo implements Parcelable {

    private int resourceId;
    private String title;
    private String filePath;

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.resourceId);
        dest.writeString(this.title);
        dest.writeString(this.filePath);
    }

    public PictureInfo() {
    }

    protected PictureInfo(Parcel in) {
        this.resourceId = in.readInt();
        this.title = in.readString();
        this.filePath = in.readString();
    }

    public static final Creator<PictureInfo> CREATOR = new Creator<PictureInfo>() {
        @Override
        public PictureInfo createFromParcel(Parcel source) {
            return new PictureInfo(source);
        }

        @Override
        public PictureInfo[] newArray(int size) {
            return new PictureInfo[size];
        }
    };
}
