package com.yu.retrofittest;

import android.app.Application;

import com.yu.retrofittest.util.AppManager;

/**
 * @author yu
 *         Create on 16/4/6.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AppManager.getInstance().init(this);
    }

}
