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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class sendfeedback extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageView menuOpenDrawer;
    LinearLayout dashboard, logoutBTN, feedback;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Student Feedback");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendfeedback);

        drawerLayout = findViewById(R.id.drawerLayout);
        dashboard = findViewById(R.id.dashboard);
        logoutBTN = findViewById(R.id.logout);
        feedback = findViewById(R.id.send_feedback);
        menuOpenDrawer = findViewById(R.id.menu);

        final EditText username = findViewById(R.id.editTextName);
        final EditText message = findViewById(R.id.editmessagetxt);
        Button clearBtn = findViewById(R.id.CLEARBTN);
        Button sendBtn = findViewById(R.id.SENDBTN);

        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.setText("");
                message.setText("");
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = username.getText().toString();
                String feedbackMessage = message.getText().toString();

                if (name.isEmpty() || feedbackMessage.isEmpty()) {
                    Toast.makeText(sendfeedback.this, "Please enter all the details.", Toast.LENGTH_LONG).show();
                } else {
                    DatabaseReference feedbackRef = databaseReference.child(name);
                    feedbackRef.child("Name").setValue(name);
                    feedbackRef.child("Feedback").setValue(feedbackMessage, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError error, @NonNull DatabaseReference ref) {
                            if (error != null) {
                                Toast.makeText(sendfeedback.this, "Failed to send feedback: " + error.getMessage(), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(sendfeedback.this, "Feedback sent successfully.", Toast.LENGTH_LONG).show();
                                username.setText("");
                                message.setText("");
                            }
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

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });

        dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(sendfeedback.this, stud_dashboard.class);
            }
        });

        logoutBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(sendfeedback.this, selectLogin.class);
                finish();
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
        activity.finish();
    }

    // Close drawer
    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }
}
