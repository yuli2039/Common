package com.yu.devlibrary.sadapter.multi;

/**
 * 多种条目类型的支持接口
 * 用于recyclerview
 *
 * @author yu
 */
public interface RecyclerMultiItemTypeSupport<T> {

    /**
     * 通过 t或者position 来判断返回的item类型
     */
    int getItemViewType(int position, T t);

    /**
     * 根据viewType来返回对应的layoutId
     */
    int getItemLayout(int viewType);

}
