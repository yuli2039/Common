package com.yu.picmultiselect.album;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Images.Thumbnails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 读取相册数据工具类
 *
 * @author yu
 */
public class AlbumHelper {

    private static AlbumHelper instance;
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

    private AlbumHelper() {
    }

    public static synchronized AlbumHelper getInstance() {
        if (instance == null) {
            instance = new AlbumHelper();
        }
        return instance;
    }

    public AlbumHelper init(Context context) {
        if (context == null)
            throw new NullPointerException();
        cr = context.getContentResolver();
        return this;
    }

    /**
     * 从数据库中得到所有图片缩略图的地址
     */
    private void getThumbnail() {
        String[] projection = {Thumbnails._ID, Thumbnails.IMAGE_ID, Thumbnails.DATA};
        Cursor cur = cr.query(Thumbnails.EXTERNAL_CONTENT_URI, projection, null, null, null);

        if (cur.moveToFirst()) {
            //	int _id;
            int image_id;
            String image_path;
            //	int _idColumn = cur.getColumnIndex(Thumbnails._ID);
            int image_idColumn = cur.getColumnIndex(Thumbnails.IMAGE_ID);
            int dataColumn = cur.getColumnIndex(Thumbnails.DATA);

            while (cur.moveToNext()) {
                // Get the field values
                //	_id = cur.getInt(_idColumn);
                image_id = cur.getInt(image_idColumn);
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
        String columns[] = new String[]{Media._ID, Media.BUCKET_ID, Media.DATA,
                Media.BUCKET_DISPLAY_NAME};
        String selection = Media.MIME_TYPE + "=? or " + Media.MIME_TYPE + "=? or " + Media.MIME_TYPE
                + "=?";
        String[] selectionArgs = new String[]{"image/jpg", "image/jpeg", "image/png"};

        Cursor cur = cr.query(
                Media.EXTERNAL_CONTENT_URI, // uri
                columns, // 列
                selection, // 条件
                selectionArgs, // 条件参数
                Media.DATE_MODIFIED);//排序

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
     *
     * @param refresh 是否刷新
     */
    public List<ImageBucket> getImagesBucketList(boolean refresh) {
        if (refresh || !hasBuildImagesBucket) {
            buildImagesBucketList();
        }

        List<ImageBucket> tmpList = new ArrayList<>();
        for (Map.Entry<String, ImageBucket> entry : bucketList.entrySet()) {
            tmpList.add(entry.getValue());
        }

        return tmpList;
    }

}
