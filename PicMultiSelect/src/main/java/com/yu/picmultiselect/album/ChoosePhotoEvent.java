package com.yu.picmultiselect.album;

import java.util.List;

/**
 * eventbus 事件
 *
 * @author yu
 */
public class ChoosePhotoEvent {

    /**
     * 选中的图片
     */
    public List<ImageItem> data;

    public ChoosePhotoEvent(List<ImageItem> data) {
        super();
        this.data = data;
    }

}
