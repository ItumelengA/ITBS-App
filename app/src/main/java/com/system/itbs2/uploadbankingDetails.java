package com.system.itbs2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class uploadbankingDetails extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ImageView menuOpenDrawer;
    LinearLayout SUBMITEVA, LOGOUT, FEEDBACK, DASHBOARD,UPLOADBANKINGDETAILS,CLAIMSERV,VIEWREQUEST;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://itbs-fe906-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadbanking_details);

        EditText accountNo = (EditText) findViewById(R.id.accountNumberEditText);
        EditText holderName = findViewById(R.id.accountHolderNameEditText);
        EditText bankName = findViewById(R.id.bankNameEditText);
        EditText branchName = findViewById(R.id.branchNameEditText);
        EditText switchCode = findViewById(R.id.swiftCodeEditText);
        EditText routingNumber = findViewById(R.id.routingNumberEditText);
        EditText iban = findViewById(R.id.ibanEditText);
        EditText workid = findViewById(R.id.workIDEditText);

        drawerLayout = findViewById(R.id.drawerLayout);

        SUBMITEVA = findViewById(R.id.submitevaluation);
        DASHBOARD = findViewById(R.id.tutordashboard);
        LOGOUT = findViewById(R.id.tutorlogout);
        VIEWREQUEST = findViewById(R.id.viewrequestbooked);
        FEEDBACK = findViewById(R.id.tutorfeedback);
        UPLOADBANKINGDETAILS = findViewById(R.id.uploadDetails);
        CLAIMSERV = findViewById(R.id.claim_service);

        menuOpenDrawer = findViewById(R.id.menu);

        Button CLEARDETAILS = (Button) findViewById(R.id.CLEARbtn);
        CLEARDETAILS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountNo.setText("");
                holderName.setText("");
                bankName.setText("");
                branchName.setText("");
                switchCode.setText("");
                routingNumber.setText("");
                iban.setText("");
                workid.setText("");
            }
        });


        Button UPLOADDETAILS = (Button) findViewById(R.id.UPLOAD);
        UPLOADDETAILS.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                databaseReference.child("Tutor Banking Details").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                        ProgressBar progressBar = findViewById(R.id.progressBar);

                        String ACCOUNTNO = accountNo.getText().toString();
                        String HOLDERNAME = holderName.getText().toString();
                        String BANKNAME = bankName.getText().toString();
                        String BRANCHNAME = branchName.getText().toString();
                        String SWITCHCODE = switchCode.getText().toString();
                        String ROUTINGNO = routingNumber.getText().toString();
                        String IBAN = iban.getText().toString();
                        String WORKIDTXT = workid.getText().toString();

                        if (WORKIDTXT.isEmpty() || ACCOUNTNO.isEmpty() || HOLDERNAME.isEmpty() || BANKNAME.isEmpty() || BRANCHNAME.isEmpty() || SWITCHCODE.isEmpty() ||
                            ROUTINGNO.isEmpty() || IBAN.isEmpty() ){
                            Toast.makeText(uploadbankingDetails.this, "Please enter all the details.", Toast.LENGTH_LONG).show();

                        }else {
                            progressBar.setVisibility(View.VISIBLE);
                            databaseReference.child("Tutor Work ID").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.hasChild(WORKIDTXT)) {
                                        databaseReference.child("Tutor Banking Details").child(WORKIDTXT).child("Work ID").setValue(WORKIDTXT);
                                        databaseReference.child("Tutor Banking Details").child(WORKIDTXT).child("Account Number").setValue(ACCOUNTNO);
                                        databaseReference.child("Tutor Banking Details").child(WORKIDTXT).child("Account Holder Name").setValue(HOLDERNAME);
                                        databaseReference.child("Tutor Banking Details").child(WORKIDTXT).child("Bank Name").setValue(BANKNAME);
                                        databaseReference.child("Tutor Banking Details").child(WORKIDTXT).child("Branch Name").setValue(BRANCHNAME);
                                        databaseReference.child("Tutor Banking Details").child(WORKIDTXT).child("SWIFT Code").setValue(SWITCHCODE);
                                        databaseReference.child("Tutor Banking Details").child(WORKIDTXT).child("Routing Number").setValue(ROUTINGNO);
                                        databaseReference.child("Tutor Banking Details").child(WORKIDTXT).child("IBAN").setValue(IBAN);

                                        Toast.makeText(uploadbankingDetails.this, "Your data is updated.", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.INVISIBLE);
                                    }else{
                                        Toast.makeText(uploadbankingDetails.this, "Incorrect work id.", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.INVISIBLE);
                                        }
                                    }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(uploadbankingDetails.this, "FAILED to send, check your network connection.", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(uploadbankingDetails.this, "FAILED to send, check your network connection.", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });


    menuOpenDrawer.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openDrawer(drawerLayout);
        }
    });
        DASHBOARD.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            redirectActivity(uploadbankingDetails.this,tutordashboard.class);
        }
    });
        SUBMITEVA.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            redirectActivity(uploadbankingDetails.this,submitEvaluation.class);
        }
    });
        UPLOADBANKINGDETAILS.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            recreate();
        }
    });
        LOGOUT.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            redirectActivity(uploadbankingDetails.this,selectLogin.class);
        }
    });
        CLAIMSERV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(uploadbankingDetails.this, tutorClaimService.class);
            }
        });
        VIEWREQUEST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(uploadbankingDetails.this,viewStudent.class);
            }
        });

}
// Method to open drawer layout
public static void openDrawer(DrawerLayout drawerLayout) {
    drawerLayout.openDrawer(GravityCompat.START);
}

// Method to close drawer layout if is opened
public static void closeDrawer(DrawerLayout drawerLayout) {
    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
        drawerLayout.closeDrawer(GravityCompat.START);
    }
}

public static void redirectActivity(Activity activity, Class secondActivity) {
    Intent intent = new Intent(activity, secondActivity);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    activity.startActivity(intent);
    activity.finish();
}

// Close drawer
@Override
protected void onPause() {
    super.onPause();
    closeDrawer(drawerLayout);
}
}