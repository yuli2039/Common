package com.yu.devlibrary.picker.multi;

import java.io.Serializable;
import java.util.List;

/**
 * 相册对象
 */
public class ImageBucket implements Serializable {

    private static final long serialVersionUID = 1L;
    public int count = 0;
    public String bucketName;
    public List<ImageItem> imageList;

    public ImageBucket(String bucketName, List<ImageItem> imageList) {
        super();
        this.bucketName = bucketName;
        this.imageList = imageList;
    }

}
