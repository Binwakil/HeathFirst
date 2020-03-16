package com.wakili.almustapha.healthfirst;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wakili.almustapha.healthfirst.Models.ChatListDataItem;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import static services.Config.FIRE_BASE_USER_URL;

public class Doctorsignup extends AppCompatActivity {

    protected EditText etPassword;
    protected EditText etEmail;
    protected EditText etUsername;
    protected Button btnSignup;
    private FirebaseAuth auth;
    protected TextView btnsignin;
    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctorsignup);
        Doctorsignup.this.setTitle("HEALTH FIRST SIGNUP");
        auth = FirebaseAuth.getInstance();
        Firebase.setAndroidContext(this);
        btnsignin = (TextView)findViewById(R.id.tvlogin);
        etPassword = (EditText)findViewById(R.id.etPassword);
        etEmail = (EditText)findViewById(R.id.etEmail);
        btnSignup = (Button)findViewById(R.id.btnsignup);
        etUsername = (EditText)findViewById(R.id.etName);

        btnsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Doctorsignup.this, Dlogin.class);
                startActivity(intent);
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = etPassword.getText().toString().trim();
                String email = etEmail.getText().toString().trim();

                if (password.isEmpty() || email.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Doctorsignup.this);
                    builder.setMessage("Error")
                            .setTitle("Error ")
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else if (!email.contains("@")|| !email.contains(".")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Doctorsignup.this);
                    builder.setMessage("the email address you entered is not valid You Most entered a valid Email Address!")
                            .setTitle("Invalid Email!")
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return;

                }
                else if (password.length() < 6) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Doctorsignup.this);
                    builder.setMessage("Password too short, enter minimum 6 characters!")
                            .setTitle("Invalid Password")
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return;

                } else {
                    pDialog = new ProgressDialog(Doctorsignup.this);
                    pDialog.setMessage("Please wait... While Creating your Account");
                    pDialog.setCancelable(false);
                    pDialog.show();


                    final String uname = etUsername.getText().toString();



                    auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(Doctorsignup.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        //add user details in real time database
                                        Firebase reference = new Firebase(FIRE_BASE_USER_URL);
                                        reference.child(user.getUid()).child("user_id").setValue(user.getUid());
                                        reference.child(user.getUid()).child("user_row").setValue(1);
                                        reference.child(user.getUid()).child("name").setValue(uname);
                                        reference.child(user.getUid()).child("imageUrl").setValue("https://example.com/jane-q-user/profile.jpg");

                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(uname)
                                                .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg")).build();
                                        user.updateProfile(profileUpdates);

                                        ChatListDataItem userDetail = new ChatListDataItem();
                                        userDetail.setActive_user_row("1");




                                        Intent intent = new Intent(Doctorsignup.this, DupdateProfile.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                        intent.putExtra("avatar1", "https://avatars2.githubusercontent.com/u/8110201?v=4");
                                        intent.putExtra("name1", uname);
                                        intent.putExtra("user_row", "1");
                                        intent.putExtra("id1", " Welecome");

                                        startActivity(intent);



                                    } else {
                                        if (pDialog.isShowing())
                                            pDialog.dismiss();
                                        AlertDialog.Builder builder = new AlertDialog.Builder(Doctorsignup.this);
                                        builder.setMessage(task.getException().getMessage())
                                                .setTitle("Error")
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