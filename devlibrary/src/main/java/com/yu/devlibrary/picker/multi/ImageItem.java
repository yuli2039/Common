package com.yu.devlibrary.picker.multi;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 图片对象
 */
public class ImageItem implements Parcelable {

    public String thumbnailPath;
    public String srcPath;
    public boolean isSelected = false;
    public boolean isAdd = false;

    public ImageItem() {
    }

    public ImageItem(boolean isAdd) {
        this.isAdd = isAdd;
    }

    public ImageItem(String thumbnailPath, String srcPath) {
        this.thumbnailPath = thumbnailPath;
        this.srcPath = srcPath;
    }

    @Override
    public int hashCode() {
        if (srcPath != null)
            return srcPath.hashCode();
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ImageItem) {
            ImageItem item = (ImageItem) o;
            if (item.isAdd) {
                return this.isAdd;
            }
            return item.srcPath.equals(this.srcPath);
        }
        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.thumbnailPath);
        dest.writeString(this.srcPath);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isAdd ? (byte) 1 : (byte) 0);
    }

    protected ImageItem(Parcel in) {
        this.thumbnailPath = in.readString();
        this.srcPath = in.readString();
        this.isSelected = in.readByte() != 0;
        this.isAdd = in.readByte() != 0;
    }

    public static final Parcelable.Creator<ImageItem> CREATOR = new Parcelable.Creator<ImageItem>() {
        @Override
        public ImageItem createFromParcel(Parcel source) {
            return new ImageItem(source);
        }

        @Override
        public ImageItem[] newArray(int size) {
            return new ImageItem[size];
        }
    };
}
