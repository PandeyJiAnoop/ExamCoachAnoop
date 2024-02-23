package com.akp.examcoach.Basic.slidingmenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.examcoach.Basic.Bottomview.Api_Urls;
import com.akp.examcoach.Basic.Bottomview.Das_Doubt;
import com.akp.examcoach.Basic.Bottomview.Utility;
import com.akp.examcoach.Basic.DashboardActivity;
import com.akp.examcoach.Basic.NotificationList;
import com.akp.examcoach.Basic.OTPVerify;
import com.akp.examcoach.Basic.adapter.NotificationListAdapter;
import com.akp.examcoach.Basic.adapter.StudentWorkVideoAdapter;
import com.akp.examcoach.R;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentWorkUpload extends AppCompatActivity {

    ImageView back_img;
    TextView select_tv;
    CircleImageView img_showProfile;

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    String temp;

    TextView questions_tv,articles_tv,video_tv;
    LinearLayout question_ll,article_ll,video_ll;
    AppCompatButton video_upload_btn;
    TextInputEditText title_et,query_et,video_url_et,article_et;
    String UserId;
    RecyclerView cust_recyclerView,cust_recyclerView1;

    ArrayList<HashMap<String, String>> arrayList1 = new ArrayList<>();
    AppCompatButton article_btn;



    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_work_upload);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("username", "");

        img_showProfile = findViewById(R.id.img_showProfile);
        select_tv = findViewById(R.id.select_tv);
        back_img=findViewById(R.id.back_img);

        video_upload_btn=findViewById(R.id.video_upload_btn);
        title_et=findViewById(R.id.title_et);
        query_et = findViewById(R.id.query_et);
        video_url_et=findViewById(R.id.video_url_et);
        cust_recyclerView=findViewById(R.id.cust_recyclerView);

        article_btn=findViewById(R.id.article_btn);
        article_et=findViewById(R.id.article_et);
        cust_recyclerView1=findViewById(R.id.cust_recyclerView1);





        questions_tv=findViewById(R.id.questions_tv);
        articles_tv = findViewById(R.id.articles_tv);
        video_tv=findViewById(R.id.video_tv);


        question_ll=findViewById(R.id.question_ll);
        article_ll = findViewById(R.id.article_ll);
        video_ll=findViewById(R.id.video_ll);


        article_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (article_et.getText().toString().equalsIgnoreCase("")){
                    article_et.setError("Fields can't be blank!");
                    article_et.requestFocus();
                }
                else {
//                    UploddArticleAPI();
                }
            }
        });


        video_upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (title_et.getText().toString().equalsIgnoreCase("")){
                    title_et.setError("Fields can't be blank!");
                    title_et.requestFocus();
                }
                else if (title_et.getText().toString().equalsIgnoreCase("")){
                    title_et.setError("Fields can't be blank!");
                    title_et.requestFocus();
                }
                else if (title_et.getText().toString().equalsIgnoreCase("")){
                    title_et.setError("Fields can't be blank!");
                    title_et.requestFocus();
                }
                else {
                    UploddVideoAPI();
                }

            }
        });
        UploddgetHistoryAPI();


        questions_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                question_ll.setVisibility(View.VISIBLE);
                article_ll.setVisibility(View.GONE);
                video_ll.setVisibility(View.GONE);

                questions_tv.setBackgroundResource(R.color.red);
                articles_tv.setBackgroundResource(R.color.grey);
                video_tv.setBackgroundResource(R.color.grey);
            }
        });
        articles_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                question_ll.setVisibility(View.GONE);
                article_ll.setVisibility(View.VISIBLE);
                video_ll.setVisibility(View.GONE);

                articles_tv.setBackgroundResource(R.color.red);
                questions_tv.setBackgroundResource(R.color.grey);
                video_tv.setBackgroundResource(R.color.grey);
            }
        });


        video_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                question_ll.setVisibility(View.GONE);
                article_ll.setVisibility(View.GONE);
                video_ll.setVisibility(View.VISIBLE);
                articles_tv.setBackgroundResource(R.color.grey);
                questions_tv.setBackgroundResource(R.color.grey);
                video_tv.setBackgroundResource(R.color.red);
            }
        });



        select_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });


        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));

            }
        });
        
    }


    private void selectImage() {
        final CharSequence[] items = {"Choose from Gallery",
                "Cancel" };

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(StudentWorkUpload.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(StudentWorkUpload.this);
                if (items[item].equals("Choose from Gallery")) {
                    userChoosenTask ="Choose from Gallery";
                    if(result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        Toast.makeText(getApplicationContext(),""+bm,Toast.LENGTH_LONG).show();
        img_showProfile.setImageBitmap(bm);
        Bitmap immagex=bm;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] b = baos.toByteArray();
        temp = Base64.encodeToString(b,Base64.DEFAULT);
    }






    public void UploddVideoAPI() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL + "WorkWithUs_UpdateVideo", new Response.Listener<String>() {
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

                    if (object.getString("status").equalsIgnoreCase("1")) {
                        String res_message= object.getString("msg");
                        Toast.makeText(StudentWorkUpload.this, res_message, Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(StudentWorkUpload.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(StudentWorkUpload.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserId", UserId);
                params.put("Title", title_et.getText().toString());
                params.put("Query", query_et.getText().toString());
                params.put("VideoUrl", video_url_et.getText().toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(StudentWorkUpload.this);
        requestQueue.add(stringRequest);
    }

    public void UploddgetHistoryAPI() {
        final ProgressDialog progressDialog = new ProgressDialog(StudentWorkUpload.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL+"WorkWithUs_VideoList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>"," ");
                jsonString = jsonString.replace("<string xmlns=\"http://examcoach.in/\">"," ");
                jsonString = jsonString.replace("</string>"," ");
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray jsonArrayr = jsonObject.getJSONArray("UpdateVideoList");
                    for (int i = 0; i < jsonArrayr.length(); i++) {
                        JSONObject jsonObject1 = jsonArrayr.getJSONObject(i);
                        HashMap<String, String> hm = new HashMap<>();
                        hm.put("VideoId", jsonObject1.getString("VideoId"));
                        hm.put("Title", jsonObject1.getString("Title"));
                        hm.put("videoUrl", jsonObject1.getString("videoUrl"));
                        hm.put("Message", jsonObject1.getString("Message"));
                        hm.put("studentId", jsonObject1.getString("studentId"));
                        hm.put("EntryDate", jsonObject1.getString("EntryDate"));
                        hm.put("adminStatus", jsonObject1.getString("adminStatus"));

                        hm.put("Admin_msg", jsonObject1.getString("Admin_msg"));
                        hm.put("approveDate", jsonObject1.getString("approveDate"));
                        arrayList1.add(hm);
                    }
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(StudentWorkUpload .this, 1);
                    StudentWorkVideoAdapter customerListAdapter = new StudentWorkVideoAdapter(StudentWorkUpload.this, arrayList1);
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
                Toast.makeText(StudentWorkUpload.this, "Something went wrong!"+error, Toast.LENGTH_SHORT).show();
            }
        }) {  @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            HashMap<String, String> params = new HashMap<>();
            params.put("UserId",UserId);
            return params; }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(StudentWorkUpload.this);
        requestQueue.add(stringRequest);

    }









    public void UploddArticleAPI() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL + "", new Response.Listener<String>() {
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

                    if (object.getString("status").equalsIgnoreCase("1")) {
                        String res_message= object.getString("msg");
                        Toast.makeText(StudentWorkUpload.this, res_message, Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(StudentWorkUpload.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(StudentWorkUpload.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserId", UserId);
                params.put("Title", title_et.getText().toString());
                params.put("Query", query_et.getText().toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(StudentWorkUpload.this);
        requestQueue.add(stringRequest);
    }

}