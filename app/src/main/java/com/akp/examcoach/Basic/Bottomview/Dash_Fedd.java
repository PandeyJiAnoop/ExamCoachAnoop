package com.akp.examcoach.Basic.Bottomview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.akp.examcoach.Basic.DashboardActivity;
import com.akp.examcoach.Basic.NetworkConnectionHelper;
import com.akp.examcoach.Basic.NotificationList;
import com.akp.examcoach.R;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Dash_Fedd extends AppCompatActivity {
    ImageView back_img;

    RecyclerView cust_recyclerView;
    ArrayList<HashMap<String, String>> arrayList1 = new ArrayList<>();
    String UserId;
    SwipeRefreshLayout srl_refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash__fedd);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("username", "");


        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));

            }
        });
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.rlBottom);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        displayView(2);



        cust_recyclerView=findViewById(R.id.cust_recyclerView);
        srl_refresh=findViewById(R.id.srl_refresh);

        srl_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkConnectionHelper.isOnline(Dash_Fedd.this)) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());
                            overridePendingTransition(0, 0);
                            srl_refresh.setRefreshing(false);
                        }
                    }, 2000);
                } else {
                    Toast.makeText(Dash_Fedd.this, "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
                }
            }
        });
        getHistory();


    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home:
                    displayView(1);
                    // hitFilterApi();
                    return true;
                case R.id.feed:
                    displayView(2);
                    return true;
                case R.id.liveclass:
                    displayView(3);
                    return true;
                case R.id.doubts:
                    displayView(4);
                    return true;
                case R.id.testseries:
                    displayView(5);
                    return true;
            }
            return false;
        }
    };

    private void displayView(int position) {
        switch (position) {
            case 1:
                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                break;
            case 2:
                break;
            case 3:
                startActivity(new Intent(getApplicationContext(), Das_LiveClass.class));
                break;
            case 4:
                startActivity(new Intent(getApplicationContext(), Das_Doubt.class));
                break;
            case 5:
                startActivity(new Intent(getApplicationContext(), Das_Testseries.class));

                break;
            default:
                break;
        }
    }


    public void getHistory() {
        final ProgressDialog progressDialog = new ProgressDialog(Dash_Fedd.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Api_Urls.BaseURL+"GetEArticle_details", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {                 Log.d("myTaga", "message:"+response);

                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>"," ");
                jsonString = jsonString.replace("<string xmlns=\"http://examcoach.in/\">"," ");
                jsonString = jsonString.replace("</string>"," ");
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray jsonArrayr = jsonObject.getJSONArray("EArticleList");
                    for (int i = 0; i < jsonArrayr.length(); i++) {
                        JSONObject jsonObject1 = jsonArrayr.getJSONObject(i);
                        HashMap<String, String> hm = new HashMap<>();
                        hm.put("artId", jsonObject1.getString("artId"));
                        hm.put("Title", jsonObject1.getString("Title"));
                        hm.put("Description", jsonObject1.getString("Description"));
                        hm.put("BookURL", jsonObject1.getString("BookURL"));
                        hm.put("imageUrl", jsonObject1.getString("imageUrl"));
                        hm.put("EntryDate", jsonObject1.getString("EntryDate"));
                        hm.put("ViewCount", jsonObject1.getString("ViewCount"));
                        hm.put("CommentCount", jsonObject1.getString("CommentCount"));
                        hm.put("uid", UserId);

                        arrayList1.add(hm);
                    }
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(Dash_Fedd .this, 1);
                    FeedAdapter customerListAdapter = new FeedAdapter(Dash_Fedd.this, arrayList1);
                    cust_recyclerView.setLayoutManager(gridLayoutManager);
                    cust_recyclerView.setAdapter(customerListAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                } }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(Dash_Fedd.this, "Something went wrong!"+error, Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(Dash_Fedd.this);
        requestQueue.add(stringRequest);

    }
}

