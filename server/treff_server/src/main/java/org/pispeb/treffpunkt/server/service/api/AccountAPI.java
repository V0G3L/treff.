package org.pispeb.treffpunkt.server.service.api;

import org.pispeb.treffpunkt.server.service.auth.Secured;
import org.pispeb.treffpunkt.server.service.domain.Account;
import org.pispeb.treffpunkt.server.service.domain.Position;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

@Path("/accounts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface AccountAPI {

    /**
     * Shows account details.
     * @param aid Account ID
     * @param context
     * @return Account details
     */
    @GET
    @Path("/{aid}/details")
    @Secured
    Account getDetails(@PathParam("aid") int aid, @Context SecurityContext context);

    /**
     * Translates a username into an ID.
     * @param name The unique account username
     * @param context
     * @return The account ID corresponding to the username
     */
    @GET
    @Path("/id-of/{name}")
    @Secured
    int getID(@PathParam("name") String name, @Context SecurityContext context);

    /**
     * Changes own username.
     * @param username New username
     * @param password Current password
     * @param context
     */
    @PUT
    @Path("/username")
    @Secured
    void setUsername(String username, String password, @Context SecurityContext context);

    /**
     * Changes own e-mail address.
     * @param email New e-mail address
     * @param password Current password
     * @param context
     */
    @PUT
    @Path("/email")
    @Secured
    void setEmail(String email, String password, @Context SecurityContext context);

    /**
     * Changes own password
     * @param oldPassword Current password
     * @param newPassword New password
     * @param context
     */
    @PUT
    @Path("/password")
    @Secured
    void setPassword(String oldPassword, String newPassword, @Context SecurityContext context);

    /**
     * Updates own position
     * @param position New position
     * @param context
     */
    @PUT
    @Path("/position")
    @Secured
    void updatePosition(Position position, @Context SecurityContext context);

}
