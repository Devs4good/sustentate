package com.sustentate.app;

import android.app.Application;
import android.content.Context;

public class SustentateApplication extends Application {

    public static Context CONTEXT;

    @Override
    public void onCreate () {
        super.onCreate();
        CONTEXT =  getApplicationContext();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}
