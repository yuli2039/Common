package com.yu.devlibrary.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public final class BitmapUtils {

    /**
     * 先压缩尺寸，再进行质量压缩，获取小图
     *
     * @param src 原图片的绝对路径
     * @return 压缩后的小图的绝对路径
     */
    public static String getSmallImage(Context context, String src) {
        Bitmap thumbnail = thumbnail(src, 480, 800);
        Bitmap bitmap = compressImage(thumbnail);
        return save(context, bitmap);
    }

    /**
     * 质量压缩
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 100;
        while (baos.toByteArray().length > 100 * 1024) { //循环判断压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//生成图片
        if (!image.isRecycled()) {
            image.recycle();
        }
        return bitmap;
    }

    /**
     * 根据reqWidth, reqHeight计算最合适的inSampleSize
     *
     * @param options
     * @param maxWidth
     * @param maxHeight
     * @return
     */
    public static int sampleSize(BitmapFactory.Options options, int maxWidth, int maxHeight) {
        // raw height and width of image
        int rawWidth = options.outWidth;
        int rawHeight = options.outHeight;

        // calculate best sample size
        int inSampleSize = 0;
        if (rawHeight > maxHeight || rawWidth > maxWidth) {
            float ratioWidth = (float) rawWidth / maxWidth;
            float ratioHeight = (float) rawHeight / maxHeight;
            inSampleSize = (int) Math.min(ratioHeight, ratioWidth);
        }
        inSampleSize = Math.max(1, inSampleSize);

        Log.w("BitmapUtils", "inSampleSize = " + inSampleSize);
        return inSampleSize;
    }

    /**
     * 更节省内存的读取raw资源成Bitmap
     *
     * @param context
     * @param rawId
     * @return
     */
    public static Bitmap raw(Context context, int rawId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        InputStream is = context.getResources().openRawResource(rawId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    /**
     * 旋转Bitmap(默认回收传进来的原始Bitmap)
     *
     * @param originBitmap
     * @param angle
     * @return
     */
    public static Bitmap rotate(Bitmap originBitmap, int angle) {
        return rotate(originBitmap, angle, true);
    }

    /**
     * 旋转Bitmap
     *
     * @param angle
     * @param originBitmap
     * @param recycle      是否回收传进来的原始Bitmap
     * @return
     */
    public static Bitmap rotate(Bitmap originBitmap, int angle, boolean recycle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        Bitmap rotatedBitmap = Bitmap.createBitmap(originBitmap,
                0, 0, originBitmap.getWidth(), originBitmap.getHeight(), matrix, true);
        if (recycle && originBitmap != null && !originBitmap.isRecycled()) {
            originBitmap.recycle();
        }
        return rotatedBitmap;
    }

    /**
     * 缩放Bitmap(默认回收传进来的原始Bitmap)
     *
     * @param originBitmap
     * @param scaleX
     * @param scaleY
     * @return
     */
    public static Bitmap scale(Bitmap originBitmap, float scaleX, float scaleY) {
        return scale(originBitmap, scaleX, scaleY, true);
    }

    /**
     * 缩放Bitmap - 按缩放倍数
     *
     * @param originBitmap
     * @param scaleX
     * @param scaleY
     * @param recycle      是否回收传进来的原始Bitmap
     * @return
     */
    public static Bitmap scale(Bitmap originBitmap, float scaleX, float scaleY, boolean recycle) {
        Matrix matrix = new Matrix();
        matrix.postScale(scaleX, scaleY);
        Bitmap scaledBitmap = Bitmap.createBitmap(originBitmap,
                0, 0, originBitmap.getWidth(), originBitmap.getHeight(), matrix, true);
        if (recycle && originBitmap != null && !originBitmap.isRecycled()) {
            originBitmap.recycle();
        }
        return scaledBitmap;
    }

    /**
     * 缩放Bitmap - 缩放到目标大小
     *
     * @param originBitmap
     * @param dstWidth
     * @param dstHeight
     * @param recycle      是否回收传进来的原始Bitmap
     * @return
     */
    public static Bitmap scale(Bitmap originBitmap, int dstWidth, int dstHeight, boolean recycle) {
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originBitmap, dstWidth, dstHeight, true);
        if (recycle && originBitmap != null && !originBitmap.isRecycled()) {
            originBitmap.recycle();
        }
        return scaledBitmap;
    }

    /**
     * 获取缩略图（默认关闭自动旋转）
     *
     * @param path
     * @param maxWidth
     * @param maxHeight
     * @return
     */
    public static Bitmap thumbnail(String path, int maxWidth, int maxHeight) {
        return thumbnail(path, maxWidth, maxHeight, false);
    }

    /**
     * 获取缩略图
     * 1. 支持自动旋转
     *
     * @param path
     * @param maxWidth
     * @param maxHeight
     * @param autoRotate
     * @return
     */
    public static Bitmap thumbnail(String path, int maxWidth, int maxHeight, boolean autoRotate) {

        int angle = 0;
        if (autoRotate) {
            angle = exifRotateAngle(path);
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高, 此时返回bm为空
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        options.inJustDecodeBounds = false;
        // 计算缩放比
        int sampleSize = sampleSize(options, maxWidth, maxHeight);
        options.inSampleSize = sampleSize;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inPurgeable = true;
        options.inInputShareable = true;

        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        bitmap = BitmapFactory.decodeFile(path, options);

        if (autoRotate && angle != 0) {
            bitmap = rotate(bitmap, angle);
        }

        return bitmap;
    }

    /**
     * 保存到本地，默认路径
     *
     * @return
     */
    public static String save(Context context, Bitmap bitmap) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return null;
        }

        File dir = context.getExternalCacheDir();
        if (dir == null) {
            dir = context.getCacheDir();
        }
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File destFile = new File(dir, System.currentTimeMillis() + ".jpeg");
        return save(bitmap, Bitmap.CompressFormat.JPEG, 100, destFile);
    }

    /**
     * 保存到本地destFile
     *
     * @param bitmap
     * @param format   JPEG    PNG    WEBP
     * @param quality
     * @param destFile 存放的文件
     * @return
     */
    public static String save(Bitmap bitmap, Bitmap.CompressFormat format, int quality, File destFile) {
        try {
            FileOutputStream out = new FileOutputStream(destFile);
            if (bitmap.compress(format, quality, out)) {
                out.flush();
                out.close();
            }

            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }

            return destFile.getAbsolutePath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 圆角Bitmap
     *
     * @param originBitmap
     * @param radius
     * @param recycle
     * @return
     */
    public static Bitmap round(Bitmap originBitmap, int radius, boolean recycle) {
        // 准备画笔
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        // 准备裁剪的矩阵
        Rect rect = new Rect(0, 0, originBitmap.getWidth(), originBitmap.getHeight());
        RectF rectF = new RectF(new Rect(0, 0, originBitmap.getWidth(), originBitmap.getHeight()));

        Bitmap roundBitmap = Bitmap.createBitmap(originBitmap.getWidth(), originBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(roundBitmap);
        canvas.drawRoundRect(rectF, radius, radius, paint);

        // 这一句是核心，关于Xfermode和SRC_IN请自行查阅
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(originBitmap, rect, rect, paint);

        // 是否回收原始Bitmap
        if (recycle && originBitmap != null && !originBitmap.isRecycled()) {
            originBitmap.recycle();
        }

        return roundBitmap;
    }

    /**
     * 圆形Bitmap：居中裁剪
     *
     * @param originBitmap
     * @param recycle
     * @return
     */
    public static Bitmap circle(Bitmap originBitmap, boolean recycle) {
        int min = originBitmap.getWidth() > originBitmap.getHeight() ? originBitmap.getHeight() : originBitmap.getWidth();
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap circleBitmap = Bitmap.createBitmap(min, min, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(circleBitmap);
        canvas.drawCircle(min / 2, min / 2, min / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        // 居中显示
        int left = -(originBitmap.getWidth() - min) / 2;
        int top = -(originBitmap.getHeight() - min) / 2;
        canvas.drawBitmap(originBitmap, left, top, paint);

        // 是否回收原始Bitmap
        if (recycle && originBitmap != null && !originBitmap.isRecycled()) {
            originBitmap.recycle();
        }

        return circleBitmap;
    }

    /**
     * 灰阶效果
     *
     * @param originBitmap
     * @param recycle
     * @return
     */
    public static Bitmap gray(Bitmap originBitmap, boolean recycle) {
        Bitmap grayBitmap = Bitmap.createBitmap(originBitmap.getWidth(),
                originBitmap.getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(grayBitmap);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        ColorMatrixColorFilter colorMatrixColorFilter =
                new ColorMatrixColorFilter(colorMatrix);
        paint.setColorFilter(colorMatrixColorFilter);
        canvas.drawBitmap(originBitmap, 0, 0, paint);

        // 是否回收原始Bitmap
        if (recycle && originBitmap != null && !originBitmap.isRecycled()) {
            originBitmap.recycle();
        }

        return grayBitmap;
    }

    /**
     * 获取图片的exif的旋转角度
     *
     * @param path
     * @return
     */
    public static int exifRotateAngle(String path) {
        int angle = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    angle = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    angle = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    angle = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return angle;
    }

    /**
     * bitmap转换为byte数组
     */
    public static byte[] bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * bitmap转换为byte数组 cos：图片质量
     */
    public static byte[] bitmap2Bytes(Bitmap bm, int cos) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, cos, baos);
        return baos.toByteArray();
    }

    /**
     * 图片合成(右下角水印)
     */
    public Bitmap createBitmap(Bitmap src, Bitmap watermark) {
        if (src == null) {
            return null;
        }
        int w = src.getWidth();
        int h = src.getHeight();
        int ww = watermark.getWidth();
        int wh = watermark.getHeight();
        // create the new blank bitmap
        Bitmap newb = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
        Canvas cv = new Canvas(newb);
        // draw src into
        cv.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入src
        // draw watermark into
        cv.drawBitmap(watermark, w - ww + 5, h - wh + 5, null);// 在src的右下角画入水印
        // save all clip
        cv.save(Canvas.ALL_SAVE_FLAG);// 保存
        // store
        cv.restore();// 存储
        return newb;
    }
}
