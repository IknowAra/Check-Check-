package com.javaProject.checkcheck;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Notification;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class GroupActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction;

    Member memberFragment = new Member();
    Todo todoFragment = new Todo();
    Graph graphFragment = new Graph();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        bottomNavigationView = findViewById(R.id.nav_view);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, memberFragment).commitAllowingStateLoss();

        bottomNavigationView.setOnNavigationItemSelectedListener(new bottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        fragmentTransaction.replace(R.id.frameLayout, homeFragment).commitAllowingStateLoss();
                        break;
                    case R.id.navigation_dashboard:
                        fragmentTransaction.replace(R.id.frameLayout, dashboardFragment).commitAllowingStateLoss();
                        break;
                    case R.id.navigation_notifications:
                        fragmentTransaction.replace(R.id.frameLayout, notificationFragment).commitAllowingStateLoss();
                        break;
                }
                return false;
            }
        });
    }
}