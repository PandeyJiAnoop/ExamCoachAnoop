package com.akp.examcoach.Basic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.window.SplashScreen;

import com.akp.examcoach.Basic.Bottomview.Api_Urls;
import com.akp.examcoach.Basic.Bottomview.Das_Doubt;
import com.akp.examcoach.Basic.Bottomview.Das_Testseries;
import com.akp.examcoach.Basic.Bottomview.Dash_Fedd;
import com.akp.examcoach.Basic.LiveClass.Das_Live_AyogList;
import com.akp.examcoach.Basic.LiveClass.LiveSelectionAdapter;
import com.akp.examcoach.Basic.Teacher.TeacherDashboard;
import com.akp.examcoach.Basic.adapter.DashboardListAcademicAdapter;
import com.akp.examcoach.Basic.adapter.DashboardListAdapter;
import com.akp.examcoach.Basic.adapter.DashboardListAyogAdapter;
import com.akp.examcoach.Basic.adapter.DashboardListCourceAdapter;
import com.akp.examcoach.Basic.adapter.DashboardListEbookPDFAdapter;
import com.akp.examcoach.Basic.adapter.DashboardListElibraryAdapter;
import com.akp.examcoach.Basic.adapter.NotificationListAdapter;
import com.akp.examcoach.Basic.planpackage.AllPlanPackage;
import com.akp.examcoach.Basic.planpackage.MenuPackageList;
import com.akp.examcoach.Basic.slidingmenu.AllOrderListAdapter;
import com.akp.examcoach.Basic.slidingmenu.AnimationHelper;
import com.akp.examcoach.Basic.slidingmenu.Books;
import com.akp.examcoach.Basic.slidingmenu.CustomerOrderDeatils;
import com.akp.examcoach.Basic.slidingmenu.ELibraryMenu;
import com.akp.examcoach.Basic.slidingmenu.FAQ;
import com.akp.examcoach.Basic.slidingmenu.MyOrder;
import com.akp.examcoach.Basic.slidingmenu.MyProfile;
import com.akp.examcoach.Basic.Bottomview.Das_LiveClass;
import com.akp.examcoach.Basic.slidingmenu.MyWallet;
import com.akp.examcoach.Basic.slidingmenu.PrivacyPolicy;
import com.akp.examcoach.Basic.slidingmenu.ReferShare;
import com.akp.examcoach.Basic.slidingmenu.WorkWithUs;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.viewpagerindicator.CirclePageIndicator;

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
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashboardActivity extends AppCompatActivity {
    ImageView sliding_menu;
    DrawerLayout mDrawer;
    TextView home_ll,logout_ll,profile_ll,myorder_ll,book_ll,faq_ll,privacy_policy_ll,refer_ll,call_ll,mywallet_ll;
    ImageView notification_img;
    ArrayList<HashMap<String, String>> arrayList1 = new ArrayList<>();
    ArrayList<HashMap<String, String>> arrayList7 = new ArrayList<>();
    ArrayList<HashMap<String, String>> arrayList2 = new ArrayList<>();
    ArrayList<HashMap<String, String>> arrayList3 = new ArrayList<>();
    ArrayList<HashMap<String, String>> arrayList4 = new ArrayList<>();
    ArrayList<HashMap<String, String>> arrayList5 = new ArrayList<>();
    ArrayList<HashMap<String, String>> arrayList6 = new ArrayList<>();
    private ViewPager pager;

    RelativeLayout main_rl;
    LinearLayout profile;
    private PopupWindow popupWindow1;
    RecyclerView wallet_histroy1;

    CirclePageIndicator indicator;
    ViewPager viewPager;
    Integer[] imageId = {R.drawable.a, R.drawable.b, R.drawable.a,  R.drawable.b};
    String[] imagesName = {"image1","image2","image3","image4"};
    Timer timer;
    List<BannerData> bannerData = new ArrayList<>();
    private static int currentPage = 0;
    TextView work_withus_ll,elibrary_ll;
    PopupWindow popupWindow2;
    String  UserId;
    RecyclerView cust_recyclerView;
    ImageView offer_img;
    TextView link_tv;
//    SwipeRefreshLayout srl_refresh;
    LinearLayout nav_home,nav_live,nav_lib,nav_test,nav_douts;
    TextView txt_username,txt_usergmail,packagelist_ll;
    ImageView fb,telegram,youtube,whatsap,fb1,telegram1,whatsap1,youtube1;
    RecyclerView cource_rec,cource_rec1,exam_rec,library_rec,academic_rec;

    TextView txtMarquee;
    LinearLayout  mypurchase_ll,mycourse_ll;
    String versionName = "", versionCode = "";
    TextView version_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("username", "");
//        srl_refresh=findViewById(R.id.srl_refresh);
        // casting of textview
        txtMarquee = (TextView) findViewById(R.id.marqueeText);
        // Now we will call setSelected() method
        // and pass boolean value as true
        txtMarquee.setSelected(true);
        sliding_menu = (ImageView) findViewById(R.id.sliding_menu);
//        slidingmenu
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        home_ll=findViewById(R.id.home_ll);
        logout_ll=findViewById(R.id.logout_ll);
        profile_ll=findViewById(R.id.profile_ll);
        myorder_ll=findViewById(R.id.myorder_ll);
        faq_ll=findViewById(R.id.faq_ll);
        privacy_policy_ll=findViewById(R.id.privacy_policy_ll);
        refer_ll=findViewById(R.id.refer_ll);
        call_ll=findViewById(R.id.call_ll);
        profile=findViewById(R.id.profile);
        mywallet_ll=findViewById(R.id.mywallet_ll);
        version_tv=findViewById(R.id.version_tv);
        mypurchase_ll=findViewById(R.id.mypurchase_ll);
        mycourse_ll=findViewById(R.id.mycourse_ll);


        txt_username=findViewById(R.id.txt_username);
        txt_usergmail=findViewById(R.id.txt_usergmail);

        fb=findViewById(R.id.fb);
        telegram=findViewById(R.id.telegram);
        youtube=findViewById(R.id.youtube);
        whatsap=findViewById(R.id.whatsap);

        fb1=findViewById(R.id.fb1);
        telegram1=findViewById(R.id.telegram1);
        youtube1=findViewById(R.id.youtube1);
        whatsap1=findViewById(R.id.whatsap1);

        notification_img=findViewById(R.id.notification_img);
        main_rl=findViewById(R.id.main_rl);

        indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        pager = (ViewPager) findViewById(R.id.pager);

        work_withus_ll=findViewById(R.id.work_withus_ll);
        elibrary_ll=findViewById(R.id.elibrary_ll);

        cust_recyclerView=findViewById(R.id.cust_recyclerView);

        cource_rec=findViewById(R.id.cource_rec);
        cource_rec1=findViewById(R.id.cource_rec1);
        exam_rec=findViewById(R.id.exam_rec);
        library_rec=findViewById(R.id.library_rec);
        academic_rec=findViewById(R.id.academic_rec);

        packagelist_ll=findViewById(R.id.packagelist_ll);

        getVersionInfo();


//        //        Floating layout call here
//        FloatingButtonGameDetails f_details = (FloatingButtonGameDetails) findViewById(R.id.f_details);
//        f_details.initFloating();

 //        Floating layout call here
        ImageView f_details = (ImageView) findViewById(R.id.f_details);
        f_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String smsNumber = "919876543210"; //without '+'
                try {
                    Intent sendIntent = new Intent("android.intent.action.MAIN");
                    //sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.setType("text/plain");
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "Welcome to \nExam Coach \n\n How May I help you?");
                    sendIntent.putExtra("ExamCoach", smsNumber + "@s.whatsapp.net"); //phone number without "+" prefix
                    sendIntent.setPackage("com.whatsapp");
                    startActivity(sendIntent);
                } catch(Exception e) {
                    Toast.makeText(getApplicationContext(), "Error/n" + e.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        });

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(getOpenFacebookIntent());
            }
        });
        telegram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.instagram.com/exam_coach1";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.youtube.com/c/ExamCoach1/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        whatsap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://t.me/examcoach1";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });


    /*     indicator = (CirclePageIndicator) findViewById(R.id.indicator);
         viewPager = findViewById(R.id.pager);
        PagerAdapter adapter = new CustomAdapter(DashboardActivity.this,imageId,imagesName);
        viewPager.setAdapter(adapter);
        viewPager.setClipToPadding(false);
        indicator.setViewPager(viewPager);
        indicator.setFillColor(Color.RED);
//        viewPager.setPageMargin(24);
//        viewPager.setPadding(48, 8, 130, 8);
        viewPager.setOffscreenPageLimit(3);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                viewPager.post(new Runnable(){
                    @Override
                    public void run() {
                        viewPager.setCurrentItem((viewPager.getCurrentItem()+1)%imageId.length);
                    }
                });
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 3000, 3000);*/

        getBanner();

        ViewProfileAPI();


     /*   srl_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkConnectionHelper.isOnline(DashboardActivity.this)) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());
                            overridePendingTransition(0, 0);
                            srl_refresh.setRefreshing(false);
                        }
                    }, 2000);
                } else {
                    Toast.makeText(DashboardActivity.this, "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
                }
            }
        });*/
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.rlBottom1);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        displayView(1);


        notification_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),NotificationList.class);
                startActivity(intent);
            }
        });

//        getDashboardHistory();

        getDashboardHistoryAyogList();
//        getDashboardHistoryAyogList1();
        getDashboardHistoryCourceList();
//        getDashboardHistoryELibraryList();
//        getDashboardHistoryAcademicList();

        SharedPreferences sharedPrefs1 = getSharedPreferences("MyPref", 0);
        long time1 = sharedPrefs1.getLong("displayedTime", 0);
        if(time1 == 0 || time1 < System.currentTimeMillis() - 259200000) // 259200000 (Millisecond) = 24 Hours
        {
            OfferBanner();
            // Show welcome screen
            SharedPreferences.Editor prefsEditor1 = sharedPrefs1.edit();
            prefsEditor1.putLong("displayedTime", System.currentTimeMillis()).commit();
            prefsEditor1.apply();
        }


        //     start   bottom view
        nav_home=findViewById(R.id.nav_home);
        nav_live=findViewById(R.id.nav_live);
        nav_lib=findViewById(R.id.nav_lib);
        nav_test=findViewById(R.id.nav_test);
        nav_douts=findViewById(R.id.nav_douts);

        nav_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        nav_live.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectiontype();
            }
        });
        nav_lib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Dash_Fedd.class));
            }
        });
        nav_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), Das_Testseries.class);
                intent.putExtra("type","das");
                startActivity(intent);
            }
        });
        nav_douts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Das_Doubt.class));
            }
        });
        //     end   bottom view
        mypurchase_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), CustomerOrderDeatils.class);
                startActivity(intent);
            }
        });

        mycourse_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), CustomerCourseList.class);
//                intent.putExtra("AayogId",arrayList.get(i).get("AayogId"));
//                intent.putExtra("AayogTitle",arrayList.get(i).get("AayogTitle"));
//                intent.putExtra("HomeCatId","1");
                startActivity(intent);
            }
        });
        mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        sliding_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
//                mDrawer.openDrawer(Gravity.START);
                mDrawer.openDrawer(Gravity.START);
                home_ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDrawer.closeDrawer(Gravity.START);
                    }
                });
                profile_ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(getApplicationContext(), MyProfile.class);
                        startActivity(intent);
                        mDrawer.closeDrawer(Gravity.START);
                    }
                });



                myorder_ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(getApplicationContext(), CustomerOrderDeatils.class);
                        startActivity(intent);
                        mDrawer.closeDrawer(Gravity.START);
                    }
                });



                privacy_policy_ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(getApplicationContext(), PrivacyPolicy.class);
                        startActivity(intent);
                        mDrawer.closeDrawer(Gravity.START);
                    }
                });

                faq_ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(getApplicationContext(), FAQ.class);
                        startActivity(intent);
                        mDrawer.closeDrawer(Gravity.START);
                    }
                });


                refer_ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(getApplicationContext(), ReferShare.class);
                        startActivity(intent);
                        mDrawer.closeDrawer(Gravity.START);
                    }
                });





                packagelist_ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(getApplicationContext(), MenuPackageList.class);
//                        intent.putExtra("type","menu");
                        startActivity(intent);
                        mDrawer.closeDrawer(Gravity.START);
                    }
                });



                call_ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + "+91 9876543210"));
                        startActivity(intent);
                        mDrawer.closeDrawer(Gravity.START);
                    }
                });

                profile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(getApplicationContext(), MyProfile.class);
                        startActivity(intent);
                        mDrawer.closeDrawer(Gravity.START);
                    }
                });
                mywallet_ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(getApplicationContext(), MyWallet.class);
                        startActivity(intent);
                        mDrawer.closeDrawer(Gravity.START);
                    }
                });
                elibrary_ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(getApplicationContext(), ELibraryMenu.class);
                        intent.putExtra("a_type","das");
                        startActivity(intent);
                        mDrawer.closeDrawer(Gravity.START);
                    }
                });
                work_withus_ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(getApplicationContext(), WorkWithUs.class);
                        startActivity(intent);
                        mDrawer.closeDrawer(Gravity.START);
                    }
                });
                fb1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(getOpenFacebookIntent());
                        mDrawer.closeDrawer(Gravity.START);
                    }
                });
                telegram1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = "https://www.instagram.com/exam_coach1";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                        mDrawer.closeDrawer(Gravity.START);
                    }
                });
                youtube1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = "https://www.youtube.com/c/ExamCoach1/";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                        mDrawer.closeDrawer(Gravity.START);
                    }
                });
                whatsap1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = "https://t.me/examcoach1";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                        mDrawer.closeDrawer(Gravity.START);
                    }
                });

                logout_ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this).setTitle("Logout")
                                .setMessage("Are you sure want to Logout").setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                      LogoutAPI();
                                    }
                                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();

                    }
                });
            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home:
                    displayView(1);
                    // hitFilterApi();
                    return true;
                case R.id.feed:
                    displayView(2);
                    return true;
                case R.id.liveclass:
                    displayView(3);
                    return true;
                case R.id.doubts:
                    displayView(4);
                    return true;
                case R.id.testseries:
                    displayView(5);
                    return true;
            }
            return false;
        }
    };
    private void displayView(int position) {
        switch (position) {
            case 1:
                break;
            case 2:
                startActivity(new Intent(getApplicationContext(), Dash_Fedd.class));
                break;
            case 3:
                selectiontype();
                break;
            case 4:
                startActivity(new Intent(getApplicationContext(), Das_Doubt.class));
                break;
            case 5:
                startActivity(new Intent(getApplicationContext(), Das_Testseries.class));
                break;
            default:
                break;
        }
    }


    public void getBanner() {
        final ProgressDialog progressDialog = new ProgressDialog(DashboardActivity.this);
        progressDialog.show();
        progressDialog.setMessage("Loading");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Api_Urls.BaseURL+"GetBannerImage", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>"," ");
                jsonString = jsonString.replace("<string xmlns=\"http://examcoach.in/\">"," ");
                jsonString = jsonString.replace("</string>"," ");
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
//                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                    String JsonInString = jsonObject.getString("BannerList");
                    bannerData = BannerData.createJsonInList(JsonInString);
                    pager.setAdapter(new AdapterForBanner(DashboardActivity.this, bannerData));
                    indicator.setViewPager(pager);
                    indicator.setFillColor(Color.RED);

                    final float density = getResources().getDisplayMetrics().density;
//Set circle indicator radius
                    indicator.setRadius(5 * density);
                    // Auto start of viewpager
                    final Handler handler = new Handler();
                    final Runnable Update = new Runnable() {
                        public void run() {
                            if (currentPage == bannerData.size()) {
                                currentPage = 0;
                            }
                            pager.setCurrentItem(currentPage++, true);
                        }
                    };
                    Timer swipeTimer = new Timer();
                    swipeTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            handler.post(Update);
                        }
                    }, 2000, 2000);
                    // Pager listener over indicator
                    indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageSelected(int position) {
                            currentPage = position;
                        }
                        @Override
                        public void onPageScrolled(int pos, float arg1, int arg2) { }
                        @Override
                        public void onPageScrollStateChanged(int pos) { }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //  Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(DashboardActivity.this, "Please check your Internet Connection! try again...", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(DashboardActivity.this);
        requestQueue.add(stringRequest);
    }

    public void selectiontype(){
        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.dynamic_das_type,null);
        wallet_histroy1 = customView.findViewById(R.id.wallet_histroy1);
        ImageView Goback = (ImageView) customView.findViewById(R.id.Goback);
        final ProgressDialog progressDialog = new ProgressDialog(DashboardActivity.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Api_Urls.BaseURL + "GetExamType", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.d("REsponse_Data", Constants.getSavedPreferences(getApplicationContext(), LOGINKEY, ""));
                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>"," ");
                jsonString = jsonString.replace("<string xmlns=\"http://examcoach.in/\">", " ");
                jsonString = jsonString.replace("</string>"," ");
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray jsonArrayr = jsonObject.getJSONArray("ExamTypeList");
                    for (int i = 0; i < jsonArrayr.length(); i++) {
                        JSONObject jsonObject1 = jsonArrayr.getJSONObject(i);
                        HashMap<String, String> hm = new HashMap<>();
                        hm.put("examtypeId", jsonObject1.getString("examtypeId"));
                        hm.put("imageUrl", jsonObject1.getString("imageUrl"));
                        hm.put("examtypeTitle", jsonObject1.getString("examtypeTitle"));
                        hm.put("HomeCatId", "1");
                        arrayList1.add(hm);
                    }
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(DashboardActivity.this, 2);
                    LiveSelectionAdapter customerListAdapter = new LiveSelectionAdapter(DashboardActivity.this, arrayList1);
                    wallet_histroy1.setLayoutManager(gridLayoutManager);
                    wallet_histroy1.setAdapter(customerListAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(DashboardActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(DashboardActivity.this);
        requestQueue.add(stringRequest);


        Goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrayList1.clear();
                popupWindow1.dismiss();
            }
        });


        //instantiate popup window
        popupWindow1 = new PopupWindow(customView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //display the popup window
        popupWindow1.showAtLocation(main_rl, Gravity.BOTTOM, 0, 0);
        popupWindow1.setFocusable(false);
        // Settings disappear when you click somewhere else
        popupWindow1.setOutsideTouchable(false);

        popupWindow1.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);

        popupWindow1.update();
    }




    public void OfferBanner(){
        LayoutInflater layoutInflater1 = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView3 = layoutInflater1.inflate(R.layout.dashboard_offer_popup,null);
        ImageButton Goback1 = (ImageButton) customView3.findViewById(R.id.ib_close);
        link_tv=(TextView) customView3.findViewById(R.id.link_tv);
        offer_img=customView3.findViewById(R.id.offer_img);
        // Now we will call setSelected() method
        // and pass boolean value as true
        link_tv.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        link_tv.setText("Exam Coach Better Study..http://examcoach.in/");
        link_tv.setSelected(true);
        link_tv.setSingleLine(true);
        GetSingleOfferBanner();
        //instantiate popup window
        popupWindow2 = new PopupWindow(customView3, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //display the popup window
        new Handler().postDelayed(new Runnable(){
            public void run() {
                popupWindow2.showAtLocation(main_rl, Gravity.CENTER, 0, 0);
            }
        }, 100L);
        popupWindow2.setFocusable(true);
        // Settings disappear when you click somewhere else
        popupWindow2.setOutsideTouchable(true);
        popupWindow2.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow2.update();
        popupWindow2.setBackgroundDrawable(new ColorDrawable(R.color.transparent));
        Goback1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow2.dismiss();
            }
        });
    }

    public void getDashboardHistory() {
        final ProgressDialog progressDialog = new ProgressDialog(DashboardActivity.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Api_Urls.BaseURL+"GetDashboardData", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>"," ");
                jsonString = jsonString.replace("<string xmlns=\"http://examcoach.in/\">"," ");
                jsonString = jsonString.replace("</string>"," ");
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray jsonArrayr = jsonObject.getJSONArray("DashboardList");
                    for (int i = 0; i < jsonArrayr.length(); i++) {
                        JSONObject jsonObject1 = jsonArrayr.getJSONObject(i);
                        HashMap<String, String> hm = new HashMap<>();
                        hm.put("ArticleId", jsonObject1.getString("ArticleId"));
                        hm.put("ArticleTitle", jsonObject1.getString("ArticleTitle"));
                        hm.put("imageUrl", jsonObject1.getString("imageUrl"));
                        hm.put("discription", jsonObject1.getString("discription"));
                        hm.put("documentUrl", jsonObject1.getString("documentUrl"));
                        hm.put("EntryDate", jsonObject1.getString("EntryDate"));
                        hm.put("ViewCount", jsonObject1.getString("ViewCount"));
                        hm.put("CommentCount", jsonObject1.getString("CommentCount"));
                        hm.put("get_userid", UserId);
                        arrayList2.add(hm);
                    }
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(DashboardActivity .this, 1);
                    DashboardListAdapter customerListAdapter = new DashboardListAdapter(DashboardActivity.this, arrayList2);
                    cust_recyclerView.setLayoutManager(gridLayoutManager);
                    cust_recyclerView.setAdapter(customerListAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                } }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("myTag", "message:"+error);
                Toast.makeText(DashboardActivity.this, "Something went wrong!"+error, Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(DashboardActivity.this);
        requestQueue.add(stringRequest);

    }


    public void GetSingleOfferBanner() {
        final ProgressDialog progressDialog = new ProgressDialog(DashboardActivity.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL+"GetDashboardOffer", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>"," ");
                jsonString = jsonString.replace("<string xmlns=\"http://examcoach.in/\">"," ");
                jsonString = jsonString.replace("</string>"," ");
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray jsonArrayr = jsonObject.getJSONArray("OfferList");
                    for (int i = 0; i < jsonArrayr.length(); i++) {
                        JSONObject jsonObject1 = jsonArrayr.getJSONObject(i);
                        link_tv.setText(jsonObject1.getString("discription")+"\n"+jsonObject1.getString("documentUrl")+")");
                        if (jsonObject1.getString("imageUrl").equalsIgnoreCase("")){
                        }
                        else {
                            Glide.with(getApplicationContext()).load(jsonObject1.getString("imageUrl")).error(R.drawable.logo).into(offer_img);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("myTag", "message:"+error);
                Toast.makeText(DashboardActivity.this, "Something went wrong!"+error, Toast.LENGTH_SHORT).show();
            }
        }) {  @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            HashMap<String, String> params = new HashMap<>();
            params.put("UserId",UserId);
            return params; }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(DashboardActivity.this);
        requestQueue.add(stringRequest);

    }




    private void ViewProfileAPI() {
        final ProgressDialog progressDialog = new ProgressDialog(DashboardActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL + "GetStudentProfile", new Response.Listener<String>() {
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
                    txt_username.setText(object.getString("StudentName")+"("+object.getString("MobileNo")+")");
                    txt_usergmail.setText(object.getString("EmailAddress"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(DashboardActivity.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserId", UserId);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(DashboardActivity.this);
        requestQueue.add(stringRequest);
    }


    public Intent getOpenFacebookIntent() {
        try {
            getPackageManager().getPackageInfo("com.akp.examcoach", 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/examcoach1"));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/examcoach1"));
        }
    }




    public void getDashboardHistoryAyogList() {
        final ProgressDialog progressDialog = new ProgressDialog(DashboardActivity.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Api_Urls.BaseURL+"GetHomePageList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>"," ");
                jsonString = jsonString.replace("<string xmlns=\"http://examcoach.in/\">"," ");
                jsonString = jsonString.replace("</string>"," ");
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray jsonArrayr = jsonObject.getJSONArray("HomeCategoryList");
                    for (int i = 0; i < jsonArrayr.length(); i++) {
                        JSONObject jsonObject1 = jsonArrayr.getJSONObject(i);
                        HashMap<String, String> hm = new HashMap<>();
                        hm.put("HomeCatId", jsonObject1.getString("HomeCatId"));
                        hm.put("HomeCatName", jsonObject1.getString("HomeCatName"));
                        hm.put("ImageUrl", jsonObject1.getString("ImageUrl"));
                        arrayList3.add(hm);
                    }
//                    LinearLayoutManager HorizontalLayout1 = new LinearLayoutManager(
//                            getApplicationContext(),
//                            LinearLayoutManager.HORIZONTAL,
//                            false);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(DashboardActivity .this, 3);
                    DashboardListAyogAdapter customerListAdapter = new DashboardListAyogAdapter(DashboardActivity.this, arrayList3);
                    cource_rec.setLayoutManager(gridLayoutManager);
                    cource_rec.setAdapter(customerListAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                } }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("myTag", "message:"+error);
                Toast.makeText(DashboardActivity.this, "Something went wrong!"+error, Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(DashboardActivity.this);
        requestQueue.add(stringRequest);
    }



    public void getDashboardHistoryAyogList1() {
        final ProgressDialog progressDialog = new ProgressDialog(DashboardActivity.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Api_Urls.BaseURL+"GetDashboardList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>"," ");
                jsonString = jsonString.replace("<string xmlns=\"http://examcoach.in/\">"," ");
                jsonString = jsonString.replace("</string>"," ");
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray jsonArrayr = jsonObject.getJSONArray("AayogList");
                    for (int i = 0; i < jsonArrayr.length(); i++) {
                        JSONObject jsonObject1 = jsonArrayr.getJSONObject(i);
                        HashMap<String, String> hm = new HashMap<>();
                        hm.put("AayogId", jsonObject1.getString("AayogId"));
                        hm.put("AayogTitle", jsonObject1.getString("AayogTitle"));
                        hm.put("imageUrl", jsonObject1.getString("imageUrl"));
                        arrayList7.add(hm);
                    }
//                    LinearLayoutManager HorizontalLayout1 = new LinearLayoutManager(
//                            getApplicationContext(),
//                            LinearLayoutManager.HORIZONTAL,
//                            false);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(DashboardActivity .this, 3);
                    DashboardListEbookPDFAdapter customerListAdapter = new DashboardListEbookPDFAdapter(DashboardActivity.this, arrayList7);
                    cource_rec1.setLayoutManager(gridLayoutManager);
                    cource_rec1.setAdapter(customerListAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                } }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("myTag", "message:"+error);
                Toast.makeText(DashboardActivity.this, "Something went wrong!"+error, Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(DashboardActivity.this);
        requestQueue.add(stringRequest);

    }




    public void getDashboardHistoryCourceList() {
        final ProgressDialog progressDialog = new ProgressDialog(DashboardActivity.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Api_Urls.BaseURL+"GetHomePageList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>"," ");
                jsonString = jsonString.replace("<string xmlns=\"http://examcoach.in/\">"," ");
                jsonString = jsonString.replace("</string>"," ");
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray jsonArrayr = jsonObject.getJSONArray("ExamTypeList");
                    for (int i = 0; i < jsonArrayr.length(); i++) {
                        JSONObject jsonObject1 = jsonArrayr.getJSONObject(i);
                        HashMap<String, String> hm = new HashMap<>();
                        hm.put("examtypeId", jsonObject1.getString("examtypeId"));
                        hm.put("imageUrl", jsonObject1.getString("imageUrl"));
                        hm.put("examtypeTitle", jsonObject1.getString("examtypeTitle"));
                        hm.put("HomeCatId", "1");
                        arrayList4.add(hm);
                    }
//                    LinearLayoutManager HorizontalLayout1 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(DashboardActivity .this, 2);
                    DashboardListCourceAdapter customerListAdapter = new DashboardListCourceAdapter(DashboardActivity.this, arrayList4);
                    exam_rec.setLayoutManager(gridLayoutManager);
                    exam_rec.setAdapter(customerListAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                } }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("myTag", "message:"+error);
                Toast.makeText(DashboardActivity.this, "Something went wrong!"+error, Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(DashboardActivity.this);
        requestQueue.add(stringRequest);

    }

    public void getDashboardHistoryELibraryList() {
        final ProgressDialog progressDialog = new ProgressDialog(DashboardActivity.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Api_Urls.BaseURL+"GetDashboardList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("sadada",response);
                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>"," ");
                jsonString = jsonString.replace("<string xmlns=\"http://examcoach.in/\">"," ");
                jsonString = jsonString.replace("</string>"," ");
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray jsonArrayr = jsonObject.getJSONArray("E_libraryList");
                    for (int i = 0; i < jsonArrayr.length(); i++) {
                        JSONObject jsonObject1 = jsonArrayr.getJSONObject(i);
                        HashMap<String, String> hm = new HashMap<>();
                        hm.put("E_libraryId", jsonObject1.getString("E_libraryId"));
                        hm.put("Title", jsonObject1.getString("Title"));
                        hm.put("pdfFile", jsonObject1.getString("pdfFile"));
                        hm.put("thumbnailUrl", jsonObject1.getString("thumbnailUrl"));
                        hm.put("PaidStatus", jsonObject1.getString("PaidStatus"));
                        hm.put("amount", jsonObject1.getString("amount"));
                        hm.put("ViewType", jsonObject1.getString("ViewType"));
                        hm.put("Description", jsonObject1.getString("Description"));
                        arrayList5.add(hm);
                    }
                    LinearLayoutManager HorizontalLayout1 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                    DashboardListElibraryAdapter customerListAdapter = new DashboardListElibraryAdapter(DashboardActivity.this, arrayList5);
                    library_rec.setLayoutManager(HorizontalLayout1);
                    library_rec.setAdapter(customerListAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                } }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("myTag", "message:"+error);
                Toast.makeText(DashboardActivity.this, "Something went wrong!"+error, Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(DashboardActivity.this);
        requestQueue.add(stringRequest);

    }


    public void getDashboardHistoryAcademicList() {
        final ProgressDialog progressDialog = new ProgressDialog(DashboardActivity.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Api_Urls.BaseURL+"GetDashboardList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>"," ");
                jsonString = jsonString.replace("<string xmlns=\"http://examcoach.in/\">"," ");
                jsonString = jsonString.replace("</string>"," ");
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray jsonArrayr = jsonObject.getJSONArray("Academiclist");
                    for (int i = 0; i < jsonArrayr.length(); i++) {
                        JSONObject jsonObject1 = jsonArrayr.getJSONObject(i);
                        HashMap<String, String> hm = new HashMap<>();
                        hm.put("AayogId", jsonObject1.getString("AayogId"));
                        hm.put("imageUrl", jsonObject1.getString("imageUrl"));
                        hm.put("AayogTitle", jsonObject1.getString("AayogTitle"));
                        arrayList6.add(hm);
                    }
                    LinearLayoutManager HorizontalLayout1 = new LinearLayoutManager(
                            getApplicationContext(),
                            LinearLayoutManager.HORIZONTAL,
                            false);
                    DashboardListAcademicAdapter customerListAdapter = new DashboardListAcademicAdapter(DashboardActivity.this, arrayList6);
                    academic_rec.setLayoutManager(HorizontalLayout1);
                    academic_rec.setAdapter(customerListAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                } }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("myTag", "message:"+error);
                Toast.makeText(DashboardActivity.this, "Something went wrong!"+error, Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(DashboardActivity.this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    public void LogoutAPI() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL + "UserLogOut", new Response.Listener<String>() {
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
                    if (object.getString("msg").equalsIgnoreCase("Success")) {
                        SharedPreferences myPrefs = getSharedPreferences("login_preference", MODE_PRIVATE);
                        SharedPreferences.Editor editor = myPrefs.edit();
                        editor.clear();
                        editor.commit();
                        SharedPreferences myPrefs1 = getSharedPreferences("refer_preference", MODE_PRIVATE);
                        SharedPreferences.Editor editor1 = myPrefs1.edit();
                        editor1.clear();
                        Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
                        intent.putExtra("finish", true);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clean up all activities
                        startActivity(intent);
                    } else {
                        Toast.makeText(DashboardActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(DashboardActivity.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserName", UserId);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(DashboardActivity.this);
        requestQueue.add(stringRequest);
    }

    private void getVersionInfo() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
            versionCode = String.valueOf(packageInfo.versionCode);
            Log.v("vname", versionName + " ," + String.valueOf(versionCode));
            version_tv.setText("Version : "+versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

}