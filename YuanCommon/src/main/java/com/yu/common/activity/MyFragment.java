package com.yu.common.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yu.common.R;
import com.yu.devlibrary.refresh.XRefreshLayout;
import com.yu.devlibrary.sadapter.ListAdapter;
import com.yu.devlibrary.sadapter.RecyclerAdapter;
import com.yu.devlibrary.sadapter.multi.ListMultiItemTypeSupport;
import com.yu.devlibrary.sadapter.multi.RecyclerMultiItemTypeSupport;
import com.yu.devlibrary.sadapter.viewholder.ListViewHolder;
import com.yu.devlibrary.sadapter.viewholder.RecyclerViewHolder;
import com.yu.devlibrary.xlist.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yu
 *         Create on 16/8/28.
 */
public class MyFragment extends Fragment {

    private XRefreshLayout p2r;
    private XRecyclerView list;
    private MyRecyclerAdapter2 adapter;

//    private XListView list;
//    private MyListAdapter1 adapter;

    private int loadMoreCount = 0;
    private Handler handler = new Handler(Looper.getMainLooper());

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.test_fragment_main, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        p2r = (XRefreshLayout) view.findViewById(R.id.p2r);
        p2r.setOnRefreshListener(new XRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        //  test recyclerview
        list = (XRecyclerView) view.findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new MyRecyclerAdapter2(null);
        list.setAdapter(adapter);
        list.setLoadMoreListener(new XRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadMore();
            }
        });

//        //  test listview
//        list = (XListView) findViewById(R.id.list);
//        adapter = new MyListAdapter1(null);
//        list.setLoadMoreListener(new XListView.LoadMoreListener() {
//            @Override
//            public void onLoadMore() {
//                loadMore();
//            }
//        });
//        list.setAdapter(adapter);

        p2r.setRefreshing(true);
    }

    private void refresh() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<Object> data = new ArrayList<>();
                for (int i = 0; i < 15; i++) {
                    data.add(new Object());
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter.refreshWithNewData(data);
                        p2r.refreshComplete();
                        list.setNoMore(false);
                        loadMoreCount = 0;
                    }
                }, 1000);
            }
        }).start();
    }

    private void loadMore() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<Object> data = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    data.add(new Object());
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (loadMoreCount < 2) {
                            adapter.addItems(data);
                            list.loadMoreComplete();
                            loadMoreCount++;
                        } else {
                            list.setNoMore(true);
                        }
                    }
                }, 1000);
            }
        }).start();
    }

    private static class MyListAdapter1 extends ListAdapter<Object> {
        public MyListAdapter1(List<Object> datas) {
            super(new ListMultiItemTypeSupport<Object>() {
                @Override
                public int getViewTypeCount() {
                    return 3;
                }

                @Override
                public int getItemViewType(int position, Object o) {
                    if (position % 3 == 0)
                        return 0;
                    if (position % 3 == 1)
                        return 1;
                    return 2;
                }

                @Override
                public int getItemLayout(int viewType) {
                    switch (viewType) {
                        case 0://视频
                            return R.layout.test_item1;
                        default:// 图文、图
                            return R.layout.test_item2;
                    }
                }
            }, datas);
        }

        @Override
        protected void onBindData(ListViewHolder holder, int position, Object item) {
        }
    }

    private static class MyRecyclerAdapter2 extends RecyclerAdapter<Object> {
        public MyRecyclerAdapter2(List<Object> datas) {
            super(new RecyclerMultiItemTypeSupport<Object>() {
                @Override
                public int getItemViewType(int position, Object o) {
                    if (position % 3 == 0)
                        return 0;
                    if (position % 3 == 1)
                        return 1;
                    return 2;
                }

                @Override
                public int getItemLayout(int viewType) {
                    switch (viewType) {
                        case 0://视频
                            return R.layout.test_item1;
                        default:// 图文、图
                            return R.layout.test_item2;
                    }
                }
            }, datas);
        }

        @Override
        protected void onBindData(RecyclerViewHolder holder, int position, Object item) {

        }
    }
}
