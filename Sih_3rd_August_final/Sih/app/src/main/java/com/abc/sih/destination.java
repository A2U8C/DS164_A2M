package com.abc.sih;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class destination extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private double myLat,myLong;
    private String mUid;
    private double desLat,desLong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mUid = FirebaseAuth.getInstance().getUid();
        FirebaseDatabase.getInstance().getReference().child("Users").child(mUid).child("find_location").child("location").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myLat = dataSnapshot.child("latitude").getValue(double.class);
                myLong = dataSnapshot.child("longitude").getValue(double.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                desLat = latLng.latitude;
                desLong = latLng.longitude;
                mMap.addMarker(new MarkerOptions().position(latLng).title("Destination"));
                poppup();

            }
        });
        LatLng sydney = new LatLng(-34, 151);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private void poppup() {
        new AlertDialog.Builder(this)
                .setTitle("Mark this point")
                .setMessage("Are you sure you want to proceed?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        LatLng start = new LatLng(myLat,myLong);
                        LatLng dest = new LatLng(desLat,desLong);
                        LatLng c2 = LatLngBounds.builder().include(start).include(dest).build().getCenter();
                        LatLng c3 = LatLngBounds.builder().include(start).include(c2).build().getCenter();
                        LatLng c4 = LatLngBounds.builder().include(c2).include(dest).build().getCenter();
                        Map cord = new HashMap<>();
                        cord.put("startlon",start.longitude);
                        cord.put("startlat",start.latitude);
                        cord.put("c1lat",c3.latitude);
                        cord.put("c1lon",c3.longitude);
                        cord.put("c2lat",c2.latitude);
                        cord.put("c2lon",c2.longitude);
                        cord.put("c3lat",c4.latitude);
                        cord.put("c3lon",c4.longitude);
                        cord.put("c4lat",dest.latitude);
                        cord.put("c4lon",dest.longitude);
                        FirebaseDatabase.getInstance().getReference().child("CheckCreate").child(mUid).setValue(cord).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Intent stay = new Intent(destination.this,MainActivity.class);
                                startActivity(stay);
                            }
                        });



                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}