package com.akp.examcoach.Basic.LiveClass;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akp.examcoach.Basic.Bottomview.Api_Urls;
import com.akp.examcoach.Basic.Bottomview.PlayVideo;
import com.akp.examcoach.Basic.CustomePlayerYoutube;
import com.akp.examcoach.Basic.YoutubeJune.June_YoutubeWebview;
import com.akp.examcoach.Basic.planpackage.AllPackageListAdapter;
import com.akp.examcoach.Basic.planpackage.AllPlanPackage;
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
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LiveClassDetailsAdapter extends RecyclerView.Adapter<LiveClassDetailsAdapter.VH> {
    Context context;
    List<HashMap<String,String>> arrayList3;


    public LiveClassDetailsAdapter(Context context, List<HashMap<String,String>> arrayList3) {
        this.arrayList3=arrayList3;
        this.context=context;
    }

    @NonNull
    @Override
    public LiveClassDetailsAdapter.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.dynamic_liveclass, viewGroup, false);
        LiveClassDetailsAdapter.VH viewHolder = new LiveClassDetailsAdapter.VH(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull LiveClassDetailsAdapter.VH vh, int i) {
        AnimationHelper.animatate(context,vh.itemView, R.anim.alfa_animation);
        vh.title_tv.setText(arrayList3.get(i).get("Title")+"\n Topic Name:- "+arrayList3.get(i).get("SubjectName"));
        vh.date_tv.setText(arrayList3.get(i).get("Description"));

        if (arrayList3.get(i).get("thumbnailUrl").equalsIgnoreCase("")){
//            Toast.makeText(context,"Image not found!",Toast.LENGTH_LONG).show();
        }
        else {
            Glide.with(context).load(arrayList3.get(i).get("thumbnailUrl")).error(R.drawable.logo).into(vh.thumbnali_img);
        }


        if (arrayList3.get(i).get("IsPaidStatus").equalsIgnoreCase("Paid")){
            vh.paid_img.setVisibility(View.GONE);
        }
        else {
            vh.paid_img.setVisibility(View.VISIBLE);
        }


        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String USERID=arrayList3.get(i).get("userid");
                String BOOKURL=arrayList3.get(i).get("BookURL");
                String VID=arrayList3.get(i).get("EleaId");
                String BATCHID=arrayList3.get(i).get("BatchId");
                String STSTUS=arrayList3.get(i).get("IsPaidStatus");
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
                            JSONArray jsonArrayr = jsonObject.getJSONArray("PackagesList");
                            for (int i = 0; i < jsonArrayr.length(); i++) {
                                JSONObject jsonObject1 = jsonArrayr.getJSONObject(i);
                                if (jsonObject1.getString("PackagePurchaseStatus").equalsIgnoreCase("True")){
                                    Intent intent=new Intent(context, CustomePlayerYoutube.class);
                                    intent.putExtra("video_id",BOOKURL);
                                    intent.putExtra("vid",VID);
                                    context.startActivity(intent);

//                                    Intent intent=new Intent(context, June_YoutubeWebview.class);
//                                    intent.putExtra("video_id",BOOKURL);
//                                    intent.putExtra("vid",VID);
//                                    context.startActivity(intent);

                                }
                                else {
                                    if (STSTUS.equalsIgnoreCase("Paid")){
                                        Intent intent=new Intent(context, AllPlanPackage.class);
                                        intent.putExtra("batch_id",BATCHID);
                                        intent.putExtra("type","liveclass");
                                        context.startActivity(intent);

                                    }
                                    else {
                                        Intent intent=new Intent(context, CustomePlayerYoutube.class);
                                        intent.putExtra("video_id",BOOKURL);
                                        intent.putExtra("vid",VID);
                                        context.startActivity(intent);

//
//                                        Intent intent=new Intent(context, June_YoutubeWebview.class);
//                                        intent.putExtra("video_id",BOOKURL);
//                                        intent.putExtra("vid",VID);
//                                        context.startActivity(intent);
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
                        Log.d("desf", ""+params);
                        return params;
                    }
                };;
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        30000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(stringRequest);


//                Toast.makeText(context,arrayList.get(i).get("BookURL"),Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList3.size();
    }

    public class VH extends RecyclerView.ViewHolder {
        TextView title_tv,date_tv;
        ImageView paid_img,thumbnali_img;

        public VH(@NonNull View itemView) {
            super(itemView);
            title_tv = itemView.findViewById(R.id.title_tv);
            date_tv = itemView.findViewById(R.id.date_tv);
            thumbnali_img = itemView.findViewById(R.id.thumbnali_img);
            paid_img=itemView.findViewById(R.id.paid_img);

        }
    }
}



