package com.akp.examcoach.Basic.LiveClass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.examcoach.Basic.Bottomview.Api_Urls;
import com.akp.examcoach.Basic.Bottomview.Das_Testseries;
import com.akp.examcoach.Basic.Bottomview.TestCatAdapter;
import com.akp.examcoach.Basic.QuizExam.StartQuizTest;
import com.akp.examcoach.Basic.QuizExam.StartQuizTestWeb;
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

import java.util.HashMap;
import java.util.Map;

public class ExamPrivacyPolicy extends AppCompatActivity {
ImageView back_img;
RecyclerView wallet_histroy;
String UserId,getexamcode,ExamCode,ExamType;
AppCompatButton login_btn;
TextView exam_name_tv,des_textview;
CheckBox cbWindows;
LinearLayout ll_start,ll_notstart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_privacy_policy);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("username", "");
        getexamcode=getIntent().getStringExtra("examcode");
        back_img=findViewById(R.id.back_img);
        exam_name_tv=findViewById(R.id.exam_name_tv);
        des_textview=findViewById(R.id.des_textview);
        cbWindows=findViewById(R.id.cbWindows);
        login_btn=findViewById(R.id.login_btn);
        ll_notstart=findViewById(R.id.ll_notstart);
        ll_start=findViewById(R.id.ll_start);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getHistory();



    }

    public void getHistory() {
        final ProgressDialog progressDialog = new ProgressDialog(ExamPrivacyPolicy.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL + "ExamInstructions", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("REsponse_Dataprivacy", response);

                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>"," ");
                jsonString = jsonString.replace("<string xmlns=\"http://examcoach.in/\">", " ");
                jsonString = jsonString.replace("</string>"," ");
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    ExamType=jsonObject.getString("ExamType");
                    ExamCode=jsonObject.getString("ExamCode");
                        String Description=jsonObject.getString("Description");
                        String TotalQuestion=jsonObject.getString("TotalQuestion");
                    String etstatus=jsonObject.getString("status");
                    Toast.makeText(getApplicationContext(),jsonObject.getString("msg"),Toast.LENGTH_LONG).show();

                    if (etstatus.equalsIgnoreCase("0")) {
                        ll_notstart.setVisibility(View.VISIBLE);
                        ll_start.setVisibility(View.GONE);
                    }
                    else if (etstatus.equalsIgnoreCase("1")){
                        ll_notstart.setVisibility(View.GONE);
                        ll_start.setVisibility(View.VISIBLE);
                        exam_name_tv.setText(ExamType+" ("+ExamCode+")");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            des_textview.setText(Html.fromHtml(Description, Html.FROM_HTML_MODE_COMPACT));
                        } else {
                            des_textview.setText(Html.fromHtml(Description));
                        }
                    }


                    login_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(!cbWindows.isChecked()){
                                    cbWindows.requestFocus();
                                    Toast.makeText(getApplicationContext(),"Accept our instructions!",Toast.LENGTH_LONG).show();
                                    //do some validation
                                }
                                else {
                                    if (etstatus.equalsIgnoreCase("1")) {
                                        ll_notstart.setVisibility(View.GONE);
                                        ll_start.setVisibility(View.VISIBLE);

                                        Intent intent = new Intent(getApplicationContext(), StartQuizTestWeb.class);
                                        intent.putExtra("ExamCode", ExamCode);
                                        intent.putExtra("ExamType", ExamType);
                                        startActivity(intent);

//                                        Intent intent = new Intent(getApplicationContext(), StartQuizTest.class);
//                                        intent.putExtra("ExamCode", ExamCode);
//                                        intent.putExtra("ExamType", ExamType);
//                                        startActivity(intent);

//                    Toast.makeText(getApplicationContext(),"Exam Start coming soon please wait!",Toast.LENGTH_LONG).show();
                                    }
                                    else if (etstatus.equalsIgnoreCase("0")){
                                        Toast.makeText(getApplicationContext(),"Exam Start coming soon please wait!",Toast.LENGTH_LONG).show();
                                        ll_notstart.setVisibility(View.VISIBLE);
                                        ll_start.setVisibility(View.GONE);
                                    }

                                }
                            }
                        });




                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(ExamPrivacyPolicy.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserId", UserId);
                params.put("ExamTypeCode",getexamcode);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(ExamPrivacyPolicy.this);
        requestQueue.add(stringRequest);
    }

}