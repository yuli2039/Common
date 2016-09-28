package com.yu.devlibrary.picker;

import android.net.Uri;

/**
 * @author yu
 *         Create on 16/4/20.
 */
public abstract class ResultHandler {
    public abstract void onPhotoSelected(Uri uri);

    public void onSelectFail(String msg) {

    }
}
