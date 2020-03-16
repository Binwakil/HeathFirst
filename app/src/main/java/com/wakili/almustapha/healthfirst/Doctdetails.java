package com.wakili.almustapha.healthfirst;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class Doctdetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctdetails);
        Intent intent = getIntent();

        String contactName = intent.getStringExtra("username");
        TextView nameTextView = (TextView)findViewById(R.id.profilename);
        nameTextView.setText(contactName);

        String DtName = intent.getStringExtra("Name");
        TextView DtV = (TextView)findViewById(R.id.txtName);
        DtV.setText(DtName);

        String contactImage = intent.getStringExtra("profile_pic");
        ImageView contactImageView = (ImageView) findViewById(R.id.profileimg);
        Picasso.with(getApplicationContext())
                .load(contactImage)
                .resize(50, 50)
                .into(contactImageView);

        String Dphone = intent.getStringExtra("Email");
        TextView dtxthone = (TextView) findViewById(R.id.txtEmail);
        dtxthone.setText(Dphone);


        String Dword = intent.getStringExtra("Phone");
        TextView dtxtword = (TextView) findViewById(R.id.txtphone);
        dtxtword.setText(Dword);

        String Demail = intent.getStringExtra("Office");
        TextView DtxtEmail = (TextView) findViewById(R.id.txtadd);
        DtxtEmail.setText(Demail);

        String Shub = intent.getStringExtra("Qual");
        TextView txthubV = (TextView) findViewById(R.id.txtspecial);
        txthubV.setText(Shub);

        String SArea = intent.getStringExtra("Spec");
        TextView txtarea = (TextView) findViewById(R.id.txtspecial);
        txtarea.setText(SArea);


        String Sab = intent.getStringExtra("About");
        TextView tab = (TextView) findViewById(R.id.txtabout);
        txtarea.setText(SArea);

/**
 String Dgit = intent.getStringExtra("github");
 TextView Vgit = (TextView) findViewById(R.id.github);
 Vgit.setText(Dgit);




 /**

 String Special = intent.getStringExtra("special");
 TextView txtspecial = (TextView) findViewById(R.id.txtSpecialization);
 txtspecial.setText(Special);
 */

    }
}
