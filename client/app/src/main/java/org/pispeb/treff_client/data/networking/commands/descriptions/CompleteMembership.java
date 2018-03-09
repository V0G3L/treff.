package org.pispeb.treff_client.data.networking.commands.descriptions;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by matth on 17.02.2018.
 */

public class CompleteMembership {

    public final String type;
    public final int id;
    @JsonProperty("account-id")
    public final int accountId;


    //TODO permissions
    public final Object permissions;

    public CompleteMembership(@JsonProperty("type") String type,
                              @JsonProperty("id") int membershipId,
                              @JsonProperty("account-id") int accountId,
                              @JsonProperty("permissions") Object permissions){
        this.type = type;
        this.id = membershipId;
        this.accountId = accountId;
        this.permissions = permissions;
    }
}
