package com.abc.sih;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abc.sih.Reports.ReportComment;
import com.abc.sih.Reports.Reports;
import com.abc.sih.Reports.ViewDialog;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private RecyclerView mUsersList;
    private LinearLayoutManager mLayoutManager;
    private String user_name;
    private ImageView mProfileImage;
    private TextView mProfileName,mProfileStatus, mProfileFriendsCount,mPosts_count,mLikes_count;

    private DatabaseReference mPostsDatabase;

    private DatabaseReference mUsersDatabase;

    private ProgressDialog mProgressDialog;

    private DatabaseReference mFriendReqDatabase;
    private DatabaseReference mFriendDatabase;
    private DatabaseReference mNotificationDatabase;
    private FirebaseUser mCurrent_user;
    private String disp_name, disp_user_image, disp_uploaded_image, disp_likes;
    private FirebaseAuth mAuth;

    private int countFriends = 0;
    private RelativeLayout k1;
    private String pass;
    private String user_id;
    String image;
    static Context context;
    private String unique_code;
    private FirebaseRecyclerAdapter firebaseRecyclerAdapter;

    private static Activity myac;
    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    @Override
    protected void onStart() {
        super.onStart();
        context=ProfileActivity.this;
        myac = ProfileActivity.this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        user_id = getIntent().getStringExtra("user_id");

        setPass(user_id);
        mAuth = FirebaseAuth.getInstance();
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);

        mFriendReqDatabase = FirebaseDatabase.getInstance().getReference().child("Friend_req");
        mFriendDatabase = FirebaseDatabase.getInstance().getReference().child("Friends");
        //  mNotificationDatabase = FirebaseDatabase.getInstance().getReference().child("notifications");
        mCurrent_user = FirebaseAuth.getInstance().getCurrentUser();

        mProfileImage = (ImageView) findViewById(R.id.profile_image);
        mProfileName = (TextView) findViewById(R.id.profile_displayName);
        mProfileStatus = (TextView) findViewById(R.id.profile_status);
        mProfileFriendsCount = (TextView) findViewById(R.id.profile_totalFriends);



        mPostsDatabase = FirebaseDatabase.getInstance().getReference().child("Posts");

        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "hi", Toast.LENGTH_SHORT).show();
                final AlertDialog.Builder myDialog = new AlertDialog.Builder(ProfileActivity.this);

                LayoutInflater inflater = LayoutInflater.from(ProfileActivity.this);

                final View myview = inflater.inflate(R.layout.zoom_image,null);
                myDialog.setView(myview);

                final AlertDialog dialog = myDialog.create();

                final ImageView zoom_img = myview.findViewById(R.id.zoomed);
                Picasso.with(getApplicationContext()).load(image).placeholder(R.drawable.default_avatar)
                        .networkPolicy(NetworkPolicy.OFFLINE).into(zoom_img, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                        Picasso.with(getApplicationContext()).load(image).placeholder(R.drawable.default_avatar).into(zoom_img);
                    }
                });
                dialog.show();



            }
        });

        ////progress dialog




        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Loading User Data");
        mProgressDialog.setMessage("Please wait while we load the data!");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();


        mLayoutManager = new LinearLayoutManager(ProfileActivity.this);
        mUsersList = (RecyclerView) findViewById(R.id.recvPost);
        mUsersList.setHasFixedSize(true);
        mUsersList.setLayoutManager(mLayoutManager);
        FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    user_name = dataSnapshot.child("name").getValue().toString();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        postSection();
        ///////////// isse total friends count milega//////////


        ///////




        //////



        ///////


        /////////
        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String display_name = dataSnapshot.child("name").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                image = dataSnapshot.child("image").getValue().toString();

                mProfileName.setText(display_name);
                mProfileStatus.setText(status);

                Picasso.with(ProfileActivity.this).load(image).placeholder(R.drawable.default_avatar)
                        .networkPolicy(NetworkPolicy.OFFLINE).into(mProfileImage, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                        Picasso.with(ProfileActivity.this).load(image).placeholder(R.drawable.default_avatar).into(mProfileImage);
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }

    private void postSection() {
        final FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        Query ftp = mPostsDatabase.orderByChild("name").equalTo(user_id);
        //  Query mms = ftp.orderByChild("time");
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Reports, ProfileActivity.PhotoViewHolder>(
                Reports.class,
                R.layout.single_report_item,
                ProfileActivity.PhotoViewHolder.class,
                ftp

        ) {
            @Override
            protected void populateViewHolder(final ProfileActivity.PhotoViewHolder photoViewHolder, Reports users, int position) {

                final String list_user_id = getRef(position).getKey();


                mPostsDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.child("name").getValue()!=null && dataSnapshot.child("image").getValue() !=null &&
                                dataSnapshot.child("link").getValue() !=null)
                        {

                            disp_name = dataSnapshot.child("user_name").getValue().toString();
                            disp_user_image = dataSnapshot.child("image").getValue().toString();
                            disp_uploaded_image = dataSnapshot.child("link").getValue().toString();
                            // Toast.makeText(getApplicationContext(),list_user_id,Toast.LENGTH_SHORT).show();




                            photoViewHolder.comments.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent iint = new Intent(ProfileActivity.this, ReportComment.class);
                                    iint.putExtra("post_id",list_user_id);
                                    iint.putExtra("who_am_i",user_name);
                                    startActivity(iint);
                                }
                            });

                            photoViewHolder.setDisplayName(disp_name);
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
                            photoViewHolder.setUserImage(disp_user_image, getApplicationContext());
                            photoViewHolder.setUploadedImage(disp_uploaded_image, getApplicationContext());
                            photoViewHolder.upVote(list_user_id, mCurrentUser.getUid());
                            // int m= photoViewHolder.setlikes(list_user_id);
                            //  int up = photoViewHolder.getupVoteCount(list_user_id);
                            // int down = PhotoViewHolder.getDownVoteCount(list_user_id);
                            photoViewHolder.setLikes(list_user_id);
                            photoViewHolder.setTime(list_user_id);
                            mProgressDialog.dismiss();

                            //      photoViewHolder.setDoubleTap(list_user_id,mCurrentUser.getUid());
                            //     photoViewHolder.setTry(list_user_id,mCurrentUser.getUid());


                        }



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
    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        View mView;
        ImageButton pop;
        ImageView comments;
        CircleImageView userImageView;
        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;
            pop = (ImageButton) mView.findViewById(R.id.popup);
            comments = mView.findViewById(R.id.comment);
            userImageView = (CircleImageView) mView.findViewById(R.id.user_single_image);
        }



        public void setDisplayName(String name) {
            TextView userNameView = (TextView) mView.findViewById(R.id.user_single_name);
            userNameView.setText(name);

        }


        public void setUserImage(String image, Context ctx) {


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

        public void setUploadedImage(String image, Context ctx) {

            ImageView userImageView = (ImageView) mView.findViewById(R.id.uploaded_image);
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
                        up_vote.setImageResource(R.drawable.filled_heart);
                    }else{
                        up_vote.setImageResource(R.drawable.heart);
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
                                up_vote.setImageResource(R.drawable.heart);
                                FirebaseDatabase.getInstance().getReference().child("UpVote").child(post_id).child(viewing_user).removeValue();
                            }
                            else{


                                up_vote.setImageResource(R.drawable.filled_heart);

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
            final TextView cmtcount = (TextView) mView.findViewById(R.id.comment_count);
            final TextView likes_view = (TextView) mView.findViewById(R.id.likes);
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
                    ;
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
                        up_vote.setImageResource(R.drawable.heart);
                        FirebaseDatabase.getInstance().getReference().child("UpVote").child(post_id).child(viewing_user).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                up_vote.setEnabled(true);
                            }
                        });
                    }
                    else{


                        up_vote.setImageResource(R.drawable.filled_heart);

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
