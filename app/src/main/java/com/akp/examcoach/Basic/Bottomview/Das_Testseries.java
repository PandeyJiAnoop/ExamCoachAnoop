package com.akp.examcoach.Basic.Bottomview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.akp.examcoach.Basic.DashboardActivity;
import com.akp.examcoach.Basic.adapter.TestCatListAdapter;
import com.akp.examcoach.Basic.slidingmenu.EBookCategoryAdapter;
import com.akp.examcoach.Basic.slidingmenu.ELibraryMenu;
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

public class Das_Testseries extends AppCompatActivity {
    ImageView back_img;

    RecyclerView wallet_histroy2,wallet_histroy;
    ArrayList<HashMap<String, String>> arrayList2 = new ArrayList<>();
    String UserId,getExamcode,getType;
    ArrayList<HashMap<String, String>> arrayList1 = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_das__testseries);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("username", "");
        getExamcode=getIntent().getStringExtra("examtypecode");
        getType=getIntent().getStringExtra("type");

        back_img = findViewById(R.id.back_img);

        wallet_histroy2 = findViewById(R.id.wallet_histroy2);
        wallet_histroy = findViewById(R.id.wallet_histroy);

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));

            }
        });
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.rlBottom);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        displayView(5);

        getHistory();


        if (getType.equalsIgnoreCase("adapter")){
            getHistory1(getExamcode);
        }
        else {
            getHistory1("abcd");
        }



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
                startActivity(new Intent(getApplicationContext(), Das_LiveClass.class));
                break;
            case 4:
                startActivity(new Intent(getApplicationContext(), Das_Doubt.class));
                break;
            case 5:
                break;
            default:
                break;
        }
    }


    public void getHistory() {
        final ProgressDialog progressDialog = new ProgressDialog(Das_Testseries.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL + "ExamTestList", new Response.Listener<String>() {
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
                    JSONArray jsonArrayr = jsonObject.getJSONArray("ExamTestList");
                    for (int i = 0; i < jsonArrayr.length(); i++) {
                        JSONObject jsonObject1 = jsonArrayr.getJSONObject(i);
                        HashMap<String, String> hm = new HashMap<>();
                        hm.put("ExamType", jsonObject1.getString("ExamType"));
                        hm.put("ExamTypeCode", jsonObject1.getString("ExamTypeCode"));
                        hm.put("uid", UserId);
                        arrayList2.add(hm);
                    }
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(Das_Testseries.this, 1, GridLayoutManager.HORIZONTAL, false);
                    TestCatAdapter customerListAdapter = new TestCatAdapter(Das_Testseries.this, arrayList2);
                    wallet_histroy2.setLayoutManager(gridLayoutManager);
                    wallet_histroy2.setAdapter(customerListAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(Das_Testseries.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserId", UserId);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(Das_Testseries.this);
        requestQueue.add(stringRequest);
    }


    public void getHistory1(String excode){
        final ProgressDialog progressDialog = new ProgressDialog(Das_Testseries.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL + "AvailableTestList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("REsponse_alllist", response);

//                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>"," ");
                jsonString = jsonString.replace("<string xmlns=\"http://examcoach.in/\">", " ");
                jsonString = jsonString.replace("</string>"," ");
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray jsonArrayr = jsonObject.getJSONArray("TestList");
                    for (int i = 0; i < jsonArrayr.length(); i++) {
                        JSONObject jsonObject1 = jsonArrayr.getJSONObject(i);
                        HashMap<String, String> hm = new HashMap<>();
                        hm.put("ExamCode", jsonObject1.getString("ExamCode"));
                        hm.put("ExamType", jsonObject1.getString("ExamType"));
                        hm.put("ExamName", jsonObject1.getString("ExamName"));
                        hm.put("NoOfQuestions", jsonObject1.getString("NoOfQuestions"));
                        hm.put("ExamDurationInMins", jsonObject1.getString("ExamDurationInMins"));
                        hm.put("TotalMarks", jsonObject1.getString("TotalMarks"));
                        hm.put("TestStatus", jsonObject1.getString("TestStatus"));
                        hm.put("ExamDate", jsonObject1.getString("ExamDate"));
                        hm.put("ExamTime", jsonObject1.getString("ExamTime"));
                        hm.put("PackageStatus", jsonObject1.getString("PackageStatus"));
                        hm.put("TestStartStatus", jsonObject1.getString("TestStartStatus"));
                        hm.put("ResultStatus", jsonObject1.getString("ResultStatus"));
                        hm.put("id", UserId);
                        arrayList1.add(hm);
                    }
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(Das_Testseries.this, 1, GridLayoutManager.VERTICAL, false);
                    TestCatListAdapter customerListAdapter = new TestCatListAdapter(Das_Testseries.this, arrayList1);
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
                Toast.makeText(Das_Testseries.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("ExamTypeCode", excode);
                params.put("UserId", UserId);
                Log.d("dfdgdgdg","" +params);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(Das_Testseries.this);
        requestQueue.add(stringRequest);

    }
}