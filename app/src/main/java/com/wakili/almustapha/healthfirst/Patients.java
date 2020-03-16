package com.wakili.almustapha.healthfirst;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.wakili.almustapha.healthfirst.Models.DoctorsClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Patients extends AppCompatActivity {



    DoctorsClass[] doctorsArray;
    int[] imagesArray;
    String response = null;
    String line;
    DoctorsClass mdoctorsClass;
    int i;
    ListView mListView;

    private StorageReference storage;
    private FirebaseDatabase database;
    private DatabaseReference databaseRef, mDatabaseUsers;
    private FirebaseAuth auth;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patients);

        storage = FirebaseStorage.getInstance().getReference();
        databaseRef = database.getInstance().getReference().child("Doctors").child("Posts");

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Doctors").child("Posts").child(user.getUid()).push();

        mListView = (ListView) findViewById(R.id.mlistview);


        try{



            for (i = 0; i < 20; i++) {

                mdoctorsClass = new DoctorsClass();
                mdoctorsClass.setName("Name");
                mdoctorsClass.setPhone("Desc");
                mdoctorsClass.setEmail("Image");
                mdoctorsClass.setOffice("Uname");
                //mcontactClass.setPSp(jsonObject.getString("specialzation"));

                doctorsArray[i] = mdoctorsClass;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        CustomAdapter mCustomAdapter = new CustomAdapter() {
            @Override
            public int getCount() {
                return doctorsArray.length;
            }

            @Override
            public Object getItem(int position) {
                return doctorsArray[position];
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }
        };

        mListView.setAdapter(mCustomAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent mIntent = new Intent(Patients.this, Home.class);

                mIntent.putExtra("title", doctorsArray[position].getName());
                mIntent.putExtra("desc", doctorsArray[position].getPhone());
                mIntent.putExtra("image", doctorsArray[position].getEmail());
                mIntent.putExtra("uname", doctorsArray[position].getOffice());


                startActivity(mIntent);
            }
        });
    }

    public abstract class CustomAdapter extends BaseAdapter {
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.doctors_layout, parent,
                        false);


                DoctorsClass postClass = (DoctorsClass) getItem(position);

                String hoto;
                hoto = postClass.getImageurl();

                ((TextView) convertView.findViewById(R.id.d_name)).
                        setText(postClass.getName());


                ((TextView) convertView.findViewById(R.id.d_phone)).
                        setText(postClass.getPhone());

                ((TextView) convertView.findViewById(R.id.d_email)).
                        setText(postClass.getEmail());

                ((TextView) convertView.findViewById(R.id.d_office)).
                        setText(postClass.getOffice());

            }
            return convertView;
        }
    }
}
