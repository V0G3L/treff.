package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treffpunkt.server.commands.io.CommandInput;
import org.pispeb.treffpunkt.server.commands.io.CommandInputLoginRequired;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.io.ErrorOutput;
import org.pispeb.treffpunkt.server.hibernate.Account;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

/**
 * a command to get the ID of an account by its name
 */
public class GetUserIdCommand extends AbstractCommand {


    public GetUserIdCommand(SessionFactory sessionFactory,
                            ObjectMapper mapper) {
        super(sessionFactory,Input.class, mapper);
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) {
        Input input = (Input) commandInput;

        // get account
        Account account = accountManager.getAccountByUsername(input.username);
        if (account == null)
            return new ErrorOutput(ErrorCode.USERNAMEINVALID);

        return new Output(account.getID());
    }

    public static class Input extends CommandInputLoginRequired {

        final String username;

        public Input(@JsonProperty("user") String username,
                     @JsonProperty("token") String token) {
            super(token);
            this.username = username;
        }
    }

    public static class Output extends CommandOutput {

        public final int id;

        Output(int id) {
            this.id = id;
        }
    }
}
