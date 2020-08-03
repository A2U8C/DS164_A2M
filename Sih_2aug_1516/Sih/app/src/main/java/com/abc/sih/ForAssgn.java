package com.abc.sih;


import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;


public class ForAssgn extends Fragment {
    private RecyclerView rec;
    private String email,type,station;
    private DatabaseReference pol;
    private String Div,Rang,Beat;


    public ForAssgn() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_for_assgn, container, false);
        rec = (RecyclerView) view.findViewById(R.id.resource);
        email = FirebaseAuth.getInstance().getCurrentUser().getUid();

        pol = FirebaseDatabase.getInstance().getReference().child("Users").child(email);
        pol.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Div = dataSnapshot.child("Division").getValue().toString();
                Rang= dataSnapshot.child("Range").getValue().toString();
                Beat = dataSnapshot.child("Beat").getValue().toString();
                UpdateUI();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return  view;
    }

    private void UpdateUI() {
        rec.setHasFixedSize(true);
        rec.setLayoutManager(new LinearLayoutManager(getActivity()));
        final Query allot = FirebaseDatabase.getInstance().getReference().child("ForRes").child(Div).child(Rang).child(Beat).orderByChild("Assigned").equalTo(email);
        FirebaseRecyclerAdapter<resource, resourceHolder> fr = new FirebaseRecyclerAdapter<resource, resourceHolder>(
                resource.class,
                R.layout.resource_layout,
                resourceHolder.class,
                allot
        ) {

            @Override
            protected void populateViewHolder(final resourceHolder resourceHolder, resource resource, int i) {

                final String list_user_id = getRef(i).getKey();

                FirebaseDatabase.getInstance().getReference().child("ForRes").child(station).child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {
                        String number = dataSnapshot.child("resId").getValue().toString();

                        String model = dataSnapshot.child("type").getValue().toString();
                        final String alloted = dataSnapshot.child("alloted").getValue().toString();
                        final String type = dataSnapshot.child("type").getValue().toString();
                        resourceHolder.setNumber(number);
                        resourceHolder.setModel(type);
                        resourceHolder.setAllotment(alloted);
                        resourceHolder.setResImage(type);

                        resourceHolder.allot.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                resourceHolder.setDeAllocate(type,email,list_user_id,Div,Rang,Beat);


                            }
                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        };
        rec.setAdapter(fr);
    }
    public  static class resourceHolder extends RecyclerView.ViewHolder{
        View mView;
        ImageButton arr;
        Button allot;

        public resourceHolder(@NonNull View itemView) {
            super(itemView);
            mView  = itemView;
            arr = mView.findViewById(R.id.expand_button);
            arr.setVisibility(View.GONE);
            allot = mView.findViewById(R.id.allot);
        }

        public void setNumber(String number) {
            TextView veh = mView.findViewById(R.id.ID);
            veh.setText(number);
        }

        public void setModel(String model) {
            TextView mod = mView.findViewById(R.id.model);
            mod.setText(model);
        }



        public void setAllotment(String alloted) {
            TextView last = mView.findViewById(R.id.last);


            allot.setEnabled(true);
            allot.setBackgroundColor(Color.RED);
            allot.setText("DONE");
            last.setText("Allocated to");

        }

        public void setResImage(String type) {
            ImageView res = mView.findViewById(R.id.resource);
            if (type.equals("timber")){
                res.setImageResource(R.drawable.timber);
            }else {
                res.setImageResource(R.drawable.rubber);
            }
        }

        public void setDeAllocate(String type, String assgn, String list_user_id, String Div, String Rang, String Beat) {
            Log.d("allotedname",assgn);
            FirebaseDatabase.getInstance().getReference().child("Users").child(assgn).child(type+"_assigned").removeValue();
            DatabaseReference pol = FirebaseDatabase.getInstance().getReference().child("ForRes").child(Div).child(Rang).child(Beat).child(list_user_id);
            pol.child("alloted").setValue("no");
            pol.child("Assigned").removeValue();
            DatabaseReference setTime = FirebaseDatabase.getInstance().getReference().child("Logs").child(list_user_id);
            setTime.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds: dataSnapshot.getChildren()){
                        if (!ds.hasChild("returned")){
                            ds.getRef().child("returned").setValue(ServerValue.TIMESTAMP);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
