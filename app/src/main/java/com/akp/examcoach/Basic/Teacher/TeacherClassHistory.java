package com.akp.examcoach.Basic.Teacher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.examcoach.Basic.Bottomview.Api_Urls;
import com.akp.examcoach.Basic.NetworkConnectionHelper;
import com.akp.examcoach.Basic.NotificationList;
import com.akp.examcoach.Basic.adapter.NotificationListAdapter;
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

public class TeacherClassHistory extends AppCompatActivity {
TextView running_tv;
ImageView back_img;
String UserId;


    RecyclerView cust_recyclerView;
    ArrayList<HashMap<String, String>> arrayList1 = new ArrayList<>();
    SwipeRefreshLayout srl_refresh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_class_history);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        back_img=findViewById(R.id.back_img);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);
        UserId = sharedPreferences.getString("username", "");//        pref = new Preferences(context);
        running_tv=findViewById(R.id.running_tv);
        cust_recyclerView=findViewById(R.id.cust_recyclerView);
        srl_refresh=findViewById(R.id.srl_refresh);

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        srl_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkConnectionHelper.isOnline(TeacherClassHistory.this)) {
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
                    Toast.makeText(TeacherClassHistory.this, "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
                }
            }
        });



        GetTeacherLectureAPI("ALL","0",UserId);

//        running_tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(TeacherClassHistory.this);
//                builder.setMessage("Are you sure want to end Class?");
//                builder.setPositiveButton(Html.fromHtml("<font color='#097FC5'>Yes</font>"), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(TeacherClassHistory.this,"Class End!",Toast.LENGTH_LONG).show();
//                        running_tv.setText("Completed");
//                        running_tv.setTextColor(Color.GREEN);
//                    }
//                });
//                builder.setNegativeButton(Html.fromHtml("<font color='#D83226'>No</font>"), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//                builder.show();
//            }
//        });

    }












    private void GetTeacherLectureAPI(String action,String lid, String tid) {
        final ProgressDialog progressDialog = new ProgressDialog(TeacherClassHistory.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL + "GetTeacherLecture", new Response.Listener<String>() {
            //        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.Signature_BASE_URL + url, new  Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("sdsds", "sd" + response);
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", " ");
                jsonString = jsonString.replace("<string xmlns=\"http://examcoach.in/\">", " ");
                jsonString = jsonString.replace("</string>", " ");
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray jsonArrayr = jsonObject.getJSONArray("LectureList");
                    for (int i = 0; i < jsonArrayr.length(); i++) {
                        JSONObject jsonObject1 = jsonArrayr.getJSONObject(i);
                        HashMap<String, String> hm = new HashMap<>();
                        hm.put("LectureId", jsonObject1.getString("LectureId"));
                        hm.put("LectureCode", jsonObject1.getString("LectureCode"));
                        hm.put("TeacherCode", jsonObject1.getString("TeacherCode"));
                        hm.put("LectureTitle", jsonObject1.getString("LectureTitle"));
                        hm.put("LectureUrl", jsonObject1.getString("LectureUrl"));
                        hm.put("Description", jsonObject1.getString("Description"));
                        hm.put("LectureStatus", jsonObject1.getString("LectureStatus"));

                        hm.put("EntryDate", jsonObject1.getString("EntryDate"));
                        hm.put("StartDate", jsonObject1.getString("StartDate"));
                        hm.put("StartTime", jsonObject1.getString("StartTime"));

                        hm.put("EndDate", jsonObject1.getString("EndDate"));
                        hm.put("EndTime", jsonObject1.getString("EndTime"));

                        arrayList1.add(hm);
                    }
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(TeacherClassHistory .this, 1);
                    ClassHistoryAdpter customerListAdapter = new ClassHistoryAdpter(TeacherClassHistory.this, arrayList1);
                    cust_recyclerView.setLayoutManager(gridLayoutManager);
                    cust_recyclerView.setAdapter(customerListAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(TeacherClassHistory.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("Action", action);
                params.put("LectureId", lid);
                params.put("TeacherCode", tid);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(TeacherClassHistory.this);
        requestQueue.add(stringRequest);
    }

}