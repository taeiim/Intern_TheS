package com.example.parktaeim.sensor_test_application;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;


/**
 * Created by parktaeim on 2018. 1. 11..
 */

public class SpeakerActivity extends AppCompatActivity {
    private TextView hzTextView;
    private SeekBar hzSeekBar;
    private Button playStopBtn;
    private AudioTrack tone;

    private Button changeTestBtn;
    private ImageView playSoundBtn;
    private ImageView pauseSoundBtn;
    private SeekBar soundSeekbar;

    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;

    LinearLayout hzTestLayout;
    LinearLayout soundTestLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speaker);

        hzTestLayout = (LinearLayout) findViewById(R.id.hzTestLayout);
        soundTestLayout = (LinearLayout) findViewById(R.id.soundTestLayout);

        changeTestBtn = (Button) findViewById(R.id.changeTestBtn);
        playSoundBtn = (ImageView) findViewById(R.id.icon_play);
        pauseSoundBtn = (ImageView) findViewById(R.id.icon_pause);
        soundSeekbar = (SeekBar) findViewById(R.id.soundSeekbar);

        hzTextView = (TextView) findViewById(R.id.hzTextView);
        hzSeekBar = (SeekBar) findViewById(R.id.hzSeekbar);
        playStopBtn = (Button) findViewById(R.id.playStopBtn);

        audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);

        mediaPlayer = MediaPlayer.create(this,R.raw.music);
        mediaPlayer.setLooping(true);

        setView();
        hzTester();
        soundTester();
    }

    private void hzTester() {
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

    }

    private void soundTester() {
        TextView volumeTv = (TextView) findViewById(R.id.volumeTextView);

        int currentVolume_max15 = audioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);
        int currentVolume = (int)((100.0/15.0)*currentVolume_max15);
        soundSeekbar.setProgress(currentVolume);
        volumeTv.setText(String.valueOf(currentVolume));

        playSoundBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mediaPlayer.start();
                playSoundBtn.setVisibility(View.GONE);
                pauseSoundBtn.setVisibility(View.VISIBLE);
            }
        });

        pauseSoundBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSoundBtn.setVisibility(View.VISIBLE);
                pauseSoundBtn.setVisibility(View.GONE);

                mediaPlayer.pause();
            }
        });

        soundSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,(int)(progress/100.0*15),0);
                Log.d("volume!!!!!!!!!!===",String.valueOf(progress/100.0*15));
                volumeTv.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void setView() {
        changeTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(changeTestBtn.getText().equals("음량 테스트")){
                    hzTestLayout.setVisibility(View.GONE);
                    soundTestLayout.setVisibility(View.VISIBLE);
                    changeTestBtn.setText("hz 테스트");

                } else if(changeTestBtn.getText().equals("hz 테스트")){
                    soundTestLayout.setVisibility(View.GONE);
                    hzTestLayout.setVisibility(View.VISIBLE);
                    changeTestBtn.setText("음량 테스트");

                    if(mediaPlayer.isPlaying()){
                        mediaPlayer.pause();
                        pauseSoundBtn.setVisibility(View.GONE);
                        playSoundBtn.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
