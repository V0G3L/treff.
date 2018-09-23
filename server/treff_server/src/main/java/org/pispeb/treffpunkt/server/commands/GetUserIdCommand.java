package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;
import org.pispeb.treffpunkt.server.commands.io.CommandInputLoginRequired;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.hibernate.Account;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

/**
 * a command to get the ID of an account by its name
 */
public class GetUserIdCommand extends AbstractCommand
        <GetUserIdCommand.Input,GetUserIdCommand.Output> {


    public GetUserIdCommand(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    protected Output executeInternal(Input input) {

        // get account
        Account account = accountManager.getAccountByUsername(input.username);
        if (account == null)
            throw ErrorCode.USERNAMEINVALID.toWebException();

        return new Output(account.getID());
    }

    public static class Input extends CommandInputLoginRequired {

        final String username;

        public Input(String username, String token) {
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
