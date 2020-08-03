package com.abc.sih.Reports;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.abc.sih.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class ViewDialog {

    private String ussr;
    public void showDialog(Activity activity, final String display_name, final String rptId, final String refId){
        final Dialog dialog = new Dialog(activity);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog);
        final FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();

        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        final EditText rs = (EditText) dialog.findViewById(R.id.reason);


        Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rs.getText().toString()!=null){
                    final String rsn = rs.getText().toString();
                    String uu;
                    if(refId.equalsIgnoreCase(rptId)){
                        uu="none";
                    }else{
                        uu=rptId;
                    }
                    /////  final String rpId = FirebaseDatabase.getInstance().getReference().child("Report").push().getKey();
//                    HashMap<String, String > userMap = new HashMap<>();
//                  //  userMap.put("Reportername", mAuth.getUid());
//                   // userMap.put("Reportedname", display_name);
//                 //   userMap.put("reference",uu);
//                    userMap.put("Reason",rsn);

                    //  userMap.put("thumb_image","default");

                    FirebaseDatabase.getInstance().getReference().child("PostReport").child(refId).child(mAuth.getUid()).setValue(rsn).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            dialog.dismiss();
                        }
                    });
                }
                else{
                    dialog.dismiss();
                }
            }
        });
        Button cancel = (Button) dialog.findViewById(R.id.btn_can);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }
}
