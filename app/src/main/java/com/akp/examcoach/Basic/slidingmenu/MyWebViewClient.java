package com.akp.examcoach.Basic.slidingmenu;

import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class MyWebViewClient extends WebViewClient {


    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        String hostname;
        // YOUR HOSTNAME
        hostname = "https://www.amazon.in/Books/b?ie=UTF8&node=976389031";
        Uri uri = Uri.parse(url);
        if (url.startsWith("file:") || uri.getHost() != null && uri.getHost().endsWith(hostname)) {
            return false;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        view.getContext().startActivity(intent);
        return true;
    }
}
