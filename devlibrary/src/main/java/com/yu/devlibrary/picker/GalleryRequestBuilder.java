package com.yu.devlibrary.picker;

import android.app.Activity;
import android.content.Intent;

/**
 * @author yu
 *         Create on 16/4/20.
 */
public class GalleryRequestBuilder {

    private Activity mContext;
    private CropParams mParams;
    private boolean needCrop = false;

    public GalleryRequestBuilder(Activity aty) {
        mContext = aty;
        mParams = new CropParams(aty);
    }

    public GalleryRequestBuilder needCrop(boolean crop) {
        needCrop = crop;
        return this;
    }

    public GalleryRequestBuilder cropParams(CropParams params) {
        mParams = params;
        return this;
    }

    public GalleryRequest build() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT)
                .addCategory(Intent.CATEGORY_OPENABLE)
                .setType("image/*");
        return new GalleryRequest(mContext, intent);
    }

    public static class GalleryRequest {
        private Activity mContext;
        private Intent mIntent;

        public GalleryRequest(Activity context, Intent intent) {
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
            mContext.startActivityForResult(mIntent, PhotoPicker.REQUEST_GALLERY);
        }
    }

    public boolean isNeedCrop() {
        return needCrop;
    }

    public CropParams getParams() {
        return mParams;
    }

}
