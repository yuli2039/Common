package com.yu.devlibrary.picker;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import java.io.File;

/**
 * @author yu
 *         Create on 16/7/27.
 */
public class CameraRequest {

    private Uri uri;// 通过目录和文件名构建的uri，拍照完成后原图保存在该地址
    private boolean needCrop;
    private CropParams mParams;
    private Activity mContext;

    public CameraRequest(@NonNull CameraRequestBuilder builder) {
        this.uri = builder.uri;
        this.needCrop = builder.needCrop;
        this.mParams = builder.mParams;
        this.mContext = builder.mContext;
    }

    public void execute() {
        if (mContext == null)
            throw new IllegalArgumentException("context must not be null");

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                .putExtra(MediaStore.EXTRA_OUTPUT, uri);
        mContext.startActivityForResult(intent, PhotoPicker.REQUEST_CAMERA);
    }

    public boolean isNeedCrop() {
        return needCrop;
    }

    public CropParams getParams() {
        return mParams;
    }

    public Uri getSourceUri() {
        return uri;
    }


    public static class CameraRequestBuilder {

        private PhotoPicker picker;

        private File mDir;
        private String mFileName = "tempPhotoFromCamera.jpg";

        public Activity mContext;
        public boolean needCrop = false;
        public CropParams mParams;
        public Uri uri;// 通过目录和文件名构建的uri，拍照完成后原图保存在该地址

        public CameraRequestBuilder(Activity aty, PhotoPicker picker) {
            this.picker = picker;
            mContext = aty;
            mDir = aty.getExternalCacheDir();
            if (mDir == null) {
                mDir = aty.getCacheDir();
            }
            if (!mDir.exists()) {
                mDir.mkdirs();
            }
        }

        public CameraRequestBuilder directory(File dir) {
            if (dir == null || !dir.isDirectory())
                throw new IllegalArgumentException("This directory is illegal");
            mDir = dir;
            return this;
        }

        public CameraRequestBuilder fileName(String fileName) {
            mFileName = fileName;
            return this;
        }

        public CameraRequestBuilder needCrop(boolean crop) {
            needCrop = crop;
            return this;
        }

        public CameraRequestBuilder cropParams(CropParams params) {
            mParams = params;
            return this;
        }

        public CameraRequest build() {
            uri = Uri.fromFile(mDir)
                    .buildUpon()
                    .appendPath(mFileName)
                    .build();

            CameraRequest request = new CameraRequest(this);
            picker.mCameraRequest = request;
            picker = null;

            return request;
        }

    }
}
