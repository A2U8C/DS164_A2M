package com.abc.sih;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

import java.util.HashMap;
import java.util.Map;

import static org.webrtc.ContextUtils.getApplicationContext;


public class SelectRfo extends Fragment {
    private RecyclerView rec;

    public SelectRfo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_select_rfo, container, false);
        rec = view.findViewById(R.id.rec);
        final String email = FirebaseAuth.getInstance().getUid();
        FirebaseDatabase.getInstance().getReference().child("Users").child(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String station = dataSnapshot.child("station").getValue().toString();
                rec.setHasFixedSize(true);
                rec.setLayoutManager(new LinearLayoutManager(getActivity()));
                updateUi(station,email);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

    private void updateUi(final String station, final String email) {
        final DatabaseReference rfoDb = FirebaseDatabase.getInstance().getReference().child("Users");
        DatabaseReference rfo  = FirebaseDatabase.getInstance().getReference().child("rfo").child(station);
        FirebaseRecyclerAdapter<String, resourceHolder> adapter = new FirebaseRecyclerAdapter<String, resourceHolder>(
                String.class,
                R.layout.assignlist,
                resourceHolder.class,
                rfo
        ) {
            @Override
            protected void populateViewHolder(final resourceHolder resourceHolder, String police, int i) {
                final String list_user =  getRef(i).getKey();

                rfoDb.child(list_user).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String stat = dataSnapshot.child("status").getValue().toString();
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
                                FirebaseDatabase.getInstance().getReference().child("hierarchy").child(station).child(list_user).child(email).child(email).setValue(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        ((UserDetails)getActivity()).nextpage();

                                    }
                                });
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

        public resourceHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            assign = mView.findViewById(R.id.assign);
            assign.setText("Select");

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

    }
}