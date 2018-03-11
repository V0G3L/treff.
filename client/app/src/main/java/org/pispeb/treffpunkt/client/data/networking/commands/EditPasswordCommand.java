package org.pispeb.treffpunkt.client.data.networking.commands;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class representing a JSON edit-password object
 */

public class EditPasswordCommand extends AbstractCommand{

    private Request output;

    public EditPasswordCommand(String pass, String newPass, String token) {
        super(Response.class);
        output = new Request(pass, newPass, token);
    }

    @Override
    public Request getRequest() {
        return output;
    }

    @Override
    public void onResponse(AbstractResponse abstractResponse) {
        Response response = (Response) abstractResponse;
        // TODO handle response
    }

    public static class Request extends AbstractRequest {

        public final String pass;

        @JsonProperty("new-pass")
        public final String newPass;

        public final String token;

        public Request(String pass, String newPass, String token) {
            super(CmdDesc.EDIT_PASSWORD.toString());
            this.pass = pass;
            this.newPass = newPass;
            this.token = token;
        }
    }

    //server returns empty json object
    public static class Response extends AbstractResponse {
        public Response() {
        }
    }
}
