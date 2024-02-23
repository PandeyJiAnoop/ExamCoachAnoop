package com.akp.examcoach.Basic.adapter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
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
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akp.examcoach.Basic.Bottomview.Api_Urls;
import com.akp.examcoach.Basic.DashboardActivity;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DashboardListAdapter extends RecyclerView.Adapter<DashboardListAdapter.VH> {
    Context context;
    List<HashMap<String,String>> arrayList;
    EditText message_edit;
    PopupWindow popupWindow;


    RecyclerView cust_chat_recyclerView1;
    ArrayList<HashMap<String, String>> arrayList1 = new ArrayList<>();
    private ChatListAdapter chatHistoryAdapter;

    String getIntemId;




    public DashboardListAdapter(Context context, List<HashMap<String,String>> arrayList) {
        this.arrayList=arrayList;
        this.context=context;
    }

    @NonNull
    @Override
    public DashboardListAdapter.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.dynamic_dashboard, viewGroup, false);
        DashboardListAdapter.VH viewHolder = new DashboardListAdapter.VH(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardListAdapter.VH vh, int i) {
        AnimationHelper.animatate(context,vh.itemView, R.anim.alfa_animation);
        vh.title_tv.setText(arrayList.get(i).get("ArticleTitle"));
        vh.des_tv.setText(arrayList.get(i).get("discription")+"\n"+arrayList.get(i).get("documentUrl")+"\n"+arrayList.get(i).get("EntryDate"));
        vh.view_tv.setText(arrayList.get(i).get("ViewCount"));



        getIntemId=(arrayList.get(i).get("ArticleId"));


        if (arrayList.get(i).get("imageUrl").equalsIgnoreCase("")){
//            Toast.makeText(context,"Image not found!",Toast.LENGTH_LONG).show();
        }
        else {
            Picasso.with(context).load(arrayList.get(i).get("imageUrl")).error(R.drawable.logo).into(vh.article_img);
        }



        vh.message_edit.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
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
                cust_chat_recyclerView1 =view.findViewById(R.id.cust_chat_recyclerView1);
                ImageView send_btn = (ImageView) view.findViewById(R.id.send_btn);
                message_edit = (EditText) view.findViewById(R.id.message_edit);
                getCommentData(arrayList.get(i).get("ArticleId"));
                send_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SaveCommentAPI(arrayList.get(i).get("get_userid"),arrayList.get(i).get("ArticleId"));
//                        Toast.makeText(getApplicationContext(),""+message_edit.getText().toString(), Toast.LENGTH_LONG).show();
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

        vh.share_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ShareProduct().execute();
            }
        });




    }




    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public class VH extends RecyclerView.ViewHolder {
        TextView title_tv, des_tv,view_tv,message_edit;
        ImageView article_img,share_img;
        public VH(@NonNull View itemView) {
            super(itemView);
            title_tv = itemView.findViewById(R.id.title_tv);
            des_tv = itemView.findViewById(R.id.des_tv);
            view_tv = itemView.findViewById(R.id.view_tv);
            message_edit = itemView.findViewById(R.id.message_edit);
            share_img=itemView.findViewById(R.id.share_img);
            article_img=itemView.findViewById(R.id.article_img);

        }
    }


    private class ShareProduct extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                Bitmap bitmap = null;
                URL url = null;
                url = new URL("http://examcoach.in/websiteassetsnew/images/logo.png");
                bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.US);
                Date now = new Date();
                File file = new File(context.getExternalCacheDir(), formatter.format(now) + ".png");
                FileOutputStream fos = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();

                Uri uri = FileProvider.getUriForFile(context, "com.akp.examcoach"+ ".provider", file);
                String message ="Exam Coach - Best Exam Prepration Application\nMADE IN INDIA PRODUCT!";

//                String message =get_videoid +"\n"+des_tv.getText().toString()+"("+title_text.getText().toString()+")";
//                message = message + Constant.share_url + "itemdetail/" + product.getId() + "/" + product.getSlug()+"/" +"https://play.google.com/store/apps/details?id=com.code.aanior";
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, context.getString(R.string.app_name));
                intent.setDataAndType(uri, context.getContentResolver().getType(uri));
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                intent.putExtra(android.content.Intent.EXTRA_TEXT, message);
                context.startActivity(Intent.createChooser(intent, context.getString(R.string.str_share_this_video)));

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
        }

        @Override
        protected void onPreExecute() {
        }

    }


    private void SaveCommentAPI(String userid,String gettaskid) {
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
                    cust_chat_recyclerView1.getAdapter().notifyDataSetChanged();
                    getCommentData(getIntemId);

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
                params.put("TaskId",gettaskid);
                params.put("Comment",message_edit.getText().toString());
                params.put("Type","HOMETASK");
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }


    public void getCommentData(String taskid) {
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
                params.put("TaskId", taskid);
                params.put("Type","HOMETASK");
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

}
