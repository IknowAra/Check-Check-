package com.javaProject.checkcheck;


import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.google.android.material.bottomnavigation.BottomNavigationView;


public class GroupActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction;

    MemberFragment memberFragment = new MemberFragment();
    TodoFragment todoFragment = new TodoFragment();
    GraphFragment graphFragment = new GraphFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        bottomNavigationView = findViewById(R.id.nav_view);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.frameLayout, memberFragment).commitAllowingStateLoss();

        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            switch (menuItem.getItemId()) {
                case R.id.navigation_member:
                    fragmentTransaction.replace(R.id.frameLayout,memberFragment).commitAllowingStateLoss();
                break;
                case R.id.navigation_todo:
                    fragmentTransaction.replace(R.id.frameLayout,todoFragment).commitAllowingStateLoss();
                    break;
                case R.id.navigation_graph:
                    fragmentTransaction.replace(R.id.frameLayout,graphFragment).commitAllowingStateLoss();
                    break;
            }
            return false;
        });

    }
}