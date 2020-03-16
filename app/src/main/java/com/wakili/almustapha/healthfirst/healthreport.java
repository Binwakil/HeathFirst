package com.wakili.almustapha.healthfirst;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wakili.almustapha.healthfirst.Adapters.TipsListAdapter;
import com.wakili.almustapha.healthfirst.Models.ChatListDataItem;
import com.wakili.almustapha.healthfirst.Models.TipsListDataItem;
import com.wakili.almustapha.healthfirst.Util.MyDividerItemDecoration;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class healthreport extends AppCompatActivity {
    private List<TipsListDataItem> ChatList;
    private TipsListAdapter ChatAdapter;
    private RecyclerView.LayoutManager mLayoutManager;



    private FirebaseUser user;
    private FirebaseAuth auth;
    RecyclerView ChatRecyclerView;
    Firebase UserReference;
    private TextView texttitle;

    String userRown = "1";
    String a, b;
    private DatabaseReference databaseRef, mDatabaseUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healthreport);
        healthreport.this.setTitle("MY HEALTH REPORT");


        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        a =user.getDisplayName().toString();
        b = user.getPhotoUrl().toString();


        user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase MDR = FirebaseDatabase.getInstance();
        DatabaseReference DR =    MDR.getReference("users");

        Firebase.setAndroidContext(this);
        ChatRecyclerView = (RecyclerView) findViewById(R.id.HreportRecycler);
        user = FirebaseAuth.getInstance().getCurrentUser();
        //get firebase auth instance
        final ProgressBar progressBar;

        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        auth = FirebaseAuth.getInstance();
        ChatList =  new ArrayList<>();


        mLayoutManager = new LinearLayoutManager(this);




        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();

        final ChatListDataItem userDetail = new ChatListDataItem();
        DatabaseReference databaseReference =    mFirebaseDatabase.getReference("HealthReports");
        DatabaseReference mDatabase =    mFirebaseDatabase.getReference("HealthReports");

        //getting user details by id

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Userlist = new ArrayList<String>();

                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {

                    //int rowsss = Integer.valueOf(userRown);
                    TipsListDataItem tiplist = childDataSnapshot.getValue(TipsListDataItem.class);
                    //System.out.println("The updated post title is: " + tiplist.title);
                    // Toast.makeText(Tips.this, childDataSnapshot.child("photo_url").getValue().toString(), Toast.LENGTH_LONG).show();

                    //ChatList.add(tiplist);
                    /*ChatList.add(new TipsListDataItem(childDataSnapshot.getKey(),
                            childDataSnapshot.child("username").getValue().toString(),
                            childDataSnapshot.child("Title:").getValue().toString(),
                            childDataSnapshot.child("photo_url").getValue().toString(),
                            childDataSnapshot.child("photo_url").getValue().toString()));
                    //progressBar.setVisibility(ProgressBar.GONE);*/


                    //TipsListDataItem tiplist = childDataSnapshot.getValue(TipsListDataItem.class);
                    //System.out.println("The updated post title is: " + tiplist.title);
                    //Toast.makeText(Tips.this, childDataSnapshot.child("tip").getValue().toString(), Toast.LENGTH_LONG).show();

                    //ChatList.add(tiplist);
                    
                    if (user.getUid().toString().equals(childDataSnapshot.child("uid").getValue().toString())){
                    ChatList.add(new TipsListDataItem(childDataSnapshot.getKey(),
                                childDataSnapshot.child("username").getValue().toString(),
                                childDataSnapshot.child("photo_url").getValue().toString(),
                                childDataSnapshot.child("Title").getValue().toString(),
                                childDataSnapshot.child("Details").getValue().toString()));}





                }


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(healthreport.this, databaseError.toString(), Toast.LENGTH_LONG).show();
            }
        });



        ChatAdapter = new TipsListAdapter(healthreport.this, healthreport.this, ChatList);
        ChatRecyclerView.setLayoutManager(mLayoutManager);
        ChatRecyclerView.setItemAnimator(new DefaultItemAnimator());
        ChatRecyclerView.addItemDecoration(new MyDividerItemDecoration(healthreport.this, LinearLayoutManager.VERTICAL, 16));
        ChatRecyclerView.setAdapter(ChatAdapter);
    }
}
