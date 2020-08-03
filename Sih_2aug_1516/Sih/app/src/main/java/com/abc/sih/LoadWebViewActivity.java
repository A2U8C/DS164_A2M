package com.abc.sih;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class LoadWebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_web_view);

        WebView myWebView = (WebView) findViewById(R.id.webView);
        String websitelink = getIntent().getStringExtra("website-link");
        myWebView.loadUrl(websitelink);
        // myWebView.loadUrl("https://midnightbot.github.io/leaderboar-try/");

        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }
}
