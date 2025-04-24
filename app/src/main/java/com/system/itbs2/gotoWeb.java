package com.system.itbs2;

import static com.system.itbs2.stud_dashboard.openDrawer;
import static com.system.itbs2.stud_dashboard.redirectActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

public class gotoWeb extends AppCompatActivity {
    // Declare variables
    DrawerLayout drawerLayout;
    ImageView menuOpenDrawer;
    LinearLayout logoutBtn, dashboard, feedback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goto_web);

       ImageButton bookICT = (ImageButton) findViewById(R.id.BOOK);
       ImageButton selfLearning = (ImageButton) findViewById(R.id.selfLearning);
        ImageButton attemptTest = (ImageButton) findViewById(R.id.attempttest);

        // Initialize variables with corresponding views
        drawerLayout = findViewById(R.id.drawerLayout);
        dashboard = findViewById(R.id.dashboard);
        feedback = findViewById(R.id.send_feedback);
        logoutBtn = findViewById(R.id.logout);

        bookICT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent = new Intent(gotoWeb.this, book_Online.class);
              startActivity(intent);
            }
        });
        selfLearning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //path of the programming.
                String url = "https://quizlet.com/search?query=java-programming-8-edition&type=sets&useOriginal=";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        attemptTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //path of the programming.
                String url = "https://quizizz.com/admin/search/java%20programming?source=HeroSearchBar&page=FeaturedPage&searchSource=normal&arid=&apos=2&contentTypes=%5B%22quiz%22%2C%22presentation%22%2C%22video-quiz%22%2C%22reading-quiz%22%5D";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
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

                redirectActivity(gotoWeb.this,stud_dashboard.class);
            }
        });
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(gotoWeb.this,LoginAct.class);
                finish();
            }
        });

        // Set click listener for "Send Feedback" menu item
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to the send feedback activity
                redirectActivity(gotoWeb.this, sendfeedback.class);
            }
        });

    }
}