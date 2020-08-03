package com.abc.sih;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ForestRes extends AppCompatActivity {
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
        setContentView(R.layout.activity_forest_res);
        mUserId = FirebaseAuth.getInstance().getUid();

        DatabaseReference pol = FirebaseDatabase.getInstance().getReference().child("Users").child(mUserId);

        pol.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                type = dataSnapshot.child("type").getValue().toString();
                if (type.equals("B")){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_contanier,new ResourceForest()).commit();

                }else{
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_contanier,new ForAssgn()).commit();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
