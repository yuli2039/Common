package com.yu.devlibrary.picker;

import android.net.Uri;

/**
 * @author yu
 *         Create on 16/4/20.
 */
public abstract class PickCallback {
    public abstract void onSelected(Uri uri);

    public void onError(int requestCode, Exception e) {

    }
}
