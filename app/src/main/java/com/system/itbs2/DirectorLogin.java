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

public class DirectorLogin extends AppCompatActivity {

    EditText textKey;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://itbs-fe906-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_director_login);

        textKey = findViewById(R.id.textSafeKey);

        Button adminLogin = findViewById(R.id.loginBtn);
        adminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String SAFEKEY = textKey.getText().toString();

                if (SAFEKEY.isEmpty()) {
                    Toast.makeText(DirectorLogin.this, "Please enter all the details", Toast.LENGTH_LONG).show();
                } else {
                    databaseReference.child("Director").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(SAFEKEY)) {
                                Toast.makeText(DirectorLogin.this, "Login successful.", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(DirectorLogin.this, DirectorChoose.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(DirectorLogin.this, "Incorrect Safe Key", Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(DirectorLogin.this, "Error: Check your network connection" , Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}