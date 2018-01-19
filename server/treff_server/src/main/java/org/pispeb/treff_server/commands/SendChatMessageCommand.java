package org.pispeb.treff_server.commands;

import javax.json.JsonObject;

/**
 * a command to send a chat message to a Usergroup
 */
public class SendChatMessageCommand extends AbstractCommand {

    public SendChatMessageCommand(JsonObject jsonObject) {
        super(jsonObject);
    }

    /**
     * @return an empty JsonObject
     */
    public JsonObject execute() {
        return null; //TODO
    }

    protected void parseParameters(JsonObject jsonObject) {
        //TODO
    }
}
