package com.system.itbs2;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RegisteredTutors extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://itbs-fe906-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered_tutors);

        ArrayList<String> acceptedTutorIds = getIntent().getStringArrayListExtra("acceptedTutorIds");

        if (acceptedTutorIds == null) {
            acceptedTutorIds = new ArrayList<>();
        }

        TableLayout tableLayout = findViewById(R.id.tablelayout); // Replace with your TableLayout's ID

        for (String tutorId : acceptedTutorIds) {
            databaseReference.child("Tutor Accounts").child(tutorId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Model tutor = dataSnapshot.getValue(Model.class);

                    // Create a new TableRow
                    TableRow tableRow = new TableRow(RegisteredTutors.this);

                    // Create TextViews for each attribute of the tutor
                    TextView nameTextView = new TextView(RegisteredTutors.this);
                    nameTextView.setText(tutor.getName());
                    nameTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.9F));

                    TextView surnameTextView = new TextView(RegisteredTutors.this);
                    surnameTextView.setText(tutor.getSurname());
                    surnameTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.9F));

                    TextView moduleTextView = new TextView(RegisteredTutors.this);
                    moduleTextView.setText(tutor.getModule());
                    moduleTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.8F));

                    TextView DepartmentTextView = new TextView(RegisteredTutors.this);
                    DepartmentTextView.setText(tutor.getDepartment());
                    DepartmentTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.6F));

                    // Add the TextViews to the TableRow
                    tableRow.addView(nameTextView);
                    tableRow.addView(surnameTextView);
                    tableRow.addView(moduleTextView);
                    tableRow.addView(DepartmentTextView);

                    // Add the TableRow to the TableLayout
                    tableLayout.addView(tableRow);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(RegisteredTutors.this, "Error: Check your network connection" , Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
