package com.wakili.almustapha.healthfirst;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Dlogin extends AppCompatActivity {

    protected EditText etEmail;
    protected EditText etPassword;
    protected Button btnLogin;
    protected TextView tvSignUp,DoctorSignUp;
    private FirebaseAuth auth;
    private ProgressDialog pDialog;


    private FirebaseUser user;
    Firebase UserReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dlogin);

        Dlogin.this.setTitle("HEALTH FIRST LOGIN");

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {

            user = FirebaseAuth.getInstance().getCurrentUser();
            auth = FirebaseAuth.getInstance();

            FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference  databaseReference =    mFirebaseDatabase.getReference("users");

            DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("users");
            ref.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    String userRown = "1";
                    int rowsss = Integer.valueOf(userRown);

                    if(Integer.valueOf(dataSnapshot.child("user_row").getValue().toString()) == 1){
                        Toast.makeText(Dlogin.this, "You Login as Doctor", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(Dlogin.this, Home.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                    }

                    else if(Integer.valueOf(dataSnapshot.child("user_row").getValue().toString()) == 0){
                        Toast.makeText(Dlogin.this, "You Login as User", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(Dlogin.this, PatientDB.class);
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

        tvSignUp = (TextView) findViewById(R.id.tvSignUp);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);


        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(Dlogin.this, Singupmenu.class);
                startActivity(intent);
            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();



                if (email.isEmpty() || password.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Dlogin.this);
                    builder.setMessage("Pls Enter Your Login Credential ")
                            .setTitle("Error ")
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    pDialog = new ProgressDialog(Dlogin.this);
                    pDialog.setMessage("Please wait... While logging in");
                    pDialog.setCancelable(false);
                    pDialog.show();

                    auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(Dlogin.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        user = FirebaseAuth.getInstance().getCurrentUser();
                                        auth = FirebaseAuth.getInstance();

                                        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
                                        DatabaseReference  databaseReference =    mFirebaseDatabase.getReference("users");

                                        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("users");
                                        ref.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {


                                                String userRown = "1";
                                                int rowsss = Integer.valueOf(userRown);

                                                if(Integer.valueOf(dataSnapshot.child("user_row").getValue().toString()) == 1){
                                                    Toast.makeText(Dlogin.this, "You Login as Doctor", Toast.LENGTH_SHORT).show();

                                                    Intent intent = new Intent(Dlogin.this, Home.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);

                                                }

                                                else if(Integer.valueOf(dataSnapshot.child("user_row").getValue().toString()) == 0){
                                                    Toast.makeText(Dlogin.this, "You Login as User", Toast.LENGTH_SHORT).show();

                                                    Intent intent = new Intent(Dlogin.this, PatientDB.class);
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


                                    } else {
                                        if (pDialog.isShowing())
                                            pDialog.dismiss();
                                        AlertDialog.Builder builder = new AlertDialog.Builder(Dlogin.this);
                                        builder.setMessage(task.getException().getMessage())
                                                .setTitle("Error ")
                                                .setPositiveButton(android.R.string.ok, null);
                                        AlertDialog dialog = builder.create();
                                        dialog.show();
                                    }
                                }
                            });
                }
            }
        });

    }
}
