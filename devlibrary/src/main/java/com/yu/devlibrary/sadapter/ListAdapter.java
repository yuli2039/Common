package com.yu.devlibrary.sadapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.yu.devlibrary.sadapter.multi.ListMultiItemTypeSupport;
import com.yu.devlibrary.sadapter.viewholder.ListViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * ListAdapter
 *
 * @author yu
 */
public abstract class ListAdapter<D> extends BaseAdapter {

    /**
     * 数据集
     */
    protected final List<D> mDataSet = new ArrayList<>();
    /**
     * Item Layout
     */
    private int mItemLayoutId;

    private ListMultiItemTypeSupport<D> mMultiItemSupport;

    /**
     * 单条目类型使用此构造方法
     */
    public ListAdapter(int layoutId, List<D> datas) {
        mItemLayoutId = layoutId;
        addItems(datas);
    }

    /**
     * 多种条目类型时需要传入一个支持接口
     */
    public ListAdapter(ListMultiItemTypeSupport<D> support, List<D> datas) {
        this.mMultiItemSupport = support;
        addItems(datas);
    }

    @Override
    public int getViewTypeCount() {
        return mMultiItemSupport == null ?
                super.getViewTypeCount() : mMultiItemSupport.getViewTypeCount();
    }

    @Override
    public int getItemViewType(int position) {
        return (mMultiItemSupport == null || mDataSet.isEmpty())
                ? super.getItemViewType(position)
                : mMultiItemSupport.getItemViewType(position, mDataSet.get(position));
    }

    /**
     * 根据View Type返回布局资源
     */
    public int getItemLayout(int type) {
        return mMultiItemSupport == null ? mItemLayoutId : mMultiItemSupport.getItemLayout(type);
    }

    /**
     * 获取数据集
     */
    public List<D> getDataSet() {
        return mDataSet;
    }

    public boolean contains(D d) {
        return mDataSet.contains(d);
    }

    /**
     * @param item
     */
    public void addItem(D item) {
        mDataSet.add(item);
        notifyDataSetChanged();
    }

    /**
     * @param items
     */
    public void addItems(List<D> items) {
        if (items != null && !items.isEmpty()) {
            mDataSet.addAll(items);
            notifyDataSetChanged();
        }
    }

    /**
     * @param item
     */
    public void addItemToHead(D item) {
        mDataSet.add(0, item);
        notifyDataSetChanged();
    }

    /**
     * @param items
     */
    public void addItemsToHead(List<D> items) {
        if (items != null && !items.isEmpty()) {
            mDataSet.addAll(0, items);
            notifyDataSetChanged();
        }
    }

    /**
     * @param position
     */
    public void remove(int position) {
        mDataSet.remove(position);
        notifyDataSetChanged();
    }

    /**
     * @param item
     */
    public void remove(D item) {
        mDataSet.remove(item);
        notifyDataSetChanged();
    }

    public void clear() {
        mDataSet.clear();
        notifyDataSetChanged();
    }

    /**
     * 清空原数据，添加新数据
     */
    public void refreshWithNewData(List<D> items) {
        mDataSet.clear();
        if (items != null && !items.isEmpty())
            mDataSet.addAll(items);
        notifyDataSetChanged();
    }

    /**
     * @return
     */
    @Override
    public int getCount() {
        return mDataSet.size();
    }

    /**
     * @param position
     * @return
     */
    @Override
    public D getItem(int position) {
        return mDataSet.get(position);
    }

    /**
     * @param position
     * @return
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 封装getView逻辑,将根据viewType获取布局资源、解析布局资源、创建ViewHolder等逻辑封装起来,简化使用流程
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int layoutId = getItemLayout(getItemViewType(position));
        ListViewHolder viewHolder = ListViewHolder.get(convertView, parent, layoutId);
        // 绑定数据
        onBindData(viewHolder, position, getItem(position));
        return viewHolder.getItemView();
    }

    /**
     * 绑定数据到Item View上
     *
     * @param holder
     * @param position 数据的位置
     * @param item     数据项
     */
    protected abstract void onBindData(ListViewHolder holder, int position, D item);

}
