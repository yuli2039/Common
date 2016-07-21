package com.yu.devlibrary.picker.multi;

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

    public ImageItem(boolean isAdd) {
        this.isAdd = isAdd;
    }

    public ImageItem(String thumbnailPath, String srcPath) {
        this.thumbnailPath = thumbnailPath;
        this.srcPath = srcPath;
    }

    // 保证唯一
    @Override
    public int hashCode() {
        if (srcPath != null)
            return srcPath.hashCode();
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ImageItem) {
            ImageItem item = (ImageItem) o;
            if (item.isAdd) {
                return this.isAdd;
            }
            return item.srcPath.equals(this.srcPath);
        }
        return false;
    }
}
