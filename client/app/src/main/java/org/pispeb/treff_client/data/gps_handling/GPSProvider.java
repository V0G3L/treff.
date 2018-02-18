package org.pispeb.treff_client.data.gps_handling;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.BuildConfig;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import org.pispeb.treff_client.R;
import org.pispeb.treff_client.data.networking.RequestEncoder;
import org.pispeb.treff_client.view.home.HomeActivity;

import java.util.Calendar;
import java.util.Date;
import java.util.PriorityQueue;

/**
 * Receives updates about the users position, passes them to server.
 * Receives requests for position until specified date via startService.
 * Stops self once all requests are dealt with.
 */

public class GPSProvider extends Service implements LocationListener {

    // Lock that all startService calls for this service synchronize to, to
    // avoid conflicts when stopping the Service
    public static final Object LOCK = new Object();

    // most accurate and recent location
    private Location currentBestLocation;

    // queue of requests for the users position from different groups
    private PriorityQueue<ListEntry> queue;

    private RequestEncoder encoder;

    // indicator of noticeable delay to last location
    private static final int TWO_MINUTES = 1000 * 60 * 2;

    // keys for values passed in Intent
    public static final String INTENT_CMD = "commandIntent";
    public static final String INTENT_GRP = "groupIntent";
    public static final String INTENT_TIME = "timeIntent";

    // modes in which the queue can be changed
    public static final int CMD_ADD = 1;
    public static final int CMD_REMOVE = 2;
    private final String channelId = "my_channel_01";
    private final int notificationId = 130898;

    @Override
    public void onCreate() {
        Log.i("GPSProvider", "Service starting1");
        encoder = RequestEncoder.getInstance();

        Log.i("GPSProvider", "Service starting2");
        queue = new PriorityQueue<>();

        String locationProvider = LocationManager.GPS_PROVIDER;
        Log.i("GPSProvider", "Service starting3");
        LocationManager locationManager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);
        // check if Permission to use GPS is granted
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager
                .PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            return;
        }
        // TODO define minimum update interval in config
        locationManager.requestLocationUpdates(locationProvider, 5000, 0,
                this);

        //setupNotificationChannel();
        setupNotification();

        Log.i("GPSProvider", "Service starting done");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Always synchronized to LOCK!!

        int cmd = intent.getExtras().getInt(INTENT_CMD);
        int groupId = intent.getExtras().getInt(INTENT_GRP);
        Date end = (Date) intent.getExtras().get(INTENT_TIME);
        encoder.publishPosition(groupId, end);
        ListEntry c = new ListEntry(groupId, end);
        synchronized (queue) {
            switch (cmd) {
                case CMD_ADD:
                    queue.add(c);
                    break;
                case CMD_REMOVE:
                    while (queue.remove(c)) {
                        // Do nothing, remove is true if an item equal to c is
                        // found and removed. In case an event is added multiple
                        // times, repeat
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Unknown command");
            }
        }

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * Is called when the service stops, clean up
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }



    @Override
    public void onLocationChanged(Location location) {
        // update location if needed
        if (isBetterLocation(location)) {
            currentBestLocation = location;
        } else {
            return;
        }
        // avoid racing conditions between this and startService by
        // synchronizing to LOCK, which all startService calls should be
        // synchronized to as well
        synchronized (LOCK) {
            // remove all items from queue that ended before current time
            Date currentDate = Calendar.getInstance().getTime();
            while (!queue.isEmpty() && currentDate.after(queue.peek()
                    .endOfTransmission)) {
                queue.poll();
            }
            if (queue.isEmpty()) {
                stopSelf();
                NotificationManager mNotificationManager =
                        (NotificationManager) this.getSystemService(Context
                                .NOTIFICATION_SERVICE);
                mNotificationManager.cancel(notificationId);
            } else {
                Log.i("GPSProvider", currentBestLocation.toString());
                encoder.updatePosition(currentBestLocation.getLatitude(),
                        currentBestLocation.getLongitude(),
                        new Date(currentBestLocation.getTime()));
            }
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    /**
     * Combination of group and date until which this group requests updates
     * about the users position.
     */
    private class ListEntry implements Comparable<ListEntry> {
        public int groupId;
        public Date endOfTransmission;

        public ListEntry(int groupId, Date endOfTransmission) {
            this.groupId = groupId;
            this.endOfTransmission = endOfTransmission;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ListEntry listEntry = (ListEntry) o;

            if (groupId != listEntry.groupId) return false;
            return endOfTransmission.equals(listEntry.endOfTransmission);
        }

        @Override
        public int compareTo(@NonNull ListEntry o) {
            return endOfTransmission.compareTo(o.endOfTransmission);
        }
    }

    private void setupNotification() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this.getApplicationContext(),
                        channelId);
        Intent ii = new Intent(this.getApplicationContext(),
                HomeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, ii,
                0);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.setBigContentTitle(getString(R.string.channel_description));

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap
                        .ic_launcher_round);
        mBuilder.setLargeIcon(icon);
        mBuilder.setContentTitle(getString(R.string.app_name));
        mBuilder.setContentText(getString(R.string.tap_for_back));
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setStyle(bigText);
        mBuilder.setOngoing(true);

        NotificationManager mNotificationManager =
                (NotificationManager) this.getSystemService(Context
                        .NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(channel);
        }

        mNotificationManager.notify(notificationId, mBuilder.build());
    }

    /**
     * Compares given location to current best location in terms of accuracy,
     * delay and provider credibility.
     *
     * @param location new, alternative location
     * @return true if given location is "better"
     */
    private boolean isBetterLocation(Location location) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location,
        // use
        // the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it
            // must be
            // worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location
                .getAccuracy() - currentBestLocation
                .getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and
        // accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate &&
                isFromSameProvider) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether two providers are the same
     */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }
}
