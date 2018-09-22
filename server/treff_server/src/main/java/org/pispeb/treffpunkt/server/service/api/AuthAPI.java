package org.pispeb.treffpunkt.server.service.api;

import org.pispeb.treffpunkt.server.service.domain.Credentials;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public interface AuthAPI {

    @POST
    @Path("/register")
    String register(Credentials creds);

    @POST
    @Path("/login")
    String login(Credentials creds);
}
