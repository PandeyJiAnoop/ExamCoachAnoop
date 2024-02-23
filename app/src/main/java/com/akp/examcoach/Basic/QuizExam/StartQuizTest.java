package com.akp.examcoach.Basic.QuizExam;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.examcoach.Basic.Bottomview.Api_Urls;
import com.akp.examcoach.Basic.DashboardActivity;
import com.akp.examcoach.Basic.LoginActivity;
import com.akp.examcoach.Basic.MainActivity;
import com.akp.examcoach.Basic.OTPVerify;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class StartQuizTest extends AppCompatActivity implements View.OnClickListener{
    private ArrayList<HashMap<String, String>> arraypadflist = new ArrayList<>();
    private ArrayList<HashMap<String, String>> arraypadflistt = new ArrayList<>();
    RecyclerView rcvPdfList;
    RelativeLayout relativeLayout;
    SharedPreferences sharedpreferences;
    String data;
    Button btnSubmit;
    String ID,UserId,getExamType;
    TextView tv1,tv3,tv4,title_tv;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis;
    ImageView back_img;
    private PopupWindow popupWindow;
    RelativeLayout main_rl;
    private AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_quiz_test);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("username", "");
        ID = getIntent().getStringExtra("ExamCode");
        getExamType=getIntent().getStringExtra("ExamType");
        findViewById();
        title_tv.setText(getExamType);
        GetExamType();
        setListner();

    }

    private void setListner() {
        relativeLayout.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }

    private void findViewById() {
        relativeLayout = findViewById(R.id.relativeLayout);
        btnSubmit = findViewById(R.id.btnSubmit);
        rcvPdfList = findViewById(R.id.rcvPdfList);
        tv1 = findViewById(R.id.tv1);
        tv3 = findViewById(R.id.tv3);
        tv4 = findViewById(R.id.tv4);
        title_tv=findViewById(R.id.title_tv);
        back_img=findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Please Don't Pressed back Button without submit your exam Otherwise your result will be failed ",Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.relativeLayout:
                break;

            case R.id.btnSubmit:
                AlertDialog.Builder builder = new AlertDialog.Builder(StartQuizTest.this).setTitle("Alert!! Test Final Submit")
                        .setMessage("Are you sure want to final Submit your Test?").setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                PostSubmitExam();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                /*startActivity(new Intent(getApplicationContext(), ResultDeatils.class));*/
                break;

        }

    }


    private class SelectQuestionAdapTer extends RecyclerView.Adapter<holdercolor> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        public SelectQuestionAdapTer(Context applicationContext, ArrayList<HashMap<String, String>> arraypadflist) {
            data = arraypadflist;
        }
        public holdercolor onCreateViewHolder(ViewGroup parent, int viewType) {
            return new holdercolor(LayoutInflater.from(parent.getContext()).inflate(R.layout.questionlist, parent, false));
        }

        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final holdercolor holdercolor, final int position) {
            holdercolor.tvQuestion.setText(position+1+". "+Html.fromHtml(data.get(position).get("Question")));
            holdercolor.radioone.setText(Html.fromHtml(data.get(position).get("OptionA")));
            holdercolor.radiotwo.setText(Html.fromHtml(data.get(position).get("OptionB")));
            holdercolor.radiothree.setText(Html.fromHtml(data.get(position).get("OptionC")));
            holdercolor.radiofour.setText(Html.fromHtml(data.get(position).get("OptionD")));
            holdercolor.radioone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    if (b) {
                        // Toast.makeText(getApplicationContext(), "vallue", Toast.LENGTH_SHORT).show();
                        HashMap<String, String> paramss = new HashMap<>();
                        paramss.put("answer", "A");
                        paramss.put("answern", "A");
                        paramss.put("QuestionType", data.get(position).get("QuestionType"));
                        paramss.put("QuestionCode", data.get(position).get("QuestionCode"));
                        arraypadflistt.add(paramss);
                    } else {
                        arraypadflistt.remove(position);
                    }

                }
            });
            holdercolor.radiotwo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        // Toast.makeText(getApplicationContext(), "vallue", Toast.LENGTH_SHORT).show();
                        HashMap<String, String> paramss = new HashMap<>();
                        paramss.put("answer", "A");
                        paramss.put("answern", "B");
                        paramss.put("QuestionType", data.get(position).get("QuestionType"));
                        paramss.put("QuestionCode", data.get(position).get("QuestionCode"));
                        arraypadflistt.add(paramss);
                    } else {
                        arraypadflistt.remove(position);
                    }

                }
            });
            holdercolor.radiothree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        // Toast.makeText(getApplicationContext(), "vallue", Toast.LENGTH_SHORT).show();
                        HashMap<String, String> paramss = new HashMap<>();
                        paramss.put("answer", "A");
                        paramss.put("answern", "C");
                        paramss.put("QuestionType", data.get(position).get("QuestionType"));
                        paramss.put("QuestionCode", data.get(position).get("QuestionCode"));
                        arraypadflistt.add(paramss);
                    } else {
                        arraypadflistt.remove(position);
                    }


                }
            });
            holdercolor.radiofour.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        // Toast.makeText(getApplicationContext(), "vallue", Toast.LENGTH_SHORT).show();
                        HashMap<String, String> paramss = new HashMap<>();
                        paramss.put("answer", "A");
                        paramss.put("answern", "D");
                        paramss.put("QuestionType", data.get(position).get("QuestionType"));
                        paramss.put("QuestionCode", data.get(position).get("QuestionCode"));
                        arraypadflistt.add(paramss);
                    } else {
                        arraypadflistt.remove(position);
                    }



                }
            });

        }
        public int getItemCount() {
            return data.size();
        }
    }
    public class holdercolor extends RecyclerView.ViewHolder {
        TextView tvQuestion;
        RadioButton radioone, radiotwo, radiothree, radiofour;
        RelativeLayout relativeLayout;
        public holdercolor(View itemView) {
            super(itemView);
            //TextView
            radioone = itemView.findViewById(R.id.radioone);
            radiotwo = itemView.findViewById(R.id.radiotwo);
            radiothree = itemView.findViewById(R.id.radiothree);
            radiofour = itemView.findViewById(R.id.radiofour);
            tvQuestion = itemView.findViewById(R.id.tvQuestion);
        }
    }


    public void GetExamType() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL+"ExamQuestionsListAll", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                  Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", " ");
                jsonString = jsonString.replace("<string xmlns=\"http://examcoach.in/\">", " ");
                jsonString = jsonString.replace("</string>", " ");
                Log.d("test", jsonString);
                try {
                    JSONObject object = new JSONObject(jsonString);
                    JSONArray Jarray = object.getJSONArray("QuestionDetailList");
                    String status = object.getString("status");
//                    int ExamDurationInMins = Integer.parseInt(object.getString("QuestionDetailList"));
                    if (status.equalsIgnoreCase("1")) {
//                        BranchCode = object.getString("SectionName");
//                        ExamCode = object.getString("ExamCode");
                        for (int i = 0; i < Jarray.length(); i++) {
                            JSONObject arrayJSONObject = Jarray.getJSONObject(i);
                            HashMap<String, String> hashlist = new HashMap();
                            hashlist.put("Question", arrayJSONObject.getString("Question"));
                            hashlist.put("OptionA", arrayJSONObject.getString("OptionA"));
                            hashlist.put("OptionB", arrayJSONObject.getString("OptionB"));
                            hashlist.put("OptionC", arrayJSONObject.getString("OptionC"));
                            hashlist.put("OptionD", arrayJSONObject.getString("OptionD"));
                            hashlist.put("QuestionType", arrayJSONObject.getString("QuestionType"));
                            hashlist.put("QuestionCode", arrayJSONObject.getString("Id"));
                            hashlist.put("RN", arrayJSONObject.getString("RN"));
                            hashlist.put("answer", "");
                            hashlist.put("answern", "");
                            arraypadflist.add(hashlist);

                        }

                        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
                        SelectQuestionAdapTer pdfAdapTer = new SelectQuestionAdapTer(getApplicationContext(), arraypadflist);
                        rcvPdfList.setLayoutManager(layoutManager);
                        rcvPdfList.setAdapter(pdfAdapTer);
                    }
                    else {
                        Toast.makeText(StartQuizTest.this, "No Exam Available", Toast.LENGTH_SHORT).show();
                    }
                    JSONArray Jarray1 = object.getJSONArray("TestDetail");
                        for (int j = 0; j < Jarray1.length(); j++) {
                            JSONObject arrayJSONObject1 = Jarray1.getJSONObject(j);
                            tv1.setText(arrayJSONObject1.getString("ExamCode")+"("+arrayJSONObject1.getString("ExamName")+")");
                            tv3.setText(arrayJSONObject1.getString("NoOfQuestions")+"("+arrayJSONObject1.getString("NegativePer")+")");
//                            tv4.setText(arrayJSONObject1.getString("ExamDurationInMins")+"min.");



                            int ExamDurationInMins = Integer.parseInt(arrayJSONObject1.getString("ExamDurationInMins"));
                          /*   mTimeLeftInMillis = Long.parseLong(arrayJSONObject1.getString("ExamDurationInMins"));
                            mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    mTimeLeftInMillis = millisUntilFinished;
                                    updateCountDownText();
                                }
                                @Override
                                public void onFinish() {
                                    mTimerRunning = false;
                                }
                            }.start();*/





                            new CountDownTimer(ExamDurationInMins * 60 * 1000, 1000) { // adjust the milli seconds here

                                public void onTick(long millisUntilFinished) {
                                    data = "" + String.format("%d:%02d",

                                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -

                                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                                    Log.v("dadada", data);
                                    tv4.setText("" + String.format("%d:%02d",
                                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));


                                }
                                public void onFinish() {
                                    if (SimpleHTTPConnection.isNetworkAvailable(getApplicationContext())) {
                                        PostSubmitExam();

                                    } else {
                                        AppUtils.showToastSort(getApplicationContext(), getString(R.string.errorInternet));
                                    }

                                }
                            }.start();



                        }
                } catch (JSONException e) {
                    e.printStackTrace();
                } }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(StartQuizTest.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserId", UserId);
                params.put("ExamTypeCode", ID);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(StartQuizTest.this);
        requestQueue.add(stringRequest);
    }


  /*  public void PostSubmitExam() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://quiz.signaturesoftware.org/WebService1.asmx/PostSubmitExam", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //  Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", " ");
                jsonString = jsonString.replace("<string xmlns=\"http://tempuri.org/\">", " ");
                jsonString = jsonString.replace("</string>", " ");
                Log.d("test", jsonString);
                try {
                    JSONObject object = new JSONObject(jsonString);


                    String status = object.getString("Status");


                    if (status.equalsIgnoreCase("true")) {
                        Intent intent = new Intent(getApplicationContext(), StartQuizTest.class);
                        intent.putExtra("ExamCode", ExamCode);
                        intent.putExtra("BranchCode", username);
                        startActivity(intent);
                        Toast.makeText(StartQuizTest.this, "Submit Successfully", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(StartQuizTest.this, "Something wrong", Toast.LENGTH_SHORT).show();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(StartQuizTest.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();

                JSONArray Live = new JSONArray();
                for (int Color = 0; Color < arraypadflist.size(); Color++) {
                    JSONObject jsonObjectcolor = new JSONObject();

                    try {
                        jsonObjectcolor.put("BranchCode", BranchCode);
                        jsonObjectcolor.put("StudentCode", username);
                        jsonObjectcolor.put("ExamCode", ExamCode);
                        jsonObjectcolor.put("GivenAnswer", arraypadflist.get(Color).get("answer").replaceAll("\"", "'"));
                        jsonObjectcolor.put("AnswerStatus", arraypadflist.get(Color).get("answern").replaceAll("\"", "'"));
                        jsonObjectcolor.put("FinalSubmitTimer", data);
                        jsonObjectcolor.put("QuestionType", arraypadflist.get(Color).get("QuestionType").replaceAll("\"", "'"));
                        Live.put(jsonObjectcolor);
                        Log.v("dadadada", String.valueOf(Live));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    params.put("AnsArray", String.valueOf(Live));
                }

                return params;

            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(StartQuizTest.this);
        requestQueue.add(stringRequest);

    }*/
//  private void updateCountDownText() {
//      int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
//      int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
//      String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
//
//      tv4.setText(timeLeftFormatted);
//  }







    public void PostSubmitExam() {
        final ProgressDialog progressDialog = new ProgressDialog(StartQuizTest.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.DEPRECATED_GET_OR_POST, "http://examcoach.in/ExamService.asmx/PostSubmitExam", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                  Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", " ");
                jsonString = jsonString.replace("<string xmlns=\"http://examcoach.in/\">", " ");
                jsonString = jsonString.replace("</string>", " ");
                Log.d("ResquestPost", jsonString);

//                {"msg":"Exam Submitted Sucessfully","status":1,"TotalQuestions":"2","ResultMsg":"5Hr. Wait For Result...","TotalAttamp":"2"}


                try {
                    JSONObject object = new JSONObject(jsonString);
                    String status = object.getString("status");
                    if (status.equalsIgnoreCase("1")) {

                        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
                        ViewGroup viewGroup = findViewById(android.R.id.content);
                        //then we will inflate the custom alert dialog xml that we created
                        View dialogView = LayoutInflater.from(StartQuizTest.this).inflate(R.layout.submitresult_popup, viewGroup, false);
                        Button ok = (Button) dialogView.findViewById(R.id.btnDialog);
                        TextView txt_msg=dialogView.findViewById(R.id.txt_msg);
                        TextView tvCorrect1=dialogView.findViewById(R.id.tvCorrect1);
                        TextView tvtvWrong1=dialogView.findViewById(R.id.tvtvWrong1);
                        tvCorrect1.setText(object.getString("TotalQuestions"));
                        tvtvWrong1.setText(object.getString("TotalAttamp"));
                        txt_msg.setText(object.getString("msg")+"\n"+object.getString("ResultMsg"));
                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(getApplicationContext(), DashboardActivity.class);
                                startActivity(intent);
                                alertDialog.dismiss();
                            }
                        });
                        //Now we need an AlertDialog.Builder object
                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(StartQuizTest.this);
                        //setting the view of the builder to our custom view that we already inflated
                        builder.setView(dialogView);
                        //finally creating the alert dialog and displaying it
                        alertDialog = builder.create();
                        alertDialog.show();
                        alertDialog. setCancelable(false);

                       /* Intent intent = new Intent(getApplicationContext(), ViewResult.class);
                        intent.putExtra("ExamCode", ID);
                        intent.putExtra("BranchCode", UserId);
                        startActivity(intent);*/
                        Toast.makeText(StartQuizTest.this, object.getString("msg")+"\n"+object.getString("ResultMsg"), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(StartQuizTest.this, object.getString("msg")+"\n"+object.getString("ResultMsg"), Toast.LENGTH_SHORT).show();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(StartQuizTest.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                JSONArray Live = new JSONArray();
                for (int Color = 0; Color < arraypadflistt.size(); Color++) {
                    JSONObject jsonObjectcolor = new JSONObject();

                    try {
                        jsonObjectcolor.put("BranchCode", getExamType);
                        jsonObjectcolor.put("StudentCode", UserId);
                        jsonObjectcolor.put("ExamCode", ID);
                        jsonObjectcolor.put("TimerCount",tv4.getText().toString());
                        jsonObjectcolor.put("GivenAnswer", arraypadflistt.get(Color).get("answer").replaceAll("\"", "'"));
                        jsonObjectcolor.put("AnswerStatus", arraypadflistt.get(Color).get("answern").replaceAll("\"", "'"));
                        jsonObjectcolor.put("finalSubmitTimer", data);
                        jsonObjectcolor.put("QuestionType", arraypadflistt.get(Color).get("QuestionType").replaceAll("\"", "'"));
                        jsonObjectcolor.put("QuestionId", arraypadflist.get(Color).get("QuestionCode"));
                        Live.put(jsonObjectcolor);
                        Log.v("dadadada", String.valueOf(Live));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    params.put("AnsArray", String.valueOf(Live));
                }
                return params;

            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(StartQuizTest.this);
        requestQueue.add(stringRequest);

    }























}

