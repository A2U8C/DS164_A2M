package com.abc.sih;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AndamanActivity extends AppCompatActivity {

    private RecyclerView titles;
    private String email;
    private DatabaseReference mTitle;
    private int stopPosition;

    private TextView title_heading;
    ImageView back_icon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_andaman);


        titles = findViewById(R.id.rv);
        titles.setHasFixedSize(true);
        titles.setLayoutManager(new LinearLayoutManager(AndamanActivity.this));
        mTitle = FirebaseDatabase.getInstance().getReference().child("Andaman-Divisons");
        mTitle.keepSynced(true);

        FirebaseRecyclerAdapter<Divisions, titlesHolder> adapter = new FirebaseRecyclerAdapter<Divisions, titlesHolder>(
                Divisions.class,
                R.layout.single_division_layout,
                titlesHolder.class,
                mTitle

        ) {

            @Override
            protected void populateViewHolder(final titlesHolder titlesHolder, Divisions title, int i) {
                String list_news_id = getRef(i).getKey();
                mTitle.child(list_news_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String Title = dataSnapshot.child("name").getValue().toString();
                        String num_of_range = dataSnapshot.child("num_of_range").getValue().toString();
                        String topic_num = dataSnapshot.child("num").getValue().toString();

                        Log.e("Anish_tag",Title);

                            titlesHolder.setTitle1(Title);
                            titlesHolder.setnum1(topic_num);
                            titlesHolder.setNum_of_ranges(num_of_range);




                        titlesHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                //Toast.makeText(AndamanActivity.this,Title,Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AndamanActivity.this,DivisiontoRangeActivity.class);
                                intent.putExtra("division_name",Title);
                                //intent.putExtra("section_name",section_name);
                                startActivity(intent);

                            }
                        });



                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        };



        titles.setAdapter(adapter);

    }

    public static class titlesHolder extends RecyclerView.ViewHolder{
        View mView;
        int  stopPosition;
        TextView title;
        RelativeLayout rel1;
        TextView n1;
        TextView num_of_ranges;
        public titlesHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            title = mView.findViewById(R.id.title_text);
            rel1 = mView.findViewById(R.id.rel1);
            n1 = mView.findViewById(R.id.number_text);
            num_of_ranges = mView.findViewById(R.id.num_of_ranges);
        }

        public void setTitle1(String title) {
            TextView tit = mView.findViewById(R.id.title_text);
            tit.setText(title);
        }

        public void setnum1(String topic_num)
        {
            n1.setText(topic_num);
        }
        public void setNum_of_ranges(String num_of_rangess){

            num_of_ranges.setText("Has "+num_of_rangess+" ranges");
        }

    }

}