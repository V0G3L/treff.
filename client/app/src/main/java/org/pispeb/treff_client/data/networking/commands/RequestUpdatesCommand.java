package org.pispeb.treff_client.data.networking.commands;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.pispeb.treff_client.data.networking.commands.updates.Update;
import org.pispeb.treff_client.data.repositories.RepositorySet;

import java.io.IOException;

/**
 * Request an array of all updates that accumulated on the server since the
 * last time this was requested
 */

public class RequestUpdatesCommand extends AbstractCommand {

    private final Request output;
    private final RepositorySet repositorySet;
    private final ObjectMapper mapper;

    public RequestUpdatesCommand(String token, RepositorySet repositorySet, ObjectMapper mapper) {
        super(Response.class);
        output = new Request(token);
        this.repositorySet = repositorySet;
        this.mapper = mapper;
    }

    @Override
    public Request getRequest() {
        return output;
    }

    @Override
    public void onResponse(AbstractResponse abstractResponse) {
        Response response = (Response) abstractResponse;

        for (String updateString : response.updates) {
            // translate to Update object
            Update update;
            try {
                update = Update.parseUpdate(updateString, mapper);
            } catch (IOException | JSONException e) {
                Log.e("Updates",
                        String.format("Could not parse update:\n%s",
                                updateString));
                // will most likely lead to client being out-of-sync
                // when trying to apply other updates
                continue;
            }

            update.applyUpdate(repositorySet);
        }
    }

    public static class Request extends AbstractRequest {

        @JsonProperty("token")
        public final String token;

        public Request(String token) {
            super(CmdDesc.REQUEST_UPDATES.toString());
            this.token = token;
        }
    }

    public static class Response extends AbstractResponse {
        public String[] updates;

        public Response(@JsonProperty("updates") String[] updates) {
            this.updates = updates;
        }
    }


}
