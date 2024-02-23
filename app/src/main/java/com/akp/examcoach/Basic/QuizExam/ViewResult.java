package com.akp.examcoach.Basic.QuizExam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.HashMap;
import java.util.Map;

public class ViewResult extends AppCompatActivity implements View.OnClickListener  {
    TextView tvCorrect1, tvWrong1, tvResult1, tvRank1, tvtotalquestions1,tvTotalMarks,tvObtainMarks,tvDate;
    RecyclerView rcvPdfList;
    SharedPreferences sharedpreferences;
    String ExamCode = "";
    String BranchCode = "",UserId;
    RelativeLayout relativeLayout;
    ImageView ivBack;


    private SharedPreferences.Editor login_editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_result);
        findViewById();
        GetExamResult();
        setListner();

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), DashboardActivity.class);
                startActivity(intent);
            }
        });

    }

    private void setListner() {
        relativeLayout.setOnClickListener(this);

    }

    private void findViewById() {
        ivBack=findViewById(R.id.ivBack);
        tvDate=findViewById(R.id.tvDate);


        tvCorrect1 = findViewById(R.id.tvCorrect1);
        tvWrong1 = findViewById(R.id.tvtvWrong1);
        tvResult1 = findViewById(R.id.tvResult1);
        rcvPdfList = findViewById(R.id.rcvWrongWuestionList);
        relativeLayout = findViewById(R.id.relativeLayout);
        tvRank1 = findViewById(R.id.tvRank1);
        tvtotalquestions1 = findViewById(R.id.tvtotalquestions1);
        tvTotalMarks = findViewById(R.id.tvTotalMarks);
        tvObtainMarks = findViewById(R.id.tvObtainMarks);
        BranchCode = getIntent().getStringExtra("BranchCode");
        ExamCode = getIntent().getStringExtra("ExamCode");
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("username", "");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.relativeLayout:
                startActivity(new Intent(getApplicationContext(), ViewResult.class));
                break;


        }

    }

    public void GetExamResult() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://examcoach.in/ExamService.asmx/GetExamResult", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //  Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", " ");
                jsonString = jsonString.replace("<string xmlns=\"http://examcoach.in/\">", " ");
                jsonString = jsonString.replace("</string>", " ");
                Log.d("responsetest", jsonString);
                try {
                    JSONObject object = new JSONObject(jsonString);
                    String status = object.getString("status");
                    if (status.equals("true")) {
                        JSONArray jsonArray = object.getJSONArray("Response");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject arrayJSONObject = jsonArray.getJSONObject(i);
                            tvCorrect1.setText(arrayJSONObject.getString("TotalCorrect"));
                            tvWrong1.setText(arrayJSONObject.getString("TotalIncurrect"));
                            tvResult1.setText(arrayJSONObject.getString("Result"));
                            tvtotalquestions1.setText(arrayJSONObject.getString("TotalQuestions"));
                            /* tvRank1.setText(arrayJSONObject.getString("Rank"));*/
                            tvTotalMarks.setText(arrayJSONObject.getString("TotalMarks"));
                            tvObtainMarks.setText(arrayJSONObject.getString("ObtainMarks"));
                            tvDate.setText("Exam date:- "+arrayJSONObject.getString("ExamDate"));
                        }
                    }
                    else {
                        Toast.makeText(ViewResult.this, "No Result found", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(ViewResult.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("StudentCode", BranchCode);
                params.put("ExamCode", ExamCode);
                return params; }};
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(ViewResult.this);
        requestQueue.add(stringRequest);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), ViewResult.class));

    }


}