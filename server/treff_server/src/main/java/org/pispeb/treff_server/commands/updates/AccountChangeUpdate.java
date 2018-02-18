package org.pispeb.treff_server.commands.updates;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.pispeb.treff_server.commands.serializers.AccountCompleteSerializer;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.Update;

import java.util.Date;

public class AccountChangeUpdate extends UpdateToSerialize {
    @JsonProperty("account")
    @JsonSerialize(using = AccountCompleteSerializer.class)
    public final Account account;

    public AccountChangeUpdate(Date date, int creator, Account account) {
        super(Update.UpdateType.ACCOUNT_CHANGE.toString(), date, creator);
        this.account = account;
    }
}
