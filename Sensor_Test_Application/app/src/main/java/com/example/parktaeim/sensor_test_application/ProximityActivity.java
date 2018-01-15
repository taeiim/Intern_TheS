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

public class ProximityActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private Sensor proximitySensor;
    private SensorEventListener proximityListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proximity);

        // 뒤로가기
        ImageView backIcon = (ImageView) findViewById(R.id.proximity_icon_back);
        backIcon.setOnClickListener(v -> finish());

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        proximityListener = new proximityListener();


    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(proximityListener,proximitySensor,SensorManager.SENSOR_PROXIMITY);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(proximityListener);
    }

    private class proximityListener implements SensorEventListener{
        @Override
        public void onSensorChanged(SensorEvent event) {
            LinearLayout proximityLayout = (LinearLayout) findViewById(R.id.proximityLayout);
            TextView proximityTextView = (TextView) findViewById(R.id.proximityTextView);
            TextView cmTextView = (TextView) findViewById(R.id.cmTextView);
            proximityTextView.setText(String.format("%.0f",event.values[0]));

            Log.d("proximity max",String.valueOf(proximitySensor.getMaximumRange()));
            // 백그라운드--근접하였을 때 검은색, 멀리 떨어졌을때 흰색으로
            float proximityValue = event.values[0];
            float a = 255/proximitySensor.getMaximumRange();
            int alpha = (int) ((int) 255-(a*proximityValue));
            if(alpha<0) alpha = 0;
            proximityLayout.setBackgroundColor(Color.argb(alpha,0,0,0));
            Log.d("alpha=====",String.valueOf(alpha));

            if(alpha>180){
                proximityTextView.setTextColor(Color.WHITE);
                cmTextView.setTextColor(Color.WHITE);
            }else{
                proximityTextView.setTextColor(Color.BLACK);
                cmTextView.setTextColor(Color.DKGRAY);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }
}
