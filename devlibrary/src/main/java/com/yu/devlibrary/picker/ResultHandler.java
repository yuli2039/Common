package com.yu.devlibrary.picker;

import android.net.Uri;

/**
 * @author yu
 *         Create on 16/4/20.
 */
public interface ResultHandler {
    void onPhotoSelected(Uri uri);

    void onSelectFail(String msg);
}
