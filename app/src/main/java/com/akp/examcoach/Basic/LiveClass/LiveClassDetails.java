package com.akp.examcoach.Basic.LiveClass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.examcoach.Basic.Bottomview.Api_Urls;
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

public class LiveClassDetails extends AppCompatActivity {
    LinearLayout currentorder_rl,myorder_rl;
    TextView running_tv,myorder_tv;
    ImageView back_img;
    LinearLayout play_youtube_video_ll;
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    RecyclerView wallet_histroy;
    ArrayList<HashMap<String, String>> arrayList1 = new ArrayList<>();
    RecyclerView wallet_histroy1;
    String getSubjcetId,getSubjcetName,getBathchId;
    TextView title_tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_class_details);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

          back_img = findViewById(R.id.back_img);
          getSubjcetId=getIntent().getStringExtra("SubjectCode");
        getBathchId=getIntent().getStringExtra("BatchId");
          getSubjcetName=getIntent().getStringExtra("SubjectName");
//        play_youtube_video_ll=findViewById(R.id.play_youtube_video_ll);
        wallet_histroy = findViewById(R.id.wallet_histroy);
        wallet_histroy1 = findViewById(R.id.wallet_histroy1);
        title_tv=findViewById(R.id.title_tv);
        title_tv.setText(getSubjcetName);

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();

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
            }
        });

        myorder_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentorder_rl.setVisibility(View.GONE);
                myorder_rl.setVisibility(View.VISIBLE);

                myorder_tv.setBackgroundResource(R.color.red);
                running_tv.setBackgroundResource(R.color.grey);
            }
        });
        getHistory();
        getHistory1();
//        play_youtube_video_ll.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(getApplicationContext(), PlayVideo.class);
//                startActivity(intent);
//            }
//        });

    }
    public void getHistory() {
        final ProgressDialog progressDialog = new ProgressDialog(LiveClassDetails.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL + "ElearningList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("dfdsfsf22",""+response);
//                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                //Log.d("REsponse_Data", Constants.getSavedPreferences(getApplicationContext(), LOGINKEY, ""));
                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>"," ");
                jsonString = jsonString.replace("<string xmlns=\"http://examcoach.in/\">", " ");
                jsonString = jsonString.replace("</string>"," ");
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
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
                        hm.put("BatchId", jsonObject1.getString("BatchId"));
                        arrayList.add(hm);
                    }
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(LiveClassDetails.this, 1);
                    LiveClassDetailsAdapter customerListAdapter = new LiveClassDetailsAdapter(LiveClassDetails.this, arrayList);
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
                Toast.makeText(LiveClassDetails.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("SubjectId", getSubjcetId);
                params.put("BatchId", getBathchId);


                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(LiveClassDetails.this);
        requestQueue.add(stringRequest);
    }



    public void getHistory1() {
        final ProgressDialog progressDialog = new ProgressDialog(LiveClassDetails.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL + "ElibraryList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(getApplicationContext(),"dssdsd"+response,Toast.LENGTH_LONG).show();

                //Log.d("REsponse_Data", Constants.getSavedPreferences(getApplicationContext(), LOGINKEY, ""));
                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>"," ");
                jsonString = jsonString.replace("<string xmlns=\"http://examcoach.in/\">", " ");
                jsonString = jsonString.replace("</string>"," ");
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray jsonArrayr = jsonObject.getJSONArray("ElibraryList");
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
                        arrayList1.add(hm);
                    }
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(LiveClassDetails.this, 1);
                    LiveClassDetailsNotesAdapter customerListAdapter = new LiveClassDetailsNotesAdapter(LiveClassDetails.this, arrayList1);
                    wallet_histroy1.setLayoutManager(gridLayoutManager);
                    wallet_histroy1.setAdapter(customerListAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(LiveClassDetails.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("SubjectId", getSubjcetId);
                params.put("BatchId", getBathchId);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(LiveClassDetails.this);
        requestQueue.add(stringRequest);
    }
}
