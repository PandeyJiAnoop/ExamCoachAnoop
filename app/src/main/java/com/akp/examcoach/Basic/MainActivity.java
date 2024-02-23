package com.akp.examcoach.Basic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
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
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

public class  MainActivity extends AppCompatActivity {
    // Splash screen timer
    ImageView imageView;
    private static int SPLASH_TIME_OUT = 3000;
    String UserId,Usertype;
    //Handler
    private final Handler handler = new Handler();
    //SPLASHTIMEOUT
    String versionName = "", versionCode = "";
    String TAG ="splash";
    int progresscount = 0;
    NumberProgressBar npb_progress;
    String prodid;
    private SharedPreferences refer_preference;
    private SharedPreferences.Editor refer_editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);
        UserId = sharedPreferences.getString("username", "");
        Usertype = sharedPreferences.getString("logintype", "");
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {

                if (!task.isSuccessful()) {
                    return;
                }

                //get the token from task
                String token = task.getResult();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                Log.e(TAG, "Failed to get the token : " + e.getLocalizedMessage());
            }
        });
//        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(MainActivity.this, new OnSuccessListener<InstanceIdResult>() {
//            @Override
//            public void onSuccess(InstanceIdResult instanceIdResult) {
//                String newToken = instanceIdResult.getToken();
//                android.util.Log.e("fcmoken", newToken);
//            }
//        });

        imageView = (ImageView) findViewById(R.id.imageView1);
        npb_progress = findViewById(R.id.npb_progress);
        Animation an2 = AnimationUtils.loadAnimation(this, R.anim.life_to_right);
//        imageView.startAnimation(an2);
        npb_progress.setProgress(progresscount);
        UpdateVersion();


        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
//                            Toast.makeText(getApplicationContext(),deepLink.toString(),Toast.LENGTH_LONG).show();
                            Log.e(TAG, " my referlink "+deepLink.toString());
                            //   "http://www.blueappsoftware.com/myrefer.php?custid=cust123-prod456"
                            String referlink = deepLink.toString();
                            try {
                                referlink = referlink.substring(referlink.lastIndexOf("=")+1);
                                Log.e(TAG, " substring "+referlink); //cust123-prod456
                                String custid = referlink.substring(0, referlink.indexOf("-"));
                                prodid = referlink.substring(referlink.indexOf("-")+1);

                                refer_preference = getSharedPreferences("refer_preference", MODE_PRIVATE);
                                refer_editor = refer_preference.edit();
                                refer_editor.putString("referral_id",prodid);
                                refer_editor.commit();
                                Log.e(TAG, " custid "+custid +"----prpdiid "+ prodid);
//                                Toast.makeText(getApplicationContext(),"ID----"+prodid,Toast.LENGTH_LONG).show();
                                // shareprefernce (prodid, custid);
                                //sharepreference  (refercustid, custid)
                            }catch (Exception e){
                                Log.e(TAG, " error "+e.toString());
                            }
                        }
                        // Handle the deep link. For example, open the linked
                        // content, or apply promotional credit to the user's
                        // account.
                        // ...
                        // ...
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "getDynamicLink:onFailure", e);
                    }
                });


    }


    private void checkLogin() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (progresscount == 100) {
                    /**/
                } else {
                    handler.postDelayed(this, 30);
                    progresscount++;
                    npb_progress.setProgress(progresscount);
                }
            }
        }, 200);

        Timer RunSplash = new Timer();
        // Task to do when the timer ends
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (NetworkConnectionHelper.isOnline(MainActivity.this)) {
                    if (UserId.equalsIgnoreCase("")) {
                        Intent myIntent = new Intent(MainActivity.this,WelcomeSlider.class);//                        intent.putExtra("ref_code",prodid);
                        startActivity(myIntent);
                    } else {
                        if (Usertype.equalsIgnoreCase("TEACHER")){
                            Intent intent=new Intent(getApplicationContext(), TeacherDashboard.class);
                            startActivity(intent);
                        }
                        else {
                            Intent intent=new Intent(getApplicationContext(),DashboardActivity.class);
                            startActivity(intent);
                        }
                    }
                } else {

                    Toast.makeText(MainActivity.this, "Please Check your Internet Connection!!!", Toast.LENGTH_SHORT).show();
                } }
        }, SPLASH_TIME_OUT); }

    public void AlertVersion() {
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_ok);
        TextView tvMessage = dialog.findViewById(R.id.tvMessage);
        Button btnSubmit = dialog.findViewById(R.id.btnSubmit);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        tvMessage.setText(getString(R.string.newVersion));
        btnSubmit.setText(getString(R.string.update));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity(); // or finish();
              /*  if (code.equalsIgnoreCase("")){
                    Intent myIntent = new Intent(SplashScreen.this,WelcomeSlider.class);
                    startActivity(myIntent);
                }
                else {
                    Intent myIntent = new Intent(SplashScreen.this, DashboardScreen.class);
                    startActivity(myIntent);
                }*/
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                dialog.dismiss();
            }
        });
    }

    private void getVersionInfo() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
            versionCode = String.valueOf(packageInfo.versionCode);
            Log.v("vname", versionName + " ," + String.valueOf(versionCode));

            /*
             */
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void UpdateVersion() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Api_Urls.BaseURL+"VersionList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                  Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_LONG).show();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", " ");
                jsonString = jsonString.replace("<string xmlns=\"http://examcoach.in/\">", " ");
                jsonString = jsonString.replace("</string>", " ");
                try {
                    JSONObject object = new JSONObject(jsonString);
                    String UpdateVersion = object.getString("Version");
//                        Toast.makeText(getApplicationContext(),""+UpdateVersion,Toast.LENGTH_LONG).show();
                    String status = object.getString("messege");
                    if (status.equalsIgnoreCase("success"))
                        getVersionInfo();
                    {
                        if (versionName.equalsIgnoreCase(UpdateVersion)) {
                            checkLogin();
                        } else {
                            AlertVersion();
                            //checkLogin();
                        } }
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }
}