package com.akp.examcoach.Basic;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.akp.examcoach.R;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class FloatingButtonGameDetails extends RelativeLayout {
    String UserId, UserName;
    FloatingActionButton fab_add_my_album_listing;
    RelativeLayout main_rl;
    private AlertDialog alertDialog1;
    private AlertDialog alertDialog2;

    public FloatingButtonGameDetails(Context context) {
        super(context);
    }

    public FloatingButtonGameDetails(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FloatingButtonGameDetails(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void initFloating() {
        inflateHeader();
    }

    private void inflateHeader() {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.activity_floating_button_game_details, this);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("login_preference", MODE_PRIVATE);
        UserId = sharedPreferences.getString("U_id", "");
        UserName = sharedPreferences.getString("U_name", "");
        fab_add_my_album_listing=findViewById(R.id.fab_add_my_album_listing);
        Animation startAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.bounce);
        fab_add_my_album_listing.startAnimation(startAnimation);
        main_rl=findViewById(R.id.mail_rl);
        fab_add_my_album_listing.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String smsNumber = "919876543210"; //without '+'
                try {
                    Intent sendIntent = new Intent("android.intent.action.MAIN");
                    //sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.setType("text/plain");
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "Welcome to \nExam Coach \n\n How May I help you?");
                    sendIntent.putExtra("ExamCoach", smsNumber + "@s.whatsapp.net"); //phone number without "+" prefix
                    sendIntent.setPackage("com.whatsapp");
                    getContext().startActivity(sendIntent);
                } catch(Exception e) {
                    Toast.makeText(getContext(), "Error/n" + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}



