package org.pispeb.treffpunkt.client.data.networking.commands;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.pispeb.treffpunkt.client.data.entities.UserGroup;
import org.pispeb.treffpunkt.client.data.networking.RequestEncoder;
import org.pispeb.treffpunkt.client.data.networking.commands.descriptions.CompleteUsergroup;



import org.pispeb.treffpunkt.client.data.repositories.EventRepository;
import org.pispeb.treffpunkt.client.data.repositories.UserGroupRepository;

/**
 * Created by matth on 17.02.2018.
 */

public class GetGroupDetailsCommand extends AbstractCommand {

    private final Request output;
    private final UserGroupRepository userGroupRepository;
    private final EventRepository eventRepository;
    private final RequestEncoder encoder;

    public GetGroupDetailsCommand(int id, String token,
                                  UserGroupRepository userGroupRepository,
                                  EventRepository eventRepository,
                                  RequestEncoder encoder) {
        super(Response.class);
        this.eventRepository = eventRepository;
        this.encoder = encoder;
        output = new Request(id, token);
        this.userGroupRepository = userGroupRepository;
    }

    @Override
    public Request getRequest() {
        return output;
    }

    @Override
    public void onResponse(AbstractResponse abstractResponse) {
        Response response = (Response) abstractResponse;
        UserGroup newGroup = new UserGroup(output.id, response.usergroup.name);
        if (userGroupRepository.getGroup(output.id) == null) {
            userGroupRepository.addGroup(newGroup);
        } else {
            userGroupRepository.updateGroup(
                    new UserGroup(output.id, response.usergroup.name));
        }
        userGroupRepository.updateGroupMembers(output.id, response.usergroup
                .members);

        for (int eventId : response.usergroup.events) {
            if (eventRepository.getEvent(eventId) == null) {
                encoder.getEventDetails(eventId, output.id);
            }
        }
    }

    public static class Request extends AbstractRequest {

        public final int id;
        public final String token;

        public Request(int id, String token) {
            super(CmdDesc.GET_GROUP_DETAILS.toString());
            this.id = id;
            this.token = token;
        }
    }


    public static class Response extends AbstractResponse {

        public final CompleteUsergroup usergroup;

        public Response(@JsonProperty("group")
                        CompleteUsergroup usergroup) {
            this.usergroup = usergroup;
        }
    }
}
