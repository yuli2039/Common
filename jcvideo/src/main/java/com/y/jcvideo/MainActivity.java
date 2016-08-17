package com.y.jcvideo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewpager = (ViewPager) findViewById(R.id.viewpager);

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                JCVideoPlayer.releaseAllVideos();// 切换页面停止播放
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new ListFragment());
        fragments.add(new RcvFragment());
        viewpager.setAdapter(new MyAdapter(getSupportFragmentManager(), fragments));
    }

    public static class MyAdapter extends FragmentStatePagerAdapter {
        List<Fragment> fragments;

        public MyAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {//
            return;
        }
        super.onBackPressed();
    }
}
