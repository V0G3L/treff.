package org.pispeb.treffpunkt.server.commands;

import org.hibernate.SessionFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pispeb.treffpunkt.server.commands.io.CommandInput;
import org.pispeb.treffpunkt.server.commands.io.CommandInputLoginRequired;
import org.pispeb.treffpunkt.server.commands.io.CommandOutput;
import org.pispeb.treffpunkt.server.commands.io.ErrorOutput;
import org.pispeb.treffpunkt.server.exceptions.DatabaseException;
import org.pispeb.treffpunkt.server.exceptions.DuplicateEmailException;
import org.pispeb.treffpunkt.server.hibernate.Account;
import org.pispeb.treffpunkt.server.networking.ErrorCode;

/**
 * a command to edit the email of the executing account
 */
public class EditEmailCommand extends AbstractCommand {


    public EditEmailCommand(SessionFactory sessionFactory,
                            ObjectMapper mapper) {
        super(sessionFactory,Input.class, mapper);
    }

    @Override
    protected CommandOutput executeInternal(CommandInput commandInput) throws
            DatabaseException {
        Input input = (Input) commandInput;
        Account actingAccount = input.getActingAccount();

        // check if password is correct
        if (!actingAccount.checkPassword(input.pass)) {
            return new ErrorOutput(ErrorCode.CREDWRONG);
        }

        // edit email
        // TODO: don't use exceptions for this
        try {
            actingAccount.setEmail(input.email, accountManager);
        } catch (DuplicateEmailException e) {
            return new ErrorOutput(ErrorCode.EMAILINVALID);
        }
        return new Output();
    }

    public static class Input extends CommandInputLoginRequired {

        final String pass;
        final String email;

        public Input(@JsonProperty("pass") String pass,
                     @JsonProperty("email") String email,
                     @JsonProperty("token") String token) {
            super(token);
            this.pass = pass;
            this.email = email;
        }

        @Override
        public boolean syntaxCheck() {
            return validateEmail(email)
                    && validatePassword(pass);
        }
    }

    public static class Output extends CommandOutput { }

}
