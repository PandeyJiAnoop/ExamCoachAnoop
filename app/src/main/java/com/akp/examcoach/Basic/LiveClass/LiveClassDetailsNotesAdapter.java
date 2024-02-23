package com.akp.examcoach.Basic.LiveClass;


import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.akp.examcoach.Basic.Bottomview.Api_Urls;
import com.akp.examcoach.Basic.Bottomview.DownloadTask;
import com.akp.examcoach.Basic.CustomePlayerYoutube;
import com.akp.examcoach.Basic.ViewPDF;
import com.akp.examcoach.Basic.planpackage.AllPlanPackage;
import com.akp.examcoach.Basic.slidingmenu.AnimationHelper;
import com.akp.examcoach.Basic.slidingmenu.PermissionUtils;
import com.akp.examcoach.R;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LiveClassDetailsNotesAdapter extends RecyclerView.Adapter<LiveClassDetailsNotesAdapter.VH> {
    Context context;
    List<HashMap<String,String>> arrayList;
    PermissionUtils permissionUtils;
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 1;
    DownloadManager downloadManager;
    ArrayList<Long> list = new ArrayList<>();
    public LiveClassDetailsNotesAdapter(Context context, List<HashMap<String,String>> arrayList) {
        this.arrayList=arrayList;
        this.context=context;
    }

    @NonNull
    @Override
    public LiveClassDetailsNotesAdapter.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.dynamic_liveclassnotes, viewGroup, false);
        LiveClassDetailsNotesAdapter.VH viewHolder = new LiveClassDetailsNotesAdapter.VH(view);

        context.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        downloadManager = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
        isStoragePermissionGranted();

        permissionUtils = new PermissionUtils();

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull LiveClassDetailsNotesAdapter.VH vh, int i) {
        AnimationHelper.animatate(context,vh.itemView, R.anim.alfa_animation);
        vh.title_tv.setText(arrayList.get(i).get("Title")+"\n Topic Name:- "+arrayList.get(i).get("TopicName"));
        vh.des_tv.setText(arrayList.get(i).get("Description"));

        if (arrayList.get(i).get("thumbnailUrl").equalsIgnoreCase("")){
//            Toast.makeText(context,"Image not found!",Toast.LENGTH_LONG).show();
        }
        else {
            Glide.with(context).load(arrayList.get(i).get("thumbnailUrl")).error(R.drawable.logo).into(vh.img);
        }


        if (arrayList.get(i).get("IsPaidStatus").equalsIgnoreCase("Paid")){
            vh.paid_img.setVisibility(View.GONE);

        }
        else {
            vh.paid_img.setVisibility(View.VISIBLE);
        }


        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String BOOKURL=arrayList.get(i).get("BookURL");
                String USERID=arrayList.get(i).get("userid");
                String BATCHID=arrayList.get(i).get("BatchId");
                String STSTUS=arrayList.get(i).get("IsPaidStatus");
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL+"GetPackagesList", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

//                Toast.makeText(context,response,Toast.LENGTH_LONG).show();
                        String jsonString = response;
                        jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>"," ");
                        jsonString = jsonString.replace("<string xmlns=\"http://examcoach.in/\">"," ");
                        jsonString = jsonString.replace("</string>"," ");
                        try {
                            JSONObject jsonObject = new JSONObject(jsonString);
                            Toast.makeText(context,jsonObject.getString("msg"),Toast.LENGTH_LONG).show();
                            JSONArray jsonArrayr = jsonObject.getJSONArray("PackagesList");
                            for (int j = 0; j < jsonArrayr.length(); j++) {
                                JSONObject jsonObject1 = jsonArrayr.getJSONObject(j);
                                if (jsonObject1.getString("PackagePurchaseStatus").equalsIgnoreCase("True")){
                                    Intent intent=new Intent(context, ViewPDF.class);
                                    intent.putExtra("pdfurl",BOOKURL);
                                    context.startActivity(intent);
                                }
                                else {
                                    if (STSTUS.equalsIgnoreCase("Paid")){
                                        Toast.makeText(context,"You Don't have purchase any package!",Toast.LENGTH_LONG).show();
                                    }
                                    else {
                                        Intent intent=new Intent(context, ViewPDF.class);
                                        intent.putExtra("pdfurl",BOOKURL);
                                        context.startActivity(intent);
                                    }

                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("myTag", "message:"+error);
                        Toast.makeText(context, "Something went wrong!"+error, Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> params = new HashMap<>();
                        params.put("UserId", USERID);
                        params.put("BatchId", BATCHID);
                        params.put("packageFor", "ELearning");
                        Log.d("responsed123", ""+params);

                        return params;
                    }
                };;
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        30000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(stringRequest);


            }
        });

        vh.download_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String BOOKURL=arrayList.get(i).get("BookURL");
                String USERID=arrayList.get(i).get("userid");
                String BATCHID=arrayList.get(i).get("BatchId");
                String STSTUS=arrayList.get(i).get("IsPaidStatus");
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL+"GetPackagesList", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                Toast.makeText(context,response,Toast.LENGTH_LONG).show();
                        String jsonString = response;
                        jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>"," ");
                        jsonString = jsonString.replace("<string xmlns=\"http://examcoach.in/\">"," ");
                        jsonString = jsonString.replace("</string>"," ");
                        try {
                            JSONObject jsonObject = new JSONObject(jsonString);
                            Toast.makeText(context,jsonObject.getString("msg"),Toast.LENGTH_LONG).show();

                            JSONArray jsonArrayr = jsonObject.getJSONArray("PackagesList");
                            for (int j = 0; j < jsonArrayr.length(); j++) {
                                JSONObject jsonObject1 = jsonArrayr.getJSONObject(j);
                                if (jsonObject1.getString("PackagePurchaseStatus").equalsIgnoreCase("True")){
                                    if (permissionUtils.checkPermission(context, STORAGE_PERMISSION_REQUEST_CODE, v)) {
                                        if (BOOKURL.length() > 0) {
                                            try {
                                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(BOOKURL));
                                                context.startActivity(intent);
//                                                new DownloadTask(context, BOOKURL);
                                            } catch (Exception e) {
                                                e.getStackTrace();
                                            } } }
                                }
                                else {
                                    if (STSTUS.equalsIgnoreCase("Paid")){
                                        Toast.makeText(context,"You Don't have purchase any package!",Toast.LENGTH_LONG).show();
                                    }
                                    else {
                                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(BOOKURL));
                                        context.startActivity(intent);
//                                        new DownloadTask(context, BOOKURL);
                                    }

                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("myTag", "message:"+error);
                        Toast.makeText(context, "Something went wrong!"+error, Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> params = new HashMap<>();
                        params.put("UserId", USERID);
                        params.put("BatchId", BATCHID);
                        params.put("packageFor", "ELearning");
                        Log.d("responsed123", ""+params);

                        return params;
                    }
                };;
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        30000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(stringRequest);





           /*
                if (arrayList.get(i).get("IsPaidStatus").equalsIgnoreCase("Paid")){
                    Toast.makeText(context,"You Don't have purchase any package!",Toast.LENGTH_LONG).show();
//                    Intent intent=new Intent(context, AllPlanPackage.class);
//                    intent.putExtra("batch_id",arrayList.get(i).get("BatchId"));
//                    intent.putExtra("type","liveclass");
//                    context.startActivity(intent);
                }
                else {
                    if (permissionUtils.checkPermission(context, STORAGE_PERMISSION_REQUEST_CODE, v)) {
                        if (arrayList.get(i).get("BookURL").length() > 0) {
                            try {
                                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(arrayList.get(i).get("BookURL"))));
                            } catch (Exception e) {
                                e.getStackTrace();
                            } } } }*/
//                new DownloadTask(context, arrayList.get(i).get("BookURL"));
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class VH extends RecyclerView.ViewHolder {
        TextView title_tv,des_tv;
        ImageView img,paid_img;
        RelativeLayout download_rl;


        public VH(@NonNull View itemView) {
            super(itemView);
            title_tv = itemView.findViewById(R.id.title_tv);
            des_tv = itemView.findViewById(R.id.des_tv);
            img = itemView.findViewById(R.id.img);
            download_rl = itemView.findViewById(R.id.download_rl);
            paid_img=itemView.findViewById(R.id.paid_img);

        }
    }
    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (context.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {

                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }
    BroadcastReceiver onComplete = new BroadcastReceiver() {

        public void onReceive(Context ctxt, Intent intent) {
            long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            Log.e("IN", "" + referenceId);
            list.remove(referenceId);
            if (list.isEmpty()) {
                Log.e("INSIDE", "" + referenceId);
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(context)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle("ExamCoach")
                                .setContentText("All Download completed");
                NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(455, mBuilder.build());


            }

        }
    };


}




