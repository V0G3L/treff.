package org.pispeb.treffpunkt.client.view.util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

/**
 * Static methods to access context globally for SharedPreference interactions
 */
public class TreffPunkt extends Application {

    public final static String STANDARD_URL =
            "ws://[2a02:8071:21a1:5500:5d2e:7ae5:d772:9cd0]:8080" +
            "/treff_server/ws";

    private static Context context;

    public void onCreate() {
        super.onCreate();
        TreffPunkt.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return TreffPunkt.context;
    }

    public static SharedPreferences getSharedPref() {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static String getString(int resourceid, String def) {
        return getSharedPref().getString(context.getString(resourceid), def);
    }

    public static void showToast(String data) {
        Toast.makeText(context, data,
                Toast.LENGTH_SHORT).show();
    }
}