package com.wakili.almustapha.healthfirst;

import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wakili.almustapha.healthfirst.Adapters.CommentListAdapter;
import com.wakili.almustapha.healthfirst.Models.ChatListDataItem;
import com.wakili.almustapha.healthfirst.Models.CommentlistDataItem;
import com.wakili.almustapha.healthfirst.Util.MyDividerItemDecoration;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Answers extends AppCompatActivity {

    private  TextView usernametxt, titletxt, answertxt;
    String question_key, question, question_title, usersname, photo_url;
    ImageView imageView;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseRef, mDatabaseUsers;
    private FirebaseAuth auth;
    private FirebaseUser user, muser;
    private Button sendbtn;
    private EditText answeredittxt;


    private List<CommentlistDataItem> CommentList;
    RecyclerView ChatRecyclerView;
    private CommentListAdapter CommentAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    String a, b;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answers);
        Answers.this.setTitle("ANSWERS");

        usernametxt = (TextView)findViewById(R.id.singleusernametxt);
        titletxt = (TextView)findViewById(R.id.singletitletxt);
        answertxt = (TextView)findViewById(R.id.singleanswertxt);
        imageView = (ImageView) findViewById(R.id.profile_imagebtn);

        question_key = getIntent().getExtras().getString("user_id");
        question = getIntent().getExtras().getString("Question");
        question_title = getIntent().getExtras().getString("Title");
        usersname = getIntent().getExtras().getString("Username");
        photo_url = getIntent().getExtras().getString("Photo");

        usernametxt.setText(usersname);
        titletxt.setText(question_title);
        answertxt.setText(question);
        Picasso.with(Answers.this).load(photo_url).into(imageView);




        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        databaseRef = FirebaseDatabase.getInstance().getReference().child("Answers");
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Answers").push();
        sendbtn = (Button)findViewById(R.id.sendButton);
        answeredittxt = (EditText)findViewById(R.id.answerEditText);

        user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase MDR = FirebaseDatabase.getInstance();
        DatabaseReference DR =    MDR.getReference("users");

        DatabaseReference ref1= FirebaseDatabase.getInstance().getReference().child("users");
        ref1.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(Integer.valueOf(dataSnapshot.child("user_row").getValue().toString()) == 1){

                    answeredittxt.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            sendbtn.setEnabled(charSequence.toString().trim().length() > 0);
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });

                    sendbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final String Comment = answeredittxt.getText().toString().trim();
                            final String Commentuser = user.getUid();
                            final DatabaseReference newComment = databaseRef.push();

                            // do a check for empty fields
                            if (!TextUtils.isEmpty(Comment) && !TextUtils.isEmpty(Commentuser)){

                                //adding post contents to database reference

                                mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        String picx = photo_url;
                                        newComment.child("Answer").setValue(Comment);
                                        newComment.child("QuestionId").setValue(question_key);;
                                        newComment.child("uid").setValue(user.getUid());
                                        newComment.child("username").setValue(user.getDisplayName())
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            answeredittxt.setText("");

                                                            AlertDialog.Builder builder = new AlertDialog.Builder(Answers.this);
                                                            builder.setMessage("You answered the question")
                                                                    .setTitle("Suceess")
                                                                    .setPositiveButton(android.R.string.ok, null);
                                                            AlertDialog dialog = builder.create();
                                                            dialog.show();
                                                        }}});}
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Toast.makeText(Answers.this, databaseError.toString(), Toast.LENGTH_LONG).show();

                                    } });

                            }

                        }
                    });



                }

                else if(Integer.valueOf(dataSnapshot.child("user_row").getValue().toString()) == 0){

                    answeredittxt.setVisibility(View.INVISIBLE);
                    sendbtn.setVisibility(View.INVISIBLE);


                }


                //}

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        a =user.getDisplayName().toString();
        b = user.getPhotoUrl().toString();

        muser = auth.getCurrentUser();

        //getting user details by id
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        final ChatListDataItem userDetail = new ChatListDataItem();
        DatabaseReference  databaseReference =    mFirebaseDatabase.getReference("Answers");



        LinearLayoutManager mm =new LinearLayoutManager(this);
        mLayoutManager = new LinearLayoutManager(this);

        ChatRecyclerView = (RecyclerView) findViewById(R.id.answersRecycler);
        CommentList =  new ArrayList<>();


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {


                    try {      //if((childDataSnapshot.child("Postid").getValue()).equals(post_key)) {

                     if (question_key.equals(childDataSnapshot.child("QuestionId").getValue().toString())) {


                    CommentList.add(new CommentlistDataItem(childDataSnapshot.child("Answer").getValue().toString(),
                                childDataSnapshot.child("username").getValue().toString(),
                                childDataSnapshot.child("QuestionId").getValue().toString(),
                                childDataSnapshot.child("uid").getValue().toString(),
                                childDataSnapshot.child("uid").getValue().toString()));
                                }



                }
                catch (Exception e)
                {
                    //Toast.makeText(Answers.this, e.toString(), Toast.LENGTH_SHORT).show();

                }


            }}
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Answers.this, databaseError.toString(), Toast.LENGTH_LONG).show();
            }
        });



        CommentAdapter = new CommentListAdapter(Answers.this, Answers.this, CommentList);
        ChatRecyclerView.setLayoutManager(mLayoutManager);
        ChatRecyclerView.addItemDecoration(new MyDividerItemDecoration(Answers.this, LinearLayoutManager.VERTICAL, 16));
        ChatRecyclerView.setAdapter(CommentAdapter);



    }
}
