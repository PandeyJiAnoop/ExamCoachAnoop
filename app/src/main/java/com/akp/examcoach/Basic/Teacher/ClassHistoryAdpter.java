package com.akp.examcoach.Basic.Teacher;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.akp.examcoach.Basic.Bottomview.Api_Urls;
import com.akp.examcoach.Basic.NotificationList;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassHistoryAdpter extends RecyclerView.Adapter<ClassHistoryAdpter.VH> {
    Context context;
    List<HashMap<String,String>> arrayList;
    RelativeLayout start_time,end_time;

    public ClassHistoryAdpter(Context context, List<HashMap<String,String>> arrayList) {
        this.arrayList=arrayList;
        this.context=context;
    }

    @NonNull
    @Override
    public ClassHistoryAdpter.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.dynamic_classhistory, viewGroup, false);
        ClassHistoryAdpter.VH viewHolder = new ClassHistoryAdpter.VH(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ClassHistoryAdpter.VH vh, int i) {
        AnimationHelper.animatate(context,vh.itemView, R.anim.alfa_animation);
        vh.name_tv.setText(arrayList.get(i).get("LectureTitle")+"\nDescription:- "+arrayList.get(i).get("Description")+"\nLecture Url:- "+arrayList.get(i).get("LectureUrl")+"\nCode-"+arrayList.get(i).get("LectureCode"));
        vh.tv1.setText("  Start Date:- "+arrayList.get(i).get("StartDate"));
        vh.tv.setText("  Start Time:- "+arrayList.get(i).get("StartTime"));

        vh.status.setText(arrayList.get(i).get("LectureStatus"));
        vh.date_tv.setText(arrayList.get(i).get("EntryDate"));

        vh.tvPay.setText(arrayList.get(i).get("EndDate"));
        vh.tvCheckCouple.setText(arrayList.get(i).get("EndTime"));

        if (arrayList.get(i).get("LectureStatus").equalsIgnoreCase("Upcoming")){
            start_time.setVisibility(View.VISIBLE);
            start_time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TeacherStartEndLectureAPI("STARTLECTURE", "", "", "", arrayList.get(i).get("LectureId"), arrayList.get(i).get("TeacherCode"), "12/6/2022 12:00:00 AM", "", "22:00:00", "");
                }
            });
        }

        if (arrayList.get(i).get("LectureStatus").equalsIgnoreCase("Active")){
            start_time.setVisibility(View.GONE);
            end_time.setVisibility(View.VISIBLE);
            end_time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TeacherStartEndLectureAPI("ENDLECTURE", "", "", "", arrayList.get(i).get("LectureId"), arrayList.get(i).get("TeacherCode"), "", "12/6/2022 12:00:00 AM", "", "23:00:00");
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public class VH extends RecyclerView.ViewHolder {
        TextView name_tv, tv,tv1,tvPay,tvCheckCouple,date_tv,status;

        public VH(@NonNull final View itemView) {
            super(itemView);
            name_tv = itemView.findViewById(R.id.name_tv);
            tv = itemView.findViewById(R.id.tv);
            tv1 = itemView.findViewById(R.id.tv1);

            tvPay = itemView.findViewById(R.id.tvPay);
            tvCheckCouple = itemView.findViewById(R.id.tvCheckCouple);
            date_tv = itemView.findViewById(R.id.date_tv);
            status = itemView.findViewById(R.id.status);
            start_time = itemView.findViewById(R.id.start_time);
            end_time = itemView.findViewById(R.id.end_time);

        }
    }




    private void TeacherStartEndLectureAPI(String action, String title, String link, String des, String lid, String tid, String sdate, String edate, String stime, String etime) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL + "InsertTeacherLecture", new Response.Listener<String>() {
            //        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.Signature_BASE_URL + url, new  Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("sdsds", "sd" + response);
//                Toast.makeText(context, response, Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", " ");
                jsonString = jsonString.replace("<string xmlns=\"http://examcoach.in/\">", " ");
                jsonString = jsonString.replace("</string>", " ");
                try {
                    JSONObject object = new JSONObject(jsonString);

                    if (object.getString("status").equalsIgnoreCase("1")){
                        Toast.makeText(context, "Lecture Start Successfully", Toast.LENGTH_SHORT).show();
                        end_time.setVisibility(View.VISIBLE);
                        start_time.setVisibility(View.GONE);

                    }
                    else {
                        Toast.makeText(context, object.getString("msg"), Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(context, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("Action", action);
                params.put("Title", title);
                params.put("UrlLink", link);
                params.put("Discription", des);
                params.put("LectureId", lid);
                params.put("TeacherCode", tid);
                params.put("StartDate", sdate);
                params.put("EndDate", edate);
                params.put("StartTime", stime);
                params.put("EndTime", etime);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}

