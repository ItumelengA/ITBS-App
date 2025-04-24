package com.system.itbs2;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class stud_dashboard extends AppCompatActivity {

    // Declare variables
    DrawerLayout drawerLayout;
    ImageView menuOpenDrawer;
    LinearLayout logoutBtn, dashboard, feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stud_dashboard);

        // Initialize variables with corresponding views
        drawerLayout = findViewById(R.id.drawerLayout);
        dashboard = findViewById(R.id.dashboard);
        feedback = findViewById(R.id.send_feedback);
        logoutBtn = findViewById(R.id.logout);

        ImageButton DATASCINCE = findViewById(R.id.DATASCIENE);
        ImageButton ICT = findViewById(R.id.ICT);
        ImageButton COMPUTERSCIENCE = findViewById(R.id.COMPUTERSCIENCE);

        menuOpenDrawer = findViewById(R.id.menu);

        // Set click listener for opening the drawer
        menuOpenDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer(drawerLayout);
            }
        });

        // Set click listener for "My Account" menu item
        dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Refresh the current activity
                recreate();
            }
        });
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(stud_dashboard.this,selectLogin.class);
                finish();
            }
        });

        // Set click listener for "Send Feedback" menu item
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to the send feedback activity
                redirectActivity(stud_dashboard.this, sendfeedback.class);
            }
        });
  ICT.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
          Intent intent = new Intent(stud_dashboard.this,gotoWeb.class);
          startActivity(intent);
      }
  });
  DATASCINCE.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
          Intent intent = new Intent(stud_dashboard.this,gotoWeb.class);
          startActivity(intent);
      }
  });
  COMPUTERSCIENCE.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
          Intent intent = new Intent(stud_dashboard.this,gotoWeb.class);
          startActivity(intent);
      }
  });

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
        activity.finish();
    }

    // Close the drawer when the activity is paused
    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }
}
