package org.pispeb.treff_client.data.networking.commands;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.pispeb.treff_client.R;
import org.pispeb.treff_client.view.util.TreffPunkt;

/**
 * Class representing a JSON register object
 */

public class RegisterCommand extends AbstractCommand{

    private Request output;

    public RegisterCommand(String user, String pass) {
        super(Response.class);
        output = new Request(user, pass);
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
        pref.edit()
                .putString(ctx.getString(R.string.key_token), response.token)
                .putInt(ctx.getString(R.string.key_userId), response.id)
                .commit();
    }

    public static class Request extends AbstractRequest {

        public final String user;
        public final String pass;

        public Request(String user, String pass) {
            super("register");
            this.user = user;
            this.pass = pass;
        }
    }


    public static class Response extends AbstractResponse {

        public final String token;
        public final int id;

        public Response(@JsonProperty("token") String token,
                        @JsonProperty("id")int id) {
            this.token = token;
            this.id = id;
        }
    }

}
