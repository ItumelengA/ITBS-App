package com.system.itbs2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class tutor_acc extends AppCompatActivity {
    // create object of DatabaseReference class to access firebase's Realtime Database
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://itbs-fe906-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_acc);

        final EditText nametxt = findViewById(R.id.name);
        final EditText surnametxt = findViewById(R.id.surname);
        final EditText phonetxt= findViewById(R.id.phonenumber);
        final EditText worktxt= findViewById(R.id.workNumber);
        final EditText emailtxt = findViewById(R.id.email);
        final EditText passwordtxt = findViewById(R.id.password);
        final EditText repasswordtxt = findViewById(R.id.repassword);

        Button crtbtn = findViewById(R.id.creataccountbtn);
        Button exit = findViewById(R.id.Exit);
        Button clear = findViewById(R.id.clear);

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear the text in each EditText field
                nametxt.setText("");
                surnametxt.setText("");
                phonetxt.setText("");
                worktxt.setText("");
                emailtxt.setText("");
                passwordtxt.setText("");
                repasswordtxt.setText("");
            }
        });

        //go to login if have an account
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(tutor_acc.this,tutorLogin.class);
                startActivity(intent); //go to login
                finish();
            }
        });

        crtbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //get data from EditTexts into String variables
                final String NAME = nametxt.getText().toString();
                final String SURNAME = surnametxt.getText().toString();
                final String PHONE = phonetxt.getText().toString();
                final String WORKID = worktxt.getText().toString();
                final String EMAIL = emailtxt.getText().toString();
                final String PASSWORD = passwordtxt.getText().toString();
                final String REPASSWORD = repasswordtxt.getText().toString();

                Model model = new Model ();

                //check if user fill all fields before sending data to database
                if (NAME.isEmpty() || SURNAME.isEmpty() || PHONE.isEmpty() || EMAIL.isEmpty() || PASSWORD.isEmpty() || REPASSWORD.isEmpty() || WORKID.isEmpty()) {
                    Toast.makeText(tutor_acc.this, "Please enter all the details.", Toast.LENGTH_LONG).show();
                }
                // check if user password are matching with each other
                else if (!PASSWORD.equals(REPASSWORD)) {
                    Toast.makeText(tutor_acc.this, "Password are not matching.", Toast.LENGTH_LONG).show();
                } else {
                    databaseReference.child("Tutor Work ID").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            // Check if the entered Tutor Work ID exists
                            if (snapshot.hasChild(WORKID)) {
                                // Check if the Tutor Account with the same Phone Number already exists
                                databaseReference.child("Tutor Accounts").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot accountSnapshot) {
                                        if (accountSnapshot.hasChild(WORKID)) {
                                            // Tutor Account with the same Phone Number exists, show toast
                                            Toast.makeText(tutor_acc.this, "Account already exists.", Toast.LENGTH_LONG).show();
                                        } else {
                                            if (!isValidPhoneNumber(PHONE)) {
                                                showSnackbar(v, "Phone number must be 10 digits.");
                                                return;
                                            }
                                            if (!isValidEmail(EMAIL)) {
                                                showSnackbar(v, "Use your school email.");
                                                return;
                                            }
                                            if (isStrongPassword(PASSWORD)) {
                                                showSnackbar(v, "Password is Strong.");
                                            } else if (isMediumPassword(PASSWORD)) {
                                                showSnackbar(v, "Password is medium.");
                                            } else {
                                                showSnackbar(v, "Password is weakPassword is weak must 8char in length(1 Cap,1 special char,and at least 1 number.");

                                                passwordtxt.setText("");
                                                repasswordtxt.setText("");
                                                return;
                                            }

                                            String hashedPassword = hashPassword(PASSWORD);

                                            model.setName(NAME);
                                            databaseReference.child("Tutor Accounts").child(WORKID).child("Name").setValue(NAME);

                                            model.setSurname(SURNAME);
                                            databaseReference.child("Tutor Accounts").child(WORKID).child("Surname").setValue(SURNAME);

                                            model.setPhoneNum(PHONE);
                                            databaseReference.child("Tutor Accounts").child(WORKID).child("Phone number").setValue(PHONE);
                                            databaseReference.child("Tutor Accounts").child(WORKID).child("Email").setValue(EMAIL);
                                            databaseReference.child("Tutor Accounts").child(WORKID).child("WorkID").setValue(WORKID);
                                            databaseReference.child("Tutor Accounts").child(WORKID).child("Password").setValue(hashedPassword);

                                            //show successful message and finish activity
                                            Toast.makeText(tutor_acc.this, "Tutor account created successful.", Toast.LENGTH_LONG).show();

                                            Intent intent = new Intent(tutor_acc.this, tutorLogin.class);
                                            startActivity(intent); //go to login
                                            finish();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        // Handle database error
                                        Toast.makeText(tutor_acc.this, "Error: Check your network connection" , Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Toast.makeText(tutor_acc.this, "Invalid work ID.", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle database error
                            Toast.makeText(tutor_acc.this, "Error: Check your network connection", Toast.LENGTH_SHORT).show();
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
    // Method to check if the password is medium
    private boolean isMediumPassword(String password) {
        return password.matches("(?=.*[A-Z])(?=.*[@#$%^&+=]).{6,}");
    }
    // Method to check if the password is strong
    private boolean isStrongPassword(String password) {
        return password.matches("(?=.*[0-9].*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,}");
    }

    // Method to display Snackbar
    private void showSnackbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }
    // Method to check if the phone number is in the correct format
    private boolean isValidPhoneNumber(String phoneNumber) {
        String regex = "^\\+(?:[0-9] ?){6,14}[0-9]$";
        return phoneNumber.matches(regex);
    }
    // Method to check if email is in correct form
    private boolean isValidEmail(String email) {
        // Check if the password ends with "@spu.ac.za"
        // and starts with 9 numbers
        String regex = "^\\d{9}.*@spu\\.ac\\.za$";
        return email.matches(regex);
    }
}
