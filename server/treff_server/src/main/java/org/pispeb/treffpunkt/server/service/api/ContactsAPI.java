package org.pispeb.treffpunkt.server.service.api;

import org.pispeb.treffpunkt.server.service.auth.Secured;
import org.pispeb.treffpunkt.server.service.domain.ContactList;

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

@Path("/contacts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface ContactsAPI {

    /**
     * Lists all contacts, all incoming and outgoing contact requests, and all blocks.
     * @param context
     * @return See ContactList
     */
    @GET
    @Path("/")
    @Secured
    ContactList getContactList(@Context SecurityContext context);

    /**
     * Sends a contact request to another user.
     * @param aid Account ID of recipient
     * @param context
     */
    @POST
    @Path("/{aid}")
    @Secured
    void sendRequest(@PathParam("aid") int aid, @Context SecurityContext context);

    /**
     * Removes an account from the contact list.
     * @param aid Account ID of contact that is to be removed
     * @param context
     */
    @DELETE
    @Path("/{aid}")
    @Secured
    void removeContact(@PathParam("aid") int aid, @Context SecurityContext context);

    /**
     * Accepts an incoming contact request.
     * @param aid Account ID of sender
     * @param context
     */
    @POST
    @Path("/{aid}/accept")
    @Secured
    void acceptRequest(@PathParam("aid") int aid, @Context SecurityContext context);

    /**
     * Rejects an incoming contact request.
     * @param aid Account ID of sender
     * @param context
     */
    @DELETE
    @Path("/{aid}/deny")
    @Secured
    void rejectRequest(@PathParam("aid") int aid, @Context SecurityContext context);

    /**
     * Cancels an outgoing contact request.
     * @param aid Account ID of the recipient
     * @param context
     */
    @DELETE
    @Path("/{aid}/cancel")
    @Secured
    void cancelRequest(@PathParam("aid") int aid, @Context SecurityContext context);

    /**
     * Blocks another account and, if applicable, removes it as a contact.
     * @param aid Account ID of block recipient
     * @param context
     */
    @POST
    @Path("/{aid}/block")
    @Secured
    void block(@PathParam("aid") int aid, @Context SecurityContext context);

    /**
     * Unblocks another account.
     * @param aid Account ID of block recipient
     * @param context
     */
    @DELETE
    @Path("/{aid}/block")
    @Secured
    void unblock(@PathParam("aid") int aid, @Context SecurityContext context);
}
