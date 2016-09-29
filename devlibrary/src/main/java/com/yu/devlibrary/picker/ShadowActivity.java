package com.yu.devlibrary.picker;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import java.io.File;

import static com.yu.devlibrary.picker.PickConstants.REQUEST_CODE_CAMERA;
import static com.yu.devlibrary.picker.PickConstants.REQUEST_CODE_CROP;
import static com.yu.devlibrary.picker.PickConstants.REQUEST_CODE_GALLERY;

/**
 * Wrapper for onActivityResult
 *
 * @author yu
 *         Create on 16/9/29.
 */
public class ShadowActivity extends Activity {

    private PickRequest request;
    private Uri corpFileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            handleIntent(getIntent());
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        Intent requestIntent = intent.getParcelableExtra(PickConstants.REQUEST_INTENT);
        int requestCode = intent.getIntExtra(PickConstants.REQUEST_CODE, 0);
        request = PickConstants.request;

        File cacheDir = getExternalCacheDir();
        if (!cacheDir.exists())
            cacheDir.mkdirs();
        if (corpFileUri == null)
            corpFileUri = Uri.fromFile(cacheDir).buildUpon().appendPath("tempCropFile.jpg").build();

        try {
            startActivityForResult(requestIntent, requestCode);
        } catch (ActivityNotFoundException exception) {
            request.getCallback().onError(requestCode, exception);
            PickConstants.request = null;
            finish();
        }
    }

    private Intent buildCropIntent(CropParams params, Uri sourceData) {
        return new Intent("com.android.camera.action.CROP", null)
                .setDataAndType(sourceData, "image/*")
                .putExtra("crop", "true")
                .putExtra("scale", true)
                .putExtra("aspectX", params.aspectX)
                .putExtra("aspectY", params.aspectY)
                .putExtra("outputX", params.outputX)
                .putExtra("outputY", params.outputY)
                .putExtra("return-data", false)
                .putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
                .putExtra("noFaceDetection", true)
                .putExtra("scaleUpIfNeeded", true)
                .putExtra(MediaStore.EXTRA_OUTPUT, corpFileUri);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PickCallback callback = request.getCallback();
        if (resultCode == Activity.RESULT_CANCELED) {
            callback.onError(requestCode, null);
            PickConstants.request = null;
            finish();
        } else if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_CROP:
                    callback.onSelected(corpFileUri);
                    PickConstants.request = null;
                    finish();
                    break;
                case REQUEST_CODE_CAMERA:
                    if (request.isNeedCrop()) {
                        Intent intent = buildCropIntent(request.getCropParams(), request.getCameraUri());
                        startActivityForResult(intent, REQUEST_CODE_CROP);
                    } else {
                        callback.onSelected(request.getCameraUri());
                        PickConstants.request = null;
                        finish();
                    }
                    break;
                case REQUEST_CODE_GALLERY:
                    String path = PickUtils.getRealPathFromURIKK(this, data.getData());
                    if (request.isNeedCrop()) {
                        Intent intent2 = buildCropIntent(request.getCropParams(), Uri.fromFile(new File(path)));
                        startActivityForResult(intent2, REQUEST_CODE_CROP);
                    } else {
                        callback.onSelected(Uri.fromFile(new File(path)));
                        PickConstants.request = null;
                        finish();
                    }
                    break;
            }
        }
    }


}