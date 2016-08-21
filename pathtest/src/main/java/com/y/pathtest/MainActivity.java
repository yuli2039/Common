package com.y.pathtest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private MyWave wave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wave = (MyWave) findViewById(R.id.wave);
    }

    int count = 0;

    public void btnRatio(View v) {
        if (count == 0)
            wave.setRatio(0.4f);
        else if (count == 1)
            wave.setRatio(0.8f);
        else if (count == 2) {
            wave.setRatio(0.4f);
            count = 0;
        }
        count++;
    }
}
