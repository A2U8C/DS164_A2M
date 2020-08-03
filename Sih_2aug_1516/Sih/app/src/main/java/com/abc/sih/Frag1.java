package com.abc.sih;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.abc.sih.Helper.LocaleHelper;
import com.abc.sih.Reports.ReportComp;
import com.abc.sih.Reports.ReportConfid;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;


public class Frag1 extends Fragment {

    TextView text1,text2,text3,text4,text5,text6,text7;
    Button bt1,bt2,bt3,bt4,bt5,bt6,bt7;
    ImageView sos;
    private String mUid,type;
    CardView fore, dept;
    public Frag1() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_frag1, container, false);
        mUid = FirebaseAuth.getInstance().getUid();
        ImageView rate = (ImageView) view.findViewById(R.id.smiley_image);
        Button compalain_button = (Button) view.findViewById(R.id.btn_1);
        Button qr_button = (Button) view.findViewById(R.id.btn_2);
        Button btn_try = (Button) view.findViewById(R.id.btn_3);
        Button attd_btn = (Button) view.findViewById(R.id.btn_4);
        Button ForDept = (Button) view.findViewById(R.id.btn_5) ;
        Button ForRes = (Button) view.findViewById(R.id.btn_6);
        Button map = (Button) view.findViewById(R.id.btn_7);
        Button andaman = (Button) view.findViewById(R.id.btn_8);
        fore = view.findViewById(R.id.card6);
        dept = view.findViewById(R.id.card5);

        sos = (ImageView) view.findViewById(R.id.sos_image);

        ///////////////
        text1 = (TextView) view.findViewById(R.id.primary_text);
        text2 = (TextView) view.findViewById(R.id.primary_text_2);
        text3 = (TextView) view.findViewById(R.id.primary_text_3);
        text4 = (TextView) view.findViewById(R.id.primary_text_4);
        text5 = (TextView) view.findViewById(R.id.primary_text_5);
        text6 = (TextView) view.findViewById(R.id.primary_text_6);
        text6 = (TextView) view.findViewById(R.id.primary_text_6);
        text7 = (TextView) view.findViewById(R.id.primary_text_7);

        bt1 = (Button) view.findViewById(R.id.btn_1);
        bt2 = (Button) view.findViewById(R.id.btn_2);
        bt3 = (Button) view.findViewById(R.id.btn_3);
        bt4 = (Button) view.findViewById(R.id.btn_4);
        bt5 = (Button) view.findViewById(R.id.btn_5);
        bt6 = (Button) view.findViewById(R.id.btn_6);
        bt7 = (Button) view.findViewById(R.id.btn_7);
        FirebaseDatabase.getInstance().getReference().child("Users").child(mUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                type = dataSnapshot.child("type").getValue().toString();
                if (type.equals("D") || type.equals("R")){
                    fore.setVisibility(View.GONE);
                    dept.setVisibility(View.GONE);

                }else {
                    ForDept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getContext(), Deptres.class);
                            startActivity(intent);
                        }
                    });
                    ForRes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getContext(), ForestRes.class);
                            startActivity(intent);
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


//

        andaman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),AndamanActivity.class);
                startActivity(intent);
            }
        });
        sos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //firRegistraion
                Intent intent = new Intent(getContext(),AdminMainActivity.class);

                //Intent intent=new Intent(getContext(),LocationFinder.class);


                startActivity(intent);
            }
        });

        /////////////
        Paper.init(getContext());

        String language = Paper.book().read("language");
        if(language == null)
            Paper.book().write("language", "en");

        updateView((String)Paper.book().read("language"));
        //updateView("hi");
        ////////////

        ImageView chatbot = (ImageView) view.findViewById(R.id.question_image);
        chatbot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),ChatbotActivity.class);
                startActivity(intent);
            }
        });

////////////////////rate button onclick listener////////////////////////////
        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                View view_bottom = getLayoutInflater().inflate(R.layout.activity_rate_app,null);
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
                bottomSheetDialog.setContentView(view_bottom);
                BottomSheetBehavior mBehavior = BottomSheetBehavior.from((View) view_bottom.getParent());
                mBehavior.setPeekHeight(500);
                final ImageView img1 = (ImageView) view_bottom.findViewById(R.id.sad);
                final ImageView img2 = (ImageView) view_bottom.findViewById(R.id.equal);
                final ImageView img3 = (ImageView) view_bottom.findViewById(R.id.smile);
                final TextView review_txt = (TextView) view_bottom.findViewById(R.id.review_text);




                bottomSheetDialog.show();


                img1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        img1.setImageResource(R.drawable.mesadcolored);
                        img2.setImageResource(R.drawable.meequal);
                        img3.setImageResource(R.drawable.mesmile);
                        review_txt.setText("Not Satisying");
                        review_txt.setTextColor(Color.RED);
                    }
                });
                img2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        img2.setImageResource(R.drawable.meequalcolored);
                        img1.setImageResource(R.drawable.mesad);
                        img3.setImageResource(R.drawable.mesmile);
                        review_txt.setText("Ok");
                        review_txt.setTextColor(Color.GRAY);
                    }
                });

                img3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        img1.setImageResource(R.drawable.mesad);
                        img2.setImageResource(R.drawable.meequal);
                        img3.setImageResource(R.drawable.mesmilecolored);
                        review_txt.setText("Satisying");
                        review_txt.setTextColor(Color.GREEN);
                    }
                });


            }
        });
        ////////////////////////////////////////////
        /////////////////////////complain button//////////////////////////
        compalain_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),ReportComp.class);
                startActivity(intent);

            }
        });

        //////////////////////////////
        /////////////////qr button//////////////////////
        qr_button
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(),CheckpointActivity.class);
                        startActivity(intent);
                    }
                });

        /////////////////////
        ///btn_try/////
        btn_try.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setComponent(new ComponentName("com.hypertrack.live","com.hypertrack.live.LaunchActivity"));
                startActivity(intent);
                //Toast.makeText(getContext(),"No Code",Toast.LENGTH_SHORT).show();
            }
        });
        //////////////attd_btn////////////
        attd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Selfie.class);
                startActivity(intent);
            }
        });

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent map = new Intent(getContext(),MapsActivity.class);
                startActivity(map);
            }
        });
        return view;
    }

    private void updateView(String lang) {

        Context context = LocaleHelper.setLocale(getContext(), lang);
        Resources resources = context.getResources();

        text1.setText(resources.getString(R.string.launch_complain));
        text2.setText(resources.getString(R.string.mark_check_point));
        text3.setText(resources.getString(R.string.your_tracker));
        text4.setText(resources.getString(R.string.mark_attendance));
        text5.setText(resources.getString(R.string.department_assets));
        text6.setText(resources.getString(R.string.forest_resource));
        text7.setText(resources.getString(R.string.alert_zones));

        bt1.setText(resources.getString(R.string.open));
        bt2.setText(resources.getString(R.string.open));
        bt3.setText(resources.getString(R.string.open));
        bt4.setText(resources.getString(R.string.open));
        bt5.setText(resources.getString(R.string.open));
        bt6.setText(resources.getString(R.string.open));
        bt7.setText(resources.getString(R.string.open));




    }

}
