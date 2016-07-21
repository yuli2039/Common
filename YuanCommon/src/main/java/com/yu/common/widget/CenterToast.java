package com.yu.common.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yu.common.R;


/**
 * 位于屏幕中心，圆角的toast，屏蔽多次显示
 *
 * @author yu
 */
public class CenterToast {

    private static long oneTime = 0;
    private static long twoTime = 0;
    private static String msg = "";

    public static void show(Context context, String str) {

        if (TextUtils.isEmpty(str))
            return;

        TextView textView = (TextView) View.inflate(context, R.layout.toast_view, null);
        textView.setText(str);

        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(textView);

        if (msg.equals(str)) {
            twoTime = System.currentTimeMillis();
            if (twoTime - oneTime > Toast.LENGTH_SHORT) {
                toast.show();
                oneTime = twoTime;
            }
        } else {
            toast.show();
            oneTime = System.currentTimeMillis();
        }
    }

}
