package org.pispeb.treff_client.data.networking.commands.descriptions;

/**
 * Created by matth on 17.02.2018.
 */

public class CompleteAccount {

    public final int id;
    public final String username;

    public CompleteAccount(int id, String username){
        this.id = id;
        this.username = username;
    }
}
