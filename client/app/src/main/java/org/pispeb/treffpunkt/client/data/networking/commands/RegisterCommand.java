package org.pispeb.treffpunkt.client.data.networking.commands;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.pispeb.treffpunkt.client.R;
import org.pispeb.treffpunkt.client.data.networking.RequestEncoder;
import org.pispeb.treffpunkt.client.view.util.TreffPunkt;
import org.pispeb.treffpunkt.client.view.util.ViewModelFactory;

/**
 * Class representing a JSON register object
 */

public class RegisterCommand extends AbstractCommand{

    private String email;
    private String pass;
    private Request output;

    public RegisterCommand(String user, String pass, String email) {
        super(Response.class);
        this.email = email;
        this.pass = pass;
        output = new Request(user, pass);
    }

    @Override
    public Request getRequest() {
        return output;
    }

    @Override
    public void onResponse(AbstractResponse abstractResponse) {
    }

    public static class Request extends AbstractRequest {

        public final String user;
        public final String pass;

        public Request(String user, String pass) {
            super(CmdDesc.REGISTER.toString());
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
