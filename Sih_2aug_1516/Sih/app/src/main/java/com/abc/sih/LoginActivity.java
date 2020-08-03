package com.abc.sih;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Database;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.jeevandeshmukh.glidetoastlib.GlideToast;

public class LoginActivity extends AppCompatActivity {
    private EditText pswd,user;
    private Button login;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pswd = (EditText)findViewById(R.id.pswd);
        user = (EditText)findViewById(R.id.username);
        login = findViewById(R.id.login);
        mAuth = FirebaseAuth.getInstance();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = user.getText().toString();
                String psswrd = pswd.getText().toString();
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(psswrd)){
                    Toast.makeText(LoginActivity.this,"Please Enter all details",Toast.LENGTH_SHORT).show();
                }else {
                    LoginUser(username,psswrd);
                }
            }
        });

    }

    private void LoginUser(String email, String password) {
        email = email + "@hotmail.com";

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                                String deviceToken = FirebaseInstanceId.getInstance().getToken();
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
                            FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                                ref.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String mok = dataSnapshot.child("verified_user").getValue().toString();
                                        Log.e("trial_log",mok);
                                        if(mok == "yes" || mok.equals("yes") || "yes".equals(mok))
                                        {
                                            Log.e("trial_log1",mok);
                                            ref.child(FirebaseAuth.getInstance().getUid()).child("device_token_login").setValue(deviceToken);
                                            // Sign in success, update UI with the signed-in user's information
                                            Intent start = new Intent(LoginActivity.this,MainActivity.class);
                                            start.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(start);
                                        }
                                        else{
                                            new GlideToast.makeToast(LoginActivity.this,"Not Verified User", GlideToast.LENGTHLONG,GlideToast.FAILTOAST).show();
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });



                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }
}
