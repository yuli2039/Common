package com.yu.devlibrary.picker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * @author yu
 *         Create on 16/4/21.
 */
public class PickUtils {

    /**
     * 获取到裁剪后图片的uri调用该方法解析成bitmap
     */
    public static Bitmap decodeUriAsBitmap(Context context, Uri uri) {
        if (context == null || uri == null)
            return null;

        Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    /**
     * 获取到裁剪后图片的uri调用该方法保存到本地
     * package/cache目录
     */
    public static String saveImg2SD(Context context, Uri uri, String fileName) {
        if (context == null || uri == null)
            return null;

        File dir = context.getExternalCacheDir();
        if (dir == null) {
            dir = context.getCacheDir();
        }
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File f = new File(dir, fileName);
        int len;
        FileOutputStream fos = null;
        InputStream is = null;
        byte[] buffer = new byte[1024];
        try {
            f.createNewFile();
            fos = new FileOutputStream(f);
            is = context.getContentResolver().openInputStream(uri);
            while ((len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            fos.flush();
            fos.close();
            return f.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
