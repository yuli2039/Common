package com.yu.devlibrary.picker;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import java.io.File;


// 用法：
// picker.newRequest()
//        .pickFrom(SourceType.GALLERY)
//        .needCrop(true)
//        .cropParams(new CropParams(this, 1, 1, 400, 400))
//        .build()
//        .doTask();
//
// picker.newRequest()
//        .pickFrom(SourceType.CAMERA)
//        .savePhotoTo(new File(Environment.getExternalStorageDirectory(), "temp.jpeg"))
//        .needCrop(true)
//        .cropParams(new CropParams(this, 1, 1, 400, 400))
//        .build()
//        .doTask();
//
//         protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//              super.onActivityResult(requestCode, resultCode, data);
//         // 处理回调
//              mPicker.handleResult(requestCode, resultCode, data, new ResultHandler() {
//                      public void onPhotoSelected(Uri uri) {
//                          // 通过uri获取图片
//                        Bitmap bitmap = PickUtils.decodeUriAsBitmap(MainActivity.this, uri);
//                         iv.setImageBitmap(bitmap);
//                     }
//
//               });
//         }

/**
 * @author yu
 *         Create on 16/4/20.
 */
public class PhotoPicker {

    public static final int REQUEST_CROP_CAMERA = 1210;
    public static final int REQUEST_CROP_GALLERY = 1211;
    public static final int REQUEST_CAMERA = 1212;
    public static final int REQUEST_GALLERY = 1213;

    private Activity mContext;
    private PickRequest cameraRequest;
    private PickRequest galleryRequest;

    public PhotoPicker(@NonNull Activity context) {
        this.mContext = context;
    }

    public void setCameraRequest(PickRequest cameraRequest) {
        this.cameraRequest = cameraRequest;
    }

    public void setGalleryRequest(PickRequest galleryRequest) {
        this.galleryRequest = galleryRequest;
    }

    /**
     * 入口
     */
    public PickRequest.PickRequestBuilder newRequest() {
        return new PickRequest.PickRequestBuilder(mContext, this);
    }

    public void handleResult(int requestCode, int resultCode, Intent data, ResultHandler handler) {
        if (handler == null)
            return;

        if (resultCode == Activity.RESULT_CANCELED) {
            handler.onSelectFail("获取失败");
        } else if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CROP_CAMERA:
                    handler.onPhotoSelected(cameraRequest.getCropParams().uri);
                    break;
                case REQUEST_CROP_GALLERY:
                    handler.onPhotoSelected(galleryRequest.getCropParams().uri);
                    break;
                case REQUEST_CAMERA:
                    if (cameraRequest.isNeedCrop()) {
                        Intent intent = buildCropIntent(cameraRequest.getCropParams(), cameraRequest.getCameraUri());
                        mContext.startActivityForResult(intent, REQUEST_CROP_CAMERA);
                    } else {
                        handler.onPhotoSelected(cameraRequest.getCameraUri());
                    }
                    break;
                case REQUEST_GALLERY:
                    String path = PickUtils.getRealPathFromURIKK(mContext, data.getData());
                    if (galleryRequest.isNeedCrop()) {
                        Intent intent2 = buildCropIntent(galleryRequest.getCropParams(), Uri.fromFile(new File(path)));
                        mContext.startActivityForResult(intent2, REQUEST_CROP_GALLERY);
                    } else {
                        handler.onPhotoSelected(Uri.fromFile(new File(path)));
                    }
                    break;
            }
        }
    }

    private Intent buildCropIntent(CropParams params, Uri data) {
        return new Intent("com.android.camera.action.CROP", null)
                .setDataAndType(data, params.type)//输入
                .putExtra("crop", params.crop)
                .putExtra("scale", params.scale)
                .putExtra("aspectX", params.aspectX)
                .putExtra("aspectY", params.aspectY)
                .putExtra("outputX", params.outputX)
                .putExtra("outputY", params.outputY)
                .putExtra("return-data", params.returnData)
                .putExtra("outputFormat", params.outputFormat)
                .putExtra("noFaceDetection", params.noFaceDetection)
                .putExtra("scaleUpIfNeeded", params.scaleUpIfNeeded)
                .putExtra(MediaStore.EXTRA_OUTPUT, params.uri);// 输出
    }

}
