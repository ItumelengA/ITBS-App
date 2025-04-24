package com.system.itbs2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;


public class selectLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_login);

        ImageButton student = findViewById(R.id.studLogin);
        ImageButton admin= findViewById(R.id.adminLogin);
        ImageButton tutor= findViewById(R.id.tutorLogin);

        //set on click
        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(selectLogin.this, LoginAct.class);
                startActivity(intent);
            }
        });
        tutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(selectLogin.this, tutorLogin.class);
                startActivity(intent);
            }
        });
        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (selectLogin.this, admin_login.class);
                startActivity(intent);
            }
        });

    }
}