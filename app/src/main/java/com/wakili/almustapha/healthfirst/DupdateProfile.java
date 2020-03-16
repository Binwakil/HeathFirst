package com.wakili.almustapha.healthfirst;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import static services.Config.FIRE_BASE_USER_URL;

public class DupdateProfile extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_PICK = 2;

    private DatabaseReference db;
    private String email;
    private  String tname,temail, tphone, toffice, tqual, tspec, tabout, userId;
    private StorageReference mStorageRef;

    private ImageView mUserPhotoImageView, imgset;
    private Button licience;
    private static final int GALLERY_REQUEST_CODE = 2;
    private static final int GALLERY_REQUEST_CODE1 = 2;
    private Uri uri = null;
    private EditText textTitle, textDesc;
    private Button postBtn;
    private StorageReference storage;
    private FirebaseDatabase database;
    private DatabaseReference databaseRef;
    private DatabaseReference profiles;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private ProgressDialog pDialog;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dupdate_profile);
        DupdateProfile.this.setTitle("DOCTOR'S PROFILE UPDATE");

        auth = FirebaseAuth.getInstance();
        Firebase.setAndroidContext(this);
        user = auth.getCurrentUser();
        db = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance().getReference();
        userId = user.getUid();

        databaseRef = database.getInstance().getReference().child("Doctors").child("Profile").child(userId);


        final ImageView btnsave = (ImageView) findViewById(R.id.btnsave);
        final EditText etName = (EditText) findViewById(R.id.editName);
        final EditText etEmail = (EditText) findViewById(R.id.editEmail);
        final EditText etPhone = (EditText) findViewById(R.id.editPhone);
        final EditText etOffice = (EditText) findViewById(R.id.editoffice);
        final EditText etQual = (EditText) findViewById(R.id.editqualification);
        final EditText etSpec = (EditText) findViewById(R.id.editspecial);
        final EditText etAbut = (EditText) findViewById(R.id.editabout);
        final TextView pname = (TextView) findViewById(R.id.profilename);
        imgset = (ImageView) findViewById(R.id.profileimg);
        mUserPhotoImageView = (ImageView) findViewById(R.id.imageadd);
        licience = (Button)findViewById(R.id.btnupload);


        final FirebaseAuth mAuth = FirebaseAuth.getInstance();


        mDatabase = FirebaseDatabase.getInstance().getReference().child("Doctors").child("Profile");
        user = mAuth.getCurrentUser();




        final String uName = user.getDisplayName();
        final String uEmail = user.getEmail();
        etName.setText(uName);
        etEmail.setText(uEmail);
        pname.setText(uName);


        pDialog = new ProgressDialog(DupdateProfile.this);
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
                etAbut.setText(tabout);
                pname.setText(tname);

                Picasso.with(DupdateProfile.this).load(img).into(imgset);
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


        licience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DupdateProfile.this);
                builder.setTitle("Upload Licience");
                builder.setPositiveButton("Upload", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pickPhotoFromGallery();
                    }
                });
                builder.create().show();

            }
        });






        mUserPhotoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DupdateProfile.this);
                builder.setTitle("Change photo");
                builder.setMessage("Choose a method to change photo");
                builder.setPositiveButton("Upload", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pickPhotoFromGallery();
                    }
                });
                builder.setNegativeButton("Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dispatchTakePictureIntent();
                    }
                });
                builder.create().show();

            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DupdateProfile.this, "Uploading…", Toast.LENGTH_LONG).show();
                pDialog = new ProgressDialog(DupdateProfile.this);
                pDialog.setMessage("Please wait... Updating Your Profile");
                pDialog.setCancelable(false);
                pDialog.show();
                userId = user.getUid();
                tname = etName.getText().toString();
                temail = etEmail.getText().toString();
                tphone = etPhone.getText().toString();
                tqual = etQual.getText().toString();
                tspec = etSpec.getText().toString();
                tabout = etAbut.getText().toString();
                toffice = etOffice.getText().toString();

                // do a check for empty fields
                if (!TextUtils.isEmpty(tname) && !TextUtils.isEmpty(temail)){
                    StorageReference filepath = storage.child("profile_images").child(uri.getLastPathSegment());
                    filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                            @SuppressWarnings("VisibleForTests")
                            //getting the post image download url
                            final Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            final DatabaseReference newPost = databaseRef;
                            Toast.makeText(getApplicationContext(), "Succesfully Uploaded", Toast.LENGTH_SHORT).show();






                            //adding post contents to database reference
                            databaseRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    newPost.child("Name").setValue(tname);
                                    newPost.child("Email").setValue(temail);
                                    newPost.child("Phone").setValue(tphone);
                                    newPost.child("Office").setValue(toffice);
                                    newPost.child("Qualification").setValue(tqual);
                                    newPost.child("Specialization").setValue(tspec);
                                    newPost.child("About Us").setValue(tabout);
                                    newPost.child("imageUrl").setValue(downloadUrl.toString())
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                    //add user details in real time database
                                                    Firebase  reference = new Firebase(FIRE_BASE_USER_URL);
                                                    reference.child(user.getUid()).child("user_id").setValue(user.getUid());
                                                    reference.child(user.getUid()).child("name").setValue(tname);
                                                    reference.child(user.getUid()).child("email").setValue(temail);
                                                    reference.child(user.getUid()).child("phone").setValue(tphone);
                                                    reference.child(user.getUid()).child("office").setValue(toffice);
                                                    reference.child(user.getUid()).child("qualification").setValue(tqual);
                                                    reference.child(user.getUid()).child("specialization").setValue(tspec);
                                                    reference.child(user.getUid()).child("about").setValue(tabout);
                                                    reference.child(user.getUid()).child("imageUrl").setValue(downloadUrl.toString());
                                                    reference.child(user.getUid()).child("medicallicienceURL").setValue(downloadUrl.toString());

                                                    if (task.isSuccessful()){
                                                        if (pDialog.isShowing())
                                                            pDialog.dismiss();
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(DupdateProfile.this);
                                                        builder.setMessage("Recorder Saved Successfully")
                                                                .setTitle(tname);
                                                        builder.show();

                                                        Intent intent = new Intent(DupdateProfile.this, Home.class);
                                                        startActivity(intent);
                                                    }}});}
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                } }); } }); }}}); }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //image from gallery result
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            uri = data.getData();
            imgset.setImageURI(uri);
        }
        else if (requestCode == GALLERY_REQUEST_CODE1 && resultCode == RESULT_OK) {
            uri = data.getData();
            imgset.setImageURI(uri);
        }

    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    private void pickPhotoFromGallery(){
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE1);
    }


    }

