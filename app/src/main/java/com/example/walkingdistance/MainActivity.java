package com.example.walkingdistance;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements SensorEventListener, StepListener {
    private TextView TvSteps,tasker,status,TvOrientation,TvDir;
    private StepDetector simpleStepDetector;
    private SensorManager sensorManager,mSensorManager;
    private Sensor accel;
    private static final String TEXT_NUM_STEPS = "Number of Steps: ";
    private int numSteps;
    Context context;
    private String random_task;
    final double distnace = 0.74;
    private ImageView image,pointer;
    private float currentDegree = 0f;
    Intent i;
    float deg,dis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        i = getIntent();
        deg = i.getFloatExtra("direction",0.0f);
        dis = i.getFloatExtra("distance",0.0f);
        // Get an instance of the SensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);
        //final String[] task = getApplicationContext().getResources().getStringArray(R.array.task);

        tasker = (TextView) findViewById(R.id.task);
        TvSteps = (TextView) findViewById(R.id.tv_steps);
        Button BtnStart = (Button) findViewById(R.id.btn_start);
        Button BtnStop = (Button) findViewById(R.id.btn_stop);
        TvOrientation = (TextView) findViewById(R.id.orientation);
        image = (ImageView) findViewById(R.id.main_iv);
        image.setRotation(deg);
        pointer = (ImageView) findViewById(R.id.pointer);
        TvDir = (TextView) findViewById(R.id.Tv_dir);

        BtnStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                numSteps = 0;
                sensorManager.registerListener(MainActivity.this, accel, SensorManager.SENSOR_DELAY_FASTEST);
                mSensorManager.registerListener(MainActivity.this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_GAME);
                //random_task = task[new Random().nextInt(task.length)];
                tasker.setText(dis+" meters");
            }
        });

        BtnStop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                sensorManager.unregisterListener(MainActivity.this);
                mSensorManager.unregisterListener(MainActivity.this);
                Intent j = new Intent(getApplicationContext(),Initial.class);
                startActivity(j);

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
            if(degree >= deg-3 && degree <= deg+3)
            {
                TvDir.setTextColor(Color.GREEN);
                TvDir.setText("Correct Direction");
            }
            else
            {
                TvDir.setTextColor(Color.RED);
                TvDir.setText("Wrong Direction");
            }
            RotateAnimation ra = new RotateAnimation(
                    currentDegree,
                    -degree,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f);

            ra.setDuration(210);

            ra.setFillAfter(true);
            image.startAnimation(ra);
            currentDegree = -degree;
        }
    }

    @Override
    public void step(long timeNs) {
        numSteps++;
        status = (TextView) findViewById(R.id.status);

        if(numSteps*distnace >= dis) { status.setText(dis+" meter task completed"); }

        TvSteps.setText(TEXT_NUM_STEPS + numSteps);
    }

}