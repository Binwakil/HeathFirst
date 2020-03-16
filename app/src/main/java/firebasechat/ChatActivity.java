package firebasechat;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.wakili.almustapha.healthfirst.R;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

import static services.Config.GLOBAL_ADDRESS;
import static services.Config.NOTIFICATION_ID;

public class ChatActivity extends AppCompatActivity {

    LinearLayout layout;
    RelativeLayout layout_2;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;
    Firebase reference1, reference2, notifi;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        user = FirebaseAuth.getInstance().getCurrentUser();
        layout = (LinearLayout) findViewById(R.id.layout1);
        layout_2 = (RelativeLayout)findViewById(R.id.layout2);
        sendButton = (ImageView)findViewById(R.id.sendButton);
        messageArea = (EditText)findViewById(R.id.messageArea);
        scrollView = (ScrollView)findViewById(R.id.scrollView);
        final String UserChtWith = getIntent().getStringExtra("user_id");

        //Add to Activity
        FirebaseMessaging.getInstance().subscribeToTopic("pushNotifications");

        Firebase.setAndroidContext(this);
        reference1 = new Firebase(GLOBAL_ADDRESS+"messages/" + user.getUid() + "_" + UserChtWith);
        reference2 = new Firebase(GLOBAL_ADDRESS+"messages/" + UserChtWith + "_" + user.getUid());
        notifi = new Firebase(GLOBAL_ADDRESS+"notifi/" + UserChtWith);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();

                if(!messageText.equals("")){
                    Map<String, String> map = new HashMap<String, String>();
                    Map<String, String> mapnotifi = new HashMap<String, String>();
                    map.put("message", messageText);
                    map.put("user", user.getUid());
                    reference1.push().setValue(map);
                    reference2.push().setValue(map);



                    //add user notification
                    Firebase NotificationReference = new Firebase(GLOBAL_ADDRESS+"notifi");
                    NotificationReference.child(UserChtWith).child("user_id").setValue(UserChtWith);
                    NotificationReference.child(UserChtWith).child("chatter_user_id").setValue(user.getUid());
                    NotificationReference.child(UserChtWith).child("message").setValue(messageText);
                    NotificationReference.child(UserChtWith).child("sender_name").setValue(getIntent().getStringExtra("username"));
                    NotificationReference.child(UserChtWith).child("read").setValue("false");

                    messageArea.setText("");
                }
            }
        });

        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String userName = map.get("user").toString();

                if(userName.equals(user.getUid())){
                    addMessageBox("You:-\n" + message, 1);
                }
                else{
                    addMessageBox(getIntent().getStringExtra("username") + ":-\n" + message, 2);

                    NotificationManager  mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ChatActivity.this)
                                .setSmallIcon(R.drawable.bubble_in)
                                .setContentTitle("New Message from " + getIntent().getStringExtra("username"))
                                .setContentText(message)
                                .setOnlyAlertOnce(true)
                                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
                        mBuilder.setAutoCancel(true);
                        mBuilder.setLocalOnly(false);


                    // Create pending intent, mention the Activity which needs to be
                    //triggered when user clicks on notification(ChatActivity.class in this case)

                    PendingIntent contentIntent = PendingIntent.getActivity(ChatActivity.this, 0,
                            new Intent(ChatActivity.this, ChatActivity.class)
                                    .putExtra("user_id",UserChtWith)
                                    .putExtra("username",getIntent().getStringExtra("username")), PendingIntent.FLAG_UPDATE_CURRENT);


                    mBuilder.setContentIntent(contentIntent);




                    mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());



                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void addMessageBox(String message, int type){
        TextView textView = new TextView(ChatActivity.this);
        textView.setText(message);

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.weight = 1.0f;

        if(type == 1) {
            lp2.gravity = Gravity.LEFT;
            textView.setBackgroundResource(R.drawable.bubbleout);
        }
        else{
            lp2.gravity = Gravity.RIGHT;
            textView.setBackgroundResource(R.drawable.bubblein);
        }
        textView.setLayoutParams(lp2);
        layout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }
}
