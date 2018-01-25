package com.example.parktaeim.settingsui.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.example.parktaeim.settingsui.R;

/**
 * Created by parktaeim on 2018. 1. 25..
 */

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        LinearLayout wifi = (LinearLayout) findViewById(R.id.settings_wifi);
        LinearLayout bluetooth = (LinearLayout) findViewById(R.id.settings_bluetooth);
        LinearLayout location = (LinearLayout) findViewById(R.id.settings_location);
        LinearLayout wallpaper = (LinearLayout) findViewById(R.id.device_wallpaper);
        LinearLayout capacity = (LinearLayout) findViewById(R.id.device_capacity);
        LinearLayout language = (LinearLayout) findViewById(R.id.device_language);
        LinearLayout info = (LinearLayout) findViewById(R.id.device_info);
        LinearLayout update = (LinearLayout) findViewById(R.id.device_software_update);
        LinearLayout reset = (LinearLayout) findViewById(R.id.device_reset);

        wifi.setOnClickListener(v-> startActivity(new Intent(getApplicationContext(),SettingsWifiActivity.class)));
        bluetooth.setOnClickListener(v-> startActivity(new Intent(getApplicationContext(),SettingsBluetoothActivity.class)));
        location.setOnClickListener(v-> startActivity(new Intent(getApplicationContext(),SettingsLocationActivity.class)));

        wallpaper.setOnClickListener(v-> startActivity(new Intent(getApplicationContext(),DeviceWallpaperActivity.class)));
        capacity.setOnClickListener(v-> startActivity(new Intent(getApplicationContext(),DeviceCapacityActivity.class)));
        language.setOnClickListener(v-> startActivity(new Intent(getApplicationContext(),DeviceLanguageActivity.class)));
        info.setOnClickListener(v-> startActivity(new Intent(getApplicationContext(),DeviceInfoActivity.class)));
        reset.setOnClickListener(v-> startActivity(new Intent(getApplicationContext(),DeviceResetActivity.class)));
    }

}
