package com.yu.devlibrary.picker.multi;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Images.Thumbnails;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 读取相册数据工具类,用于自定义相册的图片多选
 *
 * @author yu
 */
public class AlbumHelper {

    private ContentResolver cr;
    private boolean hasBuildImagesBucket = false;//是否已经获取过图片索引

    /**
     * 缩略图列表<br>
     * key - imageId / value - imagePath
     */
    private HashMap<String, String> thumbnailList = new HashMap<>();

    /**
     * 相册列表<br>
     * key - bucketId / value - 相册
     */
    private HashMap<String, ImageBucket> bucketList = new HashMap<>();

    public AlbumHelper(@NonNull Context context) {
        cr = context.getContentResolver();
    }

    /**
     * 从数据库中得到所有图片缩略图的地址
     */
    private void getThumbnail() {
        String[] projection = {Thumbnails._ID, Thumbnails.IMAGE_ID, Thumbnails.DATA};
        Cursor cur = cr.query(Thumbnails.EXTERNAL_CONTENT_URI, projection, null, null, null);

        if (cur.moveToFirst()) {
            int image_id;
            String image_path;

            int imageIdColumn = cur.getColumnIndex(Thumbnails.IMAGE_ID);
            int dataColumn = cur.getColumnIndex(Thumbnails.DATA);

            while (cur.moveToNext()) {
                image_id = cur.getInt(imageIdColumn);
                image_path = cur.getString(dataColumn);

                thumbnailList.put(String.valueOf(image_id), image_path);
            }
            cur.close();
        }
    }

    /**
     * 得到图片集
     */
    private void buildImagesBucketList() {

        // 清空集合
        thumbnailList.clear();
        bucketList.clear();
        hasBuildImagesBucket = false;

        // 构造缩略图索引
        getThumbnail();

        // 构造相册索引
        String columns[] = new String[]{Media._ID, Media.BUCKET_ID, Media.DATA, Media.BUCKET_DISPLAY_NAME};
        String selection = Media.MIME_TYPE + "=? or " + Media.MIME_TYPE + "=? or " + Media.MIME_TYPE + "=?";
        String[] selectionArgs = new String[]{"image/jpg", "image/jpeg", "image/png"};

        Cursor cur = cr.query(Media.EXTERNAL_CONTENT_URI, columns, selection, selectionArgs, Media.DATE_MODIFIED);

        if (cur.moveToFirst()) {
            // 获取指定列的索引
            int photoIDIndex = cur.getColumnIndexOrThrow(Media._ID);
            int photoPathIndex = cur.getColumnIndexOrThrow(Media.DATA);
            int bucketDisplayNameIndex = cur.getColumnIndexOrThrow(Media.BUCKET_DISPLAY_NAME);
            int bucketIdIndex = cur.getColumnIndexOrThrow(Media.BUCKET_ID);

            while (cur.moveToNext()) {
                String _id = cur.getString(photoIDIndex);
                String path = cur.getString(photoPathIndex);
                String bucketName = cur.getString(bucketDisplayNameIndex);
                String bucketId = cur.getString(bucketIdIndex);

                ImageBucket bucket = bucketList.get(bucketId);
                if (bucket == null) {
                    bucket = new ImageBucket(bucketName, new ArrayList<ImageItem>());
                    bucketList.put(bucketId, bucket);
                }
                bucket.count++;
                bucket.imageList.add(new ImageItem(thumbnailList.get(_id), path));
            }
        }
        cur.close();

        hasBuildImagesBucket = true;
    }

    /**
     * 得到图片集
     */
    public List<ImageBucket> getImagesBucketList() {
        if (!hasBuildImagesBucket) {
            buildImagesBucketList();
        }

        List<ImageBucket> tmpList = new ArrayList<>();
        for (Map.Entry<String, ImageBucket> entry : bucketList.entrySet()) {
            tmpList.add(entry.getValue());
        }

        return tmpList;
    }

}
