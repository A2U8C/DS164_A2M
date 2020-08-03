package com.abc.sih;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class ViewCPNotesActivity extends AppCompatActivity {

    String user_id,date,childval;
    DatabaseReference ref;
    ImageView attach1,attach2,attach3;
    TextView heading,cp_notes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_c_p_notes);
        user_id = getIntent().getStringExtra("user_id");
        date = getIntent().getStringExtra("date");
        childval = getIntent().getStringExtra("childval");

        attach1 = (ImageView) findViewById(R.id.input_attach1);
        attach2 = (ImageView) findViewById(R.id.input_attach2);
        attach3 = (ImageView) findViewById(R.id.input_attach3);

        heading = (TextView) findViewById(R.id.head);
        cp_notes = (TextView) findViewById(R.id.cp_notes);

        heading.setText("Checkpoint "+childval + " Notes");

        ref = FirebaseDatabase.getInstance().getReference().child("Checkpoint").child(user_id).child(date);

        Log.e("aa_aa",user_id + "\n" +date+"\n"+childval);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               String abc = dataSnapshot.child(childval).getValue().toString();
               cp_notes.setText(abc);
               if(dataSnapshot.child(childval+"link1").getValue()!=null){
                   attach1.setVisibility(View.VISIBLE);
                   String ip_1 = dataSnapshot.child(childval+"link1").getValue().toString();
                   Picasso.with(ViewCPNotesActivity.this).load(ip_1).placeholder(R.color.grey).networkPolicy(NetworkPolicy.OFFLINE).into(attach1, new Callback() {
                       @Override
                       public void onSuccess() {

                       }

                       @Override
                       public void onError() {
                           Picasso.with(ViewCPNotesActivity.this).load(ip_1).into(attach1);
                       }
                   });
               }

                if(dataSnapshot.child(childval+"link2").getValue()!=null){
                    attach2.setVisibility(View.VISIBLE);
                    String ip_1 = dataSnapshot.child(childval+"link2").getValue().toString();
                    Picasso.with(ViewCPNotesActivity.this).load(ip_1).placeholder(R.color.grey).networkPolicy(NetworkPolicy.OFFLINE).into(attach2, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(ViewCPNotesActivity.this).load(ip_1).into(attach2);
                        }
                    });
                }

                if(dataSnapshot.child(childval+"link3").getValue()!=null){
                    attach3.setVisibility(View.VISIBLE);
                    String ip_1 = dataSnapshot.child(childval+"link3").getValue().toString();
                    Picasso.with(ViewCPNotesActivity.this).load(ip_1).placeholder(R.color.grey).networkPolicy(NetworkPolicy.OFFLINE).into(attach3, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(ViewCPNotesActivity.this).load(ip_1).into(attach3);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}