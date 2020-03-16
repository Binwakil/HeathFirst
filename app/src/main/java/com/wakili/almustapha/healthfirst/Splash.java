package com.wakili.almustapha.healthfirst;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Splash extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        final ProgressBar progressBar;

        progressBar = (ProgressBar)findViewById(R.id.progressBar1);
        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(500);

                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{

                    user = FirebaseAuth.getInstance().getCurrentUser();
                    auth = FirebaseAuth.getInstance();

                    if (auth.getCurrentUser() != null) {


                        user = FirebaseAuth.getInstance().getCurrentUser();
                        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference databaseReference =    mFirebaseDatabase.getReference("users");

                        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("users");
                        ref.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if(Integer.valueOf(dataSnapshot.child("user_row").getValue().toString()) == 1){
                                    Intent intent = new Intent(Splash.this, Home.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);

                                }

                                else if(Integer.valueOf(dataSnapshot.child("user_row").getValue().toString()) == 0){
                                    Intent intent = new Intent(Splash.this, PatientDB.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);

                                }


                                //}

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                    else{
                        Intent intent = new Intent(Splash.this, Menumain.class);
                        startActivity(intent);

                    }
                }
            }
        };
        timerThread.start();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }
}