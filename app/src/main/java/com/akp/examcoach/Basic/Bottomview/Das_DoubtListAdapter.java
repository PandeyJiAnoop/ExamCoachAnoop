package com.akp.examcoach.Basic.Bottomview;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
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

import com.akp.examcoach.Basic.CustomePlayerYoutube;
import com.akp.examcoach.Basic.adapter.ChatListAdapter;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

public class Das_DoubtListAdapter extends RecyclerView.Adapter<Das_DoubtListAdapter.VH> {
    Context context;
    List<HashMap<String,String>> arrayList;
    private final SharedPreferences sharedPreferences;
    String userid;

    EditText message_edit;
    RecyclerView cust_chat_recyclerView1;
    ArrayList<HashMap<String, String>> arrayList1 = new ArrayList<>();
    private ChatListAdapter chatHistoryAdapter;
    private PopupWindow popupWindow;



    public Das_DoubtListAdapter(Context context, List<HashMap<String,String>> arrayList) {
        this.arrayList=arrayList;
        this.context=context;
        sharedPreferences = context.getSharedPreferences("login_preference", MODE_PRIVATE);
        userid = sharedPreferences.getString("username", "");
    }

    @NonNull
    @Override
    public Das_DoubtListAdapter.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.dynamic_douts, viewGroup, false);
        Das_DoubtListAdapter.VH viewHolder = new Das_DoubtListAdapter.VH(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Das_DoubtListAdapter.VH vh, int i) {
        AnimationHelper.animatate(context,vh.itemView, R.anim.alfa_animation);
        vh.subject_id.setText(arrayList.get(i).get("UserId"));
        vh.username_tv.setText(arrayList.get(i).get("UserName"));
        vh.title_tv.setText(arrayList.get(i).get("DoubtsQuery"));
        vh.des_tv.setText(arrayList.get(i).get("EntryDate"));
        vh.view_tv.setText(arrayList.get(i).get("DoubtsId"));

        vh.message_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(context).inflate(R.layout.dynamic_comment, null);
                popupWindow = new PopupWindow(view,
                        android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT, true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                popupWindow.setFocusable(true);
                // Settings disappear when you click somewhere else
                popupWindow.setOutsideTouchable(true);
                popupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
                popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                popupWindow.showAtLocation(v, Gravity.BOTTOM
                        | Gravity.CENTER_VERTICAL, 0, 0);
                ImageView Goback = (ImageView) view.findViewById(R.id.Goback);
                cust_chat_recyclerView1 =(RecyclerView) view.findViewById(R.id.cust_chat_recyclerView1);
                ImageView send_btn = (ImageView) view.findViewById(R.id.send_btn);
                message_edit = (EditText) view.findViewById(R.id.message_edit);
                final ProgressDialog progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("Loading...");
                progressDialog.show();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL + "GetCommentList", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        String jsonString = response;
                        jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", " ");
                        jsonString = jsonString.replace("<string xmlns=\"http://examcoach.in/\">", " ");
                        jsonString = jsonString.replace("</string>", " ");
                        try {
                            JSONObject jsonObject = new JSONObject(jsonString);
                            JSONArray jsonArrayr = jsonObject.getJSONArray("CommentList");
                            for (int i = 0; i < jsonArrayr.length(); i++) {
                                JSONObject jsonObject1 = jsonArrayr.getJSONObject(i);
                                HashMap<String, String> hm = new HashMap<>();
                                hm.put("ChId", jsonObject1.getString("ChId"));
                                hm.put("TaskId", jsonObject1.getString("TaskId"));
                                hm.put("Comment", jsonObject1.getString("Comment"));
                                hm.put("ondate", jsonObject1.getString("ondate"));
                                hm.put("userId", jsonObject1.getString("userId"));
                                hm.put("UserName", jsonObject1.getString("UserName"));
                                arrayList1.add(hm);
                            }
                            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 1);
                            chatHistoryAdapter = new ChatListAdapter(context, arrayList1);
                            cust_chat_recyclerView1.setLayoutManager(gridLayoutManager);
                            cust_chat_recyclerView1.setAdapter(chatHistoryAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
//                Toast.makeText(MainActivity.this, "msg"+error, Toast.LENGTH_SHORT).show();
                        Log.d("myTag", "message:" + error);
                        Toast.makeText(context, "Internet connection is slow Or no internet connection", Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> params = new HashMap<>();
                        params.put("TaskId", arrayList.get(i).get("DoubtsId"));
                        params.put("Type","DOUBTTASK");
                        return params;
                    }
                };
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(stringRequest);




                send_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final ProgressDialog progressDialog = new ProgressDialog(context);
                        progressDialog.setMessage("Loading...");
                        progressDialog.show();
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL+"InsertComment", new  Response.Listener<String>() {
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
                                    message_edit.setText("");
                                    Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
                                    arrayList1.clear();
//                    cust_chat_recyclerView1.getAdapter().notifyDataSetChanged();
                                    final ProgressDialog progressDialog = new ProgressDialog(context);
                                    progressDialog.setMessage("Loading...");
                                    progressDialog.show();
                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL + "GetCommentList", new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            progressDialog.dismiss();
                                            String jsonString = response;
                                            jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", " ");
                                            jsonString = jsonString.replace("<string xmlns=\"http://examcoach.in/\">", " ");
                                            jsonString = jsonString.replace("</string>", " ");
                                            try {
                                                JSONObject jsonObject = new JSONObject(jsonString);
                                                JSONArray jsonArrayr = jsonObject.getJSONArray("CommentList");
                                                for (int i = 0; i < jsonArrayr.length(); i++) {
                                                    JSONObject jsonObject1 = jsonArrayr.getJSONObject(i);
                                                    HashMap<String, String> hm = new HashMap<>();
                                                    hm.put("ChId", jsonObject1.getString("ChId"));
                                                    hm.put("TaskId", jsonObject1.getString("TaskId"));
                                                    hm.put("Comment", jsonObject1.getString("Comment"));
                                                    hm.put("ondate", jsonObject1.getString("ondate"));
                                                    hm.put("userId", jsonObject1.getString("userId"));
                                                    hm.put("UserName", jsonObject1.getString("UserName"));
                                                    arrayList1.add(hm);
                                                }
                                                GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 1);
                                                chatHistoryAdapter = new ChatListAdapter(context, arrayList1);
                                                cust_chat_recyclerView1.setLayoutManager(gridLayoutManager);
                                                cust_chat_recyclerView1.setAdapter(chatHistoryAdapter);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            progressDialog.dismiss();
//                Toast.makeText(MainActivity.this, "msg"+error, Toast.LENGTH_SHORT).show();
                                            Log.d("myTag", "message:" + error);
                                            Toast.makeText(context, "Internet connection is slow Or no internet connection", Toast.LENGTH_SHORT).show();
                                        }
                                    }) {
                                        @Override
                                        protected Map<String, String> getParams() throws AuthFailureError {
                                            HashMap<String, String> params = new HashMap<>();
                                            params.put("TaskId", arrayList.get(i).get("DoubtsId"));
                                            params.put("Type","DOUBTTASK");
                                            return params;
                                        }
                                    };
                                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                                    requestQueue.add(stringRequest);

                                } catch (JSONException e) {
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
                                params.put("UserId",userid);
                                params.put("TaskId",arrayList.get(i).get("DoubtsId"));
                                params.put("Comment",message_edit.getText().toString());
                                params.put("Type","DOUBTTASK");
                                Log.d("sdsdsd",""+params);
                                return params;
                            }
                        };
                        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        RequestQueue requestQueue = Volley.newRequestQueue(context);
                        requestQueue.add(stringRequest);//                        Toast.makeText(getApplicationContext(),""+message_edit.getText().toString(), Toast.LENGTH_LONG).show();
                    }
                });
                Goback.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        arrayList1.clear();
                        cust_chat_recyclerView1.getAdapter().notifyDataSetChanged();
                        popupWindow.dismiss();
                    }
                });
            }});


        if (arrayList.get(i).get("UserId").equalsIgnoreCase(userid)){
            vh.delet.setVisibility(View.VISIBLE);
            vh.delet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final ProgressDialog progressDialog = new ProgressDialog(context);
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL+"DeleteDoubts", new  Response.Listener<String>() {
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
                            params.put("UserId",arrayList.get(i).get("UserId"));
                            params.put("DoubtsId",arrayList.get(i).get("DoubtsId"));
//                            params.put("VideoId",getVideoId);

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





        if (arrayList.get(i).get("ImageUrl").equalsIgnoreCase("")){
//            Toast.makeText(context,"Image not found!",Toast.LENGTH_LONG).show();
        }
        else {
            Glide.with(context).load(arrayList.get(i).get("ImageUrl")).error(R.drawable.logo).into(vh.dout_img);
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class VH extends RecyclerView.ViewHolder {
        TextView subject_id,username_tv,title_tv,des_tv,view_tv,message_edit;
        ImageView dout_img,delet;
        public VH(@NonNull View itemView) {
            super(itemView);
            subject_id = itemView.findViewById(R.id.subject_id);
            username_tv = itemView.findViewById(R.id.username_tv);
            title_tv = itemView.findViewById(R.id.title_tv);
            des_tv = itemView.findViewById(R.id.des_tv);
            view_tv = itemView.findViewById(R.id.view_tv);
            dout_img = itemView.findViewById(R.id.dout_img);
            delet=itemView.findViewById(R.id.delet);
            message_edit=itemView.findViewById(R.id.message_edit);

        }
    }












}



