package com.wakili.almustapha.healthfirst;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Singupmenu extends AppCompatActivity {
    Button doctor, userclient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singupmenu);

        doctor = (Button) findViewById(R.id.btnDoctor);
        doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = (new Intent(Singupmenu.this, Doctorsignup.class));
                startActivity(intent);

            }
        });

        userclient = (Button) findViewById(R.id.btnUser);
        userclient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = (new Intent(Singupmenu.this, Dsignup.class));
                startActivity(intent);

            }
        });
    }
}
