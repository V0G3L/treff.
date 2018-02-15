package org.pispeb.treff_client.data.networking.commands;

/**
 * Class representing a  JSON get-user-id object
 */

public class GetUserIdCommand {

    public final String cmd = "get-user-id";
    public String user;
    public String token;

    public GetUserIdCommand (String user, String token) {
        this.user = user;
        this.token = token;
    }
}
