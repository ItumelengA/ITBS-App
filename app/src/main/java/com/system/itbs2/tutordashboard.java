package com.system.itbs2;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class tutordashboard extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ImageView menuOpenDrawer;
    LinearLayout SUBMITEVA,LOGOUT,FEEDBACK,DASHBOARD,UPLOADBANKINGDETAILS,CLAIMSERV,VIEWREQUEST;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://itbs-fe906-default-rtdb.firebaseio.com/");
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PICK_PDF_REQUEST = 2;

    private Uri imageUri;
    private Uri pdfUri;
    ProgressBar progressBar;
    CheckBox time1, time2, time3;
    EditText module1, department, status, workid;

    // Variables to store checkbox values
    String time1Text;
    String time2Text;
    String time3Text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutordashboard);


        // Initialize variables with corresponding views
        drawerLayout = findViewById(R.id.drawerLayout);

        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.INVISIBLE);

        workid = findViewById(R.id.work_id);

        time1 = findViewById(R.id.radioButtonTime1);
        time2 = findViewById(R.id.radioButtonTime2);
        time3 = findViewById(R.id.radioButtonTime3);

        module1 = findViewById(R.id.module1);
        department = findViewById(R.id.department);
        status = findViewById(R.id.tutorStatus);

        // DrawerLayout
        menuOpenDrawer = findViewById(R.id.menu);

        VIEWREQUEST = findViewById(R.id.viewrequestbooked);
        FEEDBACK = findViewById(R.id.tutorfeedback);
        DASHBOARD= findViewById(R.id.tutordashboard);
        LOGOUT = findViewById(R.id.tutorlogout);
        UPLOADBANKINGDETAILS = findViewById(R.id.uploadDetails);
        CLAIMSERV = findViewById(R.id.claim_service);
        SUBMITEVA = findViewById(R.id.submitevaluation);

        // Set click listener for opening the drawer
        menuOpenDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer(drawerLayout);
            }
        });
      // Set click listener for "My Account" menu item
        DASHBOARD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Refresh the current activity
                recreate();
            }
        });
        // Set click listener for "Send Feedback" menu item
        SUBMITEVA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to the send feedback activity
                redirectActivity(tutordashboard.this, submitEvaluation.class);
            }
        });
        CLAIMSERV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(tutordashboard.this, tutorClaimService.class);
            }
        });
        LOGOUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(tutordashboard.this,selectLogin.class);
                finish();
            }
        });
        UPLOADBANKINGDETAILS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(tutordashboard.this,uploadbankingDetails.class);
            }
        });
       VIEWREQUEST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(tutordashboard.this, viewClaims.class);
            }
        });

        // Select the image
        Button selectImage = findViewById(R.id.uploadImage);


        // Select the PDF transcript
        Button selectTranscript = findViewById(R.id.uploadTranscript);
        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to open the image picker
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

            }
        });  selectTranscript.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to open the file picker
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                startActivityForResult(Intent.createChooser(intent, "Select PDF"), PICK_PDF_REQUEST);

            }
        });

        // Set OnClickListener for the save button
        Button sendToDatabase = findViewById(R.id.buttonSave);
        sendToDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get all the text
                String WORKID = workid.getText().toString();
                String MODULE1 = module1.getText().toString();
                String DEPARTMENT = department.getText().toString();
                String STATUS = department.getText().toString();

                // Check if user filled all fields before sending data to the database
                if (WORKID.isEmpty() || MODULE1.isEmpty() || DEPARTMENT.isEmpty() || STATUS.isEmpty()) {
                    Toast.makeText(tutordashboard.this, "Please enter all the details.", Toast.LENGTH_LONG).show();
                    return; // Exit the method if any required field is empty
                }

                // Retrieve checkbox values
                time1Text = time1.isChecked() ? time1.getText().toString() : null;
                time2Text = time2.isChecked() ? time2.getText().toString() : null;
                time3Text = time3.isChecked() ? time3.getText().toString() : null;

                Model model = new Model();

                databaseReference.child("Tutor Accounts").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // If work id is correct continue
                        if (snapshot.hasChild(WORKID)) {
                            // Check if image and PDF are selected
                            if (imageUri == null || pdfUri == null) {
                                Toast.makeText(tutordashboard.this, "Please upload both an image and a PDF.", Toast.LENGTH_LONG).show();
                                return; // Exit the method if either image or PDF is missing
                            }
                            // Upload image URI along with module details
                            if (imageUri != null) {
                                model.setImageUrl(imageUri.toString()); // Store the URI string in the model
                                // If image is selected, upload its URI along with module details
                                databaseReference.child("Tutor Accounts").child(WORKID).child("imageUri").setValue(imageUri.toString()); // Store the URI string in the database
                            }

                            // Upload PDF URI along with module details
                            if (pdfUri != null) {
                                model.setPdfUri(pdfUri.toString()); // Store the URI string in the model
                                // If PDF is selected, upload its URI along with module details
                                databaseReference.child("Tutor Accounts").child(WORKID).child("pdfUri").setValue(pdfUri.toString()); // Store the URI string in the database
                            }

                            // Save Module 1
                            model.setModule(MODULE1);
                            databaseReference.child("Tutor Accounts").child(WORKID).child("module").setValue(MODULE1);

                            // Save Module 2 if not empty
                            if (DEPARTMENT != null && !DEPARTMENT.isEmpty()) {
                                model.setDepartment(DEPARTMENT);
                                databaseReference.child("Tutor Accounts").child(WORKID).child("Department").setValue(DEPARTMENT);
                            }

                            // Save Module 3 if not empty
                            if (STATUS != null && !STATUS.isEmpty()) {
                                model.setStatus(STATUS);
                                databaseReference.child("Tutor Accounts").child(WORKID).child("Status").setValue(STATUS);
                            }

                            // Save checkbox values
                            if (time1Text != null) {
                                databaseReference.child("Tutor Accounts").child(WORKID).child("time1").setValue(time1Text);
                                model.setTime1(time1Text);
                            }
                            if (time2Text != null) {
                                model.setTime2(time2Text);
                                databaseReference.child("Tutor Accounts").child(WORKID).child("time2").setValue(time2Text);

                            }
                            if (time3Text != null) {
                                model.setTime3(time3Text);
                                databaseReference.child("Tutor Accounts").child(WORKID).child("time3").setValue(time3Text);

                            }

                            progressBar.setVisibility(View.VISIBLE);
                            Toast.makeText(tutordashboard.this, "Data sent to admin successfully.", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            Intent intent = new Intent(tutordashboard.this, admin_dashboard.class);
                            startActivity(intent);

                        } else {
                            // Work ID does not exist, show error message
                            Toast.makeText(tutordashboard.this, "Invalid work ID.", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(tutordashboard.this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    // On activity that handle way to select
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Get the URI of the selected image
            imageUri = data.getData();
            showSnackbar(findViewById(android.R.id.content), "Image Uploaded.");
        } else if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Get the URI of the selected PDF
            pdfUri = data.getData();
            showSnackbar(findViewById(android.R.id.content), "Pdf Uploaded.");
        }
    }

    // Method to open the drawer
    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    // Method to close the drawer
    public static void closeDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    // Method to redirect to another activity
    public static void redirectActivity(Activity activity, Class secondActivity) {
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    // Close the drawer when the activity is paused
    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }
    private void showSnackbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }
}
