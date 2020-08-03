package com.abc.sih;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssignResource extends AppCompatActivity {
    String id,adr,uid,type,div;
    private String Div,Rang,Beat;

    int k=0;
    RecyclerView rec;
    DatabaseReference hr,policeDB;
    List<String> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_resource);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            this.setFinishOnTouchOutside(false);
        }
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.x = -100;
        params.height = 1000;
        params.width = ActionBar.LayoutParams.MATCH_PARENT;
        params.y = -50;

        this.getWindow().setAttributes(params);
        list  = new ArrayList<String>();

        String email = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("Users").child(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Div = dataSnapshot.child("Division").getValue().toString();
                Rang= dataSnapshot.child("Range").getValue().toString();
                Beat = dataSnapshot.child("Beat").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        div = getIntent().getExtras().getString("Division");
        id = getIntent().getExtras().getString("resourceId");
        adr = getIntent().getExtras().getString("location");
        type = getIntent().getExtras().getString("type");
        hr = FirebaseDatabase.getInstance().getReference().child("hierarchy").child(adr).child(uid);
        hr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot r:dataSnapshot.getChildren()){
                    list.add(r.getKey().toString());

                }

                rec = findViewById(R.id.Policeres);
                rec.setHasFixedSize(true);
                rec.setLayoutManager(new LinearLayoutManager(AssignResource.this));
                UpdateUI();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void UpdateUI() {
        policeDB  = FirebaseDatabase.getInstance().getReference().child("Users");
        FirebaseRecyclerAdapter<Police,resourceHolder> adapter = new FirebaseRecyclerAdapter<Police, resourceHolder>(
                Police.class,
                R.layout.assignlist,
                resourceHolder.class,
                hr
        ) {
            @Override
            protected void populateViewHolder(final resourceHolder resourceHolder, Police police, int i) {
                final String list_user = list.get(i);

                policeDB.child(list_user).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String stat = " "+type+" assigned";
                        if (dataSnapshot.hasChild(type+"_assigned")){
                            long i = dataSnapshot.child(type+"_assigned").getChildrenCount();
                            stat = i+stat;
                        }else{
                            stat = "0"+stat;
                        }

                        final String name = dataSnapshot.child("name").getValue().toString();
                        String pic = "default";
                        if (dataSnapshot.hasChild("image")){
                            pic = dataSnapshot.child("image").getValue().toString();

                        }
                        resourceHolder.setUserImage(pic,getApplicationContext());
                        resourceHolder.setUserStatus(stat);
                        resourceHolder.setDisplayName(name);
                        resourceHolder.assign.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                resourceHolder.AssignPolice(list_user,id,type,div,name,Div,Rang,Beat);
                            }
                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        };
        rec.setAdapter(adapter);
    }
    public static class resourceHolder extends RecyclerView.ViewHolder{
        View mView;
        Button assign;
        String email,username;
        public resourceHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            assign = mView.findViewById(R.id.assign);
            assign.setText("Assign");
            email = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseDatabase.getInstance().getReference().child("Users").child(email).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    username = dataSnapshot.child("name").getValue().toString();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        public void setDisplayName(String name){
            TextView userNameView = (TextView) mView.findViewById(R.id.user_single_name);

            userNameView.setText(name);

        }

        public void setUserStatus(String status){

            TextView userStatusView = (TextView) mView.findViewById(R.id.user_single_status);
            userStatusView.setText(status);
        }

        public void setUserImage(final String image, final Context ctx){

            final ImageView userImageView = (ImageView) mView.findViewById(R.id.user_single_image);

            Picasso.with(ctx).load(image).placeholder(R.drawable.default_avatar).networkPolicy(NetworkPolicy.OFFLINE).into(userImageView, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {

                    Picasso.with(ctx).load(image).placeholder(R.drawable.default_avatar).into(userImageView);
                }
            });


        }

        public void AssignPolice(String list_user_id, String id, String type, String div, String name, String Div, String Rang, String Beat) {
            FirebaseDatabase.getInstance().getReference().child(div).child(Div).child(Rang).child(Beat).child(id).child("alloted").setValue("yes");
            FirebaseDatabase.getInstance().getReference().child(div).child(Div).child(Rang).child(Beat).child(id).child("Assigned").setValue(list_user_id);
            FirebaseDatabase.getInstance().getReference().child(div).child(Div).child(Rang).child(Beat).child(id).child("lastallocated").setValue(name);
            FirebaseDatabase.getInstance().getReference().child("Users").child(list_user_id)
                    .child(type +"_assigned").child(id).setValue("Assigned");

            Map logdata = new HashMap<>();
            logdata.put("assgnTime", ServerValue.TIMESTAMP);
            logdata.put("assgnBy", username);
            logdata.put("assgnto",name);

            FirebaseDatabase.getInstance().getReference().child("Logs").child(id).push().updateChildren(logdata);

        }
    }
}
