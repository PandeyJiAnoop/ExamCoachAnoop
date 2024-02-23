 package com.akp.examcoach.Basic.LiveClass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.examcoach.Basic.Bottomview.Api_Urls;
import com.akp.examcoach.Basic.Bottomview.Das_Doubt;
import com.akp.examcoach.Basic.Bottomview.Das_Testseries;
import com.akp.examcoach.Basic.Bottomview.Dash_Fedd;
import com.akp.examcoach.Basic.DashboardActivity;
import com.akp.examcoach.Basic.NetworkConnectionHelper;
import com.akp.examcoach.Basic.slidingmenu.AnimationHelper;
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
import java.util.List;
import java.util.Map;

public class LiveClassBatchDetails extends AppCompatActivity {
    ImageView back_img;
    String Url = "GetProjectList";
    ArrayList<HashMap<String, String>> arrayList4 = new ArrayList<>();
    RecyclerView wallet_histroy,wallet_histroy2,wallet_histroy1;
    ImageView txt_nodata;
    String getSubjectId,getSubjectName,getType,getSubjectcode,getbatchid;
    TextView totle_tv;
    ArrayList<HashMap<String, String>> arrayList1 = new ArrayList<>();
    ArrayList<HashMap<String, String>> arrayList2 = new ArrayList<>();
    LinearLayout currentorder_rl,myorder_rl;
    TextView running_tv,myorder_tv;
    SwipeRefreshLayout srl_refresh;
    String  UserId,getHomeCatId;
    LiveClassDetailsAdapter customerListAdapter;
    LiveClassDetailsNotesAdapter customerListAdapter1;
    LinearLayout all_ll;
    public static AppCompatButton previousSeletedButton = null;
    ImageView txt_nodata2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_class_batch_details);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("username", "");

        all_ll=findViewById(R.id.all_ll);
        txt_nodata2=findViewById(R.id.txt_nodata2);
        back_img = findViewById(R.id.back_img);
        getSubjectId=getIntent().getStringExtra("BatchId");
        getSubjectName=getIntent().getStringExtra("BatchName");
        getHomeCatId=getIntent().getStringExtra("HomeCatId");
        getSubjectcode=getIntent().getStringExtra("SubjectCode");
        getbatchid=getIntent().getStringExtra("BatchId");

        getType=getIntent().getStringExtra("a_type");
//        Toast.makeText(LiveClassBatchDetails.this, getSubjectId, Toast.LENGTH_SHORT).show();
        totle_tv=findViewById(R.id.totle_tv);
        totle_tv.setText(getSubjectName);


        wallet_histroy2 = findViewById(R.id.wallet_histroy2);
        wallet_histroy1 = findViewById(R.id.wallet_histroy1);


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.rlBottom);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        displayView(3);


        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             finish();
            }
        });

        all_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }
        });


        currentorder_rl=findViewById(R.id.currentorder_rl);
        myorder_rl=findViewById(R.id.myorder_rl);
        running_tv=findViewById(R.id.running_tv);
        myorder_tv=findViewById(R.id.myorder_tv);
        running_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentorder_rl.setVisibility(View.VISIBLE);
                myorder_rl.setVisibility(View.GONE);
                running_tv.setBackgroundResource(R.color.red);
                myorder_tv.setBackgroundResource(R.color.grey);
                txt_nodata.setVisibility(View.GONE);
            }
        });

        myorder_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentorder_rl.setVisibility(View.GONE);
                myorder_rl.setVisibility(View.VISIBLE);

                myorder_tv.setBackgroundResource(R.color.red);
                running_tv.setBackgroundResource(R.color.grey);
                txt_nodata2.setVisibility(View.GONE);
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



        srl_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkConnectionHelper.isOnline(LiveClassBatchDetails.this)) {
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
                    Toast.makeText(LiveClassBatchDetails.this, "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
                }
            }
        });
        getHistory(getSubjectId);
        getHistory2("",getSubjectId);
        getHistory1("",getSubjectId);

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


    public void getHistory(String a_subcode) {
        final ProgressDialog progressDialog = new ProgressDialog(LiveClassBatchDetails.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL+"SubjectList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("SubjectList", response);
                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>"," ");
                jsonString = jsonString.replace("<string xmlns=\"http://examcoach.in/\">", " ");
                jsonString = jsonString.replace("</string>"," ");
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray jsonArrayr = jsonObject.getJSONArray("SubjectList");
                    for (int i = 0; i < jsonArrayr.length(); i++) {
                        JSONObject jsonObject1 = jsonArrayr.getJSONObject(i);
                        HashMap<String, String> hm = new HashMap<>();
                        hm.put("Subjectcode", jsonObject1.getString("Subjectcode"));
                        hm.put("SubjectName", jsonObject1.getString("SubjectName"));
                        hm.put("BatchId", jsonObject1.getString("BatchId"));
                        hm.put("BatchName", jsonObject1.getString("BatchName"));
                        hm.put("Streams", jsonObject1.getString("Streams"));
                        hm.put("HomeCatId", "1");
                        arrayList4.add(hm);
                    }
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(LiveClassBatchDetails.this, 1, GridLayoutManager.HORIZONTAL, false);
                    LiveBatchAdapter customerListAdapter = new LiveBatchAdapter(LiveClassBatchDetails.this, arrayList4);
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
                Toast.makeText(LiveClassBatchDetails.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("BatchId",a_subcode);
                params.put("HomeCatId","1");
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(LiveClassBatchDetails.this);
        requestQueue.add(stringRequest);
    }





    public void getHistory2(String sub_id,String batchid) {
        final ProgressDialog progressDialog = new ProgressDialog(LiveClassBatchDetails.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL + "ElearningList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ElearningList",""+response);
//                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>"," ");
                jsonString = jsonString.replace("<string xmlns=\"http://examcoach.in/\">", " ");
                jsonString = jsonString.replace("</string>"," ");
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    if (jsonObject.getString("msg").equalsIgnoreCase("failed")){
                        txt_nodata2.setVisibility(View.VISIBLE);
//                        txt_nodata.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(),jsonObject.getString("msg"),Toast.LENGTH_LONG).show();
                    }
                    else {
                        txt_nodata2.setVisibility(View.GONE);
//                        txt_nodata.setVisibility(View.GONE);
                        JSONArray jsonArrayr = jsonObject.getJSONArray("ElearningList");
                        for (int i = 0; i < jsonArrayr.length(); i++) {
                            JSONObject jsonObject1 = jsonArrayr.getJSONObject(i);
                            HashMap<String, String> hm = new HashMap<>();
                            hm.put("Title", jsonObject1.getString("Title"));
                            hm.put("thumbnailUrl", jsonObject1.getString("thumbnailUrl"));
                            hm.put("TopicName", jsonObject1.getString("TopicName"));
                            hm.put("SubjectName", jsonObject1.getString("SubjectName"));
                            hm.put("CourseName", jsonObject1.getString("CourseName"));
                            hm.put("BookURL", jsonObject1.getString("BookURL"));
                            hm.put("Description", jsonObject1.getString("Description"));
                            hm.put("EleaId", jsonObject1.getString("EleaId"));
                            hm.put("IsPaidStatus", jsonObject1.getString("IsPaidStatus"));
                            hm.put("BatchId", jsonObject1.getString("BatchId"));
                            hm.put("userid", UserId);
                            hm.put("HomeCatId", "1");
                            arrayList2.add(hm);
                        }
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(LiveClassBatchDetails.this, 1);
                        customerListAdapter = new LiveClassDetailsAdapter(LiveClassBatchDetails.this, arrayList2);
                        wallet_histroy2.setLayoutManager(gridLayoutManager);
                        wallet_histroy2.setAdapter(customerListAdapter);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(LiveClassBatchDetails.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("SubjectId", sub_id);
                params.put("BatchId", batchid);
                params.put("HomeCatId","1");

                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(LiveClassBatchDetails.this);
        requestQueue.add(stringRequest);
    }



    public void getHistory1(String sub_id,String batchid) {
        final ProgressDialog progressDialog = new ProgressDialog(LiveClassBatchDetails.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL + "EClassList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(getApplicationContext(),"dssdsd"+response,Toast.LENGTH_LONG).show();

                Log.d("EClassList", response);
                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>"," ");
                jsonString = jsonString.replace("<string xmlns=\"http://examcoach.in/\">", " ");
                jsonString = jsonString.replace("</string>"," ");
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    if (jsonObject.getString("msg").equalsIgnoreCase("failed")){
                        Toast.makeText(getApplicationContext(),jsonObject.getString("msg"),Toast.LENGTH_LONG).show();
//                        txt_nodata2.setVisibility(View.VISIBLE);
                        txt_nodata.setVisibility(View.VISIBLE);
                    }
                    else {
//                        txt_nodata2.setVisibility(View.GONE);
                        txt_nodata.setVisibility(View.GONE);
                    JSONArray jsonArrayr = jsonObject.getJSONArray("EClassList");
                    for (int i = 0; i < jsonArrayr.length(); i++) {
                        JSONObject jsonObject1 = jsonArrayr.getJSONObject(i);
                        HashMap<String, String> hm = new HashMap<>();
                        hm.put("Title", jsonObject1.getString("Title"));
                        hm.put("thumbnailUrl", jsonObject1.getString("thumbnailUrl"));
                        hm.put("TopicName", jsonObject1.getString("TopicName"));
                        hm.put("SubjectName", jsonObject1.getString("SubjectName"));
                        hm.put("CourseName", jsonObject1.getString("CourseName"));
                        hm.put("BookURL", jsonObject1.getString("BookURL"));
                        hm.put("Description", jsonObject1.getString("Description"));
                        hm.put("IsPaidStatus", jsonObject1.getString("IsPaidStatus"));
                        hm.put("BatchId", batchid);
                        hm.put("userid", UserId);
                        hm.put("HomeCatId", "1");
                        arrayList1.add(hm);
                    }
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(LiveClassBatchDetails.this, 1);
                    customerListAdapter1 = new LiveClassDetailsNotesAdapter(LiveClassBatchDetails.this, arrayList1);
                    wallet_histroy1.setLayoutManager(gridLayoutManager);
                    wallet_histroy1.setAdapter(customerListAdapter1);
                }} catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(LiveClassBatchDetails.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("SubjectId", sub_id);
                params.put("BatchId", batchid);
                params.put("HomeCatId","1");
                Log.d("gdfdparam",""+params);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(LiveClassBatchDetails.this);
        requestQueue.add(stringRequest);
    }














    public class LiveBatchAdapter extends RecyclerView.Adapter<LiveBatchAdapter.VH> {
        Context context;
        List<HashMap<String,String>> arrayList;

        public LiveBatchAdapter(Context context, List<HashMap<String,String>> arrayList) {
            this.arrayList=arrayList;
            this.context=context;
        }

        @NonNull
        @Override
        public LiveBatchAdapter.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.dynamic_live_batchlist, viewGroup, false);
            LiveBatchAdapter.VH viewHolder = new LiveBatchAdapter.VH(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull LiveBatchAdapter.VH vh, int i) {
            AnimationHelper.animatate(context,vh.itemView, R.anim.alfa_animation);
            vh.cat_btn.setText(arrayList.get(i).get("SubjectName"));
//        Animation animBlink = AnimationUtils.loadAnimation(context,
//                R.anim.blink);
//        vh.cat_name.startAnimation(animBlink);

//        if (arrayList.get(i).get("imageUrl").equalsIgnoreCase("")){
////            Toast.makeText(context,"Image not found!",Toast.LENGTH_LONG).show();
//        }
//        else {
//            Glide.with(context).load(arrayList.get(i).get("imageUrl")).error(R.drawable.logo).into(vh.cat_img);
//        }
            //Set on click listener for each item view





            vh.cat_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ( ( previousSeletedButton == null ) || ( previousSeletedButton == vh.cat_btn ) ) {
                        vh.cat_btn.setBackgroundColor( ContextCompat.getColor( context, R.color.green) );
                    }
                    else{
                        previousSeletedButton.setBackgroundColor( ContextCompat.getColor( context, R.color.grey) );
                        vh.cat_btn.setBackgroundColor( ContextCompat.getColor( context, R.color.green) );
                    }
                    previousSeletedButton = vh.cat_btn;
                    arrayList2.clear();
                    arrayList1.clear();
//                    customerListAdapter.notifyDataSetChanged();
//                    customerListAdapter1.notifyDataSetChanged();
                    getHistory2(arrayList.get(i).get("Subjectcode"),arrayList.get(i).get("BatchId"));
                    getHistory1(arrayList.get(i).get("Subjectcode"),arrayList.get(i).get("BatchId"));
                }
            });


        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        public class VH extends RecyclerView.ViewHolder {
            AppCompatButton cat_btn;
            public VH(@NonNull View itemView) {
                super(itemView);
                cat_btn = itemView.findViewById(R.id.cat_btn);
            }
        }
    }
}