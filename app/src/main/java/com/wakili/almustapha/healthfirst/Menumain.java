package com.wakili.almustapha.healthfirst;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Menumain extends AppCompatActivity {
    Button signin, signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menumain);

        signin = (Button) findViewById(R.id.Loginbtn);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = (new Intent(Menumain.this, Dlogin.class));
                startActivity(intent);

            }
        });

        signup = (Button) findViewById(R.id.Signupbtn);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = (new Intent(Menumain.this, Singupmenu.class));
                startActivity(intent);

            }
        });
    }
}
