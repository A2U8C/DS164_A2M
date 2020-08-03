package com.abc.sih;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class CheckPointAttachActivity extends AppCompatActivity {

    private String link1 = null, link2 = null, link3 = null, mCurrent_user_id, email;
    private static final int GALLERY_PICK = 1;
    private ProgressDialog mProgress;
    private ProgressDialog mProgressDialog;
    private StorageReference mImageStorage;
    private ImageView attachment1, attachment2, attachment3;
    private int choice = 1;
    EditText title;
    Button btnSave;

    DatabaseReference user_data;
    String childval;
    DatabaseReference qrdatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_point_attach);

        childval = getIntent().getStringExtra("childval");

        mImageStorage = FirebaseStorage.getInstance().getReference();
        attachment1 = (ImageView) findViewById(R.id.input_attach1);
        attachment2 = (ImageView) findViewById(R.id.input_attach2);
        attachment3 = (ImageView) findViewById(R.id.input_attach3);
        title = (EditText) findViewById(R.id.edt_about);
        btnSave = (Button) findViewById(R.id.btn_save);
        ///
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        qrdatabase = FirebaseDatabase.getInstance().getReference().child("Beats-Record").child(childval).child(date);
        user_data = FirebaseDatabase.getInstance().getReference().child("Checkpoint").child(user_id).child(date);
        user_data.keepSynced(true);

        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.e("anish_tt",userid);
        ///

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

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mTitle=title.getText().toString().trim();


                if(TextUtils.isEmpty(mTitle))
                {
                    title.setError("Required Field..");
                    return;
                }
                user_data.child(childval).setValue(mTitle);
                user_data.child(childval+"time").setValue(ServerValue.TIMESTAMP);
                user_data.child(childval+"link1").setValue(link1);
                user_data.child(childval+"link2").setValue(link2);
                user_data.child(childval+"link3").setValue(link3);

                qrdatabase.child(user_id).child("id").setValue(user_id);
                qrdatabase.child(user_id).child("time").setValue(ServerValue.TIMESTAMP);
                qrdatabase.child(user_id).child("link1").setValue(link1);
                qrdatabase.child(user_id).child("link2").setValue(link2);
                qrdatabase.child(user_id).child("link3").setValue(link3);

                //notification//
                String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                FirebaseDatabase.getInstance().getReference().child("Attendance").child(date).child(userid).setValue(userid);

                final String key = FirebaseDatabase.getInstance().getReference().child("Notification").push().getKey();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy ");
                String formattedDate = dateFormat.format(new Date()).toString();

                SimpleDateFormat dateFormatt = new SimpleDateFormat("hh.mm.ss aa");
                String formattedDatet = dateFormatt.format(new Date()).toString();
                ////updating notification

                final HashMap<String, Object> userdataMap = new HashMap<>();
                userdataMap.put("date",date);
                userdataMap.put("desc","Your Checkpoint " + childval+ "is successfully marked for the day"+ date);
                userdataMap.put("type","Checkpoint Admin");
                userdataMap.put("time",formattedDatet);

                FirebaseDatabase.getInstance().getReference().child("Notification")
                        .child(userid).child(key).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Intent intent = new Intent(CheckPointAttachActivity.this,CheckpointActivity.class);
                        startActivity(intent);
                    }
                });

                //

                Toast.makeText(CheckPointAttachActivity.this, "Data Inserted", Toast.LENGTH_SHORT).show();
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

                mProgressDialog = new ProgressDialog(CheckPointAttachActivity.this);
                mProgressDialog.setTitle("Uploading Image..");
                mProgressDialog.setMessage("Please wait while we upload and process the image");
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.show();

                Uri resultUri = result.getUri();

                //  final String current_user_id = mCurrentUser.getUid();
                final Date currentTime = Calendar.getInstance().getTime();

                StorageReference filepath = mImageStorage.child("attachments-qr").child("_"+ currentTime+ ".jpg");

                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if(task.isSuccessful()){


                            mImageStorage.child("attachments-qr").child("_"+currentTime+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    if(choice == 1)
                                    {
                                        mProgressDialog.dismiss();
                                        link1 = uri.toString();
                                        Picasso.with(CheckPointAttachActivity.this).load(link1).into(attachment1);
                                    }
                                    if(choice == 2)
                                    {
                                        mProgressDialog.dismiss();
                                        link2 = uri.toString();
                                        Picasso.with(CheckPointAttachActivity.this).load(link2).into(attachment2);
                                    }
                                    if(choice == 3)
                                    {
                                        mProgressDialog.dismiss();
                                        link3 = uri.toString();
                                        Picasso.with(CheckPointAttachActivity.this).load(link3).into(attachment3);
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

                            Toast.makeText(CheckPointAttachActivity.this, "Error in uploading", Toast.LENGTH_SHORT).show();
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