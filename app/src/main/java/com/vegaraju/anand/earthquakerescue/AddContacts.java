package com.vegaraju.anand.earthquakerescue;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Anand on 24-12-2017.
 */

public class AddContacts extends AppCompatActivity {

    private Button pickcontactsbutton, registerbutton;
    TextView textView;
    EditText nametext;
    String uname, ename, econtact, tmpph;
    private static final int RESULT_PICK_CONTACT = 1;
    DatabaseReference userref;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcontacts);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        final String phoneNumber = user.getPhoneNumber();
        pickcontactsbutton = (Button) findViewById(R.id.button2);
        registerbutton = (Button) findViewById(R.id.register_button);
        textView = findViewById(R.id.emcontactname);
        nametext = findViewById(R.id.nameInput);

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
                        Toast.makeText(getApplicationContext(), "Registered", Toast.LENGTH_SHORT).show();
                    }
                }
        );

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
            String tmp = "Name : " + name + "\nPhone : " + phoneNo;
            tmpph = phoneNo;
            ename = name;
            textView.setText(tmp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
