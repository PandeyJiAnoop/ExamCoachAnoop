package com.akp.examcoach.Basic.QuizExam;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.akp.examcoach.Basic.DashboardActivity;
import com.akp.examcoach.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StartQuizTestWeb extends AppCompatActivity {
    Context context;
    private WebView web;
    public static final int REQUEST_CODE_LOLIPOP = 1;
    private final static int RESULT_CODE_ICE_CREAM = 2;
    private ValueCallback<Uri[]> mFilePathCallback;
    private String mCameraPhotoPath;
    private ValueCallback<Uri> mUploadMessage;
    String UserId,getExamCoad,getExamType;
    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_quiz_test_web);
        context=this.getApplicationContext();

        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("username", "");
        getExamCoad=getIntent().getStringExtra("ExamCode");
        getExamType=getIntent().getStringExtra("ExamType");
        web = (WebView) findViewById(R.id.activity_main_webview);
        web.setWebViewClient(new WebViewClient() {
            // This method will be triggered when the Page Started Loading

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                dialog = ProgressDialog.show(StartQuizTestWeb.this, null,
                        "Please Wait...");
                dialog.setCancelable(true);
                super.onPageStarted(view, url, favicon);
            }

            // This method will be triggered when the Page loading is completed

            public void onPageFinished(WebView view, String url) {
                dialog.dismiss();
                super.onPageFinished(view, url);
            }

            // This method will be triggered when error page appear

            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                dialog.dismiss();
                // You can redirect to your own page instead getting the default
                // error page
                Toast.makeText(StartQuizTestWeb.this,
                        "The Requested Page Does Not Exist", Toast.LENGTH_LONG).show();
                web.loadUrl("http://examcoach.in/Exam/TakeExam_app?ExamCode="+getExamCoad+"&UserName="+UserId);
                super.onReceivedError(view, errorCode, description, failingUrl);
            }
        });
        WebSettings webSettings = web.getSettings();
        web.loadUrl("http://examcoach.in/Exam/TakeExam_app?ExamCode="+getExamCoad+"&UserName="+UserId);
        web.getSettings().setLoadWithOverviewMode(true);
        web.getSettings().setUseWideViewPort(true);
        webSettings.setJavaScriptEnabled(true);


        web.getSettings().setDomStorageEnabled(true);
        web.getSettings().setSaveFormData(true);
        web.getSettings().setAllowContentAccess(true);
        web.getSettings().setAllowFileAccess(true);
        web.getSettings().setAllowFileAccessFromFileURLs(true);
        web.getSettings().setAllowUniversalAccessFromFileURLs(true);
        web.getSettings().setSupportZoom(true);
        web.setWebViewClient(new WebViewClient());
        web.setClickable(true);
        web.setWebChromeClient(new WebChromeClient());


       /* *web.setWebChromeClient(new WebChromeClient());
        web.setWebViewClient(new WebViewClient());**/

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        AlertDialog.Builder builder = new AlertDialog.Builder(StartQuizTestWeb.this);
        builder.setMessage("Are you sure want to exit?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(DialogInterface dialog, int which) {
               Intent intent=new Intent(getApplicationContext(), DashboardActivity.class);
               startActivity(intent);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

}

//        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
//        UserId= sharedPreferences.getString("username", "");
//        ConnectivityManager connectivityManager=(ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo wifi=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//        NetworkInfo network=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//        if (wifi.isConnected())
//        {
//            //Internet available
//            mWebView = findViewById(R.id.activity_main_webview);
//            WebSettings webSettings = mWebView.getSettings();
//            webSettings.setJavaScriptEnabled(true);
//            mWebView.setWebViewClient(new MyWebViewClient());
//            mWebView.setWebChromeClient(new WebChromeClient() {
//                private String TAG;
//                // For Android 3.0+
//                public void openFileChooser(ValueCallback<Uri> uploadMsg) {
//                    mUploadMessage = uploadMsg;
//                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//                    i.addCategory(Intent.CATEGORY_OPENABLE);
//                    i.setType("image/*");
//                    startActivityForResult(Intent.createChooser(i, "File Chooser"),
//                            RESULT_CODE_ICE_CREAM);
//
//                }
//                // For Android 3.0+
//                public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
//                    mUploadMessage = uploadMsg;
//                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//                    i.addCategory(Intent.CATEGORY_OPENABLE);
//                    i.setType("*/*");
//                    startActivityForResult(Intent.createChooser(i, "File Browser"),
//                            RESULT_CODE_ICE_CREAM);
//                }
//                //For Android 4.1
//                public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
//                    mUploadMessage = uploadMsg;
//                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//                    i.addCategory(Intent.CATEGORY_OPENABLE);
//                    i.setType("image/*");
//                    startActivityForResult(Intent.createChooser(i, "File Chooser"),
//                            RESULT_CODE_ICE_CREAM);
//
//                }
//                //For Android5.0+
//                public boolean onShowFileChooser(
//                        WebView webView, ValueCallback<Uri[]> filePathCallback,
//                        FileChooserParams fileChooserParams) {
//                    if (mFilePathCallback != null) {
//                        mFilePathCallback.onReceiveValue(null);
//                    }
//                    mFilePathCallback = filePathCallback;
//                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    if (takePictureIntent.resolveActivity(StartQuizTestWeb.this.getPackageManager()) != null) {
//                        // Create the File where the photo should go
//                        File photoFile = null;
//                        try {
//                            photoFile = createImageFile();
//                            takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
//                        } catch (IOException ex) {
//                            // Error occurred while creating the File
//                            Log.e(TAG, "Unable to create Image File", ex);
//                        }
//                        // Continue only if the File was successfully created
//                        if (photoFile != null) {
//                            mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
//                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
//                                    Uri.fromFile(photoFile));
//                        } else {
//                            takePictureIntent = null;
//                        }
//                    }
//                    Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
//                    contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
//                    contentSelectionIntent.setType("image/*");
//                    Intent[] intentArray;
//                    if (takePictureIntent != null) {
//                        intentArray = new Intent[]{takePictureIntent};
//                    } else {
//                        intentArray = new Intent[0];
//                    }
//                    Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
//                    chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
//                    chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
//                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
//
//                    startActivityForResult(chooserIntent, REQUEST_CODE_LOLIPOP);
//
//                    return true;
//                }
//            });
//            //REMOTE RESOURCE
//            mWebView.loadUrl("http://examcoach.in/Exam/TakeExam_app?ExamCode="+getExamCoad+"&UserName="+UserId);
//        }
//        else if(network.isConnected())
//        {
//            //Internet available
//            mWebView = findViewById(R.id.activity_main_webview);
//            WebSettings webSettings = mWebView.getSettings();
//            webSettings.setJavaScriptEnabled(true);
//            mWebView.setWebViewClient(new MyWebViewClient());
//            mWebView.setWebChromeClient(new WebChromeClient() {
//                private String TAG;
//                // For Android 3.0+
//                public void openFileChooser(ValueCallback<Uri> uploadMsg) {
//                    mUploadMessage = uploadMsg;
//                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//                    i.addCategory(Intent.CATEGORY_OPENABLE);
//                    i.setType("image/*");
//                    startActivityForResult(Intent.createChooser(i, "File Chooser"),
//                            RESULT_CODE_ICE_CREAM);
//
//                }
//                // For Android 3.0+
//                public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
//                    mUploadMessage = uploadMsg;
//                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//                    i.addCategory(Intent.CATEGORY_OPENABLE);
//                    i.setType("*/*");
//                    startActivityForResult(Intent.createChooser(i, "File Browser"),
//                            RESULT_CODE_ICE_CREAM);
//                }
//                //For Android 4.1
//                public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
//                    mUploadMessage = uploadMsg;
//                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//                    i.addCategory(Intent.CATEGORY_OPENABLE);
//                    i.setType("image/*");
//                    startActivityForResult(Intent.createChooser(i, "File Chooser"),
//                            RESULT_CODE_ICE_CREAM);
//
//                }
//                //For Android5.0+
//                public boolean onShowFileChooser(
//                        WebView webView, ValueCallback<Uri[]> filePathCallback,
//                        FileChooserParams fileChooserParams) {
//                    if (mFilePathCallback != null) {
//                        mFilePathCallback.onReceiveValue(null);
//                    }
//                    mFilePathCallback = filePathCallback;
//                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    if (takePictureIntent.resolveActivity(StartQuizTestWeb.this.getPackageManager()) != null) {
//                        // Create the File where the photo should go
//                        File photoFile = null;
//                        try {
//                            photoFile = createImageFile();
//                            takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
//                        } catch (IOException ex) {
//                            // Error occurred while creating the File
//                            Log.e(TAG, "Unable to create Image File", ex);
//                        }
//                        // Continue only if the File was successfully created
//                        if (photoFile != null) {
//                            mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
//                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
//                                    Uri.fromFile(photoFile));
//                        } else {
//                            takePictureIntent = null;
//                        }
//                    }
//                    Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
//                    contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
//                    contentSelectionIntent.setType("image/*");
//                    Intent[] intentArray;
//                    if (takePictureIntent != null) {
//                        intentArray = new Intent[]{takePictureIntent};
//                    } else {
//                        intentArray = new Intent[0];
//                    }
//                    Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
//                    chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
//                    chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
//                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
//
//                    startActivityForResult(chooserIntent, REQUEST_CODE_LOLIPOP);
//
//                    return true;
//                }
//            });
//            //REMOTE RESOURCE
//            mWebView.loadUrl("http://examcoach.in/Exam/TakeExam_app?ExamCode="+getExamCoad+"&UserName="+UserId);
//
//
//        }
//        else
//        {
//            Toast.makeText(getApplicationContext(),"Network Is Not Available", Toast.LENGTH_LONG).show();
//        }
//
//
//        // LOCAL RESOURCE
//        // mWebView.loadUrl("file:///android_asset/index.html");
//    }
//
//
//    @Override
//    public void onBackPressed() {
//        if(mWebView.canGoBack()) {
//            mWebView.goBack();
//        } else {
//            super.onBackPressed();
//        }
//    }
//
//
//    private File createImageFile() throws IOException {
//        // Create an image file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_PICTURES);
//        File imageFile = File.createTempFile(
//                imageFileName,  /* prefix */
//                ".jpg",         /* suffix */
//                storageDir      /* directory */
//        );
//        return imageFile;
//    }
//    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
//    private void setUpWebViewDefaults(WebView webView) {
//        WebSettings settings = webView.getSettings();
//        // Enable Javascript
//        settings.setJavaScriptEnabled(true);
//        // Use WideViewport and Zoom out if there is no viewport defined
//        settings.setUseWideViewPort(true);
//        settings.setLoadWithOverviewMode(true);
//        // Enable pinch to zoom without the zoom buttons
//        settings.setBuiltInZoomControls(true);
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
//            // Hide the zoom controls for HONEYCOMB+
//            settings.setDisplayZoomControls(false);
//        }
//        // Enable remote debugging via chrome://inspect
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            WebView.setWebContentsDebuggingEnabled(true);
//        }
//        // We set the WebViewClient to ensure links are consumed by the WebView rather
//        // than passed to a browser if it can
//        webView.setWebViewClient(new WebViewClient());
//    }
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            case RESULT_CODE_ICE_CREAM:
//                Uri uri = null;
//                if (data != null) {
//                    uri = data.getData();
//                }
//                mUploadMessage.onReceiveValue(uri);
//                mUploadMessage = null;
//                break;
//            case REQUEST_CODE_LOLIPOP:
//                Uri[] results = null;
//                // Check that the response is a good one
//                if (resultCode == Activity.RESULT_OK) {
//                    if (data == null) {
//                        // If there is not data, then we may have taken a photo
//                        if (mCameraPhotoPath != null) {
//                            results = new Uri[]{Uri.parse(mCameraPhotoPath)};
//                        }
//                    } else {
//                        String dataString = data.getDataString();
//                        if (dataString != null) {
//                            results = new Uri[]{Uri.parse(dataString)};
//                        }
//                    }
//                }
//                mFilePathCallback.onReceiveValue(results);
//                mFilePathCallback = null;
//                break;
//        }
//    }
//
//}
