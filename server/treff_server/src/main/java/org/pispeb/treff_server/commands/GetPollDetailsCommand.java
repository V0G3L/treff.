package org.pispeb.treff_server.commands;

import org.pispeb.treff_server.exceptions.DatabaseException;
import org.pispeb.treff_server.interfaces.AccountManager;
import org.pispeb.treff_server.networking.CommandResponse;

import javax.json.JsonObject;

//TODO needs to be tested
/**
 * a command to get a detailed description of a Poll of a Usergroup
 */
public class GetPollDetailsCommand extends AbstractCommand {

    private int id;
    private int groupId;

    public GetPollDetailsCommand(AccountManager accountManager) {
        super(accountManager);
    }

    /**
     * @param jsonObject the command encoded as a JsonObject
     * @return a detailed description of the Poll encoded as a JsonObject
     */
    public CommandResponse execute(JsonObject jsonObject) throws
            DatabaseException {
        return null; //TODO need a Map<Token, Account> or an account id
    }

    protected CommandResponse parseParameters(JsonObject jsonObject) {
        CommandResponse response = extractIntegerParameter(this.groupId,
                "group-id", jsonObject);
        if (response == null) {
            response = extractIntegerParameter(this.id, "id", jsonObject);
        }
        return response;
    }
}
