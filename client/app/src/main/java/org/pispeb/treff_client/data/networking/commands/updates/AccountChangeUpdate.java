package org.pispeb.treff_client.data.networking.commands.updates;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.pispeb.treff_client.data.networking.commands.descriptions.CompleteAccount;

import java.util.Date;

public class AccountChangeUpdate extends UpdateToSerialize {

    public final CompleteAccount account;

    public AccountChangeUpdate(@JsonProperty("type") String type,
                               @JsonProperty("time-created") Date date,
                               @JsonProperty("creator") int creator,
                               @JsonProperty("account") CompleteAccount account) {
        super(UpdateType.ACCOUNT_CHANGE.toString(), date, creator);
        this.account = account;
    }
}
