package com.akp.examcoach.Basic;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.akp.examcoach.R;
public class DemoAkp extends AppCompatActivity {
        private static final String API_KEY = "AIzaSyAWDN1vhtk0eW9TLBciVrss4ptxQENqKlY";
        private static final String VIDEO_ID = "IWJ_nIFP200";

        WebView webView;
        ProgressBar progressBar;
        ImageView back_btn;

        String video_url = "IWJ_nIFP200", html = "";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_demo_akp);

//                back_btn = (ImageView) findViewById(R.id.full_videoview_btn);
//                back_btn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                                webView.loadData("", "text/html", "UTF-8");
//                                finish();
//                        }
//                });

                webView = (WebView) findViewById(R.id.webview);
                progressBar = (ProgressBar) findViewById(R.id.progressBar);

                if (video_url.equalsIgnoreCase("")) {
                        finish();
                        return;
                }

                WebSettings ws = webView.getSettings();
                ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
                ws.setPluginState(WebSettings.PluginState.ON);
                ws.setJavaScriptEnabled(true);
                webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                webView.reload();
                html = getHTML(video_url);

                webView.loadData(html, "text/html", "UTF-8");

                WebClientClass webViewClient = new WebClientClass(progressBar);
                webView.setWebViewClient(webViewClient);
                WebChromeClient webChromeClient = new WebChromeClient();
                webView.setWebChromeClient(webChromeClient);
                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) webView.getLayoutParams();
                layoutParams.setMargins(0, 0, 0, 0);
                webView.setLayoutParams(layoutParams);
                webView.addJavascriptInterface(new Object() {
                        @JavascriptInterface
                        public void onHeightReceived(final float height) {
                                runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                                ViewGroup.LayoutParams layoutParams = webView.getLayoutParams();
                                                layoutParams.height = (int) height;
                                                webView.setLayoutParams(layoutParams);
                                        }
                                });
                        }
                }, "AndroidWebView");

        }


        @Override
        protected void onDestroy() {
                super.onDestroy();
                try {
                        webView.loadData("", "text/html", "UTF-8");
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }

        @Override
        public void onBackPressed() {
                super.onBackPressed();
                try {
                        webView.loadData("", "text/html", "UTF-8");
                        finish();
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }


        public class WebClientClass extends WebViewClient {
                ProgressBar ProgressBar = null;

                WebClientClass(ProgressBar progressBar) {
                        ProgressBar = progressBar;
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        super.onPageStarted(view, url, favicon);
                        ProgressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        ProgressBar.setVisibility(View.GONE);
                        // Calculate and adjust the WebView's height based on its content
                        view.evaluateJavascript("(function() { " +
                                "var height = Math.max(" +
                                "document.documentElement.clientHeight, " +
                                "document.body.scrollHeight, " +
                                "document.documentElement.scrollHeight, " +
                                "document.body.offsetHeight, " +
                                "document.documentElement.offsetHeight); " +
                                "window.AndroidWebView.onHeightReceived(height); " +
                                "})();", null);
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(getHTML(video_url));
                        return true;
                }




        }

        public String getHTML(String videoId) {
                String html = "<iframe src=\"https://video-player.penpencil.co?type=YOUTUBE&amp;url=https://www.youtube.com/embed/IWJ_nIFP200\" allow=\"accelerometer; autoplay;fullscreen; encrypted-media; gyroscope; picture-in picture\" class=\"aspect-video\" style=\"width:100%;height:100%;overflow:hidden\"></iframe>";
//                LogShowHide.LogShowHideMethod("video-id from html url= ", "" + html);
                return html;
        }
}
//        // Initialize the YouTube player view
//        mYouTubePlayerView = findViewById(R.id.youtube_player_view);
//        mOnInitializedListener = new YouTubePlayer.OnInitializedListener() {
//            @Override
//            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
//                // Load the video with the given video ID 2qYivvdmeqQ
//                youTubePlayer.loadVideo("2qYivvdmeqQ");
//
//                // Hide the YouTube logo
//                youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
//            }
//
//            @Override
//            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
//                // Handle initialization failure
//            }
//        };
//
//        // Initialize the YouTube player
//        mYouTubePlayerView.initialize("AIzaSyAWDN1vhtk0eW9TLBciVrss4ptxQENqKlY", mOnInitializedListener);
//    }      public String getHTML() {
//        String html = "<div class=\"wrap\">\n" +"<body style='margin:0;padding:0;'>\n"+
//                "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/?playlist="+getVideoId+"&?rel=0"+"&loop=1&amp;showinfo=0?ecver=2&theme=dark&autohide=1&modestbranding=1&autoplay=1&controls=2&fs=0\" frameborder=\"0\" allowfullscreen>\n" +
//                "//     </iframe>"+
//                "<TouchableOpacity\n" +
//                "    // TouchableOpacity to \"steal\" taps\n" +
//                "    // absolutely positioned to the top\n" +
//                "    // height must be adjusted to\n" +
//                "    // just cover the top 3 dots\n" +
//                "    style={{\n" +
//                "      top: 0,\n" +
//                "      height: 50,\n" +
//                "      width: '100%',\n" +
//                "      position: 'absolute',\n" +
//                "    }}\n" +
//                "  />"+
//                "</div>\n"+
//                "<style>\n" +
//                "    .wrap{\n" +
//                "        height: 400px;\n" +
//                "        overflow: hidden;\n" +
//                "    }\n" +
//                "    iframe {\n" +
//                "        position: relative;\n" +
//                "        top: -60px;\n" +
//                "    }\n" +
//                "</style>";
//
//        Log.d("ressss","https://www.youtube.com/embed/?playlist="+getVideoId);
//        return html;
//                }
//}