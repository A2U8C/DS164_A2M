package com.abc.sih.Reports;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.abc.sih.FullCaseViewActivity;
import com.abc.sih.GetTimeAgo;
import com.abc.sih.ProfileActivity;
import com.abc.sih.ProfileActivityNakli;
import com.abc.sih.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


public class HotFragment extends Fragment {

    private RecyclerView mUsersList;
    private String user_station,user_type,mCode;

    private DatabaseReference mUsersDatabase;
    private DatabaseReference mUserRef;
    private FirebaseAuth mAuth;
    private String disp_name, disp_user_image, disp_uploaded_image, disp_likes,disp_case_type,disp_case_time,disp_case_assgn;
    private CircleImageView mPostBtn;
    private String post_participated;
    private StorageReference mImageStorage;
    private ProgressDialog mProgressDialog;
    private static final int GALLERY_PICK = 1;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mUserDatabase;
    private TextView imptxt;
    private ImageView upvote,downvote;
    private String current_uid;
    private DatabaseReference mPostsDatabase;
    List<String> utt;
    private String download_url;
    private String user_name,user_dp;
    private LinearLayoutManager mLayoutManager;
    static Context context;
    private String unique_code;
    private FirebaseRecyclerAdapter firebaseRecyclerAdapter;

    private static Activity myac;
    View view;
    public HotFragment() {
        // Required empty public constructor
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
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_hot, container, false);


        //imptxt = (TextView)view.findViewById(R.id.imp_txt);
        mAuth = FirebaseAuth.getInstance();
        mImageStorage = FirebaseStorage.getInstance().getReference();
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());

        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        mUserDatabase.keepSynced(true);
        mPostsDatabase = FirebaseDatabase.getInstance().getReference().child("Posts");
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists() && dataSnapshot.child("name").getValue()!=null && dataSnapshot.child("image").getValue()!= null){
                    user_name = dataSnapshot.child("name").getValue().toString();
                    user_dp = dataSnapshot.child("image").getValue().toString();
                    user_station = dataSnapshot.child("station").getValue().toString();
                    user_type = Objects.requireNonNull(dataSnapshot.child("type").getValue()).toString();
                    switch (user_type){
                        case "D":
                            mCode = dataSnapshot.child("Division").getValue().toString();

                            break;
                        case "R":
                            String a = dataSnapshot.child("Division").getValue().toString();
                            String b =dataSnapshot.child("Range").getValue().toString();
                            mCode = a+" "+b;

                            break;
                        default:
                            String a1 = dataSnapshot.child("Division").getValue().toString();
                            String b1 =dataSnapshot.child("Range").getValue().toString();
                            String c = dataSnapshot.child("Beat").getValue().toString();
                            mCode = a1+ " "+ b1 + " "+c;
                            break;

                    }
                    hotSection();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        String current_uid = mCurrentUser.getUid();
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true); // THIS ALSO SETS setStackFromBottom to true
        mLayoutManager.setStackFromEnd(true);

        mUsersList = (RecyclerView) view.findViewById(R.id.users_list);
        mUsersList.setHasFixedSize(true);
        mUsersList.setLayoutManager(mLayoutManager);




        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        //Query ftp = mUsersDatabase.orderByChild("participated").equalTo("yes");
       // hotSection();


    }
    private void hotSection(){
        Query ftp = mPostsDatabase.orderByChild("likes");//.limitToLast(0);
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Reports, PhotoViewHolder>(
                Reports.class,
                R.layout.single_report_item_nakli,
                PhotoViewHolder.class,
                ftp

        ) {
            @Override
            protected void populateViewHolder(final PhotoViewHolder photoViewHolder, Reports users, int position) {

                final String list_user_id = getRef(position).getKey();


                mPostsDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.child("name").getValue()!=null && dataSnapshot.child("image").getValue() !=null &&
                                dataSnapshot.child("link").getValue() !=null && dataSnapshot.child("type").getValue()!=null &&
                                dataSnapshot.child("time_date_case").getValue()!=null && dataSnapshot.child("case_assigned").getValue()!=null)
                        {

                            disp_name = dataSnapshot.child("user_name").getValue().toString();
                            disp_user_image = dataSnapshot.child("image").getValue().toString();
                            unique_code = dataSnapshot.child("name").getValue().toString();
                            disp_uploaded_image = dataSnapshot.child("link").getValue().toString();
                            disp_case_type = dataSnapshot.child("type").getValue().toString();
                            disp_case_time = dataSnapshot.child("time_date_case").getValue().toString();
                            disp_case_assgn = dataSnapshot.child("case_assigned").getValue().toString();
                            // Toast.makeText(getApplicationContext(),list_user_id,Toast.LENGTH_SHORT).show();
                            if (dataSnapshot.hasChild("access") && checkAccess(dataSnapshot.child("access").getValue().toString())){
                                photoViewHolder.itemView.setVisibility(View.GONE);
                                RecyclerView.LayoutParams param = (RecyclerView.LayoutParams) photoViewHolder.itemView.getLayoutParams();
                                param.height = 0;
                                param.width = 0;
                                photoViewHolder.itemView.setLayoutParams(param);
                            }else{
                                if (dataSnapshot.hasChild("access")){
                                    photoViewHolder.access.setBackgroundColor(Color.RED);
                                    photoViewHolder.access.setTextColor(Color.WHITE);
                                    photoViewHolder.access.setText("Restricted");

                                }else{
                                    photoViewHolder.access.setBackgroundColor(Color.GREEN);
                                    photoViewHolder.access.setTextColor(Color.BLACK);
                                    photoViewHolder.access.setText("Open");
                                }


                                photoViewHolder.more_details.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(getActivity(), FullCaseViewActivity.class);
                                        intent.putExtra("post_id",list_user_id);
                                        startActivity(intent);
                                    }
                                });


                                photoViewHolder.comments.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent iint = new Intent(getActivity(), ReportComment.class);
                                        iint.putExtra("post_id",list_user_id);
                                        iint.putExtra("who_am_i",user_name);
                                        startActivity(iint);
                                    }
                                });
                                photoViewHolder.userNameView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        mPostsDatabase.child(list_user_id).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                if(dataSnapshot.child("name")!=null){

                                                    unique_code = dataSnapshot.child("name").getValue().toString();
                                                    Intent profileIntent = new Intent(getActivity(), ProfileActivityNakli.class);
                                                    profileIntent.putExtra("user_id",unique_code);
                                                    startActivity(profileIntent);
                                                }

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                                    }
                                });

                                photoViewHolder.pop.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mPostsDatabase.child(list_user_id).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                if( dataSnapshot.child("name")!=null && dataSnapshot.child("user_name")!=null){

                                                    unique_code = dataSnapshot.child("name").getValue().toString();
                                                    String usssr = dataSnapshot.child("user_name").getValue().toString();
                                                    if(unique_code.equalsIgnoreCase(mAuth.getCurrentUser().getUid())){

                                                        photoViewHolder.showPopup(list_user_id,firebaseRecyclerAdapter);}
                                                    else{

                                                        photoViewHolder.showCrop(usssr,list_user_id);
                                                    }

                                                }


                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                            }
                                        });
                                    }
                                });
                                photoViewHolder.setDisplayName(disp_name);

                                photoViewHolder.setUserImage(disp_user_image, context);
                                photoViewHolder.setUploadedImage(disp_uploaded_image, context);
                                photoViewHolder.upVote(list_user_id, mCurrentUser.getUid());
                                // int m= photoViewHolder.setlikes(list_user_id);
                                //  int up = photoViewHolder.getupVoteCount(list_user_id);
                                // int down = PhotoViewHolder.getDownVoteCount(list_user_id);
                                photoViewHolder.setLikes(list_user_id);
                                photoViewHolder.setTime(list_user_id);
                                photoViewHolder.setCasetype(disp_case_type);
                                photoViewHolder.setCaseTime(disp_case_time);
                                photoViewHolder.setCaseasgn(disp_case_assgn);
                                //      photoViewHolder.setDoubleTap(list_user_id,mCurrentUser.getUid());
                                //     photoViewHolder.setTry(list_user_id,mCurrentUser.getUid());

                            }



                        }

                        // this code I have added to remove if only link attribute is getting stored in postst table
                        else if(!dataSnapshot.hasChild("name")){
                            //  Toast.makeText(context, "Hi", Toast.LENGTH_SHORT).show();

                            mPostsDatabase.child(list_user_id).removeValue();




                        }

                        ////////////////////////////



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



                //final String user_id = getRef(position).getKey();

            }
        };


        mUsersList.setAdapter(firebaseRecyclerAdapter);
        FirebaseUser currentUser = mAuth.getCurrentUser();





        // mUserRef.child("online").setValue("true");

    }
    private boolean checkAccess(String access) {
        switch (user_type){
            case "D":
                return !access.startsWith(mCode);

            case "R":
                return !access.startsWith(mCode);

            default:
                return !access.equals(mCode);

        }



    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView userNameView;
        ImageView comments;
        ImageButton pop;
        TextView more_details;
        TextView casetype;
        TextView casetime;
        TextView caseasgn,access;


        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            comments = mView.findViewById(R.id.comment);
            userNameView = (TextView) mView.findViewById(R.id.user_single_name);
            pop = (ImageButton) mView.findViewById(R.id.popup);
            more_details = (TextView) mView.findViewById(R.id.view_full_case);
            casetype = (TextView) mView.findViewById(R.id.case_type);
            casetime = (TextView) mView.findViewById(R.id.case_time);
            caseasgn = (TextView) mView.findViewById(R.id.case_assgnd);
            access = mView.findViewById(R.id.access);
        }



        public void setDisplayName(String name) {

            userNameView.setText(name);


        }


        public void setUserImage(final String image, final Context ctx) {

            final CircleImageView userImageView = (CircleImageView) mView.findViewById(R.id.user_single_image);


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

        public void setUploadedImage(final String image, final Context ctx) {

            final ImageView userImageView = (ImageView) mView.findViewById(R.id.uploaded_image);


            Picasso.with(ctx).load(image).placeholder(R.drawable.loading).networkPolicy(NetworkPolicy.OFFLINE).into(userImageView, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {

                    Picasso.with(ctx).load(image).placeholder(R.drawable.loading).into(userImageView);
                }
            });


        }
        public void setCasetype(final String case_type)
        {
            casetype.setText(case_type);
        }
        public void setCaseTime(final String case_time)
        {
            casetime.setText(case_time);
        }
        public void setCaseasgn(final String case_assigned){

            if(case_assigned.toLowerCase().equals("no"))
            {
                caseasgn.setText("NO");
                caseasgn.setTextColor(Color.RED);
            }
            else if(case_assigned.toLowerCase().equals("yes"))
            {
                caseasgn.setText("YES");
                caseasgn.setTextColor(Color.GREEN);
            }

        }

        // upvote button set

        public void upVote(final String post_id, final String viewing_user){


            Log.d(post_id,viewing_user);
            final boolean[] downvote_check = {false};
            final boolean[] upvote_check = {false};

            final ImageView up_vote = (ImageView) mView.findViewById(R.id.upvote_icon);

            FirebaseDatabase.getInstance().getReference().child("UpVote").child(post_id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(dataSnapshot.hasChild(viewing_user)){
                        up_vote.setImageResource(R.drawable.arrow_colored);
                    }else{
                        up_vote.setImageResource(R.drawable.arrow_uncolored);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            up_vote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    FirebaseDatabase.getInstance().getReference().child("UpVote").child(post_id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if(dataSnapshot.hasChild(viewing_user)){
                                up_vote.setImageResource(R.drawable.arrow_uncolored);
                                FirebaseDatabase.getInstance().getReference().child("UpVote").child(post_id).child(viewing_user).removeValue();
                            }
                            else{


                                up_vote.setImageResource(R.drawable.arrow_colored);

                                FirebaseDatabase.getInstance().getReference().child("UpVote").child(post_id).child(viewing_user).setValue("1");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            });






        }





        public void setLikes(final String post_id) {

            final int[] upvotes = {0};
            final int[] downvotes = {0};
            final int likess=0;
            final TextView likes_view = (TextView) mView.findViewById(R.id.likes);
            final TextView cmtcount = (TextView) mView.findViewById(R.id.comment_count);
            FirebaseDatabase.getInstance().getReference().child("Comments").child(post_id).addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.getChildrenCount() > 0) {

                        cmtcount.setText(dataSnapshot.getChildrenCount() + "");  //yaha
                    }else {
                        cmtcount.setText("");


                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            FirebaseDatabase.getInstance().getReference().child("UpVote").child(post_id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    long i =dataSnapshot.getChildrenCount();
                    FirebaseDatabase.getInstance().getReference().child("Posts").child(post_id).child("likes").setValue(i);
                    likes_view.setText(""+i);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



        }

        public void setTime(String list_user_id) {
            FirebaseDatabase.getInstance().getReference().child("Posts").child(list_user_id).child("time").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Long h = dataSnapshot.getValue(Long.class);
                    GetTimeAgo getTimeAgo = new GetTimeAgo();
                    String lastSeenTime = getTimeAgo.getTimeAgo(h);
                    TextView ct = (TextView) mView.findViewById(R.id.Posttime);
                    ct.setText(lastSeenTime);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        public void setDoubleTap1(final String post_id, final String viewing_user) {
            final ImageView up_vote = (ImageView) mView.findViewById(R.id.upvote_icon);
            FirebaseDatabase.getInstance().getReference().child("UpVote").child(post_id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(dataSnapshot.hasChild(viewing_user)){
                        up_vote.setImageResource(R.drawable.arrow_uncolored);
                        FirebaseDatabase.getInstance().getReference().child("UpVote").child(post_id).child(viewing_user).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                up_vote.setEnabled(true);
                            }
                        });
                    }
                    else{


                        up_vote.setImageResource(R.drawable.arrow_colored);

                        FirebaseDatabase.getInstance().getReference().child("UpVote").child(post_id).child(viewing_user).setValue("1").addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                up_vote.setEnabled(true);
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        public void showPopup(final String list_user_id, final FirebaseRecyclerAdapter firebaseRecyclerAdapter) {
            PopupMenu popup = new PopupMenu(context,pop);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.actions, popup.getMenu());
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    if(item.getItemId() == R.id.delete){
                        firebaseRecyclerAdapter.getRef(getAdapterPosition()).removeValue();
                        firebaseRecyclerAdapter.notifyItemChanged(getAdapterPosition());
                        FirebaseDatabase.getInstance().getReference().child("UpVote").child(list_user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                FirebaseDatabase.getInstance().getReference().child("Posts").child(list_user_id).removeValue();
                                FirebaseDatabase.getInstance().getReference().child("Comments").child(list_user_id).removeValue();


                            }
                        });


                    }

                    return true;
                }
            });
            popup.show();
        }

        public void showCrop(final String listUserId, final String list_user_id) {
            PopupMenu popup = new PopupMenu(context,pop);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.report, popup.getMenu());
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    if(item.getItemId() == R.id.report){
                        ViewDialog alert = new ViewDialog();
                        alert.showDialog(myac,listUserId,list_user_id,list_user_id);

                    }

                    return true;
                }
            });
            popup.show();


        }
    }
}


