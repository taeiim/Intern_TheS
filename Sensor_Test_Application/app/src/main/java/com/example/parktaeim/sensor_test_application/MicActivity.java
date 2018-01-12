package com.example.parktaeim.sensor_test_application;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by parktaeim on 2018. 1. 11..
 */

public class MicActivity extends AppCompatActivity {
    MediaRecorder recorder = new MediaRecorder();
    private TextView micDbTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mic);

        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new RecorderTask(recorder), 0, 500);
        recorder.setOutputFile("/dev/null");

        try {
            recorder.prepare();
            recorder.start();

        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        micDbTextView = (TextView) findViewById(R.id.dbTextView);

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
                    double amplitudeDb = 20 * Math.log10((double)Math.abs(amplitude));
                    Log.d("mic haha",String.valueOf(amplitudeDb));
                    if(String.valueOf(amplitudeDb).equals("-Infinity")) amplitudeDb=0;

                    micDbTextView.setText(String.format("%.2f",amplitudeDb));
                }
            });

        }
    }

}



