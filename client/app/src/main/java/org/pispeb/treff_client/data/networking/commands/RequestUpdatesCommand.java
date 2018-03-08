package org.pispeb.treff_client.data.networking.commands;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;


import org.pispeb.treff_client.data.networking.commands.updates.UpdateToSerialize;
import org.pispeb.treff_client.data.repositories.ChatRepository;
import org.pispeb.treff_client.data.repositories.EventRepository;
import org.pispeb.treff_client.data.repositories.UserGroupRepository;
import org.pispeb.treff_client.data.repositories.UserRepository;

import javax.json.JsonObject;
import javax.json.Json;

import java.io.IOException;
import java.io.StringReader;


/**
 * Request an array of all updates that accumulated on the server since the
 * last time this was requested
 */

public class RequestUpdatesCommand extends AbstractCommand {

    private Request output;
    private ChatRepository chatRepository;
    private EventRepository eventRepository;
    private UserGroupRepository userGroupRepository;
    private UserRepository userRepository;

    public RequestUpdatesCommand(String token,
                                 ChatRepository chatRepository,
                                 EventRepository eventRepository,
                                 UserGroupRepository userGroupRepository,
                                 UserRepository userRepository) {
        super(Response.class);
        output = new Request(token);
        this.chatRepository = chatRepository;
        this.eventRepository = eventRepository;
        this.userGroupRepository = userGroupRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Request getRequest() {
        return output;
    }


    @Override
    public void onResponse(AbstractResponse abstractResponse) {
        Response response = (Response) abstractResponse;

        for(String updateString : response.updates) {

            JsonObject update = Json
                    .createReader(new StringReader(updateString))
                    .readObject();

            String updateType = update.getString("type");

            Class<? extends UpdateToSerialize> updateClass
                    = UpdateToSerialize.getUpdateByStringIdentifier(updateType);

            if (updateClass == null) {
                //TODO skip run
            }

            final ObjectMapper mapper;
            mapper = new ObjectMapper();
            mapper.enable(DeserializationFeature
                    .FAIL_ON_MISSING_CREATOR_PROPERTIES);

            UpdateToSerialize actualUpdate = null;


            try {
                actualUpdate = mapper.readValue(updateString,
                        updateClass);
            } catch (IOException e) {
                //Should not happen
                e.printStackTrace();
            }
        }
    }

    public static class Request extends AbstractRequest {

        String token;

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
