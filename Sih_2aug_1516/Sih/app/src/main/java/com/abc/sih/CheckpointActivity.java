package com.abc.sih;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.renderscript.Script;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.jeevandeshmukh.glidetoastlib.GlideToast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class CheckpointActivity extends AppCompatActivity {

    Button btn1,btn2,btn3,btn4;

    TextView notes1,notes2,notes3,notes4;

    DatabaseReference user_data;
    String childval;
    DatabaseReference qrdatabase;

    ImageView img1,img2,img3,img4;
    TextView info1,info2,info3,info4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkpoint);

        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn4 = (Button) findViewById(R.id.btn4);

        notes1 = (TextView) findViewById(R.id.notes1);
        notes2 = (TextView) findViewById(R.id.notes2);
        notes3 = (TextView) findViewById(R.id.notes3);
        notes4 = (TextView) findViewById(R.id.notes4);

        img1 = (ImageView) findViewById(R.id.tick1);
        img2 = (ImageView) findViewById(R.id.tick2);
        img3 = (ImageView) findViewById(R.id.tick3);
        img4 = (ImageView) findViewById(R.id.tick4);

        info1 = (TextView) findViewById(R.id.info1);
        info2 = (TextView) findViewById(R.id.info2);
        info3 = (TextView) findViewById(R.id.info3);
        info4 = (TextView) findViewById(R.id.info4);




        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        user_data = FirebaseDatabase.getInstance().getReference().child("Checkpoint").child(user_id).child(date);
        user_data.keepSynced(true);

       info1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckpointActivity.this,ViewCPNotesActivity.class);
                intent.putExtra("user_id",user_id);
                intent.putExtra("date",date);
                intent.putExtra("childval","C1");
                startActivity(intent);
            }
        });
        info2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckpointActivity.this,ViewCPNotesActivity.class);
                intent.putExtra("user_id",user_id);
                intent.putExtra("date",date);
                intent.putExtra("childval","C2");
                startActivity(intent);
            }
        });
        info3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckpointActivity.this,ViewCPNotesActivity.class);
                intent.putExtra("user_id",user_id);
                intent.putExtra("date",date);
                intent.putExtra("childval","C3");
                startActivity(intent);
            }
        });
        info4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckpointActivity.this,ViewCPNotesActivity.class);
                intent.putExtra("user_id",user_id);
                intent.putExtra("date",date);
                intent.putExtra("childval","C4");
                startActivity(intent);
            }
        });


        user_data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.child("C1").getValue()==null)
                {
                btn2.setVisibility(View.INVISIBLE);
                btn3.setVisibility(View.INVISIBLE);
                btn4.setVisibility(View.INVISIBLE);
                }

                if(dataSnapshot.child("C2").getValue()==null)
                {
                    btn3.setVisibility(View.INVISIBLE);
                    btn4.setVisibility(View.INVISIBLE);
                }

                if(dataSnapshot.child("C3").getValue()==null)
                {

                    btn4.setVisibility(View.INVISIBLE);
                }

                if(dataSnapshot.child("C1").getValue()!=null && dataSnapshot.child("C1time").getValue()!=null)
                {
                    String abc = dataSnapshot.child("C1").getValue().toString();
                    notes1.setText("Recorded");
                    info1.setVisibility(View.VISIBLE);
                    btn1.setVisibility(View.INVISIBLE);
                    img1.setVisibility(View.VISIBLE);
                }

                if(dataSnapshot.child("C2").getValue()!=null && dataSnapshot.child("C2time").getValue()!=null)
                {
                    String abc = dataSnapshot.child("C2").getValue().toString();
                    notes2.setText("Recorded");
                    info2.setVisibility(View.VISIBLE);
                    btn2.setVisibility(View.INVISIBLE);
                    img1.setVisibility(View.VISIBLE);
                    img2.setVisibility(View.VISIBLE);
                }

                if(dataSnapshot.child("C3").getValue()!=null && dataSnapshot.child("C3time").getValue()!=null)
                {
                    String abc = dataSnapshot.child("C3").getValue().toString();
                    notes3.setText("Recorded");
                    info3.setVisibility(View.VISIBLE);
                    btn3.setVisibility(View.INVISIBLE);
                    img1.setVisibility(View.VISIBLE);
                    img2.setVisibility(View.VISIBLE);
                    img3.setVisibility(View.VISIBLE);
                }

                if(dataSnapshot.child("C4").getValue()!=null && dataSnapshot.child("C4time").getValue()!=null)
                {
                    String abc = dataSnapshot.child("C4").getValue().toString();
                    notes4.setText("Recorded");
                    info4.setVisibility(View.VISIBLE);
                    btn4.setVisibility(View.INVISIBLE);
                    img1.setVisibility(View.VISIBLE);
                    img2.setVisibility(View.VISIBLE);
                    img3.setVisibility(View.VISIBLE);
                    img4.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                childval = "C1";
                qrdatabase = FirebaseDatabase.getInstance().getReference().child("Beats-Record").child(childval).child(date);
                IntentIntegrator intentIntegrator = new IntentIntegrator(CheckpointActivity.this);
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                intentIntegrator.setCameraId(0);
                intentIntegrator.setOrientationLocked(false);
                intentIntegrator.setPrompt("Scanning");
                intentIntegrator.setBeepEnabled(true);
                intentIntegrator.setBarcodeImageEnabled(true);
                intentIntegrator.initiateScan();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                childval = "C2";
                qrdatabase = FirebaseDatabase.getInstance().getReference().child("Beats-Record").child(childval).child(date);
                IntentIntegrator intentIntegrator = new IntentIntegrator(CheckpointActivity.this);
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                intentIntegrator.setCameraId(0);
                intentIntegrator.setOrientationLocked(false);
                intentIntegrator.setPrompt("Scanning");
                intentIntegrator.setBeepEnabled(true);
                intentIntegrator.setBarcodeImageEnabled(true);
                intentIntegrator.initiateScan();
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                childval = "C3";
                qrdatabase = FirebaseDatabase.getInstance().getReference().child("Beats-Record").child(childval).child(date);
                IntentIntegrator intentIntegrator = new IntentIntegrator(CheckpointActivity.this);
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                intentIntegrator.setCameraId(0);
                intentIntegrator.setOrientationLocked(false);
                intentIntegrator.setPrompt("Scanning");
                intentIntegrator.setBeepEnabled(true);
                intentIntegrator.setBarcodeImageEnabled(true);
                intentIntegrator.initiateScan();
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                childval = "C4";
                qrdatabase = FirebaseDatabase.getInstance().getReference().child("Beats-Record").child(childval).child(date);
                IntentIntegrator intentIntegrator = new IntentIntegrator(CheckpointActivity.this);
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                intentIntegrator.setCameraId(0);
                intentIntegrator.setOrientationLocked(false);
                intentIntegrator.setPrompt("Scanning");
                intentIntegrator.setBeepEnabled(true);
                intentIntegrator.setBarcodeImageEnabled(true);
                intentIntegrator.initiateScan();
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        final IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);

        if(result.getContents().equals(childval))
        {
            Intent intent = new Intent(CheckpointActivity.this,CheckPointAttachActivity.class);
            intent.putExtra("childval",childval);
            //intent.putExtra("user_id",userid);
            startActivity(intent);

           /* String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
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

                }
            });*/


            //idr naya attach krna hai

           /* AlertDialog.Builder myDialog= new AlertDialog.Builder(CheckpointActivity.this);

            LayoutInflater inflater= LayoutInflater.from(CheckpointActivity.this);

            final View myview= inflater.inflate(R.layout.checkpoint_data,null);

            myDialog.setView(myview);

            final AlertDialog dialog=myDialog.create();
            final EditText title= myview.findViewById(R.id.edt_about);



            Button btnSave= myview.findViewById(R.id.btn_save);

            btnSave.setOnClickListener(new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.KITKAT)
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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

                    qrdatabase.child(userid).child("id").setValue(userid);
                    qrdatabase.child(userid).child("time").setValue(ServerValue.TIMESTAMP);

                    Toast.makeText(CheckpointActivity.this, "Data Inserted", Toast.LENGTH_SHORT).show();

                    dialog.dismiss();

                    ///
                    new GlideToast.makeToast(CheckpointActivity.this,"CP Recorded", GlideToast.LENGTHLONG,GlideToast.SUCCESSTOAST).show();

                }
            });*/

            //dialog.show();



        }
        else{
            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseDatabase.getInstance().getReference().child("Attendance").child(date).child(userid).setValue("Present");
            final String key = FirebaseDatabase.getInstance().getReference().child("Notification").push().getKey();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy ");
            String formattedDate = dateFormat.format(new Date()).toString();

            SimpleDateFormat dateFormatt = new SimpleDateFormat("hh.mm.ss aa");
            String formattedDatet = dateFormatt.format(new Date()).toString();
            ////updating notification

            final HashMap<String, Object> userdataMap = new HashMap<>();
            userdataMap.put("date",date);
            userdataMap.put("desc","Some Misbehaviour is observed with you account. If it was not you report it to your head office " );
            userdataMap.put("type","Checkpoint Admin");
            userdataMap.put("time",formattedDatet);

            FirebaseDatabase.getInstance().getReference().child("Notification")
                    .child(userid).child(key).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    new GlideToast.makeToast(CheckpointActivity.this,"Wrong Office Code", GlideToast.LENGTHLONG,GlideToast.FAILTOAST).show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            Intent intent = new Intent(CheckpointActivity.this,MainActivity.class);
                            startActivity(intent);
                        }
                    }, 2000); //5seconds
                }
            });


        }



        super.onActivityResult(requestCode, resultCode, data);
    }

}