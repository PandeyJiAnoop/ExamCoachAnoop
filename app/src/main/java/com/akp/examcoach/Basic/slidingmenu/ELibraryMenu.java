package com.akp.examcoach.Basic.slidingmenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.akp.examcoach.Basic.Bottomview.Api_Urls;
import com.akp.examcoach.Basic.DashboardActivity;
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

public class ELibraryMenu extends AppCompatActivity {
    ImageView sliding_menu;
    RecyclerView wallet_histroy2,wallet_histroy1;
    ArrayList<HashMap<String, String>> arrayList1 = new ArrayList<>();
    ArrayList<HashMap<String, String>> arrayList2 = new ArrayList<>();
    String getId,getType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e_library_menu);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getId=getIntent().getStringExtra("SubjectCode");
        getType=getIntent().getStringExtra("a_type");
        sliding_menu=findViewById(R.id.sliding_menu);
        wallet_histroy2 = findViewById(R.id.wallet_histroy2);
        wallet_histroy1 = findViewById(R.id.wallet_histroy1);
        sliding_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), DashboardActivity.class);
                startActivity(intent );
            }
        });

        getHistory();


        if (getType.equalsIgnoreCase("adapter")){
            getHistory1(getId);
        }
        if (getType.equalsIgnoreCase("das")){
            getHistory1("0");
        }


    }



    public void getHistory() {
        final ProgressDialog progressDialog = new ProgressDialog(ELibraryMenu.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Api_Urls.BaseURL + "SubjectListAll", new Response.Listener<String>() {
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
                    JSONArray jsonArrayr = jsonObject.getJSONArray("SubjectList");
                    for (int i = 0; i < jsonArrayr.length(); i++) {
                        JSONObject jsonObject1 = jsonArrayr.getJSONObject(i);
                        HashMap<String, String> hm = new HashMap<>();
                        hm.put("Subjectcode", jsonObject1.getString("Subjectcode"));
                        hm.put("SubjectName", jsonObject1.getString("SubjectName"));
                        arrayList2.add(hm);
                    }
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(ELibraryMenu.this, 1, GridLayoutManager.HORIZONTAL, false);
                    EBookCategoryAdapter customerListAdapter = new EBookCategoryAdapter(ELibraryMenu.this, arrayList2);
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
                Toast.makeText(ELibraryMenu.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(ELibraryMenu.this);
        requestQueue.add(stringRequest);
    }

    public void getHistory1(String id) {
        final ProgressDialog progressDialog = new ProgressDialog(ELibraryMenu.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL + "EBookList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
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
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(ELibraryMenu.this, 2);
                    EBookAdapter customerListAdapter = new EBookAdapter(ELibraryMenu.this, arrayList1);
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
                Toast.makeText(ELibraryMenu.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("SubjectId", id);

                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(ELibraryMenu.this);
        requestQueue.add(stringRequest);
    }
}