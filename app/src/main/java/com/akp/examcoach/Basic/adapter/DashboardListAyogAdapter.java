package com.akp.examcoach.Basic.adapter;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akp.examcoach.Basic.Bottomview.Api_Urls;
import com.akp.examcoach.Basic.Bottomview.Das_LiveClass;
import com.akp.examcoach.Basic.DasELibraryNew.LiveSelectionAdapterELibrary;
import com.akp.examcoach.Basic.DashboardActivity;
import com.akp.examcoach.Basic.LiveClass.Das_Live_AyogList;
import com.akp.examcoach.Basic.LiveClass.LiveSelectionAdapter;
import com.akp.examcoach.Basic.QuizExam.LiveSelectionAdapterExam;
import com.akp.examcoach.Basic.slidingmenu.AnimationHelper;
import com.akp.examcoach.R;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashboardListAyogAdapter extends RecyclerView.Adapter<DashboardListAyogAdapter.VH> {
    Context context;
    List<HashMap<String,String>> arrayList;
    PopupWindow popupWindow;
    ArrayList<HashMap<String, String>> arrayList1 = new ArrayList<>();

    public DashboardListAyogAdapter(Context context, List<HashMap<String,String>> arrayList) {
        this.arrayList=arrayList;
        this.context=context;
    }

    @NonNull
    @Override
    public DashboardListAyogAdapter.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.dynamic_das_ayog, viewGroup, false);
        DashboardListAyogAdapter.VH viewHolder = new DashboardListAyogAdapter.VH(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardListAyogAdapter.VH vh, int i) {
        AnimationHelper.animatate(context,vh.itemView, R.anim.alfa_animation);

        if (arrayList.get(i).get("HomeCatName").equalsIgnoreCase("Exam Portal")){
            vh.provider_name_tv.setText("Test Series");
        }
        else if (arrayList.get(i).get("HomeCatName").equalsIgnoreCase("E-Library")){
            vh.provider_name_tv.setText("Study Material");
        }
        else {
            vh.provider_name_tv.setText(arrayList.get(i).get("HomeCatName"));

        }


        if (arrayList.get(i).get("ImageUrl").equalsIgnoreCase("")){
//            Toast.makeText(context,"Image not found!",Toast.LENGTH_LONG).show();
        }
        else {
            Picasso.with(context).load(arrayList.get(i).get("ImageUrl")).error(R.drawable.logo).into(vh.provider_img);
        }

        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View customView = layoutInflater.inflate(R.layout.dynamic_ayogdas,null);
                ImageView Goback = (ImageView) customView.findViewById(R.id.Goback);
                RecyclerView wallet_histroy1=customView.findViewById(R.id.wallet_histroy1);

                if (arrayList.get(i).get("HomeCatId").equalsIgnoreCase("1")){
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, Api_Urls.BaseURL + "GetExamType", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //Log.d("REsponse_Data", Constants.getSavedPreferences(getApplicationContext(), LOGINKEY, ""));
                            String jsonString = response;
                            jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>"," ");
                            jsonString = jsonString.replace("<string xmlns=\"http://examcoach.in/\">", " ");
                            jsonString = jsonString.replace("</string>"," ");
                            try {
                                JSONObject jsonObject = new JSONObject(jsonString);
                                JSONArray jsonArrayr = jsonObject.getJSONArray("ExamTypeList");
                                for (int i = 0; i < jsonArrayr.length(); i++) {
                                    JSONObject jsonObject1 = jsonArrayr.getJSONObject(i);
                                    HashMap<String, String> hm = new HashMap<>();
                                    hm.put("examtypeId", jsonObject1.getString("examtypeId"));
                                    hm.put("imageUrl", jsonObject1.getString("imageUrl"));
                                    hm.put("examtypeTitle", jsonObject1.getString("examtypeTitle"));
                                    hm.put("HomeCatId", "1");
                                    arrayList1.add(hm);
                                }
                                GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
                                LiveSelectionAdapter customerListAdapter = new LiveSelectionAdapter(context, arrayList1);
                                wallet_histroy1.setLayoutManager(gridLayoutManager);
                                wallet_histroy1.setAdapter(customerListAdapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                            30000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                    requestQueue.add(stringRequest);
                }


                if (arrayList.get(i).get("HomeCatId").equalsIgnoreCase("3")){
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, Api_Urls.BaseURL + "GetExamType", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //Log.d("REsponse_Data", Constants.getSavedPreferences(getApplicationContext(), LOGINKEY, ""));
                            String jsonString = response;
                            jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>"," ");
                            jsonString = jsonString.replace("<string xmlns=\"http://examcoach.in/\">", " ");
                            jsonString = jsonString.replace("</string>"," ");
                            try {
                                JSONObject jsonObject = new JSONObject(jsonString);
                                JSONArray jsonArrayr = jsonObject.getJSONArray("ExamTypeList");
                                for (int i = 0; i < jsonArrayr.length(); i++) {
                                    JSONObject jsonObject1 = jsonArrayr.getJSONObject(i);
                                    HashMap<String, String> hm = new HashMap<>();
                                    hm.put("examtypeId", jsonObject1.getString("examtypeId"));
                                    hm.put("imageUrl", jsonObject1.getString("imageUrl"));
                                    hm.put("examtypeTitle", jsonObject1.getString("examtypeTitle"));
                                    hm.put("HomeCatId", "3");
                                    arrayList1.add(hm);
                                }
                                GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
                                LiveSelectionAdapterELibrary customerListAdapter = new LiveSelectionAdapterELibrary(context, arrayList1);
                                wallet_histroy1.setLayoutManager(gridLayoutManager);
                                wallet_histroy1.setAdapter(customerListAdapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                            30000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                    requestQueue.add(stringRequest);
                }


                if (arrayList.get(i).get("HomeCatId").equalsIgnoreCase("2")){
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, Api_Urls.BaseURL + "GetExamType", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //Log.d("REsponse_Data", Constants.getSavedPreferences(getApplicationContext(), LOGINKEY, ""));
                            String jsonString = response;
                            jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>"," ");
                            jsonString = jsonString.replace("<string xmlns=\"http://examcoach.in/\">", " ");
                            jsonString = jsonString.replace("</string>"," ");
                            try {
                                JSONObject jsonObject = new JSONObject(jsonString);
                                JSONArray jsonArrayr = jsonObject.getJSONArray("ExamTypeList");
                                for (int i = 0; i < jsonArrayr.length(); i++) {
                                    JSONObject jsonObject1 = jsonArrayr.getJSONObject(i);
                                    HashMap<String, String> hm = new HashMap<>();
                                    hm.put("examtypeId", jsonObject1.getString("examtypeId"));
                                    hm.put("imageUrl", jsonObject1.getString("imageUrl"));
                                    hm.put("examtypeTitle", jsonObject1.getString("examtypeTitle"));
                                    hm.put("HomeCatId", "2");
                                    arrayList1.add(hm);
                                }
                                GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
                                LiveSelectionAdapterExam customerListAdapter = new LiveSelectionAdapterExam(context, arrayList1);
                                wallet_histroy1.setLayoutManager(gridLayoutManager);
                                wallet_histroy1.setAdapter(customerListAdapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                            30000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                    requestQueue.add(stringRequest);
                }

/*
                else {
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, Api_Urls.BaseURL + "GetExamType", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //Log.d("REsponse_Data", Constants.getSavedPreferences(getApplicationContext(), LOGINKEY, ""));
                            String jsonString = response;
                            jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>"," ");
                            jsonString = jsonString.replace("<string xmlns=\"http://examcoach.in/\">", " ");
                            jsonString = jsonString.replace("</string>"," ");
                            try {
                                JSONObject jsonObject = new JSONObject(jsonString);
                                JSONArray jsonArrayr = jsonObject.getJSONArray("ExamTypeList");
                                for (int i = 0; i < jsonArrayr.length(); i++) {
                                    JSONObject jsonObject1 = jsonArrayr.getJSONObject(i);
                                    HashMap<String, String> hm = new HashMap<>();
                                    hm.put("examtypeId", jsonObject1.getString("examtypeId"));
                                    hm.put("imageUrl", jsonObject1.getString("imageUrl"));
                                    hm.put("examtypeTitle", jsonObject1.getString("examtypeTitle"));
                                    hm.put("HomeCatId", "1");
                                    arrayList1.add(hm);
                                }
                                GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
                                LiveSelectionAdapter customerListAdapter = new LiveSelectionAdapter(context, arrayList1);
                                wallet_histroy1.setLayoutManager(gridLayoutManager);
                                wallet_histroy1.setAdapter(customerListAdapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                            30000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                    requestQueue.add(stringRequest);
                }
*/


                Goback.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        arrayList1.clear();
                        popupWindow.dismiss();
                    }
                });


                //instantiate popup window
                popupWindow = new PopupWindow(customView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                //display the popup window
                popupWindow.showAtLocation(customView, Gravity.BOTTOM, 0, 0);
                popupWindow.setFocusable(true);
                arrayList1.clear();
                // Settings disappear when you click somewhere else
                popupWindow.setOutsideTouchable(true);
                popupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
                popupWindow.update();


//                Intent intent=new Intent(context, Das_LiveClass.class);
//                intent.putExtra("CourseId",arrayList.get(i).get("HomeCatId"));
//                intent.putExtra("CourseName",arrayList.get(i).get("HomeCatName"));
//                context.startActivity(intent);



//                Intent intent=new Intent(context, Das_Live_AyogList.class);
//                intent.putExtra("examtypeId","100");
//                intent.putExtra("examtypeTitle","Competitive Exam Preparation");
//                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public class VH extends RecyclerView.ViewHolder {
        TextView provider_name_tv;
        CircleImageView provider_img;


        public VH(@NonNull View itemView) {
            super(itemView);
            provider_name_tv = itemView.findViewById(R.id.provider_name_tv);
            provider_img = itemView.findViewById(R.id.provider_img);

        }
    }


}


