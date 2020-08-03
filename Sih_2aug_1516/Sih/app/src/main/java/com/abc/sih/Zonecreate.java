package com.abc.sih;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.abc.sih.Helper.LocaleHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import io.paperdb.Paper;

public class Zonecreate extends AppCompatActivity {
    private EditText edt,edt2;
    private Button btn;
    private String selected_item;
    private double lat,lon;

    TextView txt1,txt2;
    Button btn1;

    Spinner dropdown;
    String[] items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zonecreate);

        edt = findViewById(R.id.radius);
        btn = (Button)findViewById(R.id.submit);
        edt2 = findViewById(R.id.title);

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
            items = new String[]{"Red", "Yellow"};
        }
        else if(language.equals("hi"))
        {
            dropdown = findViewById(R.id.spinner1);
            items = new String[]{"लाल", "पीला"};
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
    }

    private void Submit(double rad, String selected_item, String title) {
        DatabaseReference zone = FirebaseDatabase.getInstance().getReference().child("Zones");
        if (selected_item.equals("Red")){
            zone = zone.child("red");
        }else {
            zone = zone.child("yellow");
        }
        Map cord = new HashMap<>();
        cord.put("rad",rad);
        cord.put("lon",lon);
        cord.put("lat",lat);
        cord.put("title",title);
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
}
