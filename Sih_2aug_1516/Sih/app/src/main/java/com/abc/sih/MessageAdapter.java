package com.abc.sih;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Messages> mMessageList;
    private FirebaseAuth mAuth;
    private Context mContext;
    private DatabaseReference meraDatabase;
    private DatabaseReference uskaDatabase;
    public MessageAdapter(List<Messages> mMessageList){
        this.mMessageList = mMessageList;
    }

    //@NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //return null;
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_single_layout, parent, false);
        return new MessageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MessageViewHolder viewHolder, int i) {

        mAuth = FirebaseAuth.getInstance();
        String current_user_id = mAuth.getCurrentUser().getUid();

        Messages c = mMessageList.get(i);
        String DD = ChatTimeTab.getTimeAgo(c.getTime());
        if(i==0){

            viewHolder.date.setVisibility(View.VISIBLE);
            viewHolder.date.setText(DD);

        }else{
            Messages e = mMessageList.get(i-1);
            String GG = ChatTimeTab.getTimeAgo(e.getTime());
            if(GG.equalsIgnoreCase(DD)){
                viewHolder.date.setVisibility(View.GONE);
            }else{
                viewHolder.date.setVisibility(View.VISIBLE);
                viewHolder.date.setText(DD);
            }

        }

        String from_user = c.getFrom();
        String message_type = c.getType();
       final Boolean tickseen = c.getSeen();
        if(from_user.equals(current_user_id)){// means we have sent the message

            //viewHolder.messageText.setBackgroundColor(Color.WHITE);
            //viewHolder.messageText.setTextColor(Color.BLACK);
            meraDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_user_id);
            meraDatabase.keepSynced(true);
            meraDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    final String image = dataSnapshot.child("image").getValue().toString();
                    final String name = dataSnapshot.child("name").getValue().toString();

         Picasso.with(mContext).load(image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.default_avatar).into(viewHolder.profileImage, new Callback() {
             @Override
             public void onSuccess() {

             }

             @Override
             public void onError() {

                 Picasso.with(mContext).load(image).placeholder(R.drawable.default_avatar).into(viewHolder.profileImage);

             }
         });
                        viewHolder.nameText.setText(name);

                   viewHolder.arrow.setVisibility(View.GONE);
                    viewHolder.arrow_right.setVisibility(View.VISIBLE);
//                    viewHolder.tick.setVisibility(View.VISIBLE);
//                    if (tickseen){
//                        viewHolder.tick.setImageResource(R.drawable.greentick);
//                    }else{
//                        viewHolder.tick.setImageResource(R.drawable.tick);
//
//                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



        }else{// we have received the message

            //viewHolder.messageText.setBackgroundResource(R.drawable.message_text_background);
            //viewHolder.messageText.setTextColor(Color.WHITE);
            uskaDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(from_user);
            uskaDatabase.keepSynced(true);

            uskaDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    final String image = dataSnapshot.child("image").getValue().toString();
                    final String name = dataSnapshot.child("name").getValue().toString();

                    Picasso.with(mContext).load(image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.default_avatar).into(viewHolder.profileImage, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {

                            Picasso.with(mContext).load(image).placeholder(R.drawable.default_avatar).into(viewHolder.profileImage);

                        }
                    });
                    viewHolder.nameText.setText(name);

                     viewHolder.arrow.setVisibility(View.VISIBLE);
                   viewHolder.arrow_right.setVisibility(View.GONE);
//                    viewHolder.tick.setVisibility(View.GONE);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        if(message_type.equals("text")){
            viewHolder.messageText.setText(c.getMessage());
            viewHolder.messageImage.setVisibility(View.INVISIBLE);
            String sfd = DateFormat.getTimeInstance(SimpleDateFormat.SHORT, Locale.US).format(new Date(c.getTime()));
            viewHolder.sendText.setText(sfd);
        }else{
            viewHolder.messageText.setVisibility(View.INVISIBLE);
            Picasso.with(viewHolder.profileImage.getContext()).load(c.getMessage())
                    .placeholder(R.drawable.default_avatar).into(viewHolder.messageImage);
        }


    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder{

        public TextView messageText;
        private CircleImageView profileImage;
        public TextView nameText;
        public TextView sendText;
        public ImageView messageImage;

        public TextView time;
        public TextView date;
        public RelativeLayout arrow;
        public RelativeLayout arrow_right;
        public RelativeLayout msg;


        public MessageViewHolder(View view) {
            super(view);

            messageText = (TextView) view.findViewById(R.id.message_text_layout);
            profileImage = (CircleImageView) view.findViewById(R.id.message_profile_layout);
            nameText = (TextView) view.findViewById(R.id.name_text_layout);
            sendText = (TextView) view.findViewById(R.id.time_text_layout);
            messageImage = (ImageView) view.findViewById(R.id.message_image_layout);
            date = (TextView) view.findViewById(R.id.date);
            arrow = (RelativeLayout) view.findViewById(R.id.arrow);
            arrow_right = (RelativeLayout) view.findViewById(R.id.arrow_right);
            msg = (RelativeLayout) view.findViewById(R.id.msg);


        }
    }
    }

