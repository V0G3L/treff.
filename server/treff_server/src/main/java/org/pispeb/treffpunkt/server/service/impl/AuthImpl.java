package org.pispeb.treffpunkt.server.service.impl;

import org.pispeb.treffpunkt.server.service.api.AuthAPI;
import org.pispeb.treffpunkt.server.service.domain.Credentials;

import javax.jws.WebService;
import javax.ws.rs.Path;

@WebService(endpointInterface = "org.pispeb.treffpunkt.server.service.api.AuthAPI")
@Path("/auth")
public class AuthImpl extends ServiceImpl implements AuthAPI {

    @Override
    public String register(Credentials creds) {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public String login(Credentials creds) {

    }

}
