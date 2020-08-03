package com.abc.sih;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abc.sih.Reports.ReportComp;
import com.abc.sih.Reports.StartReport;
import com.github.ag.floatingactionmenu.OptionsFabLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class Frag1New extends Fragment {

    SpeedDialView speedDialView;
    Button demo_btn;
    private int chck=1;
    TextView resources_demo;
    ImageView sos,chatbot,rate;
    private String[] descriptionData = {"Attendance", "Check-\npoint A", "Check-\npoint A", "Confirm"};
    private LinearLayout l1,l2,l3,l4;
    Button btn1,btn2,btn3,btn4;
    String childval;

    TextView notes1,notes2,notes3,notes4;
    ImageView img1,img2,img3,img4;
    TextView info1,info2,info3,info4;
    private DatabaseReference user_data;
    private String user_id,date;
    ImageButton resource_btn;
    public Frag1New() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_frag1_new, container, false);

        OptionsFabLayout fabWithOptions = (OptionsFabLayout) view.findViewById(R.id.fab_l);
        fabWithOptions.setMiniFabsColors(
                R.color.colorPrimary,
                R.color.green_fab);
        Button attd_btn = (Button) view.findViewById(R.id.btn_4);
        l1 = (LinearLayout) view.findViewById(R.id.cpa);

        info1 = (TextView) view.findViewById(R.id.ca1);
        info2 = (TextView) view.findViewById(R.id.ca2);
        btn1 = (Button) view.findViewById(R.id.btn1);
        ((MainActivity)getActivity()).showprog();

        CardView card = view.findViewById(R.id.card4);
        demo_btn = (Button) view.findViewById(R.id.demo_btn);
        resources_demo = (TextView) view.findViewById(R.id.resources_demo);
        sos = (ImageView) getActivity().findViewById(R.id.sos_image);
        chatbot = (ImageView) getActivity().findViewById(R.id.question_image);
        rate = (ImageView) getActivity().findViewById(R.id.smiley_image);
        resource_btn = (ImageButton) view.findViewById(R.id.resources_btn);

        resource_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),Deptres.class);
                startActivity(intent);
            }
        });

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

        chatbot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),PolygonMarkingActivity.class);
                startActivity(intent);
            }
        });

        sos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //firRegistraion
                //Intent intent = new Intent(getContext(),AdminMainActivity.class);

                Intent intent=new Intent(getContext(),LocationFinder.class);


                startActivity(intent);
            }
        });

        demo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                chck++;
                Log.e("anish_demo_check",chck+"");
                Animation anim = new AlphaAnimation(0.0f, 1.0f);
                anim.setDuration(1000);
                anim.setStartOffset(20);
                anim.setRepeatMode(Animation.REVERSE);
                anim.setRepeatCount(Animation.INFINITE);
                if(chck%2==0){
                    Log.e("anish_inin",chck+"inside");
                    resources_demo.setVisibility(View.VISIBLE);

                    resources_demo.startAnimation(anim);
                    demo_btn.setText("Stop Demo");
                    demo_btn.setBackgroundResource(R.drawable.rec_gradient_tptry);

                }
                if(chck%2!=0){
                    demo_btn.setText("Start Demo");
                    resources_demo.setVisibility(View.INVISIBLE);
                    demo_btn.setBackgroundResource(R.drawable.rec_gradient);
                    resources_demo.clearAnimation();
                    anim.cancel();
                    anim.reset();
                }
            }
        });

        //Set main fab clicklistener.
        fabWithOptions.setMainFabOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Main fab clicked!", Toast.LENGTH_SHORT).show();
            }
        });

        //Set mini fabs clicklisteners.
        fabWithOptions.setMiniFabSelectedListener(new OptionsFabLayout.OnMiniFabSelectedListener() {
            @Override
            public void onMiniFabSelected(MenuItem fabItem) {
                switch (fabItem.getItemId()) {
                    case R.id.fab_add:
             Intent intent = new Intent(getContext(), ReportComp.class);
             startActivity(intent);
                        break;
                    case R.id.fab_link:
                  Intent intent1 = new Intent(getContext(),MapsActivity.class);
                  startActivity(intent1);
                    default:
                        break;
                }
            }
        });

        date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        user_data = FirebaseDatabase.getInstance().getReference().child("Checkpoint").child(user_id).child(date);
        user_data.keepSynced(true);
        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference atd = FirebaseDatabase.getInstance().getReference().child("Attendance");
        atd.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(date) && dataSnapshot.child(date).hasChild(userid)){
                    card.setVisibility(View.GONE);
                    checkshow();
                    l1.setVisibility(View.VISIBLE);
                    btn1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(),CheckPointAttachActivity.class);
                            intent.putExtra("childval",childval);
                            //intent.putExtra("user_id",userid);
                            startActivity(intent);
                        }
                    });



                }else {
                    card.setVisibility(View.VISIBLE);
                    ((MainActivity)getActivity()).setbar(1);

                    l1.setVisibility(View.GONE);
                    attd_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getContext(), Selfie.class);
                            startActivity(intent);

                        }
                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        return view;
    }
    private void checkshow() {
        user_data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                childval = "C1";
                info1.setText("CP A");
                ((MainActivity) Objects.requireNonNull(getActivity())).setbar(2);

                if (dataSnapshot.hasChild("C1")){
                    childval = "C2";
                    ((MainActivity)getActivity()).setbar(3);
                    info1.setText("CP B");
                }
                if (dataSnapshot.hasChild("C2")){
                    childval = "C3";
                    ((MainActivity)getActivity()).setbar(4);
                    info1.setText("CP C");
                }
                if (dataSnapshot.hasChild("C3")){
                    childval = "C4";
                    ((MainActivity)getActivity()).setbar(5);
                    info1.setText("CP D");
                }
                if (dataSnapshot.hasChild("C4")){
                    info2.setVisibility(View.VISIBLE);
                    info1.setVisibility(View.GONE);
                    btn1.setVisibility(View.GONE);
                }else {
                    info2.setVisibility(View.GONE);
                    info1.setVisibility(View.VISIBLE);
                    btn1.setVisibility(View.VISIBLE);
                }
                info1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(),ViewCPNotesActivity.class);
                        intent.putExtra("user_id",user_id);
                        intent.putExtra("date",date);
                        intent.putExtra("childval",childval);
                        startActivity(intent);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}