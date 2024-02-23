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
import android.widget.TextView;
import android.widget.Toast;

import com.akp.examcoach.Basic.Bottomview.Api_Urls;
import com.akp.examcoach.Basic.DashboardActivity;
import com.akp.examcoach.Basic.NetworkConnectionHelper;
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

public class CustomerOrderDeatils extends AppCompatActivity {
    RecyclerView cust_recyclerView;
    ArrayList<HashMap<String, String>> arrayList1 = new ArrayList<>();
    String UserId;
    SwipeRefreshLayout srl_refresh;
    ImageView sliding_menu;
    ImageView norecord_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_order_deatils);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("username", "");

        sliding_menu=findViewById(R.id.sliding_menu);
        cust_recyclerView=findViewById(R.id.cust_recyclerView);
        srl_refresh=findViewById(R.id.srl_refresh);
        norecord_tv=findViewById(R.id.norecord_tv);
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
                if (NetworkConnectionHelper.isOnline(CustomerOrderDeatils.this)) {
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
                    Toast.makeText(CustomerOrderDeatils.this, "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
                }
            }
        });


        getHistory();



    }
    public void getHistory() {
        final ProgressDialog progressDialog = new ProgressDialog(CustomerOrderDeatils.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL+"OrderList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("OrderList",response);
                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>"," ");
                jsonString = jsonString.replace("<string xmlns=\"http://examcoach.in/\">"," ");
                jsonString = jsonString.replace("</string>"," ");
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    if (jsonObject.getString("msg").equalsIgnoreCase("Success")){
                        JSONArray jsonArrayr = jsonObject.getJSONArray("OrderList");
                        for (int i = 0; i < jsonArrayr.length(); i++) {
                            JSONObject jsonObject1 = jsonArrayr.getJSONObject(i);
                            HashMap<String, String> hm = new HashMap<>();
                            hm.put("OrderId", jsonObject1.getString("OrderId"));
                            hm.put("OrderDate", jsonObject1.getString("OrderDate"));
                            hm.put("PackageName", jsonObject1.getString("PackageName"));
                            hm.put("packageFor", jsonObject1.getString("packageFor"));
                            hm.put("PackageId", jsonObject1.getString("PackageId"));
                            hm.put("PackageAmount", jsonObject1.getString("PackageAmount"));
                            hm.put("TotalAmount", jsonObject1.getString("TotalAmount"));
                            hm.put("PaymentMode", jsonObject1.getString("PaymentMode"));

                            hm.put("OrderStatus", jsonObject1.getString("OrderStatus"));
                            hm.put("PaymentStatus", jsonObject1.getString("PaymentStatus"));
                            hm.put("validity", jsonObject1.getString("validity"));

//                        "OrderId":"EC2022260",
//                                "OrderDate":"11/21/2022 2:00:18 PM",
//                                "PackageName":"BALYAVASTHA COMPLETE COURSE",
//                                "packageFor":"ELibrary",
//                                "PackageId":"LI00036",
//                                "PackageAmount":"499.00",
//                                "TotalAmount":"499.00",
//                                "PaymentMode":"ONLINE",
//                                "OrderStatus":"Pending",
//                                "PaymentStatus":"FAILED",
//                                "validity":"11/21/2023 2:00:18 PM"

                            arrayList1.add(hm);
                        }
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(CustomerOrderDeatils .this, 1);
                        AllOrderListAdapter customerListAdapter = new AllOrderListAdapter(CustomerOrderDeatils.this, arrayList1);
                        cust_recyclerView.setLayoutManager(gridLayoutManager);
                        cust_recyclerView.setAdapter(customerListAdapter);
                    }
                    else {
                        norecord_tv.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    norecord_tv.setVisibility(View.VISIBLE);
                    e.printStackTrace();
                } }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                norecord_tv.setVisibility(View.VISIBLE);
                progressDialog.dismiss();
                Log.d("myTag", "message:"+error);
                Toast.makeText(CustomerOrderDeatils.this, "Something went wrong!"+error, Toast.LENGTH_SHORT).show();
            }
        }) {  @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            HashMap<String, String> params = new HashMap<>();
            params.put("UserId",UserId);
            params.put("OrderId","");
            params.put("FromDate","");
            params.put("ToDate","");

            return params; }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(CustomerOrderDeatils.this);
        requestQueue.add(stringRequest);

    }
}