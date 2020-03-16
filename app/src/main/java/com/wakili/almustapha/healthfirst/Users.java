package com.wakili.almustapha.healthfirst;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wakili.almustapha.healthfirst.Adapters.ChatListAdapter;
import com.wakili.almustapha.healthfirst.Models.ChatListDataItem;
import com.wakili.almustapha.healthfirst.Util.MyDividerItemDecoration;
import com.wakili.almustapha.healthfirst.Util.RecyclerTouchListener;
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

import firebasechat.ChatActivity;
import firebasechat.UserDetails;

import static services.Config.GLOBAL_ADDRESS;
import static services.Config.NOTIFICATION_ID;

public class Users extends AppCompatActivity {
    private List<ChatListDataItem> ChatList;
    private ChatListAdapter ChatAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseUser user;
    private FirebaseAuth auth;
    RecyclerView ChatRecyclerView;
    Firebase UserReference;
    String userRown = "1";
    private  TextView texttitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase MDR = FirebaseDatabase.getInstance();
        DatabaseReference DR =    MDR.getReference("users");

        DatabaseReference ref1= FirebaseDatabase.getInstance().getReference().child("users");
        ref1.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(Integer.valueOf(dataSnapshot.child("user_row").getValue().toString()) == 1){

                    texttitle = (TextView)findViewById(R.id.userstitle);
                    texttitle.setText("APP USERS");

                }

                else if(Integer.valueOf(dataSnapshot.child("user_row").getValue().toString()) == 0){


                    texttitle = (TextView)findViewById(R.id.userstitle);
                    texttitle.setText("APP DOCTORS");
                }


                //}

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        Firebase.setAndroidContext(this);
        ChatRecyclerView = (RecyclerView) findViewById(R.id.ChatRecyler);
        user = FirebaseAuth.getInstance().getCurrentUser();
        //get firebase auth instance
        final ProgressBar progressBar;

        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        auth = FirebaseAuth.getInstance();
        ChatList =  new ArrayList<>();


        mLayoutManager = new LinearLayoutManager(this);




        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();

        final ChatListDataItem userDetail = new ChatListDataItem();
        DatabaseReference databaseReference =    mFirebaseDatabase.getReference("users");


        //get user unread notifications
        DatabaseReference chatref=FirebaseDatabase.getInstance().getReference().child("notifi");
        chatref.orderByChild("user_id").equalTo(user.getUid()).addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot datas: dataSnapshot.getChildren()){
                    String message=datas.child("message").getValue().toString();
                    String sender_name=datas.child("sender_name").getValue().toString();
                    String read_status=datas.child("read").getValue().toString();
                    String chatter_id=datas.child("chatter_user_id").getValue().toString();


                    if(!read_status.equals("true")) {


                        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(Users.this)
                                .setSmallIcon(R.drawable.bubble_in)
                                .setContentTitle("New Message from " + sender_name)
                                .setContentText(message)
                                .setOnlyAlertOnce(true)
                                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
                        mBuilder.setAutoCancel(true);
                        mBuilder.setLocalOnly(false);


                        // Create pending intent, mention the Activity which needs to be
                        //triggered when user clicks on notification(ChatActivity.class in this case)

                        PendingIntent contentIntent = PendingIntent.getActivity(Users.this, 0,
                                new Intent(Users.this, ChatActivity.class)
                                        .putExtra("user_id",chatter_id)
                                        .putExtra("username",sender_name), PendingIntent.FLAG_UPDATE_CURRENT);


                        mBuilder.setContentIntent(contentIntent);


                        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

                        Firebase NotificationReference = new Firebase(GLOBAL_ADDRESS + "notifi");
                        NotificationReference.child(user.getUid()).child("read").setValue("true");

                    }



                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });


        //getting user details by id
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("users");
        ref.orderByChild("user_id").equalTo(user.getUid()).addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot datas: dataSnapshot.getChildren()){
                    String keys=datas.child("user_row").getValue().toString();
                    userRown = keys;
                    //Toast.makeText(ChatUserActivity.this, "User Row: "+userRown + "Username: "+ datas.child("name").getValue().toString(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {

                    int rowsss = Integer.valueOf(userRown);


                    //if its not current login user
                    if(!user.getUid().equals(childDataSnapshot.getKey())){

                        if(rowsss != Integer.valueOf(childDataSnapshot.child("user_row").getValue().toString())){

                            ChatList.add(new ChatListDataItem(childDataSnapshot.getKey().toString(),childDataSnapshot.child("name").getValue().toString(),
                                    childDataSnapshot.child("imageUrl").getValue().toString()));
                            //progressBar.setVisibility(ProgressBar.GONE);
                        }}


                }


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Users.this, databaseError.toString(), Toast.LENGTH_LONG).show();
            }
        });


        ChatAdapter = new ChatListAdapter(Users.this, Users.this, ChatList);
        ChatRecyclerView.setLayoutManager(mLayoutManager);
        ChatRecyclerView.setItemAnimator(new DefaultItemAnimator());
        ChatRecyclerView.addItemDecoration(new MyDividerItemDecoration(Users.this, LinearLayoutManager.VERTICAL, 16));
        ChatRecyclerView.setAdapter(ChatAdapter);
        progressBar.setVisibility(View.GONE);

        /**
         * On long press on RecyclerView item, open alert dialog
         * with options to choose
         * Edit and Delete
         * */
        ChatRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(Users.this,
                ChatRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                final ChatListDataItem current = ChatList.get(position);
                //UserDetails.username = current.get(position);
                UserDetails.chatWith = current.user_id;
                user = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference =    mFirebaseDatabase.getReference("users");

                final DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("users");
                ref.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(Integer.valueOf(dataSnapshot.child("user_row").getValue().toString()) == 1){
                            DatabaseReference mDatabase;
                            mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
                            ref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    try {

                                    String tname = (String) dataSnapshot.child(current.user_id).child("name").getValue();
                                    String temail = (String) dataSnapshot.child(current.user_id).child("email").getValue();
                                    String tphone = (String) dataSnapshot.child(current.user_id).child("phone").getValue();
                                    String Office = (String) dataSnapshot.child(current.user_id).child("address").getValue();
                                    String tqual = (String) dataSnapshot.child(current.user_id).child("genotype").getValue();
                                    String tspec = (String) dataSnapshot.child(current.user_id).child("bloodgroup").getValue();
                                    String tabout = (String) dataSnapshot.child(current.user_id).child("next").getValue();
                                    String img = (String) dataSnapshot.child(current.user_id).child("imageUrl").getValue();
                                    Intent i = new Intent(Users.this, com.wakili.almustapha.healthfirst.UserDetails.class);
                                    i.putExtra("user_id", current.user_id);
                                    i.putExtra("username", current.username);
                                    i.putExtra("profile_pic", img);
                                    i.putExtra("Name", tname);
                                    i.putExtra("Email", temail);
                                    i.putExtra("Phone", tphone);
                                    i.putExtra("Address", Office);
                                    i.putExtra("Genotype", tqual);
                                    i.putExtra("Bloodgroup", tspec);
                                    i.putExtra("Next", tabout);

                                    startActivity(i);



                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Toast.makeText(Users.this,  e.toString(), Toast.LENGTH_SHORT).show();
                                    }



                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            Intent intent = new Intent(Users.this, Home.class);

                            startActivity(intent);

                        }

                        else if(Integer.valueOf(dataSnapshot.child("user_row").getValue().toString()) == 0){

                            DatabaseReference mDatabase;
                            mDatabase = FirebaseDatabase.getInstance().getReference().child("Doctors").child("Profile");
                            mDatabase.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String tname = (String) dataSnapshot.child(current.user_id).child("Name").getValue();
                                    String temail = (String) dataSnapshot.child(current.user_id).child("Email").getValue();
                                    String tphone = (String) dataSnapshot.child(current.user_id).child("Phone").getValue();
                                    String Office = (String) dataSnapshot.child(current.user_id).child("Office").getValue();
                                    String tqual = (String) dataSnapshot.child(current.user_id).child("Qualification").getValue();
                                    String tspec = (String) dataSnapshot.child(current.user_id).child("Specialization").getValue();
                                    String tabout = (String) dataSnapshot.child(current.user_id).child("About Us").getValue();
                                    String img = (String) dataSnapshot.child(current.user_id).child("imageUrl").getValue();
                                    Intent i = new Intent(Users.this, Doctdetails.class);
                                    i.putExtra("user_id", current.user_id);
                                    i.putExtra("username", current.username);
                                    i.putExtra("profile_pic", current.image);
                                    i.putExtra("Name", tname);
                                    i.putExtra("Email", temail);
                                    i.putExtra("Phone", tphone);
                                    i.putExtra("Office", Office);
                                    i.putExtra("Qual", tqual);
                                    i.putExtra("Spec", tspec);
                                    i.putExtra("About", tabout);
                                    startActivity(i);


                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            Intent intent = new Intent(Users.this, Home.class);

                            startActivity(intent);
                        }


                        //}

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onLongClick(View view, final int position) {


            }
        }));



    }
}
