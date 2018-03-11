package org.pispeb.treff_client.data.gps_handling;

import android.content.Context;
import android.content.Intent;

import java.util.Date;

/**
 * Starts GPSProvider (Service).
 * Avoids conflicts in queue by synchronizing to LOCK, which queue operations
 * in Service are also locked by
 */

public class GPSProviderManager {

    /**
     * Adds a transmission request to the GPSProviders queue
     * Avoids racing conditions when accessing the queue by locking to common
     * object Lock
     * @param context Context from which the intent was sent
     * @param groupId Id of the group that the user wants to share his/her
     *                position with
     * @param endOfTransmission time until which the transmission lasts
     */
    public static void addRequestToService(Context context, int groupId,
                                            Date endOfTransmission) {
        Intent intent = new Intent(context, GPSProvider.class);
        intent.putExtra(GPSProvider.INTENT_CMD, GPSProvider.CMD_ADD);
        intent.putExtra(GPSProvider.INTENT_GRP, groupId);
        intent.putExtra(GPSProvider.INTENT_TIME, endOfTransmission);

        startService(context, intent);
    }

    /**
     * Removes a transmission request for a group from the queue
     * if the user decides to interrupt the transmission
     * @param context Context from which the intent was sent
     * @param groupId Id of the group that the user no longer wants to share
     *                his/her position with
     */
    public static void removeRequestFromService(Context context, int groupId) {
        Intent intent = new Intent(context, GPSProvider.class);
        intent.putExtra(GPSProvider.INTENT_CMD, GPSProvider.CMD_REMOVE);
        intent.putExtra(GPSProvider.INTENT_GRP, groupId);

        startService(context, intent);
    }

    /**
     * actually start the Service itself.
     * This should be the only place that this is done from to avoid racing
     * conditions
     * @param context Context from which the intent was sent
     * @param intent Intent containing the Information described above
     */
    private static void startService(Context context, Intent intent) {
        synchronized (GPSProvider.LOCK) {
            context.startService(intent);
        }
    }
}
