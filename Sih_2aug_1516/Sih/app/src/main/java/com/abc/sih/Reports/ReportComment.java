package com.abc.sih.Reports;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.abc.sih.GetTimeAgo;
import com.abc.sih.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import at.markushi.ui.CircleButton;
import de.hdodenhof.circleimageview.CircleImageView;

public class ReportComment extends AppCompatActivity {

    private CircleImageView disp_image;
    private TextView disp_name;
    private TextView disp_time;
    TextView disp_case_type,disp_case_time,disp_case_assgnd;
    private ImageView uploaded_image,upvot_icon,downvote_icon,comments_icon;
    private  TextView likes;
    private DatabaseReference mPostsDatabase;
    private FirebaseAuth mAuth;
    private RecyclerView comment_list;
    private CircleButton comment_btn;
    private EditText comment_input;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mCommentsDatabase;
    private DatabaseReference mCmtDatabse;
    private String cmt,cmt_name,cmt_time,postid;
    private String unq_code;
    private Context ctx;
    private Activity myac;
    private FirebaseRecyclerAdapter firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_comment);
        final ImageView up_vote = (ImageView) findViewById(R.id.upvote_icon);

        final String post_id = getIntent().getStringExtra("post_id");
        final String me = getIntent().getStringExtra("who_am_i");
        disp_name = findViewById(R.id.user_single_name);
        disp_time = findViewById(R.id.postime);
        disp_image = findViewById(R.id.user_single_image);
        disp_case_type = findViewById(R.id.case_type);
        disp_case_time = findViewById(R.id.case_time);
        disp_case_assgnd = findViewById(R.id.case_assgnd);
        uploaded_image = findViewById(R.id.uploaded_image);

        comments_icon = findViewById(R.id.comment);

        comment_input = findViewById(R.id.comment_input);
        comment_btn = findViewById(R.id.type);
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        comment_list = (RecyclerView) findViewById(R.id.comments_list);
        comment_list.setHasFixedSize(true);
        comment_list.setLayoutManager(new LinearLayoutManager(this));


        // Toast.makeText(MemeComment.this, post_id, Toast.LENGTH_SHORT).show();
        mAuth = FirebaseAuth.getInstance();
        mPostsDatabase = FirebaseDatabase.getInstance().getReference().child("Posts");

        mCommentsDatabase = FirebaseDatabase.getInstance().getReference().child("Comments");
        mCmtDatabse = FirebaseDatabase.getInstance().getReference().child("Comments").child(post_id);
        mPostsDatabase.child(post_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    String meme_user_name = dataSnapshot.child("user_name").getValue().toString();
                    String meme_user_image = dataSnapshot.child("image").getValue().toString();
                    String meme_uploaded = dataSnapshot.child("link").getValue().toString();
                    String meme_case_type = dataSnapshot.child("type").getValue().toString();
                    String meme_case_time = dataSnapshot.child("time_date_case").getValue().toString();
                    String meme_case_assgnd = dataSnapshot.child("case_assigned").getValue().toString();

                    long likes = dataSnapshot.child("likes").getValue(long.class);
                    TextView jj = (TextView) findViewById(R.id.likes);
                    jj.setText(""+likes);
                    Long h = dataSnapshot.child("time").getValue(Long.class);
                    GetTimeAgo getTimeAgo = new GetTimeAgo();
                    String lastSeenTime = getTimeAgo.getTimeAgo(h);

                    disp_time.setText(lastSeenTime);
                    disp_name.setText(meme_user_name);
                    disp_case_type.setText(meme_case_type);
                    disp_case_time.setText(meme_case_time);
                    if(meme_case_assgnd.toLowerCase().equals("yes"))
                    {
                        disp_case_assgnd.setText("YES");
                        disp_case_assgnd.setTextColor(Color.GREEN);
                    }
                    else if(meme_case_assgnd.toLowerCase().equals("no"))
                    {
                        disp_case_assgnd.setText("NO");
                        disp_case_assgnd.setTextColor(Color.RED);
                    }
                    Picasso.with(getApplicationContext()).load(meme_user_image).placeholder(R.drawable.default_avatar).networkPolicy(NetworkPolicy.OFFLINE).into(disp_image, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {

                            Picasso.with(getApplicationContext()).load(meme_user_image).placeholder(R.drawable.default_avatar).into(disp_image);
                        }
                    });
                    //////////////////////////
                    Picasso.with(getApplicationContext()).load(meme_uploaded).placeholder(R.drawable.default_avatar).networkPolicy(NetworkPolicy.OFFLINE).into(uploaded_image, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {

                            Picasso.with(getApplicationContext()).load(meme_uploaded).placeholder(R.drawable.default_avatar).into(uploaded_image);
                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        String current_uid = mCurrentUser.getUid();

        FirebaseDatabase.getInstance().getReference().child("UpVote").child(post_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChild(mCurrentUser.getUid())){
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

                        if(dataSnapshot.hasChild(mCurrentUser.getUid())){
                            up_vote.setImageResource(R.drawable.arrow_uncolored);
                            FirebaseDatabase.getInstance().getReference().child("UpVote").child(post_id).child(mCurrentUser.getUid()).removeValue();
                        }
                        else{


                            up_vote.setImageResource(R.drawable.arrow_colored);

                            FirebaseDatabase.getInstance().getReference().child("UpVote").child(post_id).child(mCurrentUser.getUid()).setValue("1");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });


        comment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String cmt_input = comment_input.getText().toString();


                if(TextUtils.isEmpty(cmt_input))
                {
                    Toast.makeText(getApplicationContext(),"Enter Comment",Toast.LENGTH_SHORT).show();
                }

                else{
                    final String cmt_id = mCommentsDatabase.push().getKey();


                    mCommentsDatabase.child(post_id).child(cmt_id).child("user_id").setValue(me).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){

                                Log.d("anish_nnn","inside");

                                mCommentsDatabase.child(post_id).child(cmt_id).child("time").setValue(ServerValue.TIMESTAMP);
                                mCommentsDatabase.child(post_id).child(cmt_id).child("comment").setValue(cmt_input);

                                comment_input.setText("");
                            }else {

                                Toast.makeText(getApplicationContext(),"Some Error Occurred",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }



            }

        });//onclick of comment btn ends


    }/// oncreate ends here


    @Override
    protected void onStart() {
        super.onStart();

        //Query ftp = mUsersDatabase.orderByChild("participated").equalTo("yes");
        ctx=ReportComment.this;
        myac = ReportComment.this;
        FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                unq_code = dataSnapshot.child("name").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        postid = getIntent().getStringExtra("post_id");
        Query ftp = mCmtDatabse;
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ReportComments, ReportComment.PhotoViewHolder>(
                ReportComments.class,
                R.layout.single_comment_item,
                ReportComment.PhotoViewHolder.class,
                ftp

        ) {
            @Override
            protected void populateViewHolder(final ReportComment.PhotoViewHolder photoViewHolder, ReportComments users, int position) {

                final String list_user_id = getRef(position).getKey();


                mCmtDatabse.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.child("user_id").getValue()!=null && dataSnapshot.child("time").getValue() !=null &&
                                dataSnapshot.child("comment").getValue() !=null)
                        {


                            // Toast.makeText(getApplicationContext(),list_user_id,Toast.LENGTH_SHORT).show();



                            cmt_name = dataSnapshot.child("user_id").getValue().toString();
                            cmt_time = dataSnapshot.child("time").getValue().toString();
                            cmt = dataSnapshot.child("comment").getValue().toString();

                            photoViewHolder.pop.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {


                                    mCmtDatabse.child(list_user_id).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            if(dataSnapshot.exists() && dataSnapshot.child("user_id")!=null){

                                                cmt_name = dataSnapshot.child("user_id").getValue().toString();
                                                Log.d("cootmarika",unq_code+cmt_name);
                                                if(cmt_name.equalsIgnoreCase(unq_code)){

                                                    photoViewHolder.showPopup(ctx,mCmtDatabse,list_user_id,firebaseRecyclerAdapter);
                                                }
                                                else{

                                                    photoViewHolder.showCrop(myac,ctx,cmt_name,list_user_id,postid);
                                                }

                                            }


                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                        }
                                    });

                                }
                            });

                            photoViewHolder.setName(cmt_name, getApplicationContext());
                            photoViewHolder.setTime(cmt_time);
                            photoViewHolder.setCm(cmt);
                            // photoViewHolder.setUserImage(list_user_id,getApplicationContext());







                        }



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



                //final String user_id = getRef(position).getKey();

            }
        };
        comment_list.setAdapter(firebaseRecyclerAdapter);
        ViewCompat.setNestedScrollingEnabled(comment_list, false);
        FirebaseUser currentUser = mAuth.getCurrentUser();





    }


    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        View mView;
        CircleImageView cmt_user_dp;

        ImageButton pop;
        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;
            pop = (ImageButton) mView.findViewById(R.id.popup);
            //  cmt_user_dp = mView.findViewById(R.id.user_dp);

        }
        public void setUserImage(String uid,final Context ctx) {
            FirebaseDatabase.getInstance().getReference().child("Users").child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String image = dataSnapshot.child("image").getValue(String.class);
                    //   CircleImageView userImageView = (CircleImageView) mView.findViewById(R.id.user_dp);
                    //    Picasso.with(ctx).load(image).placeholder(R.drawable.default_avatar).into(userImageView);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }

        public void setName(String cmt_name, final Context ctx) {

            TextView cm_name = (TextView) mView.findViewById(R.id.user_name);
            cm_name.setText(cmt_name);

          /*  FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("name").equalTo(cmt_name)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if(dataSnapshot.exists() && dataSnapshot.child("image")!=null)
                            {
                                String bt = dataSnapshot.child("image").getValue().toString();

                                Picasso.with(ctx).load(bt).into(cmt_user_dp);
                            }



                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });*/

        }


        public void setTime(String cmt_time) {


            Long h = Long.parseLong(cmt_time);
            GetTimeAgo getTimeAgo = new GetTimeAgo();
            String lastSeenTime = getTimeAgo.getTimeAgo(h);
            TextView ct = (TextView) mView.findViewById(R.id.commentTime);
            ct.setText(lastSeenTime);


        }

        public void setCm(String cmt) {

            TextView cm_cm = (TextView) mView.findViewById(R.id.cmmt);
            cm_cm.setText(cmt);
        }

        public void showPopup(Context ctx, final DatabaseReference mCmtDatabse, final String list_user_id, final FirebaseRecyclerAdapter firebaseRecyclerAdapter) {
            Context wrapper = new ContextThemeWrapper(ctx, R.style.Popstyle);

            PopupMenu popup = new PopupMenu(wrapper,pop);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.actions, popup.getMenu());

            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    if(item.getItemId() == R.id.delete){
                        firebaseRecyclerAdapter.getRef(getAdapterPosition()).removeValue();
                        firebaseRecyclerAdapter.notifyItemChanged(getAdapterPosition());

                        mCmtDatabse.child(list_user_id).removeValue();


                    }

                    return true;
                }
            });
            popup.show();
        }

        public void showCrop(final Activity myac, Context ctx,  final String disp_name, final String list_user_id,final String postid) {
            PopupMenu popup = new PopupMenu(ctx,pop);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.report, popup.getMenu());
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    if(item.getItemId() == R.id.report){
                        ViewCommentDialog alert = new ViewCommentDialog();
                        alert.showDialog(myac,disp_name,list_user_id,postid);

                    }

                    return true;
                }
            });
            popup.show();

        }
    }

}





    /*@Override
    protected void onStart() {
        super.onStart();

        //Query ftp = mUsersDatabase.orderByChild("participated").equalTo("yes");

        Query ftp = mCmtDatabse;
        FirebaseRecyclerAdapter<MemeComments, MemeComment.PhotoViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<MemeComments, MemeComment.PhotoViewHolder>(
                MemeComments.class,
                R.layout.single_comment_item,
                MemeComment.PhotoViewHolder.class,
                ftp) {
            @Override
            protected void populateViewHolder(final MemeComment.PhotoViewHolder photoViewHolder, MemeComments model, int position) {

                final String list_user_id = getRef(position).getKey();
                mCmtDatabse.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){

                            cmt_name = dataSnapshot.child("name").getValue().toString();
                            cmt_time = dataSnapshot.child("time").getValue().toString();
                            cmt = dataSnapshot.child("comment").getValue().toString();

                            photoViewHolder.setName(cmt_name);
                            photoViewHolder.setTime(cmt_time);
                            photoViewHolder.setCm(cmt);


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }


        };
        comment_list.setAdapter(firebaseRecyclerAdapter);
    }


    public static class PhotoViewHolder extends RecyclerView.ViewHolder {


        View mView;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;

        }

        public void setName(String cmt_name) {

            TextView usr_name = (TextView) mView.findViewById(R.id.user_name);
            usr_name.setText(cmt_name);
        }

        public void setTime(String cmt_time) {

        }

        public void setCm(String cmt) {
            TextView usr_cmt = (TextView) mView.findViewById(R.id.cmmt);
            usr_cmt.setText(cmt);
        }
    }*/


