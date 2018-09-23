package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;
import org.pispeb.treffpunkt.server.commands.io.CommandInputLoginRequired;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.hibernate.Account;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

/**
 * a command to get a detailed description of an account by its ID
 */
public class GetUserDetailsCommand extends AbstractCommand
        <GetUserDetailsCommand.Input,GetUserDetailsCommand.Output> {


    public GetUserDetailsCommand(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    protected Output executeInternal(Input input) {

        // get account
        Account account = accountManager.getAccount(input.id);
        if (account == null)
            throw ErrorCode.USERIDINVALID.toWebException();

        return new Output(account);
    }

    public static class Input extends CommandInputLoginRequired {

        final int id;

        public Input(int id, String token) {
            super(token);
            this.id = id;
        }
    }

    public static class Output extends CommandOutput {

        public final org.pispeb.treffpunkt.server.service.domain.Account account;

        Output(Account hibAccount) {
            this.account = new org.pispeb.treffpunkt.server.service.domain.Account(hibAccount);
        }
    }
}
