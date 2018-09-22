package org.pispeb.treffpunkt.server.service.api;

import org.pispeb.treffpunkt.server.service.auth.Secured;
import org.pispeb.treffpunkt.server.service.domain.Account;

import javax.jws.WebService;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Secured
@WebService
public interface AccountAPI {

    @GET
    @Path("/{aid}/details")
    Account getDetails(@PathParam("aid") int aid);

    @GET
    @Path("/id-of/{name}")
    int getID(@PathParam("name") String name);

    @POST
    @Path("/username")
    void setUsername(String username, String password);

    @POST
    @Path("/email")
    void setEmail(String email, String password);

    @POST
    @Path("/password")
    void setPassword(String oldPassword, String newPassword);


}
