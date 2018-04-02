package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.pispeb.treffpunkt.server.commands.io.CommandInput;
import org.pispeb.treffpunkt.server.commands.io.CommandInputLoginRequired;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.io.ErrorOutput;
import org.pispeb.treffpunkt.server.commands.serializers.AccountCompleteSerializer;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

/**
 * a command to get a detailed description of an account by its ID
 */
public class GetUserDetailsCommand extends AbstractCommand {


    public GetUserDetailsCommand(SessionFactory sessionFactory,
                                 ObjectMapper mapper) {
        super(sessionFactory,Input.class, mapper);
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) {
        Input input = (Input) commandInput;

        // check if account still exists
        Account actingAccount
                = getSafeForReading(input.getActingAccount());
        if (actingAccount == null)
            return new ErrorOutput(ErrorCode.TOKENINVALID);

        // get account
        Account account = getSafeForReading(
                accountManager.getAccount(input.id));
        if (account == null)
            return new ErrorOutput(ErrorCode.USERIDINVALID);

        return new Output(account);
    }

    public static class Input extends CommandInputLoginRequired {

        final int id;

        public Input(@JsonProperty("id") int id,
                     @JsonProperty("token") String token) {
            super(token);
            this.id = id;
        }
    }

    public static class Output extends CommandOutput {

        @JsonSerialize(using = AccountCompleteSerializer.class)
        @JsonProperty("account")
        final Account account;

        Output(Account account) {
            this.account = account;
        }
    }
}
