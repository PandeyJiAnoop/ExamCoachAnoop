package com.akp.examcoach.Basic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.examcoach.Basic.Bottomview.Api_Urls;
import com.akp.examcoach.Basic.Teacher.TeacherDashboard;
import com.akp.examcoach.R;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    LinearLayout login_rl;
    AppCompatButton login_btn;
    TextView forget_tv;
    private PopupWindow popupWindow;
    private PopupWindow popupWindow1;
    private AlertDialog alertDialog;
    RelativeLayout main_rl;
    TextView view_details_tv;
    CheckBox cbWindows;
    EditText editTextEmail,editTextPassword;
    private SharedPreferences login_preference;
    private SharedPreferences.Editor login_editor;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    return;
                }
                //get the token from task
                token = task.getResult();
            }
         }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                Log.e(TAG, "Failed to get the token : " + e.getLocalizedMessage());
            }
         });

        editTextEmail=findViewById(R.id.editTextEmail);
        editTextPassword=findViewById(R.id.editTextPassword);
        login_btn=findViewById(R.id.login_btn);
        login_rl=findViewById(R.id.login_rl);
        forget_tv=findViewById(R.id.forget_tv);
        main_rl=findViewById(R.id.main_rl);
        view_details_tv=findViewById(R.id.view_details_tv);
        cbWindows=findViewById(R.id.cbWindows);
        forget_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forget_password();
            }
        });
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextEmail.getText().toString().equalsIgnoreCase("")){
                    editTextEmail.setError("Fields can't be blank!");
                    editTextEmail.requestFocus();
                }
                else if (editTextPassword.getText().toString().equalsIgnoreCase("")){
                    editTextPassword.setError("Fields can't be blank!");
                    editTextPassword.requestFocus();
                }
                  else if(!cbWindows.isChecked()){
                    cbWindows.requestFocus();
                    Toast.makeText(getApplicationContext(),"Accept our terms and conditions!",Toast.LENGTH_LONG).show();
                    //do some validation
                }
                  else {
                    LoginAPI(editTextEmail.getText().toString(),editTextPassword.getText().toString()); 
                }
            }
        });

        login_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),RegistartionActivity.class);
                startActivity(intent);
            }
        });

        view_details_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://examcoach.in/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    public void forget_password(){
        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.forget_password,null);
        ImageView Goback = (ImageView) customView.findViewById(R.id.Goback);
        EditText email_et = (EditText) customView.findViewById(R.id.email_et);
        AppCompatButton continue_btn = (AppCompatButton) customView.findViewById(R.id.continue_btn);
        //instantiate popup window
        popupWindow = new PopupWindow(customView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //display the popup window
        popupWindow.showAtLocation(main_rl, Gravity.BOTTOM, 0, 0);
        popupWindow.setFocusable(true);
        // Settings disappear when you click somewhere else
        popupWindow.setOutsideTouchable(true);
        popupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.update();
        Goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FogetPassword(email_et.getText().toString());
            }
        });
    }

    public void LoginAPI(String name,String pass) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL + "GetLogin", new Response.Listener<String>() {
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
                    Log.d("sdsds","sd"+object);
                    if (object.getString("msg").equalsIgnoreCase("Success")) {
                        if (object.getString("LoginType").equalsIgnoreCase("STUDENT")){
                            Toast.makeText(LoginActivity.this, "Login Successfully😀", Toast.LENGTH_SHORT).show();
                            Intent intent= new Intent(getApplicationContext(),DashboardActivity.class);
                            startActivity(intent);
                            login_preference = getSharedPreferences("login_preference", MODE_PRIVATE);
                            login_editor = login_preference.edit();
                            login_editor.putString("username",object.getString("userId"));
                            login_editor.putString("useremail",object.getString("userEmail"));
                            login_editor.putString("logintype",object.getString("LoginType"));
                            login_editor.commit();
                        }
                        else {
                            Toast.makeText(LoginActivity.this, "Teacher Login Successfully😀", Toast.LENGTH_SHORT).show();
                            Intent intent= new Intent(getApplicationContext(),TeacherDashboard.class);
                            startActivity(intent);
                            login_preference = getSharedPreferences("login_preference", MODE_PRIVATE);
                            login_editor = login_preference.edit();
                            login_editor.putString("username",object.getString("userId"));
                            login_editor.putString("useremail",object.getString("userEmail"));
                            login_editor.putString("logintype",object.getString("LoginType"));
                            login_editor.commit();
                        }
                    }
                    if (object.getString("msg").equalsIgnoreCase("You are Login in Other Device")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setTitle("Login Alert Confirmation!")
                                .setMessage("You are already Login in Other Device\nPlease Logout and Retry again....")
                                .setCancelable(false)
                                .setIcon(R.drawable.logo)
                                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        finishAffinity();

                                    }
                                });
                        builder.create().show();

                    }

                    else {
                        Toast.makeText(LoginActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
//                        showpopupwindow();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserName", name);
                params.put("Password", pass);
                params.put("fcmid", token);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        requestQueue.add(stringRequest);
    }
    private void showpopupwindow() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);
        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(LoginActivity.this).inflate(R.layout.successfullyforget_popup, viewGroup, false);
        Button ok = (Button) dialogView.findViewById(R.id.btnDialog);
        TextView txt_msg=dialogView.findViewById(R.id.txt_msg);
        TextView id_tv= dialogView.findViewById(R.id.id_tv);
        TextView pass_tv= dialogView.findViewById(R.id.pass_tv);
//        id_tv.setText("Your Id- "+getname+" ("+getemail+")");
//        pass_tv.setText("New Password- "+getpass);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                alertDialog.dismiss();
            }
        });
        //Now we need an AlertDialog.Builder object
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);
        //finally creating the alert dialog and displaying it
        alertDialog = builder.create();
        alertDialog.show();
    }


    public void FogetPassword(String email) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL + "ForgetPassword", new Response.Listener<String>() {
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
                    if (object.getString("Msg").equalsIgnoreCase("Success")){
                        Toast.makeText(LoginActivity.this, "Password send to your Email-Id Successfully!", Toast.LENGTH_SHORT).show();
                        popupWindow.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace(); }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("EmailID", email);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        requestQueue.add(stringRequest);
    }
}