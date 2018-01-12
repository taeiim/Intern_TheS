package com.example.parktaeim.sensor_test_application;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by parktaeim on 2018. 1. 9..
 */

public class CompassActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private Sensor orientationSensor;
    private SensorEventListener compassListener;
    private TextView compassR, compassP, compassA;
    private TextView directionTextView, azimuthTextView;

    float[] gravity, geomagnetic;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);

        // 뒤로가기
        ImageView backIcon = (ImageView) findViewById(R.id.compass_icon_back);
        backIcon.setOnClickListener(v -> finish());

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        orientationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);  // 방위 센서

        compassListener = new compassListener();

        compassA = (TextView) findViewById(R.id.compass_azimuth);
        compassP = (TextView) findViewById(R.id.compass_pitch);
        compassR = (TextView) findViewById(R.id.compass_roll);
        directionTextView = (TextView) findViewById(R.id.directionTextView);
        azimuthTextView = (TextView) findViewById(R.id.azimuthTextView);

    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(compassListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(compassListener,orientationSensor,SensorManager.SENSOR_DELAY_UI);
    }

    private class compassListener implements SensorEventListener{
        @Override
        public void onSensorChanged(SensorEvent event) {

            // 나침반 이미지 회전
            float degree = Math.round(event.values[0]);
            ImageView compassImg = (ImageView) findViewById(R.id.compassImg);
            RotateAnimation rotateAnimation = new RotateAnimation(
                    -degree, -degree, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f
            );

            rotateAnimation.setFillAfter(true);
            compassImg.startAnimation(rotateAnimation);

            // Setting textView
            compassA.setText(String.format("%.2f", event.values[0]));  // Azimuth (방위)
            compassP.setText(String.format("%.2f", event.values[1]));  // Pitch (경사도)
            compassR.setText(String.format("%.2f", event.values[2]));  // Roll (좌우 회전)

            directionTextView.setText(getDirection(event.values[0]));
            azimuthTextView.setText(event.values[0]+" °");

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    private String getDirection(float degrees) {
        if(degrees >= 0 && degrees < 45) { return "N"; }
        if(degrees >= 45 && degrees < 90) { return "NE"; }
        if(degrees >= 90 && degrees < 135) { return "E"; }
        if(degrees >= 135 && degrees < 180) { return "SE"; }
        if(degrees >= 180 && degrees < 225) { return "S"; }
        if(degrees >= 225 && degrees < 270) { return "SW"; }
        if(degrees >= 270 && degrees < 315) { return "W"; }
        if(degrees >= 315 && degrees < 360) { return "NW"; }

        return null;
    }


}
