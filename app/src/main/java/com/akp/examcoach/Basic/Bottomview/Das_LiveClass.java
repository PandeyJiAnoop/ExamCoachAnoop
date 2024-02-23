package com.akp.examcoach.Basic.Bottomview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.examcoach.Basic.DashboardActivity;
import com.akp.examcoach.Basic.EbooksDashboard.LiveCategoryDetailsAdapterEbook;
import com.akp.examcoach.Basic.EbooksDashboard.LiveClassCategoryDetailsEbook;
import com.akp.examcoach.Basic.LiveClass.LiveCategoryAdapter;
import com.akp.examcoach.Basic.LoginActivity;
import com.akp.examcoach.Basic.NetworkConnectionHelper;
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

public class Das_LiveClass extends AppCompatActivity {
    ImageView back_img;
    String Url = "GetProjectList";
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    RecyclerView wallet_histroy;
    TextView txt_nodata,title_tv;
    String getAayogId,getAayogTitle,getHomeCatId;
    SwipeRefreshLayout srl_refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_das__live_class);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        back_img = findViewById(R.id.back_img);
        title_tv=findViewById(R.id.title_tv);
        getHomeCatId=getIntent().getStringExtra("HomeCatId");
        getAayogId=getIntent().getStringExtra("CourseId");
        getAayogTitle=getIntent().getStringExtra("CourseName");
        title_tv.setText(getAayogTitle);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.rlBottom);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        displayView(3);

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });

//        rl.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(), LiveClassDetails.class));
//
//            }
//        });


//        animBlink = AnimationUtils.loadAnimation(Das_LiveClass.this,
//                R.anim.blink);
//        blink.startAnimation(animBlink);
//        blink1.startAnimation(animBlink);
//        blink2.startAnimation(animBlink);
//        blink3.startAnimation(animBlink);


        wallet_histroy = findViewById(R.id.wallet_histroy);
        txt_nodata = findViewById(R.id.txt_nodata);
        srl_refresh = findViewById(R.id.srl_refresh);

        getHistory();

        srl_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkConnectionHelper.isOnline(Das_LiveClass.this)) {
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
                    Toast.makeText(Das_LiveClass.this, "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
                startActivity(new Intent(getApplicationContext(), Dash_Fedd.class));
                break;
            case 3:
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
        final ProgressDialog progressDialog = new ProgressDialog(Das_LiveClass.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL + "BatchList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

//                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();

                //Log.d("REsponse_Data", Constants.getSavedPreferences(getApplicationContext(), LOGINKEY, ""));
                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>"," ");
                jsonString = jsonString.replace("<string xmlns=\"http://examcoach.in/\">", " ");
                jsonString = jsonString.replace("</string>"," ");
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray jsonArrayr = jsonObject.getJSONArray("BatchList");
                    for (int i = 0; i < jsonArrayr.length(); i++) {
                        JSONObject jsonObject1 = jsonArrayr.getJSONObject(i);
                        HashMap<String, String> hm = new HashMap<>();
                        hm.put("BatchId", jsonObject1.getString("BatchId"));
                        hm.put("BatchName", jsonObject1.getString("BatchName"));
                        hm.put("CourseId", jsonObject1.getString("CourseId"));
                        hm.put("CourseName", jsonObject1.getString("CourseName"));
                        hm.put("HomeCatId", "1");
                        arrayList.add(hm);
                    }
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(Das_LiveClass.this, 1);
                    LiveCategoryDetailsAdapterEbook customerListAdapter = new LiveCategoryDetailsAdapterEbook(Das_LiveClass.this, arrayList);
                    wallet_histroy.setLayoutManager(gridLayoutManager);
                    wallet_histroy.setAdapter(customerListAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(Das_LiveClass.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("CourseId", getAayogId);
                params.put("HomeCatId", "1");
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(Das_LiveClass.this);
        requestQueue.add(stringRequest);
    }


    /*public void getHistory() {
        final ProgressDialog progressDialog = new ProgressDialog(Das_LiveClass.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL + "CourseList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.d("REsponse_Data", Constants.getSavedPreferences(getApplicationContext(), LOGINKEY, ""));
                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>"," ");
                jsonString = jsonString.replace("<string xmlns=\"http://examcoach.in/\">", " ");
                jsonString = jsonString.replace("</string>"," ");
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray jsonArrayr = jsonObject.getJSONArray("GetCourseList");
                    for (int i = 0; i < jsonArrayr.length(); i++) {
                        JSONObject jsonObject1 = jsonArrayr.getJSONObject(i);
                        HashMap<String, String> hm = new HashMap<>();
                        hm.put("Id", jsonObject1.getString("Id"));
                        hm.put("ExamType", jsonObject1.getString("ExamType"));
                        hm.put("imageUrl", jsonObject1.getString("imageUrl"));
                        arrayList.add(hm);
                    }
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(Das_LiveClass.this, 3);
                    LiveCategoryAdapter customerListAdapter = new LiveCategoryAdapter(Das_LiveClass.this, arrayList);
                    wallet_histroy.setLayoutManager(gridLayoutManager);
                    wallet_histroy.setAdapter(customerListAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(Das_LiveClass.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("AayogId", getAayogId);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(Das_LiveClass.this);
        requestQueue.add(stringRequest);
    }*/
}
