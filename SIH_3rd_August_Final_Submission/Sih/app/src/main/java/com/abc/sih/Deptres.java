package com.abc.sih;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Deptres extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private String mUserId,type,station;
    @Override
    public void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deptres);
        mUserId = FirebaseAuth.getInstance().getUid();

        DatabaseReference pol = FirebaseDatabase.getInstance().getReference().child("Users").child(mUserId);
        //Log.d("CuurentUserId",mUserId);
        pol.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                type = dataSnapshot.child("type").getValue().toString();
                station = dataSnapshot.child("station").getValue().toString();
                if (type.equals("B")){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_contanier,new ResourceFragment()).commit();

                }else{
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_contanier,new AssignedFrag()).commit();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
