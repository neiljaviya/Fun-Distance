package com.example.walkingdistance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Initial extends AppCompatActivity {

    Button next;
    EditText dir,dis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);

        dis = findViewById(R.id.distance);
        dir = findViewById(R.id.direction);
        next = findViewById(R.id.button);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dir.getText().toString().matches("")) {
                    Toast.makeText(Initial.this, "Enter Direction", Toast.LENGTH_SHORT).show();
                }
                else if(dis.getText().toString().matches("")) {
                    Toast.makeText(Initial.this,"Enter Distance",Toast.LENGTH_SHORT).show();
                }
                else {
                    final String dr = dir.getText().toString().trim();
                    final String di = dis.getText().toString().trim();
                    float degree;
                    if(dr.equals("East")||dr.equals("east")){
                        degree = 90.0f;
                    }
                    else if(dr.equals("South")||dr.equals("south")){
                        degree = 180.0f;
                    }
                    else if(dr.equals("West")||dr.equals("west")){
                        degree = 270.0f;
                    }
                    else{
                        degree = 0.0f;
                    }
                    Intent i = new Intent(Initial.this, MainActivity.class);
                    i.putExtra("direction", degree);
                    i.putExtra("distance", Float.parseFloat(di));
                    startActivity(i);
                }
            }
        });



    }
}
