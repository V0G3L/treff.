package org.pispeb.treff_client.data.networking.commands;

import android.content.Context;
import android.content.Intent;

import org.pispeb.treff_client.view.login.LoginActivity;
import org.pispeb.treff_client.view.util.TreffPunkt;

/**
 * Class representing a JSON delete-account object
 */

public class DeleteAccountCommand extends AbstractCommand{

    private Request output;

    public DeleteAccountCommand(String pass, String token) {
        super(Response.class);
        output = new Request(pass, token);
    }

    @Override
    public Request getRequest() {
        return output;
    }

    @Override
    public void onResponse(AbstractResponse abstractResponse) {
        Response response = (Response) abstractResponse;
        Context appctx = TreffPunkt.getAppContext();
        Intent restartApp = new Intent(appctx, LoginActivity.class);
        restartApp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        appctx.startActivity(restartApp);
    }

    public static class Request extends AbstractRequest {

        public final String pass;
        public final String token;

        public Request(String pass, String token) {
            super(CmdDesc.DELETE_ACCOUNT.toString());
            this.pass = pass;
            this.token = token;
        }
    }

    //server returns empty json object
    public static class Response extends AbstractResponse {
        public Response() {
        }
    }
}
