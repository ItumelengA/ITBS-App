package com.system.itbs2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class viewStudent extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ImageView menuOpenDrawer;
    LinearLayout SUBMITEVA, LOGOUT, FEEDBACK, DASHBOARD, UPLOADBANKINGDETAILS, CLAIMSERV, VIEWREQUEST;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Booked Students");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_student);

        TableLayout tableLayout = findViewById(R.id.tablelayout);

        // Initialize variables with corresponding views
        drawerLayout = findViewById(R.id.drawerLayout);
        menuOpenDrawer = findViewById(R.id.menu);

        VIEWREQUEST = findViewById(R.id.viewrequestbooked);
        FEEDBACK = findViewById(R.id.tutorfeedback);
        DASHBOARD = findViewById(R.id.tutordashboard);
        LOGOUT = findViewById(R.id.tutorlogout);
        UPLOADBANKINGDETAILS = findViewById(R.id.uploadDetails);
        CLAIMSERV = findViewById(R.id.claim_service);
        SUBMITEVA = findViewById(R.id.submitevaluation);

        // Set click listener for opening the drawer
        menuOpenDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer(drawerLayout);
            }
        });

        // Set click listener for "My Account" menu item
        DASHBOARD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to the dashboard activity
                redirectActivity(viewStudent.this, tutordashboard.class);
            }
        });

        // Set click listener for "Submit Evaluation" menu item
        SUBMITEVA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to the submit evaluation activity
                redirectActivity(viewStudent.this, submitEvaluation.class);
            }
        });

        // Set click listener for "Claim Service" menu item
        CLAIMSERV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(viewStudent.this, tutorClaimService.class);
            }
        });

        // Set click listener for "Logout" menu item
        LOGOUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(viewStudent.this, tutorLogin.class);
            }
        });

        // Set click listener for "Upload Banking Details" menu item
        UPLOADBANKINGDETAILS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(viewStudent.this, uploadbankingDetails.class);
            }
        });

        // Set click listener for "View Request" menu item
        VIEWREQUEST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });

        // Retrieve data from the database
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot tutorSnapshot : dataSnapshot.getChildren()) {
                    hold_book_data tutor = tutorSnapshot.getValue(hold_book_data.class);
                    if (tutor != null) {
                        // Create a new TableRow
                        TableRow tableRow = new TableRow(viewStudent.this);

                        // Create TextViews for each attribute of the tutor
                        TextView nameTextView = new TextView(viewStudent.this);
                        nameTextView.setText(tutor.getName());
                        nameTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.8F));

                        TextView surnameTextView = new TextView(viewStudent.this);
                        surnameTextView.setText(tutor.getSurname());
                        surnameTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.5F));

                        TextView moduleTextView = new TextView(viewStudent.this);
                        moduleTextView.setText(tutor.getModule());
                        moduleTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.7F));

                        TextView topicTextView = new TextView(viewStudent.this);
                        topicTextView.setText(tutor.getTopicName());
                        topicTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.7F));

                        TextView timeTextView = new TextView(viewStudent.this);
                        timeTextView.setText(tutor.getTime());
                        timeTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.5F));

                        TextView dateTextView = new TextView(viewStudent.this);
                        dateTextView.setText(tutor.getDate());
                        dateTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.5F));

                        // Add the TextViews to the TableRow
                        tableRow.addView(nameTextView);
                        tableRow.addView(surnameTextView);
                        tableRow.addView(moduleTextView);
                        tableRow.addView(topicTextView);
                        tableRow.addView(timeTextView);
                        tableRow.addView(dateTextView);

                        // Add the TableRow to the TableLayout
                        tableLayout.addView(tableRow);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(viewStudent.this, "Error: Check your network connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to open the drawer
    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    // Method to close the drawer
    public static void closeDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    // Method to redirect to another activity
    public static void redirectActivity(Activity activity, Class secondActivity) {
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    // Close the drawer when the activity is paused
    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }
}
