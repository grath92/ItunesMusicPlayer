package com.gopal.network;

/*
* Created By Gopal Krishna Rath
* -----27/11/2016
* */

import android.app.Application;

public class AppApplication extends Application {

    private static AppApplication sInstance;

    public static synchronized AppApplication getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

}
