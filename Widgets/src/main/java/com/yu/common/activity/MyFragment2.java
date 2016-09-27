package com.yu.common.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.yu.common.R;
import com.yu.devlibrary.refresh.SRefreshLayout;
import com.yu.devlibrary.sadapter.ListAdapter;
import com.yu.devlibrary.sadapter.viewholder.ListViewHolder;
import com.yu.devlibrary.slist.SListView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yu
 *         Create on 16/8/28.
 */
public class MyFragment2 extends Fragment {

    private SRefreshLayout p2r;

    private SListView list;
    private MyListAdapter1 adapter;

    private int loadMoreCount = 0;
    private Handler handler = new Handler(Looper.getMainLooper());

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.test_fragment_main2, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        p2r = (SRefreshLayout) view.findViewById(R.id.p2r);
        p2r.setOnRefreshListener(new SRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });


        //  test listview
        list = (SListView) view.findViewById(R.id.list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(), i+"", Toast.LENGTH_SHORT).show();
            }
        });
        adapter = new MyListAdapter1(null);
        list.setLoadMoreListener(new SListView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadMore();
            }
        });
        list.setAdapter(adapter);

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
                for (int i = 0; i < 15; i++) {
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
            super(datas, R.layout.test_item1, R.layout.test_item2);
        }

        @Override
        public int getLayoutIndex(int position, Object item) {
            return position % 3 == 0 ? 0 : 1;
        }

        @Override
        protected void onBindData(ListViewHolder holder, int position, Object item) {
            switch (getItemViewType(position)) {
                case 0:
                    holder.setText(R.id.tv1, "1111111");
                    break;
                case 1:
                    holder.setText(R.id.tv2, "tv2tv2tv2tv2tv2");
                    break;
            }
        }
    }
}
