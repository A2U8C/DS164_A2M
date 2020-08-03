package com.abc.sih;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.applozic.mobicommons.commons.core.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import io.kommunicate.KmChatBuilder;
import io.kommunicate.Kommunicate;
import io.kommunicate.callbacks.KmCallback;
import io.kommunicate.users.KMUser;


public class ChatbotActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user1 = mAuth.getCurrentUser();

        Kommunicate.init(this, "3b2948665595052a37eb5f424007c93c");









        //  Kommunicate.init(getApplicationContext(), "735c9c1eac19259ee9d34361b05f15f7");



        new KmChatBuilder(getApplicationContext()).launchChat(new KmCallback() {
            @Override
            public void onSuccess(Object message) {
                Utils.printLog(ChatbotActivity.this, "ChatTest", "Success : " + message);
            }

            @Override
            public void onFailure(Object error) {
                Utils.printLog(ChatbotActivity.this, "ChatTest", "Failure : " + error);
            }
        });


        List<String> agentList = new ArrayList();
        // agentList.add("anishadnani007@gmail.com"); //add your agentID. The agentId id the email id you have used to signup on kommunicate dashboard

        agentList.add("supriyapatil08011999@gmail.com");
        List<String> botList = new ArrayList();
        //botList.add("smart-cop"); //Go to bots(https://dashboard.kommunicate.io/bot) -> Integrated bots -> Copy botID

        botList.add("ranger-bot-p5nnx");

        new KmChatBuilder(getApplicationContext()).setAgentIds(agentList).setBotIds(botList).launchChat(new KmCallback() {
            @Override
            public void onSuccess(Object message) {
                Utils.printLog(ChatbotActivity.this, "ChatTest", "Success : " + message);
            }

            @Override
            public void onFailure(Object error) {
                Utils.printLog(ChatbotActivity.this, "ChatTest", "Failure : " + error);
            }
        });


        new KmChatBuilder(ChatbotActivity.this).setWithPreChat(true).launchChat(new KmCallback() {
            @Override
            public void onSuccess(Object message) {
                Utils.printLog(ChatbotActivity.this, "ChatTest", "Success : " + message);
            }

            @Override
            public void onFailure(Object error) {
                Utils.printLog(ChatbotActivity.this, "ChatTest", "Failure : " + error);
            }
        });

        KMUser user = new KMUser();
        user.setUserId("1019"); // Pass a unique key
        // user.setImageLink(""); // Optiona

        new KmChatBuilder(ChatbotActivity.this).setKmUser(user).launchChat(new KmCallback() {
            @Override
            public void onSuccess(Object message) {
                Utils.printLog(ChatbotActivity.this, "ChatTest", "Success : " + message);
            }

            @Override
            public void onFailure(Object error) {
                Utils.printLog(ChatbotActivity.this, "ChatTest", "Failure : " + error);
            }
        });





    }
}
