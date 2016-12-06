package com.beelancrp.genesis;

import android.app.Application;

/**
 * Created by bilan on 06.12.2016.
 */

public class App extends Application {

    private static App instance;

    public App() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static App getInstance() {
        return instance;
    }

}
