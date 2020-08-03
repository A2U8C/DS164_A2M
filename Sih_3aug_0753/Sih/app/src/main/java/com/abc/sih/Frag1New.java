package com.abc.sih;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ag.floatingactionmenu.OptionsFabLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;

public class Frag1New extends Fragment {

    SpeedDialView speedDialView;
    Button demo_btn;
    private int chck=1;
    TextView resources_demo;
    ImageView sos,chatbot,rate;

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

        demo_btn = (Button) view.findViewById(R.id.demo_btn);
        resources_demo = (TextView) view.findViewById(R.id.resources_demo);
        sos = (ImageView) view.findViewById(R.id.sos_image);
        chatbot = (ImageView) view.findViewById(R.id.question_image);
        rate = (ImageView) view.findViewById(R.id.smiley_image);

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
                        Toast.makeText(
                                getContext(),
                                fabItem.getTitle() + " clicked!",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.fab_link:
                        Toast.makeText(getContext(),
                                fabItem.getTitle() + "clicked!",
                                Toast.LENGTH_SHORT).show();
                    default:
                        break;
                }
            }
        });


        return view;
    }
}