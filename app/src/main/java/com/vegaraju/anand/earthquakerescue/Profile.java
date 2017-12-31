package com.vegaraju.anand.earthquakerescue;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Anand on 01-01-2018.
 */

public class Profile extends AppCompatActivity {

    Button editprof, menubutton;
    TextView nameview, phoneview, emview;
    String username, phonenumber, emname, emphone;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference emergencynameref, emergencyphoneref, nameref;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        editprof = findViewById(R.id.button3);
        menubutton = findViewById(R.id.button4);
        nameview = findViewById(R.id.textView);
        phoneview = findViewById(R.id.textView2);
        emview = findViewById(R.id.textView4);
        database = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        try {
            //username = user.getDisplayName().toString();
            //nameview.append(username);
            phonenumber = user.getPhoneNumber().toString();
            phoneview.append(phonenumber);
            emergencynameref = database.getReference(phonenumber).child("ename");
            emergencyphoneref = database.getReference(phonenumber).child("econtact");
            nameref = database.getReference(phonenumber).child("name");
        }catch (NullPointerException e){}

        nameref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        nameview.append(dataSnapshot.getValue(String.class));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
        emergencyphoneref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        emphone = dataSnapshot.getValue(String.class);
                        emview.append("\n"+emphone);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );

        emergencynameref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        emname = dataSnapshot.getValue(String.class);
                        emview.append("\n"+emname);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );

        editprof.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Profile.this, AddContacts.class));
                    }
                }
        );

        menubutton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Profile.this, Menu.class));
                    }
                }
        );

    }
}
