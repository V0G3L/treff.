package org.pispeb.treff_client.data.networking.commands.descriptions;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by matth on 17.02.2018.
 */

public class CompleteAccount {

    public final String type;
    public final int id;
    public final String username;

    public CompleteAccount(@JsonProperty("type") String type,
                           @JsonProperty("id") int id,
                           @JsonProperty("username") String username){
        this.type = "account";
        this.id = id;
        this.username = username;
    }

    public boolean equals(CompleteAccount completeAccount) {
        return (this.username.equals(completeAccount.username) && this.id == completeAccount.id);
    }
}
