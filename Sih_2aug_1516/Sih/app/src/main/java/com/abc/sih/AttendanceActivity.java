package com.abc.sih;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.jeevandeshmukh.glidetoastlib.GlideToast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import cdflynn.android.library.checkview.CheckView;

public class AttendanceActivity extends AppCompatActivity {
private String link;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        link = getIntent().getStringExtra("link");
        IntentIntegrator intentIntegrator = new IntentIntegrator(AttendanceActivity.this);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        intentIntegrator.setCameraId(0);
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.setPrompt("Scanning");
        intentIntegrator.setBeepEnabled(true);
        intentIntegrator.setBarcodeImageEnabled(true);
        intentIntegrator.initiateScan();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        final IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);

        /*if(result!=null && result.getContents()!=null)
        {
            new AlertDialog.Builder(AttendanceActivity.this)
                    .setTitle("Scan Result")
                    .setMessage(result.getContents())
                    .setPositiveButton("Copy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            ClipboardManager manager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                            ClipData data = ClipData.newPlainText("result",result.getContents());
                            manager.setPrimaryClip(data);
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                }
            }) .create().show();
        }*/
        if(result.getContents().equals("markmyattendance"))
        {
            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseDatabase.getInstance().getReference().child("Attendance").child(date).child(userid).child("status").setValue(userid);
            final String key = FirebaseDatabase.getInstance().getReference().child("Notification").push().getKey();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy ");
            String formattedDate = dateFormat.format(new Date()).toString();

            SimpleDateFormat dateFormatt = new SimpleDateFormat("hh.mm.ss aa");
            String formattedDatet = dateFormatt.format(new Date()).toString();
            ////updating notification

            final HashMap<String, Object> userdataMap = new HashMap<>();
            userdataMap.put("date",date);
            userdataMap.put("desc","Your Attendance is Successfully marked for the day " + date);
            userdataMap.put("type","Attendance Admin");
            userdataMap.put("time",formattedDatet);

            FirebaseDatabase.getInstance().getReference().child("Notification")
                    .child(userid).child(key).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                }
            });



            ///
            Intent intent = new Intent(AttendanceActivity.this,AttendanceActivityNextStep.class);
            startActivity(intent);
        }
        else{
            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseDatabase.getInstance().getReference().child("Attendance").child(date).child(userid).child("status").setValue(userid);
            FirebaseDatabase.getInstance().getReference().child("Attendance").child(date).child(userid).child("link").setValue(link);

            final String key = FirebaseDatabase.getInstance().getReference().child("Notification").push().getKey();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy ");
            String formattedDate = dateFormat.format(new Date()).toString();

            SimpleDateFormat dateFormatt = new SimpleDateFormat("hh.mm.ss aa");
            String formattedDatet = dateFormatt.format(new Date()).toString();
            ////updating notification

            final HashMap<String, Object> userdataMap = new HashMap<>();
            userdataMap.put("date",date);
            userdataMap.put("desc","Some Misbehaviour is observed with you account. If it was not you report it to your head office " );
            userdataMap.put("type","Attendance Admin");
            userdataMap.put("time",formattedDatet);

            FirebaseDatabase.getInstance().getReference().child("Notification")
                    .child(userid).child(key).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    new GlideToast.makeToast(AttendanceActivity.this,"Wrong Office Code", GlideToast.LENGTHLONG,GlideToast.FAILTOAST).show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            Intent intent = new Intent(AttendanceActivity.this,MainActivity.class);
                            startActivity(intent);
                        }
                    }, 2000); //5seconds
                }
            });


        }



        super.onActivityResult(requestCode, resultCode, data);
    }

}
