package org.pispeb.treffpunkt.server.service.impl;

import org.pispeb.treffpunkt.server.commands.LoginCommand;
import org.pispeb.treffpunkt.server.commands.RegisterCommand;
import org.pispeb.treffpunkt.server.service.api.AuthAPI;
import org.pispeb.treffpunkt.server.service.domain.AuthDetails;
import org.pispeb.treffpunkt.server.service.domain.Credentials;

import javax.jws.WebService;

@WebService(endpointInterface = "org.pispeb.treffpunkt.server.service.api.AuthAPI")
public class AuthImpl extends ServiceImpl implements AuthAPI {

    @Override
    public AuthDetails register(Credentials creds) {
        RegisterCommand.Input input
                = new RegisterCommand.Input(creds.getUsername(), creds.getPassword());
        RegisterCommand.Output output = new RegisterCommand(sessionFactory).execute(input);
        return new AuthDetails(output.token, output.id);
    }

    @Override
    public AuthDetails login(Credentials creds) {
        LoginCommand.Input input = new LoginCommand.Input(creds.getUsername(), creds.getPassword());
        LoginCommand.Output output = new LoginCommand(sessionFactory).execute(input);
        return new AuthDetails(output.token, output.id);

    }

}
