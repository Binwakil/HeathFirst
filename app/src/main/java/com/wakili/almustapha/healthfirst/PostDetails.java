package com.wakili.almustapha.healthfirst;

import android.content.Intent;
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

public class PostDetails extends AppCompatActivity {


    private ImageView singelImage;
    private Button commentbtn;
    private TextView singleTitle, singleDesc, commenttxt;
    private String post_key = null;
    private DatabaseReference mDatabase;
    private Button deleteBtn;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseRef, mDatabaseUsers;
    private FirebaseAuth auth;
    private FirebaseUser user, muser;

    private List<CommentlistDataItem> CommentList;
    RecyclerView ChatRecyclerView;
    private CommentListAdapter CommentAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    String a, b;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);


        singelImage = (ImageView)findViewById(R.id.singleImageview);
        singleTitle = (TextView)findViewById(R.id.singleTitle);
        singleDesc = (TextView)findViewById(R.id.singleDesc);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Doctors").child("Posts");
        post_key = getIntent().getExtras().getString("PostID");
        deleteBtn = (Button)findViewById(R.id.deleteBtn);
        mAuth = FirebaseAuth.getInstance();
        deleteBtn.setVisibility(View.INVISIBLE);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDatabase.child(post_key).removeValue();

                Intent mainintent = new Intent(PostDetails.this, Posts.class);
                startActivity(mainintent);
            }
        });


        mDatabase.child(post_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String post_title = (String) dataSnapshot.child("title").getValue();
                String post_desc = (String) dataSnapshot.child("desc").getValue();
                String post_image = (String) dataSnapshot.child("imageUrl").getValue();
                String post_uid = (String) dataSnapshot.child("uid").getValue();

                singleTitle.setText(post_title);
                singleDesc.setText(post_desc);
                Picasso.with(PostDetails.this).load(post_image).into(singelImage);
                if (mAuth.getCurrentUser().getUid().equals(post_uid)){

                    deleteBtn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        databaseRef = FirebaseDatabase.getInstance().getReference().child("Comments");
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Comments").child(user.getUid()).push();
        commentbtn = (Button)findViewById(R.id.sendButton);
        commenttxt = (EditText)findViewById(R.id.commentEditText);
        commenttxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                commentbtn.setEnabled(charSequence.toString().trim().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        a =user.getDisplayName().toString();
        b = user.getPhotoUrl().toString();

        commentbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Comment = commenttxt.getText().toString().trim();
                final String Commentuser = user.getUid();
                final DatabaseReference newComment = databaseRef.push();

                // do a check for empty fields
                if (!TextUtils.isEmpty(Comment) && !TextUtils.isEmpty(Commentuser)){

                            //adding post contents to database reference

                            mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    newComment.child("comment").setValue(Comment);
                                    newComment.child("postid").setValue(post_key);;
                                    newComment.child("uid").setValue(user.getUid());
                                    newComment.child("photo_url").setValue(b);
                                    newComment.child("username").setValue(a)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){

                                                        AlertDialog.Builder builder = new AlertDialog.Builder(PostDetails.this);
                                                        builder.setMessage("Your Comment is Posted")
                                                                .setTitle("Suceess")
                                                                .setPositiveButton(android.R.string.ok, null);
                                                        AlertDialog dialog = builder.create();
                                                        dialog.show();
                                                        commenttxt.setText("");
                                                    }}});}
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Toast.makeText(PostDetails.this, databaseError.toString(), Toast.LENGTH_LONG).show();

                                } });

                }

            }
        });






        muser = auth.getCurrentUser();

        //getting user details by id
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        final ChatListDataItem userDetail = new ChatListDataItem();
        DatabaseReference  databaseReference =    mFirebaseDatabase.getReference("Comments");



        LinearLayoutManager mm =new LinearLayoutManager(this);
        mLayoutManager = new LinearLayoutManager(this);

        ChatRecyclerView = (RecyclerView) findViewById(R.id.commentRecycler);
        CommentList =  new ArrayList<>();


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {

       try{
                    //if((childDataSnapshot.child("Postid").getValue()).equals(post_key)) {

                        if (post_key.equals(childDataSnapshot.child("postid").getValue().toString())) {

                            CommentList.add(new CommentlistDataItem(childDataSnapshot.child("comment").getValue().toString(),
                                    childDataSnapshot.child("username").getValue().toString(),
                                    childDataSnapshot.child("postid").getValue().toString(),
                                    childDataSnapshot.child("uid").toString(),
                                    childDataSnapshot.child("photo_url").getValue().toString()));
                        }

                }
                catch (Exception e)
                {
                    //Toast.makeText(PostDetails.this, e.toString(), Toast.LENGTH_SHORT).show();

                }


                }


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(PostDetails.this, databaseError.toString(), Toast.LENGTH_LONG).show();
            }
        });



        CommentAdapter = new CommentListAdapter(PostDetails.this, PostDetails.this, CommentList);
        ChatRecyclerView.setLayoutManager(mLayoutManager);
        ChatRecyclerView.addItemDecoration(new MyDividerItemDecoration(PostDetails.this, LinearLayoutManager.VERTICAL, 16));
        ChatRecyclerView.setAdapter(CommentAdapter);



    }
}
