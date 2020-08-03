package com.abc.sih;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.maps.model.Circle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminAttendanceActivity extends AppCompatActivity {


    private DatabaseReference mAttendanceDatabase;
    private DatabaseReference mUserRef;
    private FirebaseAuth mAuth;
    private LinearLayoutManager mLayoutManager;

    private FirebaseRecyclerAdapter firebaseRecyclerAdapter;

    private RecyclerView mAttendanceList;
    EditText date;
    Button filter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_attendance);

        EditText date;
        date = (EditText)findViewById(R.id.input_date);
        new DateInputMask(date);

        String datee = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        mAttendanceDatabase = FirebaseDatabase.getInstance().getReference().child("Attendance").child(datee);
        mAttendanceDatabase.keepSynced(true);

        filter = (Button) findViewById(R.id.submit_button);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String abc = date.getText().toString();

                char[] cArr = new char[]{abc.charAt(6),abc.charAt(7),abc.charAt(8),abc.charAt(9),'-',abc.charAt(3),abc.charAt(4),'-',abc.charAt(0),abc.charAt(1)};
                Log.e("anish_abc", String.valueOf(cArr));
                mAttendanceDatabase = FirebaseDatabase.getInstance().getReference().child("Attendance").child(String.valueOf(cArr));
                mAttendanceDatabase.keepSynced(true);
                showlist();
            }
        });






        mLayoutManager =new LinearLayoutManager(AdminAttendanceActivity.this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        mAttendanceList = (RecyclerView) findViewById(R.id.rv);
        mAttendanceList.setHasFixedSize(true);
        mAttendanceList.setLayoutManager(mLayoutManager);

        showlist();


    }

    public void showlist()
    {

        Query abc = mAttendanceDatabase;
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<AdminAttd, PhotoViewHolder>(
                AdminAttd.class,
                R.layout.single_officer_nakli,
                PhotoViewHolder.class,
                mAttendanceDatabase

        ) {
            @Override
            protected void populateViewHolder(final PhotoViewHolder photoViewHolder, AdminAttd users, int position) {

                final String list_user_id = getRef(position).getKey();


                mAttendanceDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists() && dataSnapshot.child("status").exists())
                        {


                            String usr_name = dataSnapshot.child("status").getValue().toString();

                            Log.e("anish_adndn",usr_name);
                            //String name,img;

                            FirebaseDatabase.getInstance().getReference().child("Users").child(usr_name).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    String name = dataSnapshot.child("name").getValue().toString();
                                    String img = dataSnapshot.child("image").getValue().toString();
                                    String type = dataSnapshot.child("type").getValue().toString();

                                    photoViewHolder.setName(name);
                                    photoViewHolder.setDp(img,getApplicationContext());
                                    photoViewHolder.setType(type);
                                    Log.e("anish_adndn1",name);

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                           /* photoViewHolder.noti_icon.setAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.fade_transition_animation));

                            photoViewHolder.container.setAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.fade_scale_animation));
                            photoViewHolder.setNotifType(notif_type);
                            photoViewHolder.setNotifImg(notif_type,context);
                            photoViewHolder.setNotifTime(notif_time);
                            photoViewHolder.setNotifDate(notif_date);
                            photoViewHolder.setNotifDesc(notif_desc); */


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


        mAttendanceList.setAdapter(firebaseRecyclerAdapter);




    }


    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        View mView;
        CircleImageView dp;
        TextView name,type;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            dp = (CircleImageView) mView.findViewById(R.id.doc_profile);
            name = (TextView) mView.findViewById(R.id.docId);
            type = (TextView) mView.findViewById(R.id.description);


        }


        public void setName(String namee){

            name.setText(namee);
        }
        public void setType(String typee){
            type.setText(typee);
        }
        public void setDp(String link, Context ctx){
            Picasso.with(ctx).load(link).placeholder(R.drawable.default_avatar).networkPolicy(NetworkPolicy.OFFLINE).into(dp, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {

                    Picasso.with(ctx).load(link).placeholder(R.drawable.default_avatar).into(dp);
                }
            });
        }


    }

    public class DateInputMask implements TextWatcher {

        private String current = "";
        private String ddmmyyyy = "DDMMYYYY";
        private Calendar cal = Calendar.getInstance();
        private EditText input;

        public DateInputMask(EditText input) {
            this.input = input;
            this.input.addTextChangedListener(this);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.toString().equals(current)) {
                return;
            }

            String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
            String cleanC = current.replaceAll("[^\\d.]|\\.", "");

            int cl = clean.length();
            int sel = cl;
            for (int i = 2; i <= cl && i < 6; i += 2) {
                sel++;
            }
            //Fix for pressing delete next to a forward slash
            if (clean.equals(cleanC)) sel--;

            if (clean.length() < 8){
                clean = clean + ddmmyyyy.substring(clean.length());
            }else{
                //This part makes sure that when we finish entering numbers
                //the date is correct, fixing it otherwise
                int day  = Integer.parseInt(clean.substring(0,2));
                int mon  = Integer.parseInt(clean.substring(2,4));
                int year = Integer.parseInt(clean.substring(4,8));

                mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;
                cal.set(Calendar.MONTH, mon-1);
                year = (year<1900)?1900:(year>2100)?2100:year;
                cal.set(Calendar.YEAR, year);
                // ^ first set year for the line below to work correctly
                //with leap years - otherwise, date e.g. 29/02/2012
                //would be automatically corrected to 28/02/2012

                day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                clean = String.format("%02d%02d%02d",day, mon, year);
            }

            clean = String.format("%s/%s/%s", clean.substring(0, 2),
                    clean.substring(2, 4),
                    clean.substring(4, 8));

            sel = sel < 0 ? 0 : sel;
            current = clean;
            input.setText(current);
            input.setSelection(sel < current.length() ? sel : current.length());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }


    }

}