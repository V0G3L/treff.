package org.pispeb.treffpunkt.server.service.api;

import org.pispeb.treffpunkt.server.service.auth.Secured;
import org.pispeb.treffpunkt.server.service.domain.Event;
import org.pispeb.treffpunkt.server.service.domain.Usergroup;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@Path("/groups")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface UsergroupAPI {

    /**
     * Lists all groups of which own account is a member.
     * @param context
     * @return List of group IDs.
     */
    @GET
    @Path("/")
    @Secured
    List<Integer> list(@Context SecurityContext context);

    /**
     * Creates a new group.
     * @param group Group details. Group ID and event IDs supplied by the client are ignored.
     * @param context
     * @return ID of new group
     */
    @POST
    @Path("/")
    @Secured
    int create(Usergroup group, @Context SecurityContext context);

    /**
     * Shows group details.
     * @param gid Group ID
     * @param context
     * @return Group details
     */
    @GET
    @Path("/{gid}/")
    @Secured
    Usergroup getDetails(@PathParam("gid") int gid, @Context SecurityContext context);

    /**
     * Updates the group's values.
     * @param gid ID of the group that is to be edited
     * @param usergroup New values. Currently all values but the name are ignored.
     * @param context Security context containing login token
     */
    @PUT
    @Path("/{gid}/")
    @Secured
    void edit(@PathParam("gid") int gid, Usergroup usergroup, @Context SecurityContext context);

    /**
     * Adds new members to a group.
     * @param gid Group ID
     * @param memberIds Account IDs of new members
     * @param context
     */
    @POST
    @Path("/{gid}/members")
    @Secured
    void addMembers(@PathParam("gid") int gid, List<Integer> memberIds, @Context SecurityContext context);

    /**
     * Removes members from a group.
     * @param gid Group ID
     * @param memberIds Account IDs of members that are to be removed
     * @param context
     */
    @DELETE
    @Path("/{gid}/members")
    @Secured
    void removeMembers(@PathParam("gid") int gid, List<Integer> memberIds,
                       @Context SecurityContext context);

    /**
     * Leaves a group.
     * @param gid Group ID
     * @param context
     */
    @POST
    @Path("/{gid}/leave")
    @Secured
    void leaveGroup(@PathParam("gid") int gid, @Context SecurityContext context);

    /**
     * Adds a new event to a group.
     * @param gid Group ID
     * @param event Event details. Participant list set by the client is ignored.
     * @param context
     * @return ID of new event
     */
    @POST
    @Path("/{gid}/events")
    @Secured
    int addEvent(@PathParam("gid") int gid, Event event, @Context SecurityContext context);

    /**
     * Removes an event from a group.
     * @param gid Group ID
     * @param eid Event ID
     * @param context
     */
    @DELETE
    @Path("/{gid}/events/{eid}")
    @Secured
    void removeEvent(@PathParam("gid") int gid, @PathParam("eid") int eid,
                  @Context SecurityContext context);

    /**
     * Sends a chat message to a group.
     * @param gid Group ID
     * @param chatMessage Chat message
     * @param context
     */
    @POST
    @Path("/{gid}/chat")
    @Secured
    void sendChatMessage(@PathParam("gid") int gid, String chatMessage,
                     @Context SecurityContext context);

    /**
     * Sends a position request to a group.
     * @param gid Group ID
     * @param untilTimestamp Time until which the positions are requested
     * @param context
     */
    @POST
    @Path("/{gid}/positionrequest")
    @Secured
    void sendPositionRequest(@PathParam("gid") int gid, long untilTimestamp,
                             @Context SecurityContext context);

    /**
     * Share own position with a group.
     * @param gid Group ID
     * @param untilTimestamp Time until which the position is shared
     * @param context
     */
    @PUT
    @Path("/{gid}/positionsharing")
    @Secured
    void sharePosition(@PathParam("gid") int gid, long untilTimestamp,
                       @Context SecurityContext context);

    /**
     * Show event details.
     * @param gid Group ID
     * @param eid Event ID
     * @param context
     * @return Event details
     */
    @GET
    @Path("/{gid}/events/{eid}/")
    @Secured
    Event getEventDetails(@PathParam("gid") int gid, @PathParam("eid") int eid,
                         @Context SecurityContext context);

    /**
     * Updates an event's values.
     * @param gid Group ID
     * @param eid Event ID
     * @param event New values. ID and participant list are ignored.
     * @param context Security context containing login token
     */
    @PUT
    @Path("/{gid}/events/{eid}/")
    @Secured
    void editEvent(@PathParam("gid") int gid, @PathParam("eid") int eid,
                   Event event, @Context SecurityContext context);

    /**
     * Joins an event
     * @param gid Group ID
     * @param eid Event ID
     * @param context
     */
    @PUT
    @Path("/{gid}/events/{eid}/participation")
    @Secured
    void joinEvent(@PathParam("gid") int gid, @PathParam("eid") int eid,
                   @Context SecurityContext context);

    /**
     * Leaves an event
     * @param gid Group ID
     * @param eid Event ID
     * @param context
     */
    @DELETE
    @Path("/{gid}/events/{eid}/participation")
    @Secured
    void leaveEvent(@PathParam("gid") int gid, @PathParam("eid") int eid,
                    @Context SecurityContext context);
}
