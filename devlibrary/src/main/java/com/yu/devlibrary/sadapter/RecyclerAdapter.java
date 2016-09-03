package com.yu.devlibrary.sadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yu.devlibrary.sadapter.multi.RecyclerMultiItemTypeSupport;
import com.yu.devlibrary.sadapter.viewholder.RecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView的通用Adapter
 *
 * @author yu
 */
public abstract class RecyclerAdapter<D> extends RecyclerView.Adapter<RecyclerViewHolder> {

    protected final List<D> mDataSet = new ArrayList<>();

    private int mItemLayoutId;
    private RecyclerMultiItemTypeSupport<D> mMultiItemSupport;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    /**
     * @param layoutId 布局id
     * @param datas    数据集
     */
    public RecyclerAdapter(int layoutId, List<D> datas) {
        mItemLayoutId = layoutId;
        addItems(datas);
    }

    /**
     * @param support 多种条目类型支持接口
     * @param datas   数据集
     */
    public RecyclerAdapter(RecyclerMultiItemTypeSupport<D> support, List<D> datas) {
        mMultiItemSupport = support;
        addItems(datas);
    }

    public List<D> getDataSet() {
        return mDataSet;
    }

    public boolean contains(D d) {
        return mDataSet.contains(d);
    }

    /**
     * 追加单个数据
     *
     * @param item
     */
    public void addItem(D item) {
        mDataSet.add(item);
        notifyDataSetChanged();
//        notifyItemInserted(mDataSet.size());
    }

    /**
     * 追加数据集
     *
     * @param items
     */
    public void addItems(List<D> items) {
//        int startIndex = mDataSet.size();
        if (items != null && !items.isEmpty()) {
            mDataSet.addAll(items);
            notifyDataSetChanged();
        }
//        notifyItemRangeInserted(startIndex, items.size());
    }

    /**
     * 添加单个数据到列表头部
     *
     * @param item
     */
    public void addItemToHead(D item) {
        mDataSet.add(0, item);
        notifyDataSetChanged();
//        notifyItemInserted(0);
    }

    /**
     * 添加数据集到列表头部
     *
     * @param items
     */
    public void addItemsToHead(List<D> items) {
        if (items != null && !items.isEmpty()) {
            mDataSet.addAll(0, items);
            notifyDataSetChanged();
        }
//        notifyItemRangeInserted(0, items.size());
    }

    /**
     * 移除某个数据
     *
     * @param position
     */
    public void remove(int position) {
        mDataSet.remove(position);
        notifyDataSetChanged();
//        notifyItemRemoved(position);
    }

    /**
     * 移除某个数据项
     *
     * @param item
     */
    public void remove(D item) {
//        int index = mDataSet.indexOf(item);
        mDataSet.remove(item);
        notifyDataSetChanged();
//        notifyItemRemoved(index);
    }

    public void clear() {
//        int size = mDataSet.size();
        mDataSet.clear();
        notifyDataSetChanged();
//        notifyItemRangeRemoved(0, size);
    }

    /**
     * 删除原先的数据，添加新数据后，刷新view
     *
     * @param items
     */
    public void refreshWithNewData(List<D> items) {
        mDataSet.clear();
        if (items != null && !items.isEmpty())
            mDataSet.addAll(items);
        notifyDataSetChanged();
    }

    public D getItem(int position) {
        return mDataSet.get(position);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int itemLayout = getItemLayout(viewType);
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(itemLayout, parent, false);
        return new RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        final D item = getItem(position);
        // 绑定数据
        onBindData(holder, position, item);
        // 设置单击事件
        setupItemClickListener(holder, position);
        // 设置长按事件
        setupItemLongClickListener(holder, position);
    }

    @Override
    public int getItemViewType(int position) {
        return (mMultiItemSupport == null || mDataSet.isEmpty())
                ? super.getItemViewType(position)
                : mMultiItemSupport.getItemViewType(position, mDataSet.get(position));
    }

    public int getItemLayout(int type) {
        return mMultiItemSupport == null ? mItemLayoutId : mMultiItemSupport.getItemLayout(type);
    }


    /**
     * 绑定数据到Item View上
     *
     * @param holder viewholder
     * @param position 数据的位置
     * @param item     数据项
     */
    protected abstract void onBindData(RecyclerViewHolder holder, int position, D item);

    protected void setupItemClickListener(final RecyclerViewHolder viewHolder, final int position) {
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    int pos = viewHolder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(pos, mDataSet.get(pos));
                }
            }
        });
    }

    protected void setupItemLongClickListener(final RecyclerViewHolder viewHolder, final int position) {
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {
                if (mOnItemLongClickListener != null) {
                    int pos = viewHolder.getLayoutPosition();
                    mOnItemLongClickListener.onItemLongClick(pos, mDataSet.get(pos));
                }
                return false;
            }
        });
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mOnItemLongClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, Object item);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(int position, Object item);
    }

}
