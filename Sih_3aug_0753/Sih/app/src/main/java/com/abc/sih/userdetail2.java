package com.abc.sih;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class userdetail2 extends Fragment {
    private EditText pst1,pst2,pst3,dsc1,dsc2,dsc3,tme1,tme2,tme3;
    private String usrpst1,usrpst2,usrpst3,desc1,desc2,desc3,time1,time2,time3;
    private Button submit;

    public userdetail2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_userdetail2, container, false);
        pst1 = view.findViewById(R.id.usrpst1);
        pst2 = view.findViewById(R.id.usrpst2);
        pst3 = view.findViewById(R.id.usrpst3);
        dsc1 = view.findViewById(R.id.desc1);
        dsc2 = view.findViewById(R.id.desc2);
        dsc3 = view.findViewById(R.id.desc3);
        tme1 = view.findViewById(R.id.time1);
        tme2 = view.findViewById(R.id.time2);
        tme3 = view.findViewById(R.id.time3);
        submit = view.findViewById(R.id.login);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usrpst1 = pst1.getText().toString();
                usrpst2 = pst2.getText().toString();
                usrpst3 = pst3.getText().toString();
                desc1 = dsc1.getText().toString();
                desc2 = dsc2.getText().toString();
                desc3 = dsc3.getText().toString();
                time1 = tme1.getText().toString();
                time2 = tme2.getText().toString();
                time3 = tme3.getText().toString();
                if (TextUtils.isEmpty(usrpst1)||TextUtils.isEmpty(usrpst2)||TextUtils.isEmpty(usrpst3)
                        ||TextUtils.isEmpty(desc1)||TextUtils.isEmpty(desc2)||TextUtils.isEmpty(desc3)
                        ||TextUtils.isEmpty(time1)||TextUtils.isEmpty(time2)||TextUtils.isEmpty(time3)){
                    Toast.makeText(getActivity(),"Please Enter all details",Toast.LENGTH_SHORT).show();

                }else {
                    Done();
                }
            }
        });

        return view;
    }

    private void Done() {
        HashMap<String, Object> userdataMap = new HashMap<>();
        userdataMap.put("user_post1",usrpst1);
        userdataMap.put("user_post2",usrpst2);
        userdataMap.put("user_post3",usrpst3);
        userdataMap.put("user_post1_descp",desc1);
        userdataMap.put("user_post2_descp",desc2);
        userdataMap.put("user_post3_descp",desc3);
        userdataMap.put("user_post1_time",time1);
        userdataMap.put("user_post2_time",time2);
        userdataMap.put("user_post3_time",time3);
        String mUid = FirebaseAuth.getInstance().getUid();
        FirebaseDatabase.getInstance().getReference().child("Users").child(mUid).updateChildren(userdataMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent pass = new Intent(getActivity(),MainActivity.class);
                        pass.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(pass);
                    }
                });
    }

}
