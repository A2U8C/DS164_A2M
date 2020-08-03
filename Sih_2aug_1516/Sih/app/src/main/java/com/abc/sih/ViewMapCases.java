package com.abc.sih;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.temporal.TemporalAmount;
import java.util.ArrayList;

public class ViewMapCases extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ArrayList<LatLng> listpoints = new ArrayList<LatLng>();
    /*LatLng sydney = new LatLng(-34,151);
    LatLng tamWorth = new LatLng(-31.083332, 150.916672);
    LatLng NewCaste = new LatLng(-32.916668, 151.750000);
    LatLng Brisbane = new LatLng(-27.470125, 153.021072);
    LatLng Dubbo = new LatLng(-32.256943, 148.601105);*/
    DatabaseReference ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_map_cases);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        /*arrayList.add(sydney);
        arrayList.add(tamWorth);
        arrayList.add(NewCaste);
        arrayList.add(Brisbane);
        arrayList.add(Dubbo);*/
        ref = FirebaseDatabase.getInstance().getReference().child("points");
        listpoints = new ArrayList<>();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               /* if(dataSnapshot.exists() && dataSnapshot.child("lat").getValue()!=null && dataSnapshot.child("lon").getValue()!=null
                && dataSnapshot.child("type").getValue()!=null && dataSnapshot.child("date").getValue()!=null)
                {*/
                    for(DataSnapshot ds : dataSnapshot.getChildren()){

                        Double lat = ds.child("lat").getValue(double.class);
                        Double lon = ds.child("lon").getValue(double.class);
                        String tt_type = ds.child("type").getValue().toString();
                        String tt_title = ds.child("date").getValue().toString();
                        if(tt_type.toLowerCase().equals("tree_cutting"))
                        {
                            LatLng locaa = new LatLng(lat,lon);
                            // mMap.addMarker(new MarkerOptions().position(locaa).title("IMP"));
                            // new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.tree_cut));
                            Marker marker = mMap.addMarker(new MarkerOptions().position(locaa).title(tt_title)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.tree_cut)));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(locaa));
                        }
                        else if(tt_type.toLowerCase().equals("poaching"))
                        {
                            LatLng locaa = new LatLng(lat,lon);
                            // mMap.addMarker(new MarkerOptions().position(locaa).title("IMP"));
                            // new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.tree_cut));
                            Marker marker = mMap.addMarker(new MarkerOptions().position(locaa).title(tt_title)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.hunting)));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(locaa));
                        }
                        else{
                            LatLng locaa = new LatLng(lat,lon);
                            mMap.addMarker(new MarkerOptions().position(locaa).title(tt_title));
                            // new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.tree_cut));
                            // Marker marker = mMap.addMarker(new MarkerOptions().position(locaa)
                            //   .icon(BitmapDescriptorFactory.fromResource(R.drawable.tree_cut)));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(locaa));
                        }


                    }
                //}


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

       /* for(int i=0;i<listpoints.size();i++)
        {
            mMap.addMarker(new MarkerOptions().position(listpoints.get(i)).title("marker"));
           // marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.my_marker_icon)));
            new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.default_avatar));

            mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(listpoints.get(i)));
        }*/

        // Add a marker in Sydney and move the camera
       // LatLng sydney = new LatLng(-34, 151);
       // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
       // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}