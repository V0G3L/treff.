package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;
import org.pispeb.treffpunkt.server.commands.io.CommandInput;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.hibernate.Account;

/**
 * Validates a login token.
 */
public class CheckLoginCommand extends AbstractCommand
        <CheckLoginCommand.Input, CheckLoginCommand.Output> {


    public CheckLoginCommand(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    protected Output executeInternal(Input input) {
        // check if account exists
        Account actingAccount = accountManager.getAccountByLoginToken(input.token);
        return new Output(actingAccount != null);
    }

    public static class Input extends CommandInput {

        final String token;

        public Input(String token) {
            this.token = token;
        }
    }

    public static class Output extends CommandOutput {

        public final boolean tokenValid;

        public Output(boolean tokenValid) {
            this.tokenValid = tokenValid;
        }
    }
}
