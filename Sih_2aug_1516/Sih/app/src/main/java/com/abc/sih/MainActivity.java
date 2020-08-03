package com.abc.sih;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.plotprojects.retail.android.Plot;

public class MainActivity extends AppCompatActivity implements LocationListener {
    private FirebaseAuth mAuth;
    private DatabaseReference mUserRef;
    private String mUserId;
    private String type,station;

    Location location;
    private LocationManager locationManager;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {//not logged in

            sendtoStart();

        }
        else{
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
            location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
            onLocationChanged(location);
        }
    }

    private void sendtoStart() {
        Intent startIntent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(startIntent);
        //finish(); // back button is disabled
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Plot.init(this);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            mUserId = FirebaseAuth.getInstance().getUid();
            mUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mUserId);

        }


        Log.d("findIt Bro",mUserId);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_filter);
        bottomNav.bringToFront();
        bottomNav.setOnNavigationItemSelectedListener(navListener);


        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_contanier, new Frag5()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            selectedFragment = new Frag1();
                            break;
                        case R.id.nav_favorites:
                            selectedFragment = new NewsFragment();
                            break;
                        case R.id.nav_search:
                            selectedFragment = new Frag3();
                            break;

                        case R.id.nav_person:
                            selectedFragment = new Frag4();
                            break;

                        case R.id.nav_filter:
                            selectedFragment = new Frag5();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_contanier, selectedFragment).commit();

                    return true;
                }
            };

    @Override
    public void onLocationChanged(Location location) {
        LocationsHelper helper=new LocationsHelper(location);
        Toast.makeText(this, "Location Updated", Toast.LENGTH_SHORT).show();
        mUserRef.child("find_location").setValue(helper);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}