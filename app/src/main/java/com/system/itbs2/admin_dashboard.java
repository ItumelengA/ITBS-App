package com.system.itbs2;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.util.ArrayList;

public class admin_dashboard extends AppCompatActivity {

    DatabaseReference databaseReference;
    DrawerLayout drawerLayout;
    ImageView menuOpenDrawer;
    LinearLayout viewRequest,logout,makePayment,RgsTutors,StudentsR,studFeedback,claimsTutor,evaluations,director,viewStudentsBooked;
    LinearLayout tableLayout;
    LinearLayout dataLayout;
    ArrayList<String> acceptedTutorIds = new ArrayList<>();
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Tutor Accounts");
        drawerLayout = findViewById(R.id.drawerLayout);
        tableLayout = findViewById(R.id.tablelayout);
        dataLayout = findViewById(R.id.data_layout);

        //opener drawers
        menuOpenDrawer = findViewById(R.id.menu);
        viewRequest = findViewById(R.id.viewTutorrequest);
        StudentsR = findViewById(R.id.viewStudents);
        RgsTutors = findViewById(R.id.registeredTutors);
        makePayment = findViewById(R.id.makepayment);
        studFeedback = findViewById(R.id.student_feedback);
        claimsTutor = findViewById(R.id.claims);
        evaluations = findViewById(R.id.tutor_evaluations);
        logout = findViewById(R.id.logoutAdmin);
        viewStudentsBooked = findViewById(R.id.viewStudentsBooked);

        director = findViewById(R.id.loginDirector);


        Button SAVEBTN = findViewById(R.id.saveBTN);
        SAVEBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(admin_dashboard.this, RegisteredTutors.class);
                intent1.putStringArrayListExtra("acceptedTutorIds", acceptedTutorIds);
                startActivity(intent1);

                Intent intent2 = new Intent(admin_dashboard.this, AvailableTutors.class);
                intent2.putStringArrayListExtra("acceptedTutorIds", acceptedTutorIds);
                startActivity(intent2);
            }
        });
        menuOpenDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer(drawerLayout);
            }
        });
        StudentsR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(admin_dashboard.this,RegisteredStudents.class);
            }
        });
        viewRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });
        RgsTutors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(admin_dashboard.this,RegisteredTutors.class);
            }
        });
        makePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(admin_dashboard.this,com.system.itbs2.makePayment.class);
            }
        });
        evaluations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(admin_dashboard.this,viewEvaluations.class);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(admin_dashboard.this, selectLogin.class);
                finish();
            }
        });
        studFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(admin_dashboard.this, viewClaims.class);
            }
        });
        claimsTutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(admin_dashboard.this, viewclaimedservice.class);
            }
        });
        director.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(admin_dashboard.this, DirectorLogin.class);
                finish();
            }
        });
        viewStudentsBooked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(admin_dashboard.this,viewStudent.class);
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataLayout.removeAllViews();
                if (dataSnapshot != null && dataSnapshot.exists()) {
                    loadCheckBoxStates(dataSnapshot);
                    for (DataSnapshot tutorSnapshot : dataSnapshot.getChildren()) {
                        TableRow tableRow = new TableRow(admin_dashboard.this);
                        tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                        Model tutor = tutorSnapshot.getValue(Model.class);

                        CheckBox Accept = new CheckBox(admin_dashboard.this);
                        Accept.setChecked(tutor.isAccepted());
                        Accept.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.5F));

                        boolean isChecked = sharedPreferences.getBoolean(tutorSnapshot.getKey(), false);
                        Accept.setChecked(isChecked);

                        Accept.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                                myEdit.putBoolean(tutorSnapshot.getKey(), isChecked);
                                myEdit.apply();

                                if (isChecked) {
                                    acceptedTutorIds.add(tutorSnapshot.getKey());
                                    tutor.setAccepted(isChecked);
                                    databaseReference.child("Available Tutors").child(tutorSnapshot.getKey()).setValue(tutor);
                                    Toast.makeText(admin_dashboard.this, "Accepted", Toast.LENGTH_SHORT).show();
                                } else {
                                    acceptedTutorIds.remove(tutorSnapshot.getKey());
                                    databaseReference.child("Available Tutors").child(tutorSnapshot.getKey()).removeValue();
                                }
                            }
                        });

                        CheckBox Decline = new CheckBox(admin_dashboard.this);
                        Decline.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.5F));

                        TextView nameTextView = new TextView(admin_dashboard.this);
                        nameTextView.setText(tutor.getName());
                        nameTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.9F));

                        TextView surnameTextView = new TextView(admin_dashboard.this);
                        surnameTextView.setText(tutor.getSurname());
                        surnameTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.9F));

                        TextView moduleTextView = new TextView(admin_dashboard.this);
                        moduleTextView.setText(tutor.getModule());
                        moduleTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.8F));

                        TextView departmentTextView = new TextView(admin_dashboard.this);
                        departmentTextView.setText(tutor.getDepartment());
                        departmentTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.6F));

                        TextView averageTextView = new TextView(admin_dashboard.this);
                        averageTextView.setText(tutor.getPdfUri());
                        averageTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.6F));

                        tableRow.addView(nameTextView);
                        tableRow.addView(surnameTextView);
                        tableRow.addView(moduleTextView);
                        tableRow.addView(departmentTextView);
                        tableRow.addView(averageTextView);
                        tableRow.addView(Accept);
                        tableRow.addView(Decline);

                        dataLayout.addView(tableRow);

                        averageTextView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String pdfUri = tutor.getPdfUri();
                                openPdf(pdfUri);
                            }
                        });
                    }
                } else {
                    Toast.makeText(admin_dashboard.this, "No Tutor Available", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(admin_dashboard.this, "Error: Check your network connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

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

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }

    private void openPdf(String pdfUri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(pdfUri), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(admin_dashboard.this, "No application available to view PDF", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadCheckBoxStates(DataSnapshot dataSnapshot) {
        for (DataSnapshot tutorSnapshot : dataSnapshot.getChildren()) {
            boolean isChecked = sharedPreferences.getBoolean(tutorSnapshot.getKey(), false);
            if (isChecked) {
                acceptedTutorIds.add(tutorSnapshot.getKey());
            }
        }
    }
}
