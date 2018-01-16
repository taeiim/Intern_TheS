package com.example.parktaeim.sensor_test_application;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;


/**
 * Created by parktaeim on 2018. 1. 11..
 */

public class SpeakerActivity extends AppCompatActivity {
    private TextView hzTextView;
    private SeekBar hzSeekBar;
    private Button playStopBtn;
    private AudioTrack tone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speaker);

        hzTextView = (TextView) findViewById(R.id.hzTextView);
        hzSeekBar = (SeekBar) findViewById(R.id.seekbar);
        playStopBtn = (Button) findViewById(R.id.playStopBtn);

        hzSeekBar.setMax(4020);
        hzSeekBar.setProgress(0);
        tone = generateTone(0,1000);

        hzSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tone = generateTone(progress,1000);
                hzTextView.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        playStopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(playStopBtn.getText().equals("PLAY")){
                    if(tone.getPlayState() == AudioTrack.PLAYSTATE_PLAYING){
                        tone = generateTone(hzSeekBar.getProgress(),1000);
                    }
                    tone.play();
                }
            }
        });

        Thread thread = new Thread(){
            @Override
            public void run() {
                super.run();
                Log.d("getPlayState===",String.valueOf(tone.getPlayState()));
                if(tone.getPlayState()==AudioTrack.PLAYSTATE_PLAYING){
                    playStopBtn.setText("PAUSE");
                }else {
                    playStopBtn.setText("PLAY");
                }
            }
        };
        thread.start();

    }

    private AudioTrack generateTone(double freqHz, int durationMs)
    {
        int count = (int)(44100.0 * 2.0 * (durationMs / 1000.0)) & ~1;
        short[] samples = new short[count];
        for(int i = 0; i < count; i += 2){
            short sample = (short)(Math.sin(2 * Math.PI * i / (44100.0 / freqHz)) * 0x7FFF);
            samples[i + 0] = sample;
            samples[i + 1] = sample;
        }
        AudioTrack track = new AudioTrack(AudioManager.STREAM_MUSIC, 44100,
                AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT,
                count * (Short.SIZE / 8), AudioTrack.MODE_STATIC);
        track.write(samples, 0, count);
        return track;
    }
}
