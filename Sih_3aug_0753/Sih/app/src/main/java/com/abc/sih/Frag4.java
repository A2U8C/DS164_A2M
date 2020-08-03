package com.abc.sih;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.abc.sih.Reports.ReportComment;
import com.abc.sih.Reports.ReportComp;
import com.abc.sih.Reports.Reports;
import com.abc.sih.Reports.ViewDialog;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.plotprojects.retail.android.Plot;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import at.markushi.ui.CircleButton;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static org.webrtc.ContextUtils.getApplicationContext;


public class Frag4 extends Fragment {
    LinearLayout personalinfo, experience, review;
    TextView personalinfobtn, experiencebtn, reviewbtn;
    DatabaseReference user_data;
    String user_name,user_image,user_status,user_about,user_phone,user_email,user_posting;
    String user_res1,user_res2,user_res3;
    String user_post1,user_post2,user_post3;
    String user_post1_descp,user_post2_descp,user_post3_descp;
    String user_post1_time,user_post2_time,user_post3_time;
    private static final int GALLERY_PICK = 1;
    private ProgressDialog mProgressDialog;
    private StorageReference mImageStorage;
    private DatabaseReference mUserDatabase;

    private RecyclerView mUsersList;
    private LinearLayoutManager mLayoutManager;
    private DatabaseReference mPostsDatabase;
    private FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    private String disp_name, disp_user_image, disp_uploaded_image, disp_likes,disp_case_type,disp_case_time,disp_case_assgn;
    private String unique_code;
    private FirebaseAuth mAuth;


    private static Activity myac;


    public Frag4() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_frag4, container, false);
//////////////////////////////////////
        mImageStorage = FirebaseStorage.getInstance().getReference();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mUserDatabase.keepSynced(true);
        final TextView name_of_user = (TextView) view.findViewById(R.id.user_name);
        final CircleImageView image_of_user = (CircleImageView) view.findViewById(R.id.user_image);
        final TextView status_of_user = (TextView) view.findViewById(R.id.user_status);
        final TextView about_edit = (TextView) view.findViewById(R.id.edit1);
        final TextView about_of_user = (TextView) view.findViewById(R.id.user_about);
        final TextView phone_of_user = (TextView) view.findViewById(R.id.user_phone);
        final TextView email_of_user = (TextView) view.findViewById(R.id.user_email);
        final TextView posting_of_user = (TextView) view.findViewById(R.id.user_posting_location);
        final TextView contact_details_edit = (TextView) view.findViewById(R.id.edit2);
        final TextView res1_of_user = (TextView) view.findViewById(R.id.user_res1);
        final TextView res2_of_user = (TextView) view.findViewById(R.id.user_res2);
        final TextView res3_of_user = (TextView) view.findViewById(R.id.user_res3);
        final TextView resource_edit = (TextView) view.findViewById(R.id.edit3);
        final TextView post1_user = (TextView) view.findViewById(R.id.user_post1);
        final TextView post1_descp_user = (TextView) view.findViewById(R.id.user_post1_descp);
        final TextView post1_time_user = (TextView) view.findViewById(R.id.user_post1_time);

        final TextView post2_user = (TextView) view.findViewById(R.id.user_post2);
        final TextView post2_descp_user = (TextView) view.findViewById(R.id.user_post2_descp);
        final TextView post2_time_user = (TextView) view.findViewById(R.id.user_post2_time);

        final TextView post3_user = (TextView) view.findViewById(R.id.user_post3);
        final TextView post3_descp_user = (TextView) view.findViewById(R.id.user_post3_descp);
        final TextView post3_time_user = (TextView) view.findViewById(R.id.user_post3_time);


        mAuth = FirebaseAuth.getInstance();

        ImageView settings = (ImageView) view.findViewById(R.id.toolbar_image);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),SettingsActivity.class);
                startActivity(intent);
            }
        });

       // final CircleButton update_photo = (CircleButton) view.findViewById(R.id.update_photo);
///////////////////////////        ////////
        personalinfo = view.findViewById(R.id.personalinfo);
        experience = view.findViewById(R.id.experience);
        review = view.findViewById(R.id.review);
        personalinfobtn = view.findViewById(R.id.personalinfobtn);
        experiencebtn = view.findViewById(R.id.experiencebtn);
        reviewbtn = view.findViewById(R.id.reviewbtn);
        /*making personal info visible*/
        personalinfo.setVisibility(View.VISIBLE);
        experience.setVisibility(View.GONE);
        review.setVisibility(View.GONE);
        String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        user_data = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        user_data.keepSynced(true);
        //Toast.makeText(getActivity(),user_id,Toast.LENGTH_SHORT).show();
        ////////////////////////////////////////update profile photo//////////////////////////////////////////////////////
       /* update_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");

                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PICK);
            }
        });

*/

//////////about edit section//////////////////////
        about_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder myDialog= new AlertDialog.Builder(getContext());

                LayoutInflater inflater= LayoutInflater.from(getContext());

                final View myview= inflater.inflate(R.layout.about_edit,null);

                myDialog.setView(myview);

                final AlertDialog dialog=myDialog.create();
                final EditText title= myview.findViewById(R.id.edt_about);
                user_data.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String old_about = dataSnapshot.child("about_me").getValue().toString();

                        title.setText(old_about);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                Button btnSave= myview.findViewById(R.id.btn_save);

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.KITKAT)
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onClick(View v) {

                        String mTitle=title.getText().toString().trim();


                        if(TextUtils.isEmpty(mTitle))
                        {
                            title.setError("Required Field..");
                            return;
                        }
                        final Date currentTime = Calendar.getInstance().getTime();

                        user_data.child("about_me"+currentTime).setValue(mTitle);
                        // tracking records

                        user_data.child("about_me").setValue(mTitle);

                        Toast.makeText(getContext(), "Data Inserted", Toast.LENGTH_SHORT).show();

                        dialog.dismiss();

                    }
                });

                dialog.show();
            }
        });

        ////////////////
        ///////contact details edit section///////////////////
        contact_details_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder myDialog= new AlertDialog.Builder(getContext());

                LayoutInflater inflater= LayoutInflater.from(getContext());

                final View myview= inflater.inflate(R.layout.contact_details_edit,null);

                myDialog.setView(myview);

                final AlertDialog dialog=myDialog.create();
                final EditText phone= myview.findViewById(R.id.edt_phone);
                final EditText email = myview.findViewById(R.id.edt_email);
                final EditText posting = myview.findViewById(R.id.edt_posting);
                user_data.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String old_phone = dataSnapshot.child("phone_number").getValue().toString();
                        String old_email = dataSnapshot.child("email").getValue().toString();
                        String old_posting = dataSnapshot.child("posting_location").getValue().toString();

                        phone.setText(old_phone);
                        email.setText(old_email);
                        posting.setText(old_posting);


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                Button btnSave= myview.findViewById(R.id.btn_save);

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.KITKAT)
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onClick(View v) {

                        String mPhone=phone.getText().toString().trim();
                        String mEmail = email.getText().toString().trim();
                        String mPosting = posting.getText().toString().trim();


                        if(TextUtils.isEmpty(mPhone))
                        {
                            phone.setError("Enter Phone number");
                            return;
                        }
                        if(TextUtils.isEmpty(mEmail))
                        {
                            phone.setError("Enter Email");
                            return;
                        }if(TextUtils.isEmpty(mPosting))
                        {
                            phone.setError("Enter Posting Location");
                            return;
                        }


                        user_data.child("phone_number").setValue(mPhone);
                        user_data.child("email").setValue(mEmail);
                        user_data.child("posting_location").setValue(mPosting);

                        Toast.makeText(getContext(), "Data Inserted", Toast.LENGTH_SHORT).show();

                        dialog.dismiss();

                    }
                });

                dialog.show();
            }
        });

        ///////////////
        ////////resource edit section//////////
        resource_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder myDialog= new AlertDialog.Builder(getContext());

                LayoutInflater inflater= LayoutInflater.from(getContext());

                final View myview= inflater.inflate(R.layout.resource_edit,null);

                myDialog.setView(myview);

                final AlertDialog dialog=myDialog.create();
                final EditText res1= myview.findViewById(R.id.edt_res1);
                final EditText res2 = myview.findViewById(R.id.edt_res2);
                final EditText res3 = myview.findViewById(R.id.edt_res3);
                user_data.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String old_phone = dataSnapshot.child("resource1").getValue().toString();
                        String old_email = dataSnapshot.child("resource2").getValue().toString();
                        String old_posting = dataSnapshot.child("resource3").getValue().toString();

                        res1.setText(old_phone);
                        res2.setText(old_email);
                        res3.setText(old_posting);


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                Button btnSave= myview.findViewById(R.id.btn_save);

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.KITKAT)
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onClick(View v) {

                        String mRes1=res1.getText().toString().trim();
                        String mRes2 = res2.getText().toString().trim();
                        String mRes3 = res3.getText().toString().trim();


                        if(TextUtils.isEmpty(mRes1))
                        {
                            res1.setError("Enter Resource Name. \n Enter None for no resource");
                            return;
                        }
                        if(TextUtils.isEmpty(mRes2))
                        {
                            res2.setError("Enter Resource Name. \n Enter None for no resource");
                            return;
                        }if(TextUtils.isEmpty(mRes3))
                        {
                            res3.setError("Enter Resource Name. \n Enter None for no resource");
                            return;
                        }


                        user_data.child("resource1").setValue(mRes1);
                        user_data.child("resource2").setValue(mRes2);
                        user_data.child("resource3").setValue(mRes3);

                        Toast.makeText(getContext(), "Data Inserted", Toast.LENGTH_SHORT).show();

                        dialog.dismiss();

                    }
                });

                dialog.show();
            }
        });
        /////////////////

        user_data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())
                {
                    user_name = dataSnapshot.child("name").getValue().toString();
                    user_image = dataSnapshot.child("image").getValue().toString();
                    user_status = dataSnapshot.child("status").getValue().toString();
                    user_about = dataSnapshot.child("about_me").getValue().toString();
                    user_phone = dataSnapshot.child("phone_number").getValue().toString();
                    user_email = dataSnapshot.child("email").getValue().toString();
                    user_posting = dataSnapshot.child("posting_location").getValue().toString();
                    user_res1 = dataSnapshot.child("resource1").getValue().toString();
                    user_res2 = dataSnapshot.child("resource2").getValue().toString();
                    user_res3 = dataSnapshot.child("resource3").getValue().toString();

                    user_post1 = dataSnapshot.child("user_post1").getValue().toString();
                    user_post1_descp = dataSnapshot.child("user_post1_descp").getValue().toString();
                    user_post1_time = dataSnapshot.child("user_post1_time").getValue().toString();

                    user_post2 = dataSnapshot.child("user_post2").getValue().toString();
                    user_post2_descp = dataSnapshot.child("user_post2_descp").getValue().toString();
                    user_post2_time = dataSnapshot.child("user_post2_time").getValue().toString();

                    user_post3 = dataSnapshot.child("user_post3").getValue().toString();
                    user_post3_descp = dataSnapshot.child("user_post3_descp").getValue().toString();
                    user_post3_time = dataSnapshot.child("user_post3_time").getValue().toString();



                    name_of_user.setText(user_name);

                    Picasso.with(getContext()).load(user_image).placeholder(R.drawable.default_avatar).networkPolicy(NetworkPolicy.OFFLINE).into(image_of_user, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {

                            Picasso.with(getContext()).load(user_image).placeholder(R.drawable.default_avatar).into(image_of_user);
                        }
                    });
                    status_of_user.setText(user_status);
                    about_of_user.setText(user_about);
                    phone_of_user.setText(user_phone);
                    email_of_user.setText(user_email);
                    posting_of_user.setText(user_posting);
                    res1_of_user.setText(user_res1);
                    res2_of_user.setText(user_res2);
                    res3_of_user.setText(user_res3);

                    post1_user.setText(user_post1);
                    post1_descp_user.setText(user_post1_descp);
                    post1_time_user.setText(user_post1_time);

                    post2_user.setText(user_post2);
                    post2_descp_user.setText(user_post2_descp);
                    post2_time_user.setText(user_post2_time);

                    post3_user.setText(user_post3);
                    post3_descp_user.setText(user_post3_descp);
                    post3_time_user.setText(user_post3_time);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        personalinfobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                personalinfo.setVisibility(View.VISIBLE);
                experience.setVisibility(View.GONE);
                review.setVisibility(View.GONE);
                personalinfobtn.setTextColor(getResources().getColor(R.color.blue));
                experiencebtn.setTextColor(getResources().getColor(R.color.grey));
                reviewbtn.setTextColor(getResources().getColor(R.color.grey));

            }
        });

        experiencebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                personalinfo.setVisibility(View.GONE);
                experience.setVisibility(View.VISIBLE);
                review.setVisibility(View.GONE);
                personalinfobtn.setTextColor(getResources().getColor(R.color.grey));
                experiencebtn.setTextColor(getResources().getColor(R.color.blue));
                reviewbtn.setTextColor(getResources().getColor(R.color.grey));

            }
        });

        reviewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                personalinfo.setVisibility(View.GONE);
                experience.setVisibility(View.GONE);
                review.setVisibility(View.VISIBLE);
                personalinfobtn.setTextColor(getResources().getColor(R.color.grey));
                experiencebtn.setTextColor(getResources().getColor(R.color.grey));
                reviewbtn.setTextColor(getResources().getColor(R.color.blue));

            }
        });
        mPostsDatabase = FirebaseDatabase.getInstance().getReference().child("Posts");
        mPostsDatabase.keepSynced(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        mUsersList = (RecyclerView) view.findViewById(R.id.recvPost);
        mUsersList.setHasFixedSize(true);
        mUsersList.setLayoutManager(mLayoutManager);
        postSection();
        return view;
    }
    private void postSection() {
        final FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Query ftp = mPostsDatabase.orderByChild("name").equalTo(user_id);
        //  Query mms = ftp.orderByChild("time");
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Reports, Frag4.PhotoViewHolder>(
                Reports.class,
                R.layout.single_report_item_nakli,
                Frag4.PhotoViewHolder.class,
                ftp

        ) {
            @Override
            protected void populateViewHolder(final Frag4.PhotoViewHolder photoViewHolder, Reports users, int position) {

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
                            disp_case_type = dataSnapshot.child("type").getValue().toString();
                            disp_case_time = dataSnapshot.child("time_date_case").getValue().toString();
                            disp_case_assgn = dataSnapshot.child("case_assigned").getValue().toString();
                            // Toast.makeText(getApplicationContext(),list_user_id,Toast.LENGTH_SHORT).show();


                            photoViewHolder.more_details.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getContext(), FullCaseViewActivity.class);
                                    intent.putExtra("post_id",list_user_id);
                                    startActivity(intent);
                                }
                            });


                            photoViewHolder.comments.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent iint = new Intent(getContext(), ReportComment.class);
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

                                                    photoViewHolder.showPopup(getActivity(),list_user_id,firebaseRecyclerAdapter);}
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

                            photoViewHolder.setCasetype(disp_case_type);
                            photoViewHolder.setCaseTime(disp_case_time);
                            photoViewHolder.setCaseasgn(disp_case_assgn);
                            //  mProgressDialog.dismiss();

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
        TextView more_details;
        TextView casetype;
        TextView casetime;
        TextView caseasgn;
        android.content.res.Resources res;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;
            pop = (ImageButton) mView.findViewById(R.id.popup);
            comments = mView.findViewById(R.id.comment);
            userImageView = (CircleImageView) mView.findViewById(R.id.user_single_image);
            more_details = (TextView) mView.findViewById(R.id.view_full_case);
            casetype = (TextView) mView.findViewById(R.id.case_type);
            casetime = (TextView) mView.findViewById(R.id.case_time);
            caseasgn = (TextView) mView.findViewById(R.id.case_assgnd);

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
        public void showPopup(Context ctx, final String list_user_id, final FirebaseRecyclerAdapter firebaseRecyclerAdapter) {

            PopupMenu popup = new PopupMenu(ctx,pop);
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
            PopupMenu popup = new PopupMenu(getApplicationContext(),pop);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {

            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                     .setAspectRatio(1, 1)
                    .start(getActivity());


        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                Log.d("anish_star","inside here");
                mProgressDialog = new ProgressDialog(getContext());
                mProgressDialog.setTitle("Uploading Image..");
                mProgressDialog.setMessage("Please wait while we upload and process the image");
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.show();

                Uri resultUri = result.getUri();

                final String current_user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                final Date currentTime = Calendar.getInstance().getTime();

                StorageReference filepath = mImageStorage.child("posts").child(current_user_id + ".jpg");

                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if(task.isSuccessful()){
                            //Toast.makeText(SettingsActivity.this, "Working", Toast.LENGTH_SHORT).show();

                            //final String download_url= task.getResult().toString();

                            /////////////////

                            mImageStorage.child("posts").child(current_user_id+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    String download_url = uri.toString();

                                    mUserDatabase.child(current_user_id).child("image").setValue(download_url).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if(task.isSuccessful()){

                                                mProgressDialog.dismiss();
                                                Toast.makeText(getContext(),"Successfully uploaded", Toast.LENGTH_SHORT).show();





                                                /////


                                                //Picasso.with(SettingsActivity.this).load(download_url).into(mDisplayImage);
                                            }

                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                }
                            });
                            //////////////////



                        }else{

                            Toast.makeText(getContext(), "Error in uploading", Toast.LENGTH_SHORT).show();
                            mProgressDialog.dismiss();
                        }
                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
