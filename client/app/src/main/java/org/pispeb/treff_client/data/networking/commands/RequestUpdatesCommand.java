package org.pispeb.treff_client.data.networking.commands;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;


import org.pispeb.treff_client.data.networking.commands.updates.Update;
import org.pispeb.treff_client.data.repositories.ChatRepository;
import org.pispeb.treff_client.data.repositories.EventRepository;
import org.pispeb.treff_client.data.repositories.RepositorySet;
import org.pispeb.treff_client.data.repositories.UserGroupRepository;
import org.pispeb.treff_client.data.repositories.UserRepository;

import java.io.IOException;

import javax.json.stream.JsonParsingException;

/**
 * Request an array of all updates that accumulated on the server since the
 * last time this was requested
 */

public class RequestUpdatesCommand extends AbstractCommand {

    private final Request output;
    private final RepositorySet repositorySet;

    public RequestUpdatesCommand(String token, RepositorySet repositorySet) {
        super(Response.class);
        output = new Request(token);
        this.repositorySet = repositorySet;
    }

    @Override
    public Request getRequest() {
        return output;
    }

    @Override
    public void onResponse(AbstractResponse abstractResponse) {
        Response response = (Response) abstractResponse;

        for (String updateString : response.updates) {
            // TODO: re-use a single mapper across entire main (non-test) code
            // this guarantees that the correct settings are active and allows
            // for serializer caching
            final ObjectMapper mapper;
            mapper = new ObjectMapper();
            mapper.enable(DeserializationFeature
                    .FAIL_ON_MISSING_CREATOR_PROPERTIES);

            // translate to Update object
            Update update;
            try {
                update = Update.parseUpdate(updateString, mapper);
            } catch (IOException | JsonParsingException e) {
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
