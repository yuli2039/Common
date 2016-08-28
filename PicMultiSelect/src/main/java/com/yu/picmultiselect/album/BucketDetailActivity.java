package com.yu.picmultiselect.album;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yu.picmultiselect.R;
import com.yu.picmultiselect.listadapter.BaseAdapterHelper;
import com.yu.picmultiselect.listadapter.QuickAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;



/**
 * 选择照片页面
 *
 * @author yu
 */
public class BucketDetailActivity extends Activity {

    public static final String KEY_ALBUM_DATA = "album_data";

    private ImageBucket bucket;
    private GridView gridview;
    private QuickAdapter<ImageItem> adapter;
    private TextView tvNum;
    private ArrayList<ImageItem> data = new ArrayList<ImageItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_album_detail);

        data.clear();

        gridview = (GridView) findViewById(R.id.gridview);
        tvNum = (TextView) findViewById(R.id.tvNum);

        bucket = (ImageBucket) getIntent().getSerializableExtra(KEY_ALBUM_DATA);
        if (null == bucket || bucket.imageList == null || bucket.imageList.size() <= 0) {
            Toast.makeText(this, "没有任何图片", Toast.LENGTH_SHORT).show();
        } else {
            adapter = new QuickAdapter<ImageItem>(this, R.layout.item_album_detail,
                    bucket.imageList) {

                @Override
                protected void convert(BaseAdapterHelper helper, ImageItem item) {
                    Glide.with(BucketDetailActivity.this)
                            .load(TextUtils.isEmpty(item.thumbnailPath)
                                    ? item.srcPath : item.thumbnailPath)
                            .centerCrop()
                            .into((ImageView) helper.getView(R.id.iv));

                    helper.setImageResource(R.id.ivSelected,
                            item.isSelected ? R.mipmap.icon_data_select : R.color.trans);
                }
            };
            gridview.setAdapter(adapter);
            gridview.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    ImageItem item = adapter.getData().get(position);
                    item.isSelected = !item.isSelected;

                    if (item.isSelected) {
                        if (data.size() >= 9) {
                            item.isSelected = !item.isSelected;

                            Toast.makeText(BucketDetailActivity.this, "最多选择9张图片",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            data.add(item);
                        }
                    } else {
                        if (data.contains(item))
                            data.remove(item);
                    }

                    adapter.notifyDataSetChanged();

                    tvNum.setText(data.size() + "/9");
                }
            });
        }

    }

    /**
     * 标题栏返回键
     */
    public void btnBack(View view) {
        this.onBackPressed();
    }

    /**
     * 确定按钮
     */
    public void btnOK(View view) {
        EventBus.getDefault().post(new ChoosePhotoEvent(data));
        this.finish();
    }

    /**
     * 预览按钮
     */
    public void btnPreview(View view) {
        Intent intent = new Intent(this, PreviewActivity.class);
        intent.putExtra(PreviewActivity.KEY_PHOTO_DATA, data);
        startActivity(intent);
    }
}
