package com.br.tomei;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by Silas on 07/02/2018.
 */

public class App extends Application {

    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
