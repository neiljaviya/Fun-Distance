package com.example.walkingdistance;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements SensorEventListener, StepListener {
    private TextView TvSteps,tasker,status,TvOrientation;
    private StepDetector simpleStepDetector;
    private SensorManager sensorManager,mSensorManager;
    private Sensor accel;
    private static final String TEXT_NUM_STEPS = "Number of Steps: ";
    private int numSteps;
    Context context;
    private String random_task;
    final double distnace = 0.74;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Get an instance of the SensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);
        final String[] task = getApplicationContext().getResources().getStringArray(R.array.task);

        tasker = (TextView) findViewById(R.id.task);
        TvSteps = (TextView) findViewById(R.id.tv_steps);
        Button BtnStart = (Button) findViewById(R.id.btn_start);
        Button BtnStop = (Button) findViewById(R.id.btn_stop);
        TvOrientation = (TextView) findViewById(R.id.orientation);



        BtnStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                numSteps = 0;
                sensorManager.registerListener(MainActivity.this, accel, SensorManager.SENSOR_DELAY_FASTEST);
                mSensorManager.registerListener(MainActivity.this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_GAME);
                random_task = task[new Random().nextInt(task.length)];
                tasker.setText(random_task);
            }
        });


        BtnStop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                sensorManager.unregisterListener(MainActivity.this);
                mSensorManager.unregisterListener(MainActivity.this);

            }
        });



    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);
        }
        else if(event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            float degree = Math.round(event.values[0]);
            TvOrientation.setText(""+degree);
        }
    }

    @Override
    public void step(long timeNs) {
        numSteps++;
        status = (TextView) findViewById(R.id.status);
        if(random_task.equals("Walk 5 meters")) {
            if(numSteps*distnace >= 5.0) { status.setText("5 meter task completed"); }
        }
        else if(random_task.equals("Walk 10 meters")) {
            if(numSteps*distnace >= 10.0) { status.setText("10 meter task completed"); }
        }
        else if(random_task.equals("Walk 20 meters")) {
            if(numSteps*distnace >= 20.0) { status.setText("20 meter task completed"); }
        }
        TvSteps.setText(TEXT_NUM_STEPS + numSteps);
    }

}