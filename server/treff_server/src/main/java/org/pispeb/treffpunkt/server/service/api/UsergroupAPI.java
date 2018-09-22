package org.pispeb.treffpunkt.server.service.api;

import org.pispeb.treffpunkt.server.service.auth.Secured;
import org.pispeb.treffpunkt.server.service.domain.Event;
import org.pispeb.treffpunkt.server.service.domain.Usergroup;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Secured
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public interface UsergroupAPI {

    // TODOL
    @GET
    @Path("/test")
    Usergroup getTest();

    @POST
    @Path("/test/{gid}")
    void test(@PathParam("gid") int gid, Usergroup ug);

    @POST
    @Path("/create")
    int create(Usergroup group);

    @POST
    @Path("/{gid}/edit")
    void edit(@PathParam("gid") int gid);

    @POST
    @Path("/{gid}/members")
    void addMembers(@PathParam("gid") int gid, int[] memberIds);

    @DELETE
    @Path("/{gid}/members")
    void removeMembers(@PathParam("gid") int gid, int[] memberIds);

    @POST
    @Path("/{gid}/leave")
    void leaveGroup(@PathParam("gid") int gid);

    @POST
    @Path("/{gid}/events")
    void addEvent(@PathParam("gid") int gid, Event event);
}
