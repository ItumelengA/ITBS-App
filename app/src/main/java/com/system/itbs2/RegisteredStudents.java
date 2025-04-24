package com.system.itbs2;

import android.os.Bundle;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisteredStudents extends AppCompatActivity {
    private static final String TAG = "RegisteredStudents";
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Students");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered_students);

        TableLayout tableLayout = findViewById(R.id.tablelayout);

        // Retrieve data from the database
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                        // Check if the studentSnapshot has children
                        if (studentSnapshot.exists()) {
                            // Directly access and extract values from DataSnapshot
                            String name = (String) studentSnapshot.child("Name").getValue();
                            String stu_no = (String) studentSnapshot.child("Student number").getValue();

                            if (name != null && stu_no != null) {
                                Log.d(TAG, "Student Name: " + name);
                                Log.d(TAG, "Student Surname: " + stu_no);

                                // Create a new TableRow
                                TableRow tableRow = new TableRow(RegisteredStudents.this);

                                // Create TextViews for each attribute of the student
                                TextView nameTextView = new TextView(RegisteredStudents.this);
                                nameTextView.setText(name);
                                nameTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.9F));

                                TextView surnameTextView = new TextView(RegisteredStudents.this);
                                surnameTextView.setText(stu_no);
                                surnameTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.9F));

                                // Add the TextViews to the TableRow
                                tableRow.addView(nameTextView);
                                tableRow.addView(surnameTextView);

                                // Add the TableRow to the TableLayout
                                tableLayout.addView(tableRow);
                            } else {
                                Log.e(TAG, "One or more attributes are null for the student");
                            }
                        }
                    }
                } else {
                    Log.e(TAG, "DataSnapshot is null or empty");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Database Error: " + databaseError.getMessage());
                Toast.makeText(RegisteredStudents.this, "Error: Check your network connection", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
