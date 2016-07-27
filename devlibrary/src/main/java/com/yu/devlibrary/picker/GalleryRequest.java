package com.yu.devlibrary.picker;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

/**
 * @author yu
 *         Create on 16/7/27.
 */
public class GalleryRequest {

    private Activity mContext;
    private CropParams mParams;
    private boolean needCrop;

    public GalleryRequest(@NonNull GalleryRequestBuilder builder) {
        mContext = builder.context;
        mParams = builder.params;
        needCrop = builder.needCrop;
    }

    public void execute() {
        if (mContext == null)
            throw new IllegalArgumentException("context must not be null");

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT)
                .addCategory(Intent.CATEGORY_OPENABLE)
                .setType("image/*");

        mContext.startActivityForResult(intent, PhotoPicker.REQUEST_GALLERY);
    }

    public boolean isNeedCrop() {
        return needCrop;
    }

    public CropParams getParams() {
        return mParams;
    }


    public static class GalleryRequestBuilder {

        private PhotoPicker picker;

        public Activity context;
        public CropParams params;
        public boolean needCrop = false;

        public GalleryRequestBuilder(@NonNull Activity aty, PhotoPicker picker) {
            context = aty;
            this.picker = picker;
        }

        public GalleryRequestBuilder needCrop(boolean crop) {
            this.needCrop = crop;
            return this;
        }

        public GalleryRequestBuilder cropParams(CropParams params) {
            this.params = params;
            return this;
        }

        public GalleryRequest build() {
            GalleryRequest request = new GalleryRequest(this);
            picker.mGalleryRequest = request;
            picker = null;

            return request;
        }

    }
}
