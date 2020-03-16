package com.wakili.almustapha.healthfirst;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wakili.almustapha.healthfirst.Models.DoctorsClass;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class Families extends AppCompatActivity {



    private RecyclerView recyclerView;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_families);


        //initialize recyclerview and FIrebase objects
        recyclerView = (RecyclerView)findViewById(R.id.recyclerfamilies);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Doctors").child("Profiles").child(mAuth.getCurrentUser().getUid());
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (mAuth.getCurrentUser()==null){
                    Intent loginIntent = new Intent(Families.this, Dsignup.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);startActivity(loginIntent);
                }
            }
        };
    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        FirebaseRecyclerAdapter<DoctorsClass, Families.BlogzoneViewHolder> FBRA = new FirebaseRecyclerAdapter<DoctorsClass, Families.BlogzoneViewHolder>(
                DoctorsClass.class,
                R.layout.doctors_layout,
                Families.BlogzoneViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(Families.BlogzoneViewHolder viewHolder, DoctorsClass model, int position) {
                viewHolder.setName(model.getName());
                viewHolder.setPhone(model.getPhone());
                viewHolder.setImageUrl(getApplicationContext(), model.getImageurl());
                viewHolder.setEmail(model.getEmail());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent singleActivity = new Intent(Families.this, PostDetails.class);
                        startActivity(singleActivity);
                    }
                });
            }
        };
        recyclerView.setAdapter(FBRA);
    }
    public static class BlogzoneViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public BlogzoneViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setName(String name){
            TextView post_title = mView.findViewById(R.id.d_name);
            post_title.setText(name);
        }
        public void setPhone(String phone){
            TextView dd_p = mView.findViewById(R.id.d_phone);
            dd_p.setText(phone);
        }
        public void setEmail(String email){
            TextView post_desc = mView.findViewById(R.id.d_email);
            post_desc.setText(email);
        }
        public void setOffice(String office){
            TextView dd_o = mView.findViewById(R.id.d_office);
            dd_o.setText(office);
        }
        public void setQualification(String qualification){
            TextView dd_q = mView.findViewById(R.id.d_qual);
            dd_q.setText(qualification);
        }
        public void setSpecialization(String specialization){
            TextView dd_s = mView.findViewById(R.id.d_special);
            dd_s.setText(specialization);
        }
        public void setAbout(String about){
            TextView dd_a = mView.findViewById(R.id.d_about);
            dd_a.setText(about);
        }
        public void setImageUrl(Context ctx, String imageUrl){
            ImageView post_image = mView.findViewById(R.id.Post_Image);
            Picasso.with(ctx).load(imageUrl).into(post_image);
        }
    }
}

