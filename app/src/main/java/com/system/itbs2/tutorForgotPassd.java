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

public class tutorForgotPassd extends AppCompatActivity {

    private TextInputEditText tutorIDEditText, newPasswordTutorEditText, confirmPasswordTutorEditText;
    private Button updatePasswordButtonTutor;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_forgot_passd);

        tutorIDEditText = findViewById(R.id.tutorID);
        newPasswordTutorEditText = findViewById(R.id.newPasswordTutor);
        confirmPasswordTutorEditText = findViewById(R.id.confirmPasswordTutor);
        updatePasswordButtonTutor = findViewById(R.id.updatePasswordButtonTutor);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Tutor Accounts");

        updatePasswordButtonTutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePassword();
            }
        });
    }

    private void updatePassword() {
        final String tutorID = tutorIDEditText.getText().toString().trim();
        final String newPassword = newPasswordTutorEditText.getText().toString().trim();
        final String confirmPassword = confirmPasswordTutorEditText.getText().toString().trim();

        if (tutorID.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(tutorForgotPassd.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(tutorForgotPassd.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check password strength
        if (!isStrongPassword(newPassword) && !isMediumPassword(newPassword)) {
            Toast.makeText(tutorForgotPassd.this, "Password is weak. Please use a stronger password", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseReference.child(tutorID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Hash the new password
                    String hashedPassword = hashPassword(newPassword);

                    // Update the password in the database
                    if (hashedPassword != null) {
                        databaseReference.child(tutorID).child("Password").setValue(hashedPassword);
                        Toast.makeText(tutorForgotPassd.this, "Password updated successfully", Toast.LENGTH_SHORT).show();

                        // Clear the form fields
                        tutorIDEditText.setText("");
                        newPasswordTutorEditText.setText("");
                        confirmPasswordTutorEditText.setText("");

                        // Check password strength after updating
                        checkPasswordStrength(newPassword);
                    } else {
                        Toast.makeText(tutorForgotPassd.this, "Error hashing password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(tutorForgotPassd.this, "Tutor ID not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(tutorForgotPassd.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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
