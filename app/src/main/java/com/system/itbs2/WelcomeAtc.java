package com.system.itbs2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeAtc extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Button startbtn = findViewById(R.id.getStarted);

        //welcome activty
        startbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Welcome Activty to select login
                Intent intent = new Intent(WelcomeAtc.this,selectLogin.class);
                startActivity(intent);
            }
        });
        Button sps = findViewById(R.id.sponsors);


        sps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Make the LinearLayout visible
                Intent intent = new Intent(WelcomeAtc.this, Sponsors.class);
                startActivity(intent);
            }
        });
        Button Contact = findViewById(R.id.contactUs);
        Contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeAtc.this, ContactUs.class);
                startActivity(intent);
            }
        });
        Button BROUGHTBY = findViewById(R.id.brought);
        BROUGHTBY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeAtc.this, broughtBy.class);
                startActivity(intent);
            }
        });

    }
}