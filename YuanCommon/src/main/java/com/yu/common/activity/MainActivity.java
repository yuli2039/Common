package com.yu.common.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.yu.common.R;
import com.yu.common.base.SimpleActivity;
import com.yu.devlibrary.picker.PhotoPicker;
import com.yu.devlibrary.picker.PickUtils;
import com.yu.devlibrary.picker.ResultHandler;


public class MainActivity extends SimpleActivity {

    private PhotoPicker picker;
    private ImageView iv;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViewAndEvent() {
        super.initViewAndEvent();

        iv = (ImageView) findViewById(R.id.iv);
        picker = new PhotoPicker(this);
    }

    public void btnCamera(View view) {
        picker.takePhoto()
//                .directory(Environment.getExternalStorageDirectory())
//                .fileName("hh.jpg")
//                .needCrop(true)
//                .cropParams(new CropParams(this))
                .build()
                .execute();
    }

    public void btnGallery(View view) {
        picker.selectFromGallery()
//                .needCrop(true)
//                .cropParams(new CropParams(this))
                .build()
                .execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        picker.handleResult(requestCode, resultCode, data, new ResultHandler() {
            @Override
            public void onPhotoSelected(Uri uri) {
                Bitmap bitmap = PickUtils.decodeUriAsBitmap(MainActivity.this, uri);
                iv.setImageBitmap(bitmap);
            }

            @Override
            public void onSelectFail(String msg) {

            }
        });
    }
}
