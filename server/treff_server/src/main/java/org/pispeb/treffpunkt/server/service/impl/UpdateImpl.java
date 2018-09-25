package org.pispeb.treffpunkt.server.service.impl;

import org.pispeb.treffpunkt.server.commands.RequestUpdatesCommand;
import org.pispeb.treffpunkt.server.service.api.UpdateAPI;

import javax.jws.WebService;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@WebService(endpointInterface = "org.pispeb.treffpunkt.server.service.api.UpdateAPI")
public class UpdateImpl extends ServiceImpl implements UpdateAPI {

    @Override
    public List<String> getUpdates(SecurityContext context) {
        return new RequestUpdatesCommand(sessionFactory)
                .execute(new RequestUpdatesCommand.Input(token(context))).updates;
    }
}
