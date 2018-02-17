package org.pispeb.treff_client.view.home;

import android.app.Application;
import android.content.Context;

/**
 * Static methods to access context globally for SharedPreference interactions
 */
public class TreffPunkt extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        TreffPunkt.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return TreffPunkt.context;
    }
}