package com.yu.picmultiselect.album;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yu.picmultiselect.R;
import com.yu.picmultiselect.widget.photoview.PhotoView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;


/**
 * 看大图页面
 *
 * @author yu
 */
public class PreviewActivity extends Activity {

    public static final String KEY_PHOTO_DATA = "selected_Photo";
    public static final String KEY_SHOW_DEL_BTN = "show_Delete_Btn";
    public static final String KEY_CURRENT_PAGE = "current_page";

    private ArrayList<ImageItem> data;
    private ViewPager vpPreview;
    private TextView tvTitle;
    private ImageView ivDelete;
    private PreviewPager adapter;
    private int currentPageOnStart;// 一进入此页面显示图片的第几页
    private boolean showDelete;// 是否显示删除按钮（发布页面进入则需要显示，选择图片进入不显示）

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_preview);

        vpPreview = (ViewPager) findViewById(R.id.vpPreview);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        ivDelete = (ImageView) findViewById(R.id.ivDelete);

        data = (ArrayList<ImageItem>) getIntent().getSerializableExtra(KEY_PHOTO_DATA);
        showDelete = getIntent().getBooleanExtra(KEY_SHOW_DEL_BTN, false);
        currentPageOnStart = getIntent().getIntExtra(KEY_CURRENT_PAGE, 0);

        ivDelete.setVisibility(showDelete && null != data && data.size() > 0
                ? View.VISIBLE : View.GONE);

        if (null != data && data.size() > 0) {
            // 有图片数据，更新viewpager
            adapter = new PreviewPager();
            vpPreview.addOnPageChangeListener(new OnPageChangeListener() {

                @Override
                public void onPageSelected(int position) {
                    tvTitle.setText(position + 1 + "/" + data.size());
                }

                @Override
                public void onPageScrolled(int arg0, float arg1, int arg2) {
                }

                @Override
                public void onPageScrollStateChanged(int arg0) {
                }
            });

            vpPreview.setAdapter(adapter);
            vpPreview.setCurrentItem(currentPageOnStart);
            tvTitle.setText(currentPageOnStart + 1 + "/" + data.size());

        } else {
            // 没有图片，200毫秒后退出
            new Handler().postDelayed(new Runnable() {

                public void run() {
                    PreviewActivity.this.finish();
                }
            }, 200);
            return;
        }

        ivDelete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 删除对应item，更新数目信息

                int dataSizeBefore = data.size();
                int currentItemBefore = vpPreview.getCurrentItem();

                data.remove(currentItemBefore);
                adapter.notifyDataSetChanged();

                int currentItemAfter = currentItemBefore == 0 ? // 删除的第一条？
                        1 : currentItemBefore == dataSizeBefore - 1 ? // 删除的最后一条？
                        currentItemBefore : currentItemBefore + 1;

                tvTitle.setText(currentItemAfter + "/" + (dataSizeBefore - 1));

                // 如果全部删除了，退出该页面，并回调
                if (data.size() == 0) {
                    ivDelete.setVisibility(View.GONE);
                    tvTitle.setText("0/0");
                    PreviewActivity.this.onBackPressed();
                }
            }

        });
    }

    /**
     * 删除照片之后的回调（发布页面更新已经选择的图片）
     */
    private void removeCallback() {
        if (showDelete) {
            // 如果是发布页面进入，则设置回调
            EventBus.getDefault().post(new RemovePhotoEvent(data));
        }
    }

    /**
     * 标题栏返回键
     */
    public void btnBack(View view) {
        this.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        removeCallback();
        super.onBackPressed();
    }

    private class PreviewPager extends PagerAdapter {

        private int mChildCount = 0;

        @Override
        public void notifyDataSetChanged() {
            mChildCount = getCount();
            super.notifyDataSetChanged();
        }

        @Override
        public int getItemPosition(Object object) {
            // 修改删除data后notify界面无刷新的问题
            if (mChildCount > 0) {
                mChildCount--;
                return POSITION_NONE;
            }
            return super.getItemPosition(object);
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            PhotoView ivPreview = new PhotoView(PreviewActivity.this);
            Glide.with(PreviewActivity.this)
                    .load(data.get(position).srcPath)
                    .fitCenter()
                    .into(ivPreview);
            container.addView(ivPreview);
            return ivPreview;
        }

    }

}
