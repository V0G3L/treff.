package org.pispeb.treffpunkt.server.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.SessionFactory;
import org.pispeb.treffpunkt.server.commands.io.CommandInputLoginRequired;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.io.ErrorOutput;
import org.pispeb.treffpunkt.server.hibernate.Account;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

/**
 * a command to manage the block list of the executing account.
 * This can either be blocking or unblocking an account.
 */
public abstract class ManageBlockCommand extends AbstractCommand {

    protected ManageBlockCommand(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    /**
     * checks if the parameters are valid
     *
     * @param input   the input of the command
     * @param issueBlock {@code true} if blocking account, {@code false} if unblocking account
     * @return the error code if an error occurred, null if not
     */
    protected ErrorOutput checkParameters(Input input, boolean issueBlock) {
        Account actingAccount = input.getActingAccount();
        Account otherAccount = accountManager.getAccount(input.accountId);
        if (otherAccount == null) {
            return new ErrorOutput(ErrorCode.USERIDINVALID);
        }

        // check block list
        if (issueBlock && actingAccount.getAllBlocks().containsKey(input.accountId)) {
            return new ErrorOutput(ErrorCode.BLOCKINGALREADY);
        }
        if (!issueBlock && !actingAccount.getAllBlocks().containsKey(input.accountId)) {
            return new ErrorOutput(ErrorCode.NOTBLOCKING);
        }

        // if parameters are correct, return null
        return null;
    }

    public static class Input extends CommandInputLoginRequired {

        final int accountId;

        public Input(@JsonProperty("id") int accountId,
                     @JsonProperty("token") String token) {
            super(token);
            this.accountId = accountId;
        }
    }

    public static class Output extends CommandOutput { }
}
