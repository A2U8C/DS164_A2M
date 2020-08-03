package com.abc.sih;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class DivisiontoRangeActivity extends AppCompatActivity {

    TextView division_name,division_description;
    ImageView division_image,division_data,img2;

    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_divisionto_range);

        String div_name = getIntent().getStringExtra("division_name");
        ref = FirebaseDatabase.getInstance().getReference().child("Andaman-Division-Ranges").child(div_name);

        division_name = (TextView) findViewById(R.id.division_name);
        division_description = (TextView) findViewById(R.id.division_description);

        division_image = (ImageView) findViewById(R.id.division_image);
        division_data = (ImageView) findViewById(R.id.division_data);
        img2 = (ImageView) findViewById(R.id.img2);


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    String name = dataSnapshot.child("div_name").getValue().toString();
                    String data1 = dataSnapshot.child("div_data").getValue().toString();
                    String descp = dataSnapshot.child("div_descp").getValue().toString();
                    String tab2 = dataSnapshot.child("div_table2").getValue().toString();
                    String map = dataSnapshot.child("div_map").getValue().toString();

                    division_name.setText(name);
                    Picasso.with(DivisiontoRangeActivity.this).load(data1).placeholder(R.color.grey).into(division_image);
                    division_description.setText(descp);
                    Picasso.with(DivisiontoRangeActivity.this).load(tab2).placeholder(R.color.grey).into(division_data);
                    Picasso.with(DivisiontoRangeActivity.this).load(map).placeholder(R.color.grey).into(img2);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}