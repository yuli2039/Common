package com.yu.common.base;

import android.content.Intent;

/**
 * @author yu
 *         Create on 16/3/3.
 */
public abstract class SimpleActivity extends BaseActivity {

//    protected final String TAG = getClass().getSimpleName();
//    private LoadingDialog loading;

    @Override
    protected void beforeContentView() {
//        Falcon.setTag(TAG);
//        loading = new LoadingDialog(this);
    }

    @Override
    protected void initViewAndEvent() {
//        ButterKnife.bind(this);
    }

    @Override
    protected void obtainParam(Intent intent) {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onDestroy() {
//        Falcon.cancelByTag(TAG);
        super.onDestroy();
    }
}
