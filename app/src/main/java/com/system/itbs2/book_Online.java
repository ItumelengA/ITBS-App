package com.system.itbs2;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Locale;

public class book_Online extends AppCompatActivity {

    // Declare variables
    // Array to hold modules names
    String[] modules = {"Programming", "Software Engineering.", "Information System", "Mobile App Development"};
    ArrayAdapter<String> adapterItems;
    AutoCompleteTextView autoCompleteTextView;
    String selectedDate, selectedTime, selectedModule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_online);

        Button searchTutor = findViewById(R.id.searchTutor);
        searchTutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the required fields are filled
                if (selectedDate == null || selectedTime == null || selectedModule == null) {
                    Toast.makeText(book_Online.this, "Please select date, time, and module", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(book_Online.this, AvailableTutors.class);
                    intent.putExtra("DATE", selectedDate);
                    intent.putExtra("TIME", selectedTime);
                    intent.putExtra("MODULE", selectedModule);
                    startActivity(intent);
                }
            }
        });

        ImageButton selectDateButton = findViewById(R.id.selectDateButton);
        selectDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalendarDialog();
            }
        });

        autoCompleteTextView = findViewById(R.id.autocompleText);
        adapterItems = new ArrayAdapter<>(this, R.layout.list_module_names, modules);
        autoCompleteTextView.setAdapter(adapterItems);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String item = adapterView.getItemAtPosition(position).toString();
                if (item.equalsIgnoreCase("Programming")) {
                    selectedModule = item;
                    Toast.makeText(book_Online.this, "Module name: " + item, Toast.LENGTH_SHORT).show();
                } else if (item.equalsIgnoreCase("Software Engineering.") || item.equalsIgnoreCase("Information System") || item.equalsIgnoreCase("Mobile App Development")) {
                    Toast.makeText(book_Online.this, item + " is not available", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ImageButton selectTimeButton = findViewById(R.id.selectTimeButton);
        selectTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDialog();
            }
        });
    }

    private void showCalendarDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
                TextInputEditText selectedDateEditText = findViewById(R.id.seletedDate);
                selectedDateEditText.setText(selectedDate);
            }
        }, year, month, dayOfMonth);
        datePickerDialog.show();
    }

    private void showTimeDialog() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                TextInputEditText selectedTimeEditText = findViewById(R.id.selectedTime);
                selectedTimeEditText.setText(selectedTime);
            }
        }, hour, minute, false);
        timePickerDialog.show();
    }
}
