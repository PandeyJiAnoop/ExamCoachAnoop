package com.akp.examcoach.Basic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.examcoach.Basic.Bottomview.Api_Urls;
import com.akp.examcoach.Basic.Bottomview.Das_Doubt;
import com.akp.examcoach.Basic.Bottomview.Utility;
import com.akp.examcoach.R;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegistartionActivity extends AppCompatActivity {
    LinearLayout login_rl;
    ImageView back_img;
    AppCompatButton submit_btn;

    EditText name_et,mobile_et,email_id_et,pasword_et,confirm_pass_et,referral_code_et,sponser_et;


    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    String temp;
    CircleImageView img_showProfile;
    Spinner gender_sp;
    private String Referral_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registartion);
        SharedPreferences sharedPreferences = getSharedPreferences("refer_preference",MODE_PRIVATE);
        Referral_id= sharedPreferences.getString("referral_id", "");

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        sponser_et=findViewById(R.id.ref_et);
        back_img=findViewById(R.id.back_img);
        submit_btn=findViewById(R.id.submit_btn);
        login_rl=findViewById(R.id.login_ll);

        img_showProfile = findViewById(R.id.img_showProfile);


        name_et=findViewById(R.id.name_et);
        mobile_et=findViewById(R.id.mobile_et);
        email_id_et=findViewById(R.id.email_id_et);

        pasword_et=findViewById(R.id.password_et);
        confirm_pass_et=findViewById(R.id.conf_pasword_et);
        referral_code_et=findViewById(R.id.referral_code_et);
        gender_sp=findViewById(R.id.gender_sp);

        if (Referral_id.equalsIgnoreCase("")){
            sponser_et.setClickable(true);
            sponser_et.setFocusable(true);
        }
        else {
            sponser_et.setClickable(false);
            sponser_et.setFocusable(false);
            sponser_et.setText(Referral_id);
        }


        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name_et.getText().toString().equalsIgnoreCase("")){
                    name_et.setError("Fields can't be blank!");
                    name_et.requestFocus();
                }
                else if (mobile_et.getText().toString().equalsIgnoreCase("")){
                    mobile_et.setError("Fields can't be blank!");
                    mobile_et.requestFocus();
                }
                else if (email_id_et.getText().toString().equalsIgnoreCase("")){
                    email_id_et.setError("Fields can't be blank!");
                    email_id_et.requestFocus();
                }
                else if (pasword_et.getText().toString().equalsIgnoreCase("")){
                    pasword_et.setError("Fields can't be blank!");
                    pasword_et.requestFocus();
                }
                else  if (confirm_pass_et.getText().toString().equalsIgnoreCase("")){
                    confirm_pass_et.setError("Fields can't be blank!");
                    confirm_pass_et.requestFocus();
                }
                               else if(!pasword_et.getText().toString().equals(confirm_pass_et.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Password Not matched!", Toast.LENGTH_LONG).show();
                }
               else {
                    RegisterAPI();
//                    Intent  intent= new Intent(getApplicationContext(),OTPVerify.class);
//                    startActivity(intent);
                }
            }
        });

        login_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }
        });


        referral_code_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });




    }

    private void selectImage() {
        final CharSequence[] items = {"Choose from Library",
                "Cancel" };

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(RegistartionActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(RegistartionActivity.this);
                if (items[item].equals("Choose from Library")) {
                    userChoosenTask ="Choose from Library";
                    if(result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        Toast.makeText(getApplicationContext(),""+bm,Toast.LENGTH_LONG).show();
        img_showProfile.setImageBitmap(bm);
        Bitmap immagex=bm;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] b = baos.toByteArray();
        temp = Base64.encodeToString(b,Base64.DEFAULT);
    }







    public void RegisterAPI() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL + "StudentRegistration", new Response.Listener<String>() {
            //        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.Signature_BASE_URL + url, new  Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", " ");
                jsonString = jsonString.replace("<string xmlns=\"http://examcoach.in/\">", " ");
                jsonString = jsonString.replace("</string>", " ");
                try {
                    JSONObject object = new JSONObject(jsonString);
                    Log.d("sdsds","sd"+object);

                    if (object.getString("msg").equalsIgnoreCase("success")) {
                        Toast.makeText(RegistartionActivity.this, "Registration Successfully😀", Toast.LENGTH_SHORT).show();
                        Intent intent= new Intent(getApplicationContext(),OTPVerify.class);
                        intent.putExtra("otp",object.getString("otp"));
                        intent.putExtra("userId",object.getString("userId"));
                        intent.putExtra("mobile", mobile_et.getText().toString());
                        startActivity(intent);

                    } else {
                        Toast.makeText(RegistartionActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
//                        showpopupwindow();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(RegistartionActivity.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("ReferralID", sponser_et.getText().toString());
                params.put("UserName", name_et.getText().toString());
                params.put("MobileNo", mobile_et.getText().toString());
                params.put("EmailId", email_id_et.getText().toString());
                params.put("FatherName", "aa");
                params.put("Address", "Lucknow");
                params.put("ImagePic", "");
                params.put("Gender", "Male");
                params.put("Password", pasword_et.getText().toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(RegistartionActivity.this);
        requestQueue.add(stringRequest);
    }
}