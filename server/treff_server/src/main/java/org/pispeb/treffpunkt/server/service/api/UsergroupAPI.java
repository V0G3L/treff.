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

@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public interface UsergroupAPI {

    @GET
    @Path("/")
    @Secured
    List<Integer> list(@Context SecurityContext context);

    @POST
    @Path("/")
    @Secured
    int create(Usergroup group, @Context SecurityContext context);

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

    @POST
    @Path("/{gid}/members")
    @Secured
    void addMembers(@PathParam("gid") int gid, int[] memberIds, @Context SecurityContext context);

    @DELETE
    @Path("/{gid}/members")
    @Secured
    void removeMembers(@PathParam("gid") int gid, int[] memberIds,
                       @Context SecurityContext context);

    @POST
    @Path("/{gid}/leave")
    @Secured
    void leaveGroup(@PathParam("gid") int gid, @Context SecurityContext context);

    @POST
    @Path("/{gid}/events")
    @Secured
    int addEvent(@PathParam("gid") int gid, Event event, @Context SecurityContext context);

    @DELETE
    @Path("/{gid}/events/{eid}")
    @Secured
    void removeEvent(@PathParam("gid") int gid, @PathParam("eid") int eid,
                  @Context SecurityContext context);

    @POST
    @Path("/{gid}/chat")
    @Secured
    void sendChatMessage(@PathParam("gid") int gid, String chatMessage,
                     @Context SecurityContext context);

    @POST
    @Path("/{gid}/positionrequest")
    @Secured
    void sendPositionRequest(@PathParam("gid") int gid, long untilTimestamp,
                             @Context SecurityContext context);

    @PUT
    @Path("/{gid}/positionsharing")
    @Secured
    void sharePosition(@PathParam("gid") int gid, long untilTimestamp,
                       @Context SecurityContext context);

    @GET
    @Path("/{gid}/events/{eid}/")
    @Secured
    Event getEventDetails(@PathParam("gid") int gid, @PathParam("eid") int eid,
                         @Context SecurityContext context);

    @PUT
    @Path("/{gid}/events/{eid}/")
    @Secured
    void editEvent(@PathParam("gid") int gid, @PathParam("eid") int eid,
                   Event event, @Context SecurityContext context);

    @POST
    @Path("/{gid}/events/{eid}/participation")
    @Secured
    void joinEvent(@PathParam("gid") int gid, @PathParam("eid") int eid,
                   @Context SecurityContext context);

    @DELETE
    @Path("/{gid}/events/{eid}/participation")
    @Secured
    void leaveEvent(@PathParam("gid") int gid, @PathParam("eid") int eid,
                    @Context SecurityContext context);
}
