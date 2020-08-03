package com.abc.sih;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;

public class VerifyAadhar extends AppCompatActivity {



    String uid,name,gender,yearOfBirth,careOf,villageTehsil,postOffice,district,state,postCode;
    String home,loc,subdist,mCurrentUser,userId;
    // UI Elements
    TextView tv_sd_uid,tv_sd_name,tv_sd_gender,tv_sd_yob,tv_sd_co,tv_sd_vtc,tv_sd_po,tv_sd_dist,
            tv_sd_state,tv_sd_pc,tv_cancel_action,tv_home,tv_loc,tv_subdist;
    LinearLayout ll_scanned_data_wrapper,ll_data_wrapper,ll_action_button_wrapper;
    FirebaseAuth mAuth;
    int i =0;
    // Storage

    private static final int MY_CAMERA_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_aadhar);

        //hide the default action bar
        userId = getIntent().getStringExtra("UID");
        mAuth = FirebaseAuth.getInstance();
        // init the UI Elements
        tv_sd_uid = (TextView)findViewById(R.id.tv_sd_uid);
        tv_sd_name = (TextView)findViewById(R.id.tv_sd_name);
        tv_sd_gender = (TextView)findViewById(R.id.tv_sd_gender);
        tv_sd_yob = (TextView)findViewById(R.id.tv_sd_yob);
        tv_sd_co = (TextView)findViewById(R.id.tv_sd_co);
        tv_sd_vtc = (TextView)findViewById(R.id.tv_sd_vtc);
        tv_sd_po = (TextView)findViewById(R.id.tv_sd_po);
        tv_sd_dist = (TextView)findViewById(R.id.tv_sd_dist);
        tv_sd_state = (TextView)findViewById(R.id.tv_sd_state);
        tv_sd_pc = (TextView)findViewById(R.id.tv_sd_pc);
        tv_home = (TextView) findViewById(R.id.tv_home);
        tv_loc = (TextView) findViewById(R.id.tv_loc);
        tv_subdist = (TextView) findViewById(R.id.tv_sub);
        // tv_cancel_action = (TextView)findViewById(R.id.tv_cancel_action);

        ll_scanned_data_wrapper = (LinearLayout)findViewById(R.id.ll_scanned_data_wrapper);
        ll_data_wrapper = (LinearLayout)findViewById(R.id.ll_data_wrapper);
        ll_action_button_wrapper = (LinearLayout)findViewById(R.id.savedel);

        final Button sav = (Button) findViewById(R.id.sav);
        sav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });


        //init storage
    }


    public void checkCameraPermission (){

    }

    /**
     * onclick handler for scan new card
     * @param view
     */
    public void scanNow( View view){
        // we need to check if the user has granted the camera permissions
        // otherwise scanner will not work
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
            return;
        }

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("Scan a Aadharcard QR Code");
        integrator.setCameraId(0);
        integrator.setOrientationLocked(true);
// Use a specific camera of the device
        integrator.initiateScan();
    }


    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //retrieve scan result
        super.onActivityResult(requestCode, resultCode, intent);
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if (scanningResult != null) {
            //we have a result
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();

            // process received data
            if (scanContent != null && !scanContent.isEmpty()) {
                processScannedData(scanContent);
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "Scan Cancelled", Toast.LENGTH_SHORT);
                toast.show();
            }

        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /**
     * process xml string received from aadhaar card QR code
     * @param scanData
     */
    protected void processScannedData(String scanData){
        Log.d("Rajdeol",scanData);
        XmlPullParserFactory pullParserFactory;

        try {
            // init the parserfactory
            pullParserFactory = XmlPullParserFactory.newInstance();
            // get the parser
            XmlPullParser parser = pullParserFactory.newPullParser();

            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(new StringReader(scanData));

            // parse the XML
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if(eventType == XmlPullParser.START_DOCUMENT) {
                    Log.d("Rajdeol","Start document");
                } else if(eventType == XmlPullParser.START_TAG && DataAttributes.AADHAAR_DATA_TAG.equals(parser.getName())) {
                    // extract data from tag
                    //uid
                    uid = parser.getAttributeValue(null,DataAttributes.AADHAR_UID_ATTR);

                    //name
                    name = parser.getAttributeValue(null,DataAttributes.AADHAR_NAME_ATTR);
                    //gender
                    gender = parser.getAttributeValue(null,DataAttributes.AADHAR_GENDER_ATTR);
                    // year of birth
                    yearOfBirth = parser.getAttributeValue(null,DataAttributes.AADHAR_YOB_ATTR);
                    // care of
                    careOf = parser.getAttributeValue(null,DataAttributes.AADHAR_CO_ATTR);
                    // village Tehsil
                    villageTehsil = parser.getAttributeValue(null,DataAttributes.AADHAR_VTC_ATTR);
                    // Post Office
                    postOffice = parser.getAttributeValue(null,DataAttributes.AADHAR_PO_ATTR);
                    // district
                    district = parser.getAttributeValue(null,DataAttributes.AADHAR_DIST_ATTR);
                    // state
                    state = parser.getAttributeValue(null,DataAttributes.AADHAR_STATE_ATTR);
                    // Post Code
                    postCode = parser.getAttributeValue(null,DataAttributes.AADHAR_PC_ATTR);
                    home = parser.getAttributeValue(null,DataAttributes.AADHAR_HO_ATTR);
                    loc = parser.getAttributeValue(null,DataAttributes.AADHAR_ST_ATTR);
                    subdist = parser.getAttributeValue(null,DataAttributes.AADHAR_LM_ATTR);

                } else if(eventType == XmlPullParser.END_TAG) {
                    Log.d("Rajdeol","End tag "+parser.getName());

                } else if(eventType == XmlPullParser.TEXT) {
                    Log.d("Rajdeol","Text "+parser.getText());
                    displayScannedData();
                }
                // update eventType
                eventType = parser.next();
            }
            displayScannedData();
            // display the data on screen
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }// EO function

    private void Login(final String uid) {


        Intent pass = new Intent(VerifyAadhar.this, MainActivity.class);
        startActivity(pass);


    }

    /**
     * show scanned information
     */
    public void displayScannedData(){
        ll_data_wrapper.setVisibility(View.GONE);
        ll_scanned_data_wrapper.setVisibility(View.VISIBLE);
//        ll_action_button_wrapper.setVisibility(View.VISIBLE);
        // clear old values if any
        tv_sd_uid.setText("");
        tv_sd_name.setText("");
        tv_sd_gender.setText("");
        tv_sd_yob.setText("");
        tv_sd_co.setText("");
        tv_sd_vtc.setText("");
        tv_sd_po.setText("");
        tv_sd_dist.setText("");
        tv_sd_state.setText("");
        tv_sd_pc.setText("");
        tv_home.setText("");
        tv_loc.setText("");
        tv_subdist.setText("");

        // update UI Elements
        tv_sd_uid.setText(uid);
        tv_sd_name.setText(name);
        tv_sd_gender.setText(gender);
        tv_sd_yob.setText(yearOfBirth);
        tv_sd_co.setText(careOf);
        tv_sd_vtc.setText(villageTehsil);
        tv_sd_po.setText(postOffice);
        tv_sd_dist.setText(district);
        tv_sd_state.setText(state);
        tv_sd_pc.setText(postCode);
        tv_home.setText(home);
        tv_loc.setText(loc);
        tv_subdist.setText(subdist);
        //save();
    }
    public void showHome(View view){
        ll_data_wrapper.setVisibility(View.VISIBLE);
        ll_scanned_data_wrapper.setVisibility(View.VISIBLE);
        ll_action_button_wrapper.setVisibility(View.GONE);
    }
    /**
     * display home screen onclick listener for cancel button
     * @param view
     */


    /**
     * save data to storage
     */
    public void save(){
        String deviceToken = FirebaseInstanceId.getInstance().getToken();
        final HashMap<String, Object> userdataMap = new HashMap<>();
        userdataMap.put("name",name);
        userdataMap.put("uid",uid);
        userdataMap.put("landmark",subdist);
        userdataMap.put("street",loc);
        userdataMap.put("home",home);
        userdataMap.put("postCode",postCode);
        userdataMap.put("state",state);
        userdataMap.put("district",district);
        userdataMap.put("postOffice",postOffice);
        userdataMap.put("villageTehsil",villageTehsil);
        userdataMap.put("yearOfBirth",yearOfBirth);
        userdataMap.put("gender",gender);
        userdataMap.put("image_link","link");
        String mUid = mAuth.getUid();
        String email = mAuth.getCurrentUser().getEmail();
        int index = email.indexOf('@');
        email = email.substring(0,index);
        userdataMap.put("phone_number",email);
        ///extra adding for profile info///
        ///afterwards form a page for this///
        userdataMap.put("resource1","not_entered");
        userdataMap.put("resource2","not_entered");
        userdataMap.put("resource3","not_entered");
        userdataMap.put("about_me","not_entered");
        userdataMap.put("email",mAuth.getCurrentUser().getEmail());
        userdataMap.put("image","Defaut");
        userdataMap.put("posting_location","not_entered");
        userdataMap.put("status","not_entered");
        userdataMap.put("user_post1","not_entered");
        userdataMap.put("user_post1_descp","not_entered");
        userdataMap.put("user_post1_time","not_entered");
        userdataMap.put("user_post2","not_entered");
        userdataMap.put("user_post2_descp","not_entered");
        userdataMap.put("user_post2_time","not_entered");
        userdataMap.put("user_post3","not_entered");
        userdataMap.put("id",mUid);
        userdataMap.put("user_post3_descp","not_entered");
        userdataMap.put("user_post3_time","not_entered");
        userdataMap.put("device_token_reg",deviceToken);
        //
        userdataMap.put("verified_user","no");
        //




        /////////////////


        FirebaseDatabase.getInstance().getReference().child("Users")
                .child(userId).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent pass = new Intent(VerifyAadhar.this,Enterdetails.class);
                pass.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(pass);
            }
        });
    }

    /**
     * onclick handler for show saved cards
     * this will start the SavedAadhaarCardActivity
     * @param view
     */

}// EO class
