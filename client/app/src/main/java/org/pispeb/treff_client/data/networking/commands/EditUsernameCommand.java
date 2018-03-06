package org.pispeb.treff_client.data.networking.commands;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.pispeb.treff_client.R;
import org.pispeb.treff_client.view.util.TreffPunkt;

/**
 * Class representing a JSON edit-username object
 */

public class EditUsernameCommand extends AbstractCommand{

    private Request output;

    public EditUsernameCommand(String username, String token) {
        super(Response.class);
        output = new Request(username, token);
    }

    @Override
    public Request getRequest() {
        return output;
    }

    @Override
    public void onResponse(AbstractResponse abstractResponse) {
        Response response = (Response) abstractResponse;
        Context ctx = TreffPunkt.getAppContext();
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        pref.edit().putString(ctx.getString(R.string.key_userName), output.username).apply();
    }

    public static class Request extends AbstractRequest {

        public final String username;
        public final String token;

        public Request(String username, String token) {
            super(CmdDesc.EDIT_USERNAME.toString());
            this.username = username;
            this.token = token;
        }
    }

    //server returns empty json object
    public static class Response extends AbstractResponse {
        public Response() {
        }
    }
}
