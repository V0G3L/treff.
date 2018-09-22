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
 * a command to edit the password of an Account
 */
public class EditPasswordCommand extends AbstractCommand {


    public EditPasswordCommand(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) {
        Input input = (Input) commandInput;

        Account actingAccount = input.getActingAccount();

        // check if password is correct
        if (!actingAccount.checkPassword(input.pass)) {
            return new ErrorOutput(ErrorCode.CREDWRONG);
        }

        // edit password
        actingAccount.setPassword(input.newPass);

        return new Output();
    }

    public static class Input extends CommandInputLoginRequired {

        final String pass;
        final String newPass;

        public Input(@JsonProperty("pass") String pass,
                     @JsonProperty("new-pass") String newPass,
                     @JsonProperty("token") String token) {
            super(token);
            this.pass = pass;
            this.newPass = newPass;
        }

        @Override
        public boolean syntaxCheck() {
            return validatePassword(pass)
                    && validatePassword(newPass);
        }
    }

    public static class Output extends CommandOutput { }

}
