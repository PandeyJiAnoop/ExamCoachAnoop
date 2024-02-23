package com.akp.examcoach.Basic.QuizExam;
/**
 * Created by Anoop Pandey on 9696381023.
 */
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;



public class MyWebViewClient extends WebViewClient {

    Context context;
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        String hostname;
        // YOUR HOSTNAME
        hostname = ("http://examcoach.in/");
        Uri uri = Uri.parse(url);
        if (url.startsWith("file:") || uri.getHost() != null && uri.getHost().endsWith(hostname)) {
            return false;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        view.getContext().startActivity(intent);
        return true;
    }
}
