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

public class viewClaims extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Student Feedback");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_claims);

        final TableLayout tableLayout = findViewById(R.id.tableLayout);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot claimSnapshot : snapshot.getChildren()) {
                    String name = claimSnapshot.child("Name").getValue(String.class);
                    String message = claimSnapshot.child("Feedback").getValue(String.class);


                    TableRow row = new TableRow(viewClaims.this);

                    TextView nameView = new TextView(viewClaims.this);
                    nameView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.7F));

                    TextView messageeView = new TextView(viewClaims.this);
                    messageeView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.9F));

                    nameView.setText(name);
                    messageeView.setText(message);

                    row.addView(nameView);
                    row.addView(messageeView);

                    tableLayout.addView(row);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(viewClaims.this, "Error: Check your network connection", Toast.LENGTH_SHORT).show();
            }
        });
    }
}