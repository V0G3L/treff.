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
     * TODO: doc
     * @param context
     * @param groupId
     * @param endOfTransmission
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
     * TODO: doc
     * @param context
     * @param groupId
     * @param endOfTransmission
     */
    public static void removeRequestFromService(Context context, int groupId,
                                           Date endOfTransmission) {
        Intent intent = new Intent(context, GPSProvider.class);
        intent.putExtra(GPSProvider.INTENT_CMD, GPSProvider.CMD_REMOVE);
        intent.putExtra(GPSProvider.INTENT_GRP, groupId);
        intent.putExtra(GPSProvider.INTENT_TIME, endOfTransmission);

        startService(context, intent);
    }

    private static void startService(Context context, Intent intent) {
        synchronized (GPSProvider.LOCK) {
            context.startService(intent);
        }
    }
}
