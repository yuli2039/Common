package com.yu.retrofittest;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxCompoundButton;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.yu.retrofittest.entity.GankEn;
import com.yu.retrofittest.entity.LoginEn;
import com.yu.retrofittest.http.ApiService;
import com.yu.retrofittest.http.HttpMethods;
import com.yu.retrofittest.presenter.BasePresenter;
import com.yu.retrofittest.rx.ApiSubscriber;
import com.yu.retrofittest.rx.DefaultTransformer;
import com.yu.retrofittest.rx.bus.RxBus;
import com.yu.retrofittest.rx.bus.Subscribe;
import com.yu.retrofittest.view.BaseActivity;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;
import rx.functions.Func1;

public class MainActivity extends BaseActivity {

    private EditText et;
    private Button btn;
    private Button btnJump;
    private CheckBox cb;
    private TextView tv;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void init() {
        RxBus.getInstance().register(this);// 注册rxbus
        et = (EditText) findViewById(R.id.et);
        btn = (Button) findViewById(R.id.btn);
        btnJump = (Button) findViewById(R.id.btnJump);
        cb = (CheckBox) findViewById(R.id.cb);
        tv = (TextView) findViewById(R.id.tv);

        /*
         * test rxbinding
         */
        RxView.clicks(btn)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
//                        Toast.makeText(MainActivity.this, "yuu", Toast.LENGTH_SHORT).show();
                        btnSend();
                    }
                });
        RxView.clicks(btnJump)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(MainActivity.this, TestRxBusAty.class));
                    }
                });

        RxCompoundButton.checkedChanges(cb)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        btn.setEnabled(aBoolean);
                        btn.setBackgroundColor(aBoolean ? Color.parseColor("#ff0000") : Color.parseColor("#ffff00"));
                    }
                });

        RxTextView.textChanges(et)
                .map(new Func1<CharSequence, String>() {
                    @Override
                    public String call(CharSequence charSequence) {
                        return charSequence.toString() + "_haha";
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        tv.setText(s);
                    }
                });
    }

    /**
     * test rxbus
     */
    @Subscribe
    public void receviceStr(LoginEn en) {
        btn.setText(en.event);
    }

    /**
     * test rx + retrofit
     */
    public void btnSend() {

        ApiService service = HttpMethods.getInstance().createService(ApiService.class);

        service.gank(10, 1)
                .compose(DefaultTransformer.<GankEn>create())// 线程切换
                .subscribe(new ApiSubscriber<GankEn>(MainActivity.this) {
                    @Override
                    public void onNext(GankEn gn) {
                        if (gn.getResults() != null && !gn.getResults().isEmpty())
                            Log.e("onNext", gn.getResults().get(0).getDesc());
                    }
                });
    }

    @Override
    protected void onDestroy() {
        RxBus.getInstance().unRegister(this);
        super.onDestroy();
    }

}