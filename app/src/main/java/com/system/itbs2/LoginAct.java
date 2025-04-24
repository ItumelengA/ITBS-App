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

public class LoginAct extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://itbs-fe906-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText studentNumber = findViewById(R.id.textstudNum);
        final EditText password = findViewById(R.id.textpassword);

        final Button login = (Button) findViewById(R.id.loginBtn);
        final TextView createAccBtn = (TextView) findViewById(R.id.studCreate);

        //open create account for student`
        createAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginAct.this, MainActivity.class);
                startActivity(intent);
            }
        });
        Button forgotPSSD = (Button) findViewById(R.id.forgotPssd);
        forgotPSSD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginAct.this, stud_ForgotPassd.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String STUDNUM = studentNumber.getText().toString();
                final String passwordTxt = password.getText().toString();

                if (STUDNUM.isEmpty() || passwordTxt.isEmpty()) {
                    Toast.makeText(LoginAct.this, "Please enter all the details", Toast.LENGTH_LONG).show();
                } else {
                    databaseReference.child("Students").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(STUDNUM)) {
                                String hashedPasswordFromDB = snapshot.child(STUDNUM).child("Password").getValue(String.class);

                                // Hash the entered password for comparison
                                String hashedPasswordEntered = hashPassword(passwordTxt);

                                // Compare the hashed passwords
                                if (hashedPasswordEntered != null && hashedPasswordEntered.equals(hashedPasswordFromDB)) {
                                    Toast.makeText(LoginAct.this, "Login successful.", Toast.LENGTH_LONG).show();
                                    // Proceed with login
                                    Intent intent = new Intent(LoginAct.this, stud_dashboard.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(LoginAct.this, "Wrong password", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(LoginAct.this, "Incorrect student number", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(LoginAct.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    // Method to hash the password using SHA-256 algorithm
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