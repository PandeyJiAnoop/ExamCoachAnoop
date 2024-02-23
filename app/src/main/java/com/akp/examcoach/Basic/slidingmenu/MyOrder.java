package com.akp.examcoach.Basic.slidingmenu;

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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.examcoach.Basic.Bottomview.Api_Urls;
import com.akp.examcoach.Basic.DashboardActivity;
import com.akp.examcoach.Basic.NetworkConnectionHelper;
import com.akp.examcoach.Basic.NotificationList;
import com.akp.examcoach.Basic.adapter.MyOrderListAdapter;
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

public class MyOrder extends AppCompatActivity {
    RecyclerView cust_recyclerView;
    ArrayList<HashMap<String, String>> arrayList1 = new ArrayList<>();
    String UserId;
    SwipeRefreshLayout srl_refresh;
    ImageView sliding_menu;
    String getPaackageId,getPacakgefor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("username", "");

        getPaackageId=getIntent().getStringExtra("package_id");
        getPacakgefor=getIntent().getStringExtra("package_for");


        sliding_menu=findViewById(R.id.sliding_menu);
        cust_recyclerView=findViewById(R.id.cust_recyclerView);
        srl_refresh=findViewById(R.id.srl_refresh);

        sliding_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              finish();
            }
        });

        srl_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkConnectionHelper.isOnline(MyOrder.this)) {
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
                    Toast.makeText(MyOrder.this, "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
                }
            }
        });


        getHistory();



    }
    public void getHistory() {
        final ProgressDialog progressDialog = new ProgressDialog(MyOrder.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL+"GetUserPackagesList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
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

                        hm.put("PaymentMode", jsonObject1.getString("PaymentMode"));
                        hm.put("OrderDate", jsonObject1.getString("OrderDate"));
                        hm.put("OrderStatus", jsonObject1.getString("OrderStatus"));

                        hm.put("PackageStatus", jsonObject1.getString("PackageStatus"));


                        arrayList1.add(hm);
                    }
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(MyOrder .this, 1);
                    MyOrderListAdapter customerListAdapter = new MyOrderListAdapter(MyOrder.this, arrayList1);
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
                Toast.makeText(MyOrder.this, "Something went wrong!"+error, Toast.LENGTH_SHORT).show();
            }
        }) {  @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            HashMap<String, String> params = new HashMap<>();
            params.put("UserId",UserId);
            params.put("packageFor",getPacakgefor);
            params.put("PackageId",getPaackageId);
            return params; }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(MyOrder.this);
        requestQueue.add(stringRequest);

    }
}