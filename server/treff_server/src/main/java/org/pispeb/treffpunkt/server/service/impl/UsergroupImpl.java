package org.pispeb.treffpunkt.server.service.impl;

import org.pispeb.treffpunkt.server.commands.AddGroupMembersCommand;
import org.pispeb.treffpunkt.server.commands.CreateEventCommand;
import org.pispeb.treffpunkt.server.commands.CreateGroupCommand;
import org.pispeb.treffpunkt.server.commands.EditEventCommand;
import org.pispeb.treffpunkt.server.commands.EditGroupCommand;
import org.pispeb.treffpunkt.server.commands.GetEventDetailsCommand;
import org.pispeb.treffpunkt.server.commands.GetGroupDetailsCommand;
import org.pispeb.treffpunkt.server.commands.JoinEventCommand;
import org.pispeb.treffpunkt.server.commands.LeaveEventCommand;
import org.pispeb.treffpunkt.server.commands.LeaveGroupCommand;
import org.pispeb.treffpunkt.server.commands.ListGroupsCommand;
import org.pispeb.treffpunkt.server.commands.PublishPositionCommand;
import org.pispeb.treffpunkt.server.commands.RemoveEventCommand;
import org.pispeb.treffpunkt.server.commands.RemoveGroupMembersCommand;
import org.pispeb.treffpunkt.server.commands.RequestPositionCommand;
import org.pispeb.treffpunkt.server.commands.SendChatMessageCommand;
import org.pispeb.treffpunkt.server.service.api.UsergroupAPI;
import org.pispeb.treffpunkt.server.service.domain.Event;
import org.pispeb.treffpunkt.server.service.domain.Usergroup;

import javax.jws.WebService;
import javax.ws.rs.Path;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@WebService(endpointInterface = "org.pispeb.treffpunkt.server.service.api.UsergroupAPI")
@Path("/groups")
public class UsergroupImpl extends ServiceImpl implements UsergroupAPI {

    @Override
    public List<Integer> list(SecurityContext context) {
        return new ListGroupsCommand(sessionFactory)
               .execute(new ListGroupsCommand.Input(token(context))).groupIDs;
    }

    @Override
    public int create(Usergroup group, SecurityContext context) {
        return new CreateGroupCommand(sessionFactory)
                .execute(new CreateGroupCommand.Input(group, token(context))).groupId;
    }

    @Override
    public Usergroup getDetails(int gid, SecurityContext context) {
        return new GetGroupDetailsCommand(sessionFactory)
                .execute(new GetGroupDetailsCommand.Input(gid, token(context))).usergroup;
    }

    @Override
    public void edit(int gid, Usergroup usergroup, SecurityContext context) {
        // Ignore ID from usergroup description and replace it with ID from URL
        usergroup.setId(gid);
        new EditGroupCommand(sessionFactory)
                .execute(new EditGroupCommand.Input(usergroup, token(context)));
    }

    @Override
    public void addMembers(int gid, int[] memberIds, SecurityContext context) {
        new AddGroupMembersCommand(sessionFactory)
                .execute(new AddGroupMembersCommand.Input(gid, memberIds, token(context)));
    }

    @Override
    public void removeMembers(int gid, int[] memberIds, SecurityContext context) {
        new RemoveGroupMembersCommand(sessionFactory)
                .execute(new RemoveGroupMembersCommand.Input(gid, memberIds, token(context)));
    }

    @Override
    public void leaveGroup(int gid, SecurityContext context) {
        new LeaveGroupCommand(sessionFactory)
                .execute(new LeaveGroupCommand.Input(gid, token(context)));
    }

    @Override
    public int addEvent(int gid, Event event, SecurityContext context) {
        return new CreateEventCommand(sessionFactory)
                .execute(new CreateEventCommand.Input(gid, event, token(context))).eventId;
    }

    @Override
    public void removeEvent(int gid, int eid, SecurityContext context) {
        new RemoveEventCommand(sessionFactory)
                .execute(new RemoveEventCommand.Input(eid, gid, token(context)));
    }

    @Override
    public void sendChatMessage(int gid, String chatMessage, SecurityContext context) {
        new SendChatMessageCommand(sessionFactory)
                .execute(new SendChatMessageCommand.Input(gid, chatMessage, token(context)));
    }

    @Override
    public void sendPositionRequest(int gid, long untilTimestamp, SecurityContext context) {
        new RequestPositionCommand(sessionFactory)
                .execute(new RequestPositionCommand.Input(gid, untilTimestamp, token(context)));
    }

    @Override
    public void sharePosition(int gid, long untilTimestamp, SecurityContext context) {
        new PublishPositionCommand(sessionFactory)
                .execute(new PublishPositionCommand.Input(gid, untilTimestamp, token(context)));
    }

    @Override
    public Event getEventDetails(int gid, int eid, SecurityContext context) {
        return new GetEventDetailsCommand(sessionFactory)
                .execute(new GetEventDetailsCommand.Input(eid, gid, token(context))).event;
    }

    @Override
    public void editEvent(int gid, int eid, Event event, SecurityContext context) {
        // Ignore ID from event description and replace it with ID from URL
        event.setId(eid);
        new EditEventCommand(sessionFactory)
                .execute(new EditEventCommand.Input(gid, event, token(context)));
    }

    @Override
    public void joinEvent(int gid, int eid, SecurityContext context) {
        new JoinEventCommand(sessionFactory)
                .execute(new JoinEventCommand.Input(gid, eid, token(context)));
    }

    @Override
    public void leaveEvent(int gid, int eid, SecurityContext context) {
        new LeaveEventCommand(sessionFactory)
                .execute(new LeaveEventCommand.Input(gid, eid, token(context)));
    }
}
