package com.yu.picmultiselect;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yu.picmultiselect.album.ChoosePhotoEvent;
import com.yu.picmultiselect.album.ImageItem;
import com.yu.picmultiselect.album.PreviewActivity;
import com.yu.picmultiselect.album.RemovePhotoEvent;
import com.yu.picmultiselect.listadapter.BaseAdapterHelper;
import com.yu.picmultiselect.listadapter.QuickAdapter;
import com.yu.picmultiselect.widget.PhotoWindow;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 发布页面
 *
 * @author yu
 */
public class MainActivity extends Activity {

    private GridView gv;
    private View rootView;
    private PhotoWindow photoWindow;
    private ImageItem imageAdd;
    private QuickAdapter<ImageItem> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        EventBus.getDefault().register(this);

        setContentView(R.layout.activity_publish);

        rootView = findViewById(R.id.rootView);
        gv = (GridView) findViewById(R.id.gv);

        ArrayList<ImageItem> data = new ArrayList<ImageItem>();
        imageAdd = new ImageItem();
        imageAdd.isAdd = true;
        data.add(imageAdd);

        adapter = new QuickAdapter<ImageItem>(this,
                R.layout.item_publish_gv, data) {

            @Override
            protected void convert(BaseAdapterHelper helper, ImageItem item) {
                if (item.isAdd) {
//                    helper.setImageResource(R.id.iv, R.mipmap.icon_add_pic);
                    Glide.with(MainActivity.this)
                            .load(R.mipmap.icon_add_pic)
                            .into((ImageView) helper.getView(R.id.iv));
                } else {
                    Glide.with(MainActivity.this)
                            .load(item.srcPath)
                            .centerCrop()
                            .into((ImageView) helper.getView(R.id.iv));
                }
            }
        };

        gv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageItem item = adapter.getData().get(position);
                if (item.isAdd) {
                    showPhotoWindow();
                } else {
                    ArrayList<ImageItem> data = new ArrayList<ImageItem>(adapter.getData());
                    if (data.contains(imageAdd)) {
                        data.remove(imageAdd);
                    }

                    Intent intent = new Intent(MainActivity.this, PreviewActivity.class);
                    intent.putExtra(PreviewActivity.KEY_PHOTO_DATA, data);
                    intent.putExtra(PreviewActivity.KEY_SHOW_DEL_BTN, true);
                    intent.putExtra(PreviewActivity.KEY_CURRENT_PAGE, position);
                    startActivity(intent);
                }
            }
        });

        gv.setAdapter(adapter);

        photoWindow = new PhotoWindow(this);
        photoWindow.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                setWindowAlpha(1f);
            }
        });

    }

    /**
     * 选中图片的回调,eventbus
     */
    public void onEventMainThread(ChoosePhotoEvent event) {

        List<ImageItem> data = adapter.getData();

        if (data.contains(imageAdd)) {
            data.remove(imageAdd);
        }

        for (ImageItem it : event.data) {
            if (data.size() < 9) {
                data.add(it);
            } else {
                Toast.makeText(this, "最多只能添加9张图片", Toast.LENGTH_SHORT).show();
                break;
            }
        }
        if (adapter.getData().size() < 9 && !data.contains(imageAdd)) {
            adapter.add(imageAdd);
        }

        adapter.notifyDataSetChanged();

    }

    /**
     * 删除图片之后的回调，eventbus
     */
    public void onEventMainThread(RemovePhotoEvent event) {

        adapter.replaceAll(event.data);

        List<ImageItem> data = adapter.getData();

        if (data.size() < 9 && !data.contains(imageAdd)) {
            adapter.add(imageAdd);
        }
    }

    /**
     * 显示选择照片或者拍照的弹窗，调暗背景
     */
    protected void showPhotoWindow() {
        photoWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
        setWindowAlpha(0.3f);
    }

    private void setWindowAlpha(float alpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = alpha;
        getWindow().setAttributes(lp);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
