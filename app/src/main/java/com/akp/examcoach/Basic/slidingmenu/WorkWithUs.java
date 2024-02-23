package com.akp.examcoach.Basic.slidingmenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.akp.examcoach.Basic.Bottomview.Api_Urls;
import com.akp.examcoach.Basic.NetworkConnectionHelper;
import com.akp.examcoach.Basic.NotificationList;
import com.akp.examcoach.Basic.WorkWithUsAdapter.WorkWithUsQuestionListAdapter;
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

public class WorkWithUs extends AppCompatActivity {

    ImageView back_img;
    ArrayList<String> StateName = new ArrayList<>();
    ArrayList<String> StateId = new ArrayList<>();
    ArrayList<String> CityName = new ArrayList<>();
    ArrayList<String> CityId = new ArrayList<>();
    String stateid, cityid, uplineid;
    Spinner sp_state,
            sp_city;
    String[] spinnercity = {"Select Course"};
    String UserId;
    RecyclerView cust_recyclerView;
    SwipeRefreshLayout srl_refresh;
    ArrayList<HashMap<String, String>> arrayList1 = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_with_us);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("username", "");

        cust_recyclerView=findViewById(R.id.cust_recyclerView);
//        srl_refresh=findViewById(R.id.srl_refresh);


//        q1_img=findViewById(R.id.q1_img);
//        q2_img=findViewById(R.id.q2_img);
        back_img=findViewById(R.id.back_img);
        sp_state=findViewById(R.id.et_state);
        sp_city=findViewById(R.id.et_city);



//        srl_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                if (NetworkConnectionHelper.isOnline(getApplicationContext())) {
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            finish();
//                            overridePendingTransition(0, 0);
//                            startActivity(getIntent());
//                            overridePendingTransition(0, 0);
//                            srl_refresh.setRefreshing(false);
//                        }
//                    }, 2000);
//                } else {
//                    Toast.makeText(getApplicationContext(), "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });


        getHistory();





        sp_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {

                    for (int j = 0; j <= StateId.size(); j++) {
                        if (sp_state.getSelectedItem().toString().equalsIgnoreCase(StateName.get(j))) {
                            // position = i;
                            stateid = StateId.get(j - 1);
                            break;
                        } } } }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });



        sp_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    for (int j = 0; j <= CityId.size(); j++) {
                        if (sp_city.getSelectedItem().toString().equalsIgnoreCase(CityName.get(j))) {
                            cityid = CityId.get(j - 1);
                            break;
                        } } } }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }});


        getState();
        getCity();









       /* q1_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),StudentWorkUpload.class);
                startActivity(intent);
            }
        });
        q1_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),StudentWorkUpload.class);
                startActivity(intent);
            }
        });*/

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }














    void getState() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL+"SubjectListAll", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", " ");
                jsonString = jsonString.replace("<string xmlns=\"http://examcoach.in/\">", " ");
                jsonString = jsonString.replace("</string>", " ");
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    StateName.add("Select Subject");
                    JSONArray jsonArray = jsonObject.getJSONArray("SubjectList");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        StateId.add(jsonObject1.getString("Subjectcode"));
                        String statename = jsonObject1.getString("SubjectName");
                        StateName.add(statename);
                    }

                    sp_state.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, StateName));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    void getCity() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL + "CourseList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", " ");
                jsonString = jsonString.replace("<string xmlns=\"http://examcoach.in/\">", " ");
                jsonString = jsonString.replace("</string>", " ");
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    CityName.add("Select Course");
                    JSONArray jsonArray = jsonObject.getJSONArray("GetCourseList");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            CityId.add(jsonObject1.getString("Id"));
                            String Cityname = jsonObject1.getString("ExamType");
                            CityName.add(Cityname);
                        }
                    sp_city.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, CityName));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("AayogId", "2");
                return params;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }


    public void getHistory() {
        final ProgressDialog progressDialog = new ProgressDialog(WorkWithUs.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL+"WorkWithUs_QuestionList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>"," ");
                jsonString = jsonString.replace("<string xmlns=\"http://examcoach.in/\">"," ");
                jsonString = jsonString.replace("</string>"," ");
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray jsonArrayr = jsonObject.getJSONArray("QuestionList");
                    for (int i = 0; i < jsonArrayr.length(); i++) {
                        JSONObject jsonObject1 = jsonArrayr.getJSONObject(i);
                        HashMap<String, String> hm = new HashMap<>();
                        hm.put("QuestionCode", jsonObject1.getString("QuestionCode"));
                        hm.put("CourseId", jsonObject1.getString("CourseId"));
                        hm.put("CourseName", jsonObject1.getString("CourseName"));
                        hm.put("SubjectId", jsonObject1.getString("SubjectId"));
                        hm.put("SubjectName", jsonObject1.getString("SubjectName"));
                        hm.put("Title", jsonObject1.getString("Title"));
                        hm.put("Message", jsonObject1.getString("Message"));
                        arrayList1.add(hm);
                    }
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
                    WorkWithUsQuestionListAdapter customerListAdapter = new WorkWithUsQuestionListAdapter(getApplicationContext(), arrayList1);
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
                Toast.makeText(getApplicationContext(), "Something went wrong!"+error, Toast.LENGTH_SHORT).show();
            }
        }) {  @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            HashMap<String, String> params = new HashMap<>();
            params.put("UserId",UserId);
            params.put("CourseId","8");
            params.put("SubjectId","12");

            return params; }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }
}