package com.yu.retrofittest.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.yu.retrofittest.R;
import com.yu.retrofittest.entity.LoginEn;
import com.yu.retrofittest.rx.bus.RxBus;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * @author yu
 *         Create on 16/7/13.
 */
public class TestRxBusAty extends AppCompatActivity {

    private EditText et;
    private Button btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        et = (EditText) findViewById(R.id.et);
        btn = (Button) findViewById(R.id.btn);

        RxView.clicks(btn)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .flatMap(new Func1<Void, Observable<String>>() {
                    @Override
                    public Observable<String> call(Void aVoid) {
                        return Observable.just(et.getText().toString());
                    }
                })
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        boolean empty = TextUtils.isEmpty(s);
                        if (empty)
                            Toast.makeText(TestRxBusAty.this, "text is empty", Toast.LENGTH_SHORT).show();
                        return !empty;
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        LoginEn en = new LoginEn(s);
                        RxBus.getInstance().post(en);
                        finish();
                    }
                });

    }
}
