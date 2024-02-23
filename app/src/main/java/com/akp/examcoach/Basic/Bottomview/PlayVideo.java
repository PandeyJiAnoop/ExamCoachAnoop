package com.akp.examcoach.Basic.Bottomview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.examcoach.Basic.CustomePlayerYoutube;
import com.akp.examcoach.R;
import com.bumptech.glide.Glide;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;


public class PlayVideo extends AppCompatActivity  {
    RelativeLayout relativeLayout;
    ImageView imageView;
    YouTubePlayerView youTubePlayerView;
    String getVideoId,getId;
    TextView fullScreen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_youtube_player_view);
        getVideoId=getIntent().getStringExtra("video_id");
        getId=getIntent().getStringExtra("vid");
        fullScreen = findViewById(R.id.fullScreen);
        imageView = findViewById(R.id.youtube_thumbnail);

        Glide.with(this)
                .load("https://img.youtube.com/vi/" + getVideoId + "/mqdefault.jpg")
                .into(imageView);

        youTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);
        youTubePlayerView.getPlayerUiController().showYouTubeButton(false);

        relativeLayout = findViewById(R.id.relative_layout);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeLayout.setVisibility(View.GONE);
                loadYoutubeVideo();
            }
        });

        fullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), CustomePlayerYoutube.class);
                intent.putExtra("video_id",getVideoId);
                intent.putExtra("vid",getId);
                startActivity(intent);
            }
        });
    }

    private void loadYoutubeVideo() {
        youTubePlayerView.getYouTubePlayerWhenReady(new YouTubePlayerCallback() {
            @Override
            public void onYouTubePlayer(@NonNull YouTubePlayer youTubePlayer) {
                youTubePlayer.loadVideo(getVideoId, 0);
            }
        });
    }
}