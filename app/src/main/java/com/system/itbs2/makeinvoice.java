package com.system.itbs2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class makeinvoice extends AppCompatActivity {

    private TextInputEditText tutorNameEditText, tutorSurnameEditText, tutorWorkIDEditText, amountPaidEditText;
    private Button submitInvoiceButton;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_makeinvoice);

        tutorNameEditText = findViewById(R.id.tutorName);
        tutorSurnameEditText = findViewById(R.id.tutorSurname);
        tutorWorkIDEditText = findViewById(R.id.tutorWorkID);
        amountPaidEditText = findViewById(R.id.amountPaid);
        submitInvoiceButton = findViewById(R.id.submitInvoice);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Invoices");

        submitInvoiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitInvoice();
            }
        });
    }

    private void submitInvoice() {
        String tutorName = tutorNameEditText.getText().toString().trim();
        String tutorSurname = tutorSurnameEditText.getText().toString().trim();
        String tutorWorkID = tutorWorkIDEditText.getText().toString().trim();
        String amountPaid = amountPaidEditText.getText().toString().trim();

        if (tutorName.isEmpty() || tutorSurname.isEmpty() || tutorWorkID.isEmpty() || amountPaid.isEmpty()) {
            Toast.makeText(makeinvoice.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference newInvoiceRef = databaseReference.push();
        newInvoiceRef.child("Tutor Name").setValue(tutorName);
        newInvoiceRef.child("Tutor Surname").setValue(tutorSurname);
        newInvoiceRef.child("Tutor Work ID").setValue(tutorWorkID);
        newInvoiceRef.child("Amount Paid").setValue(amountPaid);

        Toast.makeText(makeinvoice.this, "Invoice saved successfully", Toast.LENGTH_SHORT).show();

        // Clear the form fields
        tutorNameEditText.setText("");
        tutorSurnameEditText.setText("");
        tutorWorkIDEditText.setText("");
        amountPaidEditText.setText("");
    }
}
