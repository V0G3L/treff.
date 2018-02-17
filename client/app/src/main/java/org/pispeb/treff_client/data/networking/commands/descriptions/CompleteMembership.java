package org.pispeb.treff_client.data.networking.commands.descriptions;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by matth on 17.02.2018.
 */

public class CompleteMembership {

    public final int id;
    @JsonProperty("account-id")
    public final int accountId;


    //TODO permissions
    public final Object permissions;

    public CompleteMembership(int membershipId, int accountId, Object permissions){
        this.id = membershipId;
        this.accountId = accountId;
        this.permissions = permissions;
    }
}
