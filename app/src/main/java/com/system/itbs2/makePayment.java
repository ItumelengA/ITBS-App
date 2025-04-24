package com.system.itbs2;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class makePayment extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("claims");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_payment);

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

                    TableRow row = new TableRow(makePayment.this);

                    TextView workIdView = new TextView(makePayment.this);
                    workIdView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.9F));

                    TextView tutorialDateView = new TextView(makePayment.this);
                    tutorialDateView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.9F));

                    TextView tutorialDurationView = new TextView(makePayment.this);
                    tutorialDurationView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.9F));

                    TextView tutorialSubjectView = new TextView(makePayment.this);
                    tutorialSubjectView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.9F));

                    TextView totalFeeView = new TextView(makePayment.this);
                    totalFeeView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.9F));

                    TextView studentNameView = new TextView(makePayment.this);
                    studentNameView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.9F));

                    TextView tutorNameView = new TextView(makePayment.this);
                    tutorNameView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.9F));

                    // Create a Button
                    Button actionButton = new Button(makePayment.this);
                    actionButton.setText("PAY");
                    actionButton.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.5F));

                    actionButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(makePayment.this,viewBankDetails.class);
                            startActivity(intent);
                        }
                    });

                    workIdView.setText(workId);
                    tutorialDateView.setText(tutorialDate);
                    tutorialDurationView.setText(tutorialDuration);
                    tutorialSubjectView.setText(tutorialSubject);
                    totalFeeView.setText(totalFee);
                    studentNameView.setText(studentName);
                    tutorNameView.setText(tutorName);

                    row.addView(workIdView);
                    row.addView(tutorialDateView);
                    row.addView(tutorialDurationView);
                    row.addView(tutorialSubjectView);
                    row.addView(totalFeeView);
                    row.addView(studentNameView);
                    row.addView(tutorNameView);
                    row.addView(actionButton);

                    tableLayout.addView(row);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(makePayment.this, "Error: Check your network connection", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
