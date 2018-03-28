package com.cky.icomic;

import android.app.Application;
import android.util.DisplayMetrics;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

/**
 * Created by Admin on 2018/3/20.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //init Iconify.
        Iconify.with(new FontAwesomeModule());
    }
}
