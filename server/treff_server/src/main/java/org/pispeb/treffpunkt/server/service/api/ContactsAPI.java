package org.pispeb.treffpunkt.server.service.api;

import org.pispeb.treffpunkt.server.service.auth.Secured;
import org.pispeb.treffpunkt.server.service.domain.ContactList;

import javax.jws.WebService;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

@WebService
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public interface ContactsAPI {

    @GET
    @Path("/")
    @Secured
    ContactList getContactList(@Context SecurityContext context);

    @POST
    @Path("/{aid}")
    @Secured
    void addContact(@PathParam("aid") int aid, @Context SecurityContext context);

    @DELETE
    @Path("/{aid}")
    @Secured
    void removeContact(@PathParam("aid") int aid, @Context SecurityContext context);

    @POST
    @Path("/{aid}/accept")
    @Secured
    void acceptRequest(@PathParam("aid") int aid, @Context SecurityContext context);

    @DELETE
    @Path("/{aid}/deny")
    @Secured
    void denyRequest(@PathParam("aid") int aid, @Context SecurityContext context);

    @DELETE
    @Path("/{aid}/cancel")
    @Secured
    void cancelRequest(@PathParam("aid") int aid, @Context SecurityContext context);

    @POST
    @Path("/{aid}/block")
    @Secured
    void block(@PathParam("aid") int aid, @Context SecurityContext context);

    @DELETE
    @Path("/{aid}/block")
    @Secured
    void unblock(@PathParam("aid") int aid, @Context SecurityContext context);
}
