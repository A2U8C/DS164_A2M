package com.abc.sih;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.navigation_home);
        bottomNav.bringToFront();
        bottomNav.setOnNavigationItemSelectedListener(navListener);


        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_contanier, new AdminHomeFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            selectedFragment = new AdminHomeFragment();
                            break;
                        case R.id.navigation_news:
                            selectedFragment = new NewsFragmentAdmin();
                            break;
                        case R.id.navigation_profile:
                            selectedFragment = new AdminProfileFragment();
                            break;


                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_contanier, selectedFragment).commit();

                    return true;
                }
            };
}