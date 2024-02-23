package com.akp.examcoach.Basic.QuizExam;

import android.app.Activity;

public final class AppSettings extends OSettings {
    public static final String PREFS_MAIN_FILE = "PREFS_ZOZIMA_FILE";

    public static final String searchArray = "searchArrays";
    public static String accessToken;
    public static String latitude;
    public static String longitude;
    public static String language_code;

    public AppSettings(Activity mActivity) {
        super(mActivity);
    }


}