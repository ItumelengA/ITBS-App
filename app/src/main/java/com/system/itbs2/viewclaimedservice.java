package com.system.itbs2;

import android.os.Bundle;
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

public class viewclaimedservice extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("claims");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewclaimedservice);

        final TableLayout tableLayout = findViewById(R.id.tableLayout);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot claimSnapshot : snapshot.getChildren()) {
                    String workId = claimSnapshot.child("workId").getValue(String.class);
                    String tutorialDate = claimSnapshot.child("tutorialDate").getValue(String.class);
                    String tutorialDuration = claimSnapshot.child("tutorialDuration").getValue(String.class);
                    String tutorialSubject = claimSnapshot.child("tutorialSubject").getValue(String.class);
                    String totalFee = claimSnapshot.child("totalFee").getValue(String.class);
                    String studentName = claimSnapshot.child("studentName").getValue(String.class);
                    String tutorName = claimSnapshot.child("tutorName").getValue(String.class);

                    TableRow row = new TableRow(viewclaimedservice.this);

                    TextView workIdView = new TextView(viewclaimedservice.this);
                    workIdView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.5F));
                    workIdView.setText(workId);

                    TextView tutorialDateView = new TextView(viewclaimedservice.this);
                    tutorialDateView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.7F));
                    tutorialDateView.setText(tutorialDate);

                    TextView tutorialDurationView = new TextView(viewclaimedservice.this);
                    tutorialDurationView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.7F));
                    tutorialDurationView.setText(tutorialDuration);

                    TextView tutorialSubjectView = new TextView(viewclaimedservice.this);
                    tutorialSubjectView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.7F));
                    tutorialSubjectView.setText(tutorialSubject);

                    TextView totalFeeView = new TextView(viewclaimedservice.this);
                    totalFeeView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.7F));
                    totalFeeView.setText(totalFee);

                    TextView studentNameView = new TextView(viewclaimedservice.this);
                    studentNameView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.7F));
                    studentNameView.setText(studentName);

                    TextView tutorNameView = new TextView(viewclaimedservice.this);
                    tutorNameView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.7F));
                    tutorNameView.setText(tutorName);

                    row.addView(workIdView);
                    row.addView(tutorialDateView);
                    row.addView(tutorialDurationView);
                    row.addView(tutorialSubjectView);
                    row.addView(totalFeeView);
                    row.addView(studentNameView);
                    row.addView(tutorNameView);

                    tableLayout.addView(row);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(viewclaimedservice.this, "Error: Check your network connection", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
