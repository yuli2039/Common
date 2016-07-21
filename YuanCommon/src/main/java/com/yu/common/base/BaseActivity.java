package com.yu.common.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.io.Serializable;

/**
 * BaseActivity
 *
 * @author yu
 */
public abstract class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        beforeContentView();

//        /*
//         * 必须在第一个activity中将status设置为normal，否则将无限重启
//         * 应用被强杀或者低内存回收之后恢复，直接重启应用，避免fragment的重叠、对象为null等等一系列bug
//         */
//        if (AppTracker.getInstance().isForceKilled()) {
//            AppUtils.restartApplication();
//            LogUtils.e("====app is force killed====");
//            return;
//        }
        setContentView(getLayoutId());
        initViewAndEvent();
        obtainParam(getIntent());
        initData();
    }

    /**
     * 设置内容之前
     */
    protected abstract void beforeContentView();

    /**
     * 返回layout Id
     */
    protected abstract int getLayoutId();

    /**
     * 获取intent中传递的参数
     */
    protected abstract void obtainParam(Intent intent);

    /**
     * 初始化view和事件
     */
    protected abstract void initViewAndEvent();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    protected void jump(Class<? extends Activity> clazz) {
        jump(clazz, null, null);
    }

    protected void jump(Class<? extends Activity> clazz, String key, Serializable value) {
        Intent intent = new Intent(this, clazz);
        if (null != key && value != null)
            intent.putExtra(key, value);
        startActivity(intent);
    }

    protected void jump(Class<? extends Activity> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle && !bundle.isEmpty())
            intent.putExtras(bundle);
        startActivity(intent);
    }

}
