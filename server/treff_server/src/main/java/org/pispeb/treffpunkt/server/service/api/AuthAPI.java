package org.pispeb.treffpunkt.server.service.api;

import org.pispeb.treffpunkt.server.service.domain.AuthDetails;
import org.pispeb.treffpunkt.server.service.domain.Credentials;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface AuthAPI {

    /**
     * Registers a new account.
     * @param creds Credentials of the new account
     * @return Authentication token and ID of new account
     */
    @POST
    @Path("/register")
    AuthDetails register(Credentials creds);

    /**
     * Logs into an existing account.
     * @param creds Credentials of the account
     * @return New authentication token and ID
     */
    @POST
    @Path("/login")
    AuthDetails login(Credentials creds);
}
