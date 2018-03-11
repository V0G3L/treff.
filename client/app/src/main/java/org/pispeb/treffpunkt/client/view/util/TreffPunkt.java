package org.pispeb.treffpunkt.client.view.util;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

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

    public static void showToast(String data) {
        Toast.makeText(context, data,
                Toast.LENGTH_SHORT).show();
    }
}