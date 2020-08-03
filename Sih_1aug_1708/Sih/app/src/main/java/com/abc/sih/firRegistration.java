package com.abc.sih;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.abc.sih.MainActivity;
import com.abc.sih.R;
import com.abc.sih.SignatureActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jeevandeshmukh.glidetoastlib.GlideToast;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class firRegistration extends AppCompatActivity {

    private Calendar myCalendar;
    private Spinner input_type,species_type,check;
    private EditText  input_aadhar, input_email;
    private EditText input_details, input_relation;
    private DatabaseReference CaseDatabase, CaseRestricted;
    private ImageView attachment1, attachment2, attachment3;
    private CheckBox terms;
    private ProgressDialog mProgress;
    private ProgressDialog mProgressDialog;
    private Button savebutton, spch;
    private Button btn1, btn2, acs1, acs2;
    private TextView trial, input_mobile,species;
    private String link1 = null, link2 = null, link3 = null, mCurrent_user_id, email;
    private static final int GALLERY_PICK = 1;
    private int choice = 1;
    private StorageReference mImageStorage;
    private TextView esign;
    private String esign_link = null;
    private TextView trying;
    private String name, usermail;
    private int i = 0;

    private ImageView greentick;

    private TextView trying2;
    DatabaseReference mUserDatabase;
    private FirebaseAuth mAuth;
    private String user_name, user_dp, user_type, user_station, Div, Res, Bet;
    private List<String> item1,item2,item3;
    LocationManager locationManager;
    String latitude, longitude;
    private String spec ="N.A";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fir_registration);
////////////////////////////////
        email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        int index = email.indexOf('@');
        email = email.substring(0, index);
        acs1 = findViewById(R.id.acs1);
        acs2 = findViewById(R.id.acs2);
        species = findViewById(R.id.textspecies);
        check = findViewById(R.id.checkpoint);
        input_type = (Spinner) findViewById(R.id.input_type);
        species_type = (Spinner) findViewById(R.id.specieselect);
        item1 = new ArrayList<String>();
        item1.add("Poaching");
        item1.add("Smuggling");
        item1.add("Endangered Species Poaching");
        item1.add("other");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(firRegistration.this, android.R.layout.simple_spinner_item, item1);
        input_type.setAdapter(adapter);
        input_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String sel = input_type.getSelectedItem().toString();
                if (sel.equals("Endangered Species Poaching")){
                    species.setVisibility(View.VISIBLE);
                    species_type.setVisibility(View.VISIBLE);

                    item2 = new ArrayList<String>();
                    item2.add("Green pigeon");
                    item2.add("Andaman crake,");
                    item2.add("Andaman woodpigeon");
                    item2.add("White-headed starling");
                    ArrayAdapter<String> spec = new ArrayAdapter<String>(firRegistration.this, android.R.layout.simple_spinner_item, item2);
                    species_type.setAdapter(spec);
                }else {
                    species_type.setVisibility(View.GONE);
                    species.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        input_aadhar = (EditText) findViewById(R.id.input_aadhar);
        input_aadhar.setHint(Html.fromHtml(getString(R.string.hint_aadhar)));

        input_mobile = (TextView) findViewById(R.id.input_mobile);
        input_mobile.setText(email);

        input_email = (EditText) findViewById(R.id.input_email);
        input_email.setHint(Html.fromHtml(getString(R.string.hint_email)));

        input_details = (EditText) findViewById(R.id.input_details);
        input_details.setHint(Html.fromHtml(getString(R.string.hint_details)));

        input_relation = (EditText) findViewById(R.id.input_relation);
        input_relation.setHint(Html.fromHtml(getString(R.string.hint_relation)));

        CaseDatabase = FirebaseDatabase.getInstance().getReference().child("Posts");
        CaseRestricted = FirebaseDatabase.getInstance().getReference().child("Posts-restricted");

        terms = (CheckBox) findViewById(R.id.terms);
        savebutton = (Button) findViewById(R.id.submitbtn);
        attachment1 = (ImageView) findViewById(R.id.input_attach1);
        attachment2 = (ImageView) findViewById(R.id.input_attach2);
        attachment3 = (ImageView) findViewById(R.id.input_attach3);
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        esign = (TextView) findViewById(R.id.esign);
        trying = (TextView) findViewById(R.id.text1);
        mImageStorage = FirebaseStorage.getInstance().getReference();
        esign_link = getIntent().getStringExtra("sign_link");
        greentick = (ImageView) findViewById(R.id.green_tick);
        trying2 = (TextView) findViewById(R.id.text2);
        spch = findViewById(R.id.spchact);

        item3 = new ArrayList<String>();
        item3.add("C1");
        item3.add("C2");
        item3.add("C3");
        item3.add("C4");
        ArrayAdapter<String> checkpoint = new ArrayAdapter<String>(firRegistration.this, android.R.layout.simple_spinner_item, item3);
        check.setAdapter(checkpoint);



        spch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent st = new Intent(firRegistration.this, SpeechActivity.class);
                startActivity(st);
            }
        });

        myCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        input_relation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(firRegistration.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        acs2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acs1.setTextColor(Color.parseColor("#35ba1a"));
                int color = Color.parseColor("#ffffff");
                acs1.getBackground().mutate().setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC));
                acs2.setTextColor(Color.parseColor("#ffffff"));
                int color2 = Color.parseColor("#35ba1a");
                acs2.getBackground().mutate().setColorFilter(new PorterDuffColorFilter(color2, PorterDuff.Mode.SRC));
                i = 1;

                // input_details.setHint(Html.fromHtml(getString(R.string.hint_details)));
            }
        });
        acs1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acs2.setTextColor(Color.parseColor("#35ba1a"));
                int color = Color.parseColor("#ffffff");
                acs2.getBackground().mutate().setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC));
                acs1.setTextColor(Color.parseColor("#ffffff"));
                int color2 = Color.parseColor("#35ba1a");
                acs1.getBackground().mutate().setColorFilter(new PorterDuffColorFilter(color2, PorterDuff.Mode.SRC));
                i = 0;
            }
        });


//////////////////////
        mAuth = FirebaseAuth.getInstance();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getUid());
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists() && dataSnapshot.child("name").getValue() != null && dataSnapshot.child("image").getValue() != null) {
                    user_name = dataSnapshot.child("name").getValue().toString();
                    user_dp = dataSnapshot.child("image").getValue().toString();
                    name = dataSnapshot.child("name").getValue().toString();
                    usermail = dataSnapshot.child("email").getValue().toString();
                    input_aadhar.setText(name);
                    user_station = dataSnapshot.child("station").getValue().toString();
                    user_type = dataSnapshot.child("type").getValue().toString();
                    input_email.setText(usermail);
                    switch (user_type) {
                        case "D":
                            Div = dataSnapshot.child("Division").getValue().toString();
                            break;
                        case "R":
                            Div = dataSnapshot.child("Division").getValue().toString();
                            Res = dataSnapshot.child("Range").getValue().toString();
                            break;
                        default:
                            Div = dataSnapshot.child("Division").getValue().toString();
                            Res = dataSnapshot.child("Range").getValue().toString();
                            Bet = dataSnapshot.child("Beat").getValue().toString();
                            break;
                    }
                    //setUserid(user_type,user_station);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        ///////////
        if (esign_link != null) {
            greentick.setVisibility(View.VISIBLE);
        }

        esign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(firRegistration.this, SignatureActivity.class);
                intent.putExtra("activity", "FIR");
                startActivity(intent);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn1.setTextColor(Color.parseColor("#35ba1a"));
                int color = Color.parseColor("#ffffff");
                btn1.getBackground().mutate().setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC));
                btn2.setTextColor(Color.parseColor("#ffffff"));
                int color2 = Color.parseColor("#35ba1a");
                btn2.getBackground().mutate().setColorFilter(new PorterDuffColorFilter(color2, PorterDuff.Mode.SRC));

                // input_details.setHint(Html.fromHtml(getString(R.string.hint_details)));

                input_aadhar.setHint(Html.fromHtml(getString(R.string.hint_passport)));
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn2.setTextColor(Color.parseColor("#35ba1a"));
                int color = Color.parseColor("#ffffff");
                btn2.getBackground().mutate().setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC));
                btn1.setTextColor(Color.parseColor("#ffffff"));
                int color2 = Color.parseColor("#35ba1a");
                btn1.getBackground().mutate().setColorFilter(new PorterDuffColorFilter(color2, PorterDuff.Mode.SRC));

                input_aadhar.setHint(Html.fromHtml(getString(R.string.hint_aadhar)));
            }
        });

////////////////////////////////////////// TO ADD ALL THE ATTACHMENTS   /////////////////////////////////
        attachment1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                choice = 1;
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PICK);

            }
        });

        attachment2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                choice = 2;
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PICK);

            }
        });


        attachment3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                choice = 3;
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PICK);

            }
        });


////////////////////////////////////////////////////////////////////////////////////////////////////////
        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ////geo-tagging//
                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                if (ActivityCompat.checkSelfPermission(firRegistration.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(firRegistration.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                Location locationGPS = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
                double lat = locationGPS.getLatitude();
                double longi = locationGPS.getLongitude();
                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);

                String input1 = input_type.getSelectedItem().toString();
                String input2 = input_aadhar.getText().toString();
                String point = check.getSelectedItem().toString();
                //String input4= input_mobile.getText().toString();
                String input5 = input_email.getText().toString();
                String input6 = input_details.getText().toString();
                String input7 = input_relation.getText().toString();

                HashMap<String, Object> case_data = new HashMap<>();
                case_data.put("type",input1);
                case_data.put("uploader_name",input2);
                case_data.put("mobile",email);
                case_data.put("email",input5);
                case_data.put("case_details",input6);
                case_data.put("checkpoint",point);
                case_data.put("time_date_case",input7);
                case_data.put("time", ServerValue.TIMESTAMP);
                case_data.put("likes",0);
                case_data.put("name",FirebaseAuth.getInstance().getCurrentUser().getUid());
                case_data.put("user_name",user_name);
                case_data.put("image",user_dp);
                case_data.put("case_assigned","no");
                case_data.put("link",link1);
                case_data.put("link1",link2);
                case_data.put("link2",link3);
                case_data.put("signature_link",esign_link);
                case_data.put("geo_lat",latitude);
                case_data.put("geo_long",longitude);
                if (input1.equals("Endangered Species Poaching")){
                     spec = species_type.getSelectedItem().toString();
                }
                case_data.put("species",spec);
                // case_data.put("attachments",)

                if(input1.equals("") || input2.equals("")
                        ||input5.equals("") || input6.equals("") || input7.equals("") || !terms.isChecked() || esign_link.equals(null))
                {
                    Toast.makeText(firRegistration.this, "Enter all the required information",Toast.LENGTH_SHORT).show();
                }

                else
                {
                    mProgress = new ProgressDialog(firRegistration.this);
                    mProgress.setTitle("Reporting Your Case");
                    mProgress.setMessage("Please wait while we are reporting your case");
                    mProgress.show();

                    if (i==0){
                        final String key = CaseDatabase.push().getKey();

                        CaseDatabase.child(key).updateChildren(case_data)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            mProgress.dismiss();
                                            Toast.makeText(firRegistration.this,"Case Reported Successfully",Toast.LENGTH_SHORT).show();
                                            // new GlideToast.makeToast(firRegistration.this,"Case Successfully Reported",GlideToast.LENGTHLONG,GlideToast.SUCCESSTOAST,GlideToast.BOTTOM).show();
                                            Intent intent = new Intent(firRegistration.this, MainActivity.class);
                                            startActivity(intent);
                                            //Intent mainIntent = new Intent(firRegistration.this,MainActivity.class);
                                            //mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            //startActivity(mainIntent);
                                            //Toast.makeText(MainActivity.this,"Case Successfully Reported",Toast.LENGTH_SHORT).show();

                                        }
                                        else
                                        {
                                            mProgress.dismiss();
                                            new GlideToast.makeToast(firRegistration.this,"Network Error",GlideToast.LENGTHLONG,GlideToast.FAILTOAST,GlideToast.BOTTOM).show();
                                            //Toast.makeText(MainActivity.this,"Network error",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                    else {
                        final String key = CaseDatabase.push().getKey();
                        //case_data.put("type",user_type);
                       // case_data.put("station",user_station);
                        //case_data.put("id",mUserId);
                        switch (user_type){
                            case "D":
                                case_data.put("access",Div);
                                break;
                            case "R":
                                case_data.put("access",Div+" "+Res);
                                break;
                            default:
                                case_data.put("access",Div+" "+Res+" "+Bet);
                                break;
                        }
                        CaseDatabase.child(key).updateChildren(case_data)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            mProgress.dismiss();
                                            Toast.makeText(firRegistration.this,"Case Reported Successfully",Toast.LENGTH_SHORT).show();
                                            // new GlideToast.makeToast(firRegistration.this,"Case Successfully Reported",GlideToast.LENGTHLONG,GlideToast.SUCCESSTOAST,GlideToast.BOTTOM).show();
                                            Intent intent = new Intent(firRegistration.this, MainActivity.class);
                                            startActivity(intent);
                                            //Intent mainIntent = new Intent(firRegistration.this,MainActivity.class);
                                            //mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            //startActivity(mainIntent);
                                            //Toast.makeText(MainActivity.this,"Case Successfully Reported",Toast.LENGTH_SHORT).show();

                                        }
                                        else
                                        {
                                            mProgress.dismiss();
                                            new GlideToast.makeToast(firRegistration.this,"Network Error",GlideToast.LENGTHLONG,GlideToast.FAILTOAST,GlideToast.BOTTOM).show();
                                            //Toast.makeText(MainActivity.this,"Network error",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }
            }
        });







    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        input_relation.setText(sdf.format(myCalendar.getTime()));
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

                mProgressDialog = new ProgressDialog(firRegistration.this);
                mProgressDialog.setTitle("Uploading Image..");
                mProgressDialog.setMessage("Please wait while we upload and process the image");
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.show();

                Uri resultUri = result.getUri();

                //  final String current_user_id = mCurrentUser.getUid();
                final Date currentTime = Calendar.getInstance().getTime();

                StorageReference filepath = mImageStorage.child("attachments").child(input_mobile.getText().toString()+"_"+ currentTime+ ".jpg");

                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if(task.isSuccessful()){
                            //Toast.makeText(SettingsActivity.this, "Working", Toast.LENGTH_SHORT).show();

                            //final String download_url= task.getResult().toString();

                            /////////////////

                            mImageStorage.child("attachments").child(input_mobile.getText().toString()+"_"+currentTime+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    if(choice == 1)
                                    {
                                        mProgressDialog.dismiss();
                                        link1 = uri.toString();
                                        Picasso.with(firRegistration.this).load(link1).into(attachment1);
                                    }
                                    if(choice == 2)
                                    {
                                        mProgressDialog.dismiss();
                                        link2 = uri.toString();
                                        Picasso.with(firRegistration.this).load(link2).into(attachment2);
                                    }
                                    if(choice == 3)
                                    {
                                        mProgressDialog.dismiss();
                                        link3 = uri.toString();
                                        Picasso.with(firRegistration.this).load(link3).into(attachment3);
                                    }


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                }
                            });
                            //////////////////



                        }else{

                            Toast.makeText(firRegistration.this, "Error in uploading", Toast.LENGTH_SHORT).show();
                            mProgressDialog.dismiss();
                        }
                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }



}
