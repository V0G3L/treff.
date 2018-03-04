package org.pispeb.treff_client.data.networking.commands;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.ContextThemeWrapper;

import org.pispeb.treff_client.R;
import org.pispeb.treff_client.view.util.TreffPunkt;

/**
 * Class representing a JSON edit-email object
 */

public class EditEmailCommand extends AbstractCommand{

    private Request output;

    public EditEmailCommand(String email, String token) {
        super(Response.class);
        output = new Request(email, token);
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
        pref.edit().putString(ctx.getString(R.string.key_email), output.email).apply();
    }

    public static class Request extends AbstractRequest {

        public final String email;
        public final String token;

        public Request(String email, String token) {
            super("edit-email");
            this.email = email;
            this.token = token;
        }
    }

    //server returns empty json object
    public static class Response extends AbstractResponse {
        public Response() {
        }
    }
}