package com.akp.examcoach.Basic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.examcoach.Basic.Bottomview.Api_Urls;
import com.akp.examcoach.Basic.LiveClass.LiveClassBatchDetails;
import com.akp.examcoach.Basic.slidingmenu.AnimationHelper;
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
import java.util.List;
import java.util.Map;

public class CustomerCourseList extends AppCompatActivity {

    RecyclerView cust_recyclerView;
    ArrayList<HashMap<String, String>> arrayList1 = new ArrayList<>();
    String UserId;
    SwipeRefreshLayout srl_refresh;
    ImageView sliding_menu;
    TextView norecord_tv,title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_course_list);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("username", "");
        srl_refresh=findViewById(R.id.srl_refresh);
        norecord_tv=findViewById(R.id.norecord_tv);
        sliding_menu=findViewById(R.id.back_img);
        cust_recyclerView=findViewById(R.id.cust_recyclerView);
        title=findViewById(R.id.title);
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
                if (NetworkConnectionHelper.isOnline(CustomerCourseList.this)) {
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
                    Toast.makeText(CustomerCourseList.this, "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
                }
            }
        });


        getHistory();



    }
    public void getHistory() {
        final ProgressDialog progressDialog = new ProgressDialog(CustomerCourseList.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL+"Packagewisebatchlist", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Packagewisebatchlist",response);
                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>"," ");
                jsonString = jsonString.replace("<string xmlns=\"http://examcoach.in/\">"," ");
                jsonString = jsonString.replace("</string>"," ");
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    if (jsonObject.getString("msg").equalsIgnoreCase("Success")){
                        norecord_tv.setVisibility(View.GONE);
                        JSONArray jsonArrayr = jsonObject.getJSONArray("PackagesWiseBatchList");
                        for (int i = 0; i < jsonArrayr.length(); i++) {
                            title.setText("My Course List("+jsonArrayr.length()+")");
                            JSONObject jsonObject1 = jsonArrayr.getJSONObject(i);
                            HashMap<String, String> hm = new HashMap<>();
                            hm.put("OrderId", jsonObject1.getString("OrderId"));
                            hm.put("OrderDate", jsonObject1.getString("OrderDate"));
                            hm.put("PackageName", jsonObject1.getString("PackageName"));
                            hm.put("packageFor", jsonObject1.getString("packageFor"));
                            hm.put("BatchTitle", jsonObject1.getString("BatchTitle"));
                            hm.put("SubjectName", jsonObject1.getString("SubjectName"));
                            hm.put("Price", jsonObject1.getString("Price"));

                            hm.put("BatchId", jsonObject1.getString("BatchId"));
                            hm.put("BatchName", jsonObject1.getString("BatchTitle"));
                            hm.put("HomeCatId", jsonObject1.getString("homeCategoryId"));
                            hm.put("SubjectCode", jsonObject1.getString("Subjectcode"));

                            arrayList1.add(hm);
                        }
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(CustomerCourseList .this, 1);
                        CustomerCourseListAdapter customerListAdapter = new CustomerCourseListAdapter(CustomerCourseList.this, arrayList1);
                        cust_recyclerView.setLayoutManager(gridLayoutManager);
                        cust_recyclerView.setAdapter(customerListAdapter);
                    }
                    else {
                        norecord_tv.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("myTag", "message:"+error);
                Toast.makeText(CustomerCourseList.this, "Something went wrong!"+error, Toast.LENGTH_SHORT).show();
            }
        }) {  @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            HashMap<String, String> params = new HashMap<>();
            params.put("UserId",UserId);
            return params; }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(CustomerCourseList.this);
        requestQueue.add(stringRequest);

    }




    public class CustomerCourseListAdapter extends RecyclerView.Adapter<CustomerCourseListAdapter.VH> {
        Context context;
        List<HashMap<String,String>> arrayList;
        public CustomerCourseListAdapter(Context context, List<HashMap<String,String>> arrayList) {
            this.arrayList=arrayList;
            this.context=context;
        }

        @NonNull
        @Override
        public CustomerCourseListAdapter.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.dynamic_mycource, viewGroup, false);
            CustomerCourseListAdapter.VH viewHolder = new CustomerCourseListAdapter.VH(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull CustomerCourseListAdapter.VH vh, int i) {
            AnimationHelper.animatate(context,vh.itemView, R.anim.alfa_animation);
            vh.sr_no.setText(String.valueOf(" "+(i+1)));
            vh.tv.setText(arrayList.get(i).get("OrderId"));
            vh.tv1.setText(arrayList.get(i).get("OrderDate"));
            vh.tv2.setText(arrayList.get(i).get("Price"));
            vh.tv3.setText(arrayList.get(i).get("PackageName"));
            vh.tv4.setText(arrayList.get(i).get("packageFor"));
            vh.tv5.setText(arrayList.get(i).get("BatchTitle")+"-"+arrayList.get(i).get("SubjectName"));

            vh.view_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getApplicationContext(), LiveClassBatchDetails.class);
                    intent.putExtra("BatchId",arrayList.get(i).get("BatchId"));
                    intent.putExtra("BatchName",vh.tv5.getText().toString());
                    intent.putExtra("HomeCatId",arrayList.get(i).get("homeCategoryId"));
                    intent.putExtra("SubjectCode",arrayList.get(i).get("Subjectcode"));
                    intent.putExtra("BatchId",arrayList.get(i).get("BatchId"));
                    startActivity(intent);
//                    Toast.makeText(getApplicationContext(),"Coming Soon!",Toast.LENGTH_LONG).show();
//                    getSubjectId=getIntent().getStringExtra("BatchId");
//                    getSubjectName=getIntent().getStringExtra("BatchName");
//                    getHomeCatId=getIntent().getStringExtra("HomeCatId");
//                    getSubjectcode=getIntent().getStringExtra("SubjectCode");
//                    getbatchid=getIntent().getStringExtra("BatchId");



                }
            });


        }



        @Override
        public int getItemCount() {
            return arrayList.size();
        }
        public class VH extends RecyclerView.ViewHolder {
            TextView sr_no, tv,tv1,tv2,tv3,tv4,tv5;
            AppCompatButton view_btn;

            public VH(@NonNull View itemView) {
                super(itemView);
                sr_no = itemView.findViewById(R.id.sr_no);
                tv = itemView.findViewById(R.id.tv);
                tv1 = itemView.findViewById(R.id.tv1);
                tv2 = itemView.findViewById(R.id.tv2);
                tv3 = itemView.findViewById(R.id.tv3);
                tv4 = itemView.findViewById(R.id.tv4);
                tv5 = itemView.findViewById(R.id.tv5);
                view_btn = itemView.findViewById(R.id.view_btn);
            }
        }
    }
}