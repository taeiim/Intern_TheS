package com.example.parktaeim.sensor_test_application;

import android.Manifest;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

/**
 * Created by parktaeim on 2018. 1. 11..
 */

public class GpsActivity extends AppCompatActivity implements LocationListener, OnMapReadyCallback{

    private TextView latitudeTv;
    private TextView longitudeTv;
    private LocationManager locationManager;
    private double currentLat;
    private double currentLon;
    private ImageView mapIcon;
    private ImageView textIcon;
    private LinearLayout textLayout;

    private GoogleMap googleMap;
    private static final String[] LOCATION_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,LOCATION_PERMS,2);
        }

        for(int i=0;i<locationManager.getAllProviders().size();i++){
            Log.d("getAllProviders===",locationManager.getAllProviders().get(i));
            Log.d("getisProviderEnabled==",String.valueOf(locationManager.isProviderEnabled(locationManager.getAllProviders().get(i))));
        }
        if (locationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER )&& locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 10, this);

        if (locationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER) && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, this);

        latitudeTv = (TextView) findViewById(R.id.gpsLatitudeTextView);
        longitudeTv = (TextView) findViewById(R.id.gpsLongitudeTextView);
        mapIcon = (ImageView) findViewById(R.id.gpsMapIcon);
        textIcon = (ImageView) findViewById(R.id.gpsTextIcon);
        textLayout = (LinearLayout) findViewById(R.id.gpsTextLayout);

        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment)fragmentManager.findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mapIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapIcon.setVisibility(View.GONE);
                textIcon.setVisibility(View.VISIBLE);
                textLayout.setVisibility(View.GONE);
            }
        });

        textIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapIcon.setVisibility(View.VISIBLE);
                textIcon.setVisibility(View.GONE);
                textLayout.setVisibility(View.VISIBLE);

            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        latitudeTv.setText(String.valueOf(location.getLatitude()));
        longitudeTv.setText(String.valueOf(location.getLongitude()));
        Log.d("getLatitude===",String.valueOf(location.getLatitude()));
        Log.d("getLongitude---",String.valueOf(location.getLongitude()));
        currentLat = location.getLatitude();
        currentLon = location.getLongitude();

        setMap();
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

    public void setMap(){
        LatLng CURRENT = new LatLng(currentLat, currentLon);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(CURRENT);
        markerOptions.title("현재 위치");
        googleMap.addMarker(markerOptions);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(CURRENT));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }
}
