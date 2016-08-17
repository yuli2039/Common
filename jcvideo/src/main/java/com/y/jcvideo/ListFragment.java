package com.y.jcvideo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.yu.devlibrary.sadapter.ListAdapter;
import com.yu.devlibrary.sadapter.viewholder.ListViewHolder;

import java.util.ArrayList;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * @author yu
 *         Create on 16/8/17.
 */
public class ListFragment extends Fragment {

    private static final String[] videoUrls = {"http://video.jiecao.fm/5/1/%E8%87%AA%E5%8F%96%E5%85%B6%E8%BE%B1.mp4",
            "http://gslb.miaopai.com/stream/ed5HCfnhovu3tyIQAiv60Q__.mp4"};
    private static final String[] videoThumbs = {"http://img4.jiecaojingxuan.com/2016/5/1/3430ec64-e6a7-4d8e-b044-9d408e075b7c.jpg",
            "http://img4.jiecaojingxuan.com/2016/3/14/2204a578-609b-440e-8af7-a0ee17ff3aee.jpg"};
    private static final String[] videoTitles = {"嫂子坐火车", "嫂子打游戏"};

    private ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fmt_list, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = (ListView) view;
        ArrayList<Object> data = new ArrayList<>();
        data.add("1");
        data.add("2");
        data.add("3");
        data.add("4");
        data.add("5");
        data.add("6");
        data.add("7");
        listView.setAdapter(new LAdapter(R.layout.item_video, data));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private static class LAdapter extends ListAdapter<Object> {

        public LAdapter(int layoutId, List<Object> datas) {
            super(layoutId, datas);
        }

        @Override
        protected void onBindData(ListViewHolder holder, int position, Object item) {
            JCVideoPlayerStandard jcview = holder.get(R.id.jcView);

            jcview.setUp(videoUrls[position % 2]
                    , JCVideoPlayerStandard.SCREEN_LAYOUT_LIST, "嫂子闭眼睛");
            jcview.thumbImageView.setImageResource(R.mipmap.ic_launcher);

        }
    }
}
