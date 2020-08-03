package com.abc.sih;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.PopupWindow;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<LatLng> listpoints;
    private PopupWindow mPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        listpoints = new ArrayList<>();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method  will only be triggered once the user has
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
        DatabaseReference yellow = FirebaseDatabase.getInstance().getReference().child("Zones").child("yellow");
        DatabaseReference red = FirebaseDatabase.getInstance().getReference().child("Zones").child("red");
        yellow.keepSynced(true);
        yellow.keepSynced(true);
        red.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    double lat = ds.child("lat").getValue(double.class);
                    double lon = ds.child("lon").getValue(double.class);
                    double rad = ds.child("rad").getValue(double.class);
                    String title = ds.child("title").getValue().toString();
                    LatLng location = new LatLng(lat,lon);
                    Log.d("mapact_16","longit"+lon+"lattitude"+lat);
                    drawCircle(location,rad,"red",title);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        yellow.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    double lat = ds.child("lat").getValue(double.class);
                    double lon = ds.child("lon").getValue(double.class);
                    double rad = ds.child("rad").getValue(double.class);
                    String title = ds.child("title").getValue().toString();

                    LatLng location = new LatLng(lat,lon);
                    Log.d("mapact_16","longit"+lon+"lattitude"+lat);
                    drawCircle(location,rad,"yellow",title);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if (listpoints.size()==1){
                    listpoints.clear();
                    mMap.clear();
                }
                double lat = latLng.latitude;
                double lon = latLng.longitude;
                listpoints.add(latLng);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                mMap.addMarker(markerOptions);
                poppup(lat,lon);


            }
        });
    }

    private void poppup(final double lat, final double lon) {
        new AlertDialog.Builder(this)
                .setTitle("Create Zone")
                .setMessage("Are you sure you want to create zone here?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        Intent create = new Intent(MapsActivity.this,Zonecreate.class);
                        create.putExtra("lat",lat);
                        create.putExtra("lon",lon);
                        startActivity(create);
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();


    }


    private void drawCircle(LatLng point, double rad, String color, String title){

        // Instantiating CircleOptions to draw a circle around the marker
        CircleOptions circleOptions = new CircleOptions();

        // Specifying the center of the circle
        circleOptions.center(point);

        // Radius of the circle
        circleOptions.radius(rad);

        // Border color of the circle
        circleOptions.strokeColor(Color.BLACK);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(point);
        markerOptions.title(title);
        mMap.addMarker(markerOptions);
        // Fill color of the circle
        if(color.equals("red")) {
            circleOptions.fillColor(0x30ff0000);
        }else{
            circleOptions.fillColor(0x30ffff00);
        }
        // Border width of the circle
        circleOptions.strokeWidth(5.0f);

        // Adding the circle to the GoogleMap
        mMap.addCircle(circleOptions);

    }



}
