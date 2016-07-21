package com.yu.picmultiselect.album;

import java.util.List;

/**
 * eventbus 删除图片的事件
 *
 * @author yu
 */
public class RemovePhotoEvent {

    /**
     * 删除之后的图片数据
     */
    public List<ImageItem> data;

    public RemovePhotoEvent(List<ImageItem> data) {
        super();
        this.data = data;
    }

}
