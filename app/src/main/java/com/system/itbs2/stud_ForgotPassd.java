package com.system.itbs2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class stud_ForgotPassd extends AppCompatActivity {

    private TextInputEditText studentNumberEditText, newPasswordEditText, confirmPasswordEditText;
    private Button updatePasswordButton;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stud_forgot_passd);

        studentNumberEditText = findViewById(R.id.studentNumber);
        newPasswordEditText = findViewById(R.id.newPassword);
        confirmPasswordEditText = findViewById(R.id.confirmPassword);
        updatePasswordButton = findViewById(R.id.updatePasswordButton);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Students");

        updatePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePassword();
            }
        });
    }

    private void updatePassword() {
        final String studentNumber = studentNumberEditText.getText().toString().trim();
        final String newPassword = newPasswordEditText.getText().toString().trim();
        final String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        if (studentNumber.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(stud_ForgotPassd.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(stud_ForgotPassd.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseReference.child(studentNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Hash the new password
                    String hashedPassword = hashPassword(newPassword);

                    // Update the password in the database
                    if (hashedPassword != null) {
                        checkPasswordStrength(confirmPassword);
                        databaseReference.child(studentNumber).child("Password").setValue(hashedPassword);
                        Toast.makeText(stud_ForgotPassd.this, "Password updated successfully", Toast.LENGTH_SHORT).show();

                        // Clear the form fields
                        studentNumberEditText.setText("");
                        newPasswordEditText.setText("");
                        confirmPasswordEditText.setText("");
                        Intent intent = new Intent(stud_ForgotPassd.this,LoginAct.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(stud_ForgotPassd.this, "Error hashing password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(stud_ForgotPassd.this, "Student number not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(stud_ForgotPassd.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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
    // Method to check if the password is medium
    private boolean isMediumPassword(String password) {
        return password.matches("(?=.*[A-Z])(?=.*[@#$%^&+=]).{6,}");
    }
    // Method to check if the password is strong
    private boolean isStrongPassword(String password) {
        return password.matches("(?=.*[0-9].*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,}");
    }
    // Method to check and display password strength
    private void checkPasswordStrength(String password) {
        if (isMediumPassword(password)) {
            Toast.makeText(this, "Password Strength: Medium", Toast.LENGTH_SHORT).show();
        } else if (isStrongPassword(password)) {
            Toast.makeText(this, "Password Strength: Strong", Toast.LENGTH_SHORT).show();
        } else {
            // No need to show a Toast if password does not meet criteria
        }
    }
}
