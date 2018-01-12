package com.example.parktaeim.sensor_test_application;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by parktaeim on 2018. 1. 9..
 */

public class AccelometerActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelometer);

        // 뒤로가기
        ImageView backIcon = (ImageView) findViewById(R.id.accelometer_icon_back);
        backIcon.setOnClickListener(v -> finish());
    }
}
