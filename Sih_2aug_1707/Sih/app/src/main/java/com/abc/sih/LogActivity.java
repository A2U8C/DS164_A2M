package com.abc.sih;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LogActivity extends AppCompatActivity {
    private String resId;
    private RecyclerView rec;
    private DatabaseReference logRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            this.setFinishOnTouchOutside(false);
        }
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.x = -100;
        params.height = 1000;
        params.width = ActionBar.LayoutParams.MATCH_PARENT;
        params.y = -50;

        this.getWindow().setAttributes(params);
        resId = getIntent().getExtras().getString("resId");
        logRef = FirebaseDatabase.getInstance().getReference().child("Logs").child(resId);
        Log.d("nihal",resId);
        rec = findViewById(R.id.logtab);

        updateUi();



    }

    private void updateUi() {
        rec.setHasFixedSize(true);
        rec.setLayoutManager(new LinearLayoutManager(LogActivity.this));
        FirebaseRecyclerAdapter<Logs, resourceHolder> adapter = new FirebaseRecyclerAdapter<Logs, resourceHolder>(
                Logs.class,
                R.layout.loglayout,
                resourceHolder.class,
                logRef
        ) {
            @Override
            protected void populateViewHolder(final resourceHolder resourceHolder,final Logs log, int i) {
                final String list_user_id = getRef(i).getKey();

                logRef.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        long AssgnTime = dataSnapshot.child("assgnTime").getValue(long.class);
                        String assgnBy = dataSnapshot.child("assgnBy").getValue().toString();
                        String assgnto = dataSnapshot.child("assgnto").getValue().toString();
                        Log.d("kilimanjaro",assgnBy);
                        long returned = 0;
                        if (dataSnapshot.hasChild("returned")){
                            returned =dataSnapshot.child("returned").getValue(long.class);
                        }
                        Date date = new Date(AssgnTime);
                        SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy hh:mm aa",
                                Locale.getDefault());
                        String assgntime = sfd.format(date);
                        resourceHolder.setDetails(assgntime,assgnBy,assgnto,returned);


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        };

        rec.setAdapter(adapter);

    }

    public static class resourceHolder extends RecyclerView.ViewHolder{
        View mView;
        TextView asT,asB,asto,asR;
        public resourceHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            asT = mView.findViewById(R.id.assgntime);
            asB = mView.findViewById(R.id.assgnBy);
            asto = mView.findViewById(R.id.assgnTo);
            asR = mView.findViewById(R.id.returned);
        }




        public void setDetails(String assgnTime, String assgnBy, String assgnto, long returned) {
            asT.setText( assgnTime);
            asto.setText(assgnto);
            asB.setText(assgnBy);
            if (returned != 0) {
                Date date = new Date(returned);
                SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy hh:mm aa",
                        Locale.getDefault());
                asR.setText(sfd.format(date));
            }
        }
    }

}
