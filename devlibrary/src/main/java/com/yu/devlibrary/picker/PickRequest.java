package com.yu.devlibrary.picker;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import java.io.File;

import static com.yu.devlibrary.picker.PhotoPicker.REQUEST_CAMERA;
import static com.yu.devlibrary.picker.PhotoPicker.REQUEST_GALLERY;


/**
 * @author yu
 *         Create on 16/7/27.
 */
public class PickRequest {

    private Activity mContext;
    private boolean needCrop;
    private CropParams mCropParams;
    private SourceType mSourceType;
    private Uri cameraUri;// 请求拍照原图保存在该地址

    public PickRequest(@NonNull PickRequestBuilder builder) {
        this.mContext = builder.context;
        this.needCrop = builder.needCrop;
        this.mCropParams = builder.params;
        this.mSourceType = builder.type;
        this.cameraUri = builder.cameraUri;
    }

    public boolean isNeedCrop() {
        return needCrop;
    }

    public CropParams getCropParams() {
        return mCropParams;
    }

    public Uri getCameraUri() {
        return cameraUri;
    }

    public void doTask() {
        if (mSourceType == SourceType.GALLERY) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT)
                    .addCategory(Intent.CATEGORY_OPENABLE)
                    .setType("image/*");
            mContext.startActivityForResult(intent, REQUEST_GALLERY);
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    .putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
            mContext.startActivityForResult(intent, REQUEST_CAMERA);
        }
    }

    public static class PickRequestBuilder {

        private Activity context;
        private boolean needCrop = false;
        private CropParams params;
        private SourceType type;
        private Uri cameraUri;
        private PhotoPicker mPicker;

        public PickRequestBuilder(@NonNull Activity aty, @NonNull PhotoPicker picker) {
            this.context = aty;
            this.mPicker = picker;
        }

        public PickRequestBuilder pickFrom(SourceType type) {
            this.type = type;
            return this;
        }

        /**
         * 拍照后原图的保存地址
         */
        public PickRequestBuilder savePhotoTo(Uri uri) {
            this.cameraUri = uri;
            return this;
        }

        /**
         * 拍照后原图的保存地址
         */
        public PickRequestBuilder savePhotoTo(File file) {
            this.cameraUri = Uri.fromFile(file);
            return this;
        }

        public PickRequestBuilder needCrop(boolean crop) {
            this.needCrop = crop;
            return this;
        }

        public PickRequestBuilder cropParams(CropParams params) {
            this.params = params;
            return this;
        }

        public PickRequest build() {
            if (context == null)
                throw new IllegalStateException("the context must not be null");
            if (type == SourceType.CAMERA && cameraUri == null)
                throw new IllegalStateException("the photo uri must not be null");
            if (needCrop && params == null)
                params = new CropParams(context);

            PickRequest request = new PickRequest(this);
            if (type == SourceType.CAMERA) {
                mPicker.setCameraRequest(request);
            } else {
                mPicker.setGalleryRequest(request);
            }
            mPicker = null;

            return request;
        }
    }

}
