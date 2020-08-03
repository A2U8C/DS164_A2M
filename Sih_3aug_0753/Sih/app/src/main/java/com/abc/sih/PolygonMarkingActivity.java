package com.abc.sih;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class PolygonMarkingActivity extends AppCompatActivity implements OnMapReadyCallback, SeekBar.OnSeekBarChangeListener {

    GoogleMap gMap;
    CheckBox checkBox;
    SeekBar seekRed,seekGreen,seekBlue;
    Button btDraw,btClear;

    Polygon polygon;
    List<LatLng> latLngList = new ArrayList<>();
    List<Marker> markerList = new ArrayList<>();

    int red=0,green=0,blue=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_polygon_marking);

        checkBox = (CheckBox) findViewById(R.id.check_box);
        seekRed = (SeekBar) findViewById(R.id.seek_red);
        seekGreen = (SeekBar) findViewById(R.id.seek_green);
        seekBlue = (SeekBar) findViewById(R.id.seek_blue);
        btDraw = (Button) findViewById(R.id.bt_draw);
        btClear = (Button) findViewById(R.id.bt_clear);

        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.google_map);
        supportMapFragment.getMapAsync(this);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean b) {
                //get checkbox state
                if(b){
                   if (polygon == null) return;
                       //fill polygon color
                       polygon.setFillColor(Color.rgb(red,green,blue));

                }else{
                    //unfill polygon color
                    polygon.setFillColor(Color.TRANSPARENT);
                }
            }
        });

        btDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(polygon != null) polygon.remove();

                //create polyoptions
                PolygonOptions polygonOptions = new PolygonOptions().addAll(latLngList)
                        .clickable(true);
                polygon = gMap.addPolygon(polygonOptions);
                //set polygon stroke color
                polygon.setStrokeColor(Color.rgb(red,green,blue));
                if(checkBox.isChecked()){
                    //fill polygon color
                    polygon.setFillColor(Color.rgb(red,green,blue));
                }
            }
        });
        btClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //clear all
                if(polygon != null) polygon.remove();
                for(Marker marker:markerList) marker.remove();
                latLngList.clear();
                markerList.clear();
                checkBox.setChecked(false);
                seekRed.setProgress(0);
                seekGreen.setProgress(0);
                seekBlue.setProgress(0);
            }
        });

        seekRed.setOnSeekBarChangeListener(this);
        seekGreen.setOnSeekBarChangeListener(this);
        seekBlue.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        gMap = googleMap;
        gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //create marker options
                MarkerOptions markerOptions = new MarkerOptions().position(latLng);
                //create marker
                Marker marker = gMap.addMarker(markerOptions);
                //Add Latlng marker
                latLngList.add(latLng);
                markerList.add(marker);
            }
        });
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

        switch (seekBar.getId()){
            case R.id.seek_red:
                red = i;
                break;
            case R.id.seek_green:
                green = i;
                break;
            case R.id.seek_blue:
                blue = i;
                break;

        }
        if(polygon!=null){
            //set polygon stroke color
            polygon.setStrokeColor(Color.rgb(red,green,blue));
            if(checkBox.isChecked()){
                //fill polygon color
                polygon.setFillColor(Color.rgb(red,green,blue));
        }
    }


    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}