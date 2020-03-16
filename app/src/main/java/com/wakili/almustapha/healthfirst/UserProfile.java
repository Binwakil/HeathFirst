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

public class UserProfile extends AppCompatActivity {
    private String email;
    private  String tname,temail, tphone, toffice, tqual, tspec, tabout, userId;
    private TextView etName,etEmail, etPhone, etAddress, etGenotype, etBlood, etNext, etpname, parare;
    private ImageView proimg, etedit;
    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        UserProfile.this.setTitle("MY PROFILE");



        final FirebaseAuth mAuth = FirebaseAuth.getInstance();


        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        user = mAuth.getCurrentUser();



        etName = (TextView) findViewById(R.id.txtName);
        etEmail = (TextView) findViewById(R.id.txtEmail);
        etPhone = (TextView) findViewById(R.id.txtphone);
        etAddress = (TextView) findViewById(R.id.txtadd);
        etGenotype = (TextView) findViewById(R.id.txtgenotype);
        etBlood = (TextView) findViewById(R.id.txtbloodgroup);
        etNext = (TextView) findViewById(R.id.txtnextofkin);
        etpname = (TextView) findViewById(R.id.profilename);
        etedit = (ImageView) findViewById(R.id.edit);
        proimg = (ImageView) findViewById(R.id.profileimg);
        parare = (TextView) findViewById(R.id.prare);


        final String uName = user.getDisplayName();
        final String uEmail = user.getEmail();
        etName.setText(uName);
        etEmail.setText(uEmail);
        etpname.setText(uName);
        dialog = new ProgressDialog(UserProfile.this);
        dialog.setMessage("Please wait... Loading Profile");
        dialog.setCancelable(false);
        dialog.show();

        mDatabase.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try{
                String tname = (String) dataSnapshot.child("name").getValue();
                String temail = (String) dataSnapshot.child("email").getValue();
                String tphone = (String) dataSnapshot.child("phone").getValue();
                String taddress = (String) dataSnapshot.child("address").getValue();
                String tgenotype = (String) dataSnapshot.child("genotype").getValue();
                String tblood = (String) dataSnapshot.child("bloodgroup").getValue();
                String tnext = (String) dataSnapshot.child("nextofkin").getValue();
                String img = (String) dataSnapshot.child("imageUrl").getValue();


                etName.setText(tname);
                etPhone.setText(tphone);
                etAddress.setText(taddress);
                etGenotype.setText(tgenotype);
                etBlood.setText(tblood);
                etNext.setText(tnext);
                etpname.setText(tname);

                Picasso.with(UserProfile.this).load(img).into(proimg);
            }
                catch (Exception e)
            {
                //Toast.makeText(Tips.this, e.toString(), Toast.LENGTH_SHORT).show();

            }

                if (dialog.isShowing())
                    dialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        etedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfile.this,UserSetProfile.class);
                startActivity(intent);
            }
        });
    }

}
