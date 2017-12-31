package com.vegaraju.anand.earthquakerescue;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by Anand on 01-01-2018.
 */

public class Menu extends AppCompatActivity {
    ImageButton homebutton, settingsbutton, profilebutton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        this.setTitle("Main Menu");
        homebutton = (ImageButton)findViewById(R.id.home_button);
        settingsbutton = (ImageButton)findViewById(R.id.settings_button);
        profilebutton = (ImageButton) findViewById(R.id.profile_button);

        homebutton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "Coming soon", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        settingsbutton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Menu.this, AddContacts.class));
                    }
                }
        );

        profilebutton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(getApplicationContext(), "Coming soon enough!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Menu.this, Profile.class));
                    }
                }
        );

    }
}
