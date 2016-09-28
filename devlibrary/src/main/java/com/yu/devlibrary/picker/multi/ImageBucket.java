package com.yu.devlibrary.picker.multi;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 相册对象
 */
public class ImageBucket implements Parcelable {

    public int count = 0;
    public String bucketName;
    public List<ImageItem> imageList;

    public ImageBucket(String bucketName, List<ImageItem> imageList) {
        super();
        this.bucketName = bucketName;
        this.imageList = imageList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.count);
        dest.writeString(this.bucketName);
        dest.writeTypedList(this.imageList);
    }

    protected ImageBucket(Parcel in) {
        this.count = in.readInt();
        this.bucketName = in.readString();
        this.imageList = in.createTypedArrayList(ImageItem.CREATOR);
    }

    public static final Parcelable.Creator<ImageBucket> CREATOR = new Parcelable.Creator<ImageBucket>() {
        @Override
        public ImageBucket createFromParcel(Parcel source) {
            return new ImageBucket(source);
        }

        @Override
        public ImageBucket[] newArray(int size) {
            return new ImageBucket[size];
        }
    };
}
