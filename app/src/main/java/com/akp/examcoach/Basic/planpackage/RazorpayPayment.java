package com.akp.examcoach.Basic.planpackage;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.examcoach.Basic.Bottomview.Api_Urls;
import com.akp.examcoach.Basic.DashboardActivity;
import com.akp.examcoach.R;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
public class RazorpayPayment extends AppCompatActivity implements PaymentResultListener {
    String UserId;
    TextView status_tv;
    String getAmount,getPackid,getorderid;
    RelativeLayout exit_rl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_razorpay_payment);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);
        UserId = sharedPreferences.getString("username", "");
        exit_rl=findViewById(R.id.exit_rl);
        getAmount=getIntent().getStringExtra("amount");
        getPackid=getIntent().getStringExtra("pack_id");
        getorderid=getIntent().getStringExtra("order_id");

        status_tv=findViewById(R.id.status_tv);

        exit_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),DashboardActivity.class);
                startActivity(intent);
            }
        });

        Checkout.preload(getApplicationContext());
        double payAmount = 0;

        final String merName = UserId;
        final String urPhone = "";
        final String urEmail = "";

//        final String merName = String.valueOf(name.getText());
//        final String urPhone = String.valueOf(phone.getText());
//        final String urEmail = String.valueOf(email.getText());
//        if (String.valueOf(amount.getText()).equals("")){
//            payAmount = 0;
//        }else{
//            payAmount = Integer.parseInt(String.valueOf(amount.getText()));
//        }

        if (getAmount.equals("")){
            payAmount = 0;
        }else{
            payAmount = Double.parseDouble(getAmount);
        }

//        if (merName.isEmpty()){
//            name.setError("Enter Name");
//        }else if (urPhone.isEmpty() || urPhone.length() != 10){
//            phone.setError("Enter 10 digit Number");
//        }else if (urEmail.isEmpty()){
//            email.setError("Enter Email");
//        }else if (payAmount == 0){
//            amount.setError("Amount should be > 0");
//        }
//

//        else{
            payAmount = payAmount * 100;
            final String pay = String.valueOf(payAmount);

            Checkout checkout = new Checkout();
//            checkout.setKeyID("rzp_test_VTsuHqMX7VGC4t");
        checkout.setKeyID("rzp_live_G0zs6MmKoUh4lb");
            /**
             * Instantiate Checkout
             */

            /**
             * Set your logo here
             */
            checkout.setImage(R.drawable.logoicon);

            /**
             * Reference to current activity
             */
            final Activity activity = RazorpayPayment.this;

            /**
             * Pass your payment options to the Razorpay Checkout as a JSONObject
             */
            try {
                JSONObject options = new JSONObject();
                options.put("name", merName);
                options.put("description", "Reference No. #654321");
                options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
//            options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
                options.put("theme.color", "#3399cc");
                options.put("currency", "INR");
                options.put("amount", pay);//pass amount in currency subunits
                options.put("prefill.email", urEmail);
                options.put("prefill.contact",urPhone);
                JSONObject retryObj = new JSONObject();
                retryObj.put("enabled", true);
                retryObj.put("max_count", 4);
                options.put("retry", retryObj);
                checkout.open(RazorpayPayment.this, options);

            } catch(Exception e) {
                Log.e("TAG", "Error in starting Razorpay Checkout", e);
            }
        }
//    }


    @Override
    public void onPaymentSuccess(String s) {
        status_tv.setText("Transaction ID : "+s);
        getHistory(getPackid,getorderid,"SUCCESS",s);
//        result.setText("Transaction ID : "+s);
        Toast.makeText(RazorpayPayment.this, "Payment DONE Successfully!\n"+"Transaction ID : "+s,Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onPaymentError(int i, String s) {
        getHistory(getPackid,getorderid,"FAILED",s);
        status_tv.setText("ERROR : "+s);
        Toast.makeText(RazorpayPayment.this, "ERROR : "+s,Toast.LENGTH_SHORT).show();
    }




    public void getHistory(String packid,String orderid, String pstatus,String refid) {
        final ProgressDialog progressDialog = new ProgressDialog(RazorpayPayment.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL+"OnlinePaymentUpdate", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>"," ");
                jsonString = jsonString.replace("<string xmlns=\"http://examcoach.in/\">"," ");
                jsonString = jsonString.replace("</string>"," ");
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        Toast.makeText(getApplicationContext(), jsonObject.getString("msg"), Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(),jsonObject.getString("msg"),Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("myTag", "message:"+error);
                Toast.makeText(RazorpayPayment.this, "Something went wrong!"+error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserId", UserId);
                params.put("PackageId", packid);
                params.put("OrderId", orderid);
                params.put("PaymentStatus", pstatus);
                params.put("ReferenceNo", refid);
                Log.d("asdfasdfafdf", "message:"+params);
                return params;
            }
        };;
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(RazorpayPayment.this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {

    }
}