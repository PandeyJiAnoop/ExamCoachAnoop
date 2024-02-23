package com.akp.examcoach.Basic.planpackage;

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
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.akp.examcoach.Basic.Bottomview.Api_Urls;
import com.akp.examcoach.Basic.DashboardActivity;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AllPlanPackage extends AppCompatActivity {
    RecyclerView cust_recyclerView;
    ArrayList<HashMap<String, String>> arrayList1 = new ArrayList<>();
    String UserId;
    SwipeRefreshLayout srl_refresh;
    ImageView sliding_menu;
    String getBatchId,gettype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_plan_package);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("username", "");

        getBatchId=getIntent().getStringExtra("batch_id");
        gettype=getIntent().getStringExtra("type");
        Log.d("mshhh",""+UserId+getBatchId+gettype);

        sliding_menu=findViewById(R.id.back_img);
        cust_recyclerView=findViewById(R.id.cust_recyclerView);
        srl_refresh=findViewById(R.id.srl_refresh);


        sliding_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), DashboardActivity.class);
                startActivity(intent);
            }
        });

        srl_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkConnectionHelper.isOnline(AllPlanPackage.this)) {
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
                    Toast.makeText(AllPlanPackage.this, "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (gettype.equalsIgnoreCase("liveclass")){
            getHistory(UserId,getBatchId,"ELearning");
        }
        else if (gettype.equalsIgnoreCase("menu"))
        {            getHistory(UserId,"","");

        }
    }
    public void getHistory(String userid,String bathchid,String pacfor) {
        final ProgressDialog progressDialog = new ProgressDialog(AllPlanPackage.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL+"GetPackagesList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("xsdsds","s"+response);

//                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>"," ");
                jsonString = jsonString.replace("<string xmlns=\"http://examcoach.in/\">"," ");
                jsonString = jsonString.replace("</string>"," ");
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray jsonArrayr = jsonObject.getJSONArray("PackagesList");
                    for (int i = 0; i < jsonArrayr.length(); i++) {
                        JSONObject jsonObject1 = jsonArrayr.getJSONObject(i);
                        HashMap<String, String> hm = new HashMap<>();
                        hm.put("PackageId", jsonObject1.getString("PackageId"));
                        hm.put("PackageName", jsonObject1.getString("PackageName"));
                        hm.put("NoOfTest", jsonObject1.getString("NoOfTest"));
                        hm.put("Price", jsonObject1.getString("Price"));
                        hm.put("ValidTill", jsonObject1.getString("ValidTill"));
                        hm.put("CourseName", jsonObject1.getString("CourseName"));
                        hm.put("packageType", jsonObject1.getString("packageType"));
                        hm.put("description", jsonObject1.getString("description"));
                        hm.put("PackagePurchaseStatus", jsonObject1.getString("PackagePurchaseStatus"));
                        hm.put("PackageFor", jsonObject1.getString("PackageFor"));
                        hm.put("Userid", UserId);
                        arrayList1.add(hm);
                    }
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(AllPlanPackage .this, 1);
                    AllPackageListAdapter customerListAdapter = new AllPackageListAdapter(AllPlanPackage.this, arrayList1);
                    cust_recyclerView.setLayoutManager(gridLayoutManager);
                    cust_recyclerView.setAdapter(customerListAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                } }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("myTag", "message:"+error);
                Toast.makeText(AllPlanPackage.this, "Something went wrong!"+error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserId", userid);
                params.put("BatchId", bathchid);
                params.put("packageFor", pacfor);
                return params;
            }
        };;
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(AllPlanPackage.this);
        requestQueue.add(stringRequest);
    }
}