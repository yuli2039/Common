package com.yu.retrofittest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxCompoundButton;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.trello.rxlifecycle.ActivityEvent;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.yu.retrofittest.http.ApiService;
import com.yu.retrofittest.http.HttpMethods;
import com.yu.retrofittest.rx.SchedulersCompat;
import com.yu.retrofittest.rx.bus.RxBus;
import com.yu.retrofittest.rx.bus.Subscribe;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

public class MainActivity extends RxAppCompatActivity {

    private ProgressDialog dia;
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

        Map<String, String> map = new HashMap<>();
        map.put("grant_type", "password");
        map.put("phone", "18108187310");
        map.put("password", et.getText().toString().trim());
        service.login(map)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
                .compose(SchedulersCompat.<LoginEn>applyExecutorSchedulers())// 线程切换
//                .doOnSubscribe(new Action0() {// 显示loading,方案1
//                    @Override
//                    public void call() {
//                        showLoading();
//                    }
//                })
//                .doOnUnsubscribe(new Action0() {
//                    @Override
//                    public void call() {
//                        dismissLoading();
//                    }
//                })
                .compose(this.<LoginEn>bindUntilEvent(ActivityEvent.DESTROY))// 绑定声明周期，防止内存泄露
                .subscribe(new Subscriber<LoginEn>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        showLoading();// 方案2，如果确定是主线程订阅才能在这里显示loading，还可以cancel时同时取消订阅
                    }

                    @Override
                    public void onCompleted() {
                        Log.e("tR", "onCompleted");
                        dismissLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("tR", "onError");
                        dismissLoading();
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(LoginEn o) {
                        Log.e("tR", "onNext");
                    }
                });

    }

    @Override
    protected void onDestroy() {
        RxBus.getInstance().unRegister(this);
        super.onDestroy();
    }

    private void showLoading() {
        if (dia == null)
            dia = new ProgressDialog(this);
        dia.show();
    }

    private void dismissLoading() {
        if (dia != null)
            dia.dismiss();
    }
}
