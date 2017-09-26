package com.msemyak.imagemagic;

import android.app.Application;

import timber.log.BuildConfig;
import timber.log.Timber;

public class ImageMagicApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

    }
}
