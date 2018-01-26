package com.example.parktaeim.settingsui.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.parktaeim.settingsui.R;

/**
 * Created by parktaeim on 2018. 1. 25..
 */

public class DeviceResetActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_reset);

        setUpToolbar();
    }

    private void setUpToolbar() {
        TextView toolbarTv = (TextView) findViewById(R.id.toolbarTitle);
        toolbarTv.setText("VUECAM 초기화");
        LinearLayout backIcon = (LinearLayout) findViewById(R.id.toolbarBackIcon);
        backIcon.setOnClickListener(v -> finish());
    }

}
