package com.system.itbs2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class tutorLogin extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://itbs-fe906-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_login);

        final EditText workid = findViewById(R.id.workId);
        final EditText password = findViewById(R.id.textpassword);

        final Button login =(Button) findViewById(R.id.loginBtn);
        final TextView createAccBtn =(TextView) findViewById(R.id.createAcc);

        //open create account for student
        createAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(tutorLogin.this,tutor_acc.class);
                startActivity(intent);
            }
        });
        Button forgotPSSD = (Button) findViewById(R.id.forgotPassword);
        forgotPSSD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(tutorLogin.this, tutorForgotPassd.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String WORKID = workid.getText().toString();
                final  String passwordTxt = password.getText().toString();

                if (WORKID.isEmpty() || passwordTxt.isEmpty()){
                    Toast.makeText(tutorLogin.this,"Please enter all the details", Toast.LENGTH_LONG).show();
                }
                else {
                    databaseReference.child("Tutor Accounts").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //check if tutor exist in the database
                            if (snapshot.hasChild(WORKID)) {
                                // now get password of student from firebase and match it with the one entered
                                databaseReference.child("Tutor Accounts").child(WORKID).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        final String hashedPasswordFromDB = snapshot.child("Password").getValue(String.class);
                                        // Hash the entered password for comparison
                                        String hashedPasswordEntered = hashPassword(passwordTxt);

                                        if (hashedPasswordFromDB.equals(hashedPasswordEntered)) {
                                            Toast.makeText(tutorLogin.this, "Login successful.", Toast.LENGTH_LONG).show();


                                            Intent intent = new Intent(tutorLogin.this, tutordashboard.class);
                                            startActivity(intent); //go to dashboard

                                        }
                                        else {
                                            Toast.makeText(tutorLogin.this, "Wrong password", Toast.LENGTH_LONG).show();
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        // Handle database error
                                        Toast.makeText(tutorLogin.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                            } else {
                                Toast.makeText(tutorLogin.this, "Incorrect work ID", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle database error
                            Toast.makeText(tutorLogin.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            }
        });

    }
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
