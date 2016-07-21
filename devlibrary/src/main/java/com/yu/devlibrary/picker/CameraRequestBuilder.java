package com.yu.devlibrary.picker;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;

/**
 * @author yu
 *         Create on 16/4/20.
 */
public class CameraRequestBuilder {

    private Activity mContext;
    private File mDir ;
    private String mFileName = "tempPhotoFromCamera.jpg";
    private boolean needCrop = false;
    private CropParams mParams;
    private Uri uri;// 通过目录和文件名构建的uri，拍照完成后原图保存在该地址

    public CameraRequestBuilder(Activity aty) {
        mContext = aty;
        mDir = aty.getExternalCacheDir();
        if (mDir == null) {
            mDir = aty.getCacheDir();
        }
        if (!mDir.exists()) {
            mDir.mkdirs();
        }

        mParams = new CropParams(aty);
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

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                .putExtra(MediaStore.EXTRA_OUTPUT, uri);

        return new CameraRequest(mContext, intent);
    }

    public class CameraRequest {

        private Activity mContext;
        private Intent mIntent;

        public CameraRequest(Activity context, Intent intent) {
            this.mContext = context;
            this.mIntent = intent;
        }

        public void execute() {
            if (mContext == null) {
                throw new IllegalArgumentException("context must not be null");
            }
            if (mIntent == null) {
                throw new IllegalArgumentException("you must call the method build first");
            }
            mContext.startActivityForResult(mIntent, PhotoPicker.REQUEST_CAMERA);
        }
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

}
