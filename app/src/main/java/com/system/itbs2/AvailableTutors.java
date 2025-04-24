package com.system.itbs2;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AvailableTutors extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Tutor Accounts");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_tutors);

        TableLayout tableLayout = findViewById(R.id.tablelayout); // Replace with your TableLayout's ID

        // Retrieve data from the database
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot tutorSnapshot : dataSnapshot.getChildren()) {
                    Model tutor = tutorSnapshot.getValue(Model.class);
                    if (tutor != null) {
                        // Create a new TableRow
                        TableRow tableRow = new TableRow(AvailableTutors.this);

                        // Create TextViews for each attribute of the tutor
                        TextView nameTextView = new TextView(AvailableTutors.this);
                        nameTextView.setText(tutor.getName());
                        nameTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.8F));

                        TextView surnameTextView = new TextView(AvailableTutors.this);
                        surnameTextView.setText(tutor.getSurname());
                        surnameTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.7F));

                        TextView moduleTextView = new TextView(AvailableTutors.this);
                        moduleTextView.setText(tutor.getModule());
                        moduleTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.6F));

                        TextView departmentTextView = new TextView(AvailableTutors.this);
                        departmentTextView.setText(tutor.getDepartment());
                        departmentTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.7F));

                       TextView phoneNumTextView = new TextView(AvailableTutors.this);
                        phoneNumTextView.setText(tutor.getPhoneNum());
                        phoneNumTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.5F));

                        // Create a Button
                        Button actionButton = new Button(AvailableTutors.this);
                        actionButton.setText("Book");
                        actionButton.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.6F));

                        // Set an OnClickListener for the button
                        actionButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                databaseReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        Intent intent = new Intent(AvailableTutors.this, bookDetails.class);
                                        startActivity(intent);
                                          }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(AvailableTutors.this, "Error: Failed to book the tutor", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        });

                        // Add the TextViews and Button to the TableRow
                        tableRow.addView(nameTextView);
                        tableRow.addView(surnameTextView);
                        tableRow.addView(moduleTextView);
                        tableRow.addView(departmentTextView);
                        tableRow.addView(phoneNumTextView);
                        tableRow.addView(actionButton);

                        // Add the TableRow to the TableLayout
                        tableLayout.addView(tableRow);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AvailableTutors.this, "Error: Check your network connection", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
