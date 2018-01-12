package com.example.parktaeim.sensor_test_application;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.EventListener;

/**
 * Created by parktaeim on 2018. 1. 9..
 */

public class GyroscopeActivity extends AppCompatActivity {
    private SensorManager sensorManager;
    private Sensor gyroscopeSensor;
    private Sensor orientationSensor;
    private SensorEventListener gyroscopeListener;
    private SensorEventListener orientationListener;

    private TextView gyroX, gyroZ, gyroY;
    private ImageView ballImg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gyroscope);

        // 뒤로가기
        ImageView backIcon = (ImageView) findViewById(R.id.gyroscope_icon_back);
        backIcon.setOnClickListener(v -> finish());

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        orientationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        gyroscopeListener = new gyroscopeListener();
        orientationListener = new orientationListener();

        gyroX = (TextView) findViewById(R.id.gyroscope_x);
        gyroZ = (TextView) findViewById(R.id.gyroscope_z);
        gyroY = (TextView) findViewById(R.id.gyroscope_y);
        ballImg = (ImageView) findViewById(R.id.icon_ball);

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(gyroscopeListener,gyroscopeSensor,SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(orientationListener,orientationSensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(gyroscopeListener);
        sensorManager.unregisterListener(orientationListener);
    }

    private class gyroscopeListener implements SensorEventListener{
        @Override
        public void onSensorChanged(SensorEvent event) {
            gyroX.setText(String.format("%.2f", event.values[0]));
            gyroZ.setText(String.format("%.2f", event.values[2]));
            gyroY.setText(String.format("%.2f", event.values[1]));

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    private class orientationListener implements SensorEventListener{

        @Override
        public void onSensorChanged(SensorEvent event) {
            Animation translateAnimation = new TranslateAnimation(event.values[2],event.values[2],event.values[1],event.values[1]);
            translateAnimation.setFillAfter(true);
            translateAnimation.setFillEnabled(true);

            ballImg.startAnimation(translateAnimation);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }
}
