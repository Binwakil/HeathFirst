
package com.wakili.almustapha.healthfirst;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class DoctProfile extends AppCompatActivity {


    private String email;
    private  String tname,temail, tphone, toffice, tqual, tspec, tabout, userId;
    private TextView etName,etEmail, etPhone, etOffice, etQual, etSpec, etAbout, etpname, parare;
    private ImageView proimg, etedit;
    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doct_profile);
        DoctProfile.this.setTitle("DOCTORS PROFILE");



        final FirebaseAuth mAuth = FirebaseAuth.getInstance();


        mDatabase = FirebaseDatabase.getInstance().getReference().child("Doctors").child("Profile");
        user = mAuth.getCurrentUser();



         etName = (TextView) findViewById(R.id.txtName);
         etEmail = (TextView) findViewById(R.id.txtEmail);
         etPhone = (TextView) findViewById(R.id.txtphone);
         etOffice = (TextView) findViewById(R.id.txtadd);
         etQual = (TextView) findViewById(R.id.txtqualifi);
         etSpec = (TextView) findViewById(R.id.txtspecial);
         etAbout = (TextView) findViewById(R.id.txtabout);
         etpname = (TextView) findViewById(R.id.profilename);
         etedit = (ImageView) findViewById(R.id.edit);
         proimg = (ImageView) findViewById(R.id.profileimg);
          parare = (TextView) findViewById(R.id.prare);


        final String uName = user.getDisplayName();
        final String uEmail = user.getEmail();
        etName.setText(uName);
        etEmail.setText(uEmail);
        etpname.setText(uName);
        pDialog = new ProgressDialog(DoctProfile.this);
        pDialog.setMessage("Please wait... Loading Profile");
        pDialog.setCancelable(false);
        pDialog.show();

        mDatabase.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try{
                String tname = (String) dataSnapshot.child("Name").getValue();
                String temail = (String) dataSnapshot.child("Email").getValue();
                String tphone = (String) dataSnapshot.child("Phone").getValue();
                String Office = (String) dataSnapshot.child("Office").getValue();
                String tqual = (String) dataSnapshot.child("Qualification").getValue();
                String tspec = (String) dataSnapshot.child("Specialization").getValue();
                String tabout = (String) dataSnapshot.child("About Us").getValue();
                String img = (String) dataSnapshot.child("imageUrl").getValue();


                etName.setText(tname);
                etPhone.setText(tphone);
                etOffice.setText(toffice);
                etQual.setText(tqual);
                etSpec.setText(tspec);
                etAbout.setText(tabout);
                etpname.setText(tname);

                Picasso.with(DoctProfile.this).load(img).into(proimg);
            }
                catch (Exception e)
            {
                //Toast.makeText(Tips.this, e.toString(), Toast.LENGTH_SHORT).show();

            }

                if (pDialog.isShowing())
                    pDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




/*
        profiles.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String img = (String) dataSnapshot.child("imageUrl").getValue();
                tname = dataSnapshot.child("Name").getValue().toString();
                temail = dataSnapshot.child("Email").getValue().toString();
                tphone = dataSnapshot.child("Phone").getValue().toString();
                toffice = dataSnapshot.child("Office").getValue().toString();
                tqual = dataSnapshot.child("Qualification").getValue().toString();
                tspec = dataSnapshot.child("Specialization").getValue().toString();
                tabout = dataSnapshot.child("About Us").getValue().toString();

                etName.setText(tname);
                etPhone.setText(tphone);
                etOffice.setText(toffice);
                etQual.setText(tqual);
                etSpec.setText(tspec);
                etAbut.setText(tabout);


                Picasso.with(DoctProfile.this).load(img).into(proimg);
                if (auth.getCurrentUser().getUid().equals(userId)){
                    //deleteBtn.setVisibility(View.VISIBLE);
                } }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            } });



        userId = user.getUid();

        databaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //adapter.add((String) dataSnapshot.child("title").getValue()) ;

                Map<String, Object> newPost = (Map<String, Object>) dataSnapshot.getValue();
                tname = ("Author: " + newPost.get("Name"));
                temail = ("Title: " + newPost.get("Email"));

                String img = (String) dataSnapshot.child(userId).child("imageUrl").getValue();
                tname = dataSnapshot.child(userId).child("Name").getValue().toString();
                temail = dataSnapshot.child("Email").getValue().toString();
                tphone = dataSnapshot.child("Phone").getValue().toString();
                toffice = dataSnapshot.child("Office").getValue().toString();
                tqual = dataSnapshot.child("Qualification").getValue().toString();
                tspec = dataSnapshot.child("Specialization").getValue().toString();
                tabout = dataSnapshot.child("About Us").getValue().toString();

                etName.setText(tname);
                etPhone.setText(tphone);
                etOffice.setText(toffice);
                etQual.setText(tqual);
                etSpec.setText(tspec);
                etAbut.setText(tabout);


                Picasso.with(DoctProfile.this).load(img).into(proimg);

            }



            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                //adapter.remove((String) dataSnapshot.child("title").getValue());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
*/
        etedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctProfile.this,DupdateProfile.class);
                startActivity(intent);
            }
        });
    }

}
