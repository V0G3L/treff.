package org.pispeb.treffpunkt.client.data.networking.commands;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.pispeb.treffpunkt.client.R;
import org.pispeb.treffpunkt.client.view.util.TreffPunkt;

/**
 * Class representing a JSON edit-username object
 */

public class EditUsernameCommand extends AbstractCommand{

    private Request output;

    public EditUsernameCommand(String username, String token, String password) {
        super(Response.class);
        output = new Request(username, token, password);
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
        pref.edit().putString(ctx.getString(R.string.key_userName), output.username).apply();
    }

    public static class Request extends AbstractRequest {

        @JsonProperty("user")
        public final String username;
        @JsonProperty("pass")
        public final String password;
        @JsonProperty("token")
        public final String token;

        public Request(String username, String token, String password) {
            super(CmdDesc.EDIT_USERNAME.toString());
            this.username = username;
            this.password = password;
            this.token = token;
        }
    }

    //server returns empty json object
    public static class Response extends AbstractResponse { }
}
