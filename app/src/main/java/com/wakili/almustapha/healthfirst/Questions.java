package com.wakili.almustapha.healthfirst;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.wakili.almustapha.healthfirst.Adapters.QuestionListAdapter;
import com.wakili.almustapha.healthfirst.Models.ChatListDataItem;
import com.wakili.almustapha.healthfirst.Models.QuestionsDataitem;
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

public class Questions extends AppCompatActivity {
    private List<QuestionsDataitem> ChatList;
    private QuestionListAdapter ChatAdapter;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        Questions.this.setTitle("PUBLIC QUESTIONS");


        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        a =user.getDisplayName().toString();
        b = user.getPhotoUrl().toString();


        user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase MDR = FirebaseDatabase.getInstance();
        DatabaseReference DR =    MDR.getReference("users");

        Firebase.setAndroidContext(this);
        ChatRecyclerView = (RecyclerView) findViewById(R.id.QuestionsRecycler);
        user = FirebaseAuth.getInstance().getCurrentUser();
        //get firebase auth instance
        final ProgressBar progressBar;

        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        auth = FirebaseAuth.getInstance();
        ChatList =  new ArrayList<>();


        mLayoutManager = new LinearLayoutManager(this);




        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();

        final ChatListDataItem userDetail = new ChatListDataItem();
        DatabaseReference databaseReference =    mFirebaseDatabase.getReference("Question");


        //getting user details by id

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {

                    int rowsss = Integer.valueOf(userRown);


                    //if its not current login user
                    //Toast.makeText(Questions.this, childDataSnapshot.child("uid").getValue().toString(), Toast.LENGTH_LONG).show();
try{

                    ChatList.add(new QuestionsDataitem(
                            childDataSnapshot.child("Question").getValue().toString(),
                            childDataSnapshot.child("username").getValue().toString(),
                            childDataSnapshot.getKey().toString(),
                            childDataSnapshot.child("uid").getValue().toString(),
                            childDataSnapshot.child("photo_url").getValue().toString()));
                    //progressBar.setVisibility(ProgressBar.GONE);

                }
                catch (Exception e)
                {
                    //Toast.makeText(Questions.this, e.toString(), Toast.LENGTH_SHORT).show();

                }

                }


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Questions.this, databaseError.toString(), Toast.LENGTH_LONG).show();
            }
        });


        ChatAdapter = new QuestionListAdapter(Questions.this, Questions.this, ChatList);
        ChatRecyclerView.setLayoutManager(mLayoutManager);
        ChatRecyclerView.setItemAnimator(new DefaultItemAnimator());
        ChatRecyclerView.addItemDecoration(new MyDividerItemDecoration(Questions.this, LinearLayoutManager.VERTICAL, 16));
        ChatRecyclerView.setAdapter(ChatAdapter);


        /**
         * On long press on RecyclerView item, open alert dialog
         * with options to choose
         * Edit and Delete
         * */
        ChatRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(Questions.this,
                ChatRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                final QuestionsDataitem current = ChatList.get(position);
                //UserDetails.username = current.get(position);
                firebasechat.UserDetails.chatWith = current.Qid;
                user = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference =    mFirebaseDatabase.getReference("Question");

                DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Question");
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                            DatabaseReference mDatabase;
                            mDatabase = FirebaseDatabase.getInstance().getReference().child("Question");
                            mDatabase.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String question = (String) dataSnapshot.child(current.Qid).child("Question").getValue();
                                    String title = (String) dataSnapshot.child(current.Qid).child("Title").getValue();
                                    String username = (String) dataSnapshot.child(current.Qid).child("username").getValue();
                                    String photo_url = (String) dataSnapshot.child(current.Qid).child("photo_url:").getValue();
                                    Intent i = new Intent(Questions.this, Answers.class);
                                    i.putExtra("user_id", current.Qid);
                                    i.putExtra("Username", username);
                                    i.putExtra("Photo", photo_url);
                                    i.putExtra("Question", question);
                                    i.putExtra("Title", title);
                                    startActivity(i);




                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

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

                }

                else if(Integer.valueOf(dataSnapshot.child("user_row").getValue().toString()) == 0){
                    getMenuInflater().inflate(R.menu.askquestion, menu);


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
        if (id == R.id.nav_ask) {

            auth = FirebaseAuth.getInstance();
            user = auth.getCurrentUser();
            databaseRef = FirebaseDatabase.getInstance().getReference().child("Question");
            mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Question").push();


            Context context = Questions.this;
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);

            final EditText titleBox = new EditText(context);
            titleBox.setHint("Title");
            layout.addView(titleBox);

            final EditText descriptionBox = new EditText(context);
            descriptionBox.setHint("Question");
            layout.addView(descriptionBox);

            final String userId = user.getUid();


            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Ask new Question")
                    .setView(layout)
                    .setPositiveButton("Ask", new DialogInterface.OnClickListener() {
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
                                        newQuestion.child("Question").setValue(question);
                                        newQuestion.child("uid").setValue(user.getUid());
                                        newQuestion.child("photo_url").setValue(b);
                                        newQuestion.child("username").setValue(a)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){

                                                            AlertDialog.Builder builder = new AlertDialog.Builder(Questions.this);
                                                            builder.setMessage("You add Health Tip")
                                                                    .setTitle("Suceess")
                                                                    .setPositiveButton(android.R.string.ok, null);
                                                            AlertDialog dialog = builder.create();
                                                            dialog.show();
                                                        }}});}
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Toast.makeText(Questions.this, databaseError.toString(), Toast.LENGTH_LONG).show();

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
