package com.example.parktaeim.sensor_test_application;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by parktaeim on 2018. 1. 9..
 */

public class LightActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private Sensor lightSensor;
    private SensorEventListener lightListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        lightListener = new lightListener();

        // 뒤로가기
        ImageView backIcon = (ImageView) findViewById(R.id.light_icon_back);
        backIcon.setOnClickListener(v -> finish());

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(lightListener, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(lightListener);
    }

    // 조도 센서
    private class lightListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent event) {
            LinearLayout lightLayout = (LinearLayout) findViewById(R.id.lightLayout);
            TextView lightTextView = (TextView) findViewById(R.id.lightTextView);
            TextView luxTextView = (TextView) findViewById(R.id.luxTextView);

            if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
                // 백그라운드--조도 밝을 때 흰색, 어두울 때 검은색
                float lightValue = event.values[0];
                if(lightValue>lightSensor.getMaximumRange())  // 현재 밝기가 측정 범위보다 높으면 최대 측정값으로..
                    lightValue = lightSensor.getMaximumRange();
                lightTextView.setText(String.format("%.2f", lightValue));  // lux값 textView setting

                // lux -> alpha 변환 후 백그라운드에 색상 적용
                float a = 255/lightSensor.getMaximumRange();
                Log.d("light sensor max ==",String.valueOf(lightSensor.getMaximumRange()));
                int alpha = 255-(int)(a*lightValue);
                lightLayout.setBackgroundColor(Color.argb(alpha, 0, 0, 0));

                // 배경이 어느정도 어두워지면 글씨 white로 바꿔줌
                if (alpha > 130) {
                    lightTextView.setTextColor(Color.WHITE);
                    luxTextView.setTextColor(Color.WHITE);
                } else {
                    lightTextView.setTextColor(Color.BLACK);
                    luxTextView.setTextColor(Color.DKGRAY);
                }
            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }
}
