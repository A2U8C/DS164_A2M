package com.abc.sih;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.abc.sih.Reports.ViewDialog;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import at.markushi.ui.CircleButton;
import io.paperdb.Paper;


public class NewsFragmentAdmin extends Fragment {


    private RecyclerView news;
    private String email;
    private DatabaseReference mNews;
    private int stopPosition;
    CircleButton news_upd;
    static Context context;
    private static Activity myac;
    private FirebaseRecyclerAdapter adapter;
    public NewsFragmentAdmin() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        context = activity;
        myac = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_news_admin, container, false);
        news = view.findViewById(R.id.news);
        news_upd = (CircleButton) view.findViewById(R.id.imp_btn);

        news_upd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),AdminNewsUploadActivity.class);
                startActivity(intent);
            }
        });
        //email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        //  int index = email.indexOf('@');
        // email = email.substring(0,index);

        /////////////
        Paper.init(getContext());

        String language = Paper.book().read("language");
        if(language == null)
            Paper.book().write("language", "en");

        //updateView((String)Paper.book().read("language"));
        //updateView("hi");
        ////////////
        news.setHasFixedSize(true);
        news.setLayoutManager(new LinearLayoutManager(getActivity()));
        mNews = FirebaseDatabase.getInstance().getReference().child("news");
        mNews.keepSynced(true);
        adapter = new FirebaseRecyclerAdapter<news, NewsFragmentAdmin.newsHolder>(
                news.class,
                R.layout.news_user_nakli,
                NewsFragmentAdmin.newsHolder.class,
                mNews

        ) {

            @Override
            protected void populateViewHolder(final NewsFragmentAdmin.newsHolder newsHolder, news news, int i) {
                String list_news_id = getRef(i).getKey();
                mNews.child(list_news_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.child("title").getValue()!=null && dataSnapshot.child("content").getValue()!=null
                        && dataSnapshot.child("location").getValue()!=null && dataSnapshot.child("post").getValue()!=null
                        && dataSnapshot.child("type").getValue()!=null)
                        {
                            String Title = dataSnapshot.child("title").getValue().toString();
                            String content = dataSnapshot.child("content").getValue().toString();
                            String location = dataSnapshot.child("location").getValue().toString();
                            //String Title_hindi = dataSnapshot.child("title_hindi").getValue().toString();
                            //  String content_hindi = dataSnapshot.child("content_hindi").getValue().toString();
                            //  String location_hindi = dataSnapshot.child("location_hindi").getValue().toString();
                            String post = dataSnapshot.child("post").getValue().toString();
                            String type = dataSnapshot.child("type").getValue().toString();


                       /* if((String)Paper.book().read("language") == "en")
                        {
                            Log.e("insid","inn");
                            newsHolder.setTitle(Title);
                            newsHolder.setContent(content);
                            newsHolder.setLocation(location);
                            newsHolder.setPost(post,type,getContext());
                        }
                        else if((String)Paper.book().read("language") == "hi"){
                            newsHolder.setTitle(Title_hindi);
                            newsHolder.setContent(content_hindi);
                            newsHolder.setLocation(location_hindi);
                            newsHolder.setPost(post,type,getContext());
                        }*/
                            Log.e("insid","inn");
                            newsHolder.setTitle(Title);
                            newsHolder.setContent(content);
                            newsHolder.setLocation(location);
                            newsHolder.setPost(post,type,getContext());

                       /*newsHolder.mediaController.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                if (newsHolder.vw.isPlaying()){
                                    stopPosition = newsHolder.vw.getCurrentPosition(); //stopPosition is an int
                                  //  newsHolder.vw.pause();
                                }else{
                                    newsHolder.vw.seekTo(stopPosition);
                                   // newsHolder.vw.start();
                                }
                                return true;
                            }
                        }); */
                            newsHolder.pop.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    newsHolder.showPopup(list_news_id,adapter);
                                }
                            });

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        };
        news.setAdapter(adapter);
        return view;
    }
    public static class newsHolder extends RecyclerView.ViewHolder{
        View mView;
        VideoView vw;
        int  stopPosition;
        MediaController mediaController;
        ImageButton pop;
        public newsHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            vw = mView.findViewById(R.id.vid);
            pop = (ImageButton) mView.findViewById(R.id.popup);

        }




        public void setTitle(String title) {
            TextView tit = mView.findViewById(R.id.title);
            tit.setText(title);
        }

        public void setPost(String post, String type, Context context) {
            ImageView Iw = mView.findViewById(R.id.imag);
            if (type.equals("image")){
                vw.setVisibility(View.GONE);
                Iw.setVisibility(View.VISIBLE);


                Picasso.with(context).load(post).placeholder(R.drawable.default_avatar).networkPolicy(NetworkPolicy.OFFLINE).into(Iw, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                        Picasso.with(context).load(post).placeholder(R.drawable.default_avatar).into(Iw);
                    }
                });


            }else {
                mediaController = new MediaController(context);

                vw.setVisibility(View.VISIBLE);
                Iw.setVisibility(View.GONE);
                vw.setMediaController(mediaController);
                mediaController.setAnchorView(vw);
                vw.setVideoPath(post);
                //vw.setBackgroundResource(R.drawable.playvideo);
                // mediaController.show(5000);



                // vw.requestFocus()
                vw.start();


            }

        }

        public void setLocation(String location) {
            TextView loca = mView.findViewById(R.id.location_text);
            loca.setText(location);
        }

        public void setContent(String content) {
            TextView cont = mView.findViewById(R.id.content);
            cont.setText(content);
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
                        FirebaseDatabase.getInstance().getReference().child("news").child(list_user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //FirebaseDatabase.getInstance().getReference().child("Posts").child(list_user_id).removeValue();
                                //FirebaseDatabase.getInstance().getReference().child("Comments").child(list_user_id).removeValue();



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