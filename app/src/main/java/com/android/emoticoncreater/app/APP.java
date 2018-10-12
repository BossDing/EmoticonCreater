package com.android.emoticoncreater.app;

import android.app.Application;

import com.android.emoticoncreater.config.Constants;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * application
 */

public class APP extends Application {

    private static APP INStANCE = null;

    public static APP getInstance() {
        return INStANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        INStANCE = this;

        CrashReport.initCrashReport(getApplicationContext(), Constants.BUGLY_APP_ID, false);
    }
}
