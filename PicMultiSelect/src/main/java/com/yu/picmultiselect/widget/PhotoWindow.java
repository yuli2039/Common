package com.yu.picmultiselect.widget;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

import com.yu.picmultiselect.R;
import com.yu.picmultiselect.album.BucketActivity;

/**
 * 图库或者拍照的弹窗
 */
public class PhotoWindow extends PopupWindow {

    public PhotoWindow(final Context mContext) {

        View view = View.inflate(mContext, R.layout.dialog_picker, null);
        //		LinearLayout ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
        //		ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.push_bottom_in_2));

        setWidth(LayoutParams.MATCH_PARENT);
        setHeight(LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(new ColorDrawable(0x000000));
        setFocusable(true);
        setOutsideTouchable(true);
        setContentView(view);

        view.findViewById(R.id.item_popupwindows_camera)
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View v) {
                        // TODO 拍照按钮
                        dismiss();
                    }
                });
        view.findViewById(R.id.item_popupwindows_Photo).setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(mContext, BucketActivity.class);
                mContext.startActivity(intent);
                dismiss();
            }
        });
        view.findViewById(R.id.item_popupwindows_cancel).setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                dismiss();
            }
        });

    }
}
