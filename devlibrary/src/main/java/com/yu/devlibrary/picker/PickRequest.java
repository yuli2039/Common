package com.yu.devlibrary.picker;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import java.io.File;

//PickRequest.with(context)
//          .pickFrom()// 图库还是相机
//          .savePhotoTo()// 如果是相机，可以指定保存原图的地址
//          .needCrop()// 默认不裁剪
//          .cropParams()// 需要裁剪needCrop = true时传入参数
//          .callback()// 回调
//          .build()
//          .doTask();
/**
 * @author yu
 *         Create on 16/7/27.
 */
public class PickRequest {

    private Activity context;
    private SourceType sourceType;
    private boolean needCrop;
    private CropParams cropParams;
    private Uri cameraUri;
    private PickCallback callback;

    public PickRequest(@NonNull PickRequestBuilder builder) {
        this.context = builder.context;
        this.needCrop = builder.needCrop;
        this.cropParams = builder.params;
        this.sourceType = builder.type;
        this.cameraUri = builder.cameraUri;
        this.callback = builder.callback;
    }

    public static PickRequest.PickRequestBuilder with(Activity activity) {
        return new PickRequest.PickRequestBuilder(activity);
    }

    public boolean isNeedCrop() {
        return needCrop;
    }

    public CropParams getCropParams() {
        return cropParams;
    }

    public Uri getCameraUri() {
        return cameraUri;
    }

    public PickCallback getCallback() {
        return callback;
    }

    public void doTask() {
        Intent intent = new Intent(context, ShadowActivity.class);
        Intent requestIntent;
        if (sourceType == SourceType.GALLERY) {
            requestIntent = new Intent(Intent.ACTION_GET_CONTENT)
                    .addCategory(Intent.CATEGORY_OPENABLE)
                    .setType("image/*");
            intent.putExtra(PickConstants.REQUEST_CODE, PickConstants.REQUEST_CODE_GALLERY);
        } else {
            requestIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    .putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
            intent.putExtra(PickConstants.REQUEST_CODE, PickConstants.REQUEST_CODE_CAMERA);
        }
        intent.putExtra(PickConstants.REQUEST_INTENT, requestIntent);

        PickConstants.request = this;
        context.startActivity(intent);
        context = null;
    }

    public static class PickRequestBuilder {

        private Activity context;
        private boolean needCrop = false;
        private CropParams params;
        private SourceType type;
        private Uri cameraUri;
        private PickCallback callback;

        public PickRequestBuilder(@NonNull Activity aty) {
            this.context = aty;
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

        public PickRequestBuilder callback(PickCallback callback) {
            this.callback = callback;
            return this;
        }

        public PickRequest build() {
            if (context == null)
                throw new IllegalStateException("the context must not be null");
            if (type == SourceType.CAMERA && cameraUri == null) {
                File cacheDir = context.getExternalCacheDir();
                if (!cacheDir.exists())
                    cacheDir.mkdirs();
                cameraUri = Uri.fromFile(cacheDir).buildUpon().appendPath("tempPhoto.jpg").build();
            }
            if (needCrop && params == null)
                params = new CropParams();

            return new PickRequest(this);
        }
    }

}
