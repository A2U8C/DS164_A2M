package com.abc.sih;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

public class RegisterActivity extends AppCompatActivity {
    private EditText pswd,cnfrmpswd,user;
    private Button reg,login,admin;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

       // FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        
        pswd = (EditText)findViewById(R.id.pswd);
        cnfrmpswd = (EditText)findViewById(R.id.cnfrmpswd);
        user = (EditText)findViewById(R.id.username);
        reg = findViewById(R.id.register);
        login = findViewById(R.id.Login);
        admin = findViewById(R.id.admin);
        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent at = new Intent(RegisterActivity.this,AdminLogin.class);
                startActivity(at);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login  = new Intent(RegisterActivity.this,LoginActivity.class);
                login.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(login);
            }
        });
        mAuth = FirebaseAuth.getInstance();
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = user.getText().toString();
                String psswrd = pswd.getText().toString();
                String confirm = cnfrmpswd.getText().toString();
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(psswrd) || TextUtils.isEmpty(confirm)){
                    Toast.makeText(RegisterActivity.this,"Please Enter all details",Toast.LENGTH_SHORT).show();
                }else if(!psswrd.equalsIgnoreCase(confirm)){
                    Toast.makeText(RegisterActivity.this,"Password not matching",Toast.LENGTH_SHORT).show();

                }else {
                    registerUser(username,psswrd);
                }
            }
        });
    }

    private void registerUser( String username, final String psswrd) {
        username = username + "@hotmail.com";
        final String finalUsername = username;
        mAuth.createUserWithEmailAndPassword(username, psswrd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent verify = new Intent(RegisterActivity.this,VerifyAadhar.class);
                            verify.putExtra("UID",user.getUid());
                            startActivity(verify);

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(RegisterActivity.this, "Authentication failed."+ finalUsername +psswrd,
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null){
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
            ref.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String moo = dataSnapshot.child("verified_user").getValue().toString();
                    if(moo == "yes" || moo.equals("yes") || "yes".equals(moo)){
                        Intent send = new Intent(RegisterActivity.this,MainActivity.class);
                        startActivity(send);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }
}
