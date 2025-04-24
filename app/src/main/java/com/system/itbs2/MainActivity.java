package com.system.itbs2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://itbs-fe906-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button exitBtn = findViewById(R.id.Exit);
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginAct.class);
                startActivity(intent);
            }
        });
        Button clearBtn = findViewById(R.id.clear);
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText user_name = findViewById(R.id.name);
                final EditText stud_num = findViewById(R.id.studentnumber);
                final EditText phone_num = findViewById(R.id.phonenumber);
                final EditText email_text = findViewById(R.id.email);
                final EditText txtPassword = findViewById(R.id.password);
                final EditText txtRePssd = findViewById(R.id.repassword);

                user_name.setText("");
                stud_num.setText("");
                phone_num.setText("");
                email_text.setText("");
                txtPassword.setText("");
                txtRePssd.setText("");
            }
        });
        Button selectImage = findViewById(R.id.UPLOADIMAGE);
        selectImage.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               // Create an intent to open the image picker
                                               Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                               intent.setType("image/*");
                                               startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                                           }
        });

        Button crtbtn = findViewById(R.id.creataccountbtn);
        crtbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText user_name = findViewById(R.id.name);
                final EditText stud_num = findViewById(R.id.studentnumber);
                final EditText phone_num = findViewById(R.id.phonenumber);
                final EditText email_text = findViewById(R.id.email);
                final EditText txtPassword = findViewById(R.id.password);
                final EditText txtRePssd = findViewById(R.id.repassword);

                //get data from EditTexts into String variables
                final String NAME = user_name.getText().toString();
                final String STUDNUM = stud_num.getText().toString();
                final String PHONE = phone_num.getText().toString();
                final String EMAIL = email_text.getText().toString();
                final String PASSWORD = txtPassword.getText().toString();
                final String REPASSWD = txtRePssd.getText().toString();

                //check if user fill all fields before sending data to database
                if (NAME.isEmpty() || STUDNUM.isEmpty() || PHONE.isEmpty() || EMAIL.isEmpty()
                        || PASSWORD.isEmpty() || REPASSWD.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter all the details.", Toast.LENGTH_LONG).show();
                }
                // check if user password are matching with each other
                else if (!PASSWORD.equals(REPASSWD)) {
                    Toast.makeText(MainActivity.this, "Password are not matching.", Toast.LENGTH_LONG).show();
                } else {
                    //check if students numbers has not created the account before
                    databaseReference.child("Students").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(STUDNUM)) {
                                Toast.makeText(MainActivity.this, "Account already created.", Toast.LENGTH_LONG).show();
                            } else {
                                // Method to save student's details
                                // Sending data to Firebase Realtime Database
                                // We're using student number as unique identifier of every student
                                // So all the details of students comes under student number

                                if (!isValidStudentNumber(STUDNUM)) {
                                    showSnackbar(v, "Student number must be 9 digits.");
                                    return;
                                }

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
                                    showSnackbar(v, "Password is weak must 8char in length(1 Cap,1 special char,and at least 1 number.");
                                    txtPassword.setText("");
                                    txtRePssd.setText("");
                                    return;
                                }

                                // Encryption of password
                                String hashedPassword = hashPassword(PASSWORD);
                                RegClass regClass = new RegClass();

                                // Save student's details in Firebase
                                databaseReference.child("Students").child(STUDNUM).child("Name").setValue(NAME);
                                regClass.setStud_name(NAME);
                                databaseReference.child("Students").child(STUDNUM).child("Student number").setValue(STUDNUM);
                                regClass.setStud_surname(STUDNUM);
                                databaseReference.child("Students").child(STUDNUM).child("Phone number").setValue(PHONE);
                                databaseReference.child("Students").child(STUDNUM).child("Email").setValue(EMAIL);
                                databaseReference.child("Students").child(STUDNUM).child("Password").setValue(hashedPassword);

                                // Show successful message and finish activity
                                Toast.makeText(MainActivity.this, "Student account created successfully.", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(MainActivity.this, LoginAct.class);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle database error
                            Toast.makeText(MainActivity.this, "Error: Check your network connection", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    // Method to hash the password using SHA-256 algorithm
    //use hashmap for security
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

    // Method to check if student number is 9 digits
    private boolean isValidStudentNumber(String studentNumber) {
        String regex = "^[0-9]{9}$";
        return studentNumber.matches(regex);
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

    // On activity that handle way to select
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Get the URI of the selected image
            imageUri = data.getData();
            showSnackbar(findViewById(android.R.id.content), "Image Uploaded.");
            databaseReference.child("Student Images").child("Image").setValue(imageUri);
        }
    }
}