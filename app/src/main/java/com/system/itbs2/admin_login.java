package com.system.itbs2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class admin_login extends AppCompatActivity {

    EditText textKey;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://itbs-fe906-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        textKey = findViewById(R.id.textSafeKey);

        Button adminLogin = findViewById(R.id.loginBtn);
        adminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String SAFEKEY = textKey.getText().toString();

                if (SAFEKEY.isEmpty()) {
                    Toast.makeText(admin_login.this, "AlphaCode Required.", Toast.LENGTH_LONG).show();
                } else {
                    databaseReference.child("Admin Account").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(SAFEKEY)) {
                                    Toast.makeText(admin_login.this, "Login successful.", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(admin_login.this, admin_dashboard.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(admin_login.this, "Incorrect Safe Key", Toast.LENGTH_LONG).show();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(admin_login.this, "Error: Check your network connection" , Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}