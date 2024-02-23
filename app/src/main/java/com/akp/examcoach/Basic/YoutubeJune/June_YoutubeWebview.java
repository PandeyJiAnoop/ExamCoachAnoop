package com.akp.examcoach.Basic.YoutubeJune;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.PlaybackParams;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.ValueCallback;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class June_YoutubeWebview  extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener, View.OnClickListener {
    private static final String TAG = June_YoutubeWebview.class.getSimpleName();
    public static final String API_KEY = "AIzaSyAWDN1vhtk0eW9TLBciVrss4ptxQENqKlY";
    //https://www.youtube.com/watch?v=<VIDEO_ID>
//    public static final String VIDEO_ID = "IWJ_nIFP200";
    private YouTubePlayer mPlayer;
    private View mPlayButtonLayout;
    private TextView mPlayTimeTextView, speed_tv, quality_tv;
    private Handler mHandler = null;
    private SeekBar mSeekBar;
    //    ImageView back_img;
    String getVideoId;
    ImageView comment_video;
    TextView fullScreen;
    int i = 0;
    private PopupWindow popupWindow;
    List<HashMap<String, String>> arrayList;
    EditText message_edit1;
    RecyclerView cust_chat_recyclerView2;
    ArrayList<HashMap<String, String>> arrayList1 = new ArrayList<>();
    private ChatListAdapter chatHistoryAdapter;
    String UserId, getId;
    PlaybackParams myPlayBackParams = null;
    ImageButton send_btn1;
    TextView static_tv, static_date_tv, staticmsg_tv;

    private WebView mWebView;
    public static final int REQUEST_CODE_LOLIPOP = 1;
    private final static int RESULT_CODE_ICE_CREAM = 2;
    private ValueCallback<Uri[]> mFilePathCallback;
    private String mCameraPhotoPath;
    private ValueCallback<Uri> mUploadMessage;
    ProgressDialog pd;

    LinearLayout comment_ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_june__youtube_webview);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);
        UserId = sharedPreferences.getString("username", "");
        getVideoId = getIntent().getStringExtra("video_id");
//        getVideoId="06ISGVjgK2U";

        getId = getIntent().getStringExtra("vid");
        Log.d("resss", "res" + getId + getVideoId);









        pd = new ProgressDialog(June_YoutubeWebview.this);
        pd.setMessage("Please Wait Loading...");
        pd.show();
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo network = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifi.isConnected()) {
            //Internet available
            mWebView = findViewById(R.id.activity_main_webview);
            WebSettings webSettings = mWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            mWebView.setWebViewClient(new MyWebViewClient());
            mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            mWebView.getSettings().setDomStorageEnabled(true);
            mWebView.getSettings().setAppCacheEnabled(true);

            mWebView.loadUrl("http://examcoach.in/website/EMBVid?GroupType="+getVideoId);
        } else if (network.isConnected()) {
            //Internet available
            mWebView = findViewById(R.id.activity_main_webview);
            WebSettings webSettings = mWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            mWebView.getSettings().setDomStorageEnabled(true);
            mWebView.getSettings().setAppCacheEnabled(true);

            mWebView.setWebViewClient(new MyWebViewClient());
            mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            mWebView.setWebChromeClient(new WebChromeClient() {
                private String TAG;

                // For Android 3.0+
                public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                    mUploadMessage = uploadMsg;
                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                    i.addCategory(Intent.CATEGORY_OPENABLE);
                    i.setType("image/*");
                    startActivityForResult(Intent.createChooser(i, "File Chooser"), RESULT_CODE_ICE_CREAM);
                }

                // For Android 3.0+
                public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
                    mUploadMessage = uploadMsg;
                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                    i.addCategory(Intent.CATEGORY_OPENABLE);
                    i.setType("*/*");
                    startActivityForResult(Intent.createChooser(i, "File Browser"), RESULT_CODE_ICE_CREAM);
                }

                //For Android 4.1
                public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                    mUploadMessage = uploadMsg;
                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                    i.addCategory(Intent.CATEGORY_OPENABLE);
                    i.setType("image/*");
                    startActivityForResult(Intent.createChooser(i, "File Chooser"), RESULT_CODE_ICE_CREAM);
                }

                //For Android5.0+
                public boolean onShowFileChooser(
                        WebView webView, ValueCallback<Uri[]> filePathCallback,
                        FileChooserParams fileChooserParams) {
                    if (mFilePathCallback != null) {
                        mFilePathCallback.onReceiveValue(null);
                    }
                    mFilePathCallback = filePathCallback;
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
                        // Create the File where the photo should go
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                            takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
                        } catch (IOException ex) {
                            // Error occurred while creating the File
                            Log.e(TAG, "Unable to create Image File", ex);
                        }
                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                    Uri.fromFile(photoFile));
                        } else {
                            takePictureIntent = null;
                        }
                    }
                    Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                    contentSelectionIntent.setType("image/*");
                    Intent[] intentArray;
                    if (takePictureIntent != null) {
                        intentArray = new Intent[]{takePictureIntent};
                    } else {
                        intentArray = new Intent[0];
                    }
                    Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                    chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                    chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
                    startActivityForResult(chooserIntent, REQUEST_CODE_LOLIPOP);
                    return true;
                }
            });
            //REMOTE RESOURCE
            mWebView.loadUrl("http://examcoach.in/website/EMBVid?GroupType="+getVideoId);
        } else {
            Toast.makeText(getApplicationContext(), "Network Is Not Available", Toast.LENGTH_LONG).show();
        }
        // LOCAL RESOURCE
        // mWebView.loadUrl("file:///android_asset/index.html");
















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
        comment_video = findViewById(R.id.comment_video);
        speed_tv = findViewById(R.id.speed_tv);
        quality_tv = findViewById(R.id.quality_tv);
        cust_chat_recyclerView2 = findViewById(R.id.cust_chat_recyclerView2);
        message_edit1 = findViewById(R.id.message_edit1);
        send_btn1 = findViewById(R.id.send_btn1);
        static_tv = findViewById(R.id.static_tv);
        static_date_tv = findViewById(R.id.static_date_tv);
        staticmsg_tv = findViewById(R.id.staticmsg_tv);
        comment_ll = findViewById(R.id.comment_ll);


        fullScreen = (TextView) findViewById(R.id.fullScreen);
        fullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), June_PlayVideoWebview.class);
                intent.putExtra("video_id", getVideoId);
                intent.putExtra("vid", getId);
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
                SaveCommentAPI(UserId, getId);
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
            if (getVideoId.equalsIgnoreCase("")) {
                String videoId = "";
                player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION);
                if (!wasRestored) {
                    player.loadVideo(videoId);
                }
//                player.cueVideo(videoId);
            } else {
                player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION);
                if (!wasRestored) {
                    player.loadVideo(getVideoId);
                }
//                player.cueVideo(getVideoId);
            }

//            player.cueVideo(VIDEO_ID);
        }

        player.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);

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


    private void SaveCommentAPI(String userid, String gettaskid) {
        final ProgressDialog progressDialog = new ProgressDialog(June_YoutubeWebview.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL + "InsertComment", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", " ");
                jsonString = jsonString.replace("<string xmlns=\"http://examcoach.in/\">", " ");
                jsonString = jsonString.replace("</string>", " ");
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    String msg = jsonObject.getString("msg");
                    message_edit1.setText("");
                    Toast.makeText(June_YoutubeWebview.this, msg, Toast.LENGTH_LONG).show();
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
                Log.d("myTag", "message:" + error);
                Toast.makeText(June_YoutubeWebview.this, "Internet connection is slow Or no internet connection", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserId", userid);
                params.put("TaskId", gettaskid);
                params.put("Comment", message_edit1.getText().toString());
                params.put("Type", "ELEARNINGTASK");
                Log.d("sdsdsd", "" + params);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(June_YoutubeWebview.this);
        requestQueue.add(stringRequest);
    }


    public void getCommentData(String taskid) {
        final ProgressDialog progressDialog = new ProgressDialog(June_YoutubeWebview.this);
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
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(June_YoutubeWebview.this, 1);
                    chatHistoryAdapter = new ChatListAdapter(June_YoutubeWebview.this, arrayList1);
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
                Toast.makeText(June_YoutubeWebview.this, "Internet connection is slow Or no internet connection", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("TaskId", taskid);
                params.put("Type", "ELEARNINGTASK");
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(June_YoutubeWebview.this);
        requestQueue.add(stringRequest);
    }


    private void showPlayerSpeedDialog() {
        String[] playerSpeedArrayLabels = {"0.8x", "1.0x", "1.2x", "1.5x", "1.8x", "2.0x"};

        PopupMenu popupMenu = new PopupMenu(June_YoutubeWebview.this, speed_tv);
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

    @Override
    public void onBackPressed() {
        if(mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(imageFileName,  /* prefix */".jpg",         /* suffix */storageDir      /* directory */
        );
        return imageFile;
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setUpWebViewDefaults(WebView webView) {
        WebSettings settings = webView.getSettings();
        // Enable Javascript
        settings.setJavaScriptEnabled(true);
        // Use WideViewport and Zoom out if there is no viewport defined
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        // Enable pinch to zoom without the zoom buttons
        settings.setBuiltInZoomControls(true);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            // Hide the zoom controls for HONEYCOMB+
            settings.setDisplayZoomControls(false);
        }
        // Enable remote debugging via chrome://inspect
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        // We set the WebViewClient to ensure links are consumed by the WebView rather
        // than passed to a browser if it can
        webView.setWebViewClient(new WebViewClient());
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_CODE_ICE_CREAM:
                Uri uri = null;
                if (data != null) {
                    uri = data.getData();
                }
                mUploadMessage.onReceiveValue(uri);
                mUploadMessage = null;
                break;
            case REQUEST_CODE_LOLIPOP:
                Uri[] results = null;
                // Check that the response is a good one
                if (resultCode == Activity.RESULT_OK) {
                    if (data == null) {
                        // If there is not data, then we may have taken a photo
                        if (mCameraPhotoPath != null) {
                            results = new Uri[]{Uri.parse(mCameraPhotoPath)};
                        }
                    } else {
                        String dataString = data.getDataString();
                        if (dataString != null) {
                            results = new Uri[]{Uri.parse(dataString)};
                        }
                    }
                }
                mFilePathCallback.onReceiveValue(results);
                mFilePathCallback = null;
                break;
        }

    }



    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);

            if (!pd.isShowing()) {
                pd.show();
            }

            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            System.out.println("on finish");
            if (pd.isShowing()) {
                pd.dismiss();
            }

        }
    }
}

