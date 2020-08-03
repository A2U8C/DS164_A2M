package com.abc.sih;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.abc.sih.Helper.LocaleHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import io.paperdb.Paper;

public class Zonecreate extends AppCompatActivity {
    private static final int GALLERY_PICK = 1;
    private EditText edt,edt2;
    private Button btn;
    private String selected_item;
    private double lat,lon;
    private ImageView img;

    TextView txt1,txt2;
    Button btn1;

    Spinner dropdown;
    String[] items;
    private String link;
    private StorageReference mImageStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zonecreate);
        mImageStorage = FirebaseStorage.getInstance().getReference();
        edt = findViewById(R.id.radius);
        btn = (Button)findViewById(R.id.submit);
        edt2 = findViewById(R.id.title);
        img = findViewById(R.id.input_attach1);
        txt1 = (TextView) findViewById(R.id.txt1);
        txt2 = (TextView) findViewById(R.id.txt2);
        btn1 = (Button) findViewById(R.id.submit);
        ///////////
        Paper.init(Zonecreate.this);

        String language = Paper.book().read("language");
        if(language == null)
            Paper.book().write("language", "en");

        updateView((String)Paper.book().read("language"));
        //////////////
        if(language.equals("en"))
        {
            dropdown = findViewById(R.id.spinner1);
            items = new String[]{"Red", "Yellow","Blue"};
        }
        else if(language.equals("hi"))
        {
            dropdown = findViewById(R.id.spinner1);
            items = new String[]{"लाल", "पीला","नीला"};
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_item = adapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        lat = getIntent().getExtras().getDouble("lat");
        lon = getIntent().getExtras().getDouble("lon");

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String radius = edt.getText().toString();
                String title = edt2.getText().toString();
                double rad = Double.parseDouble(radius);
                if (TextUtils.isEmpty(radius) || TextUtils.isEmpty(selected_item) ||TextUtils.isEmpty(title)){
                    Toast.makeText(Zonecreate.this,"Please Enter all details",Toast.LENGTH_SHORT).show();
                }else {
                    Submit(rad,selected_item,title);
                }

            }
        });
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PICK);

            }
        });
    }

    private void Submit(double rad, String selected_item, String title) {
        DatabaseReference zone = FirebaseDatabase.getInstance().getReference().child("Zones");
        if (selected_item.equals("Red")){
            zone = zone.child("red");
        }else if (selected_item.equals("Yellow")){
            zone = zone.child("yellow");
        }else {
            zone = zone.child("blue");
        }
        Map cord = new HashMap<>();
        cord.put("rad",rad);
        cord.put("lon",lon);
        cord.put("lat",lat);
        cord.put("title",title);
        cord.put("link",link);
        zone.push().updateChildren(cord).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                Toast.makeText(Zonecreate.this,"Success",Toast.LENGTH_SHORT).show();
                onBackPressed();

            }
        });

    }

    private void updateView(String lang) {

        Context context = LocaleHelper.setLocale(Zonecreate.this, lang);
        Resources resources = context.getResources();

        //text1.setText(resources.getString(R.string.launch_complain));
        txt1.setText(resources.getString(R.string.zone_create));
        txt2.setText(resources.getString(R.string.zone_radius));

        btn1.setText(resources.getString(R.string.create_zone));




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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



                Uri resultUri = result.getUri();

                //  final String current_user_id = mCurrentUser.getUid();
                final Date currentTime = Calendar.getInstance().getTime();

                StorageReference filepath = mImageStorage.child("zones").child(currentTime+ ".jpg");

                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        mImageStorage.child("zones").child(currentTime+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                link = uri.toString();
                                Picasso.with(Zonecreate.this).load(link).into(img);


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                            }
                        });
                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
