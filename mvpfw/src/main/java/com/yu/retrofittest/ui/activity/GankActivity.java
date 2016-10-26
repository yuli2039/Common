package com.yu.retrofittest.ui.activity;

import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.yu.retrofittest.R;
import com.yu.retrofittest.entity.GankEntity;
import com.yu.retrofittest.presenter.GankPresenterImpl;
import com.yu.retrofittest.presenter.IGank;
import com.yu.retrofittest.base.BaseActivity;

import java.util.List;

import rx.functions.Action1;

public class GankActivity extends BaseActivity<GankPresenterImpl> implements IGank.GankView {

    private Button btnGank;
    private TextView tv;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected GankPresenterImpl createPresenter() {
        return new GankPresenterImpl(this);
    }

    @Override
    protected void afterInitview() {
        btnGank = (Button) findViewById(R.id.btnGank);
        tv = (TextView) findViewById(R.id.tv);

        RxView.clicks(btnGank)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        mPresenter.loadData("10", "1");
                    }
                });
    }

    @Override
    public void loadSuccess(List<GankEntity> data) {
        Log.e("yu", data.size() + "___");
        tv.setText("data.size() = " + data.size());
    }
}