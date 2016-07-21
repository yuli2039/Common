package com.yu.devlibrary.picker;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import java.io.File;

// 用法：
//         mPicker.takePhoto()
//       //       .directory(Environment.getExternalStorageDirectory()) // 原图片存放目录，默认 sd/image/
//       //       .fileName("haha.jpg") // 原图片文件名，默认tempPhotoFromCamera.jpg
//       //       .needCrop(true) // 默认false 不进行裁剪
//       //       .cropParams(new CropParams(1, 1, 400, 400)) // 需要裁剪则要传入CropParams参数，默认是空参数构造，比例1比1，宽高300300
//                .build()
//                .execute();
//
//         mPicker.selectFromGallery()
//       //       .needCrop(true)
//       //       .cropParams(new CropParams())
//                .build()
//                .execute();
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
//                     public void onSelectFail(String msg) {
//                     }
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
    private CameraRequestBuilder mCameraRequest;
    private GalleryRequestBuilder mGalleryRequest;

    public PhotoPicker(@NonNull Activity context) {
        this.mContext = context;
    }

    public CameraRequestBuilder takePhoto() {
        mCameraRequest = new CameraRequestBuilder(mContext);
        return mCameraRequest;
    }

    public GalleryRequestBuilder selectFromGallery() {
        mGalleryRequest = new GalleryRequestBuilder(mContext);
        return mGalleryRequest;
    }

    public void handleResult(int requestCode, int resultCode, Intent data, ResultHandler handler) {
        if (handler == null)
            return;

        if (resultCode == Activity.RESULT_CANCELED) {
            handler.onSelectFail("获取失败");
        } else if (resultCode == Activity.RESULT_OK) {

            switch (requestCode) {
                case REQUEST_CROP_CAMERA:
                    handler.onPhotoSelected(mCameraRequest.getParams().uri);
                    break;
                case REQUEST_CROP_GALLERY:
                    handler.onPhotoSelected(mGalleryRequest.getParams().uri);
                    break;
                case REQUEST_CAMERA:
                    if (mCameraRequest.isNeedCrop()) {
                        Intent intent = buildCropIntent(mCameraRequest.getParams(),
                                mCameraRequest.getSourceUri());
                        mContext.startActivityForResult(intent, REQUEST_CROP_CAMERA);
                    } else {
                        handler.onPhotoSelected(mCameraRequest.getSourceUri());
                    }
                    break;
                case REQUEST_GALLERY:
                    String path = getRealPathFromURIKK(mContext, data.getData());
                    if (mGalleryRequest.isNeedCrop()) {
                        Intent intent2 = buildCropIntent(mGalleryRequest.getParams(),
                                Uri.fromFile(new File(path)));
                        mContext.startActivityForResult(intent2, REQUEST_CROP_GALLERY);
                    } else {
                        handler.onPhotoSelected(Uri.fromFile(new File(path)));
                    }
                    break;
            }
        }
    }

    private static Intent buildCropIntent(CropParams params, Uri data) {
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

    /**
     * 4.4的版本api返回的uri不兼容，获取图片真正的path
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static String getRealPathFromURIKK(Context context, Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        if (!isKitKat) {
            if (uri.getScheme().equals("file")) {
                return uri.getPath();
            } else {
                return getRealPathFromURI(context, uri);
            }
        }

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + File.separator + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    private static String getRealPathFromURI(Context context, Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        @SuppressWarnings("deprecation")
        Cursor cursor = ((Activity) context).managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    private static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

}
