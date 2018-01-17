package com.example.parktaeim.sensor_test_application;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.PowerManager;
import android.renderscript.Sampler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.maps.LocationSource;
import com.hyperiontech.ledctl.VueLed;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private android.support.v7.widget.Toolbar toolbar;
    private SensorManager sensorManager;

    private SensorEventListener accelometerListener;
    private SensorEventListener gyroscopeListener;
    private SensorEventListener compassListener;
    private SensorEventListener proximityListener;
    private SensorEventListener lightListener;

    private Sensor accelometerSensor;
    private Sensor gyroscopeSensor;
    private Sensor proximitySensor;
    private Sensor lightSensor;
    private Sensor orientationSensor;

    private TextView accX, accZ, accY;
    private TextView gyroX, gyroZ, gyroY;
    private TextView compassR, compassP, compassA;
    private TextView proximityTv;
    private TextView lightTv;
    private TextView keyPressTextView;
    private TextView latTv, lonTv;

    private CardView accelometerCardView;
    private CardView gyroscopeCardView;
    private CardView compassCardView;
    private CardView proximityCardView;
    private CardView lightCardView;
    private CardView ledCardView;
    private CardView gpsCardView;
    private CardView keyCardView;
    private CardView speakerCardView;
    private CardView micCardView;


    private VueLed vueLed;
    private VueLed vueLed1;
    private VueLed vueLed2;
    private VueLed flash;
    private TextView colorTextView;

    private BroadcastReceiver broadcastReceiver;

    MediaRecorder recorder = new MediaRecorder();
    private TextView micDbTextView;

    private static final String[] LOCATION_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 툴바 설정
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        // textView
        accX = (TextView) findViewById(R.id.accelometer_x);
        accZ = (TextView) findViewById(R.id.accelometer_z);
        accY = (TextView) findViewById(R.id.accelometer_y);
        gyroX = (TextView) findViewById(R.id.gyroscope_x);
        gyroZ = (TextView) findViewById(R.id.gyroscope_z);
        gyroY = (TextView) findViewById(R.id.gyroscope_y);
        proximityTv = (TextView) findViewById(R.id.proximity_cm);
        lightTv = (TextView) findViewById(R.id.light_lux);
        compassA = (TextView) findViewById(R.id.compass_azimuth);
        compassP = (TextView) findViewById(R.id.compass_pitch);
        compassR = (TextView) findViewById(R.id.compass_roll);
        latTv = (TextView) findViewById(R.id.latTextView);
        lonTv = (TextView) findViewById(R.id.lonTextView);
        colorTextView = (TextView) findViewById(R.id.colorTextView);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        //Sensor
        accelometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        orientationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

        if (lightSensor == null) {
            lightTv.setText("Light Sensor is NULL!!!!!!!!");
            lightTv.setGravity(Gravity.CENTER);
            TextView luxTv = (TextView) findViewById(R.id.luxTv);
            luxTv.setVisibility(View.GONE);
        }

        //Listener
        accelometerListener = new accelometerListener();
        gyroscopeListener = new gyroscopeListener();
        proximityListener = new proximityListener();
        lightListener = new lightListener();
        compassListener = new compassListener();

        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);
        for (int i = 0; i < sensorList.size(); i++) {
            Sensor s = sensorList.get(i);
            Log.d("sensor name==", s.getName());

        }

        accelometerCardView = (CardView) findViewById(R.id.accelometerCardView);
        gyroscopeCardView = (CardView) findViewById(R.id.gyroscopeCardView);
        compassCardView = (CardView) findViewById(R.id.compassCardView);
        proximityCardView = (CardView) findViewById(R.id.proximityCardView);
        lightCardView = (CardView) findViewById(R.id.lightCardView);
        ledCardView = (CardView) findViewById(R.id.ledCardView);
        gpsCardView = (CardView) findViewById(R.id.gpsCardView);
        keyCardView = (CardView) findViewById(R.id.keyPressCardView);
        speakerCardView = (CardView) findViewById(R.id.speakerCardView);
        micCardView = (CardView) findViewById(R.id.micCardView);
        keyPressTextView = (TextView) findViewById(R.id.keyPressTextView);

        accelometerCardView.setOnClickListener(this);
        gyroscopeCardView.setOnClickListener(this);
        compassCardView.setOnClickListener(this);
        proximityCardView.setOnClickListener(this);
        lightCardView.setOnClickListener(this);
        ledCardView.setOnClickListener(this);
        gpsCardView.setOnClickListener(this);
        keyCardView.setOnClickListener(this);
        speakerCardView.setOnClickListener(this);
        micCardView.setOnClickListener(this);

        vueLed = new VueLed("red");
        vueLed1 = new VueLed("green");
        vueLed2 = new VueLed("blue");
        flash = new VueLed("flash");


        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER) && locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, LOCATION_PERMS, 2);

            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 10, locationListener);
        }

        if (locationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER) && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);


    }

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            latTv.setText(String.valueOf(location.getLatitude()));
            lonTv.setText(String.valueOf(location.getLongitude()));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "NonSleep");
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_SCREEN_OFF);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
                wakeLock.acquire();
                wakeLock.release();

                keyPressTextView.setText("전원 버튼");

                return;
            }
        };
        registerReceiver(broadcastReceiver, intentFilter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(accelometerListener, accelometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(gyroscopeListener, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(proximityListener, proximitySensor, SensorManager.SENSOR_PROXIMITY);
        sensorManager.registerListener(lightListener, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(compassListener, orientationSensor, SensorManager.SENSOR_DELAY_NORMAL);

        setMicDB();

    }

    @Override
    protected void onPause() {
        super.onPause();

        sensorManager.unregisterListener(accelometerListener);
        sensorManager.unregisterListener(gyroscopeListener);
        sensorManager.unregisterListener(proximityListener);
        sensorManager.unregisterListener(lightListener);
        sensorManager.unregisterListener(compassListener);

        if (!colorTextView.getText().equals("OFF")) {
            vueLed.setState(0);
            vueLed1.setState(0);
            vueLed2.setState(0);
            flash.setState(0);
            colorTextView.setTextColor(Color.BLACK);
            colorTextView.setText("OFF");
        }

        try {
            recorder.stop();
            recorder.reset();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.d("key press event ===", String.valueOf(event));
        Log.d("key press getAction===", String.valueOf(event.getKeyCode()));

        if (event.getKeyCode() == 27) {
            keyPressTextView.setText("카메라 촬영 버튼(" + event.getKeyCode() + ")");

        } else {
            keyPressTextView.setText("KeyCode is " + event.getKeyCode());
        }

        return super.dispatchKeyEvent(event);
    }


    // CardView 클릭 시
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.accelometerCardView:
                break;
            case R.id.gyroscopeCardView:
                startActivity(new Intent(this, GyroscopeActivity.class));
                break;
            case R.id.compassCardView:
                startActivity(new Intent(this, CompassActivity.class));
                break;
            case R.id.proximityCardView:
                startActivity(new Intent(this, ProximityActivity.class));
                break;
            case R.id.lightCardView:
                startActivity(new Intent(this, LightActivity.class));
                break;
            case R.id.ledCardView:
                if (colorTextView.getText().equals("OFF")) {
                    // 빨간색 led ON
                    vueLed.setState(1);
                    colorTextView.setTextColor(Color.RED);
                    colorTextView.setText("RED");
                    break;

                } else if (colorTextView.getText().equals("RED")) {
                    // 빨간색 led OFF , 초록색 ON
                    vueLed.setState(0);
                    vueLed1.setState(1);
                    colorTextView.setTextColor(Color.GREEN);
                    colorTextView.setText("GREEN");
                    break;
                } else if (colorTextView.getText().equals("GREEN")) {
                    // 초록색 OFF, 파란색 ON
                    vueLed1.setState(0);
                    vueLed2.setState(1);
                    colorTextView.setTextColor(Color.BLUE);
                    colorTextView.setText("BLUE");
                    break;
                } else if (colorTextView.getText().equals("BLUE")) {
                    vueLed2.setState(0);
                    flash.setState(1);
                    colorTextView.setText("FLASH");
                    colorTextView.setTextColor(Color.YELLOW);
                } else if (colorTextView.getText().equals("FLASH")) {
                    flash.setState(0);
                    colorTextView.setTextColor(Color.BLACK);
                    colorTextView.setText("OFF");
                }

                break;

            case R.id.gpsCardView:
                startActivity(new Intent(this, GpsActivity.class));
                break;

            case R.id.keyPressCardView:
                startActivity(new Intent(this, KeyPressActivity.class));
                break;

            case R.id.speakerCardView:
                startActivity(new Intent(this, SpeakerActivity.class));
                break;

            case R.id.micCardView:
                startActivity(new Intent(this, MicActivity.class));
                break;
        }
    }

    // 가속도 센서 Listener -- 값 변할 때 마다 textView 바꿔줌
    private class accelometerListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent event) {
            accX.setText(String.format("%.2f", event.values[0]));
            accZ.setText(String.format("%.2f", event.values[2]));
            accY.setText(String.format("%.2f", event.values[1]));

            Log.d("acc sensor X:", String.valueOf(event.values[0]));
            Log.d("acc sensor Y:", String.valueOf(event.values[1]));
            Log.d("acc sensor Z:", String.valueOf(event.values[2]));

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    // 중력 센서
    private class gyroscopeListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent event) {
            gyroX.setText(String.format("%.2f", event.values[0]));
            gyroZ.setText(String.format("%.2f", event.values[2]));
            gyroY.setText(String.format("%.2f", event.values[1]));

            Log.d("gyro sensor X:", String.valueOf(event.values[0]));
            Log.d("gyro sensor Y:", String.valueOf(event.values[1]));
            Log.d("gyro sensor Z:", String.valueOf(event.values[2]));

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }


    // 근접 센서
    private class proximityListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent event) {
            proximityTv.setText(String.format("%.0f", event.values[0]));

            Log.d("proximity sensor:", String.valueOf(event.values[0]));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    // 조도 센서
    private class lightListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent event) {
            Log.d("light sensor listener", String.valueOf(event.sensor.getType()));
            if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
                Log.d("light Sensor====", "STATUS_UNRELIABLE!!!!!!");
            }
            if (event.sensor.getType() == Sensor.TYPE_LIGHT) {

                lightTv.setText(String.format("%.2f", event.values[0]));

                Log.d("light sensor :", String.valueOf(event.values[0]));
            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    private class compassListener implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent event) {
            compassA.setText(String.format("%.2f", event.values[0]));  // Azimuth (방위)
            compassP.setText(String.format("%.2f", event.values[1]));  // Pitch (경사도)
            compassR.setText(String.format("%.2f", event.values[2]));  // Roll (좌우 회전)

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    private void setMicDB() {
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile("/dev/null");
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new RecorderTask(recorder), 0, 500);

        try {
            recorder.prepare();
            recorder.start();

        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        micDbTextView = (TextView) findViewById(R.id.micDbTextView);

    }

    private class RecorderTask extends TimerTask {
        private MediaRecorder recorder;

        public RecorderTask(MediaRecorder recorder) {
            this.recorder = recorder;
        }

        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int amplitude = recorder.getMaxAmplitude();
                    double amplitudeDb = 20 * Math.log10((double) Math.abs(amplitude));
                    Log.d("mic amplitude", String.valueOf(amplitude));
                    Log.d("mic amplitudeDb", String.valueOf(amplitudeDb));
                    if (String.valueOf(amplitudeDb).equals("-Infinity")) amplitudeDb = 0;

                    micDbTextView.setText(String.format("%.2f", amplitudeDb));
                }
            });

        }
    }

    // 드롭메뉴
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.accelometer:
                Toast.makeText(this, "Accelometer", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.gyroscope:
                Toast.makeText(this, "Gyroscope", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.compass:
                Toast.makeText(this, "Compass", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.proximity:
                Toast.makeText(this, "Proximity", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.light:
                Toast.makeText(this, "Light", Toast.LENGTH_SHORT).show();
                return true;

        }

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}
