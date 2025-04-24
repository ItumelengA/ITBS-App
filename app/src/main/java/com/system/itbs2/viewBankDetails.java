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

public class viewBankDetails extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Tutor Banking Details");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bank_details);

        final TableLayout tableLayout = findViewById(R.id.tableLayout);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String workId = dataSnapshot.child("Work ID").getValue(String.class);
                    String accountNo = dataSnapshot.child("Account Number").getValue(String.class);
                    String holderName = dataSnapshot.child("Account Holder Name").getValue(String.class);
                    String bankName = dataSnapshot.child("Bank Name").getValue(String.class);
                    String branchName = dataSnapshot.child("Branch Name").getValue(String.class);
                    String swiftCode = dataSnapshot.child("SWIFT Code").getValue(String.class);
                    String routingNo = dataSnapshot.child("Routing Number").getValue(String.class);
                    String iban = dataSnapshot.child("IBAN").getValue(String.class);

                    TableRow row = new TableRow(viewBankDetails.this);

                    TextView workIdView = new TextView(viewBankDetails.this);
                    workIdView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.7F));
                    workIdView.setText(workId);

                    TextView accountNoView = new TextView(viewBankDetails.this);
                    accountNoView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.7F));
                    accountNoView.setText(accountNo);

                    TextView holderNameView = new TextView(viewBankDetails.this);
                    holderNameView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.0F));
                    holderNameView.setText(holderName);

                    TextView bankNameView = new TextView(viewBankDetails.this);
                    bankNameView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.7F));
                    bankNameView.setText(bankName);

                    TextView branchNameView = new TextView(viewBankDetails.this);
                    branchNameView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.7F));
                    branchNameView.setText(branchName);

                    TextView swiftCodeView = new TextView(viewBankDetails.this);
                    swiftCodeView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.7F));
                    swiftCodeView.setText(swiftCode);

                    TextView routingNoView = new TextView(viewBankDetails.this);
                    routingNoView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.7F));
                    routingNoView.setText(routingNo);

                    TextView ibanView = new TextView(viewBankDetails.this);
                    ibanView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.7F));
                    ibanView.setText(iban);

                    // Create a Button
                    Button actionButton = new Button(viewBankDetails.this);
                    actionButton.setText("Invoice");
                    actionButton.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.7F));
                    actionButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent =new Intent(viewBankDetails.this,makeinvoice.class);
                            startActivity(intent);
                        }
                    });


                    row.addView(workIdView);
                    row.addView(accountNoView);
                    row.addView(holderNameView);
                    row.addView(bankNameView);
                    row.addView(branchNameView);
                    row.addView(swiftCodeView);
                    row.addView(routingNoView);
                    row.addView(ibanView);
                    row.addView(actionButton);

                    tableLayout.addView(row);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(viewBankDetails.this, "Error: Check your network connection", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
