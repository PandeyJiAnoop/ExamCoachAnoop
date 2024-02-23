package com.akp.examcoach.Basic.YoutubeJune;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.akp.examcoach.Basic.CustomePlayerYoutube;
import com.akp.examcoach.R;

public class June_PlayVideoWebview extends AppCompatActivity {
    private WebView webView;
    String getVideoId,getId;

    TextView fullScreen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_june__play_video_webview);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getVideoId=getIntent().getStringExtra("video_id");
        getId=getIntent().getStringExtra("vid");
        webView = findViewById(R.id.webView);
        fullScreen = findViewById(R.id.fullScreen);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);

        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("http://examcoach.in/website/EMBVid?GroupType=" + getVideoId);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        // Set a WebViewClient to handle the page load within the WebView
        webView.setWebViewClient(new WebViewClient());

        // Add a WebChromeClient to enable video playback
        webView.setWebChromeClient(new WebChromeClient());

        fullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), June_YoutubeWebview.class);
                intent.putExtra("video_id",getVideoId);
                intent.putExtra("vid",getId);
                startActivity(intent);
            }
        });
    }
}
