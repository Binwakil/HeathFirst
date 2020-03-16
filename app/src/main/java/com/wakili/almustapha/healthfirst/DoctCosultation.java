package com.wakili.almustapha.healthfirst;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DoctCosultation extends AppCompatActivity {

    private static final String TAG = "UserList" ;
    private DatabaseReference userlistReference;
    FirebaseDatabase fbdb;
    private ValueEventListener mUserListListener;
    ArrayList<String> usernamelist = new ArrayList<>();
    ArrayAdapter arrayAdapter;;

    ListView userListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doct_cosultation);

        fbdb = FirebaseDatabase.getInstance();
        userlistReference =fbdb.getInstance().getReference().child("Doctors/Profile");
        onStart();
        userListView = findViewById(R.id.userlistview);
    }

    @Override
    protected void onStart() {
        super.onStart();
        final ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usernamelist = new ArrayList<>((ArrayList) dataSnapshot.getValue());

                Log.i(TAG, "onDataChange: "+usernamelist.toString());
                arrayAdapter = new ArrayAdapter(DoctCosultation.this,android.R.layout.simple_list_item_1,usernamelist);
                userListView.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "onCancelled: ",databaseError.toException());
                Toast.makeText(DoctCosultation.this, "Failed to load User list.",
                        Toast.LENGTH_SHORT).show();
            }
        };
        userlistReference.addValueEventListener(userListener);

        mUserListListener = userListener;
    }
}
