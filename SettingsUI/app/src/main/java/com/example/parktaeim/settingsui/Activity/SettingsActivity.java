package com.example.parktaeim.settingsui.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.parktaeim.settingsui.Adapter.SettingsRecyclerAdaper;
import com.example.parktaeim.settingsui.Model.SettingsRecyclerItem;
import com.example.parktaeim.settingsui.R;
import com.example.parktaeim.settingsui.RecyclerItemClickListener;

import java.util.ArrayList;

/**
 * Created by parktaeim on 2018. 1. 25..
 */

public class SettingsActivity extends AppCompatActivity {
    private RecyclerView settingsRecyclerView;
    private SettingsRecyclerAdaper adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<SettingsRecyclerItem> settingsItemArrayList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setTitle("설정");

        setRecycler();
    }

    private void setRecycler() {
        settingsRecyclerView = (RecyclerView) findViewById(R.id.settingsRecyclerView);

        layoutManager = new LinearLayoutManager(getApplicationContext());
        settingsRecyclerView.setLayoutManager(layoutManager);

        settingsItemArrayList.add(new SettingsRecyclerItem("Wi-Fi", "The-S"));
        settingsItemArrayList.add(new SettingsRecyclerItem("블루투스", "연결된 기기"));
        settingsItemArrayList.add(new SettingsRecyclerItem("위치"));

        adapter = new SettingsRecyclerAdaper(this, settingsItemArrayList);
        settingsRecyclerView.setAdapter(adapter);

        settingsRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), settingsRecyclerView, new RecyclerItemClickListener.OnItemclickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position) {
                    case 0: //wifi
                        startActivity(new Intent(getApplicationContext(), SettingsWifiActivity.class));
                        break;
                    case 1: //bluetooth
                        startActivity(new Intent(getApplicationContext(), SettingsBluetoothActivity.class));
                        break;
                    case 2: //location
                        startActivity(new Intent(getApplicationContext(), SettingsLocationActivity.class));
                        break;

                }
            }
        }));
    }


}
