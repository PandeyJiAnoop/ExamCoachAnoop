package com.akp.examcoach.Basic.Bottomview;

import androidx.annotation.NonNull;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.examcoach.Basic.DashboardActivity;
import com.akp.examcoach.Basic.LiveClass.Das_Live_AyogList;
import com.akp.examcoach.Basic.LiveClass.LiveAyogListAdapter;
import com.akp.examcoach.R;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Das_Doubt extends AppCompatActivity {
    ImageView back_img;
    LinearLayout currentorder_rl,myorder_rl;
    TextView running_tv,myorder_tv;
    TextView select_tv;
    CircleImageView img_showProfile;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    String temp;
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    RecyclerView wallet_histroy;
    String UserId;

    AppCompatButton update_btn;
    EditText feedback_tv;
    Spinner coursesspinner;


    ArrayList<String> StateName = new ArrayList<>();
    ArrayList<String> StateId = new ArrayList<>();
    ArrayList<String> CityName = new ArrayList<>();
    ArrayList<String> CityId = new ArrayList<>();
    String stateid, cityid, uplineid;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_das__doubt);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);
        UserId = sharedPreferences.getString("username", "");
        back_img = findViewById(R.id.back_img);
        img_showProfile = findViewById(R.id.img_showProfile);
        select_tv = findViewById(R.id.select_tv);
        wallet_histroy = findViewById(R.id.wallet_histroy);
        currentorder_rl=findViewById(R.id.currentorder_rl);
        myorder_rl=findViewById(R.id.myorder_rl);
        running_tv=findViewById(R.id.running_tv);
        myorder_tv=findViewById(R.id.myorder_tv);
        coursesspinner=findViewById(R.id.coursesspinner);


        update_btn=findViewById(R.id.update_btn);
        feedback_tv=findViewById(R.id.feedback_tv);


        running_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentorder_rl.setVisibility(View.VISIBLE);
                myorder_rl.setVisibility(View.GONE);
                running_tv.setBackgroundResource(R.color.red);
                myorder_tv.setBackgroundResource(R.color.grey);
            }
        });
        select_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
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

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));

            }
        });
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.rlBottom);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        displayView(4);



        getState();
        coursesspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    for (int j = 0; j <= StateId.size(); j++) {
                        if (coursesspinner.getSelectedItem().toString().equalsIgnoreCase(StateName.get(j))) {
                            // position = i;
                            stateid = StateId.get(j - 1);
                            break;
                        } }
                    CityName.clear();
                }
                if (position == 0) {
                    CityName.clear();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });




        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (feedback_tv.getText().toString().equalsIgnoreCase("")){
                    feedback_tv.setError("Fields can't be blank!");
                    feedback_tv.requestFocus();
                }
                else {
                    SubmitDoutHistory();
                }
            }
        });
        getHistory();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home:
                    displayView(1);
                    // hitFilterApi();
                    return true;
                case R.id.feed:
                    displayView(2);
                    return true;
                case R.id.liveclass:
                    displayView(3);
                    return true;
                case R.id.doubts:
                    displayView(4);
                    return true;
                case R.id.testseries:
                    displayView(5);
                    return true;
            }
            return false;
        }
    };

    private void displayView(int position) {
        switch (position) {
            case 1:
                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                break;
            case 2:
                break;
            case 3:
                startActivity(new Intent(getApplicationContext(), Das_LiveClass.class));
                break;
            case 4:
                break;
            case 5:
                startActivity(new Intent(getApplicationContext(), Das_Testseries.class));
                break;
            default:
                break;
        }
    }



    private void selectImage() {
        final CharSequence[] items = {"Choose from Library",
                "Cancel" };

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Das_Doubt.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(Das_Doubt.this);
                if (items[item].equals("Choose from Library")) {
                    userChoosenTask ="Choose from Library";
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




    public void getHistory() {
        final ProgressDialog progressDialog = new ProgressDialog(Das_Doubt.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL + "GetDoubtsList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.d("REsponse_Data", Constants.getSavedPreferences(getApplicationContext(), LOGINKEY, ""));
                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", " ");
                jsonString = jsonString.replace("<string xmlns=\"http://examcoach.in/\">", " ");
                jsonString = jsonString.replace("</string>", " ");
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray jsonArrayr = jsonObject.getJSONArray("DoubtsList");
                    for (int i = 0; i < jsonArrayr.length(); i++) {
                        JSONObject jsonObject1 = jsonArrayr.getJSONObject(i);
                        HashMap<String, String> hm = new HashMap<>();
                        hm.put("DoubtsId", jsonObject1.getString("DoubtsId"));
                        hm.put("ImageUrl", jsonObject1.getString("ImageUrl"));
                        hm.put("DoubtsQuery", jsonObject1.getString("DoubtsQuery"));
                        hm.put("UserId", jsonObject1.getString("UserId"));
                        hm.put("UserName", jsonObject1.getString("UserName"));
                        hm.put("EntryDate", jsonObject1.getString("EntryDate"));
                        arrayList.add(hm);
                    }
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(Das_Doubt.this, 1);
                    Das_DoubtListAdapter customerListAdapter = new Das_DoubtListAdapter(Das_Doubt.this, arrayList);
                    wallet_histroy.setLayoutManager(gridLayoutManager);
                    wallet_histroy.setAdapter(customerListAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },  new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(Das_Doubt.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserId", UserId);
                params.put("CourseId", "0");
                params.put("SubjectId", "0");

                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(Das_Doubt.this);
        requestQueue.add(stringRequest);
    }





















    public void SubmitDoutHistory() {
        final ProgressDialog progressDialog = new ProgressDialog(Das_Doubt.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL + "UpdateDoubts", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.d("REsponse_Data", Constants.getSavedPreferences(getApplicationContext(), LOGINKEY, ""));
                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", " ");
                jsonString = jsonString.replace("<string xmlns=\"http://examcoach.in/\">", " ");
                jsonString = jsonString.replace("</string>", " ");
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    Toast.makeText(Das_Doubt.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getApplicationContext(),DashboardActivity.class);
                    startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },  new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(Das_Doubt.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserId", UserId);
                params.put("Title", coursesspinner.getSelectedItem().toString());
                params.put("Query", feedback_tv.getText().toString());
                params.put("CourseId", "0");
                params.put("SubjectId", "0");
                params.put("ImagePic", temp);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(Das_Doubt.this);
        requestQueue.add(stringRequest);
    }





    void getState() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Api_Urls.BaseURL+"SubjectListAll", new Response.Listener<String>() {
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
                    coursesspinner.setAdapter(new ArrayAdapter<String>(Das_Doubt.this, android.R.layout.simple_spinner_dropdown_item, StateName));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(Das_Doubt.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }


}