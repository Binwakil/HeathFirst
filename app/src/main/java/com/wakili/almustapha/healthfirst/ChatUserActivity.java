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

public class ChatUserActivity extends AppCompatActivity {

    private List<ChatListDataItem> ChatList;
    private ChatListAdapter ChatAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseUser user;
    private FirebaseAuth auth;
    RecyclerView ChatRecyclerView;
    Firebase UserReference;
    String userRown = "1";

    private TextView texttitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_user);
        Firebase.setAndroidContext(this);

        FirebaseDatabase MDR = FirebaseDatabase.getInstance();
        DatabaseReference DR =    MDR.getReference("users");
        user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference ref1= FirebaseDatabase.getInstance().getReference().child("users");
        ref1.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(Integer.valueOf(dataSnapshot.child("user_row").getValue().toString()) == 1){

                    texttitle = (TextView)findViewById(R.id.chattitle);
                    texttitle.setText("AVAILABLE USERS");

                }

                else if(Integer.valueOf(dataSnapshot.child("user_row").getValue().toString()) == 0){


                    texttitle = (TextView)findViewById(R.id.chattitle);
                    texttitle.setText("AVAILABLE DOCTORS");
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ChatRecyclerView = (RecyclerView) findViewById(R.id.ChatRecyler);
        user = FirebaseAuth.getInstance().getCurrentUser();
        //get firebase auth instance
        final ProgressBar progressBar;

        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        auth = FirebaseAuth.getInstance();
        ChatList =  new ArrayList<>();


        mLayoutManager = new LinearLayoutManager(this);




        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        final ChatListDataItem userDetail = new ChatListDataItem();
        DatabaseReference  databaseReference =    mFirebaseDatabase.getReference("users");


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
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ChatUserActivity.this)
                            .setSmallIcon(R.drawable.bubble_in)
                            .setContentTitle("New Message from " + sender_name)
                            .setContentText(message)
                            .setOnlyAlertOnce(true)
                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
                    mBuilder.setAutoCancel(true);
                    mBuilder.setLocalOnly(false);


                    // Create pending intent, mention the Activity which needs to be
                    //triggered when user clicks on notification(ChatActivity.class in this case)

                    PendingIntent contentIntent = PendingIntent.getActivity(ChatUserActivity.this, 0,
                            new Intent(ChatUserActivity.this, ChatActivity.class)
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
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

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

                        ChatList.add(new ChatListDataItem(childDataSnapshot.getKey().toString(),childDataSnapshot.child("name").getValue().toString(),childDataSnapshot.child("imageUrl").getValue().toString()));
                        //progressBar.setVisibility(ProgressBar.GONE);
                    }}


                }


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ChatUserActivity.this, databaseError.toString(), Toast.LENGTH_LONG).show();
            }
        });


        ChatAdapter = new ChatListAdapter(ChatUserActivity.this, ChatUserActivity.this, ChatList);
        ChatRecyclerView.setLayoutManager(mLayoutManager);
        ChatRecyclerView.setItemAnimator(new DefaultItemAnimator());
        ChatRecyclerView.addItemDecoration(new MyDividerItemDecoration(ChatUserActivity.this, LinearLayoutManager.VERTICAL, 16));
        ChatRecyclerView.setAdapter(ChatAdapter);


        /**
         * On long press on RecyclerView item, open alert dialog
         * with options to choose
         * Edit and Delete
         * */
        ChatRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(ChatUserActivity.this,
                ChatRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                final ChatListDataItem current = ChatList.get(position);
                //UserDetails.username = current.get(position);
                UserDetails.chatWith = current.user_id;
                Intent i = new Intent(ChatUserActivity.this, ChatActivity.class);
                i.putExtra("user_id", current.user_id);
                i.putExtra("username", current.username);
                i.putExtra("profile_pic", current.image);
                startActivity(i);
            }

            @Override
            public void onLongClick(View view, final int position) {


            }
        }));



    }
}
