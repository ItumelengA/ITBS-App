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

public class viewEvaluations extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Tutor Evaluations");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claims);

        final TableLayout tableLayout = findViewById(R.id.tableLayout);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot claimSnapshot : snapshot.getChildren()) {
                    String tutorname = claimSnapshot.child("Tutor Name").getValue(String.class);
                    String studName = claimSnapshot.child("Student Name").getValue(String.class);
                    String evaluations = claimSnapshot.child("Evaluation").getValue(String.class);


                    TableRow row = new TableRow(viewEvaluations.this);

                    TextView nameView = new TextView(viewEvaluations.this);
                    nameView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.9F));

                    TextView stunameView = new TextView(viewEvaluations.this);
                    stunameView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.9F));

                    TextView evaluationView = new TextView(viewEvaluations.this);
                    evaluationView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.9F));

                    stunameView.setText(studName);
                    nameView.setText(tutorname);
                    evaluationView.setText(evaluations);

                    row.addView(nameView);
                    row.addView(stunameView);
                    row.addView(evaluationView);

                    tableLayout.addView(row);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(viewEvaluations.this, "Error: Check your network connection", Toast.LENGTH_SHORT).show();
            }
        });
    }
}