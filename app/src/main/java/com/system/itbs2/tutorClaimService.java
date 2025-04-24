package com.system.itbs2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;

public class tutorClaimService extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ImageView menuOpenDrawer;
    LinearLayout SUBMITEVA, LOGOUT, FEEDBACK, DASHBOARD, UPLOADBANKINGDETAILS, CLAIMSERV, VIEWREQUEST;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://itbs-fe906-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim_service);

        drawerLayout = findViewById(R.id.drawerLayout);
        menuOpenDrawer = findViewById(R.id.menu);

        VIEWREQUEST = findViewById(R.id.viewrequestbooked);
        FEEDBACK = findViewById(R.id.tutorfeedback);
        DASHBOARD = findViewById(R.id.tutordashboard);
        LOGOUT = findViewById(R.id.tutorlogout);
        UPLOADBANKINGDETAILS = findViewById(R.id.uploadDetails);
        CLAIMSERV = findViewById(R.id.claim_service);
        SUBMITEVA = findViewById(R.id.submitevaluation);

        final EditText workEditText = findViewById(R.id.workEditText);
        final EditText tutorialDateEditText = findViewById(R.id.tutorialDateEditText);
        final EditText tutorialDurationEditText = findViewById(R.id.tutorialDurationEditText);
        final EditText tutorialSubjectEditText = findViewById(R.id.tutorialSubjectEditText);
        final EditText studentNameEditText = findViewById(R.id.studentNameEditText);
        final EditText tutorNameEditText = findViewById(R.id.tutorNameEditText);

        Button submitClaimButton = findViewById(R.id.submitClaimButton);
        Button clearButton = findViewById(R.id.clearButton);

        submitClaimButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String workId = workEditText.getText().toString();
                String tutorialDate = tutorialDateEditText.getText().toString();
                String tutorialDuration = tutorialDurationEditText.getText().toString();
                String tutorialSubject = tutorialSubjectEditText.getText().toString();
                String studentName = studentNameEditText.getText().toString();
                String tutorName = tutorNameEditText.getText().toString();

                if (workId.isEmpty() || tutorialDate.isEmpty() || tutorialDuration.isEmpty() || tutorialSubject.isEmpty() ||
                    studentName.isEmpty() || tutorName.isEmpty()) {
                    Toast.makeText(tutorClaimService.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                } else {

                    // Parse tutorialDuration to an integer
                    int duration = Integer.parseInt(tutorialDuration);
                    int totalFee = duration * 150;

                    // Create a new claim object
                    Claim claim = new Claim(workId, tutorialDate, tutorialDuration, tutorialSubject, totalFee, studentName, tutorName);
                    // Push the claim object to Firebase
                    databaseReference.child("claims").push().setValue(claim, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError error, DatabaseReference ref) {
                            if (error != null) {
                                Toast.makeText(tutorClaimService.this, "Failed to submit claim: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(tutorClaimService.this, "Claim submitted successfully", Toast.LENGTH_SHORT).show();
                                // Clear the form
                                workEditText.setText("");
                                tutorialDateEditText.setText("");
                                tutorialDurationEditText.setText("");
                                tutorialSubjectEditText.setText("");
                                studentNameEditText.setText("");
                                tutorNameEditText.setText("");
                            }
                        }
                    });
                }
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workEditText.setText("");
                tutorialDateEditText.setText("");
                tutorialDurationEditText.setText("");
                tutorialSubjectEditText.setText("");
                studentNameEditText.setText("");
                tutorNameEditText.setText("");
            }
        });

        menuOpenDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer(drawerLayout);
            }
        });

        UPLOADBANKINGDETAILS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(tutorClaimService.this, uploadbankingDetails.class);
            }
        });

        DASHBOARD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(tutorClaimService.this, tutordashboard.class);
            }
        });

        CLAIMSERV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });

        LOGOUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(tutorClaimService.this, selectLogin.class);
            }
        });

        VIEWREQUEST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(tutorClaimService.this, viewStudent.class);
            }
        });

        // Set click listener for "Send Feedback" menu item
        SUBMITEVA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to the send feedback activity
                redirectActivity(tutorClaimService.this, submitEvaluation.class);
            }
        });
    }

    // Method to open drawer layout
    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    // Method to close drawer layout if it is opened
    public static void closeDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public static void redirectActivity(Activity activity, Class secondActivity) {
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    // Close drawer
    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }
}