package com.system.itbs2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

public class bookDetails extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://itbs-fe906-default-rtdb.firebaseio.com/");
    hold_book_data holdBookData = new hold_book_data();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        TextInputEditText name = findViewById(R.id.namebook);
        TextInputEditText surname = findViewById(R.id.surnamebook);
        TextInputEditText cellNo = findViewById(R.id.cellNobook);
        TextInputEditText topicName = findViewById(R.id.topicNamebook);
        TextInputEditText tutorNo = findViewById(R.id.tutorCellNobook);

        hold_book_data holdBookData = new hold_book_data();
        RegClass regClass = new RegClass();

        // Retrieve the date, time, and module from the intent
        Intent intent = getIntent();
        String date = intent.getStringExtra("DATE");
        holdBookData.setDate(date);

        String time = intent.getStringExtra("TIME");
        holdBookData.setTime(time);

        String module = intent.getStringExtra("MODULE");
        holdBookData.setModule(module);

        Button continueBtn = findViewById(R.id.continueBTN);
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean allFieldsFilled = true;

                if (name.getText().toString().isEmpty()) {
                    name.setError("Name is required");
                    allFieldsFilled = false;
                }

                if (surname.getText().toString().isEmpty()) {
                    surname.setError("Surname is required");
                    allFieldsFilled = false;
                }

                if (cellNo.getText().toString().isEmpty()) {
                    cellNo.setError("Cell No is required");
                    allFieldsFilled = false;
                }

                if (topicName.getText().toString().isEmpty()) {
                    topicName.setError("Topic Name is required");
                    allFieldsFilled = false;
                }

                if (tutorNo.getText().toString().isEmpty()) {
                    tutorNo.setError("Tutor Cell Number is required");
                    allFieldsFilled = false;
                }

                if (allFieldsFilled) {
                    // Create a new booking detail object
                    hold_book_data holdBookData = new hold_book_data(
                            name.getText().toString(),
                            surname.getText().toString(),
                            cellNo.getText().toString(),
                            topicName.getText().toString(),
                            tutorNo.getText().toString(),
                            date,
                            time,
                            module
                    );

                    regClass.setStud_surname(String.valueOf(surname.getText()));
                    // Save the booking details under "Booked Students" node
                    databaseReference.child("Booked Students").push().setValue(holdBookData);
                   String cellno = cellNo.getText().toString();
                    new SendSmsTask().execute(cellno, "Student support tutor: Dear Tutor, a learner requested tutorship support from you. Enter 'yes' to accept, 'no' to reject, or 'alt' for follow up.  by clicking the below number. " + cellno);
                    // Display a toast message of successful process
                    Toast.makeText(bookDetails.this, "Booking Successful", Toast.LENGTH_SHORT).show();

                    // Clear all EditText fields
                    name.setText("");
                    surname.setText("");
                    cellNo.setText("");
                    topicName.setText("");
                    tutorNo.setText("");

                    Intent intent = new Intent(bookDetails.this, stud_dashboard.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
        private class SendSmsTask extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                String phoneNumber = params[0];
                String message = params[1];

                // This URL is used for sending messages
                String myURI = "https://api.bulksms.com/v1/messages";

                // Change these values to match your own account
                String myUsername = "melanie_02";
                String myPassword = "Leano@123";

                // The details of the message we want to send
                String myData = String.format("{to: \"%s\", encoding: \"UNICODE\", body: \"%s\"}", phoneNumber, message);

                try {
                    URL url = new URL(myURI);
                    HttpURLConnection request = (HttpURLConnection) url.openConnection();
                    request.setDoOutput(true);

                    // Supply the credentials
                    String authStr = myUsername + ":" + myPassword;
                    @SuppressLint({"NewApi", "LocalSuppress"}) String authEncoded = Base64.getEncoder().encodeToString(authStr.getBytes());
                    request.setRequestProperty("Authorization", "Basic " + authEncoded);

                    // We want to use HTTP POST
                    request.setRequestMethod("POST");
                    request.setRequestProperty("Content-Type", "application/json");

                    // Write the data to the request
                    OutputStreamWriter out = new OutputStreamWriter(request.getOutputStream());
                    out.write(myData);
                    out.close();

                    // Try to handle errors nicely
                    InputStream response = request.getInputStream();
                    BufferedReader in = new BufferedReader(new InputStreamReader(response));
                    StringBuilder replyText = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) {
                        replyText.append(line);
                    }
                    in.close();
                    request.disconnect();
                    return replyText.toString();
                } catch (IOException ex) {
                    try {
                        URL url = new URL(myURI);
                        BufferedReader in = new BufferedReader(new InputStreamReader(((HttpURLConnection) url.openConnection()).getErrorStream()));
                        StringBuilder errorText = new StringBuilder();
                        String line;
                        while ((line = in.readLine()) != null) {
                            errorText.append(line);
                        }
                        in.close();
                        return "Error: " + errorText.toString();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return "An error occurred: " + ex.getMessage();
                    }
                }
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                Toast.makeText(bookDetails.this, result, Toast.LENGTH_LONG).show();
            }
        }
    }