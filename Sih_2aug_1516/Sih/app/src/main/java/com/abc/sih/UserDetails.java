package com.abc.sih;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class UserDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_contanier, new userdetail1()).commit();
    }
    public void nextpage(){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_contanier, new userdetail2()).commit();
    }
    public void selectrfo(){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_contanier, new SelectRfo()).commit();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
