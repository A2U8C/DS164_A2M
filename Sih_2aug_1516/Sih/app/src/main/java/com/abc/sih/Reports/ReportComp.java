package com.abc.sih.Reports;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.abc.sih.R;
import com.abc.sih.firRegistration;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import at.markushi.ui.CircleButton;

import static com.applozic.mobicommons.commons.core.utils.PermissionsUtils.REQUEST_LOCATION;

public class ReportComp extends AppCompatActivity {

    // private Toolbar mToolbar;
    private RecyclerView mUsersList;

    private DatabaseReference mUsersDatabase;
    private DatabaseReference mUserRef;
    private FirebaseAuth mAuth;
    private String disp_name, disp_user_image, disp_uploaded_image, disp_likes;
    private CircleButton mPostBtn;
    private String post_participated;
    private StorageReference mImageStorage;
    private ProgressDialog mProgressDialog;
    private static final int GALLERY_PICK = 1;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mUserDatabase;
    private TextView imptxt;
    private ImageView upvote,downvote;
    private String current_uid;
    private DatabaseReference mPostsDatabase;
    List<String> utt;
    private String download_url;
    private String user_name,user_dp;
    private LinearLayoutManager mLayoutManager;

    private ViewPager mViewPager;
    private SectionsPageAdapter mSectionsPagerAdapter;


    private TabLayout mTabLayout;
    //private DatabaseReference mPostDatabase;
    private String upld_image;
    LocationManager locationManager;
    String latitude, longitude;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_comp);

        //mToolbar = (Toolbar) findViewById(R.id.users_appBar);


        // setSupportActionBar(mToolbar);
        // getSupportActionBar().setTitle("All Posts");
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mViewPager = (ViewPager) findViewById(R.id.main_tabPager);
        mSectionsPagerAdapter = new SectionsPageAdapter(getSupportFragmentManager());

       mViewPager.setAdapter(mSectionsPagerAdapter);
      //  mViewPager.setAdapter(mSectionsPagerAdapter);

        mTabLayout = (TabLayout) findViewById(R.id.main_tabs);
        mTabLayout.setupWithViewPager(mViewPager);
        mAuth = FirebaseAuth.getInstance();
        mImageStorage = FirebaseStorage.getInstance().getReference();
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());

        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        mUserDatabase.keepSynced(true);
        mPostsDatabase = FirebaseDatabase.getInstance().getReference().child("Posts");
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists() && dataSnapshot.child("name").getValue()!=null && dataSnapshot.child("image").getValue()!=null){
                    user_name = dataSnapshot.child("name").getValue().toString();
                    user_dp = dataSnapshot.child("image").getValue().toString();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseDatabase.getInstance().getReference().child("Posts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    if (!ds.hasChild("name")){
                        FirebaseDatabase.getInstance().getReference().child("Posts").child(ds.getKey()).removeValue();
                        //  karu kya runha
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mPostBtn = (CircleButton) findViewById(R.id.imp_btn);

        mPostBtn.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),abc ,Toast.LENGTH_SHORT).show();

/*

                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PICK);



*/
Intent st = new Intent(ReportComp.this, firRegistration.class);
startActivity(st);

            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {

            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    // .setAspectRatio(1, 1)
                    .start(this);

            //Toast.makeText(SettingsActivity.this, imageUri, Toast.LENGTH_SHORT).show();
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mProgressDialog = new ProgressDialog(ReportComp.this);
                mProgressDialog.setTitle("Uploading Image..");
                mProgressDialog.setMessage("Please wait while we upload and process the image");
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.show();

                Uri resultUri = result.getUri();

                final String current_user_id = mCurrentUser.getUid();
                final Date currentTime = Calendar.getInstance().getTime();

                StorageReference filepath = mImageStorage.child("posts").child(current_user_id+"_"+ currentTime+ ".jpg");

                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if(task.isSuccessful()){
                            //Toast.makeText(SettingsActivity.this, "Working", Toast.LENGTH_SHORT).show();

                            //final String download_url= task.getResult().toString();

                            /////////////////

                            mImageStorage.child("posts").child(current_user_id+"_"+currentTime+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    download_url = uri.toString();
                                    final String key = mPostsDatabase.push().getKey();
                                    mPostsDatabase.child(key).child("link").setValue(download_url).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if(task.isSuccessful()){


                                                mProgressDialog.dismiss();
                                                Toast.makeText(ReportComp.this,"Successfully uploaded", Toast.LENGTH_SHORT).show();
                                                /////////////////
                                                mPostsDatabase.child(key).child("name").setValue(mCurrentUser.getUid());
                                                mPostsDatabase.child(key).child("user_name").setValue(user_name);
                                                mPostsDatabase.child(key).child("image").setValue(user_dp);
                                                mPostsDatabase.child(key).child("time").setValue(ServerValue.TIMESTAMP);
                                                mPostsDatabase.child(key).child("likes").setValue(0);
                                                mPostsDatabase.child(key).child("link").setValue(download_url);


                                                ///update notification///
                                                String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                                                String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                final String key = FirebaseDatabase.getInstance().getReference().child("Notification").push().getKey();
                                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy ");
                                                String formattedDate = dateFormat.format(new Date()).toString();

                                                SimpleDateFormat dateFormatt = new SimpleDateFormat("hh.mm.ss aa");
                                                String formattedDatet = dateFormatt.format(new Date()).toString();
                                                ////updating notification

                                                final HashMap<String, Object> userdataMap = new HashMap<>();
                                                userdataMap.put("date",date);
                                                userdataMap.put("desc","Thanks for reporting the case with us. Investigation process will start soon and you will be notified accordingly ");
                                                userdataMap.put("type","Reporter's Console");
                                                userdataMap.put("time",formattedDatet);

                                                FirebaseDatabase.getInstance().getReference().child("Notification")
                                                        .child(userid).child(key).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                    }
                                                });

                                                /////


                                                //Picasso.with(SettingsActivity.this).load(download_url).into(mDisplayImage);
                                            }

                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                }
                            });
                            //////////////////



                        }else{

                            Toast.makeText(ReportComp.this, "Error in uploading", Toast.LENGTH_SHORT).show();
                            mProgressDialog.dismiss();
                        }
                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new  DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                ReportComp.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                ReportComp.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locationGPS != null) {
                double lat = locationGPS.getLatitude();
                double longi = locationGPS.getLongitude();
                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);
                //showLocation.setText("Your Location: " + "\n" + "Latitude: " + latitude + "\n" + "Longitude: " + longitude);
            } else {
                Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
