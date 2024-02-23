package com.akp.examcoach.Basic.slidingmenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.examcoach.Basic.Bottomview.Api_Urls;
import com.akp.examcoach.Basic.DashboardActivity;
import com.akp.examcoach.Basic.LoginActivity;
import com.akp.examcoach.R;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyProfile extends AppCompatActivity {
    ImageView back_img;

    LinearLayout change_pass_ll;
    EditText name_et,mob_et,email_id_et,dob_et,country_et,state_et,city_et,pincode_et,addet,fname_et;
    TextView save_tv;
    String UserId;
    Spinner gender_sp;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        SharedPreferences sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);
        UserId = sharedPreferences.getString("username", "");

        back_img=findViewById(R.id.back_img);
        change_pass_ll=findViewById(R.id.change_pass_ll);


        name_et=findViewById(R.id.name_et);
        mob_et=findViewById(R.id.mob_et);
        email_id_et=findViewById(R.id.email_id_et);
        dob_et=findViewById(R.id.dob_et);
        gender_sp=findViewById(R.id.gender_sp);
        country_et=findViewById(R.id.country_et);
        state_et=findViewById(R.id.state_et);
        city_et=findViewById(R.id.city_et);
        pincode_et=findViewById(R.id.pincode_et);
        save_tv=findViewById(R.id.save_tv);
        addet=findViewById(R.id.addet);
        fname_et=findViewById(R.id.fname_et);



        ViewProfileAPI();


        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), DashboardActivity.class);
                startActivity(intent);
            }
        });
        change_pass_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),ChangePassword.class);
                startActivity(intent);
            }
        });

        save_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveProfileAPI();
            }
        });
    }

    private void SaveProfileAPI() {
        final ProgressDialog progressDialog = new ProgressDialog(MyProfile.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL + "UpdateStudentProfile", new Response.Listener<String>() {
            //        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.Signature_BASE_URL + url, new  Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("sdsds","sd"+response);
//                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", " ");
                jsonString = jsonString.replace("<string xmlns=\"http://examcoach.in/\">", " ");
                jsonString = jsonString.replace("</string>", " ");
                try {
                    JSONObject object = new JSONObject(jsonString);

//                    if (object.getString("msg").equalsIgnoreCase("Success")) {
                        Toast.makeText(MyProfile.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
//                        Intent intent= new Intent(getApplicationContext(),DashboardActivity.class);
//                        startActivity(intent);
//                    } else {
//                        Toast.makeText(MyProfile.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
////                        showpopupwindow();
//                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(MyProfile.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserId", UserId);
                params.put("StudentName", name_et.getText().toString());
                params.put("EmailId", email_id_et.getText().toString());
                params.put("ContactNo", mob_et.getText().toString());
                params.put("Address", addet.getText().toString());
                params.put("FatherName","AKP");
                params.put("StateName", state_et.getText().toString());
                params.put("CityName", city_et.getText().toString());
                params.put("dob", dob_et.getText().toString());
                params.put("gender", gender_sp.getSelectedItem().toString());
                params.put("pincode", pincode_et.getText().toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(MyProfile.this);
        requestQueue.add(stringRequest);
    }









    private void ViewProfileAPI() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL + "GetStudentProfile", new Response.Listener<String>() {
            //        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.Signature_BASE_URL + url, new  Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", " ");
                jsonString = jsonString.replace("<string xmlns=\"http://examcoach.in/\">", " ");
                jsonString = jsonString.replace("</string>", " ");
                try {
                    JSONObject object = new JSONObject(jsonString);
//                    Toast.makeText(MyProfile.this, object.getString("msg"), Toast.LENGTH_SHORT).show();

                    name_et.setText(object.getString("StudentName"));
                    mob_et.setText(object.getString("MobileNo"));
                    email_id_et.setText(object.getString("EmailAddress"));
                    dob_et.setText(object.getString("dob"));

//                    gender_sp.setPrompt(object.getString("gender"));
//                    gender_et.setText(object.getString("gender"));
                    state_et.setText(object.getString("StateName"));
                    city_et.setText(object.getString("CityName"));
                    pincode_et.setText(object.getString("pincode"));
                    addet.setText(object.getString("Address"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(MyProfile.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserId", UserId);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(MyProfile.this);
        requestQueue.add(stringRequest);
    }

}