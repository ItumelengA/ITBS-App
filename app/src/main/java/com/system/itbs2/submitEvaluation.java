package com.system.itbs2;

import static com.system.itbs2.sendfeedback.openDrawer;
import static com.system.itbs2.sendfeedback.redirectActivity;

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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;

public class submitEvaluation extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ImageView menuOpenDrawer;
    LinearLayout SUBMITEVA, LOGOUT, FEEDBACK, DASHBOARD, UPLOADBANKINGDETAILS, CLAIMSERV, VIEWREQUEST;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://itbs-fe906-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_evaluation);

        drawerLayout = findViewById(R.id.drawerLayout);
        menuOpenDrawer = findViewById(R.id.menu);

        VIEWREQUEST = findViewById(R.id.viewrequestbooked);
        FEEDBACK = findViewById(R.id.tutorfeedback);
        DASHBOARD = findViewById(R.id.tutordashboard);
        SUBMITEVA = findViewById(R.id.submitevaluation);
        LOGOUT = findViewById(R.id.tutorlogout);
        UPLOADBANKINGDETAILS = findViewById(R.id.uploadDetails);
        CLAIMSERV = findViewById(R.id.claim_service);

        final EditText tutorName = findViewById(R.id.tutorname);
        final EditText studentName = findViewById(R.id.studentname);
        final EditText message = findViewById(R.id.message);

        Button cleanbtn = findViewById(R.id.clearbtn);
        cleanbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tutorName.setText("");
                studentName.setText("");
                message.setText("");
            }
        });

        Button sendEva = findViewById(R.id.sendeva);
        sendEva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tutorNameStr = tutorName.getText().toString();
                String studentNameStr = studentName.getText().toString();
                String messageStr = message.getText().toString();

                if (tutorNameStr.isEmpty() || studentNameStr.isEmpty() || messageStr.isEmpty()) {
                    Toast.makeText(submitEvaluation.this, "Please enter all the details.", Toast.LENGTH_LONG).show();
                } else {
                    databaseReference.child("Tutor Evaluations").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            databaseReference.child("Tutor Evaluations").child(tutorNameStr).child("Tutor Name").setValue(tutorNameStr);
                            databaseReference.child("Tutor Evaluations").child(tutorNameStr).child("Student Name").setValue(studentNameStr);
                            databaseReference.child("Tutor Evaluations").child(tutorNameStr).child("Evaluation").setValue(messageStr);

                            Toast.makeText(submitEvaluation.this, "Evaluation sent successfully.", Toast.LENGTH_LONG).show();
                            finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(submitEvaluation.this, "Failed to send, check your network connection.", Toast.LENGTH_LONG).show();
                        }
                    });
                }
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
                redirectActivity(submitEvaluation.this, uploadbankingDetails.class);
            }
        });

        DASHBOARD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(submitEvaluation.this, tutordashboard.class);
            }
        });

        SUBMITEVA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });

        LOGOUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(submitEvaluation.this, selectLogin.class);
                finish();
            }
        });

        CLAIMSERV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(submitEvaluation.this, tutorClaimService.class);
            }
        });

        VIEWREQUEST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(submitEvaluation.this, viewStudent.class);
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
