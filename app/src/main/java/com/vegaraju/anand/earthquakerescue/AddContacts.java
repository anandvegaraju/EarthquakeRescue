package com.vegaraju.anand.earthquakerescue;

import android.*;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

/**
 * Created by Anand on 24-12-2017.
 */

public class AddContacts extends AppCompatActivity {

    private Button pickcontactsbutton, registerbutton, service_start, menu_button;
    TextView textView;
    EditText nametext;
    String uname, ename, econtact, tmpph, tmp;
    private static final int RESULT_PICK_CONTACT = 1;
    DatabaseReference userref, phonelistref;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcontacts);
        this.setTitle("Settings");
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        final FirebaseUser user = auth.getCurrentUser();
        final String phoneNumber = user.getPhoneNumber();
        pickcontactsbutton = (Button) findViewById(R.id.button2);
        registerbutton = (Button) findViewById(R.id.register_button);
        service_start = (Button) findViewById(R.id.servicebutton);
        menu_button = (Button) findViewById(R.id.menubutton);
        textView = findViewById(R.id.emcontactname);
        nametext = findViewById(R.id.nameInput);
        phonelistref = database.getReference("reglist");

        phonelistref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        tmp = dataSnapshot.getValue(String.class);
                        if(!tmp.contains(user.getPhoneNumber())){
                            tmp += " " + user.getPhoneNumber();
                            phonelistref.setValue(tmp);
                        }
                        //tmp += " " + phonenumber;
                        //eventref.setValue(tmp);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
        // pick contact

        pickcontactsbutton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                        startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT);
                    }
                }
        );

        registerbutton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        uname = nametext.getText().toString();
                        econtact = tmpph;
                        userref = database.getReference(phoneNumber);
                        userref.child("econtact").setValue(econtact);
                        userref.child("ename").setValue(ename);
                        userref.child("name").setValue(uname);
                        userref.child("safe").setValue(true);
                        userref.child("sentsms").setValue(false);

                        Toast.makeText(getApplicationContext(), "Registered", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        service_start.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityCompat.requestPermissions(AddContacts.this,new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

                        Intent intent = new Intent(AddContacts.this, MyService.class);
                        PendingIntent pintent = PendingIntent.getService(AddContacts.this, 0, intent, 0);
                        AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                        alarm.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis(), 3000, pintent);
                    }
                }
        );

        menu_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent gotomenu = new Intent(AddContacts.this, Menu.class);
                        startActivity(gotomenu);

                    }
                }
        );

    }

    public boolean checkLocationPermission()
    {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check whether the result is ok
        if (resultCode == RESULT_OK) {
            // Check for the request code, we might be usign multiple startActivityForReslut
            switch (requestCode) {
                case RESULT_PICK_CONTACT:
                    contactPicked(data);
                    break;
            }
        } else {
            Log.e("MainActivity", "Failed to pick contact");
        }
    }

    private void contactPicked(Intent data) {
        Cursor cursor = null;
        try {
            String phoneNo = null ;
            String name = null;
            // getData() method will have the Content Uri of the selected contact
            Uri uri = data.getData();
            //Query the content uri
            cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            // column index of the phone number
            int  phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            // column index of the contact name
            int  nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            phoneNo = cursor.getString(phoneIndex);
            name = cursor.getString(nameIndex);

            // Set the value to the textviews
            String tmp = "Selected Emergency Contact details: \n\nName : " + name + "\nPhone : " + phoneNo;
            tmpph = phoneNo;
            ename = name;
            textView.setText(tmp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
