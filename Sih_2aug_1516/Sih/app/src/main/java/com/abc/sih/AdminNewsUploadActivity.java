package com.abc.sih;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jeevandeshmukh.glidetoastlib.GlideToast;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class AdminNewsUploadActivity extends AppCompatActivity {

    ImageView upd_image;
    private int choice = 1;
    private static final int GALLERY_PICK = 1;

    private ProgressDialog mProgress;
    private ProgressDialog mProgressDialog;
    private StorageReference mImageStorage;
    private String link1=null;
    EditText post_type,post_location,post_title,post_content;
    Button upd_news;
    DatabaseReference newsDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_news_upload);

        upd_image = (ImageView) findViewById(R.id.upd_image);

        mImageStorage = FirebaseStorage.getInstance().getReference();
        post_type = (EditText) findViewById(R.id.post_type);
        post_location = (EditText) findViewById(R.id.post_location);
        post_title = (EditText) findViewById(R.id.post_title);
        post_content = (EditText) findViewById(R.id.post_content);

        upd_news = (Button) findViewById(R.id.upd_news);
        newsDatabase = FirebaseDatabase.getInstance().getReference().child("news");


        upd_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String input1 = post_type.getText().toString();
                String input2 = post_location.getText().toString();
                String input3 = post_title.getText().toString();
                String input4 = post_content.getText().toString();

                HashMap<String, Object> news_data = new HashMap<>();
                news_data.put("type",input1);
                news_data.put("location",input2);
                news_data.put("title",input3);
                news_data.put("content",input4);
                news_data.put("post",link1);

                if(input1.equals("") || input2.equals("") || input3.equals("") || input4.equals("") || link1==null)
                {
                    Toast.makeText(AdminNewsUploadActivity.this, "Enter All required information", Toast.LENGTH_SHORT).show();
                }
                else{
                    mProgress = new ProgressDialog(AdminNewsUploadActivity.this);
                    mProgress.setTitle("Reporting Your Case");
                    mProgress.setMessage("Please wait while we are reporting your case");
                    mProgress.show();

                    final String key = newsDatabase.push().getKey();

                    newsDatabase.child(key).updateChildren(news_data)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){
                                        mProgress.dismiss();
                                        Toast.makeText(AdminNewsUploadActivity.this,"Case Reported Successfully",Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(AdminNewsUploadActivity.this, AdminMainActivity.class);
                                        startActivity(intent);
                                    }else{
                                        mProgress.dismiss();
                                        new GlideToast.makeToast(AdminNewsUploadActivity.this,"Network Error",GlideToast.LENGTHLONG,GlideToast.FAILTOAST,GlideToast.BOTTOM).show();
                                    }

                                }
                            });
                }


            }
        });



        upd_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // choice = 1;
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PICK);
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

                mProgressDialog = new ProgressDialog(AdminNewsUploadActivity.this);
                mProgressDialog.setTitle("Uploading Image..");
                mProgressDialog.setMessage("Please wait while we upload and process the image");
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.show();

                Uri resultUri = result.getUri();

                //  final String current_user_id = mCurrentUser.getUid();
                final Date currentTime = Calendar.getInstance().getTime();

                StorageReference filepath = mImageStorage.child("news-upd").child(currentTime+ ".jpg");

                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if(task.isSuccessful()){


                            mImageStorage.child("news-upd").child(currentTime+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {


                                        mProgressDialog.dismiss();
                                        link1 = uri.toString();
                                        Picasso.with(AdminNewsUploadActivity.this).load(link1).into(upd_image);


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                }
                            });
                            //////////////////



                        }else{

                            Toast.makeText(AdminNewsUploadActivity.this, "Error in uploading", Toast.LENGTH_SHORT).show();
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