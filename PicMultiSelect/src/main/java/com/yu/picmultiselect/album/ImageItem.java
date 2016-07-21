package com.yu.picmultiselect.album;

import java.io.Serializable;

/**
 * 图片对象
 */
public class ImageItem implements Serializable {

    private static final long serialVersionUID = 1L;
    public String thumbnailPath;
    public String srcPath;
    public boolean isSelected = false;
    public boolean isAdd = false;

    public ImageItem() {
    }

    public ImageItem(String thumbnailPath, String srcPath) {
        this.thumbnailPath = thumbnailPath;
        this.srcPath = srcPath;
    }

}
