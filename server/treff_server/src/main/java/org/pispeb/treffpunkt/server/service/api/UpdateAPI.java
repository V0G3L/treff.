package org.pispeb.treffpunkt.server.service.api;

import org.pispeb.treffpunkt.server.service.auth.Secured;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@Path("/updates")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface UpdateAPI {

    /**
     * Retrieves all unseen updates.
     * @param context
     * @return List of string representations of all unseen updates
     */
    @GET
    @Path("/")
    @Secured
    List<String> getUpdates(@Context SecurityContext context);

}
