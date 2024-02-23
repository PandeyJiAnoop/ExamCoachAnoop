package com.akp.examcoach.Basic.Teacher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TeacherStartClass extends AppCompatActivity {

    ImageView back_img;

    RelativeLayout end_time, start_time, save_rl;

    EditText title_et, urlLink_et, discription_et, sdate_et, stime_et;
    String UserId;

    private Calendar cal;
    private int day;
    private int month;
    private int year;

    static final int DATE_PICKER_ID = 1111;


    TimePickerDialog timePickerDialog;
    Calendar calendar;
    int currentHour;
    int currentMinute;
    String amPm;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_start_class);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        SharedPreferences sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);
        UserId = sharedPreferences.getString("username", "");//        pref = new Preferences(context);

//        Toast.makeText(getApplicationContext(), UserId, Toast.LENGTH_LONG).show();



        back_img = findViewById(R.id.back_img);
        start_time = findViewById(R.id.start_time);
        end_time = findViewById(R.id.end_time);
        save_rl = findViewById(R.id.save_rl);


        title_et = findViewById(R.id.title_et);
        urlLink_et = findViewById(R.id.urlLink_et);
        discription_et = findViewById(R.id.discription_et);
        sdate_et = findViewById(R.id.sdate_et);
        stime_et = findViewById(R.id.stime_et);
//        edate_et = findViewById(R.id.edate_et);
//        etime_et = findViewById(R.id.etime_et);


        sdate_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal = Calendar.getInstance();
                day = cal.get(Calendar.DAY_OF_MONTH);
                month = cal.get(Calendar.MONTH);
                year = cal.get(Calendar.YEAR);
                showDialog(DATE_PICKER_ID);
            }
        });





        stime_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(TeacherStartClass.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        stime_et.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();
            }
        });





        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Class Start", Toast.LENGTH_LONG).show();
                end_time.setVisibility(View.VISIBLE);
                start_time.setVisibility(View.GONE);
                TeacherStartEndLectureAPI("STARTLECTURE", title_et.getText().toString(), urlLink_et.getText().toString(), discription_et.getText().toString(), "0", UserId, sdate_et.getText().toString(), "", stime_et.getText().toString(), "");
            }
        });
        end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TeacherStartEndLectureAPI("ENDLECTURE", title_et.getText().toString(), urlLink_et.getText().toString(), discription_et.getText().toString(), "0", UserId, "", "", "", "");

                Toast.makeText(getApplicationContext(), "End Class!", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        save_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TeacherStartEndLectureAPI("INSERT", title_et.getText().toString(), urlLink_et.getText().toString(), discription_et.getText().toString(), "0", title_et.getText().toString(), "", "", "", "");
            }
        });


    }


    @Override
    public void onBackPressed() {

    }


    private void TeacherStartEndLectureAPI(String action, String title, String link, String des, String lid, String tid, String sdate, String edate, String stime, String etime) {
        final ProgressDialog progressDialog = new ProgressDialog(TeacherStartClass.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL + "InsertTeacherLecture", new Response.Listener<String>() {
            //        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.Signature_BASE_URL + url, new  Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("sdsds", "sd" + response);
//                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", " ");
                jsonString = jsonString.replace("<string xmlns=\"http://examcoach.in/\">", " ");
                jsonString = jsonString.replace("</string>", " ");
                try {
                    JSONObject object = new JSONObject(jsonString);
                    Toast.makeText(TeacherStartClass.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(TeacherStartClass.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(TeacherStartClass.this);
        requestQueue.add(stringRequest);
    }



    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:
                // create a new DatePickerDialog with values you want to show

                DatePickerDialog datePickerDialog = new DatePickerDialog(this, datePickerListener, year, month, day);
                Calendar calendar = Calendar.getInstance();

                calendar.add(Calendar.DATE, 0); // Add 0 days to Calendar
                Date newDate = calendar.getTime();
                datePickerDialog.getDatePicker().setMinDate(newDate.getTime()-(newDate.getTime()%(24*60*60*1000)));
                return datePickerDialog;
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        // the callback received when the user "sets" the Date in the
        // DatePickerDialog
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {

            sdate_et.setText(selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear);
        }
    };

}