package com.wakili.almustapha.healthfirst;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wakili.almustapha.healthfirst.Adapters.TipsListAdapter;
import com.wakili.almustapha.healthfirst.Models.ChatListDataItem;
import com.wakili.almustapha.healthfirst.Models.TipsListDataItem;
import com.wakili.almustapha.healthfirst.Util.MyDividerItemDecoration;
import com.wakili.almustapha.healthfirst.Util.RecyclerTouchListener;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Tips extends AppCompatActivity {
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
    private FirebaseUser muser;
    ProgressBar progressBar;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);

        Tips.this.setTitle("HEALTH TIPS");

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        a = user.getDisplayName().toString();
        b = user.getPhotoUrl().toString();


        user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase MDR = FirebaseDatabase.getInstance();
        DatabaseReference DR = MDR.getReference("users");

        Firebase.setAndroidContext(this);
        ChatRecyclerView = (RecyclerView) findViewById(R.id.tipslist);
        user = FirebaseAuth.getInstance().getCurrentUser();
        //get firebase auth instance

        auth = FirebaseAuth.getInstance();
        ChatList = new ArrayList<>();


        mLayoutManager = new LinearLayoutManager(this);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar2);





        new  AsyncTask<Object, Void, Void>() {

            @Override
            protected void onPreExecute() {

                    progressDialog = new ProgressDialog(Tips.this);
                    progressDialog.setMessage("Please Wait");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
            }

            @Override
            protected Void doInBackground(Object... params) {


                try {
                    FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();

                    final ChatListDataItem userDetail = new ChatListDataItem();
                    DatabaseReference databaseReference = mFirebaseDatabase.getReference("Tips");

                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            // Userlist = new ArrayList<String>();

                            for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {

                                //int rowsss = Integer.valueOf(userRown);
                                TipsListDataItem tiplist = childDataSnapshot.getValue(TipsListDataItem.class);

                                try {

                                    ChatList.add(new TipsListDataItem(childDataSnapshot.getKey(),
                                            childDataSnapshot.child("username").getValue().toString(),
                                            childDataSnapshot.child("photo_url").getValue().toString(),
                                            childDataSnapshot.child("Title").getValue().toString(),
                                            childDataSnapshot.child("tip").getValue().toString()));
                                } catch (Exception e) {
                                    //Toast.makeText(Tips.this, e.toString(), Toast.LENGTH_SHORT).show();

                                }


                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(Tips.this, databaseError.toString(), Toast.LENGTH_LONG).show();
                        }
                    });


                    ChatAdapter = new TipsListAdapter(Tips.this, Tips.this, ChatList);
                    ChatRecyclerView.setLayoutManager(mLayoutManager);
                    ChatRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    ChatRecyclerView.addItemDecoration(new MyDividerItemDecoration(Tips.this, LinearLayoutManager.VERTICAL, 16));
                    ChatRecyclerView.setAdapter(ChatAdapter);

                    progressDialog.dismiss();




                    ChatRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(Tips.this,
                            ChatRecyclerView, new RecyclerTouchListener.ClickListener() {
                        @Override
                        public void onClick(View view, final int position) {
                            final TipsListDataItem current = ChatList.get(position);
                            //UserDetails.username = current.get(position);
                            firebasechat.UserDetails.chatWith = current.tipid;
                            user = FirebaseAuth.getInstance().getCurrentUser();
                            FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
                            DatabaseReference databaseReference = mFirebaseDatabase.getReference("users");

                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");
                            ref.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    if (Integer.valueOf(dataSnapshot.child("user_row").getValue().toString()) == 1) {
                                        DatabaseReference mDatabase;
                                        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child("Profile");
                                        mDatabase.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {


                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });


                                    } else if (Integer.valueOf(dataSnapshot.child("user_row").getValue().toString()) == 0) {

                                        DatabaseReference mDatabase;
                                        mDatabase = FirebaseDatabase.getInstance().getReference().child("Doctors").child("Profile");
                                        mDatabase.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {


                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
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






            } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                //c
                //progressBar.setVisibility(View.GONE);
            }
        }.execute();
        progressBar.setVisibility(View.GONE);
    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase MDR = FirebaseDatabase.getInstance();
        DatabaseReference DR =    MDR.getReference("users");

        DatabaseReference ref1= FirebaseDatabase.getInstance().getReference().child("users");
        ref1.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(Integer.valueOf(dataSnapshot.child("user_row").getValue().toString()) == 1){

                    getMenuInflater().inflate(R.menu.tipmenu, menu);


                }

                else if(Integer.valueOf(dataSnapshot.child("user_row").getValue().toString()) == 0){


                }


                //}

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        //noinspection SimplifiableIfStatement
        if (id == R.id.nav_addtip) {

            auth = FirebaseAuth.getInstance();
            user = auth.getCurrentUser();
            databaseRef = FirebaseDatabase.getInstance().getReference().child("Tips");
            mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Tips").push();


            Context context = Tips.this;
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);

            final EditText titleBox = new EditText(context);
            titleBox.setHint("Title");
            layout.addView(titleBox);

            final EditText descriptionBox = new EditText(context);
            descriptionBox.setHint("Tips");
            layout.addView(descriptionBox);

            final String userId = user.getUid();


            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("post new Health Tip")
                    .setView(layout)
                    .setPositiveButton("Post", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            final String title = String.valueOf(titleBox.getText());
                            final String question = descriptionBox.getText().toString();

                            final String mtuser = user.getUid();
                            final DatabaseReference newQuestion = databaseRef.push();

                            // do a check for empty fields
                            if (!TextUtils.isEmpty(question) && !TextUtils.isEmpty(mtuser)){

                                //adding post contents to database reference

                                mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        auth = FirebaseAuth.getInstance();
                                        user = auth.getCurrentUser();

                                        newQuestion.child("Title").setValue(title);
                                        newQuestion.child("tip").setValue(question);
                                        newQuestion.child("uid").setValue(user.getUid());
                                        newQuestion.child("photo_url").setValue(b);
                                        newQuestion.child("username").setValue(a)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){

                                                            AlertDialog.Builder builder = new AlertDialog.Builder(Tips.this);
                                                            builder.setMessage("You add Health Tip")
                                                                    .setTitle("Suceess")
                                                                    .setPositiveButton(android.R.string.ok, null);
                                                            AlertDialog dialog = builder.create();
                                                            dialog.show();
                                                        }}});}
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Toast.makeText(Tips.this, databaseError.toString(), Toast.LENGTH_LONG).show();

                                    } });

                            }

                            titleBox.setText("");
                            descriptionBox.setText("");



                        }

                    })
                    .setNegativeButton("Cancel",null)
                    .create();
            dialog.show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
