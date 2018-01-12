package com.example.parktaeim.sensor_test_application;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Created by parktaeim on 2018. 1. 11..
 */

public class KeyPressActivity extends AppCompatActivity {

    private TextView keyTextView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keypress);

        keyTextView = (TextView) findViewById(R.id.keyTextView);

    }

    @Override
    protected void onStart() {
        super.onStart();
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK|PowerManager.ACQUIRE_CAUSES_WAKEUP|PowerManager.ON_AFTER_RELEASE, "NonSleep");
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_SCREEN_OFF);

        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
                wakeLock.acquire();
                wakeLock.release();

                keyTextView.setText("전원 버튼");

                return;
                                                                                        }
        };

        registerReceiver(broadcastReceiver, intentFilter);

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.d("key Press ==",String.valueOf(event));
        Log.d("key Press keyCode",String.valueOf(event.getKeyCode()));

        if(event.getKeyCode() == 24){
            keyTextView.setText("카메라 촬영 버튼 ("+event.getKeyCode()+")");
        } else {
            keyTextView.setText(event.toString());
        }

        return super.dispatchKeyEvent(event);
    }
}
