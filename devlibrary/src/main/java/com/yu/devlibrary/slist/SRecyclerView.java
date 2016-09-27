package com.yu.devlibrary.slist;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 修改自XRecyclerView
 * 可以当成普通的recyclerview来用；
 * 可以添加多个header和footer；
 * 如果设置了LoadMoreListener，将会自动添加loadMoreFooter，覆盖其他的footer；
 *
 * @author yu
 */
public class SRecyclerView extends RecyclerView {

    //下面的ItemViewType是保留值(ReservedItemViewType),如果用户的adapter与它们重复将会强制抛出异常。不过为了简化,我们检测到重复时对用户的提示是ItemViewType必须小于10000
    private static final int HEADER_INIT_INDEX = 10002;
    private static final int FOOTER_INIT_INDEX = 20002;

    private static List<Integer> sHeaderTypes = new ArrayList<>();//每个header必须有不同的type,不然滚动的时候顺序会变化
    private static List<Integer> sFooterTypes = new ArrayList<>();
    private final AdapterDataObserver mDataObserver = new DataObserver();
    private final Context mContext;

    private boolean isLoadingData = false;
    private boolean isNoMore = false;
    private boolean loadingMoreEnabled = false;// 默认不支持加载更多,如果设置了加载更多监听器，会自动变为true并添加脚布局

    private View mEmptyView;
    private List<View> mHeaderViews = new ArrayList<>();
    private List<View> mFooterViews = new ArrayList<>();
    private WrapAdapter mWrapAdapter;
    private LoadMoreListener mLoadmoreListener;

    public SRecyclerView(Context context) {
        this(context, null);
    }

    public SRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
    }

    public void addHeaderView(View view) {
        // 不允许添加相同的header
        if (!mHeaderViews.contains(view)) {
            sHeaderTypes.add(HEADER_INIT_INDEX + mHeaderViews.size());
            mHeaderViews.add(view);
        }
    }

    //根据header的ViewType判断是哪个header
    private View getHeaderViewByType(int itemType) {
        if (!isHeaderType(itemType))
            return null;
        return mHeaderViews.get(itemType - HEADER_INIT_INDEX);
    }

    //判断一个type是否为HeaderType
    private boolean isHeaderType(int itemViewType) {
        return mHeaderViews.size() > 0 && sHeaderTypes.contains(itemViewType);
    }

    //判断是否是XRecyclerView保留的itemViewType
    private boolean isReservedItemViewType(int itemViewType) {
        return sFooterTypes.contains(itemViewType) || sHeaderTypes.contains(itemViewType);
    }

    public void addFooterView(View view) {
        // 如果添加了自己的脚布局，就不允许加载更多
        if (!loadingMoreEnabled && !mFooterViews.contains(view)) {
            sFooterTypes.add(FOOTER_INIT_INDEX + mFooterViews.size());
            mFooterViews.add(view);
        }
    }

    //根据footer的ViewType判断是哪个footer
    private View getFooterViewByType(int itemType) {
        if (!isFooterType(itemType))
            return null;
        return mFooterViews.get(itemType - FOOTER_INIT_INDEX);
    }

    //判断一个type是否为HeaderType
    private boolean isFooterType(int itemViewType) {
        return mFooterViews.size() > 0 && sFooterTypes.contains(itemViewType);
    }

    public void loadMoreComplete() {
        isLoadingData = false;
        if (!mFooterViews.isEmpty() && mFooterViews.get(0) instanceof LoadMoreFooter)
            ((LoadMoreFooter) mFooterViews.get(0)).setState(LoadMoreFooter.STATE_COMPLETE);
    }

    public void setNoMore(boolean noMore) {
        isLoadingData = false;
        isNoMore = noMore;
        if (!mFooterViews.isEmpty() && mFooterViews.get(0) instanceof LoadMoreFooter)
            ((LoadMoreFooter) mFooterViews.get(0))
                    .setState(isNoMore ? LoadMoreFooter.STATE_NOMORE : LoadMoreFooter.STATE_COMPLETE);
    }

    /**
     * 如果允许加载更多，就不能 添加自定义脚布局
     */
    public void setLoadMoreEnabled(boolean enabled) {
        loadingMoreEnabled = enabled;
        // 如果已经添加了脚布局，先清空
        sFooterTypes.clear();
        mFooterViews.clear();
        if (enabled) {
            sFooterTypes.add(FOOTER_INIT_INDEX + mFooterViews.size());
            LoadMoreFooter footer = new LoadMoreFooter(mContext);
            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp2px(50));
            footer.setLayoutParams(layoutParams);
            mFooterViews.add(footer);
        }
    }

    public void setEmptyView(View emptyView) {
        this.mEmptyView = emptyView;
    }

    public View getEmptyView() {
        return mEmptyView;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        mWrapAdapter = new WrapAdapter(adapter);
        super.setAdapter(mWrapAdapter);
        adapter.registerAdapterDataObserver(mDataObserver);
//        mDataObserver.onChanged();//
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if (state == RecyclerView.SCROLL_STATE_IDLE && mLoadmoreListener != null && !isLoadingData && loadingMoreEnabled) {
            LayoutManager layoutManager = getLayoutManager();
            int lastVisibleItemPosition;
            if (layoutManager instanceof GridLayoutManager) {
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(into);
                lastVisibleItemPosition = findMax(into);
            } else {
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            }

            if (layoutManager.getChildCount() > 0
                    && lastVisibleItemPosition >= layoutManager.getItemCount() - 1
                    && layoutManager.getItemCount() > layoutManager.getChildCount()
                    && !isNoMore) {
                isLoadingData = true;
                ((LoadMoreFooter) mFooterViews.get(0)).setState(LoadMoreFooter.STATE_LOADING);
                mLoadmoreListener.onLoadMore();
            }
        }
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max)
                max = value;
        }
        return max;
    }

    private class DataObserver extends AdapterDataObserver {
        @Override
        public void onChanged() {
            Adapter<?> adapter = getAdapter();
            if (adapter != null && mEmptyView != null) {
                int emptyCount = mHeaderViews.size() + mFooterViews.size();

                if (adapter.getItemCount() == emptyCount) {
                    mEmptyView.setVisibility(View.VISIBLE);
                    SRecyclerView.this.setVisibility(View.GONE);
                } else {
                    mEmptyView.setVisibility(View.GONE);
                    SRecyclerView.this.setVisibility(View.VISIBLE);
                }
            }
            if (mWrapAdapter != null) {
                mWrapAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            mWrapAdapter.notifyItemMoved(fromPosition, toPosition);
        }
    }

    public class WrapAdapter extends Adapter<ViewHolder> {

        private Adapter adapter;

        public WrapAdapter(Adapter adapter) {
            this.adapter = adapter;
        }

        public boolean isHeader(int position) {
            return position >= 0 && position < mHeaderViews.size();
        }

        public boolean isFooter(int position) {
            return position >= getItemCount() - mFooterViews.size();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (isHeaderType(viewType)) {
                return new SimpleViewHolder(getHeaderViewByType(viewType));
            } else if (isFooterType(viewType)) {
                return new SimpleViewHolder(getFooterViewByType(viewType));
            }
            return adapter.onCreateViewHolder(parent, viewType);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            if (isHeader(position))
                return;

            int adjPosition = position - mHeaderViews.size();
            int adapterCount;
            if (adapter != null) {
                adapterCount = adapter.getItemCount();
                if (adjPosition < adapterCount) {
                    adapter.onBindViewHolder(holder, adjPosition);
                }
            }
        }

        @Override
        public int getItemCount() {
            int footCount = 0;
            if (loadingMoreEnabled) {
                footCount = 1;
            } else {
                footCount = mFooterViews.size();
            }

            int itemCount = 0;
            if (adapter != null) {
                itemCount = adapter.getItemCount();
            }

            return mHeaderViews.size() + itemCount + footCount;
        }

        @Override
        public int getItemViewType(int position) {
            int adjPosition = position - mHeaderViews.size();

            if (isHeader(position)) {
                return sHeaderTypes.get(position);
            }
            if (isFooter(position)) {
                return sFooterTypes.get(adjPosition - adapter.getItemCount());
            }

            if (adapter != null) {
                int adapterCount = adapter.getItemCount();

                if (adjPosition < adapterCount) {
                    int itemViewType = adapter.getItemViewType(adjPosition);

                    if (isReservedItemViewType(itemViewType)) {
                        throw new IllegalStateException("XRecyclerView require itemViewType in adapter should be less than 10000 ");
                    }
                    return itemViewType;
                }
            }
            return 0;
        }

        @Override
        public long getItemId(int position) {
            if (adapter != null && position >= mHeaderViews.size()) {
                int adjPosition = position - mHeaderViews.size();
                if (adjPosition < adapter.getItemCount()) {
                    return adapter.getItemId(adjPosition);
                }
            }
            return -1;
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            LayoutManager manager = recyclerView.getLayoutManager();
            if (manager instanceof GridLayoutManager) {
                final GridLayoutManager gridManager = ((GridLayoutManager) manager);
                gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return (isHeader(position) || isFooter(position)) ? gridManager.getSpanCount() : 1;
                    }
                });
            }
            adapter.onAttachedToRecyclerView(recyclerView);
        }

        @Override
        public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
            adapter.onDetachedFromRecyclerView(recyclerView);
        }

        @Override
        public void onViewAttachedToWindow(ViewHolder holder) {
            super.onViewAttachedToWindow(holder);
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null
                    && lp instanceof StaggeredGridLayoutManager.LayoutParams
                    && (isHeader(holder.getLayoutPosition()) || isFooter(holder.getLayoutPosition()))) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
            adapter.onViewAttachedToWindow(holder);
        }

        @Override
        public void onViewDetachedFromWindow(ViewHolder holder) {
            adapter.onViewDetachedFromWindow(holder);
        }

        @Override
        public void onViewRecycled(ViewHolder holder) {
            adapter.onViewRecycled(holder);
        }

        @Override
        public boolean onFailedToRecycleView(ViewHolder holder) {
            return adapter.onFailedToRecycleView(holder);
        }

        @Override
        public void unregisterAdapterDataObserver(AdapterDataObserver observer) {
            adapter.unregisterAdapterDataObserver(observer);
        }

        @Override
        public void registerAdapterDataObserver(AdapterDataObserver observer) {
            adapter.registerAdapterDataObserver(observer);
        }

        private class SimpleViewHolder extends ViewHolder {
            public SimpleViewHolder(View itemView) {
                super(itemView);
            }
        }
    }

    public void setLoadMoreListener(LoadMoreListener listener) {
        setLoadMoreEnabled(true);
        mLoadmoreListener = listener;
    }

    public interface LoadMoreListener {
        void onLoadMore();
    }

    public static int dp2px(float dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}