package com.example.parktaeim.sensor_test_application;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;
import android.widget.VideoView;

import java.net.URI;

/**
 * Created by parktaeim on 2018. 1. 16..
 */

public class RstpActivity extends AppCompatActivity {

    private VideoView videoView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rstp);

        videoView = (VideoView) findViewById(R.id.videoView);

        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);

        String movieurl = "";
        if (movieurl.startsWith("rtsp://")) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(movieurl));
            startActivity(intent);
        }

        videoView.setVideoURI(Uri.parse(movieurl));
        videoView.requestFocus();
    }
}
