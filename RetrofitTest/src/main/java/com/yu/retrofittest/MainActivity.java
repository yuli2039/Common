package com.yu.retrofittest;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxCompoundButton;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.trello.rxlifecycle.ActivityEvent;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.yu.retrofittest.entity.GankEn;
import com.yu.retrofittest.entity.LoginEn;
import com.yu.retrofittest.http.ApiService;
import com.yu.retrofittest.http.HttpMethods;
import com.yu.retrofittest.rx.SchedulersCompat;
import com.yu.retrofittest.rx.SubscriberWL;
import com.yu.retrofittest.rx.bus.RxBus;
import com.yu.retrofittest.rx.bus.Subscribe;

import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

public class  MainActivity extends RxAppCompatActivity {

    private EditText et;
    private Button btn;
    private Button btnJump;
    private CheckBox cb;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                .retry(new Func2<Integer, Throwable, Boolean>() {// 设置重试次数小于2且是socket错误才重试
                    @Override
                    public Boolean call(Integer integer, Throwable throwable) {
                        return throwable instanceof SocketTimeoutException && integer < 2;
                    }
                })
                .compose(this.<GankEn>bindUntilEvent(ActivityEvent.DESTROY))// 绑定声明周期，防止内存泄露
                .compose(SchedulersCompat.<GankEn>applyExecutorSchedulers())// 线程切换
                // 如果确定是主线程订阅才能在这里显示loading，cancel时同时取消订阅
                .subscribe(new SubscriberWL<GankEn>(MainActivity.this, true) {
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