package com.vegaraju.anand.earthquakerescue;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Anand on 17-01-2018.
 */

public class Home extends AppCompatActivity {
    ListView earthquake_list;
    FirebaseDatabase database;
    DatabaseReference countref, eq, safeornot, usereq;
    FirebaseAuth auth;
    FirebaseUser user;
    String phonenumber;
    TextView eqalert;
    Button imsafe;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        earthquake_list = (ListView)findViewById(R.id.eqlistview);
        database = FirebaseDatabase.getInstance();
        countref = database.getReference("eqcount");

        eqalert = (TextView)findViewById(R.id.safe_text);
        imsafe = (Button)findViewById(R.id.safe_button);

        // get user info
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        phonenumber = user.getPhoneNumber();
        safeornot = database.getReference(phonenumber).child("safe");
        usereq = database.getReference(phonenumber).child("eqid");
        final ArrayList<String> arrayStrings = new ArrayList<String>();
        final ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(Home.this, android.R.layout.simple_list_item_1, arrayStrings);
        earthquake_list.setAdapter(itemsAdapter);
        countref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int count = dataSnapshot.getValue(Integer.class);

                        for(int i = 0; i < count-1; i++){

                            eq = database.getReference("earthquake"+i);
                            DatabaseReference eqtitleref = eq.child("title");
                            eqtitleref.addListenerForSingleValueEvent(
                                    new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            String title = dataSnapshot.getValue(String.class);
                                            //Toast.makeText(getApplicationContext(),title,Toast.LENGTH_SHORT).show();
                                            itemsAdapter.add(title);
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    }
                            );


                        }

                        //arrayStrings.add("billi");

                        earthquake_list.setOnItemClickListener(
                                new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                                        // ListView Clicked item value
                                        final String  itemValue    = (String) earthquake_list.getItemAtPosition(position);
                                        final Intent openmaps = new Intent(Home.this, MapView.class);
                                        DatabaseReference eqref = database.getReference("earthquake"+position);
                                        final DatabaseReference latref, longref;
                                        latref = eqref.child("latitude");
                                        longref = eqref.child("longitude");
                                        latref.addListenerForSingleValueEvent(
                                                new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        final Double latitude = dataSnapshot.getValue(Double.class);
                                                        longref.addListenerForSingleValueEvent(
                                                                new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                        Double longitude = dataSnapshot.getValue(Double.class);
                                                                        openmaps.putExtra("latitude", latitude);
                                                                        openmaps.putExtra("longitude", longitude);
                                                                        openmaps.putExtra("title", itemValue);
                                                                        startActivity(openmaps);
                                                                    }

                                                                    @Override
                                                                    public void onCancelled(DatabaseError databaseError) {

                                                                    }
                                                                }
                                                        );
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                }
                                        );

                                        // Show Alert
                                        //Toast.makeText(getApplicationContext(),
                                               // "Position :"+position+"  ListItem : " +itemValue , Toast.LENGTH_LONG)
                                               // .show();
                                    }
                                }
                        );
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );

        safeornot.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Boolean b = dataSnapshot.getValue(Boolean.class);
                        if(b){
                            imsafe.setEnabled(false);
                            eqalert.setText("You are safe!");
                        }else{
                            imsafe.setEnabled(true);
                            eqalert.setText("You are in danger!");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );

        imsafe.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        usereq.setValue("");
                        safeornot.setValue(true);
                        eqalert.setText("You are safe!");
                    }
                }
        );


    }
}
