package com.abc.sih;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import cdflynn.android.library.checkview.CheckView;

public class AttendanceActivityNextStep extends AppCompatActivity {
    private static int TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_next_step);
        CheckView mCheckView = (CheckView) findViewById(R.id.check);
        mCheckView.check();
        mCheckView.uncheck();
        mCheckView.check();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(AttendanceActivityNextStep.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, TIME_OUT);
    }
}
