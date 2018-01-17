package com.example.parktaeim.sensor_test_application;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scichart.charting.model.dataSeries.XyDataSeries;
import com.scichart.charting.modifiers.ModifierGroup;
import com.scichart.charting.visuals.SciChartSurface;
import com.scichart.charting.visuals.annotations.HorizontalAnchorPoint;
import com.scichart.charting.visuals.annotations.TextAnnotation;
import com.scichart.charting.visuals.annotations.VerticalAnchorPoint;
import com.scichart.charting.visuals.axes.AutoRange;
import com.scichart.charting.visuals.axes.IAxis;
import com.scichart.charting.visuals.pointmarkers.EllipsePointMarker;
import com.scichart.charting.visuals.renderableSeries.IRenderableSeries;
import com.scichart.core.framework.UpdateSuspender;
import com.scichart.drawing.utility.ColorUtil;
import com.scichart.extensions.builders.SciChartBuilder;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by parktaeim on 2018. 1. 11..
 */

public class MicActivity extends AppCompatActivity {
    MediaRecorder recorder = new MediaRecorder();
    private TextView micDbTextView;
    SciChartBuilder sciChartBuilder;
    SciChartSurface sciChartSurface;
    XyDataSeries lineData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mic);

        sciChartSurface = new SciChartSurface(this);
        sciChartSurface = (SciChartSurface) findViewById(R.id.sciChartSurface);
        SciChartBuilder.init(this);
        sciChartBuilder = SciChartBuilder.instance();

        final IAxis xAxis = sciChartBuilder.newNumericAxis()
                .withAxisTitle("")
                .withVisibleRange(0, 30)
                .build();

        final IAxis yAxis = sciChartBuilder.newNumericAxis()
                .withAxisTitle("db")
                .withVisibleRange(0, 120)
                .withAutoRangeMode(AutoRange.Always)
                .build();

        TextAnnotation textAnnotation = sciChartBuilder.newTextAnnotation()
                .withX1(15)
                .withY1(80)
                .withHorizontalAnchorPoint(HorizontalAnchorPoint.Center)
                .withVerticalAnchorPoint(VerticalAnchorPoint.Center)
                .withFontStyle(25, ColorUtil.White)
                .build();

        ModifierGroup modifierGroup = sciChartBuilder.newModifierGroup()
                .withPinchZoomModifier().withReceiveHandledEvents(true).build()
                .withZoomPanModifier().withReceiveHandledEvents(true).build()
                .build();


        final int fifoCapacity = 150;
        lineData = sciChartBuilder.newXyDataSeries(Integer.class, Double.class)
                .withFifoCapacity(fifoCapacity)
                .build();

        final IRenderableSeries lineSeries = sciChartBuilder.newLineSeries()
                .withDataSeries(lineData)
                .withStrokeStyle(ColorUtil.Red, 2f, true)
                .build();

        sciChartSurface.getRenderableSeries().add(lineSeries);


        Collections.addAll(sciChartSurface.getYAxes(), yAxis);
        Collections.addAll(sciChartSurface.getXAxes(), xAxis);
        Collections.addAll(sciChartSurface.getAnnotations(), textAnnotation);
        Collections.addAll(sciChartSurface.getChartModifiers(), modifierGroup);


        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        Timer timer = new Timer();
        timer.schedule(new RecorderTask(recorder), 0, 50);
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

        int i = 0;
        public void run() {
            UpdateSuspender.using(sciChartSurface, new Runnable() {
                @Override
                public void run() {
                    int amplitude = recorder.getMaxAmplitude();
                    double amplitudeDb = 20 * Math.log10((double) Math.abs(amplitude));
                    Log.d("mic haha", String.valueOf(amplitudeDb));
                    if (String.valueOf(amplitudeDb).equals("-Infinity")) amplitudeDb = 0;

                    if (amplitudeDb != 0) {
                        micDbTextView.setText(String.format("%.2f", amplitudeDb));
                        lineData.append(i, amplitudeDb);
                    }
                    sciChartSurface.zoomExtents();

                    ++i;

                }
            });

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        recorder.stop();
        recorder.reset();
    }
}



