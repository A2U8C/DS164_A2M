package com.abc.sih;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class SpeechActivity extends AppCompatActivity {


    private EditText input_aadhar,input_address,input_email;
    private EditText  input_relation;
    private DatabaseReference CaseDatabase;
    private ImageView attachment1,attachment2,attachment3;
    private CheckBox terms;
    private ProgressDialog mProgress;
    private ProgressDialog mProgressDialog;
    private Button savebutton;
    private Button btn1,btn2,btn3,btn4;
    private TextView trial,input_mobile, speech;
    private String link1=null,link2=null,link3=null,mCurrent_user_id,email;
    private static final int GALLERY_PICK = 1;
    private int choice = 1;
    private StorageReference mImageStorage;
    private TextView esign;
    private String esign_link = null,name,usermail;
    private TextView trying;
    private Calendar myCalendar;

    private ImageView greentick;

    private TextView trying2,input_type,input_details;
    DatabaseReference mUserDatabase;
    private FirebaseAuth mAuth;
    private String user_name,user_dp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech);
        email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        int index = email.indexOf('@');
        email = email.substring(0,index);


        input_type = (TextView) findViewById(R.id.input_type);
        input_type.setHint(Html.fromHtml(getString(R.string.hint2)));

        input_aadhar = (EditText) findViewById(R.id.input_aadhar);
        input_aadhar.setHint(Html.fromHtml(getString(R.string.hint_aadhar)));

        input_address = (EditText) findViewById(R.id.input_address);
        input_address.setHint(Html.fromHtml(getString(R.string.hint_address)));

        input_mobile = (TextView) findViewById(R.id.input_mobile);
        input_mobile.setText(email);

        input_email = (EditText) findViewById(R.id.input_email);
        input_email.setHint(Html.fromHtml(getString(R.string.hint_email)));


        input_details = (TextView) findViewById(R.id.input_details);
        input_details.setHint(Html.fromHtml(getString(R.string.hint_details)));

        input_relation = (EditText) findViewById(R.id.input_relation);
        input_relation.setHint(Html.fromHtml(getString(R.string.hint_relation)));

        CaseDatabase = FirebaseDatabase.getInstance().getReference().child("Posts");
        terms = (CheckBox) findViewById(R.id.terms);
        savebutton = (Button) findViewById(R.id.submitbtn);
        attachment1 = (ImageView) findViewById(R.id.input_attach1);
        attachment2 = (ImageView) findViewById(R.id.input_attach2);
        attachment3 = (ImageView) findViewById(R.id.input_attach3);
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.type);
        btn4 = (Button) findViewById(R.id.details);
        esign = (TextView) findViewById(R.id.esign);
        trying = (TextView) findViewById(R.id.text1);
        mImageStorage = FirebaseStorage.getInstance().getReference();
        esign_link = getIntent().getStringExtra("sign_link");
        greentick = (ImageView) findViewById(R.id.green_tick);
        trying2 = (TextView) findViewById(R.id.text2);


//////////////////////
        mAuth = FirebaseAuth.getInstance();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists() && dataSnapshot.child("name").getValue()!=null && dataSnapshot.child("image").getValue()!=null){
                    user_name = dataSnapshot.child("name").getValue().toString();
                    user_dp = dataSnapshot.child("image").getValue().toString();
                    name = dataSnapshot.child("name").getValue().toString();
                    usermail = dataSnapshot.child("email").getValue().toString();
                    input_aadhar.setText(name);
                    input_email.setText(usermail);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        ///////////
        if(esign_link!=null)
        {
            greentick.setVisibility(View.VISIBLE);
        }

        esign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SpeechActivity.this, SignatureActivity.class);
                intent.putExtra("activity","SPC");
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
                input_address.setHint(Html.fromHtml(getString(R.string.hint_country)));
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
                input_address.setHint(Html.fromHtml(getString(R.string.hint_address)));
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

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speech = findViewById(R.id.input_type);
                startVoiceRecognitionActivity();
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speech = findViewById(R.id.input_details);
                startVoiceRecognitionActivity();
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
                new DatePickerDialog(SpeechActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });




////////////////////////////////////////////////////////////////////////////////////////////////////////
        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String input1 = input_type.getText().toString();
                String input2 = input_aadhar.getText().toString();
                String input3 = input_address.getText().toString();
                //String input4= input_mobile.getText().toString();
                String input5 = input_email.getText().toString();

                String input6 = input_details.getText().toString();
                String input7 = input_relation.getText().toString();

                HashMap<String, Object> case_data = new HashMap<>();
                case_data.put("type",input1);
                case_data.put("uploader_name",input2);
                case_data.put("circle_code",input3);
                case_data.put("mobile",email);
                case_data.put("email",input5);
                case_data.put("case_details",input6);
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
                // case_data.put("attachments",)

                if(input1.equals("") || input2.equals("") || input3.equals("")
                        ||input5.equals("") ||input6.equals("") || input7.equals("") || !terms.isChecked() || esign_link.equals(null))
                {
                    Toast.makeText(SpeechActivity.this, "Enter all the required information",Toast.LENGTH_SHORT).show();
                }

                else
                {
                    mProgress = new ProgressDialog(SpeechActivity.this);
                    mProgress.setTitle("Reporting Your Case");
                    mProgress.setMessage("Please wait while we are reporting your case");
                    mProgress.show();

                    final String key = CaseDatabase.push().getKey();

                    CaseDatabase.child(key).updateChildren(case_data)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        mProgress.dismiss();
                                        Toast.makeText(SpeechActivity.this,"Case Reported Successfully",Toast.LENGTH_SHORT).show();
                                        // new GlideToast.makeToast(firRegistration.this,"Case Successfully Reported",GlideToast.LENGTHLONG,GlideToast.SUCCESSTOAST,GlideToast.BOTTOM).show();
                                        Intent intent = new Intent(SpeechActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        //Intent mainIntent = new Intent(firRegistration.this,MainActivity.class);
                                        //mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        //startActivity(mainIntent);
                                        //Toast.makeText(MainActivity.this,"Case Successfully Reported",Toast.LENGTH_SHORT).show();

                                    }
                                    else
                                    {
                                        mProgress.dismiss();
                                        new GlideToast.makeToast(SpeechActivity.this,"Network Error",GlideToast.LENGTHLONG,GlideToast.FAILTOAST,GlideToast.BOTTOM).show();
                                        //Toast.makeText(MainActivity.this,"Network error",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });



    }
    private void startVoiceRecognitionActivity() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(getPackageManager())!=null){
            startActivityForResult(intent, 10);
        }else {
            Toast.makeText(this,"Your Device doesn't support speech input",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 10:
                if (resultCode == RESULT_OK && data!=null){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    speech.setText(result.get(0));

                }
                break;

        }
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

                mProgressDialog = new ProgressDialog(SpeechActivity.this);
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
                                        Picasso.with(SpeechActivity.this).load(link1).into(attachment1);
                                    }
                                    if(choice == 2)
                                    {
                                        mProgressDialog.dismiss();
                                        link2 = uri.toString();
                                        Picasso.with(SpeechActivity.this).load(link2).into(attachment2);
                                    }
                                    if(choice == 3)
                                    {
                                        mProgressDialog.dismiss();
                                        link3 = uri.toString();
                                        Picasso.with(SpeechActivity.this).load(link3).into(attachment3);
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

                            Toast.makeText(SpeechActivity.this, "Error in uploading", Toast.LENGTH_SHORT).show();
                            mProgressDialog.dismiss();
                        }
                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        input_relation.setText(sdf.format(myCalendar.getTime()));
    }

}