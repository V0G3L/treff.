package org.pispeb.treffpunkt.server.commands.updates;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.pispeb.treffpunkt.server.commands.serializers.AccountCompleteSerializer;
import org.pispeb.treffpunkt.server.hibernate.Account;

import java.util.Date;

public class AccountChangeUpdate extends UpdateToSerialize {
    @JsonSerialize(using = AccountCompleteSerializer.class)
    public final Account account;

    public AccountChangeUpdate(Date date, int creator, Account account) {
        super(UpdateType.ACCOUNT_CHANGE.toString(), date, creator);
        this.account = account;
    }
}
