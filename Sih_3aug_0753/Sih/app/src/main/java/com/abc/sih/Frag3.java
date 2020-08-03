package com.abc.sih;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;


public class Frag3 extends Fragment {

    private RecyclerView mNotificationList;

    private DatabaseReference mNotificationDatabase;
    private DatabaseReference mUserRef;
    private FirebaseAuth mAuth;
    private LinearLayoutManager mLayoutManager;
    private FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    private String notif_type,notif_date,notif_time,notif_desc;
    private ImageView botimg;
    private SearchView searchView;



    private static Activity myac;
    static Context context;
    public Frag3() {

    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        context=activity;
        myac = getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view =  inflater.inflate(R.layout.fragment_frag3, container, false);
      //  String userid = FirebaseAuth.getInstance().getCurrentUser().getEmail();
      //  int index = userid.indexOf("@");
      //  userid = userid.substring(0,index);

        //for now trial
        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        searchView = view.findViewById(R.id.search_input);




        mNotificationDatabase = FirebaseDatabase.getInstance().getReference().child("Notification").child(userid);
        mNotificationDatabase.keepSynced(true);

        botimg = view.findViewById(R.id.msgs);

        botimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Intent intent = new Intent(getContext(),ChatbotActivity.class);
                // startActivity(intent);
            }
        });
        mLayoutManager =new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        mNotificationList = (RecyclerView) view.findViewById(R.id.notification_list);
        mNotificationList.setHasFixedSize(true);
        mNotificationList.setLayoutManager(mLayoutManager);

        showNotification();



        return view;
    }
    @Override
    public void onStart() {
        super.onStart();

        showNotification();

        if(searchView !=null)
        {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    search(s);
                    return true;
                }
            });
        }



    }
    private void search(String str)
    {

        showUpdNotification();
    }

    private void showUpdNotification() {

        Query abc = mNotificationDatabase.orderByChild("time");
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Notif, PhotoViewHolder>(
                Notif.class,
                R.layout.single_notification_item,
                PhotoViewHolder.class,
                mNotificationDatabase

        ) {
            @Override
            protected void populateViewHolder(final PhotoViewHolder photoViewHolder, Notif users, int position) {

                final String list_user_id = getRef(position).getKey();


                mNotificationDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists() && dataSnapshot.child("type").getValue()!=null && dataSnapshot.child("date").getValue() !=null &&
                                dataSnapshot.child("time").getValue() !=null && dataSnapshot.child("desc").getValue()!=null
                                && dataSnapshot.child("desc").getValue().toString().contains(searchView.getQuery()))
                        {

                            notif_type = dataSnapshot.child("type").getValue().toString();
                            notif_date = dataSnapshot.child("date").getValue().toString();
                            notif_time = dataSnapshot.child("time").getValue().toString();
                            notif_desc = dataSnapshot.child("desc").getValue().toString();


                            photoViewHolder.noti_icon.setAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.fade_transition_animation));

                            photoViewHolder.container.setAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.fade_scale_animation));
                            photoViewHolder.setNotifType(notif_type);
                            photoViewHolder.setNotifImg(notif_type,context);
                            photoViewHolder.setNotifTime(notif_time);
                            photoViewHolder.setNotifDate(notif_date);
                            photoViewHolder.setNotifDesc(notif_desc);


                        }
                        else
                        {
                            photoViewHolder.itemView.setVisibility(View.GONE);
                        }




                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        };


        mNotificationList.setAdapter(firebaseRecyclerAdapter);


    }

    private void showNotification() {

        Query abc = mNotificationDatabase.orderByChild("time");
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Notif, PhotoViewHolder>(
                Notif.class,
                R.layout.single_notification_item,
                PhotoViewHolder.class,
                mNotificationDatabase

        ) {
            @Override
            protected void populateViewHolder(final PhotoViewHolder photoViewHolder, Notif users, int position) {

                final String list_user_id = getRef(position).getKey();


                mNotificationDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists() && dataSnapshot.child("type").getValue()!=null && dataSnapshot.child("date").getValue() !=null &&
                                dataSnapshot.child("time").getValue() !=null && dataSnapshot.child("desc").getValue()!=null)
                        {

                            notif_type = dataSnapshot.child("type").getValue().toString();
                            notif_date = dataSnapshot.child("date").getValue().toString();
                            notif_time = dataSnapshot.child("time").getValue().toString();

                            notif_desc = dataSnapshot.child("desc").getValue().toString();


                            photoViewHolder.noti_icon.setAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.fade_transition_animation));

                            photoViewHolder.container.setAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.fade_scale_animation));
                            photoViewHolder.setNotifType(notif_type);
                            photoViewHolder.setNotifImg(notif_type,context);
                            photoViewHolder.setNotifTime(notif_time);
                            photoViewHolder.setNotifDate(notif_date);
                            photoViewHolder.setNotifDesc(notif_desc);


                        }




                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        };


        mNotificationList.setAdapter(firebaseRecyclerAdapter);



    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        View mView;
        CircleImageView noti_icon;
        TextView noti_type,noti_date,noti_time,noti_desc;
        RelativeLayout container;


        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            noti_icon = (CircleImageView) mView.findViewById(R.id.notification_icon);
            noti_type = (TextView) mView.findViewById(R.id.notification_type);
            noti_date = (TextView) mView.findViewById(R.id.notification_date);
            noti_time = (TextView) mView.findViewById(R.id.notification_time);
            noti_desc = (TextView) mView.findViewById(R.id.notification_desc);
            container = (RelativeLayout) mView.findViewById(R.id.container);

        }

        public void setNotifType(String type)
        {
            noti_type.setText(type);
        }

        public void setNotifDate(String date)
        {
            noti_date.setText(date);
        }
        public void setNotifTime(String time)
        {
            noti_time.setText(time);
        }
        public void setNotifDesc(String desc)
        {
            noti_desc.setText(desc);
        }
        public void setNotifImg(String type,Context ctx)
        {

            if(type.toLowerCase().equals("insurance")|| type.toLowerCase().equals("reporter's console"))
            {
                noti_icon.setImageResource(R.drawable.insurance_icon);
            }
            else if(type.toLowerCase().equals("appointment") || type.toLowerCase().equals("booking") )
            {
                noti_icon.setImageResource(R.drawable.appointment_icon);
            }
            else if(type.toLowerCase().equals("attendance admin"))
            {
                noti_icon.setImageResource(R.drawable.attendance);
            }
        }



    }

}
