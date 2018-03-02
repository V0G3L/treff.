package org.pispeb.treff_client.data.networking.commands;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class representing a JSON reset-password-confirm object
 */

public class ResetPasswordConfirmCommand extends AbstractCommand{

    private Request output;

    public ResetPasswordConfirmCommand(String code, String newPass) {
        super(Response.class);
        output = new Request(code, newPass);
    }

    @Override
    public Request getRequest() {
        return output;
    }

    @Override
    public void onResponse(AbstractResponse abstractResponse) {
        Response response = (Response) abstractResponse;
        // TODO handle response e.g. starting login screen
    }

    public static class Request extends AbstractRequest {

        public final String code;

        @JsonProperty("new-pass")
        public final String newPass;


        public Request(String code, String newPass) {
            super("reset-password-confirm");
            this.code = code;
            this.newPass = newPass;
        }
    }

    //server returns empty json object
    public static class Response extends AbstractResponse {
        public Response() {
        }
    }


}
