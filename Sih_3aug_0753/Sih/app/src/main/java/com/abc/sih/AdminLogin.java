package com.abc.sih;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AdminLogin extends AppCompatActivity {
    private EditText user,pswd;
    private Button btn;
    private String username,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        user = findViewById(R.id.username);
        btn = findViewById(R.id.login);
        pswd = findViewById(R.id.pswd);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = user.getText().toString();
                password = pswd.getText().toString();
                if (TextUtils.isEmpty(username) ||TextUtils.isEmpty(password)){
                    Toast.makeText(AdminLogin.this,"FILL All details",Toast.LENGTH_SHORT).show();
                }else {
                    if (username.equals("admin")&& password.equals("admin")){
                        Intent st = new Intent(AdminLogin.this,AdminMainActivity.class);
                        startActivity(st);
                    }
                }username = user.getText().toString();
                password = pswd.getText().toString();
                if (TextUtils.isEmpty(username) ||TextUtils.isEmpty(password)){
                    Toast.makeText(AdminLogin.this,"FILL All details",Toast.LENGTH_SHORT).show();
                }else {
                    if (username.equals("admin")&& password.equals("admin")){
                        Intent st = new Intent(AdminLogin.this,AdminMainActivity.class);
                        startActivity(st);
                    }
                }
            }
        });

    }
}