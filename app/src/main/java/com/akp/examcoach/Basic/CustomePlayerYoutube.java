package com.akp.examcoach.Basic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.PlaybackParams;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


import com.akp.examcoach.Basic.Bottomview.Api_Urls;
import com.akp.examcoach.Basic.Bottomview.PlayVideo;
import com.akp.examcoach.Basic.adapter.ChatListAdapter;
import com.akp.examcoach.R;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.android.youtube.player.YouTubePlayer.ErrorReason;
import static com.google.android.youtube.player.YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE;
import static com.google.android.youtube.player.YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION;
import static com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import static com.google.android.youtube.player.YouTubePlayer.PlaybackEventListener;
import static com.google.android.youtube.player.YouTubePlayer.PlayerStateChangeListener;
import static com.google.android.youtube.player.YouTubePlayer.PlayerStyle;
import static com.google.android.youtube.player.YouTubePlayer.Provider;

public class CustomePlayerYoutube  extends YouTubeBaseActivity implements OnInitializedListener, View.OnClickListener  {
    private static final String TAG = CustomePlayerYoutube.class.getSimpleName();
    public static final String API_KEY = "AIzaSyAWDN1vhtk0eW9TLBciVrss4ptxQENqKlY";
    //https://www.youtube.com/watch?v=<VIDEO_ID>
//    public static final String VIDEO_ID = "IWJ_nIFP200";
    private YouTubePlayer mPlayer;
    private View mPlayButtonLayout;
    private TextView mPlayTimeTextView,speed_tv,quality_tv;
    private Handler mHandler = null;
    private SeekBar mSeekBar;
    //    ImageView back_img;
    String getVideoId;
    ImageView comment_video;
    TextView fullScreen;
    int i=0;
    private PopupWindow popupWindow;
    List<HashMap<String,String>> arrayList;
    EditText message_edit1;
    RecyclerView cust_chat_recyclerView2;
    ArrayList<HashMap<String, String>> arrayList1 = new ArrayList<>();
    private ChatListAdapter chatHistoryAdapter;
    String UserId,getId;
    PlaybackParams myPlayBackParams = null;
    ImageButton send_btn1;
    TextView static_tv,static_date_tv,staticmsg_tv;

//   ImageView back_btn;
     ProgressBar progressBar;
     String html = "";
     WebView webView;

     LinearLayout comment_ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custome_player_youtube);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("username", "");
        getVideoId=getIntent().getStringExtra("video_id");
//        getVideoId="06ISGVjgK2U";

        getId=getIntent().getStringExtra("vid");
        Log.d("resss","res"+getId+getVideoId);


        initializeViews();


        // Initializing YouTube player view
        YouTubePlayerView youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_player_view);
        youTubePlayerView.initialize(API_KEY, this);


        //Add play button to explicitly play video in YouTubePlayerView
        mPlayButtonLayout = findViewById(R.id.video_control);
        findViewById(R.id.play_video).setOnClickListener(this);
        findViewById(R.id.pause_video).setOnClickListener(this);

        mPlayTimeTextView = (TextView) findViewById(R.id.play_time);
        mSeekBar = (SeekBar) findViewById(R.id.video_seekbar);
        mSeekBar.setOnSeekBarChangeListener(mVideoSeekBarChangeListener);
//        back_img=findViewById(R.id.back_img);
        comment_video=findViewById(R.id.comment_video);
        speed_tv=findViewById(R.id.speed_tv);
        quality_tv=findViewById(R.id.quality_tv);
        cust_chat_recyclerView2=findViewById(R.id.cust_chat_recyclerView2);
        message_edit1=findViewById(R.id.message_edit1);
        send_btn1=findViewById(R.id.send_btn1);
        static_tv=findViewById(R.id.static_tv);
        static_date_tv=findViewById(R.id.static_date_tv);
        staticmsg_tv=findViewById(R.id.staticmsg_tv);
        comment_ll=findViewById(R.id.comment_ll);


        fullScreen = (TextView) findViewById(R.id.fullScreen);
        fullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), PlayVideo.class);
                intent.putExtra("video_id",getVideoId);
                intent.putExtra("vid",getId);
                startActivity(intent);

               /* fullScreen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (i == 0) {
                            fullScreen.setText("Click Here For Exit Full Screen");
                            comment_ll.setVisibility(View.GONE);
                            fullScreen.setTextColor(Color.RED);
//                            player.setFullscreen(true);
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                            i++;
                        } else if (i == 1) {
                            comment_ll.setVisibility(View.VISIBLE);
                            fullScreen.setText("Click Here For Full Screen");
                            fullScreen.setTextColor(Color.WHITE);
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                            i = 0;
                        }
                    }
                });
            */

            }
        });
//        getCommentData(getId);

        speed_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPlayerSpeedDialog();
            }
        });





        getCommentData(getId);
        send_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveCommentAPI(UserId,getId);
            }
        });






        quality_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                recorder.setVideoSize(640,480);
              /*  recorder = new MediaRecorder();
                Method[] methods = recorder.getClass().getMethods();
                recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                recorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
                recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                recorder.setVideoFrameRate(24);
                recorder.setVideoSize(720, 480);

                for (Method method: methods){
                    try{
                        if (method.getName().equals("setAudioChannels")){
                            method.invoke(recorder, String.format("audio-param-number-of-channels=%d", 1));
                        }
                        else if(method.getName().equals("setAudioEncodingBitRate")){
                            method.invoke(recorder,12200);
                        }
                        else if(method.getName().equals("setVideoEncodingBitRate")){
                            method.invoke(recorder, 3000000);
                        }
                        else if(method.getName().equals("setAudioSamplingRate")){
                            method.invoke(recorder,8000);
                        }
                        else if(method.getName().equals("setVideoFrameRate")){
                            method.invoke(recorder,24);
                        }
                    }catch (IllegalArgumentException e) {

                        e.printStackTrace();
                    } catch (IllegalAccessException e) {

                        e.printStackTrace();
                    } catch (InvocationTargetException e) {

                        e.printStackTrace();
                    }
                }

                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                recorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);*/

            }
        });

        /*//        Set Default Speed code Anoop
         *//*   mVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

                //works only from api 23
                PlaybackParams myPlayBackParams = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    myPlayBackParams = new PlaybackParams();
                    myPlayBackParams.setSpeed(0.8f); //you can set speed here
                    mp.setPlaybackParams(myPlayBackParams);
                }

            }
        });*/

        mHandler = new Handler();
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult result) {
        Toast.makeText(this, "Failed to initialize.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        if (null == player) return;
        mPlayer = player;
        displayCurrentTime();

        // Start buffering
        if (!wasRestored) {
            if (getVideoId.equalsIgnoreCase("")){
                String videoId = "";
                player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION);
                if (!wasRestored) {
                    player.loadVideo(videoId);
                }
//                player.cueVideo(videoId);
            }
            else {
                player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION);
                if (!wasRestored) {
                    player.loadVideo(getVideoId);
                }
//                player.cueVideo(getVideoId);
            }

//            player.cueVideo(VIDEO_ID);
        }

        player.setPlayerStyle(PlayerStyle.MINIMAL);

        mPlayButtonLayout.setVisibility(View.VISIBLE);

        // Add listeners to YouTubePlayer instance
        player.setPlayerStateChangeListener(mPlayerStateChangeListener);
        player.setPlaybackEventListener(mPlaybackEventListener);
    }

    YouTubePlayer.PlaybackEventListener mPlaybackEventListener = new YouTubePlayer.PlaybackEventListener() {
        @Override
        public void onBuffering(boolean arg0) {
        }

        @Override
        public void onPaused() {
            mHandler.removeCallbacks(runnable);
        }

        @Override
        public void onPlaying() {
            mHandler.postDelayed(runnable, 100);
            displayCurrentTime();
        }

        @Override
        public void onSeekTo(int arg0) {
            mHandler.postDelayed(runnable, 100);
        }

        @Override
        public void onStopped() {
            mHandler.removeCallbacks(runnable);
        }
    };


    YouTubePlayer.PlayerStateChangeListener mPlayerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {
        @Override
        public void onAdStarted() {
        }

        @Override
        public void onError(YouTubePlayer.ErrorReason arg0) {
        }

        @Override
        public void onLoaded(String arg0) {
        }

        @Override
        public void onLoading() {
        }

        @Override
        public void onVideoEnded() {
            mPlayer.seekToMillis(0);
        }

        @Override
        public void onVideoStarted() {
            displayCurrentTime();
        }
    };

    SeekBar.OnSeekBarChangeListener mVideoSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            long lengthPlayed = (mPlayer.getDurationMillis() * progress) / 100;
            mPlayer.seekToMillis((int) lengthPlayed);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play_video:
                if (null != mPlayer && !mPlayer.isPlaying())
                    mPlayer.play();
                break;
            case R.id.pause_video:
                if (null != mPlayer && mPlayer.isPlaying())
                    mPlayer.pause();
                break;
        }
    }

    private void displayCurrentTime() {
        if (null == mPlayer) return;
        String formattedTime = formatTime(mPlayer.getDurationMillis() - mPlayer.getCurrentTimeMillis());
        mPlayTimeTextView.setText(formattedTime);
    }

    private String formatTime(int millis) {
        int seconds = millis / 1000;
        int minutes = seconds / 60;
        int hours = minutes / 60;

        return (hours == 0 ? "--:" : hours + ":") + String.format("%02d:%02d", minutes % 60, seconds % 60);
    }


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
//            displayCurrentTime();  isko youtube custome ke liye uncomment krna hai anoop 21-04-2023
            mHandler.postDelayed(this, 100);
        }
    };




    private void SaveCommentAPI(String userid,String gettaskid) {
        final ProgressDialog progressDialog = new ProgressDialog(CustomePlayerYoutube.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL+"InsertComment", new  Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>"," ");
                jsonString = jsonString.replace("<string xmlns=\"http://examcoach.in/\">"," ");
                jsonString = jsonString.replace("</string>"," ");
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    String msg = jsonObject.getString("msg");
                    message_edit1.setText("");
                    Toast.makeText(CustomePlayerYoutube.this,msg,Toast.LENGTH_LONG).show();
                    arrayList1.clear();
//                    cust_chat_recyclerView1.getAdapter().notifyDataSetChanged();
                    getCommentData(getId);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
//                Toast.makeText(MainActivity.this, "msg"+error, Toast.LENGTH_SHORT).show();
                Log.d("myTag", "message:"+error);
                Toast.makeText(CustomePlayerYoutube.this, "Internet connection is slow Or no internet connection", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserId",userid);
                params.put("TaskId",gettaskid);
                params.put("Comment",message_edit1.getText().toString());
                params.put("Type","ELEARNINGTASK");
                Log.d("sdsdsd",""+params);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(CustomePlayerYoutube.this);
        requestQueue.add(stringRequest);
    }


    public void getCommentData(String taskid) {
        final ProgressDialog progressDialog = new ProgressDialog(CustomePlayerYoutube.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL + "GetCommentList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", " ");
                jsonString = jsonString.replace("<string xmlns=\"http://examcoach.in/\">", " ");
                jsonString = jsonString.replace("</string>", " ");
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray jsonArrayr = jsonObject.getJSONArray("CommentList");
                    for (int i = 0; i < jsonArrayr.length(); i++) {
                        JSONObject jsonObject1 = jsonArrayr.getJSONObject(i);
                        HashMap<String, String> hm = new HashMap<>();
                        hm.put("ChId", jsonObject1.getString("ChId"));
                        hm.put("TaskId", jsonObject1.getString("TaskId"));
                        hm.put("Comment", jsonObject1.getString("Comment"));
                        hm.put("ondate", jsonObject1.getString("ondate"));
                        hm.put("userId", jsonObject1.getString("userId"));
                        hm.put("UserName", jsonObject1.getString("UserName"));
                        static_tv.setText(jsonObject1.getString("UserName"));
                        static_date_tv.setText(jsonObject1.getString("ondate"));
                        staticmsg_tv.setText(jsonObject1.getString("Comment"));

                        arrayList1.add(hm);
                    }
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(CustomePlayerYoutube.this, 1);
                    chatHistoryAdapter = new ChatListAdapter(CustomePlayerYoutube.this, arrayList1);
                    cust_chat_recyclerView2.setLayoutManager(gridLayoutManager);
                    cust_chat_recyclerView2.setAdapter(chatHistoryAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
//                Toast.makeText(MainActivity.this, "msg"+error, Toast.LENGTH_SHORT).show();
                Log.d("myTag", "message:" + error);
                Toast.makeText(CustomePlayerYoutube.this, "Internet connection is slow Or no internet connection", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("TaskId", taskid);
                params.put("Type","ELEARNINGTASK");
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(CustomePlayerYoutube.this);
        requestQueue.add(stringRequest);
    }





    private void showPlayerSpeedDialog() {
        String[] playerSpeedArrayLabels = {"0.8x", "1.0x", "1.2x", "1.5x", "1.8x", "2.0x"};

        PopupMenu popupMenu = new PopupMenu(CustomePlayerYoutube.this, speed_tv);
        for (int i = 0; i < playerSpeedArrayLabels.length; i++) {
            popupMenu.getMenu().add(i, i, i, playerSpeedArrayLabels[i]);
        }
        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            CharSequence itemTitle = item.getTitle();
            float playbackSpeed = Float.parseFloat(itemTitle.subSequence(0, 3).toString());
            changePlayerSpeed(playbackSpeed, itemTitle.subSequence(0, 3).toString());
            return false;
        });
        popupMenu.show();
    }

    private void changePlayerSpeed(float speed, String speedLabel) {
        // Set playback speed
        speed_tv.setText(speedLabel + "x");
        //works only from api 23
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            myPlayBackParams = new PlaybackParams();
            myPlayBackParams.setSpeed(speed); //you can set speed here
        }


    }



   /* private void changePlayerSpeed(float speed, String speedLabel) {
        // Set playback speed
        ((ExoPlayerVideoDisplayComponent) brightcoveVideoView.getVideoDisplay()).getExoPlayer().setPlaybackParameters(new PlaybackParameters(speed, 1.0f));
        // Set playback speed label
        playbackSpeed.setText("Speed: " + speedLabel + "x");
    }*/





//Webview Embeded Video Player ANoop New
    public void initializeViews() {
        try {
            webView = (WebView) findViewById(R.id.webview);
            progressBar = (ProgressBar) findViewById(R.id.progressBar);

            if (getVideoId.equalsIgnoreCase("")) {
                finish();
                return;
            }

            WebSettings ws = webView.getSettings();
            ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            ws.setPluginState(WebSettings.PluginState.ON);
            ws.setJavaScriptEnabled(true);
            webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            webView.reload();






            html = getHTML();

            webView.loadData(html, "text/html", "UTF-8");

            WebClientClass webViewClient = new WebClientClass(progressBar);
            webView.setWebViewClient(webViewClient);
            WebChromeClient webChromeClient = new WebChromeClient();
            webView.setWebChromeClient(webChromeClient);


        } catch (Exception e) {
            e.printStackTrace();
        }
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
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.i("webview-click :", "" + url.toString());

            //her the youtube url gets loaded to show video
//            view.loadUrl(getHTML(getVideoId));
            return true;
        }
    }



    public String getHTML() {
        String html = "<div class=\"wrap\">\n" +"<body style='margin:0;padding:0;'>\n"+
                "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/?playlist="+getVideoId+"&?rel=0"+"&loop=1&amp;showinfo=0?ecver=2&theme=dark&autohide=1&modestbranding=1&autoplay=1&controls=2&fs=0\" frameborder=\"0\" allowfullscreen>\n" +
                "//     </iframe>";
        Log.d("ressss","https://www.youtube.com/embed/?playlist="+getVideoId);
        return html;
                }
    }







/*

    public String getHTML() {
        String html = "<div class=\"wrap\">\n" +"<body style='margin:0;padding:0;'>\n"+
                "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/?playlist="+getVideoId+"&?rel=0"+"&loop=1&amp;showinfo=0?ecver=2&theme=dark&autohide=1&modestbranding=1&autoplay=1&controls=2&fs=0\" frameborder=\"0\" allowfullscreen>\n" +
                "//     </iframe>"+
                "<TouchableOpacity\n" +
                "    // TouchableOpacity to \"steal\" taps\n" +
                "    // absolutely positioned to the top\n" +
                "    // height must be adjusted to\n" +
                "    // just cover the top 3 dots\n" +
                "    style={{\n" +
                "      top: 0,\n" +
                "      height: 70,\n" +
                "      width: '100%',\n" +
                "      position: 'absolute',\n" +
                "    }}\n" +
                "  />"+
                "</div>\n"+
                "<style>\n" +
                "    .wrap{\n" +
                "        height: 400px;\n" +
                "        overflow: hidden;\n" +
                "    }\n" +
                "    iframe {\n" +
                "        position: relative;\n" +
                "        top: -60px;\n" +
                "    }\n" +
                "</style>";

        Log.d("ressss","https://www.youtube.com/embed/?playlist="+getVideoId);
        return html;
                }


package com.akp.examcoach.Basic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.PlaybackParams;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


import com.akp.examcoach.Basic.Bottomview.Api_Urls;
import com.akp.examcoach.Basic.adapter.ChatListAdapter;
import com.akp.examcoach.R;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.android.youtube.player.YouTubePlayer.ErrorReason;
import static com.google.android.youtube.player.YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE;
import static com.google.android.youtube.player.YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION;
import static com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import static com.google.android.youtube.player.YouTubePlayer.PlaybackEventListener;
import static com.google.android.youtube.player.YouTubePlayer.PlayerStateChangeListener;
import static com.google.android.youtube.player.YouTubePlayer.PlayerStyle;
import static com.google.android.youtube.player.YouTubePlayer.Provider;

public class CustomePlayerYoutube  extends YouTubeBaseActivity implements OnInitializedListener, View.OnClickListener  {
    private static final String TAG = CustomePlayerYoutube.class.getSimpleName();
    public static final String API_KEY = "AIzaSyAWDN1vhtk0eW9TLBciVrss4ptxQENqKlY";
    //https://www.youtube.com/watch?v=<VIDEO_ID>
//    public static final String VIDEO_ID = "IWJ_nIFP200";
    private YouTubePlayer mPlayer;
    private View mPlayButtonLayout;
    private TextView mPlayTimeTextView,speed_tv,quality_tv;
    private Handler mHandler = null;
    private SeekBar mSeekBar;
    //    ImageView back_img;
    String getVideoId;
    ImageView fullScreen,comment_video;
    int i=0;
    private PopupWindow popupWindow;
    List<HashMap<String,String>> arrayList;
    EditText message_edit1;
    RecyclerView cust_chat_recyclerView2;
    ArrayList<HashMap<String, String>> arrayList1 = new ArrayList<>();
    private ChatListAdapter chatHistoryAdapter;
    String UserId,getId;
    PlaybackParams myPlayBackParams = null;
    ImageButton send_btn1;
    TextView static_tv,static_date_tv,staticmsg_tv;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custome_player_youtube);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("username", "");
        getVideoId=getIntent().getStringExtra("video_id");
        getId=getIntent().getStringExtra("vid");
        Log.d("resss","res"+getId+getVideoId);

        // Initializing YouTube player view
        YouTubePlayerView youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_player_view);
        youTubePlayerView.initialize(API_KEY, this);


        //Add play button to explicitly play video in YouTubePlayerView
        mPlayButtonLayout = findViewById(R.id.video_control);
        findViewById(R.id.play_video).setOnClickListener(this);
        findViewById(R.id.pause_video).setOnClickListener(this);

        mPlayTimeTextView = (TextView) findViewById(R.id.play_time);
        mSeekBar = (SeekBar) findViewById(R.id.video_seekbar);
        mSeekBar.setOnSeekBarChangeListener(mVideoSeekBarChangeListener);
//        back_img=findViewById(R.id.back_img);
        comment_video=findViewById(R.id.comment_video);
        speed_tv=findViewById(R.id.speed_tv);
        quality_tv=findViewById(R.id.quality_tv);
        cust_chat_recyclerView2=findViewById(R.id.cust_chat_recyclerView2);
        message_edit1=findViewById(R.id.message_edit1);
        send_btn1=findViewById(R.id.send_btn1);
        static_tv=findViewById(R.id.static_tv);
        static_date_tv=findViewById(R.id.static_date_tv);
        staticmsg_tv=findViewById(R.id.staticmsg_tv);



        fullScreen = (ImageView) findViewById(R.id.fullScreen);
        fullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fullScreen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (i == 0) {
//                            player.setFullscreen(true);
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                            i++;
                        } else if (i == 1) {
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                            i = 0;
                        }
                    }
                }); }
        });
//        getCommentData(getId);

        speed_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPlayerSpeedDialog();
            }
        });





        getCommentData(getId);
        send_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveCommentAPI(UserId,getId);
            }
        });






        quality_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                recorder.setVideoSize(640,480);
              */
/*  recorder = new MediaRecorder();
                Method[] methods = recorder.getClass().getMethods();
                recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                recorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
                recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                recorder.setVideoFrameRate(24);
                recorder.setVideoSize(720, 480);

                for (Method method: methods){
                    try{
                        if (method.getName().equals("setAudioChannels")){
                            method.invoke(recorder, String.format("audio-param-number-of-channels=%d", 1));
                        }
                        else if(method.getName().equals("setAudioEncodingBitRate")){
                            method.invoke(recorder,12200);
                        }
                        else if(method.getName().equals("setVideoEncodingBitRate")){
                            method.invoke(recorder, 3000000);
                        }
                        else if(method.getName().equals("setAudioSamplingRate")){
                            method.invoke(recorder,8000);
                        }
                        else if(method.getName().equals("setVideoFrameRate")){
                            method.invoke(recorder,24);
                        }
                    }catch (IllegalArgumentException e) {

                        e.printStackTrace();
                    } catch (IllegalAccessException e) {

                        e.printStackTrace();
                    } catch (InvocationTargetException e) {

                        e.printStackTrace();
                    }
                }

                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                recorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);*//*


            }
        });

        */
/*//*
/        Set Default Speed code Anoop
         *//*
*/
/*   mVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

                //works only from api 23
                PlaybackParams myPlayBackParams = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    myPlayBackParams = new PlaybackParams();
                    myPlayBackParams.setSpeed(0.8f); //you can set speed here
                    mp.setPlaybackParams(myPlayBackParams);
                }

            }
        });*//*


        mHandler = new Handler();
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult result) {
        Toast.makeText(this, "Failed to initialize.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        if (null == player) return;
        mPlayer = player;
        displayCurrentTime();

        // Start buffering
        if (!wasRestored) {
            if (getVideoId.equalsIgnoreCase("")){
                String videoId = "yQ2g0RkWr6Q";
                player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION);
                if (!wasRestored) {
                    player.loadVideo(videoId);
                }
//                player.cueVideo(videoId);
            }
            else {
                player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION);
                if (!wasRestored) {
                    player.loadVideo(getVideoId);
                }
//                player.cueVideo(getVideoId);
            }

//            player.cueVideo(VIDEO_ID);
        }

        player.setPlayerStyle(PlayerStyle.MINIMAL);

        mPlayButtonLayout.setVisibility(View.VISIBLE);

        // Add listeners to YouTubePlayer instance
        player.setPlayerStateChangeListener(mPlayerStateChangeListener);
        player.setPlaybackEventListener(mPlaybackEventListener);
    }

    YouTubePlayer.PlaybackEventListener mPlaybackEventListener = new YouTubePlayer.PlaybackEventListener() {
        @Override
        public void onBuffering(boolean arg0) {
        }

        @Override
        public void onPaused() {
            mHandler.removeCallbacks(runnable);
        }

        @Override
        public void onPlaying() {
            mHandler.postDelayed(runnable, 100);
            displayCurrentTime();
        }

        @Override
        public void onSeekTo(int arg0) {
            mHandler.postDelayed(runnable, 100);
        }

        @Override
        public void onStopped() {
            mHandler.removeCallbacks(runnable);
        }
    };


    YouTubePlayer.PlayerStateChangeListener mPlayerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {
        @Override
        public void onAdStarted() {
        }

        @Override
        public void onError(YouTubePlayer.ErrorReason arg0) {
        }

        @Override
        public void onLoaded(String arg0) {
        }

        @Override
        public void onLoading() {
        }

        @Override
        public void onVideoEnded() {
        }

        @Override
        public void onVideoStarted() {
            displayCurrentTime();
        }
    };

    SeekBar.OnSeekBarChangeListener mVideoSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            long lengthPlayed = (mPlayer.getDurationMillis() * progress) / 100;
            mPlayer.seekToMillis((int) lengthPlayed);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play_video:
                if (null != mPlayer && !mPlayer.isPlaying())
                    mPlayer.play();
                break;
            case R.id.pause_video:
                if (null != mPlayer && mPlayer.isPlaying())
                    mPlayer.pause();
                break;
        }
    }

    private void displayCurrentTime() {
        if (null == mPlayer) return;
        String formattedTime = formatTime(mPlayer.getDurationMillis() - mPlayer.getCurrentTimeMillis());
        mPlayTimeTextView.setText(formattedTime);
    }

    private String formatTime(int millis) {
        int seconds = millis / 1000;
        int minutes = seconds / 60;
        int hours = minutes / 60;

        return (hours == 0 ? "--:" : hours + ":") + String.format("%02d:%02d", minutes % 60, seconds % 60);
    }


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            displayCurrentTime();
            mHandler.postDelayed(this, 100);
        }
    };




    private void SaveCommentAPI(String userid,String gettaskid) {
        final ProgressDialog progressDialog = new ProgressDialog(CustomePlayerYoutube.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL+"InsertComment", new  Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>"," ");
                jsonString = jsonString.replace("<string xmlns=\"http://examcoach.in/\">"," ");
                jsonString = jsonString.replace("</string>"," ");
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    String msg = jsonObject.getString("msg");
                    message_edit1.setText("");
                    Toast.makeText(CustomePlayerYoutube.this,msg,Toast.LENGTH_LONG).show();
                    arrayList1.clear();
//                    cust_chat_recyclerView1.getAdapter().notifyDataSetChanged();
                    getCommentData(getId);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
//                Toast.makeText(MainActivity.this, "msg"+error, Toast.LENGTH_SHORT).show();
                Log.d("myTag", "message:"+error);
                Toast.makeText(CustomePlayerYoutube.this, "Internet connection is slow Or no internet connection", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserId",userid);
                params.put("TaskId",gettaskid);
                params.put("Comment",message_edit1.getText().toString());
                params.put("Type","ELEARNINGTASK");
                Log.d("sdsdsd",""+params);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(CustomePlayerYoutube.this);
        requestQueue.add(stringRequest);
    }


    public void getCommentData(String taskid) {
        final ProgressDialog progressDialog = new ProgressDialog(CustomePlayerYoutube.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL + "GetCommentList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", " ");
                jsonString = jsonString.replace("<string xmlns=\"http://examcoach.in/\">", " ");
                jsonString = jsonString.replace("</string>", " ");
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray jsonArrayr = jsonObject.getJSONArray("CommentList");
                    for (int i = 0; i < jsonArrayr.length(); i++) {
                        JSONObject jsonObject1 = jsonArrayr.getJSONObject(i);
                        HashMap<String, String> hm = new HashMap<>();
                        hm.put("ChId", jsonObject1.getString("ChId"));
                        hm.put("TaskId", jsonObject1.getString("TaskId"));
                        hm.put("Comment", jsonObject1.getString("Comment"));
                        hm.put("ondate", jsonObject1.getString("ondate"));
                        hm.put("userId", jsonObject1.getString("userId"));
                        hm.put("UserName", jsonObject1.getString("UserName"));
                        static_tv.setText(jsonObject1.getString("UserName"));
                        static_date_tv.setText(jsonObject1.getString("ondate"));
                        staticmsg_tv.setText(jsonObject1.getString("Comment"));

                        arrayList1.add(hm);
                    }
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(CustomePlayerYoutube.this, 1);
                    chatHistoryAdapter = new ChatListAdapter(CustomePlayerYoutube.this, arrayList1);
                    cust_chat_recyclerView2.setLayoutManager(gridLayoutManager);
                    cust_chat_recyclerView2.setAdapter(chatHistoryAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
//                Toast.makeText(MainActivity.this, "msg"+error, Toast.LENGTH_SHORT).show();
                Log.d("myTag", "message:" + error);
                Toast.makeText(CustomePlayerYoutube.this, "Internet connection is slow Or no internet connection", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("TaskId", taskid);
                params.put("Type","ELEARNINGTASK");
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(CustomePlayerYoutube.this);
        requestQueue.add(stringRequest);
    }





    private void showPlayerSpeedDialog() {
        String[] playerSpeedArrayLabels = {"0.8x", "1.0x", "1.2x", "1.5x", "1.8x", "2.0x"};

        PopupMenu popupMenu = new PopupMenu(CustomePlayerYoutube.this, speed_tv);
        for (int i = 0; i < playerSpeedArrayLabels.length; i++) {
            popupMenu.getMenu().add(i, i, i, playerSpeedArrayLabels[i]);
        }
        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            CharSequence itemTitle = item.getTitle();
            float playbackSpeed = Float.parseFloat(itemTitle.subSequence(0, 3).toString());
            changePlayerSpeed(playbackSpeed, itemTitle.subSequence(0, 3).toString());
            return false;
        });
        popupMenu.show();
    }

    private void changePlayerSpeed(float speed, String speedLabel) {
        // Set playback speed
        speed_tv.setText(speedLabel + "x");
        //works only from api 23
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            myPlayBackParams = new PlaybackParams();
            myPlayBackParams.setSpeed(speed); //you can set speed here
        }


    }



   */
/* private void changePlayerSpeed(float speed, String speedLabel) {
        // Set playback speed
        ((ExoPlayerVideoDisplayComponent) brightcoveVideoView.getVideoDisplay()).getExoPlayer().setPlaybackParameters(new PlaybackParameters(speed, 1.0f));
        // Set playback speed label
        playbackSpeed.setText("Speed: " + speedLabel + "x");
    }*//*

}*/
