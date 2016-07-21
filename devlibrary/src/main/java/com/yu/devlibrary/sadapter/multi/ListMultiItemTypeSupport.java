package com.yu.devlibrary.sadapter.multi;

/**
 * 多种条目类型的支持接口
 * 用于ListView
 *
 * @author yu
 */
public interface ListMultiItemTypeSupport<T> {

    /**
     * 返回item类型数量
     */
    int getViewTypeCount();

    /**
     * 通过 t或者position 来判断返回的item类型
     */
    int getItemViewType(int position, T t);

    /**
     * 根据viewType来返回对应的layoutId
     */
    int getItemLayout(int viewType);

}
