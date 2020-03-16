package com.wakili.almustapha.healthfirst;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class UserDetails extends AppCompatActivity {

    private DatabaseReference databaseRef, mDatabaseUsers;
    private FirebaseUser user;
    private FirebaseAuth auth;
    String a, b, c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        a =user.getDisplayName().toString();
        b = user.getPhotoUrl().toString();

        Intent intent = getIntent();

        String contactName = intent.getStringExtra("username");
        TextView nameTextView = (TextView)findViewById(R.id.profilename);
        nameTextView.setText(contactName);

        c = intent.getStringExtra("user_id");

        String DtName = intent.getStringExtra("Name");
        TextView DtV = (TextView)findViewById(R.id.txtName);
        DtV.setText(DtName);

        String contactImage = intent.getStringExtra("profile_pic");
        ImageView contactImageView = (ImageView) findViewById(R.id.profileimg);
        Picasso.with(getApplicationContext())
                .load(contactImage)
                .resize(50, 50)
                .into(contactImageView);

        String Dphone = intent.getStringExtra("Email");
        TextView dtxthone = (TextView) findViewById(R.id.txtEmail);
        dtxthone.setText(Dphone);


        String Dword = intent.getStringExtra("Phone");
        TextView dtxtword = (TextView) findViewById(R.id.txtphone);
        dtxtword.setText(Dword);

        String Demail = intent.getStringExtra("Address");
        TextView DtxtEmail = (TextView) findViewById(R.id.txtadd);
        DtxtEmail.setText(Demail);

        String Shub = intent.getStringExtra("Genotype");
        TextView txthubV = (TextView) findViewById(R.id.txtgenotype);
        txthubV.setText(Shub);

        String SArea = intent.getStringExtra("Bloodgroup");
        TextView txtarea = (TextView) findViewById(R.id.txtbloodgroup);
        txtarea.setText(SArea);


        String Sab = intent.getStringExtra("Next");
        TextView tab = (TextView) findViewById(R.id.txtnextofkin);
        txtarea.setText(Sab);
Toast.makeText(UserDetails.this, Sab, Toast.LENGTH_SHORT).show();
/**
 String Dgit = intent.getStringExtra("github");
 TextView Vgit = (TextView) findViewById(R.id.github);
 Vgit.setText(Dgit);




 /**

 String Special = intent.getStringExtra("special");
 TextView txtspecial = (TextView) findViewById(R.id.txtSpecialization);
 txtspecial.setText(Special);
 */

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.healthreportmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.nav_addreport) {

            auth = FirebaseAuth.getInstance();
            user = auth.getCurrentUser();
            databaseRef = FirebaseDatabase.getInstance().getReference().child("HealthReports");
            mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("HealthReports").child(user.getUid());


            Context context = UserDetails.this;
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);

            final EditText titleBox = new EditText(context);
            titleBox.setHint("Title");
            layout.addView(titleBox);

            final EditText descriptionBox = new EditText(context);
            descriptionBox.setHint("Health Report");
            layout.addView(descriptionBox);

            final String userId = user.getUid();


            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Send Health Report")
                    .setView(layout)
                    .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            final String title = String.valueOf(titleBox.getText());
                            final String question = descriptionBox.getText().toString();

                            final String mtuser = user.getUid();
                            final DatabaseReference newQuestion = databaseRef.push();

                            // do a check for empty fields
                            if (!TextUtils.isEmpty(question) && !TextUtils.isEmpty(mtuser)){

                                //adding post contents to database reference

                                mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        auth = FirebaseAuth.getInstance();
                                        user = auth.getCurrentUser();

                                        newQuestion.child("Title").setValue(title);
                                        newQuestion.child("Details").setValue(question);
                                        newQuestion.child("uid").setValue(c);
                                        newQuestion.child("photo_url").setValue(b);
                                        newQuestion.child("username").setValue(a)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){

                                                            AlertDialog.Builder builder = new AlertDialog.Builder(UserDetails.this);
                                                            builder.setMessage("U send Health Report")
                                                                    .setTitle("Suceess")
                                                                    .setPositiveButton(android.R.string.ok, null);
                                                            AlertDialog dialog = builder.create();
                                                            dialog.show();
                                                        }}});}
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Toast.makeText(UserDetails.this, databaseError.toString(), Toast.LENGTH_LONG).show();

                                    } });

                            }

                            titleBox.setText("");
                            descriptionBox.setText("");



                        }

                    })
                    .setNegativeButton("Cancel",null)
                    .create();
            dialog.show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}

