package com.yu.common.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.yu.common.R;
import com.yu.common.base.SimpleActivity;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends SimpleActivity {


    @Override
    protected int getLayoutId() {
        return R.layout.test_activity_main;
    }

    @Override
    protected void initViewAndEvent() {
        super.initViewAndEvent();
        ViewPager vp = (ViewPager) findViewById(R.id.vp);

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new MyFragment());
        fragments.add(new MyFragment2());

        vp.setAdapter(new MyAdapter(getSupportFragmentManager(), fragments));
    }

    private static class MyAdapter extends FragmentPagerAdapter {
        List<Fragment> fmts;

        public MyAdapter(FragmentManager fm, List<Fragment> fmts) {
            super(fm);
            this.fmts = fmts;
        }

        @Override
        public Fragment getItem(int position) {
            return fmts.get(position);
        }

        @Override
        public int getCount() {
            return fmts.size();
        }
    }
}
