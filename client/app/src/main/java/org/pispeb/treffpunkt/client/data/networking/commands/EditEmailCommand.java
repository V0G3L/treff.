package org.pispeb.treffpunkt.client.data.networking.commands;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.pispeb.treffpunkt.client.R;
import org.pispeb.treffpunkt.client.view.util.TreffPunkt;

/**
 * Class representing a JSON edit-email object
 */

public class EditEmailCommand extends AbstractCommand{

    private Request output;

    public EditEmailCommand(String email, String token, String password) {
        super(Response.class);
        output = new Request(email, token, password);
    }

    @Override
    public Request getRequest() {
        return output;
    }

    @Override
    public void onResponse(AbstractResponse abstractResponse) {
        Context ctx = TreffPunkt.getAppContext();
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        pref.edit().putString(ctx.getString(R.string.key_email), output.email).apply();
    }

    public static class Request extends AbstractRequest {

        public final String email;
        @JsonProperty("pass")
        public final String password;
        public final String token;

        public Request(String email, String token, String password) {
            super(CmdDesc.EDIT_EMAIL.toString());
            this.email = email;
            this.token = token;
            this.password = password;
        }
    }

    //server returns empty json object
    public static class Response extends AbstractResponse {
        public Response() {
        }
    }
}