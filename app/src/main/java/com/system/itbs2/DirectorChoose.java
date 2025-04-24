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

public class DirectorChoose extends AppCompatActivity {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Invoices");
    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_director_choose);

        tableLayout = findViewById(R.id.tableLayout);

        loadInvoices();
    }

    private void loadInvoices() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot invoiceSnapshot : snapshot.getChildren()) {
                    String tutorName = invoiceSnapshot.child("Tutor Name").getValue(String.class);
                    String tutorSurname = invoiceSnapshot.child("Tutor Surname").getValue(String.class);
                    String tutorWorkID = invoiceSnapshot.child("Tutor Work ID").getValue(String.class);
                    String amountPaid = invoiceSnapshot.child("Amount Paid").getValue(String.class);

                    TableRow row = new TableRow(DirectorChoose.this);

                    TextView tutorNameView = new TextView(DirectorChoose.this);
                    tutorNameView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
                    tutorNameView.setText(tutorName);

                    TextView tutorSurnameView = new TextView(DirectorChoose.this);
                    tutorSurnameView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
                    tutorSurnameView.setText(tutorSurname);

                    TextView tutorWorkIDView = new TextView(DirectorChoose.this);
                    tutorWorkIDView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
                    tutorWorkIDView.setText(tutorWorkID);

                    TextView amountPaidView = new TextView(DirectorChoose.this);
                    amountPaidView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
                    amountPaidView.setText(amountPaid);

                    row.addView(tutorNameView);
                    row.addView(tutorSurnameView);
                    row.addView(tutorWorkIDView);
                    row.addView(amountPaidView);

                    tableLayout.addView(row);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DirectorChoose.this, "Error: Check your network connection", Toast.LENGTH_SHORT).show();
            }
        });
        Button invoice = (Button) findViewById(R.id.GenerateInvoices);
        invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DirectorChoose.this,viewEvaluations.class);
                startActivity(intent);
            }
        });
    }
}
