package com.yu.devlibrary.picker;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import java.io.File;

/**
 * @author yu
 *         Create on 16/4/20.
 */
public class CropParams {

    public String type;
    public String outputFormat;
    public String crop;
    public boolean scale;
    public boolean returnData;
    public boolean noFaceDetection;
    public boolean scaleUpIfNeeded;
    public int aspectX;
    public int aspectY;
    public int outputX;
    public int outputY;
    public Uri uri;

    public CropParams(Context context) {
        // 默认 1:1 300*300
        this(context, 1, 1, 300, 300);
    }

    public CropParams(Context context, int aspectX, int aspectY, int outputX, int outputY) {
        File dir = context.getExternalCacheDir();
        if (dir == null) {
            dir = context.getCacheDir();
        }
        if (!dir.exists()) {
            dir.mkdirs();
        }

        uri = Uri.fromFile(dir).buildUpon().appendPath("tempCropFile.jpg").build();

        type = "image/*";
        outputFormat = Bitmap.CompressFormat.JPEG.toString();
        crop = "true";
        scale = true;
        returnData = false;
        noFaceDetection = true;
        scaleUpIfNeeded = true;
        this.aspectX = aspectX;
        this.aspectY = aspectY;
        this.outputX = outputX;
        this.outputY = outputY;
    }
}
