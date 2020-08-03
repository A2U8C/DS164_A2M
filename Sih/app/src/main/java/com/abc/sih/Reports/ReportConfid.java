package com.abc.sih.Reports;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.abc.sih.FullCaseViewActivity;
import com.abc.sih.GetTimeAgo;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReportConfid extends AppCompatActivity {
    private RecyclerView mUsersList;

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
    private ImageView upvote, downvote;
    private String current_uid;
    private DatabaseReference mPostsDatabase;
    List<String> utt;
    private String download_url;
    private String user_name, user_dp,user_station,user_type;
    private LinearLayoutManager mLayoutManager;
    private Context ctx;
    private String unique_code;
    private FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    private static Activity myac;
    View view;
    static Context context;
    private String mUserId,mCode;
    private List<String>  mStringList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_confid);
        mAuth = FirebaseAuth.getInstance();
        mImageStorage = FirebaseStorage.getInstance().getReference();
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        mStringList= new ArrayList<String>();
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        mUserDatabase.keepSynced(true);
        mPostsDatabase = FirebaseDatabase.getInstance().getReference().child("Posts");
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists() && dataSnapshot.child("name").getValue()!=null && dataSnapshot.child("image").getValue()!= null) {
                    user_name = dataSnapshot.child("name").getValue().toString();
                    user_dp = dataSnapshot.child("image").getValue().toString();
                    user_station = dataSnapshot.child("station").getValue().toString();
                    user_type = Objects.requireNonNull(dataSnapshot.child("type").getValue()).toString();
                    switch (user_type){
                        case "D":
                            mCode = dataSnapshot.child("Division").getValue().toString();
                            startRange();
                            break;
                        case "R":
                            String a = dataSnapshot.child("Division").getValue().toString();
                            String b =dataSnapshot.child("Range").getValue().toString();
                            mCode = a+" "+b;
                            startRange();
                            break;
                        default:
                            String a1 = dataSnapshot.child("Division").getValue().toString();
                            String b1 =dataSnapshot.child("Range").getValue().toString();
                            String c = dataSnapshot.child("Beat").getValue().toString();
                            mCode = a1+ " "+ b1 + " "+c;
                            startSection();
                            break;

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        String current_uid = mCurrentUser.getUid();
        mLayoutManager = new LinearLayoutManager(ReportConfid.this);
        mLayoutManager.setReverseLayout(true); // THIS ALSO SETS setStackFromBottom to true
        mLayoutManager.setStackFromEnd(true);

        mUsersList = (RecyclerView) findViewById(R.id.users_list);
        mUsersList.setHasFixedSize(true);
        mUsersList.setLayoutManager(mLayoutManager);



    }


    @Override
    public void onStart() {
        super.onStart();

        //Query ftp = mUsersDatabase.orderByChild("participated").equalTo("yes");



    }

    private void startSection() {
        Query ftp = mPostsDatabase.orderByChild("access").equalTo(mCode);//.limitToLast(10);

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Reports, StartReport.PhotoViewHolder>(
                Reports.class,
                R.layout.single_report_item_nakli,
                StartReport.PhotoViewHolder.class,
                ftp

        ) {
            @Override
            protected void populateViewHolder(final StartReport.PhotoViewHolder photoViewHolder, Reports users, int position) {

                final String list_user_id = getRef(position).getKey();


                mPostsDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.child("name").getValue() != null && dataSnapshot.child("image").getValue() != null &&
                                dataSnapshot.child("link").getValue() != null && dataSnapshot.child("type").getValue()!=null &&
                                dataSnapshot.child("time_date_case").getValue()!=null && dataSnapshot.child("case_assigned").getValue()!=null) {

                            disp_name = dataSnapshot.child("user_name").getValue().toString();
                            unique_code = dataSnapshot.child("name").getValue().toString();

                            disp_user_image = dataSnapshot.child("image").getValue().toString();
                            disp_uploaded_image = dataSnapshot.child("link").getValue().toString();
                            disp_case_type = dataSnapshot.child("type").getValue().toString();
                            disp_case_time = dataSnapshot.child("time_date_case").getValue().toString();
                            disp_case_assgn = dataSnapshot.child("case_assigned").getValue().toString();


                                photoViewHolder.itemView.setVisibility(View.VISIBLE);

                                // Toast.makeText(getApplicationContext(),list_user_id,Toast.LENGTH_SHORT).show();
                                photoViewHolder.more_details.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(ReportConfid.this, FullCaseViewActivity.class);
                                        intent.putExtra("post_id", list_user_id);
                                        startActivity(intent);
                                    }
                                });

                                photoViewHolder.comments.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent iint = new Intent(ReportConfid.this, ReportComment.class);
                                        iint.putExtra("post_id", list_user_id);
                                        iint.putExtra("who_am_i", user_name);
                                        startActivity(iint);
                                    }
                                });
                                photoViewHolder.userNameView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        mPostsDatabase.child(list_user_id).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                unique_code = dataSnapshot.child("name").getValue().toString();
                                                //Intent profileIntent = new Intent(getActivity(), ProfileActivity.class);
                                                Intent profileIntent = new Intent(ReportConfid.this, ProfileActivityNakli.class);
                                                profileIntent.putExtra("user_id", unique_code);
                                                startActivity(profileIntent);
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

                                                if (dataSnapshot.child("name") != null && dataSnapshot.child("user_name") != null) {
                                                    unique_code = dataSnapshot.child("name").getValue().toString();
                                                    String usssr = dataSnapshot.child("user_name").getValue().toString();
                                                    if (unique_code.equalsIgnoreCase(mAuth.getCurrentUser().getUid())) {

                                                        photoViewHolder.showPopup(list_user_id, firebaseRecyclerAdapter);
                                                    } else {

                                                        photoViewHolder.showCrop(usssr, list_user_id);
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
                                //   photoViewHolder.downVote(list_user_id, mCurrentUser.getUid());
                                // int m= photoViewHolder.setlikes(list_user_id);
                                //  int up = photoViewHolder.getupVoteCount(list_user_id);
                                // int down = PhotoViewHolder.getDownVoteCount(list_user_id);
                                photoViewHolder.setLikes(list_user_id);
                                photoViewHolder.setTime(list_user_id);
                                photoViewHolder.setCasetype(disp_case_type);
                                photoViewHolder.setCaseTime(disp_case_time);
                                photoViewHolder.setCaseasgn(disp_case_assgn);





                           /* ItemClickSupport.addTo(mRecyclerView)
                                    .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                                        @Override
                                        public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                                            Log.d("ITEM CLICK", "Item single clicked " + mRecyclerViewAdapter.getItemAt(position).getProductId());
                                        }

                                        @Override
                                        public void onItemDoubleClicked(RecyclerView recyclerView, int position, View v) {
                                            Log.d("ITEM CLICK", "Item double clicked " + mRecyclerViewAdapter.getItemAt(position).getProductId());
                                        }
                                    });
*/

                        }
                        //this code I have addede to remove if only link is stored in posts table
                        else if(!dataSnapshot.hasChild("name"))
                        {

                            mPostsDatabase.child(list_user_id).removeValue();
                        }

                        /////////////


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
    private void startRange() {
        Query ftp = mPostsDatabase.orderByChild("access").startAt(mCode).endAt(mCode+"\uf8ff");//.limitToLast(10);

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Reports, StartReport.PhotoViewHolder>(
                Reports.class,
                R.layout.single_report_item_nakli,
                StartReport.PhotoViewHolder.class,
                ftp

        ) {
            @Override
            protected void populateViewHolder(final StartReport.PhotoViewHolder photoViewHolder, Reports users, int position) {

                final String list_user_id = getRef(position).getKey();


                mPostsDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.child("name").getValue() != null && dataSnapshot.child("image").getValue() != null &&
                                dataSnapshot.child("link").getValue() != null && dataSnapshot.child("type").getValue()!=null &&
                                dataSnapshot.child("time_date_case").getValue()!=null && dataSnapshot.child("case_assigned").getValue()!=null) {

                            disp_name = dataSnapshot.child("user_name").getValue().toString();
                            unique_code = dataSnapshot.child("name").getValue().toString();

                            disp_user_image = dataSnapshot.child("image").getValue().toString();
                            disp_uploaded_image = dataSnapshot.child("link").getValue().toString();
                            disp_case_type = dataSnapshot.child("type").getValue().toString();
                            disp_case_time = dataSnapshot.child("time_date_case").getValue().toString();
                            disp_case_assgn = dataSnapshot.child("case_assigned").getValue().toString();



                                photoViewHolder.itemView.setVisibility(View.VISIBLE);

                                // Toast.makeText(getApplicationContext(),list_user_id,Toast.LENGTH_SHORT).show();
                                photoViewHolder.more_details.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(ReportConfid.this, FullCaseViewActivity.class);
                                        intent.putExtra("post_id", list_user_id);
                                        startActivity(intent);
                                    }
                                });

                                photoViewHolder.comments.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent iint = new Intent(ReportConfid.this, ReportComment.class);
                                        iint.putExtra("post_id", list_user_id);
                                        iint.putExtra("who_am_i", user_name);
                                        startActivity(iint);
                                    }
                                });
                                photoViewHolder.userNameView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        mPostsDatabase.child(list_user_id).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                unique_code = dataSnapshot.child("name").getValue().toString();
                                                //Intent profileIntent = new Intent(getActivity(), ProfileActivity.class);
                                                Intent profileIntent = new Intent(ReportConfid.this, ProfileActivityNakli.class);
                                                profileIntent.putExtra("user_id", unique_code);
                                                startActivity(profileIntent);
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

                                                if (dataSnapshot.child("name") != null && dataSnapshot.child("user_name") != null) {
                                                    unique_code = dataSnapshot.child("name").getValue().toString();
                                                    String usssr = dataSnapshot.child("user_name").getValue().toString();
                                                    if (unique_code.equalsIgnoreCase(mAuth.getCurrentUser().getUid())) {

                                                        photoViewHolder.showPopup(list_user_id, firebaseRecyclerAdapter);
                                                    } else {

                                                        photoViewHolder.showCrop(usssr, list_user_id);
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
                                //   photoViewHolder.downVote(list_user_id, mCurrentUser.getUid());
                                // int m= photoViewHolder.setlikes(list_user_id);
                                //  int up = photoViewHolder.getupVoteCount(list_user_id);
                                // int down = PhotoViewHolder.getDownVoteCount(list_user_id);
                                photoViewHolder.setLikes(list_user_id);
                                photoViewHolder.setTime(list_user_id);
                                photoViewHolder.setCasetype(disp_case_type);
                                photoViewHolder.setCaseTime(disp_case_time);
                                photoViewHolder.setCaseasgn(disp_case_assgn);





                           /* ItemClickSupport.addTo(mRecyclerView)
                                    .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                                        @Override
                                        public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                                            Log.d("ITEM CLICK", "Item single clicked " + mRecyclerViewAdapter.getItemAt(position).getProductId());
                                        }

                                        @Override
                                        public void onItemDoubleClicked(RecyclerView recyclerView, int position, View v) {
                                            Log.d("ITEM CLICK", "Item double clicked " + mRecyclerViewAdapter.getItemAt(position).getProductId());
                                        }
                                    });
*/

                        }
                        //this code I have addede to remove if only link is stored in posts table
                        else if(!dataSnapshot.hasChild("name"))
                        {

                            mPostsDatabase.child(list_user_id).removeValue();
                        }

                        /////////////


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

        ImageView comments;
        TextView userNameView;
        ImageButton pop;
        TextView more_details;
        TextView casetype;
        TextView casetime;
        TextView caseasgn;
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

        public void upVote(final String post_id, final String viewing_user) {

            Log.d(post_id, viewing_user);
            final boolean[] downvote_check = {false};
            final boolean[] upvote_check = {false};

            final ImageView up_vote = (ImageView) mView.findViewById(R.id.upvote_icon);

            FirebaseDatabase.getInstance().getReference().child("UpVote").child(post_id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.hasChild(viewing_user)) {
                        up_vote.setImageResource(R.drawable.arrow_colored);
                    } else {
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
                    up_vote.setEnabled(false);

                    FirebaseDatabase.getInstance().getReference().child("UpVote").child(post_id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.hasChild(viewing_user)) {
                                up_vote.setImageResource(R.drawable.arrow_uncolored);
                                FirebaseDatabase.getInstance().getReference().child("UpVote").child(post_id).child(viewing_user).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        up_vote.setEnabled(true);
                                    }
                                });
                            } else {


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
            });

        }


        public void setLikes(final String post_id) {

            final int[] upvotes = {0};
            final int[] downvotes = {0};
            final int likess = 0;
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
                    long i = dataSnapshot.getChildrenCount();
                    FirebaseDatabase.getInstance().getReference().child("Posts").child(post_id).child("likes").setValue(i);
                    likes_view.setText("" + i);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

//            FirebaseDatabase.getInstance().getReference().child("DownVote").child(post_id).addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                    int k;
//                    int m;
//                    k=(int) dataSnapshot.getChildrenCount();
//                    m= Integer.valueOf(likes_view.getText().toString());
//                    likes_view.setText(String .valueOf(m-k));
//
//                    downvotes[0] = (int) dataSnapshot.getChildrenCount();
//                    Log.d("anish_d12b",String.valueOf((int)dataSnapshot.getChildrenCount()));
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });

            //Log.d("anish_nihal",String.valueOf(upvotes[0][0]-downvotes[0]));
            //likess = upvotes[0][0] - downvotes[0];
            //likes_view.setText("" +likess);


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

      /*  public void setDoubleTap(final String post_id, final String viewing_user) {
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
    }*/

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