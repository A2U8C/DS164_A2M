package com.abc.sih;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Enterdetails extends AppCompatActivity {
    private EditText station,status,res1,res2,res3;
    private ImageView img;
    private String type_usr,stat,status_usr,resource1,resource2,resource3,link,mcode;
    private Button next;
    private DatabaseReference andam;
    private Spinner spin1,spin2,spin3,type;
    private DatabaseReference andm,hry;
    private List<String> item1,item2,item3,item4;
    private static final int GALLERY_PICK = 1;
    private StorageReference mImageStorage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enterdetails);
        type = findViewById(R.id.type);

        station = findViewById(R.id.station);
        status = findViewById(R.id.status);
        spin1 = findViewById(R.id.spinner1);
        spin2 = findViewById(R.id.spinner2);
        spin3 = findViewById(R.id.spinner3);
        img = findViewById(R.id.prof);
        item4 = new ArrayList<String>();
        item4.add("Divisional Officer");
        item4.add("Range Officer");
        item4.add("Beat Officer");
        item4.add("Forest Guard");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Enterdetails.this, android.R.layout.simple_spinner_item, item4);
        type.setAdapter(adapter);
        andm = FirebaseDatabase.getInstance().getReference().child("Andaman-trial");
        hry = FirebaseDatabase.getInstance().getReference().child("Andaman-trial");

        mImageStorage = FirebaseStorage.getInstance().getReference();

        andm.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                item1 = new ArrayList<String>();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    item1.add(ds.getKey());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Enterdetails.this, android.R.layout.simple_spinner_item, item1);
                spin1.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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

        spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String div = spin1.getSelectedItem().toString();
                resource1 = div;
                hry = hry.child(div);
                setuprange(div);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                resource2 = spin2.getSelectedItem().toString();
                int k = position+1;
                hry = hry.child("range"+k);
                setupBeat(position+1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        spin3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                resource3 = spin3.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        next = findViewById(R.id.next);
        andam = FirebaseDatabase.getInstance().getReference().child("Andaman-trial");
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type_usr = type.getSelectedItem().toString();
                stat = station.getText().toString();
                status_usr = status.getText().toString();

                if (TextUtils.isEmpty(type_usr)||TextUtils.isEmpty(stat)
                        ||TextUtils.isEmpty(status_usr)||TextUtils.isEmpty(resource1)||TextUtils.isEmpty(resource2)
                        ||TextUtils.isEmpty(resource3) ||TextUtils.isEmpty(link)){
                    Toast.makeText(Enterdetails.this,"Please Enter all details",Toast.LENGTH_SHORT).show();

                }else {
                    /*if(type_usr.equals("type1")){
                        submitrfo();

                    }else{
                        submit();
                    }*/
                    enterdetails();
                }
            }
        });
    }
    private void setupBeat(long i) {
        andm.child(resource1).child("range"+i).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                item3 = new ArrayList<String>();
                item3.add("N.A");
                String num = dataSnapshot.child("num_of_beats").getValue().toString();
                long ran = Long.parseLong(num);
                for (int i=1; i<=ran;i++){
                    item3.add("beat"+i);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Enterdetails.this, android.R.layout.simple_spinner_dropdown_item, item3);
                spin3.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setuprange(String div) {
        andm.child(div).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                item2 = new ArrayList<String>();
                item2.add("N.A");
                String num = dataSnapshot.child("num_of_range").getValue().toString();
                long ran = Long.parseLong(num);
                for (int i=1; i<=ran;i++){
                    Log.d("hhhh",div);
                    String a = dataSnapshot.child("range"+i).child("name").getValue().toString();
                    item2.add(a);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Enterdetails.this, android.R.layout.simple_spinner_dropdown_item, item2);
                spin2.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void enterdetails() {
        String muid = FirebaseAuth.getInstance().getUid();

        if (type_usr.equals("Divisional Officer")){
            mcode = "D";
            andam.child(resource1).child("division_officer").setValue(muid);
        }else if(type_usr.equals("Range Officer")){
            mcode = "R";
            andam.child(resource1).child(resource2).child("range_officer").setValue(muid);
        }else if (type_usr.equals("Beat Officer")){
            mcode = "B";
            andam.child(resource1).child(resource2).child(resource3+"_officer").setValue(muid);

        }else {
            mcode = "F";
            setheirachy();
        }

        submit();
    }
    private void setheirachy(){
        String mUid = FirebaseAuth.getInstance().getUid();
        hry.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String id = dataSnapshot.child(resource3+"_officer").getValue().toString();
                FirebaseDatabase.getInstance().getReference().child("hierarchy")
                        .child(resource1+resource2+resource3).child(id).child(mUid).child(mUid).setValue(mUid).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        submit();
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void submit() {
        HashMap<String, Object> userdataMap = new HashMap<>();
        userdataMap.put("about_me","N.A");
        userdataMap.put("type",mcode);
        userdataMap.put("status",status_usr);
        userdataMap.put("station",stat);
        userdataMap.put("resource1","N.A");
        userdataMap.put("resource2","N.A");
        userdataMap.put("resource3","N.A");
        userdataMap.put("Division",resource1);
        userdataMap.put("Range",resource2);
        userdataMap.put("image",link);
        userdataMap.put("Beat",resource3);
        String mUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("Users").child(mUid).updateChildren(userdataMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent pass = new Intent(Enterdetails.this,MainActivity.class);
                        pass.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(pass);                    }
                });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {

            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    // .setAspectRatio(1, 1)
                    .start(Enterdetails.this);

            //Toast.makeText(SettingsActivity.this, imageUri, Toast.LENGTH_SHORT).show();
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {


                Uri resultUri = result.getUri();

                //  final String current_user_id = mCurrentUser.getUid();
                final Date currentTime = Calendar.getInstance().getTime();
                String muid = FirebaseAuth.getInstance().getUid();

                StorageReference filepath = mImageStorage.child("profile_images").child(muid + ".jpg");

                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if (task.isSuccessful()) {
                            //Toast.makeText(SettingsActivity.this, "Working", Toast.LENGTH_SHORT).show();

                            //final String download_url= task.getResult().toString();

                            /////////////////

                            mImageStorage.child("profile_images").child(muid + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    link = uri.toString();
                                    Log.d("dsdsd", link);
                                    Picasso.with(Enterdetails.this).load(link).into(img);


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                }
                            });
                            //////////////////


                        } else {

                            Toast.makeText(Enterdetails.this, "Error in uploading", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}