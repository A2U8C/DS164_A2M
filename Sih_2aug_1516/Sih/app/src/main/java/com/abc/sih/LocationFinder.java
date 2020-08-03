package com.abc.sih;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

import br.com.bloder.magic.view.MagicButton;

public class LocationFinder extends AppCompatActivity implements LocationListener {


    private TextView tv_officer_name;
    private LocationManager locationManager;
    private TextView tv_officer_contact;

    private Button btn_call_closest;

    int i = 0;

    private FirebaseAuth mAuth;
    private String mCurrent_user_id;


    MagicButton magic_button;

    ArrayList<LatLng> alist = new ArrayList<LatLng>();
    ArrayList<Location> blist = new ArrayList<Location>();


    Location locationc;


    public double smallestDistance = -1;


    private String userName;
    private String userNameKey;

    private String officerName;
    public double dist;


    String a, b;

    Location closestLocation;

    DatabaseReference mDatabse;
    DatabaseReference mUserCh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_finder);

        mDatabse = FirebaseDatabase.getInstance().getReference().child("Users");


        mAuth = FirebaseAuth.getInstance();
        tv_officer_name = (TextView) findViewById(R.id.tv_officer_name);
        tv_officer_contact = (TextView) findViewById(R.id.tv_officer_contact);
        magic_button = (MagicButton) findViewById(R.id.magic_button);


        //btn_call_closest=(Button) findViewById(R.id.btn_call_closest);


        if (mAuth.getCurrentUser() != null) {
            mCurrent_user_id = mAuth.getCurrentUser().getUid();
            //mCurrent_user_id = a.substring(0, a.length() - 8);
            Log.d("authorcheck",""+ mCurrent_user_id);
            //loggs3.setText("" + mCurrent_user_id);
        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


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
        locationc = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);

        closestLocation = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
        //closestLocation =new Location("dummyProvider");

        onLocationChanged(locationc);

        mDatabse.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @NonNull String s) {

                Log.d("CheckNT", "123");

                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String jk = childSnapshot.getRef().getParent().getKey();

                    if ((!mCurrent_user_id.equals(jk))
                            && (childSnapshot.child("location").child("latitude").getValue() != null)
                    ) {
                        Location location_new = new Location("dummyprovider");


                        Float lat = Float.parseFloat("" + childSnapshot.child("location").child("latitude").getValue());
                        Float lng = Float.parseFloat("" + childSnapshot.child("location").child("longitude").getValue());
                        Float alt = Float.parseFloat("" + childSnapshot.child("location").child("altitude").getValue());

                        Float acc = Float.parseFloat("" + childSnapshot.child("location").child("accuracy").getValue());
                        Float speed = Float.parseFloat("" + childSnapshot.child("location").child("speed").getValue());


                        location_new.setLatitude(lat);
                        location_new.setLongitude(lng);
                        location_new.setAltitude(alt);


                        location_new.setAccuracy(acc);
                        location_new.setSpeed(speed);
                        blist.add(location_new);

                        //loggs4.append("\n"+location_new);

                    }

                }
//
//                loggs4.setText(""+blist);

                for (Location locationtf : blist) {

                    dist = Math.abs(locationc.distanceTo(locationtf));
                    if ((smallestDistance == -1) || (dist < smallestDistance)) {
                        smallestDistance = dist;
                        closestLocation = locationtf;
                    }
                }

                //loggs5.setText("New Longitude: "+closestLocation.getLongitude()+"\n"+"Latitude: "+closestLocation.getLatitude()+"\nHAcc: "+closestLocation.getAltitude()+"\nDistance: "+smallestDistance);


                Log.d("sMALLEST dISTANCE", ""+smallestDistance);

                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    if ((childSnapshot.child("location").child("latitude").getValue() != null)
                            && (Float.parseFloat("" + childSnapshot.child("location").child("latitude").getValue()) == closestLocation.getLatitude())
                            && (Float.parseFloat("" + childSnapshot.child("location").child("longitude").getValue()) == closestLocation.getLongitude())
                            && (Float.parseFloat("" + childSnapshot.child("location").child("altitude").getValue()) == closestLocation.getAltitude())
                            && (Float.parseFloat("" + childSnapshot.child("location").child("speed").getValue()) == closestLocation.getSpeed())
                            && (Float.parseFloat("" + childSnapshot.child("location").child("accuracy").getValue()) == closestLocation.getAccuracy())
                    ) {
                        userNameKey = childSnapshot.getRef().getParent().getKey().toString();
                        userName=dataSnapshot.child("phone_number").getValue().toString();
                        //FirebaseDatabase.getInstance().getReference().child("Users").child(userNameKey).child("phone_number").getV.toString();
//
                        Log.d("chvalue ", userName);
//                        loggs3.setText(""+userName);

//                        String smn = "tel:"+userName;
//                    Intent intent = new Intent(Intent.ACTION_CALL);
//                    Log.d("CheckVer",""+userName);
//                        intent.setData(Uri.parse(smn));
//                        startActivity(intent);


                        break;
                    }
                }
                //loggs3.setText(""+userName);
                if(userNameKey!=null)
                {
                    mUserCh = FirebaseDatabase.getInstance().getReference().child("Users").child(userNameKey);
                    mUserCh.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Map<String, Object> newPost = (Map<String, Object>) dataSnapshot.getValue();
                            tv_officer_name.setText("" + newPost.get("name"));
                            tv_officer_contact.setText(newPost.get("phone_number").toString());
                            userName=tv_officer_contact.getText().toString();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    tv_officer_name.setText(officerName);

                    magic_button.setMagicButtonClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String smn = "tel:" + userName;
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            Log.d("CheckVer", "" + userName);
                            intent.setData(Uri.parse(smn));
                            if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    Activity#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for Activity#requestPermissions for more details.
                                return;
                            }
                            startActivity(intent);
                        }
                    });
                }


                /*btn_call_closest.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String smn = "tel:"+userName;
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        Log.d("CheckVer",""+userName);
                        intent.setData(Uri.parse(smn));
                        startActivity(intent);
                    }
                });
*/

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });










    }

    @Override
    public void onLocationChanged(Location location) {
        double longitude=location.getLongitude();
        double latitude=location.getLatitude();
        //tv_officer_name.setText("Longitude: "+longitude+"\n"+"Latitude: "+latitude);
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
