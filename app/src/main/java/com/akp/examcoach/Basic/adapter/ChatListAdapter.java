package com.akp.examcoach.Basic.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.akp.examcoach.Basic.Bottomview.Api_Urls;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;


public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.VH> {
    private final SharedPreferences sharedPreferences;
    String userid;
    Context context;
    List<HashMap<String,String>> arrayList;
    String getcommetid,getUserid,getVideoId;

    public ChatListAdapter(Context context, List<HashMap<String,String>> arrayList) {
        this.arrayList=arrayList;
        this.context=context;
        sharedPreferences = context.getSharedPreferences("login_preference", MODE_PRIVATE);
        userid = sharedPreferences.getString("username", "");

    }
    @NonNull
    @Override
    public ChatListAdapter.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.dynamic_chat, viewGroup, false);
        ChatListAdapter.VH viewHolder = new ChatListAdapter.VH(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ChatListAdapter.VH vh, int i) {
        vh.username.setText(arrayList.get(i).get("UserName"));
//        vh.username.setText(arrayList.get(i).get("UserName")+"("+arrayList.get(i).get("ondate")+")");
        vh.message.setText(arrayList.get(i).get("Comment"));
        vh.date.setText(arrayList.get(i).get("ondate"));

        getcommetid=arrayList.get(i).get("ChId");
        getUserid=arrayList.get(i).get("userId");
        getVideoId=arrayList.get(i).get("TaskId");

        if (getUserid.equalsIgnoreCase(userid)){
            vh.delet.setVisibility(View.VISIBLE);
            vh.delet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final ProgressDialog progressDialog = new ProgressDialog(context);
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL+"DeleteComment", new  Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            String jsonString = response;
                            jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>"," ");
                            jsonString = jsonString.replace("<string xmlns=\"http://examcoach.in/\">"," ");
                            jsonString = jsonString.replace("</string>"," ");
                            try {
                                JSONObject jsonObject = new JSONObject(jsonString);
                                String msg = jsonObject.getString("msg");
                                Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
                                if (jsonObject.getString("status").equalsIgnoreCase("0")){
                                    Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    arrayList.remove(i);
                                    notifyItemRemoved(i);
                                    notifyItemRangeChanged(i, arrayList.size());
                                }

                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
//                Toast.makeText(MainActivity.this, "msg"+error, Toast.LENGTH_SHORT).show();
                            Log.d("myTag", "message:"+error);
                            Toast.makeText(context, "Internet connection is slow Or no internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String, String> params = new HashMap<>();
                            params.put("ChId",arrayList.get(i).get("ChId"));
//                            params.put("VideoId",getVideoId);
                            params.put("UserId",arrayList.get(i).get("userId"));
                            params.put("Type","ELEARNINGTASK");
                            return params;
                        }
                    };
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                    requestQueue.add(stringRequest);

                }
            });
        }




    }
    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public class VH extends RecyclerView.ViewHolder {
        TextView message,username,date;
        CircleImageView user_pic;
        ImageView delet;
        public VH(@NonNull View itemView) {
            super(itemView);
            message=itemView.findViewById(R.id.message);
            user_pic=itemView.findViewById(R.id.user_pic);
            username=itemView.findViewById(R.id.username);
            delet=itemView.findViewById(R.id.delet);
            date=itemView.findViewById(R.id.date);
        }}


}




