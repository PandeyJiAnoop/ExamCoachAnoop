package com.akp.examcoach.Basic.Teacher;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.window.SplashScreen;

import com.akp.examcoach.Basic.AdapterForBanner;
import com.akp.examcoach.Basic.BannerData;
import com.akp.examcoach.Basic.Bottomview.Api_Urls;
import com.akp.examcoach.Basic.CustomAdapter;
import com.akp.examcoach.Basic.DashboardActivity;
import com.akp.examcoach.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TeacherDashboard extends AppCompatActivity {
    ViewPager viewPager;
    private static int currentPage = 0;
    CirclePageIndicator indicator;
    LinearLayout customer_list_cv,logout_cv,profile_cv,business_cv;
    List<BannerData> bannerData = new ArrayList<>();
    String UserId;
    TextView id_tv;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_dashboard);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        SharedPreferences sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);
        UserId = sharedPreferences.getString("username", "");//        pref = new Preferences(context);
//        sharedPreferences = getSharedPreferences("asc_login_preference", MODE_PRIVATE);
//        UserId = sharedPreferences.getString("asc_userid", "");

        customer_list_cv = findViewById(R.id.customer_list_cv);
        logout_cv = findViewById(R.id.logout_cv);
        profile_cv = findViewById(R.id.profile_cv);
        business_cv = findViewById(R.id.business_cv);

        id_tv=findViewById(R.id.id_tv);
        id_tv.setText("Teacher- Welcome:- "+ UserId);

        indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        viewPager = findViewById(R.id.viewPager);

        getBanner();



    /*
        PagerAdapter adapter = new CustomAdapter(TeacherDashboard.this,imageId,imagesName);
        viewPager.setAdapter(adapter);
        viewPager.setClipToPadding(false);
//        viewPager.setPageMargin(24);
//        viewPager.setPadding(48, 8, 130, 8);
        viewPager.setOffscreenPageLimit(3);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                viewPager.post(new Runnable(){
                    @Override
                    public void run() {
                        viewPager.setCurrentItem((viewPager.getCurrentItem()+1)%imageId.length);
                    }
                });
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 3000, 3000);*/

        customer_list_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), TeacherStartClass.class);
                startActivity(intent);
//                Intent intent=new Intent(getApplicationContext(),AddCustomer.class);
//                startActivity(intent);
            }
        });


        profile_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),TeacherProfile.class);
                startActivity(intent);
            }
        });

        business_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),TeacherClassHistory.class);
                startActivity(intent);
            }
        });


        logout_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TeacherDashboard.this);
                alertDialogBuilder.setMessage("Are you sure you want to logout?");
                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
//                                SharedPreferences myPrefs = getSharedPreferences("asc_login_preference", MODE_PRIVATE);
//                                SharedPreferences.Editor editor = myPrefs.edit();
//                                editor.clear();
//                                editor.commit();
                                Intent intent = new Intent(getApplicationContext(), SplashScreen.class);
                                intent.putExtra("finish", true);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clean up all activities
                                startActivity(intent);

                            }
                        });
                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });


    }


    public void getBanner() {
        final ProgressDialog progressDialog = new ProgressDialog(TeacherDashboard.this);
        progressDialog.show();
        progressDialog.setMessage("Loading");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Api_Urls.BaseURL+"GetBannerImage", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>"," ");
                jsonString = jsonString.replace("<string xmlns=\"http://examcoach.in/\">"," ");
                jsonString = jsonString.replace("</string>"," ");
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
//                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                    String JsonInString = jsonObject.getString("BannerList");
                    bannerData = BannerData.createJsonInList(JsonInString);
                    viewPager.setAdapter(new AdapterForBanner(TeacherDashboard.this, bannerData));
                    indicator.setViewPager(viewPager);
                    indicator.setFillColor(Color.RED);

                    final float density = getResources().getDisplayMetrics().density;
//Set circle indicator radius
                    indicator.setRadius(5 * density);
                    // Auto start of viewpager
                    final Handler handler = new Handler();
                    final Runnable Update = new Runnable() {
                        public void run() {
                            if (currentPage == bannerData.size()) {
                                currentPage = 0;
                            }
                            viewPager.setCurrentItem(currentPage++, true);
                        }
                    };
                    Timer swipeTimer = new Timer();
                    swipeTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            handler.post(Update);
                        }
                    }, 5000, 3000);
                    // Pager listener over indicator
                    indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageSelected(int position) {
                            currentPage = position;
                        }
                        @Override
                        public void onPageScrolled(int pos, float arg1, int arg2) { }
                        @Override
                        public void onPageScrollStateChanged(int pos) { }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //  Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(TeacherDashboard.this, "Please check your Internet Connection! try again...", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(TeacherDashboard.this);
        requestQueue.add(stringRequest);
    }

}