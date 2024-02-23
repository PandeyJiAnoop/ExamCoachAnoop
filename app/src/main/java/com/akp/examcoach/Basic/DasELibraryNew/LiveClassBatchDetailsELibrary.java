package com.akp.examcoach.Basic.DasELibraryNew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.examcoach.Basic.Bottomview.Api_Urls;
import com.akp.examcoach.Basic.Bottomview.Das_Doubt;
import com.akp.examcoach.Basic.Bottomview.Das_Testseries;
import com.akp.examcoach.Basic.Bottomview.Dash_Fedd;
import com.akp.examcoach.Basic.DashboardActivity;
import com.akp.examcoach.Basic.LiveClass.LiveClassBatchDetails;
import com.akp.examcoach.Basic.LiveClass.LiveClassDetailsAdapter;
import com.akp.examcoach.Basic.LiveClass.LiveClassDetailsNotesAdapter;
import com.akp.examcoach.Basic.NetworkConnectionHelper;
import com.akp.examcoach.Basic.adapter.DashboardListElibraryAdapter;
import com.akp.examcoach.Basic.slidingmenu.AnimationHelper;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LiveClassBatchDetailsELibrary extends AppCompatActivity {
    ImageView back_img;
    ArrayList<HashMap<String, String>> arrayList4 = new ArrayList<>();
    ArrayList<HashMap<String, String>> arrayList5 = new ArrayList<>();
    RecyclerView wallet_histroy,cust_recyclerView;
    TextView txt_nodata;
    String getSubjectId,getSubjectName,getType,getSubjectcode,getbatchid;
    TextView totle_tv;
    SwipeRefreshLayout srl_refresh;
    String  UserId,getHomeCatId;
   LinearLayout all_ll;
    public static AppCompatButton previousSeletedButton = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_class_batch_details_e_library);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("username", "");
        cust_recyclerView=findViewById(R.id.cust_recyclerView);
        all_ll=findViewById(R.id.all_ll);

        back_img = findViewById(R.id.back_img);
        getSubjectId=getIntent().getStringExtra("BatchId");
        getSubjectName=getIntent().getStringExtra("BatchName");
        getHomeCatId=getIntent().getStringExtra("HomeCatId");
        getSubjectcode=getIntent().getStringExtra("SubjectCode");
        getbatchid=getIntent().getStringExtra("BatchId");

        getType=getIntent().getStringExtra("a_type");
//        Toast.makeText(this, "1"+getSubjectId+"\n"+getbatchid+"\n"+getHomeCatId, Toast.LENGTH_SHORT).show();
        totle_tv=findViewById(R.id.totle_tv);
        totle_tv.setText(getSubjectName);


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.rlBottom);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        displayView(3);


        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        all_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }
        });









//        rl.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(), LiveClassDetails.class));
//
//            }
//        });


//        animBlink = AnimationUtils.loadAnimation(Das_LiveClass.this,
//                R.anim.blink);
//        blink.startAnimation(animBlink);
//        blink1.startAnimation(animBlink);
//        blink2.startAnimation(animBlink);
//        blink3.startAnimation(animBlink);


        wallet_histroy = findViewById(R.id.wallet_histroy);
        txt_nodata = findViewById(R.id.txt_nodata);
        srl_refresh = findViewById(R.id.srl_refresh);



        srl_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkConnectionHelper.isOnline(LiveClassBatchDetailsELibrary.this)) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());
                            overridePendingTransition(0, 0);
                            srl_refresh.setRefreshing(false);
                        }
                    }, 2000);
                } else {
                    Toast.makeText(LiveClassBatchDetailsELibrary.this, "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
                }
            }
        });
        getHistory(getSubjectId);
//        getHistory2("",getSubjectId);
        getDashboardHistoryELibraryList("",getSubjectId);

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
                startActivity(new Intent(getApplicationContext(), Dash_Fedd.class));
                break;
            case 3:
                break;
            case 4:
                startActivity(new Intent(getApplicationContext(), Das_Doubt.class));
                break;
            case 5:
                startActivity(new Intent(getApplicationContext(), Das_Testseries.class));

                break;
            default:
                break;
        }
    }


    public void getHistory(String a_subcode) {
        final ProgressDialog progressDialog = new ProgressDialog(LiveClassBatchDetailsELibrary.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL+"SubjectList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("REsponse_Data", response);
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
                        hm.put("BatchId", jsonObject1.getString("BatchId"));
                        hm.put("BatchName", jsonObject1.getString("BatchName"));
                        hm.put("Streams", jsonObject1.getString("Streams"));
                        hm.put("HomeCatId", "3");
                        arrayList4.add(hm);
                    }
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(LiveClassBatchDetailsELibrary.this, 1, GridLayoutManager.HORIZONTAL, false);
                    LiveClassBatchDetailsELibrary.LiveBatchAdapter customerListAdapter = new LiveClassBatchDetailsELibrary.LiveBatchAdapter(LiveClassBatchDetailsELibrary.this, arrayList4);
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
                Toast.makeText(LiveClassBatchDetailsELibrary.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("BatchId",a_subcode);
                params.put("HomeCatId","3");
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(LiveClassBatchDetailsELibrary.this);
        requestQueue.add(stringRequest);
    }




    public void getDashboardHistoryELibraryList(String sub_id,String batchid) {
        final ProgressDialog progressDialog = new ProgressDialog(LiveClassBatchDetailsELibrary.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL+"ElibraryList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("sadadasdfdf",response);
                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>"," ");
                jsonString = jsonString.replace("<string xmlns=\"http://examcoach.in/\">"," ");
                jsonString = jsonString.replace("</string>"," ");
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray jsonArrayr = jsonObject.getJSONArray("ElibraryList");
                    for (int i = 0; i < jsonArrayr.length(); i++) {
                        JSONObject jsonObject1 = jsonArrayr.getJSONObject(i);
                        HashMap<String, String> hm = new HashMap<>();
//                        hm.put("E_libraryId", jsonObject1.getString("E_libraryId"));
                        hm.put("Title", jsonObject1.getString("Title"));
                        hm.put("BookURL", jsonObject1.getString("BookURL"));
                        hm.put("thumbnailUrl", jsonObject1.getString("thumbnailUrl"));
                        hm.put("IsPaidStatus", jsonObject1.getString("IsPaidStatus"));
                        hm.put("ViewType", jsonObject1.getString("ViewType"));
                        hm.put("Description", jsonObject1.getString("Description"));
                        hm.put("EleaId", jsonObject1.getString("EleaId"));
                        //                        true=file show,false=payment
                        hm.put("PurchaseStatus", jsonObject1.getString("PurchaseStatus"));
                        hm.put("PackageAmount", jsonObject1.getString("PackageAmount"));
                        hm.put("userid",UserId);


                      /*  "Title":"BALYAVASTHA COMPLETE COURSE",
                                "thumbnailUrl":"http://examcoach.in/Libraryfile/thumb_2022_11_14_17_58_211.jpg",
                                "TopicName":"E Lib All Topic",
                                "SubjectName":"E Lib Sub",
                                "CourseName":"E Lib Test",
                                "EleaId":null,
                                "BatchId":"30",
                                "BatchTitle":"E Lib Batch",
                                "BookURL":"http://examcoach.in/Libraryfile/Img_638039808000000000fcde8881fb1748949fcd5ee444b68862.pdf",
                                "Description":"Subject :CDP, Topic : COMPLETE COURSE",
                                "IsPaidStatus":"Paid",
                                "PackageAmount":"E Lib Batch",
                                "ViewType":"View"*/


                        arrayList5.add(hm);
                    }

                    GridLayoutManager HorizontalLayout1 = new GridLayoutManager(LiveClassBatchDetailsELibrary.this, 2);
//                    LiveAyogListAdapterELibrary customerListAdapter = new LiveAyogListAdapterELibrary(Das_Live_AyogListELibrary.this, arrayList);


//                    LinearLayoutManager HorizontalLayout1 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                    DashboardListElibraryAdapter customerListAdapter = new DashboardListElibraryAdapter(LiveClassBatchDetailsELibrary.this, arrayList5);
                    cust_recyclerView.setLayoutManager(HorizontalLayout1);
                    cust_recyclerView.setAdapter(customerListAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                } }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(LiveClassBatchDetailsELibrary.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserId", UserId);
                params.put("SubjectId", sub_id);
                params.put("BatchId", batchid);
                params.put("HomeCatId","3");
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(LiveClassBatchDetailsELibrary.this);
        requestQueue.add(stringRequest);
    }







    public class LiveBatchAdapter extends RecyclerView.Adapter<LiveClassBatchDetailsELibrary.LiveBatchAdapter.VH> {
        Context context;
        List<HashMap<String,String>> arrayList;

        public LiveBatchAdapter(Context context, List<HashMap<String,String>> arrayList) {
            this.arrayList=arrayList;
            this.context=context;
        }

        @NonNull
        @Override
        public LiveClassBatchDetailsELibrary.LiveBatchAdapter.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.dynamic_live_batchlist, viewGroup, false);
            LiveClassBatchDetailsELibrary.LiveBatchAdapter.VH viewHolder = new LiveClassBatchDetailsELibrary.LiveBatchAdapter.VH(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull LiveClassBatchDetailsELibrary.LiveBatchAdapter.VH vh, int i) {
            AnimationHelper.animatate(context,vh.itemView, R.anim.alfa_animation);
            vh.cat_btn.setText(arrayList.get(i).get("SubjectName"));
//        Animation animBlink = AnimationUtils.loadAnimation(context,
//                R.anim.blink);
//        vh.cat_name.startAnimation(animBlink);

//        if (arrayList.get(i).get("imageUrl").equalsIgnoreCase("")){
////            Toast.makeText(context,"Image not found!",Toast.LENGTH_LONG).show();
//        }
//        else {
//            Glide.with(context).load(arrayList.get(i).get("imageUrl")).error(R.drawable.logo).into(vh.cat_img);
//        }
            //Set on click listener for each item view





            vh.cat_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ( ( previousSeletedButton == null ) || ( previousSeletedButton == vh.cat_btn ) ) {
                        vh.cat_btn.setBackgroundColor( ContextCompat.getColor( context, R.color.green) );
                    }
                    else{
                        previousSeletedButton.setBackgroundColor( ContextCompat.getColor( context, R.color.grey) );
                        vh.cat_btn.setBackgroundColor( ContextCompat.getColor( context, R.color.green) );
                    }
                    previousSeletedButton = vh.cat_btn;
                    arrayList5.clear();
                    getDashboardHistoryELibraryList(arrayList.get(i).get("Subjectcode"),arrayList.get(i).get("BatchId"));
                }
            });


        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        public class VH extends RecyclerView.ViewHolder {
            AppCompatButton cat_btn;
            public VH(@NonNull View itemView) {
                super(itemView);
                cat_btn = itemView.findViewById(R.id.cat_btn);
            }
        }
    }
}