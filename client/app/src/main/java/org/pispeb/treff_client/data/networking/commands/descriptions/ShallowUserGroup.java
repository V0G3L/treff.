package org.pispeb.treff_client.data.networking.commands.descriptions;

/**
 * Created by matth on 17.02.2018.
 */

public class ShallowUserGroup {

    public final int id;
    public final String checksum;

    public ShallowUserGroup(int id, String checksum) {
        this.id = id;
        this.checksum = checksum;
    }
}
