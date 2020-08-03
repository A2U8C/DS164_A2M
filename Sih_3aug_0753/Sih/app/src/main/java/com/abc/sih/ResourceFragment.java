package com.abc.sih;


import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.List;


public class ResourceFragment extends Fragment {
    private String email,type,Div,Rang,Beat;
    private DatabaseReference allot,pol;
    private RecyclerView rec;
    List<String> list;

    public ResourceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_resource, container, false);
        rec = (RecyclerView) view.findViewById(R.id.resource);
        email = FirebaseAuth.getInstance().getCurrentUser().getUid();

        pol = FirebaseDatabase.getInstance().getReference().child("Users").child(email);
        pol.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                type = dataSnapshot.child("type").getValue().toString();
                Div = dataSnapshot.child("Division").getValue().toString();
                Rang = dataSnapshot.child("Range").getValue().toString();
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
        allot = FirebaseDatabase.getInstance().getReference().child("resource").child(Div).child(Rang).child(Beat);
        FirebaseRecyclerAdapter<resource,resourceHolder> fr = new FirebaseRecyclerAdapter<resource, resourceHolder>(
                resource.class,
                R.layout.resource_layout,
                resourceHolder.class,
                allot
        ) {

            @Override
            protected void populateViewHolder(final resourceHolder resourceHolder, resource resource, int i) {

                final String list_user_id = getRef(i).getKey();

                allot.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {
                        String number = dataSnapshot.child("resId").getValue().toString();

                        String model = dataSnapshot.child("model").getValue().toString();
                        String lastallocated = dataSnapshot.child("lastallocated").getValue().toString();
                        final String alloted = dataSnapshot.child("alloted").getValue().toString();
                        final String type = dataSnapshot.child("type").getValue().toString();
                        resourceHolder.setNumber(number);
                        resourceHolder.setModel(model);
                        resourceHolder.setLastAllocated(lastallocated);
                        resourceHolder.setAllotment(alloted);
                        resourceHolder.setResImage(type);
                        resourceHolder.arr.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                resourceHolder.displayLast();
                            }
                        });
                        resourceHolder.log.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent res = new Intent(getActivity(), LogActivity.class);
                                res.putExtra("resId", list_user_id);
                                startActivity(res);
                            }
                        });
                        resourceHolder.allot.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (alloted.equals("yes")){

                                    String assgn = dataSnapshot.child("Assigned").getValue().toString();
                                    resourceHolder.setDeAllocate(type,assgn,list_user_id,Div,Rang,Beat);

                                }else {
                                    Intent res = new Intent(getActivity(), AssignResource.class);
                                    res.putExtra("Division", "resource");
                                    res.putExtra("resourceId", list_user_id);
                                    res.putExtra("location", Div+Rang+Beat);
                                    res.putExtra("type", type);
                                    startActivity(res);
                                }
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
        Button allot,log;

        public resourceHolder(@NonNull View itemView) {
            super(itemView);
            mView  = itemView;
            arr = mView.findViewById(R.id.expand_button);
            allot = mView.findViewById(R.id.allot);
            log = mView.findViewById(R.id.logs);
        }

        public void setNumber(String number) {
            TextView veh = mView.findViewById(R.id.ID);
            veh.setText(number);
        }

        public void setModel(String model) {
            TextView mod = mView.findViewById(R.id.model);
            mod.setText(model);
        }

        public void setLastAllocated(String lastallocated) {
            TextView last = mView.findViewById(R.id.police_link);
            last.setText(lastallocated);
        }

        public void displayLast() {
            LinearLayout show = mView.findViewById(R.id.police);
            LinearLayout log = mView.findViewById(R.id.logmap);
            if(show.getVisibility()==View.VISIBLE)
            {
                show.setVisibility(View.GONE);
                log.setVisibility(View.GONE);
                arr.setImageResource(R.drawable.ic_expand_more_black_36dp);
            }
            else
            {
                show.setVisibility(View.VISIBLE);
                log.setVisibility(View.VISIBLE);
                arr.setImageResource(R.drawable.ic_expand_less_black_36dp);
            }
        }

        public void setAllotment(String alloted) {
            TextView last = mView.findViewById(R.id.last);

            if (alloted.equals("yes")){
                allot.setEnabled(true);
                allot.setBackgroundColor(Color.RED);
                allot.setText("DeAllot");
                last.setText("Allocated to");
            }else {
                allot.setEnabled(true);
                allot.setBackgroundColor(Color.GREEN);
                allot.setText("Allot");
                last.setText("Last Allocated to");

            }
        }

        public void setResImage(String type) {
            ImageView res = mView.findViewById(R.id.resource);
            if (type.equals("car")){
                res.setImageResource(R.drawable.police);
            }else {
                res.setImageResource(R.drawable.gun);
            }
        }

        public void setDeAllocate(String type, String assgn, String list_user_id,String Div,String ran, String beat) {
            Log.d("allotedname",assgn);
            FirebaseDatabase.getInstance().getReference().child("Users").child(assgn).child(type+"_assigned").removeValue();
            DatabaseReference pol = FirebaseDatabase.getInstance().getReference().child("resource").child(Div).child(ran).child(beat).child(list_user_id);
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
