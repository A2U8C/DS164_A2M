package com.abc.sih;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;


public class AdminHomeFragment extends Fragment {


    public AdminHomeFragment() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_admin_home, container, false);

        CardView card1 = (CardView) view.findViewById(R.id.colorcard1);
        CardView card4 = (CardView) view.findViewById(R.id.colorcard4);
        CardView card2 = (CardView) view.findViewById(R.id.colorcard2);
        CardView card5 = (CardView) view.findViewById(R.id.colorcard5);
        CardView card6 = (CardView) view.findViewById(R.id.colorcard6);
        CardView card7 = (CardView) view.findViewById(R.id.colorcard7);
        CardView card3 = (CardView) view.findViewById(R.id.colorcard3);
        CardView card8 = (CardView)view.findViewById(R.id.colorcard8);

        card8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),AdminQueryReportActivity.class);
                startActivity(intent);
            }
        });

        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),AdminVerifyUserActivity.class);
                startActivity(intent);
            }
        });

        card7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),AdminGenerateReportActivity.class);
                startActivity(intent);
            }
        });

        card6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),AdminQrCodeGenerateActivity.class);
                startActivity(intent);
            }
        });

        card5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),ViewMapCases.class);
                startActivity(intent);
            }
        });

        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(),AdminBeatActivity.class);
                startActivity(intent);
            }
        });

        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),AdminAttendanceActivity.class);
                startActivity(intent);
            }
        });

        card4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),MapsActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}