package com.akp.examcoach.Basic.planpackage;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akp.examcoach.Basic.Bottomview.Api_Urls;
import com.akp.examcoach.Basic.NotificationList;
import com.akp.examcoach.Basic.adapter.NotificationListAdapter;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MenuAllPackageListAdapter extends RecyclerView.Adapter<MenuAllPackageListAdapter.VH> {
    Context context;
    List<HashMap<String,String>> arrayList;
    private AlertDialog alertDialog;
    EditText coupan_et; TextView des;  TextView tvFinalPrice;
    String final_amount_pay;

    String sno;
    public MenuAllPackageListAdapter(Context context, List<HashMap<String,String>> arrayList) {
        this.arrayList=arrayList;
        this.context=context;
    }
    @NonNull
    @Override
    public MenuAllPackageListAdapter.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.dynamic_menu_package, viewGroup, false);
        MenuAllPackageListAdapter.VH viewHolder = new MenuAllPackageListAdapter.VH(view);
        Random rand = new Random();
        sno = String.format("%04d", rand.nextInt(10000));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MenuAllPackageListAdapter.VH vh, int i) {
        AnimationHelper.animatate(context,vh.itemView, R.anim.alfa_animation);
        vh.name_tv.setText(arrayList.get(i).get("PackageName")+"("+arrayList.get(i).get("PackageFor")+")");
        vh.des_tv.setText(arrayList.get(i).get("packageType")+"(Including total "+arrayList.get(i).get("NoOfTest")+" test series)");
        vh.price_tv.setText(arrayList.get(i).get("Price"));

        vh.val_tv.setText("(Valid Upto- "+arrayList.get(i).get("ValidTill").trim()+")");
        vh.details_tv.setText("Course Name: -"+arrayList.get(i).get("CourseName"));
        if (arrayList.get(i).get("PackagePurchaseStatus").equalsIgnoreCase("True")){
            vh.purchase_tv.setText("Already Purchased");
            vh.purchase_tv.setClickable(false);
            vh.purchase_tv.setBackgroundResource(R.color.green);
            vh.row.setAlpha((float) 0.5);
        }
        else {
            vh.purchase_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //before inflating the custom alert dialog layout, we will get the current activity viewgroup
                    ViewGroup viewGroup =vh.itemView.findViewById(android.R.id.content);
                    //then we will inflate the custom alert dialog xml that we created
                    View dialogView = LayoutInflater.from(context).inflate(R.layout.package_payment, viewGroup, false);
                    RelativeLayout close_rl = (RelativeLayout) dialogView.findViewById(R.id.apply_rl);
                    coupan_et=dialogView.findViewById(R.id.coupan_et);
                    des=dialogView.findViewById(R.id.des);

                    tvFinalPrice=dialogView.findViewById(R.id.tvFinalPrice);

                    tvFinalPrice.setText(arrayList.get(i).get("Price"));
                    AppCompatButton checkifsc_img=dialogView.findViewById(R.id.checkifsc_img);
                    RadioGroup radioGroup = (RadioGroup)dialogView.findViewById(R.id.groupradio);
                    checkifsc_img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CoupanDetailAPI();

                        }
                    });

//                    coupan_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                        @Override
//                        public void onFocusChange(View v, boolean hasFocus) {
//                            if (!hasFocus) {
//                                hideKeyboard(v);
//                            }
//                        }
//                    });


                    // Uncheck or reset the radio buttons initially
                    radioGroup.clearCheck();
                    // Add the Listener to the RadioGroup
                    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        // The flow will come here when
                        // any of the radio buttons in the radioGroup
                        // has been clicked
                        // Check which radio button has been clicked
                        public void onCheckedChanged(RadioGroup group, int checkedId)
                        {
                            // Get the selected Radio Button
                            RadioButton radioButton = (RadioButton)group.findViewById(checkedId);
                        }
                    });

                    close_rl.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // When submit button is clicked,
                            // Ge the Radio Button which is set
                            // If no Radio Button is set, -1 will be returned
                            int selectedId = radioGroup.getCheckedRadioButtonId();
                            if (selectedId == -1) {
                                Toast.makeText(context, "No answer has been selected", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                RadioButton radioButton = (RadioButton)radioGroup.findViewById(selectedId);
                                // Now display the value of selected item
                                // by the Toast message
                                Toast.makeText(context, radioButton.getText(), Toast.LENGTH_SHORT).show();
                                if (radioButton.getText().equals("ONLINE")){
                                    final ProgressDialog progressDialog = new ProgressDialog(context);
                                    progressDialog.setMessage("Loading");
                                    progressDialog.show();
                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL+"InsertPackagesUser", new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            Log.d("InsertPackagesUser", "message:"+response);

//                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                                            progressDialog.dismiss();
                                            String jsonString = response;
                                            jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>"," ");
                                            jsonString = jsonString.replace("<string xmlns=\"http://examcoach.in/\">"," ");
                                            jsonString = jsonString.replace("</string>"," ");
                                            try {
                                                JSONObject jsonObject = new JSONObject(jsonString);
                                                if (jsonObject.getString("status").equalsIgnoreCase("1")){
                                                    if (final_amount_pay == null){
                                                        Intent intent=new Intent(context, RazorpayPayment.class);
                                                        intent.putExtra("amount",vh.price_tv.getText().toString());
                                                        intent.putExtra("pack_id",arrayList.get(i).get("PackageId"));
                                                        intent.putExtra("order_id",jsonObject.getString("OrderId"));
                                                        context.startActivity(intent);
                                                        alertDialog.dismiss();
                                                    }
                                                    else {
                                                        Intent intent=new Intent(context, RazorpayPayment.class);
                                                        intent.putExtra("amount",final_amount_pay);
                                                        intent.putExtra("pack_id",arrayList.get(i).get("PackageId"));
                                                        intent.putExtra("order_id",jsonObject.getString("OrderId"));
                                                        context.startActivity(intent);
                                                        alertDialog.dismiss();
                                                    }
                                                }
                                                Toast.makeText(context,jsonObject.getString("msg"),Toast.LENGTH_LONG).show();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            } }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            progressDialog.dismiss();
                                            Log.d("myTag", "message:"+error);
                                            Toast.makeText(context, "Something went wrong!"+error, Toast.LENGTH_SHORT).show();
                                        }
                                    }) {
                                        @Override
                                        protected Map<String, String> getParams() throws AuthFailureError {
                                            HashMap<String, String> params = new HashMap<>();
                                            params.put("UserId", arrayList.get(i).get("Userid"));
                                            params.put("PackageId", arrayList.get(i).get("PackageId"));
                                            params.put("PaymentMode", "ONLINE");
                                            params.put("coupanName", coupan_et.getText().toString());
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

                                if (radioButton.getText().equals("WALLET")){
                                    final ProgressDialog progressDialog = new ProgressDialog(context);
                                    progressDialog.setMessage("Loading");
                                    progressDialog.show();
                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL+"InsertPackagesUser", new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {                                             Log.d("InsertPackagesUser", "message:"+response);

//                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                                            progressDialog.dismiss();
                                            String jsonString = response;
                                            jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>"," ");
                                            jsonString = jsonString.replace("<string xmlns=\"http://examcoach.in/\">"," ");
                                            jsonString = jsonString.replace("</string>"," ");
                                            try {
                                                JSONObject jsonObject = new JSONObject(jsonString);
                                                if (jsonObject.getString("status").equalsIgnoreCase("1")){
                                                    alertDialog.dismiss();
                                                }
                                                Toast.makeText(context,jsonObject.getString("msg"),Toast.LENGTH_LONG).show();

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            } }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            progressDialog.dismiss();
                                            Log.d("myTag", "message:"+error);
                                            Toast.makeText(context, "Something went wrong!"+error, Toast.LENGTH_SHORT).show();
                                        }
                                    }) {
                                        @Override
                                        protected Map<String, String> getParams() throws AuthFailureError {
                                            HashMap<String, String> params = new HashMap<>();
                                            params.put("UserId", arrayList.get(i).get("Userid"));
                                            params.put("PackageId", arrayList.get(i).get("PackageId"));
                                            params.put("PaymentMode", "WALLET");
                                            params.put("coupanName", coupan_et.getText().toString());
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

                                if (radioButton.getText().equals("CASH")){
                                    final ProgressDialog progressDialog = new ProgressDialog(context);
                                    progressDialog.setMessage("Loading");
                                    progressDialog.show();
                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL+"InsertPackagesUser", new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {                                             Log.d("InsertPackagesUser", "message:"+response);

//                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                                            progressDialog.dismiss();
                                            String jsonString = response;
                                            jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>"," ");
                                            jsonString = jsonString.replace("<string xmlns=\"http://examcoach.in/\">"," ");
                                            jsonString = jsonString.replace("</string>"," ");
                                            try {
                                                JSONObject jsonObject = new JSONObject(jsonString);
                                                if (jsonObject.getString("status").equalsIgnoreCase("1")){
                                                    alertDialog.dismiss();
                                                }
                                                Toast.makeText(context,jsonObject.getString("msg"),Toast.LENGTH_LONG).show();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            } }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            progressDialog.dismiss();
                                            Log.d("myTag", "message:"+error);
                                            Toast.makeText(context, "Something went wrong!"+error, Toast.LENGTH_SHORT).show();
                                        }
                                    }) {
                                        @Override
                                        protected Map<String, String> getParams() throws AuthFailureError {
                                            HashMap<String, String> params = new HashMap<>();
                                            params.put("UserId", arrayList.get(i).get("Userid"));
                                            params.put("PackageId", arrayList.get(i).get("PackageId"));
                                            params.put("PaymentMode", "CASH");
                                            params.put("coupanName", coupan_et.getText().toString());
                                            return params;
                                        }
                                    };;
                                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                                            30000,
                                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                                    requestQueue.add(stringRequest);
                                } } }
                    });
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    //setting the view of the builder to our custom view that we already inflated
                    builder.setView(dialogView);
                    //finally creating the alert dialog and displaying it
                    alertDialog = builder.create();
                    alertDialog.show();

                }
            });
        }



    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public class VH extends RecyclerView.ViewHolder {
        TextView name_tv, des_tv,price_tv,val_tv,details_tv,purchase_tv;
        LinearLayout row;

        public VH(@NonNull View itemView) {
            super(itemView);

            name_tv = itemView.findViewById(R.id.name_tv);
            price_tv = itemView.findViewById(R.id.price_tv);
            val_tv = itemView.findViewById(R.id.val_tv);
            des_tv = itemView.findViewById(R.id.des_tv);
            details_tv = itemView.findViewById(R.id.details_tv);
            purchase_tv = itemView.findViewById(R.id.purchase_tv);
            row=itemView.findViewById(R.id.row);

        }
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        CoupanDetailAPI();
    }


    public void CoupanDetailAPI() {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL+"CoupanDetail", new Response.Listener<String>() {
            //        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.Signature_BASE_URL + url, new  Response.Listener<String>() {
            @Override
            public void onResponse(String response) { Log.d("CoupanDetail", "message:"+response);
                progressDialog.dismiss(); String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>"," ");
                jsonString = jsonString.replace("<string xmlns=\"http://examcoach.in/\">"," ");
                jsonString = jsonString.replace("</string>"," ");
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    if (jsonObject.getString("msg").equalsIgnoreCase("Success")) {
                        JSONArray jsonArrayr = jsonObject.getJSONArray("CoupanDetailList");
                        for (int i = 0; i < jsonArrayr.length(); i++) {
                            JSONObject jsonObject1 = jsonArrayr.getJSONObject(i);

                            String discountamount=jsonObject1.getString("DiscountAmount");
                            String discounttype=jsonObject1.getString("discounttype");

                            if (discounttype.equalsIgnoreCase("Amount")){
                                Double finalamount=(Double.parseDouble(tvFinalPrice.getText().toString())-(Double.parseDouble(discountamount)));
                                final_amount_pay=String.valueOf(finalamount);
                            }
                            else {
                                Double finalamount=( (Double.parseDouble(tvFinalPrice.getText().toString()))-( (Double.parseDouble(tvFinalPrice.getText().toString()))*(Double.parseDouble(discountamount))/100));
                                final_amount_pay=String.valueOf(finalamount);
                                Log.d("finalamount", "message:"+finalamount);
                            }






                            des.setText(jsonObject1.getString("discount")+"\n"+"Valid To:- "+jsonObject1.getString("ValidTo"));
                        }
                    }
                    else {
                        Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("myTag", "message:"+error);
                Toast.makeText(context, "Something went wrong!"+error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("coupanName", coupan_et.getText().toString());
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


}

