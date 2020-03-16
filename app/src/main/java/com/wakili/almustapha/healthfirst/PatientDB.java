package com.wakili.almustapha.healthfirst;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import services.Config;
import services.NotificationUtils;

public class PatientDB extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference db;
    private String name, email, photoUrl, uid, emailVerified;
    private DatabaseReference mDatabase;

    //firebase notification
    private static final String TAG = Home.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private TextView txtRegId, txtMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_db);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        db = FirebaseDatabase.getInstance().getReference();
        if (user == null) {
            // not logged in, launch the Log In activity
            //loadLogInView();
        }
        else if (user != null) {
            name = (user.getDisplayName().toString());
            email =  (user.getEmail().toString());

            // photoUrl = user.getPhotoUrl().toString();
            uid = user.getUid();  // The user's ID, unique to the Firebase project. Do NOT use
            // this value to authenticate with your backend server, if
            // you have one. Use User.getToken() instead.

        }
        LinearLayout m, fam, pat, cons, client, doc, hr, tips;

        client = (LinearLayout) findViewById(R.id.openconsult);

        client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = (new Intent(PatientDB.this, ChatUserActivity.class));
                startActivity(intent);
            }
        });
        m = (LinearLayout) findViewById(R.id.viewarticle);
        m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = (new Intent(PatientDB.this, Posts.class));
                startActivity(intent);

            }
        });
        cons = (LinearLayout) findViewById(R.id.viewtips);

        cons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = (new Intent(PatientDB.this, Tips.class));
                startActivity(intent);

            }
        });
        pat = (LinearLayout) findViewById(R.id.openaskquestions);

        pat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = (new Intent(PatientDB.this, Questions.class));
                startActivity(intent);

            }
        });
        doc = (LinearLayout) findViewById(R.id.opendoctors);
       doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = (new Intent(PatientDB.this, Users.class));
                startActivity(intent);

            }
        });
        tips = (LinearLayout) findViewById(R.id.viewtips);
        tips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = (new Intent(PatientDB.this, Tips.class));
                startActivity(intent);

            }
        });
        hr = (LinearLayout) findViewById(R.id.openhr);
        hr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = (new Intent(PatientDB.this, healthreport.class));
                startActivity(intent);

            }
        });





        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View header=navigationView.getHeaderView(0);

        final ImageView pimage = (ImageView) header.findViewById(R.id.pimageView);
        final TextView pname = (TextView)header.findViewById(R.id.txtpusername);
        final TextView ptitle = (TextView)header.findViewById(R.id.txtptitle);
        pname.setText("Hello " + user.getDisplayName().toString());


        final FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        user = mAuth.getCurrentUser();


        mDatabase.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String post_title = (String) dataSnapshot.child("Name").getValue();
                String post_desc = (String) dataSnapshot.child("Specialization").getValue();
                String img = (String) dataSnapshot.child("imageUrl").getValue();

                pname.setText(post_title);
                ptitle.setText(post_desc);
                Picasso.with(PatientDB.this).load(img).into(pimage);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        pimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientDB.this,UserProfile.class);
                startActivity(intent);
            }
        });


        /**********************************************************************************
         *
         *
         * fire base notification starts here
         *
         *
         *********************************************************************************/

        txtRegId = (TextView) findViewById(R.id.txt_reg_id);
        txtMessage = (TextView) findViewById(R.id.txt_push_message);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                    txtMessage.setText(message);
                }
            }
        };

        displayFirebaseRegId();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.patient_db, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.nav_signout) {
            auth.signOut();
            loadLogInView();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_massage) {
            Intent intent = new Intent(PatientDB.this,ChatUserActivity.class);
            startActivity(intent);
        }
        if (id == R.id.nav_notification) {
            Intent intent = new Intent(PatientDB.this,ChatUserActivity.class);
            startActivity(intent);
        }
        if (id == R.id.nav_myreport) {
            Intent intent = new Intent(PatientDB.this,healthreport.class);
            startActivity(intent);
            // Handle the camera action
        } else if (id == R.id.nav_mydoctor) {
            Intent intent = new Intent(PatientDB.this,Users.class);
            startActivity(intent);

        } else if (id == R.id.nav_viewarticle) {
            Intent intent = new Intent(PatientDB.this,Posts.class);
            startActivity(intent);

        } else if (id == R.id.nav_viewtips) {
            Intent intent = new Intent(PatientDB.this,Tips.class);
            startActivity(intent);

        } else if (id == R.id.nav_askquestion) {
            Intent intent = new Intent(PatientDB.this,Questions.class);
            startActivity(intent);

        } else if (id == R.id.nav_exit) {
            finish();
            System.exit(0);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void loadLogInView() {
        Intent intent = new Intent(this, Dlogin.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    // Fetches reg id from shared preferences
    // and displays on the screen
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e(TAG, "Firebase reg id: " + regId);

        if (!TextUtils.isEmpty(regId))

            txtRegId.setText("Firebase Reg Id: " + regId);
        else
            txtRegId.setText("Firebase Reg Id is not received yet!");
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }
}
